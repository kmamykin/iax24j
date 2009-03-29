package iax.protocol.frame;

import iax.protocol.util.ByteBuffer;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Protocol Control Frame.
 */
public class ProtocolControlFrame extends FullFrame {
    /**
     * IAX Protocol Control Frame, subclass value: NEW
     */
    public static final int NEW_SC = 1;
    /**
     * IAX Protocol Control Frame, subclass value: PING
     */
    public static final int PING_SC = 2;
    /**
     * IAX Protocol Control Frame, subclass value: PONG
     */
    public static final int PONG_SC = 3;
    /**
     * IAX Protocol Control Frame, subclass value: ACK
     */
    public static final int ACK_SC = 4;
    /**
     * IAX Protocol Control Frame, subclass value: HANGUP
     */
    public static final int HANGUP_SC = 5;
    /**
     * IAX Protocol Control Frame, subclass value: REJECT
     */
    public static final int REJECT_SC = 6;
    /**
     * IAX Protocol Control Frame, subclass value: ACCEPT
     */
    public static final int ACCEPT_SC = 7;
    /**
     * IAX Protocol Control Frame, subclass value: AUTHREQ
     */
    public static final int AUTHREQ_SC = 8;
    /**
     * IAX Protocol Control Frame, subclass value: AUTHREP
     */
    public static final int AUTHREP_SC = 9;
    /**
     * IAX Protocol Control Frame, subclass value: INVAL
     */
    public static final int INVAL_SC = 10;
    /**
     * IAX Protocol Control Frame, subclass value: LAGRQ
     */
    public static final int LAGRQ_SC = 11;
    /**
     * IAX Protocol Control Frame, subclass value: LAGRP
     */
    public static final int LAGRP_SC = 12;
    /**
     * IAX Protocol Control Frame, subclass value: REGREQ
     */
    public static final int REGREQ_SC = 13;
    /**
     * IAX Protocol Control Frame, subclass value: REGAUTH
     */
    public static final int REGAUTH_SC = 14;
    /**
     * IAX Protocol Control Frame, subclass value: REGACK
     */
    public static final int REGACK_SC = 15;
    /**
     * IAX Protocol Control Frame, subclass value: REGREJ
     */
    public static final int REGREJ_SC = 16;
    /**
     * IAX Protocol Control Frame, subclass value: REGREL
     */
    public static final int REGREL_SC = 17;
    /**
     * IAX Protocol Control Frame, subclass value: VNAK
     */
    public static final int VNAK_SC = 18;
    /**
     * IAX Protocol Control Frame, subclass value: DIAL
     */
    public static final int DIAL_SC = 21;
    /**
     * IAX Protocol Control Frame, subclass value: QUELCH
     */
    public static final int QUELCH_SC = 28;
    /**
     * IAX Protocol Control Frame, subclass value: UNQUELCH
     */
    public static final int UNQUELCH_SC = 29;
    /**
     * IAX Protocol Control Frame, subclass value: POKE
     */
    public static final int POKE_SC = 30;
    /**
     * IAX Protocol Control Frame, subclass value: UNSUPPORT
     */
    public static final int UNSUPPORT_SC = 33;
    /**
     * IAX Protocol Control Frame, subclass value: TRANSFER
     */
    public static final int TRANSFER_SC = 34;

