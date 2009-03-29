package iax.protocol.user.command;

import iax.protocol.call.Call;
import iax.protocol.frame.ControlFrame;
import iax.protocol.peer.Peer;

/**
 * Implements the user command that accepts a call.
 */
public class AnswerCall implements UserCommand {
    
    private Peer peer;
    private String callingNumber;

    /**
     * Constructor. Initializes the command with suitable values.
     * @param peer Current peer.
     * @param callingNumber the calling number of the call that is going to be accepted
     */
    public AnswerCall(Peer peer, String callingNumber) {
        this.peer = peer;
        this.callingNumber = callingNumber;
    }
    
    public void execute(){
        Thread t = new Thread(this);
        t.start();
    }
    
    public void run() {
        try {
            Call call = peer.getCall(callingNumber);
            
            ControlFrame answerFrame = 
                new ControlFrame(call.getSrcCallNo(), false, call.getDestCallNo(), call.getTimestamp(), call.getOseqno(),
                        call.getIseqno(), false, ControlFrame.ANSWER, new byte[0]);

            call.startRecorder();
            call.handleSendFrame(answerFrame);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}