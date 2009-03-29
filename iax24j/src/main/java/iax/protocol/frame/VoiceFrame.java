package iax.protocol.frame;

/**
 * Voice Frame. See IAX protocol draft. 
 */
public class VoiceFrame extends FullFrame {
    
    /**
     * Data attached to the frame.
     */
    private byte[] data;
    
    /**
     * Constructor. Initializes the frame.
     */
    protected VoiceFrame() {
        super();
    }
    
    /**
     * Constructor. Initializes the frame with the given values.
     * @param srcCallNo Source call number.
     * @param retry Indicates if the frame is being retransmitted.
     * @param destCallNo Destination call number.
     * @param timeStamp Timestamp of the frame.
     * @param oSeqno Output sequence number.
     * @param iSeqno In put sequence number.
     * @param subclassValueFormat Indicates if subclass value is a 2 power or not.
     * @param subclass Frame subclass.
     * @param data
     */
    public VoiceFrame (int srcCallNo, boolean retry, int destCallNo, long timeStamp, int oSeqno, 
            int iSeqno, boolean subclassValueFormat, int subclass, byte[] data) {
        super(Frame.VOICEFRAME_T, srcCallNo, retry, destCallNo, timeStamp, oSeqno, iSeqno, VOICE_FT, subclassValueFormat, subclass);
        this.data = data;
    }
    
    /**
     * Constructor. Initializes the frame with given values.
     * @param buffer The buffer that contains the frame bytes.
     * @throws FrameException
     */
    public VoiceFrame (byte[] buffer) throws FrameException {
        super(Frame.VOICEFRAME_T, buffer);
        try {
            data = new byte[buffer.length - FULLFRAME_HEADER_LENGTH];
            System.arraycopy(buffer, FULLFRAME_HEADER_LENGTH, data, 0, data.length);
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Gets frame data.
     * @return A byte array with frame data.
     */
    public byte[] getData() {
        return data;
    }
    
    public byte[] serialize() throws FrameException {
        try {
            byte[] superInBytes = super.serialize();
            byte[] thisInBytes = new byte[superInBytes.length+data.length];
            System.arraycopy(superInBytes, 0, thisInBytes, 0, superInBytes.length);
            System.arraycopy(data, 0, thisInBytes, superInBytes.length, data.length);
            return thisInBytes;
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }

}
