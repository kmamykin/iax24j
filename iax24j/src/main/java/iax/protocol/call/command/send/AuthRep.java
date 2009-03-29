package iax.protocol.call.command.send;

import iax.protocol.call.Call;
import iax.protocol.frame.InfoElement;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.util.Converter;
import iax.protocol.util.FrameUtil;

//import org.bouncycastle.crypto.digests.MD5Digest;

/**
 * Sends an authorization request
 */
public class AuthRep implements CallCommandSend {

    // MD5 result size in bytes
    private static final int MD5_SIZE = 16;
    // Call for sending the frame
    private Call call;
    // Authorization request frame that needs an authorization reply frame
    private ProtocolControlFrame authReqFrame;

    /**
     * Constructor
     * @param call call for sending the frame
     * @param authReqFrame authorization request frame that needs an authorization reply frame
     */
    public AuthRep(Call call, ProtocolControlFrame authReqFrame) {
        this.call = call;
        this.authReqFrame = authReqFrame;
    }

    public void execute() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try {
            String userName = authReqFrame.getUserName();
            int auth = authReqFrame.getAuthMethods();
            String challenge = authReqFrame.getChallenge();
            if (userName.equals(call.getPeer().getUserName())) {
                String password = call.getPeer().getPassword();
                ProtocolControlFrame authRepFrame = new ProtocolControlFrame(call.getSrcCallNo(), false, call.getDestCallNo(),
                        call.getTimestamp(), call.getOseqno(), call.getIseqno(), false, ProtocolControlFrame.AUTHREP_SC);
                authRepFrame.setUserName(userName);
                switch (auth) {
                case InfoElement.MD5_RSA_V:
                case InfoElement.MD5_V:
                    password = Converter.byteArrayToHexString(FrameUtil.md5(challenge.getBytes(), password.getBytes()));
                    authRepFrame.setMD5Result(password);
                    break;
                case InfoElement.RSA_V:
                    //TODO: Calcular el RSA del password.
                    // pcf.setRSAResult()
                    break;
                case InfoElement.PLAIN_V:
                    authRepFrame.setMD5Result(password);
                    break;
                default:
                    break;
                }
                call.handleSendFrame(authRepFrame);
            } //TODO else (when isn't our username)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
