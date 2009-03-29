package iax.protocol.user.command;

/**
 * Interface to user commands.
 *
 */
public interface UserCommand extends Runnable {
    
    /**
     * Method to call a user command.
     */
    public void execute();

}
