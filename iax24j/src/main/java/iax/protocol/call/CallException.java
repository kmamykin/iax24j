package iax.protocol.call;

/**
 * Call's exception
 */
public class CallException extends Exception {
    
    /**
     * Constructor from a message
     * @param message message of the exception
     */
    public CallException(String message) {
        super(message);
    }
    
    /**
     * Contructor from another exception
     * @param e another exception
     */
    public CallException(Exception e) {
        super(e.getMessage());
    }
}
