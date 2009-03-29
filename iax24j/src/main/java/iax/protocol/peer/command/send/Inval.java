package iax.protocol.peer.command.send;

import iax.protocol.frame.Frame;
import iax.protocol.frame.FullFrame;
import iax.protocol.frame.MiniFrame;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.peer.Peer;

/**
 * Sends an inval
 */
public class Inval implements PeerCommandSend {

    private Peer peer;
    private Frame frame;

    /**
     * Constructor
     * @param peer peer for sending the frame
     * @param frame the frame that needs an inval
     */
    public Inval(Peer peer, Frame frame) {
        this.peer = peer;
        this.frame = frame;
    }
    
    public void execute() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        if (frame.getFull()) {
            FullFrame fullFrame = (FullFrame)frame;
            peer.sendFrame(new ProtocolControlFrame(fullFrame.getDestCallNo(), 
                false, 
                fullFrame.getSrcCallNo(), 
                fullFrame.getTimestamp(), 
                fullFrame.getIseqno(), 
                fullFrame.getOseqno(), 
                false, 
                ProtocolControlFrame.INVAL_SC));
        } else {
            MiniFrame miniFrame = (MiniFrame)frame;
            peer.sendFrame(new ProtocolControlFrame(0, 
                    false, 
                    miniFrame.getSrcCallNo(), 
                    miniFrame.getTimestamp(), 
                    0, 
                    0, 
                    false, 
                    ProtocolControlFrame.INVAL_SC));
        }
    }

}
