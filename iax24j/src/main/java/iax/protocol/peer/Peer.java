package iax.protocol.peer;

import iax.audio.AudioFactory;
import iax.audio.impl.NullAudioFactory;
import iax.protocol.call.Call;
import iax.protocol.call.command.send.CallCommandSendFacade;
import iax.protocol.connection.Connection;
import iax.protocol.frame.Frame;
import iax.protocol.frame.FullFrame;
import iax.protocol.frame.MiniFrame;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.peer.command.send.PeerCommandSendFacade;
import iax.protocol.peer.state.PeerState;
import iax.protocol.peer.state.Registered;
import iax.protocol.peer.state.Unregistered;
import iax.protocol.peer.state.Waiting;
import iax.protocol.util.FrameUtil;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Peer tha controls phone calls.
 */
public class Peer {

    /**
     * Peer's source call number
     */
    public final static int PEER_SRCCALLNO = 1;
    /**
     * Peer's register refresh in seconds
     */
    public final static int REGISTER_REFRESH = 60;

    //Peer's retry refresh in seconds
    private final int RETRY_REFRESH = 1;
    //Peer's retry max
    private final int RETRY_MAXCOUNT = 5;

    //Registration user name.
    private String userName;
    //Registration password.
    private String password;
    //Remote host.
    private String host;
    //Connection to send data to host.
    private Connection connection;
    //Hashmap to store initiated calls.
    private Hashtable calls;
    //Mapping from destination number to source number.
    private Hashtable srcCallNoFromDestCallNo;
    //Mapping from called number to source number.
    private Hashtable srcCallNoFromCalledNumber;
    //HashMap with the frames that are waiting for an ack
    private Hashtable framesWaitingAck;
    //HashMap with the frames that are waiting for a reply
    private Hashtable framesWaitingReply;
    //Local identifier for a given call.
    private int newLocalSrcCallNo;
    //Peer listener.
    private PeerListener peerListener;
    //Peer's state
    private PeerState state;
    //Source timestamp for full frames from the first full frame sent by this call
    private long srcTimestamp;
    //Register timer task
    private Timer registerTimer;
    //Retry timer task to retry frames not commited whith a specific frame or an ack frame
    private Timer retryTimer;
    //Flag for determining if the peer is proceessing the exit
    private boolean processingExit;
    //Max calls
    private int maxCalls;
    
    private AudioFactory audioFactory;

    /**
     * Constructor. Initializes the peer with the given values.
     * @param peerListener Peer listener.
     * @param userName Registration user name.
     * @param password Registration password.
     * @param host Remote host.
     * @param register true if the peer is going to register, or false if not
     * @param maxCalls maximun number of concurrent calls
     */
    public Peer(PeerListener peerListener, String userName, String password, String host, boolean register, int maxCalls) {
    	this(peerListener, userName, password, host, register, maxCalls,new NullAudioFactory());
    }

    /**
     * Constructor. Initializes the peer with the given values.
     * @param peerListener Peer listener.
     * @param userName Registration user name.
     * @param password Registration password.
     * @param host Remote host.
     * @param register true if the peer is going to register, or false if not
     * @param maxCalls maximun number of concurrent calls
     * @param audioFactory audioFactory class
     */
    public Peer(PeerListener peerListener, String userName, String password, String host, boolean register, int maxCalls, AudioFactory audioFactory) {
        this.userName = userName;
        this.password = password;
        this.host = host;
        this.maxCalls = maxCalls;
        if (audioFactory == null){
        	this.audioFactory = new NullAudioFactory();
        }else{
            this.audioFactory = audioFactory;
        }
        this.newLocalSrcCallNo = 2;
        this.peerListener = peerListener;
        this.connection = new Connection();
        this.calls = new Hashtable();
        this.srcCallNoFromDestCallNo = new Hashtable();
        this.srcCallNoFromCalledNumber = new Hashtable();
        this.framesWaitingAck = new Hashtable();
        this.framesWaitingReply = new Hashtable();
        this.connection.start(this,host);
        this.state = Unregistered.getInstance();
        this.processingExit = false;
        this.retryTimer = new Timer();
        TimerTask retryTimerTask = new TimerTask() {
            public void run() {
                retryFramesWaiting();
            }
        };
        retryTimer.schedule(retryTimerTask, RETRY_REFRESH*1000, RETRY_REFRESH*1000);
        this.registerTimer = new Timer();
        if (register) {
            TimerTask registerTimerTask = new TimerTask() {
                public void run() {
                    register();
                }
            };
            registerTimer.schedule(registerTimerTask, 0, REGISTER_REFRESH*1000);
        }
    }

