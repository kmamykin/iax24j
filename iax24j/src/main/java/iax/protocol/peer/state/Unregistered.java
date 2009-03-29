package iax.protocol.peer.state;

import iax.protocol.frame.Frame;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.peer.Peer;

/**
 * Peer's state unregistered. It's a singleton
 */
public class Unregistered extends PeerState {
    
    // Singleton instance
    private static Unregistered instance;

    // Private constructor
    private Unregistered() {
        instance = this;
    }

    /**
     * Gets an instance of this state
     * @return the instance of this state
     */
    public static Unregistered getInstance() {
        if (instance != null) {
            return instance;
        } else return new Unregistered();
    }

    public void handleRecvFrame(Peer peer, Frame frame) {
        try {
            if (frame.getType() == Frame.PROTOCOLCONTROLFRAME_T) {
                // Received a protocol control frame
                ProtocolControlFrame protocolControlFrame = (ProtocolControlFrame) frame;
                switch (protocolControlFrame.getSubclass()) {
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
                case ProtocolControlFrame.REGREQ_SC:
                    peer.sendFullFrameAndWaitForRep(protocolControlFrame);
                    peer.setState(Waiting.getInstance());
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