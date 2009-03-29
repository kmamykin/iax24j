package iax.protocol.peer.command.recv;

import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.peer.Peer;
import iax.protocol.peer.command.send.PeerCommandSendFacade;
import iax.protocol.util.FrameUtil;

/**
 * Facade of the commands valids when a frame is received. 
 * Uses the PeerSendCommandFacade to send the frames when a response is needed.
 */
public class PeerCommandRecvFacade {

    private PeerCommandRecvFacade() {}
   

    /**
     * Handling an ack by calling the method ackedFrame of the peer
     * @param peer the peer from wich the accept was received
     * @param ackFrame the ack frame
     */
    public static void ack(Peer peer, ProtocolControlFrame ackFrame) {
        peer.ackedFrame(ackFrame.getTimestamp());
    }
    
    /**
     * Handling an hangup by sending an ack
     * @param peer the peer from which the hangup was received
     * @param hangupFrame the hangup frame
     */
    public static void hangup(Peer peer, ProtocolControlFrame hangupFrame) {
        PeerCommandSendFacade.ack(peer, hangupFrame);
    }
    
    /**
     * Handling a poke by sending a pong
     * @param peer the peer from wich the poke was received
     * @param poke the poke frame
     */
    public static void poke(Peer peer, ProtocolControlFrame poke) {
        PeerCommandSendFacade.pong(peer, poke);
    }
    
    /**
     * Handling a register ack by sending an ack
     * @param peer the peer from wich the register ack was received
     * @param regack the regack frame
     */
    public static void regack(Peer peer, ProtocolControlFrame regack) {
        int [] repliedSubclasses = FrameUtil.getReplySubclasses((regack.getSubclass()));
        for (int i=0; i<repliedSubclasses.length; i++)    
            peer.repliedFrame(repliedSubclasses[i]);
        PeerCommandSendFacade.ack(peer, regack);
    }
   
    
    /**
     * Handling a register auth by sending a register release
     * @param peer the peer from wich the register auth was received
     * @param regauth the regauth frame
     */
    public static void regauthRel(Peer peer, ProtocolControlFrame regauth) {
        int [] repliedSubclasses = FrameUtil.getReplySubclasses((regauth.getSubclass()));
        for (int i=0; i<repliedSubclasses.length; i++)    
            peer.repliedFrame(repliedSubclasses[i]);
        PeerCommandSendFacade.regrel(peer, regauth);
    }
    
    /**
     * Handling a register auth by sending a register request
     * @param peer the peer from wich the register auth was received
     * @param regauth the regauth frame
     */
    public static void regauthReq(Peer peer, ProtocolControlFrame regauth) {
        int [] repliedSubclasses = FrameUtil.getReplySubclasses((regauth.getSubclass()));
        for (int i=0; i<repliedSubclasses.length; i++)    
            peer.repliedFrame(repliedSubclasses[i]);
        PeerCommandSendFacade.regreq(peer, regauth);
    }
    
    /**
     * Handling a register reject by sending an ack
     * @param peer the peer from wich the register reject was received
     * @param regrej the regrej frame
     */
    public static void regrej(Peer peer, ProtocolControlFrame regrej) {
        int [] repliedSubclasses = FrameUtil.getReplySubclasses((regrej.getSubclass()));
        for (int i=0; i<repliedSubclasses.length; i++)    
            peer.repliedFrame(repliedSubclasses[i]);
        PeerCommandSendFacade.ack(peer, regrej);
    }
    
    /**
     * Handling an unsupported frame
     * @param peer the peer from which the unsupport frame was received
     * @param unsupportFrame the unsupported frame
     */
    public static void unsupport(Peer peer, ProtocolControlFrame unsupportFrame) {
        PeerCommandSendFacade.ack(peer, unsupportFrame);
    }
}
