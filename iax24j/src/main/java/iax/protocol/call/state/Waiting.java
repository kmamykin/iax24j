package iax.protocol.call.state;

import iax.protocol.call.Call;
import iax.protocol.call.command.recv.CallCommandRecvFacade;
import iax.protocol.frame.Frame;
import iax.protocol.frame.ProtocolControlFrame;

/**
 * Call's state waiting. It's a singleton.
 */
public class Waiting extends CallState {
    // Singleton instance
	private static Waiting instance;
    
    // Private constructor
	private Waiting() {
		instance = this;
	}

    /**
     * Gets an instance of this state
     * @return the instance of this state
     */
	public static Waiting getInstance() {
		if (instance != null) {
			return instance;
		} else return new Waiting();
	}

	public void handleRecvFrame(Call call, Frame frame) {
		try {
			if (frame.getType() == Frame.PROTOCOLCONTROLFRAME_T) {
                // Received a protocol control frame
				ProtocolControlFrame protocolControlFrame = (ProtocolControlFrame) frame;
				switch (protocolControlFrame.getSubclass()) {
				case ProtocolControlFrame.ACCEPT_SC:
                    // Received an accept frame
                    CallCommandRecvFacade.accept(call, protocolControlFrame);
					call.setState(Linked.getInstance());
					break;
				case ProtocolControlFrame.AUTHREQ_SC:
                    // Received an authorization request frame
                    CallCommandRecvFacade.authReq(call, protocolControlFrame);
                    call.bindCall(protocolControlFrame.getSrcCallNo());
					break;
				case ProtocolControlFrame.REJECT_SC:
                    // Received a reject frame
                    CallCommandRecvFacade.reject(call, protocolControlFrame);
					call.setState(Initial.getInstance());
					call.endCall();
					break;
				default:
                    // By default, delegates received frames in the super
                    super.handleRecvFrame(call, frame);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleSendFrame(Call call, Frame frame) {
		try {
			if (frame.getType() == Frame.PROTOCOLCONTROLFRAME_T) {
                // Sending a protocol control frame
				ProtocolControlFrame protocolControlFrame = (ProtocolControlFrame) frame;
				switch (protocolControlFrame.getSubclass()) {
                case ProtocolControlFrame.AUTHREP_SC:
                    // Sending an authorization reply frame
					call.sendFullFrameAndWaitForRep(protocolControlFrame);
					break;
				default:
                    // By default, delegates frames to send in the super
                    super.handleSendFrame(call, frame);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}