package iax.protocol.call.command.send;

import iax.protocol.call.Call;
import iax.protocol.frame.FullFrame;
import iax.protocol.frame.ProtocolControlFrame;

/**
 * Facade of the commands valids for sending a frame
 * Delegates its funcionality in a specific CommandSend.
 */
public class CallCommandSendFacade {

    /**
     * Sends an ack delegating in the Ack command send 
     * @param call call for sending the frame
     * @param fullFrame full frame that needs an ack
     */
    public static void ack(Call call, FullFrame fullFrame) {
    	(new Ack(call, fullFrame)).execute();
    }
    
    /**
     * Sends an accept delegating in the Accept command send 
     * @param call call for sending the frame
     */
    public static void accept(Call call) {
        (new Accept(call)).execute();
    }
    
    /**
     * Sends an authorization reply delegating in the AuthRep command send 
     * @param call call for sending the frame
     * @param authReqFrame authorization request frame that needs an authorization reply frame
     */
    public static void authRep(Call call, ProtocolControlFrame authReqFrame) {
        (new AuthRep(call, authReqFrame)).execute();
    }

    /**
     * Sends an hangup delegating in the Hangup command send 
     * @param call call for sending the frame
     */
    public static void hangup(Call call) {
    	(new Hangup(call)).execute();
    }
    
    /**
     * Sends a lag reply frame delegating in the LagRp command send 
     * @param call call for sending the frame
     * @param lagRpFrame lag request frame that needs the lag reply frame
     */
    public static void lagrp(Call call, ProtocolControlFrame lagRpFrame) {
        (new LagRp(call, lagRpFrame)).execute();
    }
    
    /**
     * Sends a ping delegating in the Ping command send 
     * @param call call for sending the frame
     */
    public static void ping(Call call) {
        (new Ping(call)).execute();
    }
    
    /**
     * Sends a pong delegating in the Pong command send 
     * @param call call for sending the frame
     * @param pingFrame ping frame that needs a pong frame
     */
    public static void pong(Call call, ProtocolControlFrame pingFrame) {
        (new Pong(call, pingFrame)).execute();
    }

    /**
     * Sends a ringing delegating in the Ringing command send 
     * @param call call for sending the frame
     */
    public static void ringing(Call call) {
        (new Ringing(call)).execute();
    }

    /**
     * Sends a voice frame delegating in the SendVoice command send 
     * @param call call for sending the frame
     * @param audioBuffer audio data to send in GSM format
     */
    public static void sendVoice(Call call, byte[] audioBuffer) {
        (new SendVoice(call, audioBuffer)).execute();
    }
    
    /**
     * Sends an unsupported frame for a fullFrame received that is not supported
     * @param call call for sending the frame
     * @param fullFrame full frame not supported that needs an unsupported frame
     */
    public static void unsupport(Call call, ProtocolControlFrame fullFrame) {
        (new Unsupport(call, fullFrame)).execute();
    }
}