    /**
     * Gets peer user name.
     * @return String with user name.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Gets peer password.
     * @return String with password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets peer remote host.
     * @return String with remote host address.
     */
    public String getHost() {
        return host;
    }

    /**
     * Gets peer's state
     * @return peer's state
     */
    public PeerState getState() {
        return state;
    }


    /**
     * Gets the timestamp from the first full frame sent
     * @return the timestamp from the first full frame sent
     */
    public long getTimestamp() {
        long now = Calendar.getInstance().getTime().getTime();
        return now-srcTimestamp;
    }

    public boolean isProcessingExit() {
        return processingExit;
    }
    
    /**
     * Sets peer's state and notifies this state to the phone controller
     * @param state peer's state
     */
    public void setState(PeerState state) {
        this.state = state;
        if (state instanceof Registered)
            peerListener.registered();
        else if (state instanceof Waiting)
            peerListener.waiting();
        else if (state instanceof Unregistered)
            peerListener.unregistered();
    }
    

    /**
     * Ackes a full frame waiting for that removing it from the list of full frames waiting for ack
     * @param timeStamp the timestamp of the full frame acked
     */
    public synchronized void ackedFrame(long timeStamp) {
        framesWaitingAck.remove(new Long(timeStamp));
    }

    /**
     * Replied a full frame waiting for that removing it from the list of full frames waiting for reply
     * @param id the id of the full frame reply
     */
    public synchronized void repliedFrame(int id) {
        framesWaitingReply.remove(new Integer(id));
    }
    
    /**
     * Registers the peer
     */
    public void register() {
        srcTimestamp = Calendar.getInstance().getTime().getTime();
        PeerCommandSendFacade.regreq(this);
    }
    
    /**
     * Exits from the system and notifies the peer listener
     */
    public void exit() {
        processingExit = true;
        registerTimer.cancel();
        retryTimer.cancel();
        PeerCommandSendFacade.regrel(this);
        //Ends all calls
        Enumeration iterator = calls.elements();
        while (iterator.hasMoreElements()) {
            CallCommandSendFacade.hangup(((Call) iterator.nextElement()));
        }
        //Wait for responses
        try {
           Thread.sleep(2000);
        } catch (Exception e) {}
        // Close the connection
        connection.stop();
        //Notifies the peer listener
        peerListener.exited();
    }

    /**
     * Gets a local call from destination call number.
     * @param localDestCallNo Destination call number
     * @return The call.
     * @throws PeerException
     */
    public synchronized Call getCall(int localDestCallNo) throws PeerException {
    	Integer callNo = new Integer(localDestCallNo);
        if (srcCallNoFromDestCallNo.containsKey(callNo)) {
            Integer localSrcCallNo = (Integer)srcCallNoFromDestCallNo.get(callNo);
            return (Call)calls.get(localSrcCallNo);
        } else throw new PeerException("Not found any call referenced with the localDestCallNo: " + localDestCallNo);
    }

