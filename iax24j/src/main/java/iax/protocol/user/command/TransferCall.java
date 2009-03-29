package iax.protocol.user.command;

import iax.protocol.call.Call;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.peer.Peer;

/**
 * Implements the user command that transfers a call.
 */
public class TransferCall implements UserCommand {

    //Current peer
    private Peer peer;
    //Source number (or name of the extension) of the peer to transfer the calling
    private String srcNumber;
    //Destination number (or name of the extension) of the peer to transfer the calling
    private String dstNumber;

    /**
     * Constructor. Initializes the command with suitable values.
     * @param peer Current peer.
     * @param srcNumber source number of the peer to transfer the calling.
     * @param dstNumber destination number of the peer to transfer the calling.
     */
    public TransferCall(Peer peer, String srcNumber, String dstNumber) {
        this.peer = peer;
        this.srcNumber = srcNumber;
        this.dstNumber = dstNumber;
    }

    public void execute() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try {
            Call call = peer.getCall(srcNumber);

            ProtocolControlFrame transferCallFrame = 
                new ProtocolControlFrame(call.getSrcCallNo(), false, call.getDestCallNo(), call.getTimestamp(), call.getOseqno(),
                        call.getIseqno(), false, ProtocolControlFrame.TRANSFER_SC);
            transferCallFrame.setCalledNumber(dstNumber);
            call.handleSendFrame(transferCallFrame);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}