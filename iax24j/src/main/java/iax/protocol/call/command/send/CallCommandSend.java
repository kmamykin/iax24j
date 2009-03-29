package iax.protocol.call.command.send;

/**
 * Interface that must be implemented by the call's commands that sends frames.
 */
public interface CallCommandSend extends Runnable {
	
    /**
     * Executes the command to send the especific frame
     */
	public void execute();

}
