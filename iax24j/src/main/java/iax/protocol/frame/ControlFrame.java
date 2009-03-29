package iax.protocol.frame;

/**
 * Control Frame. See IAX protocol draft. 
 */
public class ControlFrame extends FullFrame {
    /**
     * Control frame, subclass value HANGUP
     */
    public static final int HANGUP = 1;
    /**
     * Control frame, subclass value: RINGING
     */
    public static final int RINGING = 3;
    /**
     * Control frame, subclass value ANSWER
     */
    public static final int ANSWER = 4;
    /**
     * Control frame, subclass value BUSY
     */
    public static final int BUSY = 5;
    /**
     * Control frame, subclass value PROCEEDING
     */
    public static final int PROCEEDING = 15;
    
    
    // Data attached to the control frame.
    private byte[] data;
    
    /**
     * Constructor.
     */
    protected ControlFrame() {
        super();
    }
    
    /**
     * Constructor.
     * @param srcCallNo Source call number.
     * @param retry Indicates if the frame is being retransmitted.
     * @param destCallNo Destination call number.
     * @param timeStamp Timestamp of the frame.
     * @param oSeqno Outbound sequence number.
     * @param iSeqno Inbound sequence number.
     * @param subclassValueFormat Indicates if subclass value is a 2 power or not.
     * @param subclass Frame subclass.
     * @param data data attached to the frame.
     */
    public ControlFrame (int srcCallNo, boolean retry, int destCallNo, long timeStamp, int oSeqno, 
            int iSeqno, boolean subclassValueFormat, int subclass, byte[] data) {
        super(Frame.CONTROLFRAME_T, srcCallNo, retry, destCallNo, timeStamp, oSeqno, iSeqno, CONTROL_FT, subclassValueFormat, subclass);
        this.data = data;
    }
    
    /**
     * Constructor. 
     * @param buffer bytes representing the control frame
     * @throws FrameException when there is a problem with the construction
     */
    public ControlFrame (byte[] buffer) throws FrameException {
        super(Frame.CONTROLFRAME_T,buffer);
        try {
            data = new byte[buffer.length - FULLFRAME_HEADER_LENGTH];
            System.arraycopy(buffer, FULLFRAME_HEADER_LENGTH, data, 0, data.length);
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Gets the data attached to the control frame
     * @return an array of bytes with the data
     */
    public byte[] getData() {
        return data;
    }
    
    public byte[] serialize() throws FrameException {
        try {
            // First is the serialization of the super (the full frame's common heather)
            byte[] superInBytes = super.serialize();
            // The result is the super's result and the data attached in the control frame
            byte[] result = new byte[superInBytes.length+data.length];
            System.arraycopy(superInBytes, 0, result, 0, superInBytes.length);
            System.arraycopy(data, 0, result, superInBytes.length, data.length);
            return result;
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
}
