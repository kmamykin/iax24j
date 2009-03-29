package iax.protocol.peer.command.send;

import iax.protocol.frame.InfoElement;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.peer.Peer;
import iax.protocol.util.Converter;
import iax.protocol.util.FrameUtil;

/**
 * Sends a register request
 */
public class RegReq implements PeerCommandSend {

    private Peer peer;
    private ProtocolControlFrame regauthFrame;

    /**
     * Constructor
     * @param peer peer for sending the frame
     */
    public RegReq(Peer peer) {
        this.peer = peer;
        this.regauthFrame = null;
    }

    /**
     * Constructor
     * @param peer peer for sending the frame
     * @param regauthFrame register auth frame that needs a register req frame
     */
    public RegReq(Peer peer, ProtocolControlFrame regauthFrame) {
        this.peer = peer;
        this.regauthFrame = regauthFrame;
    }

    public void execute() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try {
            if (regauthFrame != null) {
                String userName = regauthFrame.getUserName();
                int auth = regauthFrame.getAuthMethods();
                String challenge = regauthFrame.getChallenge();
                if (userName.equals(peer.getUserName())) {
                    String password = peer.getPassword();
                    ProtocolControlFrame regreqFrame = 
                        new ProtocolControlFrame(Peer.PEER_SRCCALLNO, false, regauthFrame.getSrcCallNo(),
                                peer.getTimestamp(), regauthFrame.getIseqno(), regauthFrame.getOseqno()+1, false,
                                ProtocolControlFrame.REGREQ_SC);
                    regreqFrame.setUserName(userName);
                    regreqFrame.setRefresh(Peer.REGISTER_REFRESH);
                    switch (auth) {
                    case InfoElement.MD5_RSA_V:
                    case InfoElement.MD5_V:
                        password = Converter.byteArrayToHexString(FrameUtil.md5(challenge.getBytes(), password.getBytes()));
                        regreqFrame.setMD5Result(password);
                        break;
                    case InfoElement.RSA_V:
                        //TODO: Calcular el RSA del password.
                        // pcf.setRSAResult()
                        break;
                    case InfoElement.PLAIN_V:
                        regreqFrame.setMD5Result(password);
                        break;
                    default:
                        break;
                    }
                    peer.handleSendFrame(regreqFrame);
                } //TODO else (when isn't our username)
            } else {
                ProtocolControlFrame regreqFrame = 
                    new ProtocolControlFrame(Peer.PEER_SRCCALLNO, 
                            false, 
                            0, 
                            peer.getTimestamp(), 
                            0, 
                            0, 
                            false, 
                            ProtocolControlFrame.REGREQ_SC);
                regreqFrame.setUserName(peer.getUserName());
                regreqFrame.setRefresh(Peer.REGISTER_REFRESH);
                peer.handleSendFrame(regreqFrame);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
