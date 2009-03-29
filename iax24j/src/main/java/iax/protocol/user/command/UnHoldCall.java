package iax.protocol.user.command;

import iax.protocol.call.Call;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.peer.Peer;

/**
 * Implements the user command that unholds a call.
 */
public class UnHoldCall implements UserCommand {

    //Current peer
    private Peer peer;
    //Number of the peer to hang the call.
    private String calledNumber;

    /**
     * Constructor. Initializes the command with suitable values.
     * @param peer Current peer.
     * @param calledNumber Number of the peer to hang the call.
     */
    public UnHoldCall(Peer peer, String calledNumber) {
        this.peer = peer;
        this.calledNumber = calledNumber;
    }

    public void execute() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try {
            Call call = peer.getCall(calledNumber);
            ProtocolControlFrame unquelchCallFrame = 
                new ProtocolControlFrame(call.getSrcCallNo(), false, call.getDestCallNo(), call.getTimestamp(), call.getOseqno(),
                        call.getIseqno(), false, ProtocolControlFrame.UNQUELCH_SC);
            call.handleSendFrame(unquelchCallFrame);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}