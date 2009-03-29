package iax.protocol.peer.command.send;

/**
 * Interface that must be implemented by the peer's commands that sends frames.
 */
public interface PeerCommandSend extends Runnable {

    /**
     * Executes the command to send the especific frame
     */
    public void execute();
    
}
