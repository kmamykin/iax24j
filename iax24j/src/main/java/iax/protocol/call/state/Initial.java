package iax.protocol.call.state;

import iax.protocol.call.Call;
import iax.protocol.call.command.recv.CallCommandRecvFacade;
import iax.protocol.frame.Frame;
import iax.protocol.frame.ProtocolControlFrame;

/**
 * Call's state initial. It's a singleton.
 */
public class Initial extends CallState {

    // Singleton instance
	private static Initial instance;

    // Private constructor
	private Initial() {
		instance = this;
	}

    /**
     * Gets an instance of this state
     * @return the instance of this state
     */
	public static Initial getInstance() {
		if (instance != null) {
			return instance;
		} else return new Initial();
	}

	public void handleRecvFrame(Call call, Frame frame) {
        try {
            if (frame.getType() == Frame.PROTOCOLCONTROLFRAME_T) {
                // Received a protocol control frame
                ProtocolControlFrame protocolControlFrame = (ProtocolControlFrame) frame;
                switch (protocolControlFrame.getSubclass()) {
                case ProtocolControlFrame.NEW_SC:
                    call.bindCall(protocolControlFrame.getSrcCallNo());
                    call.setState(Linked.getInstance());
                    CallCommandRecvFacade.newCall(call, protocolControlFrame);
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
				case ProtocolControlFrame.NEW_SC:
                    // Sends a new full frame and sets the call's state to waiting
					call.sendFullFrameAndWaitForRep(protocolControlFrame);
                    call.setState(Waiting.getInstance());
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