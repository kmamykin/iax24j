package iax.protocol.call.command.send;

import iax.protocol.call.Call;
import iax.protocol.frame.ProtocolControlFrame;

/**
 * Sends an hangup
 */
public class Hangup implements CallCommandSend {
    // Call for sending the frame
	private Call call;

    /**
     * Constructor
     * @param call call for sending the frame
     */
	public Hangup(Call call) {
		this.call = call;
	}

	public void execute() {
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		try{
            ProtocolControlFrame hangupFrame = 
                new ProtocolControlFrame(call.getSrcCallNo(), false, call.getDestCallNo(), call.getTimestamp(), call.getOseqno(),
                        call.getIseqno(), false, ProtocolControlFrame.HANGUP_SC);

            call.handleSendFrame(hangupFrame);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
