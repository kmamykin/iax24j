package iax.protocol.user.command;

import iax.protocol.call.Call;
import iax.protocol.frame.InfoElement;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.peer.Peer;

/**
 * Implements the user command that starts a new call.
 */
public class NewCall implements UserCommand {

	//Current peer
    private Peer peer;
    //Number (or name of the extension) of the peer to call
    private String calledNumber;

    /**
     * Constructor. Initializes the command with suitable values.
     * @param peer Current peer.
     * @param calledNumber Number of the peer to be called.
     */
    public NewCall(Peer peer, String calledNumber) {
        this.peer = peer;
        this.calledNumber = calledNumber;
    }

    public void execute() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try {
            Call call = peer.newCall(calledNumber);

            ProtocolControlFrame newCallFrame = 
                new ProtocolControlFrame(call.getSrcCallNo(), false, 0, 0L, call.getOseqno(),
                        call.getIseqno(), false, ProtocolControlFrame.NEW_SC);
            newCallFrame.setVersion(InfoElement.IAXVERSION_V);
            newCallFrame.setCalledNumber(calledNumber);
            newCallFrame.setCallingNumber(peer.getUserName());
            newCallFrame.setCapability(call.getAudioFactory().getCodec());
            newCallFrame.setFormat(call.getAudioFactory().getCodec());
            newCallFrame.setUserName(peer.getUserName());

            call.handleSendFrame(newCallFrame);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}