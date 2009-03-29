package iax.protocol.frame;

import iax.protocol.util.ByteBuffer;

public abstract class FullFrame extends Frame {
  
	/**
     * Full frame attribute FrameType: DTMF
     */
    public static final int DTMF_FT = 1;
    /**
     * Full frame attribute FrameType: VOICE
     */
    public static final int VOICE_FT = 2;
    /**
     * Full frame attribute FrameType: CONTROL
     */
    public static final int CONTROL_FT = 4;
    /**
     * Full frame attribute FrameType: IAXPROTOCOLCONTROL
     */
    public static final int PROTOCOLCONTROL_FT = 6;
	/**
     * Full frame attribute FrameType: DTMF
     */
    public static final int DTMF_BEGIN_FT = 12;
    /**
     * FullFrame's header size in bytes 
     */
    protected static final int FULLFRAME_HEADER_LENGTH = FRAME_HEADER_LENGTH + 10;
    /**
     * Mask of the bit 0 of a short, for getting the attribute R
     */
    private final int R_SHORTMASK = 0x8000;
    /**
     * Mask of the bits 1 to 15 of a short, for getting the attribute DestCallNo
     */
    private final int DESTCALLNO_SHORTMASK = 0x7fff;  
    /**
     * Mask of the bit 0 of a short, for getting the attribute C
     */
    private final int C_BYTEMASK = 0x8000;
    /**
     * Mask of the bits 1 to 7 of a short, for getting the attribute SubClass
     */
    private final int SUBCLASS_BYTEMASK = 0x7fff;
  
    private int retryCount;
    
    /**
     * Flag for the attribute R that says if is a retry frame or not
     */
    protected boolean retry;
    /**
     * FullFrame's destination call number attribute
     */
    protected int destCallNo;
    /**
     * FullFrame's timestamp attribute
     */
    protected long timestamp;
    /**
     * FullFrame's outbound sequence number attribute
     */
    protected int oSeqNo;
    /**
     * FullFrame's inbound sequence number attribute
     */
    protected int iSeqNo;
    /**
     * FullFrame's frame type attribute
     */
    protected int frameType;
    /**
     * Flag for the attribute C that says if the subclass attribute is a power of 2 or not
     */
    protected boolean subclassPowerFormat;
    /**
     * FullFrame's subclass attribute
     */
    protected int subclass;
    

    /**
     * Constructor
     */
    protected FullFrame() {
        super();
    }
    
    /**
     * Constructor
     * @param type frame's type for doing the cast
     * @param srcCallNo Source call number.
     * @param retry Indicates if the frame is being retransmitted.
     * @param destCallNo Destination call number.
     * @param timeStamp Timestamp of the frame.
     * @param oSeqno Outbound sequence number.
     * @param iSeqno Inbound sequence number.
     * @param frameType frame type attribute
     * @param subclassPowerFormat flag for the attribute C that says if the subclass attribute is a power of 2 or not
     * @param subclass subclass
     */
    protected FullFrame (int type, int srcCallNo, boolean retry, int destCallNo, long timeStamp, int oSeqno, 
            int iSeqno, int frameType, boolean subclassPowerFormat, int subclass) {
        super(type,true,srcCallNo);
        this.retry = retry;
        this.destCallNo = destCallNo;
        this.timestamp = timeStamp;
        this.oSeqNo = oSeqno;
        this.iSeqNo = iSeqno;
        this.frameType = frameType;
        this.subclassPowerFormat = subclassPowerFormat;
        this.subclass = subclass;
        this.retryCount = 0;
    }
    /**
     * Constructor. 
     * @param type frame's type for doing the cast
     * @param buffer bytes representing the control frame
     * @throws FrameException when there is a problem with the construction
     */
    protected FullFrame (int type, byte buffer[]) throws FrameException {
        super(type, buffer);
        try {
            byte[] aux = new byte[buffer.length - FRAME_HEADER_LENGTH];
            System.arraycopy(buffer, FRAME_HEADER_LENGTH, aux, 0, aux.length);
            ByteBuffer byteBuffer = new ByteBuffer(aux);
            int aux8bits;
            int aux16bits;
            aux16bits = byteBuffer.get16bits();
            retry = (((aux16bits & R_SHORTMASK)==0)?false:true);
            destCallNo = (aux16bits & DESTCALLNO_SHORTMASK);
            timestamp = byteBuffer.get32bits();
            oSeqNo = byteBuffer.get8bits();
            iSeqNo = byteBuffer.get8bits();
            frameType = byteBuffer.get8bits();
            aux8bits = byteBuffer.get8bits();
            subclassPowerFormat = (((aux8bits & C_BYTEMASK)==0)?false:true);
            if (subclassPowerFormat)
                //The value of the subclass attribute is a power of 2
                subclass = 1 << (aux8bits & SUBCLASS_BYTEMASK); 
            else subclass = (aux8bits & SUBCLASS_BYTEMASK); 
            retryCount = 0;
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }

    /**
     * Gets the R attribute
     * @return true if is a retry, false if not
     */
    public boolean getRetry() {
        return retry;
    }
    
    /**
     * Gets the destination call number attribute
     * @return the destination call number
     */
    public int getDestCallNo() {
        return destCallNo;
    }
    
    /**
     * Gets the timestamp attribute
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * Gets the outbound sequence number attribute
     * @return the outbound sequence number
     */
    public int getOseqno() {
        return oSeqNo;
    }
    
    /**
     * Gets de inbound sequence number attribute
     * @return the inbound sequence number
     */
    public int getIseqno() {
        return iSeqNo;
    }
    
    /**
     * Gets the frame type attribute
     * @return the frame type
     */
    public int getFrameType() {
        return frameType;
    }

    /**
     * Gets the C attribute
     * @return true if the subclass is a power of 2, false if not
     */
    public boolean getSubclassPowerFormat() {
        return subclassPowerFormat;
    }

    /**
     * Gets the subclass attribute
     * @return the subclass attribute
     */
    public int getSubclass() {
        return subclass;
    }

    /**
     * Gets the retry count
     * @return the retry count
     */
    public int getRetryCount() {
        return retryCount;
    }
   
    /**
     * Increases the retry count
     */
    public void incRetryCount() {
        this.retryCount++;
    }
    
    public byte[] serialize() throws FrameException {
        try {
            // First is the serialization of the super (the frame's common heather)
            byte[] superInBytes = super.serialize();
            // The result is the super's result and the data attached in the control frame
            // (FULLFRAME_HEATHER LENGTH is the total, included the FRAME_HEATHER_LENGTH)
            byte[] thisInBytes = new byte[FULLFRAME_HEADER_LENGTH];
            ByteBuffer byteBuffer = new ByteBuffer(FULLFRAME_HEADER_LENGTH-FRAME_HEADER_LENGTH);
            int aux8bits;
            int aux16bits;
            aux16bits = ((retry?R_SHORTMASK:0) + (destCallNo & DESTCALLNO_SHORTMASK));
            byteBuffer.put16bits(aux16bits);
            byteBuffer.put32bits(timestamp);
            byteBuffer.put8bits(oSeqNo);
            byteBuffer.put8bits(iSeqNo);
            byteBuffer.put8bits(frameType);
            aux8bits = ((subclassPowerFormat?1:0) + (subclass & SUBCLASS_BYTEMASK));
            byteBuffer.put8bits(aux8bits);
            System.arraycopy(superInBytes, 0, thisInBytes, 0, superInBytes.length);
            System.arraycopy(byteBuffer.getBuffer(), 0, thisInBytes, superInBytes.length, byteBuffer.getBuffer().length);
            return thisInBytes;
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
}