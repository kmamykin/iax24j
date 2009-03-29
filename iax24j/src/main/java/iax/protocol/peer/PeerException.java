package iax.protocol.peer;

/**
 * Exception thrown when peer is unable to get a given call.
 *
 */
public class PeerException extends Exception {
    
	/**
	 * Constructor. Creates a PeerException from a String.
	 * @param message Exception message.
	 */
    public PeerException(String message) {
        super(message);
    }
    
    /**
	 * Constructor. Creates a PeerException from an Exception.
	 * @param e Inherited exception.
	 */
    public PeerException(Exception e) {
        super(e.getMessage());
    }
}
