package iax.protocol.call.command.send;

import iax.protocol.call.Call;
import iax.protocol.frame.FullFrame;
import iax.protocol.frame.ProtocolControlFrame;

/**
 * Sends a lag reply
 */
public class LagRp implements CallCommandSend {
    
    // Call for sending the frame
    private Call call;
    // Lag request frame that needs the lag reply frame
    private FullFrame lagrqFrame;

    /**
     * Constructor
     * @param call call for sending the frame
     * @param lagrqFrame lag request frame that needs the lag reply frame
     */
    public LagRp(Call call, FullFrame lagrqFrame) {
        this.call = call;
        this.lagrqFrame = lagrqFrame;
    }

    public void execute() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        call.handleSendFrame(new ProtocolControlFrame(call.getSrcCallNo(), 
                false, 
                call.getDestCallNo(), 
                lagrqFrame.getTimestamp(), 
                call.getOseqno(), 
                call.getIseqno(), 
                false, 
                ProtocolControlFrame.LAGRP_SC));
    }

}
