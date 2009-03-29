package iax.protocol.peer.command.send;

import iax.protocol.frame.ControlFrame;
import iax.protocol.frame.FullFrame;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.peer.Peer;

/**
 * Sends an accept and after them a busy
 */
public class Busy implements PeerCommandSend {

    private Peer peer;
    private FullFrame fullFrame;

    /**
     * Constructor
     * @param peer peer for sending the frame
     * @param fullFrame full frame that need a busy frame
     */
    public Busy(Peer peer, FullFrame fullFrame) {
        this.peer = peer;
        this.fullFrame = fullFrame;
    }
    
    public void execute() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        peer.handleSendFrame(new ProtocolControlFrame(Peer.PEER_SRCCALLNO, 
                false, 
                fullFrame.getSrcCallNo(), 
                peer.getTimestamp(), 
                fullFrame.getIseqno(), 
                fullFrame.getOseqno()+1, 
                false, 
                ProtocolControlFrame.ACCEPT_SC));
        peer.handleSendFrame(new ControlFrame(Peer.PEER_SRCCALLNO, 
                false, 
                fullFrame.getSrcCallNo(), 
                peer.getTimestamp(), 
                fullFrame.getIseqno()+1, 
                fullFrame.getOseqno()+2, 
                false, 
                ControlFrame.BUSY, new byte[0]));
    }

}
