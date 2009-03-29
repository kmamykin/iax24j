package iax.protocol.frame;

/**
 * Frame's exception
 */
public class FrameException extends Exception {
    
    /**
     * Constructor from a message
     * @param message message of the exception
     */
    public FrameException(String message) {
        super(message);
    }
    
    /**
     * Contructor from another exception
     * @param e another exception
     */
    public FrameException(Exception e) {
        super(e.getMessage());
    }
}
