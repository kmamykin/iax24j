package iax.protocol.user.command;

import iax.protocol.peer.Peer;

/**
 * Implements the user command that exits from the system
 */
public class Exit implements UserCommand {
    
    private Peer peer;
    
    /**
     * Constructor. Initializes the command with suitable values.
     * @param peer Current peer.
     */
    public Exit(Peer peer) {
        this.peer = peer;
    }
    
    public void execute(){
        Thread t = new Thread(this);
        t.start();
    }
    
    public void run() {
        peer.exit();
    }
}