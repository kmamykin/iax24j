package iax.protocol.call.command.send;

import iax.protocol.call.Call;
import iax.protocol.frame.ProtocolControlFrame;

/**
 * Sends a ping
 */
public class Ping implements CallCommandSend {
    // Call for sending the frame
    private Call call;

    /**
     * Constructor
     * @param call call for sending the frame
     */
    public Ping(Call call) {
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
                ProtocolControlFrame.PING_SC));
    }

}