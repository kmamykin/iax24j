package iax.protocol.call.state;

import iax.protocol.call.Call;
import iax.protocol.call.command.recv.CallCommandRecvFacade;
import iax.protocol.call.command.send.CallCommandSendFacade;
import iax.protocol.frame.ControlFrame;
import iax.protocol.frame.Frame;
import iax.protocol.frame.MiniFrame;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.frame.VoiceFrame;

/**
 * Call's state linked. It's a singleton.
 */
public class Linked extends CallState {
    // Singleton instance
    private static Linked instance;
    
    // Private constructor
    private Linked() {
        instance = this;
    }

    /**
     * Gets an instance of this state
     * @return the instance of this state
     */
    public static Linked getInstance() {
        if (instance != null) {
            return instance;
        } else return new Linked();
    }

    public void handleRecvFrame(Call call, Frame frame) {
        try {
            if (frame.getType() == Frame.CONTROLFRAME_T) {
                // Received a control frame
                ControlFrame controlFrame = (ControlFrame) frame;
                switch (controlFrame.getSubclass()) {
                case ControlFrame.ANSWER:
                    // Handles an answer frame received and sets the call's state to up
                    CallCommandRecvFacade.answer(call, controlFrame);
                    call.setState(Up.getInstance());
                    break;
                case ControlFrame.PROCEEDING:
                    // Handles a proceeding frame received
                    CallCommandRecvFacade.proceeding(call, controlFrame);
                    break;
                case ControlFrame.RINGING:
                    // Handles a ringing frame received
                    CallCommandRecvFacade.ringing(call, controlFrame);
                    break;
                default:
                    // By default, sends an ack for the control frame received
                    CallCommandSendFacade.ack(call, controlFrame);
                    break;
                }
            } else if (frame.getType() == Frame.PROTOCOLCONTROLFRAME_T) {
                // Received a protocol control frame
                ProtocolControlFrame protocolControlFrame = (ProtocolControlFrame) frame;
                switch (protocolControlFrame.getSubclass()) {
                case ProtocolControlFrame.ACCEPT_SC:
                    // Received an accept frame
                	CallCommandRecvFacade.accept(call, protocolControlFrame);
                    break;
                default:
                    // By default, delegates received frames in the super
                    super.handleRecvFrame(call, frame);
                    break;
                }
            } else if (frame.getType() == Frame.VOICEFRAME_T) {
                // Received a voice full frame
            	VoiceFrame voiceFrame = (VoiceFrame) frame;
				CallCommandRecvFacade.voiceFullFrame(call, voiceFrame);
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
            if (frame.getType() == Frame.CONTROLFRAME_T) {
                // Received a control frame
                ControlFrame controlFrame = (ControlFrame) frame;
                switch (controlFrame.getSubclass()) {
                case ControlFrame.ANSWER:
                    call.sendFullFrameAndWaitForAck(controlFrame);
                    call.setState(Up.getInstance());
                    break;
                case ControlFrame.BUSY:
                    call.sendFullFrameAndWaitForAck(controlFrame);
                    break;
                case ControlFrame.RINGING:
                    call.sendFullFrameAndWaitForAck(controlFrame);
                    break;
                default:
                    break;
                }
            } else if (frame.getType() == Frame.PROTOCOLCONTROLFRAME_T) {
                // Sending a protocol control frame
                ProtocolControlFrame protocolControlFrame = (ProtocolControlFrame) frame;
                switch (protocolControlFrame.getSubclass()) {
                case ProtocolControlFrame.ACCEPT_SC:
                    call.sendFullFrameAndWaitForAck(protocolControlFrame);
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