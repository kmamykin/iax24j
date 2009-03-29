package iax.protocol.user.command;


import iax.protocol.peer.Peer;

/**
 * Facade to user commands.
 */
public class UserCommandFacade {
    
    /**
     * Method that indicates that user has answered an incoming call.
     * @param peer Current peer.
     * @param callingNumber the calling number of the call that is going to be accepted
     */
    public static void answerCall(Peer peer, String callingNumber) {
        (new AnswerCall(peer, callingNumber)).execute();
    }

    /**
     * Method to hang up a call.
     * @param peer Current peer.
     * @param calledNumber The number of the hung call.
     */
    public static void hangupCall(Peer peer, String calledNumber) {
        (new HangupCall(peer, calledNumber)).execute();
    }

    /**
     * Method to hold a call.
     * @param peer Current peer.
     * @param calledNumber The number of the muted call.
     */
    public static void holdCall(Peer peer, String calledNumber) {
        (new HoldCall(peer, calledNumber)).execute();
    }
    
    /**
     * Method to start a new call.
     * @param peer Current peer.
     * @param calledNumber Number to call to.
     */
    public static void newCall(Peer peer, String calledNumber) {
        (new NewCall(peer, calledNumber)).execute();
    }

    /**
     * Exit from the system
     * @param peer Current peer.
     */
    public static void exit(Peer peer) {
        (new Exit(peer)).execute();
    }
    
    /**
     * Method to mute a call.
     * @param peer Current peer.
     * @param calledNumber The number of the muted call.
     */
    public static void muteCall(Peer peer, String calledNumber) {
        (new MuteCall(peer, calledNumber)).execute();
    }
    
    /**
     * Method to unhold a call.
     * @param peer Current peer.
     * @param calledNumber The number of the muted call.
     */
    public static void unHoldCall(Peer peer, String calledNumber) {
        (new UnHoldCall(peer, calledNumber)).execute();
    }
    
    /**
     * Method to unmute a call.
     * @param peer Current peer.
     * @param calledNumber The number of the muted call.
     */
    public static void unMuteCall(Peer peer, String calledNumber) {
        (new UnMuteCall(peer, calledNumber)).execute();
    }
    
    /**
     * Method to send a DTMF tone.
     * @param peer Current peer.
     * @param calledNumber The number of the muted call.
     */
    public static void sendDTMF(Peer peer, String calledNumber, char tone) {
        (new SendDTMF(peer, calledNumber, tone)).execute();
    }
    
    /**
     * Method to transfer a call.
     * @param peer Current peer.
     * @param srcNumber the source number of the transfer
     * @param dstNumber the destination number of the transfer
     */
    public static void transferCall(Peer peer, String srcNumber, String dstNumber) {
        (new TransferCall(peer, srcNumber, dstNumber)).execute();
    }
}