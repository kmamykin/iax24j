package iax.protocol.peer.command.send;

import iax.protocol.frame.Frame;
import iax.protocol.frame.FullFrame;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.peer.Peer;

public class PeerCommandSendFacade{

    private PeerCommandSendFacade() {}
    
    /**
     * Sends an ack delegating in the Ack command send 
     * @param peer peer for sending the frame
     * @param fullFrame full frame that needs an ack
     */
    public static void ack(Peer peer, FullFrame fullFrame) {
        (new Ack(peer, fullFrame)).execute();
    }
  
    /**
     * Sends a busy delegating in the Busy command send 
     * @param peer peer for sending the frame
     * @param fullFrame full frame that need a busy frame
     */
    public static void busy(Peer peer, FullFrame fullFrame) {
        (new Busy(peer, fullFrame)).execute();
    }
    
    
    /**
     * Sends an inval frame for a frame received without any call that handles it, delegating in the Inval command send 
     * @param peer peer for sending the frame
     * @param frame the frame received without any call that handles it
     */
    public static void inval(Peer peer, Frame frame) {
        (new Inval(peer, frame)).execute();
    }
    
    /**
     * Sends a pong delegating in the Pong command send 
     * @param peer peer for sending the frame
     * @param pokeFrame poke frame that needs a pong frame
     */
    public static void pong(Peer peer, ProtocolControlFrame pokeFrame) {
        (new Pong(peer, pokeFrame)).execute();  
    }
    
    /**
     * Sends a register release frame, delegating in the RegReq command send
     * @param peer peer for sending the frame
     */
    public static void regrel(Peer peer) {
        (new RegRel(peer)).execute();
    }
    
    /**
     * Sends a register release frame for a regauth frame received, delegating in the RegReq command send
     * @param peer peer for sending the frame
     * @param regauthFrame regauth frame
     */
    public static void regrel(Peer peer, ProtocolControlFrame regauthFrame) {
        (new RegRel(peer, regauthFrame)).execute();
    }
    
    /**
     * Sends a register request frame, delegating in the RegReq command send
     * @param peer peer for sending the frame
     */
    public static void regreq(Peer peer) {
        (new RegReq(peer)).execute();
    }
    
    /**
     * Sends a register request frame for a regauth frame received, delegating in the RegReq command send
     * @param peer peer for sending the frame
     * @param regauthFrame regauth frame
     */
    public static void regreq(Peer peer, ProtocolControlFrame regauthFrame) {
        (new RegReq(peer, regauthFrame)).execute();
    }
    
    /**
     * Sends an unsupported frame for a fullFrame received that is not supported, delegating in the Unsupport command send 
     * @param peer peer for sending the frame
     * @param fullFrame full frame not supported that needs an unsupported frame
     */
    public static void unsupport(Peer peer, FullFrame fullFrame) {
        (new Unsupport(peer, fullFrame)).execute();
    }
}
