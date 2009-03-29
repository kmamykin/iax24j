package iax.protocol.peer.command.send;

import iax.protocol.frame.FullFrame;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.peer.Peer;

/**
 * Sends an ack
 */
public class Ack implements PeerCommandSend {

    private Peer peer;
    private FullFrame fullFrame;

    /**
     * Constructor
     * @param peer peer for sending the frame
     * @param fullFrame full frame that needs an ack
     */
    public Ack(Peer peer, FullFrame fullFrame) {
        this.peer = peer;
        this.fullFrame = fullFrame;
    }
    
    public void execute() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        //ack's timestamp == timestamp of the full frame that need this ack
        peer.handleSendFrame(new ProtocolControlFrame(Peer.PEER_SRCCALLNO, 
                false, 
                fullFrame.getSrcCallNo(), 
                fullFrame.getTimestamp(), 
                fullFrame.getIseqno(), 
                fullFrame.getOseqno()+1, 
                false, 
                ProtocolControlFrame.ACK_SC));
    }

}