    //Hasthable that containt frame information elements.
    private Hashtable infoElements;
    
    
    /**
     * Constructor. Initializes the frame.
     */
    protected ProtocolControlFrame() {
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
     * @param subclassValueFormat Indicates if subclass value is a 2 power or not.
     * @param subclass Frame subclass.
     */
    public ProtocolControlFrame (int srcCallNo, boolean retry, int destCallNo, long timeStamp, int oSeqno, 
            int iSeqno, boolean subclassValueFormat, int subclass) {
        super(Frame.PROTOCOLCONTROLFRAME_T, srcCallNo, retry, destCallNo, timeStamp, oSeqno, iSeqno, PROTOCOLCONTROL_FT, subclassValueFormat, subclass);
        this.infoElements = new Hashtable();
    }
    
    /**
     * Constructor. Initializes the frame with given values.
     * @param buffer The buffer that contains the frame bytes.
     * @throws FrameException
     */
    public ProtocolControlFrame(byte[] buffer) throws FrameException {
        super(Frame.PROTOCOLCONTROLFRAME_T, buffer);
        this.infoElements = new Hashtable();
        try {
            byte[] iesBuffer = new byte[buffer.length - FULLFRAME_HEADER_LENGTH];
            System.arraycopy(buffer, FULLFRAME_HEADER_LENGTH, iesBuffer, 0, iesBuffer.length);
            int bytesRemaining = iesBuffer.length;
            //This while iterate the buffer to search information elements. 
            //These are variable in number, type and size. 
            while(bytesRemaining > 0) {
                byte[] aux = new byte[bytesRemaining];
                System.arraycopy(iesBuffer, iesBuffer.length - bytesRemaining, aux, 0, aux.length);
                ByteBuffer byteBuffer = new ByteBuffer(aux);
                int id = byteBuffer.get8bits();
                int dataLength = byteBuffer.get8bits();
                byte[] data = new byte[dataLength];
                System.arraycopy(byteBuffer.getByteArray(), 0, data, 0, data.length);
                infoElements.put(new Integer(id),data);
                bytesRemaining -= InfoElement.HEADER_LENGTH + dataLength;
            }
            int i=1;
            i++;
        } catch (Exception e) {
            throw new FrameException(e);
        }      
    }
    
    /**
     * Gets the authentication method.
     * See IAX draft.
     * @return The value identifier of authentication method information element.
     * @throws FrameException
     */
    public int getAuthMethods() throws FrameException {
        try {
            byte data[] = (byte[])infoElements.get(new Integer(InfoElement.AUTHMETHODS));
            ByteBuffer byteBuffer = new ByteBuffer(data);
            return byteBuffer.get16bits();
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Sets the authentication method.
     * @param authMethods The value identifier of authentication method information element.
     * @throws FrameException
     */
    public void setAuthMethods(int authMethods) throws FrameException {
        try {
            ByteBuffer byteBuffer = new ByteBuffer(ByteBuffer.SIZE_16BITS);
            byteBuffer.put16bits(authMethods);
            infoElements.put(new Integer(InfoElement.AUTHMETHODS), byteBuffer.getBuffer());
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Gets the called number.
     * @return The called number in a String.
     * @throws FrameException
     */
    public String getCalledNumber() throws FrameException {
        try {
            byte data[] = (byte[])infoElements.get(new Integer(InfoElement.CALLED_NUMBER));
            return new String(data);
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Sets the called number.
     * @param calledNumber The called number in a String.
     */
    public void setCalledNumber(String calledNumber) {
        infoElements.put(new Integer(InfoElement.CALLED_NUMBER), calledNumber.getBytes());
    }
    
    /**
     * Gets the calling name.
     * @return The calling name in a String.
     * @throws FrameException
     */
    public String getCallingName() throws FrameException {
        try {
            byte data[] = (byte[])infoElements.get(new Integer(InfoElement.CALLING_NAME));
            return new String(data);
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Sets the calling name.
     * @param callingName The calling name in a String.
     */
    public void setCallingName(String callingName) {
        infoElements.put(new Integer(InfoElement.CALLING_NAME), callingName.getBytes());
    }
    
    /**
     * Gets the calling number.
     * @return The calling number in a String.
     * @throws FrameException
     */
    public String getCallingNumber() throws FrameException {
        try {
            byte data[] = (byte[])infoElements.get(new Integer(InfoElement.CALLING_NUMBER));
            return new String(data);
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Sets the calling number.
     * @param callingNumber The calling number in a String.
     */
    public void setCallingNumber(String callingNumber) {
        infoElements.put(new Integer(InfoElement.CALLING_NUMBER), callingNumber.getBytes());
    }
   
    /** Gets capabilities.
     * @return A long with capabilities.
     * @throws FrameException
     */
    public long getCapability() throws FrameException {
        try {
            byte data[] = (byte[])infoElements.get(new Integer(InfoElement.CAPABILITY));
            ByteBuffer byteBuffer = new ByteBuffer(data);
            return byteBuffer.get32bits();
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Sets capabilities.
     * @param capability A long with capabilities.
     * @throws FrameException
     */
    public void setCapability(long capability) throws FrameException {
        try {
            ByteBuffer byteBuffer = new ByteBuffer(ByteBuffer.SIZE_32BITS);
            byteBuffer.put32bits(capability);
            infoElements.put(new Integer(InfoElement.CAPABILITY), byteBuffer.getBuffer());
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Gets cause.
     * @return The string with the cause.
     * @throws FrameException
     */
    public String getCause() throws FrameException {
    	try {
            byte data[] = (byte[])infoElements.get(new Integer(InfoElement.CAUSE));
            return new String(data);
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Sets the cause information element.
     * @param cause The string with the cause.
     */
    public void setCause(String cause) {
        infoElements.put(new Integer(InfoElement.CAUSE), cause.getBytes());
    }
    
    /**
     * Gets cause code.
     * @return The cause code as an integer.
     * @throws FrameException
     */
    public int getCauseCode() throws FrameException {
        try {
            byte data[] = (byte[])infoElements.get(new Integer(InfoElement.CAUSECODE));
            ByteBuffer byteBuffer = new ByteBuffer(data);
            return byteBuffer.get16bits();
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Sets the cause code.
     * @param causeCode The cause code as an integer.
     * @throws FrameException
     */
    public void setCauseCode(int causeCode) throws FrameException {
        try {
            ByteBuffer byteBuffer = new ByteBuffer(ByteBuffer.SIZE_16BITS);
            byteBuffer.put16bits(causeCode);
            infoElements.put(new Integer(InfoElement.CAUSECODE), byteBuffer.getBuffer());
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Gets challenge.
     * @return The challenge as string.
     * @throws FrameException
     */
    public String getChallenge() throws FrameException {
        try {
            byte data[] = (byte[])infoElements.get(new Integer(InfoElement.CHALLENGE));
            return new String(data);
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Sets the challenge.
     * @param challenge The challenge as string.
     */
    public void setChallenge(String challenge) {
        infoElements.put(new Integer(InfoElement.CHALLENGE), challenge.getBytes());
    }
    
    /**
     * Gets format.
     * @return The format as long.
     * @throws FrameException
     */
    public long getFormat() throws FrameException {
        try {
            byte data[] = (byte[])infoElements.get(new Integer(InfoElement.FORMAT));
            ByteBuffer byteBuffer = new ByteBuffer(data);
            return byteBuffer.get32bits();
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Sets format.
     * @param format The format as long.
     * @throws FrameException
     */
    public void setFormat(long format) throws FrameException {
        try {
            ByteBuffer byteBuffer = new ByteBuffer(ByteBuffer.SIZE_32BITS);
            byteBuffer.put32bits(format);
            infoElements.put(new Integer(InfoElement.FORMAT), byteBuffer.getBuffer());
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Gets MD5 result.
     * @return The MD5 result as string.
     * @throws FrameException
     */
    public String getMD5Result() throws FrameException {
    	try {
            byte data[] = (byte[])infoElements.get(new Integer(InfoElement.MD5_RESULT));
            return new String(data);
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Sets the MD5 result.
     * @param md5 The MD5 result as string.
     * @throws FrameException
     */
    public void setMD5Result(String md5) throws FrameException {
    	infoElements.put(new Integer(InfoElement.MD5_RESULT), md5.getBytes());
    }
    
    /**
     * Sets the music on hold
     * @throws FrameException
     */
    public void setMusicOnHold() throws FrameException {
        infoElements.put(new Integer(InfoElement.MUSICONHOLD), new byte[0]);
    }
    
    /**
     * Gets the register refresh
     * @return The register refresh as an intenger.
     * @throws FrameException
     */
    public int getRefresh() throws FrameException {
        try {
            byte data[] = (byte[])infoElements.get(new Integer(InfoElement.REFRESH));
            ByteBuffer byteBuffer = new ByteBuffer(data);
            return byteBuffer.get16bits();
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Sets the register refresh
     * @param refresh The register refresh as an integer.
     * @throws FrameException
     */
    public void setRefresh(int refresh) throws FrameException {
        try {
            ByteBuffer byteBuffer = new ByteBuffer(ByteBuffer.SIZE_16BITS);
            byteBuffer.put16bits(refresh);
            infoElements.put(new Integer(InfoElement.REFRESH), byteBuffer.getBuffer());
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Gets an unknown subclass
     * @return the cause code as an integer.
     * @throws FrameException
     */
    public int getUnknown() throws FrameException {
        try {
            byte data[] = (byte[])infoElements.get(new Integer(InfoElement.UNKNOWN));
            ByteBuffer byteBuffer = new ByteBuffer(data);
            return byteBuffer.get16bits();
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Sets an unknown subclass
     * @param subclass the unknown subclass as an integer.
     * @throws FrameException
     */
    public void setUnknown(int subclass) throws FrameException {
        try {
            ByteBuffer byteBuffer = new ByteBuffer(ByteBuffer.SIZE_8BITS);
            byteBuffer.put8bits(subclass);
            infoElements.put(new Integer(InfoElement.UNKNOWN), byteBuffer.getBuffer());
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Gets the user name.
     * @return The user name as string.
     * @throws FrameException
     */
    public String getUserName() throws FrameException {
        try {
            byte data[] = (byte[])infoElements.get(new Integer(InfoElement.USERNAME));
            return new String(data);
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Sets the user name.
     * @param userName The user name as string.
     */
    public void setUserName(String userName) {
        infoElements.put(new Integer(InfoElement.USERNAME), userName.getBytes());
    }
    
    /**
     * Gets the version.
     * @return The version as integer.
     * @throws FrameException
     */
    public int getVersion() throws FrameException {
        try {
            byte data[] = (byte[])infoElements.get(new Integer(InfoElement.VERSION));
            ByteBuffer byteBuffer = new ByteBuffer(data);
            return byteBuffer.get16bits();
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Sets the version
     * @param version The version as integer.
     * @throws FrameException
     */
    public void setVersion(int version) throws FrameException {
        try {
            ByteBuffer byteBuffer = new ByteBuffer(ByteBuffer.SIZE_16BITS);
            byteBuffer.put16bits(version);
            infoElements.put(new Integer(InfoElement.VERSION), byteBuffer.getBuffer());
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
   
    public byte[] serialize() {
        try {
            byte[] superInBytes = super.serialize();
            int iesBufferMaxSize = infoElements.size()*InfoElement.HEADER_LENGTH*InfoElement.MAX_DATA_LENGTH;
            byte[] iesBuffer = new byte[iesBufferMaxSize];
            Enumeration keys = infoElements.keys();
            int iesBufferSize = 0;
            for (;keys.hasMoreElements();) {
                Integer id = (Integer)keys.nextElement();
                byte[] data = (byte[])infoElements.get(id);
                int ieLength = InfoElement.HEADER_LENGTH+data.length;
                ByteBuffer byteBuffer = new ByteBuffer(ieLength);
                byteBuffer.put8bits(id.byteValue());
                byteBuffer.put8bits(data.length);
                byteBuffer.putByteArray(data);
                System.arraycopy(byteBuffer.getBuffer(), 0, iesBuffer, iesBufferSize, ieLength);
                iesBufferSize += ieLength;
            }
            
            byte[] result = new byte[superInBytes.length+iesBufferSize];
            System.arraycopy(superInBytes, 0, result, 0, superInBytes.length);
            System.arraycopy(iesBuffer, 0, result, superInBytes.length, iesBufferSize);
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
