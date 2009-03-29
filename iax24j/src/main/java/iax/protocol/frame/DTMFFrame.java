package iax.protocol.frame;


/**
 * DTMF Frame.
 */
public class DTMFFrame extends FullFrame {    
    /**
     * Constructor. Initializes the frame.
     */
    protected DTMFFrame() {
        super();
    }
    
    /**
     * Constructor. Initializes the frame with given values.
     * @param srcCallNo Source call number
     * @param retry Indicates if the frame is being retransmitted.
     * @param destCallNo Destination call number.
     * @param timeStamp Timestamp of the frame.
     * @param oSeqno Output sequence number.
     * @param iSeqno In put sequence number.
     * @param subclass Frame subclass.
     */
    public DTMFFrame (int srcCallNo, boolean retry, int destCallNo, long timeStamp, int oSeqno, 
            int iSeqno, int subclass) {
        super(Frame.DTMFFRAME_T, srcCallNo, retry, destCallNo, timeStamp, oSeqno, iSeqno, DTMF_FT, false, subclass);
    }
    
    /**
     * Constructor. Initializes the frame with given values.
     * @param buffer The buffer that contains the frame bytes.
     * @throws FrameException
     */
    public DTMFFrame(byte[] buffer) throws FrameException {
        super(Frame.DTMFFRAME_T, buffer);
    }
    
    public byte[] serialize() {
            byte[] result = null;
			try {
				result = super.serialize();
			} catch (Exception e) {
				e.printStackTrace();
			}
            return result;
    }
}
