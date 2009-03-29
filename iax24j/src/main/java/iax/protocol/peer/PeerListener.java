package iax.protocol.peer;

/**
 * Interface to be implemented by classes that has to be notified from peer changes.
 */
public interface PeerListener {

    /**
     * Notifies that peer has hung up a call.
     * @param calledNumber The number of the peer that has hung up the call.
     */
    public void hungup(String calledNumber);
    
    /**
     * Notifies that peer has received a call
     * @param callingName The name of the peer that makes the call
     * @param callingNumber The number of the peer that makes the call
     */
    public void recvCall(String callingName, String callingNumber);
    
    public void registered();
    
    public void waiting();
    
    public void unregistered();

    public void exited();
    
    public void answered(String calledNumber);
    
    public void playWaitTones(String calledNumber);
    
}
