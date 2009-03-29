package iax.protocol.call.command.send;

import iax.protocol.call.Call;
import iax.protocol.frame.ProtocolControlFrame;

/**
 * Sends an accept
 */
public class Accept implements CallCommandSend {
    // Call for sending the frame
    private Call call;

    /**
     * Constructor
     * @param call call for sending the frame
     */
    public Accept(Call call) {
        this.call = call;
    }

    public void execute() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        call.handleSendFrame(new ProtocolControlFrame(call.getSrcCallNo(), 
                false, 
                call.getDestCallNo(), 
                call.getTimestamp(), 
                call.getOseqno(), 
                call.getIseqno(), 
                false, 
                ProtocolControlFrame.ACCEPT_SC));
    }

}
