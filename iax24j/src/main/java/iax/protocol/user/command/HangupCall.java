package iax.protocol.user.command;

import iax.protocol.call.Call;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.peer.Peer;

/**
 * Implements the user command that hangs a given initiated call.
 */
public class HangupCall implements UserCommand {
  
	//Current peer
    private Peer peer;
    //Number of the peer to hang the call.
    private String calledNumber;

    /**
     * Constructor. Initializes the command with suitable values.
     * @param peer Current peer.
     * @param calledNumber Number of the peer to hang the call.
     */
    public HangupCall(Peer peer, String calledNumber) {
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

            ProtocolControlFrame hangupFrame = 
                new ProtocolControlFrame(call.getSrcCallNo(), false, call.getDestCallNo(), call.getTimestamp(), call.getOseqno(),
                        call.getIseqno(), false, ProtocolControlFrame.HANGUP_SC);

            call.handleSendFrame(hangupFrame);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}