package iax.protocol.peer.command.send;

import iax.protocol.frame.FullFrame;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.peer.Peer;

/**
 * Sends an unsupport
 */
public class Unsupport implements PeerCommandSend {

    private Peer peer;
    private FullFrame fullFrame;

    /**
     * Constructor
     * @param peer peer for sending the frame
     * @param fullFrame the full frame that needs an unsupported frame
     */
    public Unsupport(Peer peer, FullFrame fullFrame) {
        this.peer = peer;
        this.fullFrame = fullFrame;
    }
    
    public void execute() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try {
            ProtocolControlFrame unsupportFrame = new ProtocolControlFrame(fullFrame.getDestCallNo(), false, fullFrame.getSrcCallNo(), 
                fullFrame.getTimestamp(), fullFrame.getIseqno(), fullFrame.getOseqno(), false, ProtocolControlFrame.UNSUPPORT_SC);
            unsupportFrame.setUnknown(fullFrame.getSubclass());
            peer.sendFrame(unsupportFrame);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