    /**
     * Gets a locall call from called number.
     * @param calledNumber Called number.
     * @return The call.
     * @throws PeerException
     */
    public synchronized Call getCall(String calledNumber) throws PeerException {
        if (srcCallNoFromCalledNumber.containsKey(calledNumber)) {
        	Integer localSrcCallNo = (Integer)srcCallNoFromCalledNumber.get(calledNumber);
            return (Call)calls.get(localSrcCallNo);
        } else throw new PeerException("Not found any call referenced with the calledNumber: " + calledNumber);
    }

    /**
     * Handles the reception of a frame from a call or a peer
     * 
     * @param buffer The frame data.
     */
    public synchronized void handleRecvFrame(byte buffer[]) {
        try {
            Frame frame = FrameUtil.deserialize(buffer);
            // Received a FullFrame
            if (frame != null){
                if (frame.getFull()) {
                    FullFrame fullFrame = (FullFrame)frame;
                    int destCallNo = fullFrame.getDestCallNo();
                    // FullFrame for a call
                    if ((destCallNo != 0) && (destCallNo != PEER_SRCCALLNO)) {
                        Call call = (Call)calls.get(new Integer(fullFrame.getDestCallNo()));
                        // Checks if there is any call that handles this FullFrame
                        if (call!=null)
                            call.handleRecvFrame(fullFrame);
                        else PeerCommandSendFacade.inval(this, frame);
                        // FullFrame for the peer
                    } else {
                        state.handleRecvFrame(this, frame); 
                    }
                    // Received a MiniFrame    
                } else {
                	Integer localSrcCallNo = (Integer)srcCallNoFromDestCallNo.get(new Integer(frame.getSrcCallNo()));
                    Call call = (Call)calls.get(localSrcCallNo);
                    // Checks if there is any call that handles this MiniFrame
                    if (call!=null)
                        call.handleRecvFrame((MiniFrame)frame);
                    else PeerCommandSendFacade.inval(this, frame);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the sending of a frame from a call.
     * @param frame The frame object.
     */
    public synchronized void handleSendFrame(Frame frame) {
        try {
            int srcCallNo = frame.getSrcCallNo();
            //Frame for a call
            if ((srcCallNo != 0) && (srcCallNo != PEER_SRCCALLNO)) {
                Call call = (Call)calls.get(new Integer(frame.getSrcCallNo()));
                // Checks if there is any call that handles this Frame
                if (call!=null)
                    call.handleSendFrame(frame);
                else PeerCommandSendFacade.inval(this, frame);
                // Frame for the peer
            } else {
                state.handleSendFrame(this, frame);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a frame directly. 
     * @param frame The frame object.
     */
    public void sendFrame(Frame frame) {
        try {
            connection.send(frame.serialize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a full frame that needs to be acked. For that added it to the list of frame waiting to be acked and after that
     * delegates in the method sendFrame() to send it
     * @param fullFrame the full frame to send
     */
    public synchronized void sendFullFrameAndWaitForAck(FullFrame fullFrame) {
        framesWaitingAck.put(new Long(fullFrame.getTimestamp()), fullFrame);
        sendFrame(fullFrame);
    }
    
    /**
     * Sends a full frame that needs to be replied. For that added it to the list of frame waiting to be replied and after that
     * delegates in the method sendFrame() to send it
     * @param fullFrame the full frame to send
     */
    public synchronized void sendFullFrameAndWaitForRep(FullFrame fullFrame) {
        framesWaitingReply.put(new Integer(fullFrame.getSubclass()), fullFrame);
        sendFrame(fullFrame);
    }

    /**
     * Notifies to the peer listener an answered
     * @param call the call
     */
    public void answeredCall(Call call) {
        peerListener.answered(call.getCalledNumber());
    }
    
    /**
     * Notifies to the peer listener for playing wait tones
     * @param call the call
     */
    public void ringingCall(Call call) {
        peerListener.playWaitTones(call.getCalledNumber());
    }
    
    
    /**
     * Maps the destination call number to the source call number and, if is a received call, notifies the
     * @param call The call to map.
     */
    public synchronized void bindCall(Call call) {
        srcCallNoFromDestCallNo.put(new Integer(call.getDestCallNo()), new Integer(call.getSrcCallNo()));
    }

    /**
     * Ends a call.
     * @param call The call to end.
     */
    public synchronized void endCall(Call call) {
        peerListener.hungup(call.getCalledNumber());
//      int localSrcCallNo = ((Integer)srcCallNoFromDestCallNo.get(call.getDestCallNo())).intValue();
        calls.remove(new Integer(call.getSrcCallNo()));
        srcCallNoFromDestCallNo.remove(new Integer(call.getDestCallNo()));
    }

    /**
     * Starts a new call.
     * @param calledNumber The number to call.
     * @return The new call.
     * @throws PeerException
     */
    public synchronized Call newCall(String calledNumber) throws PeerException {
        if (calls.size() <= maxCalls) {
            Call newCall = null;
            try {
                newCall = new Call(this, newLocalSrcCallNo,audioFactory);
                newCall.startCall(calledNumber);
            } catch (Exception e) {
                throw new PeerException(e);
            }
            calls.put(new Integer(newLocalSrcCallNo), newCall);
            srcCallNoFromCalledNumber.put(calledNumber, new Integer(newLocalSrcCallNo));
            newLocalSrcCallNo++;
            return newCall;
        } else throw new PeerException("Reached calls maximun");
    }


    /**
     * Starts a call received
     * @param recvCallFrame the new call frame received
     * @throws PeerException
     */
    public synchronized void recvCall(ProtocolControlFrame recvCallFrame) throws PeerException {
        if (calls.size() < maxCalls) {
            String callingName = "";
            String callingNumber = "";
            try {
                callingName = recvCallFrame.getCallingName();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                callingNumber = recvCallFrame.getCallingNumber();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Call newCall = null;
            try {
                newCall = new Call(this, newLocalSrcCallNo,audioFactory);
                newCall.startCall(callingNumber);
            } catch (Exception e) {
                throw new PeerException(e);
            }
            calls.put(new Integer(newLocalSrcCallNo), newCall);
            srcCallNoFromCalledNumber.put(callingNumber, new Integer(newLocalSrcCallNo));
            newLocalSrcCallNo++;
            newCall.handleRecvFrame(recvCallFrame);
            peerListener.recvCall(callingName, callingNumber);
        } else {
            PeerCommandSendFacade.ack(this, recvCallFrame);
            PeerCommandSendFacade.busy(this, recvCallFrame);
        }
    }

    // Method to retry frames that haven't been commited whith a specific frame or an ack frame
    private synchronized void retryFramesWaiting() {
        try {
            Enumeration iterator = framesWaitingAck.elements();
            while (iterator.hasMoreElements()) {
                FullFrame retryFullFrame = (FullFrame)iterator.nextElement();
                retryFullFrame.incRetryCount();
                if (retryFullFrame.getRetryCount() < RETRY_MAXCOUNT) {
                    sendFrame(retryFullFrame);
                } else throw new PeerException("Reached retries maximum in the peer for full frame of type " +
                        retryFullFrame.getFrameType() + ", subclass " + retryFullFrame.getSubclass());
            }
            iterator = framesWaitingReply.elements();
            while (iterator.hasMoreElements()) {
                FullFrame retryFullFrame = (FullFrame)iterator.nextElement();
                retryFullFrame.incRetryCount();
                if (retryFullFrame.getRetryCount() < RETRY_MAXCOUNT) {
                    sendFrame(retryFullFrame);
                } else throw new PeerException("Reached retries maximum in the peer for full frame of type " +
                        retryFullFrame.getFrameType() + ", subclass " + retryFullFrame.getSubclass());
            }
        } catch (PeerException e) {
            framesWaitingAck.clear();
            framesWaitingReply.clear();
            setState(Unregistered.getInstance());
            e.printStackTrace();
        }
    }
}