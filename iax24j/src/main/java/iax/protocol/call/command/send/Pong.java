package iax.protocol.call.command.send;

import iax.protocol.call.Call;
import iax.protocol.frame.FullFrame;
import iax.protocol.frame.ProtocolControlFrame;

/**
 * Sends a pong
 */
public class Pong implements CallCommandSend {

    // Call for sending the frame
    private Call call;
    // Ping frame that needs a pong frame
    private FullFrame pingFrame;

    /**
     * Constructor
     * @param call call for sending the frame
     * @param pingFrame ping frame that needs a pong frame
     */
    public Pong(Call call, FullFrame pingFrame) {
        this.call = call;
        this.pingFrame = pingFrame;
    }

    public void execute() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        call.handleSendFrame(new ProtocolControlFrame(call.getSrcCallNo(), 
                false, 
                call.getDestCallNo(), 
                pingFrame.getTimestamp(), 
                call.getOseqno(), 
                call.getIseqno(), 
                false, 
                ProtocolControlFrame.PONG_SC));
    }

}
