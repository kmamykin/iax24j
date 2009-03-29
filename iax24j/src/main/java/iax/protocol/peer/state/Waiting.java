package iax.protocol.peer.state;

import iax.protocol.frame.Frame;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.peer.Peer;
import iax.protocol.peer.command.recv.PeerCommandRecvFacade;

public class Waiting extends PeerState {
    
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

    public void handleRecvFrame(Peer peer, Frame frame) {
        try {
            if (frame.getType() == Frame.PROTOCOLCONTROLFRAME_T) {
                // Received a protocol control frame
                ProtocolControlFrame protocolControlFrame = (ProtocolControlFrame) frame;
                switch (protocolControlFrame.getSubclass()) {
                case ProtocolControlFrame.REGACK_SC:
                    PeerCommandRecvFacade.regack(peer, protocolControlFrame);
                    if (peer.isProcessingExit())
                        peer.setState(Unregistered.getInstance());
                    else peer.setState(Registered.getInstance());
                    break;
                case ProtocolControlFrame.REGAUTH_SC:
                    if (peer.isProcessingExit())
                        PeerCommandRecvFacade.regauthRel(peer, protocolControlFrame);
                    else PeerCommandRecvFacade.regauthReq(peer, protocolControlFrame);
                    break;
                default:
                    // By default, delegates received frames in the super
                    super.handleRecvFrame(peer, frame);
                    break;
                }
            } else {
                // By default, delegates received frames in the super
                super.handleRecvFrame(peer, frame);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleSendFrame(Peer peer, Frame frame) {
        try {
            if (frame.getType() == Frame.PROTOCOLCONTROLFRAME_T) {
                // Sending a protocol control frame
                ProtocolControlFrame protocolControlFrame = (ProtocolControlFrame) frame;
                switch (protocolControlFrame.getSubclass()) {
                case ProtocolControlFrame.REGREL_SC:
                    peer.sendFullFrameAndWaitForRep(protocolControlFrame);
                    break;
                case ProtocolControlFrame.REGREQ_SC:
                    peer.sendFullFrameAndWaitForRep(protocolControlFrame);
                    break;
                default:
                    // By default, delegates frames to send in the super
                    super.handleSendFrame(peer, frame);
                    break;
                }
            } else {
                // By default, delegates received frames in the super
                super.handleSendFrame(peer, frame);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}