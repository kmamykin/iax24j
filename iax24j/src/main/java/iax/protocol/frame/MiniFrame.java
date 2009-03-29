package iax.protocol.frame;

import iax.protocol.util.ByteBuffer;

/**
 * IAX data MiniFrame. See IAX draft.
 */
public class MiniFrame extends Frame {   
    
	//Miniframe timestamp.
    private int timestamp;
    //Miniframe data.
    private byte[] data;
    
    /**
     * MiniFrame header length in bytes (taking count of common part present in Frame) 
     */
    protected static final int MINIFRAME_HEADER_LENGTH = FRAME_HEADER_LENGTH + 2;
    
    /**
     * Constructor. Initializes the MiniFrame object with default values.
     */
    protected MiniFrame() {
        super();
    }
    
    /**
     * Constructor. Initializes the MiniFrame object with given values.
     * @param srcCallNo Source call number.
     * @param timestamp Timestamp.
     * @param data Data.
     */
    public MiniFrame (int srcCallNo, int timestamp, byte[] data) {
        super(Frame.MINIFRAME_T, false, srcCallNo);
        this.timestamp = timestamp;
        this.data = data;
    }
    
    /**
     * Constructor. Initializes the MiniFrame object from a byte array.
     * @param buffer The byte array.
     * @throws FrameException
     */
    public MiniFrame(byte buffer[]) throws FrameException {
        super(Frame.MINIFRAME_T, buffer);
        try {
            byte[] aux = new byte[buffer.length - FRAME_HEADER_LENGTH];
            System.arraycopy(buffer, FRAME_HEADER_LENGTH, aux, 0, aux.length);
            ByteBuffer byteBuffer = new ByteBuffer(aux);
            timestamp = byteBuffer.get16bits();
            data = byteBuffer.getByteArray();
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Gets the timestamp.
     * @return The timestamp as integer.
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp.
     * @param timestamp The timestamp as short.
     */
    public void setTimestamp(short timestamp) {
        this.timestamp = timestamp;
    }
    
    /**
     * Gets the MiniFrame data.
     * @return The data as byte array.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Sets the miniFrame data.
     * @param data The data as byte array.
     */
    public void setData(byte[] data) {
        this.data = data;
    }
    
    public byte[] serialize() throws FrameException {
        try {
            byte[] superInBytes = super.serialize();
            byte[] thisInBytes = new byte[MINIFRAME_HEADER_LENGTH+data.length];
            ByteBuffer byteBuffer = new ByteBuffer(MINIFRAME_HEADER_LENGTH-FRAME_HEADER_LENGTH);
            byteBuffer.put16bits(timestamp);
            System.arraycopy(superInBytes, 0, thisInBytes, 0, superInBytes.length);
            System.arraycopy(byteBuffer.getBuffer(), 0, thisInBytes, superInBytes.length, byteBuffer.getBuffer().length);
            System.arraycopy(data, 0, thisInBytes, MINIFRAME_HEADER_LENGTH, data.length);
            return thisInBytes;
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
}