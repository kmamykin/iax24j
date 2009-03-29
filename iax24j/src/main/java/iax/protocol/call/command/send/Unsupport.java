package iax.protocol.call.command.send;

import iax.protocol.call.Call;
import iax.protocol.frame.FullFrame;
import iax.protocol.frame.ProtocolControlFrame;

/**
 * Sends an unsupport
 */
public class Unsupport implements CallCommandSend {

    private Call call;
    private FullFrame fullFrame;

    /**
     * Constructor
     * @param call call for sending the frame
     * @param fullFrame the full frame that needs an unsupported frame
     */
    public Unsupport(Call call, FullFrame fullFrame) {
        this.call = call;
        this.fullFrame = fullFrame;
    }
    
    public void execute() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try {
            ProtocolControlFrame unsupportFrame = new ProtocolControlFrame(call.getSrcCallNo(), false, call.getDestCallNo(), 
                call.getTimestamp(), call.getOseqno(), call.getIseqno(), false, ProtocolControlFrame.UNSUPPORT_SC);
            unsupportFrame.setUnknown(fullFrame.getSubclass());
            call.handleSendFrame(unsupportFrame);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}