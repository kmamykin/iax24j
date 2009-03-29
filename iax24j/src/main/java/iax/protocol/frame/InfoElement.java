package iax.protocol.frame;

import iax.protocol.util.ByteBuffer;

public class InfoElement {
    /**
     * Number or extension of whom is called.
     */
    public static final int CALLED_NUMBER = 1;
    /**
     * Number or extension of whom is calling.
     */
    public static final int CALLING_NUMBER = 2;
    /**
     * Name of whom is calling.
     */
    public static final int CALLING_NAME = 4;
    /**
     * Context.
     */
    public static final int CALLED_CONTEXT = 5;
    /**
     * User.
     */
    public static final int USERNAME = 6;
    /**
     * Password.
     */
    public static final int PASSWORD = 7;
    /**
     * Current codec.
     */
    public static final int CAPABILITY = 8;
    /**
     * Desired codec.
     */
    public static final int FORMAT = 9;
    /**
     * Desired language.
     */
    public static final int LANGUAGE = 10;
    /**
     * Protocol version.
     */
    public static final int VERSION = 11;
    /**
     * CPE ADSI capability.
     */
    public static final int ADSICPE = 12;
    /**
     * DNID originally dialled.
     */
    public static final int DNID = 13;
    /**
     * Authentication methods.
     */
    public static final int AUTHMETHODS = 14;
    /**
     * Authentication data (challenge) for MD5/RSA.
     */
    public static final int CHALLENGE = 15;
    /**
     * MD5 authentication result (challenge). 
     */
    public static final int MD5_RESULT = 16;
    /**
     * RSA authentication result (challenge).
     */
    public static final int RSA_RESULT = 17;
    /**
     * Apparent peer address.
     */
    public static final int APPARENT_ADDR = 18;
    /**
     * Time to register refresh. 
     */
    public static final int REFRESH = 19;
    /**
     * Dial plan status.
     */
    public static final int DPSTATUS = 20;
    /**
     * Call number from peer.
     */
    public static final int CALLNO = 21;
    /**
     * Cause.
     */
    public static final int CAUSE = 22;
    /**
     * IAX command unknown.
     */
    public static final int UNKNOWN = 23;
    /**
     * Number of waiting messages.
     */
    public static final int MSGCOUNT = 24;
    /**
     * Auto answer request.
     */
    public static final int AUTOANSWER = 25;
    /**
     * Music on hold request.
     */
    public static final int MUSICONHOLD = 26;
    /**
     * Transfer Request ID.
     */
    public static final int TRANSFER_ID = 27;
    /**
     * Referring DNIS.
     */
    public static final int RDNIS = 28;
    /**
     * Provisioning info.
     */
    public static final int PROVISIONING = 29;
    /**
     * AES Provisioning info.
     */
    public static final int AESPROVISIONING = 30;
    /**
     * Date.
     */
    public static final int DATETIME = 31;
    /**
     * Device type.
     */
    public static final int DEVICETYPE = 32;
    /**
     * Service identifier.
     */
    public static final int SERVICEIDENT = 33;
    /**
     * Firmware revision.
     */
    public static final int FIRMWAREVER = 34;
    /**
     * Firmware block description.
     */
    public static final int FWBLOCKDESC = 35;
    /**
     * Firmware block of data.
     */
    public static final int FWBLOCKDATA = 36;
    /**
     * Provisioning Version.
     */
    public static final int PROVVER = 37;
    /**
     * Calling presentation.
     */
    public static final int CALLINGPRES = 38;
    /**
     * Calling type of number.
     */
    public static final int CALLINGTON = 39;
    /**
     * Calling transitory network selection.
     */
    public static final int CALLINGTNS = 40;
    /**
     * Supported Sampling Rates.
     */
    public static final int SAMPLINGRATE = 41;
    /**
     * Hangup cause.
     */
    public static final int CAUSECODE = 42;
    /**
     * Encryption format.
     */
    public static final int ENCRYPTION = 43;
    /**
     * AES 128-bits encryption key.
     */
    public static final int ENCKEY = 44;
    /**
     * Codec negotiation.
     */
    public static final int CODEC_PREFS = 45;
    /**
     * Jitter received.
     */
    public static final int RR_JITTER = 46;
    /**
     * Loss received.
     */
    public static final int RR_LOSS = 47;
    /**
     * Received frames.
     */
    public static final int RR_PKTS = 48;
    /**
     * Maximum playout delay for recived frames (in ms).
     */
    public static final int RR_DELAY = 49;
    /**
     * Frames dropped
     */
    public static final int RR_DROPPED = 50;
    /**
     * Frames received out of order.
     */
    public static final int RR_OOO = 51;
   
    /**
     * Plain text authentication.
     */
    public final static int PLAIN_V = 1;
    /**
     * MD5 authentication.
     */
    public final static int MD5_V = 2;
    /**
     * MD5/RSA authentication.
     */
    public final static int MD5_RSA_V = 3;
    /**
     * RSA authentication.
     */
    public final static int RSA_V = 4;
    /**
     * IAX protocol version.
     */
    public static final int IAXVERSION_V = 2;
    /**
     * GSM codec.
     */
    public static final long GSM_V = 2;
    
    /**
     * ULAW codec.
     */
    public static final long ULAW_V = 4;
    /**
     * Information Element header length.
     */
    public static final int HEADER_LENGTH = 2;
    /**
     * Maximum size (in bytes) of IE data.
     */
    public static final int MAX_DATA_LENGTH = 255;
    
    //IE identifier.
    private int id;
    //IE data length.
    private int dataLength;
    //IE data.
    private byte data[];
    
//    private static long codec = ULAW_V;
    /**
     * Constructor. Initializes the Information Element with given values.
     * @param id Information element identifier.
     * @param dataLength IE data length.
     * @param data IE data.
     */
    public InfoElement(int id, int dataLength, byte[] data) {
        this.id = id;
        this.dataLength = dataLength;
        this.data = data;
    }

    /**
     * Constructor. Initializes the IE from a byte array.
     * @param buffer The array data.
     * @throws FrameException
     */
    public InfoElement(byte[] buffer) throws FrameException {
        ByteBuffer byteBuffer = new ByteBuffer(buffer);
        try {
            id = byteBuffer.get8bits();
            dataLength = byteBuffer.get8bits();
            data = byteBuffer.getByteArray();
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Gets the IE identifier.
     * @return The identifier as integer.
     */
    public int getID() {
        return id;
    }

    /**
     * Gets the IE data length.
     * @return The data length as integer.
     */
    public int getDataLength() {
        return dataLength;
    }
    
    /**
     * Gets the IE data.
     * @return The IE data.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Serializes the MiniFrame as a byte array.
     * @return The byte array.
     * @throws FrameException
     */
    public byte[] serialize() throws FrameException {
        try {
            ByteBuffer byteBuffer = new ByteBuffer(HEADER_LENGTH+data.length);
            byteBuffer.put8bits(id);
            byteBuffer.put8bits(dataLength);
            byteBuffer.putByteArray(data);
            return byteBuffer.getBuffer();
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
}