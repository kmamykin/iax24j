package iax.protocol.frame;

import iax.protocol.util.ByteBuffer;

/**
 * Frame. Encapsulate the common functionality of mini and full frames
 */
public abstract class Frame {
    
    /**
     * Mask of the bit 0 of a short, for getting the atributte F
     */
    public static final int F_SHORTMASK = 0x8000;     
    /**
     * Mask of the bits 1 to 15 of a short, for getting the atributte SrcCallNo
     */
    public static final int SRCCALLNO_SHORTMASK = 0x7fff;  
    /**
     * Frame's header size in bytes
     */
    public static final int FRAME_HEADER_LENGTH = 2;  
    /**
     * Frame of type Unknown for doing the cast
     */
    public static final int UNKNOWNFRAME_T = 0;   
    /**
     * Frame of type MiniFrame for doing the cast
     */
    public static final int MINIFRAME_T = 1;
    /**
     * Frame of type ControlFrame for doing the cast
     */
    public static final int CONTROLFRAME_T = 2;
    /**
     * Frame of type ProtocolControlFrame for doing the cast
     */
    public static final int PROTOCOLCONTROLFRAME_T = 3;
    /**
     * Frame of type VoiceFrame for doing the cast
     */
    public static final int VOICEFRAME_T = 4;
    /**
     * Frame of type DTMFFrame for doing the cast
     */
    public static final int DTMFFRAME_T = 5;
    /**
     * Flag for the bit F that says if is a full frame or not
     */
    protected boolean full;
    /**
     * Frame's source call number
     */
    protected int srcCallNo;
    /**
     * Frame's type for doing the cast
     */
    protected int type;

    /**
     * Constructor
     */
    protected Frame() {}
    
    /**
     * Constructor
     * @param type frame's type for doing the cast
     * @param full true if is a full frame, false if not
     * @param srcCallNo frame's source call number
     */
    protected Frame (int type, boolean full, int srcCallNo) {
        this.full = full;
        this.srcCallNo = srcCallNo;
        this.type = type;
    }
    
    /**
     * Constructor
     * @param type frame's type for doing the cast
     * @param buffer bytes representing the frame
     * @throws FrameException when there is a problem with the construction
     */
    protected Frame(int type, byte buffer[]) throws FrameException {
        this.type = type;
        try {
            ByteBuffer byteBuffer = new ByteBuffer(buffer);
            int aux16bits;
            aux16bits = byteBuffer.get16bits();
            full = (((aux16bits & F_SHORTMASK)==0)?false:true);
            srcCallNo = (aux16bits & SRCCALLNO_SHORTMASK);
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Gets the frame's type for doing the cast
     * @return the frame's type for doing the cast
     */
    public int getType() {
        return type;
    }
    /**
     * Gets the bit F of a frame that says if is a full frame of not
     * @return true if is a full frame, false if not
     */
    public boolean getFull() {
        return full;
    }
    /**
     * Gets the frame's source call number
     * @return the frame's source call number
     */
    public int getSrcCallNo() {
        return srcCallNo;
    }

    public byte[] serialize() throws FrameException {
        try {
            // Serialization of the frame's heather common in mini frames and full frames
            // (atributtes F and SrcCallNo)
            ByteBuffer byteBuffer = new ByteBuffer(FRAME_HEADER_LENGTH);
            int aux16bits;
            aux16bits = ((full?F_SHORTMASK:0) + (srcCallNo & SRCCALLNO_SHORTMASK));
            byteBuffer.put16bits(aux16bits);
            return byteBuffer.getBuffer();
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
}