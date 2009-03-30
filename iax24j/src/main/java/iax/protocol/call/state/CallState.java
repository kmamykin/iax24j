package iax.protocol.call.state;

import iax.protocol.call.Call;
import iax.protocol.call.command.recv.CallCommandRecvFacade;
import iax.protocol.frame.Frame;
import iax.protocol.frame.ProtocolControlFrame;

/**
 * Defines the abstract methods for changing the call state and handling the frames
 *
 */
public abstract class CallState {

    /**
     * Handles the frame received
     * @param call the call from wich the frame was received
     * @param frame the frame received
     */
    public void handleRecvFrame(Call call, Frame frame) {
        try {
            // By default handles the nexts protocol control frames: hangup, lag request and ping
            if (frame.getType() == Frame.PROTOCOLCONTROLFRAME_T) {
                // Received a protocol control frame
                ProtocolControlFrame protocolControlFrame = (ProtocolControlFrame) frame;

                //System.out.println("Received in CallState: " + protocolControlFrame.getSubclass());

                switch (protocolControlFrame.getSubclass()) {
                case ProtocolControlFrame.ACK_SC:
                    CallCommandRecvFacade.ack(call, protocolControlFrame);
                    break;
                case ProtocolControlFrame.HANGUP_SC:
                    CallCommandRecvFacade.hangup(call, protocolControlFrame);
                    call.setState(Initial.getInstance());
                    call.endCall();
                    break;
                case ProtocolControlFrame.INVAL_SC:
                    call.setState(Initial.getInstance());
                    call.endCall();
                    break;
                case ProtocolControlFrame.LAGRQ_SC:
                    CallCommandRecvFacade.lagrq(call, protocolControlFrame);
                    break;
                case ProtocolControlFrame.PING_SC:
                    CallCommandRecvFacade.ping(call, protocolControlFrame);
                    break;
                case ProtocolControlFrame.PONG_SC:
                    CallCommandRecvFacade.pong(call, protocolControlFrame);
                    break;
                case ProtocolControlFrame.UNSUPPORT_SC:
                    CallCommandRecvFacade.unsupport(call, protocolControlFrame);
                    break;
                default:           
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the frame to send
     * @param call call for sending the frame
     * @param frame the frame to send
     */
    public void handleSendFrame(Call call, Frame frame) {
        try {
            // By default handles the nexts protocol control frames: ack, lag reply, hangup, and pong
            if (frame.getType() == Frame.PROTOCOLCONTROLFRAME_T) {
                // Sending a protocol control frame
                ProtocolControlFrame protocolControlFrame = (ProtocolControlFrame) frame;
                switch (protocolControlFrame.getSubclass()) {
                case ProtocolControlFrame.ACK_SC:
                    call.sendFrameAndNoWait(protocolControlFrame);
                    break;
                case ProtocolControlFrame.LAGRP_SC:
                    call.sendFullFrameAndWaitForAck(protocolControlFrame);
                    break;
                case ProtocolControlFrame.HANGUP_SC:
                    call.sendFullFrameAndWaitForAck(protocolControlFrame);
                    call.setState(Initial.getInstance());
                    call.endCall();
                    break;
                case ProtocolControlFrame.PING_SC:
                    call.sendFullFrameAndWaitForRep(protocolControlFrame);
                    break;
                case ProtocolControlFrame.QUELCH_SC:
                    call.sendFullFrameAndWaitForAck(protocolControlFrame);
                    call.holdCall();
                    break;
                case ProtocolControlFrame.PONG_SC:
                    call.sendFullFrameAndWaitForAck(protocolControlFrame);
                    break;
                case ProtocolControlFrame.UNQUELCH_SC:
                    call.sendFullFrameAndWaitForAck(protocolControlFrame);
                    call.unHoldCall();
                    break;
                case ProtocolControlFrame.UNSUPPORT_SC:
                    call.sendFullFrameAndWaitForAck(protocolControlFrame);
                    break;
                default:
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}