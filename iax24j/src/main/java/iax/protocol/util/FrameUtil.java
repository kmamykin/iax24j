package iax.protocol.util;

import iax.protocol.frame.ControlFrame;
import iax.protocol.frame.DTMFFrame;
import iax.protocol.frame.Frame;
import iax.protocol.frame.FrameException;
import iax.protocol.frame.FullFrame;
import iax.protocol.frame.MiniFrame;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.frame.VoiceFrame;
import iax.security.MD5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;

/**
 * Utiliy class to work with frames.
 */
public class FrameUtil {
  
    //Posicion de F en bytes
    private static final int F_POS = 0;
    //Posicion de FrameType em bytes
    private static final int FRAMETYPE_POS = 10;
    //MD5 result size in bytes
    private static final int MD5_SIZE = 16;
    //Mapping to reply full frames
    private static final Hashtable replyMap;
    static {
        replyMap = new Hashtable();
        int accept[] = {ProtocolControlFrame.AUTHREP_SC, ProtocolControlFrame.NEW_SC};
        int authreq[] = {ProtocolControlFrame.NEW_SC};
        int pong[] = {ProtocolControlFrame.PING_SC};
        int regack[] = {ProtocolControlFrame.REGREQ_SC};
        int regauth[] = {ProtocolControlFrame.REGREL_SC, ProtocolControlFrame.REGREQ_SC};
        int regrej[] = {ProtocolControlFrame.REGREL_SC, ProtocolControlFrame.REGREQ_SC};
        int reject[] = {ProtocolControlFrame.AUTHREP_SC};
        replyMap.put(new Integer(ProtocolControlFrame.ACCEPT_SC), accept);
        replyMap.put(new Integer(ProtocolControlFrame.AUTHREQ_SC), authreq);
        replyMap.put(new Integer(ProtocolControlFrame.PONG_SC), pong);
        replyMap.put(new Integer(ProtocolControlFrame.REGACK_SC), regack);
        replyMap.put(new Integer(ProtocolControlFrame.REGAUTH_SC), regauth);
        replyMap.put(new Integer(ProtocolControlFrame.REGREJ_SC), regrej);
        replyMap.put(new Integer(ProtocolControlFrame.REJECT_SC), reject);
    }
    
    
    /**
     * Deserialize a frame from its bytes. 
     * @param buffer Frame bytes stored in an array.
     * @return A new frame of suitable type filled with data.
     * @throws FrameException
     */
    public static Frame deserialize(byte buffer[]) throws FrameException {
        ByteBuffer byteBuffer = new ByteBuffer(buffer);
        try {
            boolean full = (((byteBuffer.get16bits(F_POS) & Frame.F_SHORTMASK)==0)?false:true);
            if (!full) {
                return new MiniFrame(buffer);
            } else {
                int frameType = byteBuffer.get8bits(FRAMETYPE_POS);
                switch(frameType) {
                	case FullFrame.DTMF_FT:
                		return new DTMFFrame(buffer);
                    case FullFrame.CONTROL_FT:
                        return new ControlFrame(buffer);
                    case FullFrame.PROTOCOLCONTROL_FT:
                        return new ProtocolControlFrame(buffer);
                    case FullFrame.VOICE_FT:
                        return new VoiceFrame(buffer);
                    case FullFrame.DTMF_BEGIN_FT:
                		return null;
                    default:
                }       throw new FrameException("Frame type unknown type:" + frameType);
            }
        } catch (Exception e) {
            throw new FrameException(e);
        }
    }
    
    /**
     * Computes the md5 result
     * @param challenge challenge in bytes for the md5
     * @param password password in bytes for the md5
     * @return an array of bytes with the md5 result
     */
    public static byte[] md5(byte[] challenge, byte[] password) {
    	MD5 digest = new MD5();
    	byte input[] = new byte[challenge.length + password.length];
    	System.arraycopy(challenge, 0, input, 0, challenge.length);
    	System.arraycopy(password, 0, input, challenge.length, password.length);
    	return digest.digest(input);
    }
    
    /**
     * Gets an array with the subclass values replied by the subclass value passed in args
     * @param recvSubclass the subclass value for making the reply 
     * @return an array with the subclass values replied
     */
    public static int[] getReplySubclasses(int recvSubclass) {
        if (replyMap.containsKey(new Integer(recvSubclass))) 
            return (int[])replyMap.get(new Integer(recvSubclass));
        else return new int[0];
    }
}
