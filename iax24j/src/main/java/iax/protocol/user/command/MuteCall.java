package iax.protocol.user.command;

import iax.protocol.call.Call;
import iax.protocol.peer.Peer;

/**
 * Implements the user command that mutes a call.
 */
public class MuteCall implements UserCommand {

	//Current peer
	private Peer peer;
	//Number of the peer to hang the call.
	private String calledNumber;

	/**
	 * Constructor. Initializes the command with suitable values.
	 * @param peer Current peer.
	 * @param calledNumber Number of the peer to hang the call.
	 */
	public MuteCall(Peer peer, String calledNumber) {
		this.peer = peer;
		this.calledNumber = calledNumber;
	}

	public void execute() {
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		try {
			Call call = peer.getCall(calledNumber);
			call.muteCall();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}