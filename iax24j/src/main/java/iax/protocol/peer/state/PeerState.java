package iax.protocol.peer.state;

import iax.protocol.frame.ControlFrame;
import iax.protocol.frame.Frame;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.peer.Peer;
import iax.protocol.peer.command.recv.PeerCommandRecvFacade;

public class PeerState {

    /**
     * Handles the frame received
     * @param peer the peer from wich the frame was received
     * @param frame the frame received
     */
    public void handleRecvFrame(Peer peer, Frame frame) {
        try {
            // By default handles the nexts protocol control frames: poke and unsupport
            if (frame.getType() == Frame.PROTOCOLCONTROLFRAME_T) {
                // Received a protocol control frame
                ProtocolControlFrame protocolControlFrame = (ProtocolControlFrame) frame;

                //System.out.println("Received in PeerState: " + protocolControlFrame.getSubclass());

                switch (protocolControlFrame.getSubclass()) {      
                case ProtocolControlFrame.ACK_SC:
                    PeerCommandRecvFacade.ack(peer, protocolControlFrame);
                    break;  
                case ProtocolControlFrame.HANGUP_SC:
                    PeerCommandRecvFacade.hangup(peer, protocolControlFrame);
                    break;
                case ProtocolControlFrame.POKE_SC:
                    PeerCommandRecvFacade.poke(peer, protocolControlFrame);
                    break;
                case ProtocolControlFrame.REGREJ_SC:
                    PeerCommandRecvFacade.regrej(peer, protocolControlFrame);
                    peer.setState(Unregistered.getInstance());
                    break;
                case ProtocolControlFrame.UNSUPPORT_SC:
                    PeerCommandRecvFacade.unsupport(peer, protocolControlFrame);
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
     * @param peer peer for sending the frame
     * @param frame the frame to send
     */
    public void handleSendFrame(Peer peer, Frame frame) {
        try {
            //By default handles the nexts control frames: busy
            if (frame.getType() == Frame.CONTROLFRAME_T) {
                // Received a control frame
                ControlFrame controlFrame = (ControlFrame) frame;
                switch (controlFrame.getSubclass()) {
                case ControlFrame.BUSY:
                    peer.sendFullFrameAndWaitForAck(controlFrame);
                    break;
                default:
                    break;
                }
            // By default handles the nexts protocol control frames: ack, pong and regrel
            } else if (frame.getType() == Frame.PROTOCOLCONTROLFRAME_T) {
                // Sending a protocol control frame
                ProtocolControlFrame protocolControlFrame = (ProtocolControlFrame) frame;
                switch (protocolControlFrame.getSubclass()) {
                case ProtocolControlFrame.ACK_SC:
                    peer.sendFrame(protocolControlFrame);
                    break;
                case ProtocolControlFrame.ACCEPT_SC:
                    peer.sendFullFrameAndWaitForAck(protocolControlFrame);
                    break;
                case ProtocolControlFrame.PONG_SC:
                    peer.sendFullFrameAndWaitForAck(protocolControlFrame);
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