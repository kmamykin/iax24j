package iax.protocol.util;

/**
 * Exception thrown when ByteBuffer is unable to initialize or out of bounds.
 *
 */
public class ByteBufferException extends Exception {
   
	/**
	 * Constructor. Creates a ByteBufferException from a String.
	 * @param message Exception message.
	 */
    public ByteBufferException(String message) {
        super(message);
    }
    
    /**
	 * Constructor. Creates a ByteBufferException from an Exception.
	 * @param e Inherited exception.
	 */
    public ByteBufferException(Exception e) {
        super(e.getMessage());
    }
    
}
