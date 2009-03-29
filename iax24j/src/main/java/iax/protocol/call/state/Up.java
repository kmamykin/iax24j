package iax.protocol.call.state;

import iax.protocol.call.Call;
import iax.protocol.call.command.recv.CallCommandRecvFacade;
import iax.protocol.frame.ControlFrame;
import iax.protocol.frame.DTMFFrame;
import iax.protocol.frame.Frame;
import iax.protocol.frame.MiniFrame;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.frame.VoiceFrame;

/**
 * Call's state up. It's a singleton.
 */
public class Up extends CallState {
    // Singleton instance
	private static Up instance;
    
    // Private constructor
	private Up() {
		instance = this;
	}
    
    /**
     * Gets an instance of this state
     * @return the instance of this state
     */
	public static Up getInstance() {
		if (instance != null) {
			return instance;
		} else return new Up();
	}
    
    public void handleRecvFrame(Call call, Frame frame) {
        try {
            if (frame.getType() == Frame.CONTROLFRAME_T) {
                // Received a control frame
                ControlFrame controlFrame = (ControlFrame) frame;
                switch (controlFrame.getSubclass()) {
                case ControlFrame.RINGING:
                    // Handles a ringing frame received
                    CallCommandRecvFacade.ringing(call, controlFrame);
                    break;
                default:
                    break;
                }
            } else if (frame.getType() == Frame.PROTOCOLCONTROLFRAME_T) {
                ProtocolControlFrame protocolControlFrame = (ProtocolControlFrame) frame;
                switch (protocolControlFrame.getSubclass()) {      
                default:
                    // By default, delegates received frames in the super
                    super.handleRecvFrame(call, frame);
                    break;
                }
            } else if (frame.getType() == Frame.VOICEFRAME_T) {
                // Received a voice full frame
            	VoiceFrame voiceFrame = (VoiceFrame) frame;
				CallCommandRecvFacade.voiceFullFrame(call, voiceFrame);
            } else if (frame.getType() == Frame.DTMFFRAME_T) {
                // Received a voice full frame
            	DTMFFrame dtmfFrame = (DTMFFrame) frame;
				CallCommandRecvFacade.dtmfFullFrame(call, dtmfFrame);
            } else if (frame.getType() == Frame.MINIFRAME_T) {
                // Received a voice mini frame
            	MiniFrame miniFrame = (MiniFrame) frame;
				CallCommandRecvFacade.voiceMiniFrame(call, miniFrame);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void handleSendFrame(Call call, Frame frame) {
        try {
            if (frame.getType() == Frame.PROTOCOLCONTROLFRAME_T) {
                // Received a protocol control frame
                ProtocolControlFrame protocolControlFrame = (ProtocolControlFrame) frame;
                switch (protocolControlFrame.getSubclass()) {
                case ProtocolControlFrame.TRANSFER_SC:
                    call.sendFullFrameAndWaitForAck(protocolControlFrame);
                    break;
                default:
                    // By default, delegates frames to send in the super
                    super.handleSendFrame(call, frame);
                    break;
                }
            } else if (frame.getType() == Frame.VOICEFRAME_T) {
                // Sending a voice full frame
                VoiceFrame voiceFrame = (VoiceFrame) frame;
                call.sendFullFrameAndWaitForAck(voiceFrame);
            } else if (frame.getType() == Frame.MINIFRAME_T) {
                // Sending a voice mini frame
                MiniFrame miniFrame = (MiniFrame) frame;
                call.sendFrameAndNoWait(miniFrame);
            } else if (frame.getType() == Frame.DTMFFRAME_T) {
            	// Sending a DTMF frame
                DTMFFrame dtmfFrame = (DTMFFrame) frame;
                call.sendFullFrameAndWaitForAck(dtmfFrame);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}