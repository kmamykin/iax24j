package iax.protocol.call.command.recv;

import iax.protocol.call.Call;
import iax.protocol.call.command.send.CallCommandSendFacade;
import iax.protocol.frame.ControlFrame;
import iax.protocol.frame.DTMFFrame;
import iax.protocol.frame.Frame;
import iax.protocol.frame.MiniFrame;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.frame.VoiceFrame;
import iax.protocol.util.FrameUtil;

/**
 * Facade of the commands valids when a frame is received. 
 * Uses the CallSendCommandFacade to send the frames when a response is needed.
 */
public class CallCommandRecvFacade {

    private CallCommandRecvFacade() {}
    
    /**
     * Handling an accept by sending an ack
     * @param call the call from wich the accept was received
     * @param acceptFrame the accept frame
     */
	public static void accept(Call call, ProtocolControlFrame acceptFrame) {
        int [] repliedSubclasses = FrameUtil.getReplySubclasses((acceptFrame.getSubclass()));
        for (int i=0; i<repliedSubclasses.length; i++)    
            call.repliedFrame(repliedSubclasses[i]);
        CallCommandSendFacade.ack(call, acceptFrame);
	}
	
    /**
     * Handling an ack by calling the method ackedFrame of the call
     * @param call the call from wich the accept was received
     * @param ackFrame the ack frame
     */
	public static void ack(Call call, ProtocolControlFrame ackFrame) {
        call.ackedFrame(ackFrame.getTimestamp());
	}

    /**
     * Handling an answer by calling the method startRecorder of the call and sending an ack
     * @param call the call from wich the accept was received
     * @param answerFrame the answer frame
     */
	public static void answer(Call call, ControlFrame answerFrame) {
		call.answeredCall();
        CallCommandSendFacade.ack(call, answerFrame);
    }

     /**
     * Handling an authorization request by sending an authorization reply
     * @param call the call from which the authorization request was received
     * @param authReqFrame the authorization request frame
     */
	public static void authReq(Call call, ProtocolControlFrame authReqFrame) {
        int [] repliedSubclasses = FrameUtil.getReplySubclasses((authReqFrame.getSubclass()));
        for (int i=0; i<repliedSubclasses.length; i++)    
            call.repliedFrame(repliedSubclasses[i]);
        CallCommandSendFacade.authRep(call, authReqFrame);
	}

    /**
     * Handling an hangup by sending an ack
     * @param call the call from which the hangup was received
     * @param hangupFrame the hangup frame
     */
	public static void hangup(Call call, ProtocolControlFrame hangupFrame) {
        CallCommandSendFacade.ack(call, hangupFrame);
	}

    
    /**
     * Handling a lag request by sending an lag reply
     * @param call the call from which the lag request was received
     * @param lagrqFrame the lag request frame
     */
    public static void lagrq(Call call, ProtocolControlFrame lagrqFrame) {
        CallCommandSendFacade.lagrp(call, lagrqFrame);
    }
    
    /**
     * Handling a received call by sending an ack
     * @param call the call from which the lag request was received
     * @param recvCallFrame the received new call frame
     */
    public static void newCall(Call call, ProtocolControlFrame recvCallFrame) {
        CallCommandSendFacade.ack(call, recvCallFrame);
        CallCommandSendFacade.ringing(call);
    }
    
    /**
     * Handling a ping by sending a pong
     * @param call the call from which the ping was received
     * @param pingFrame the ping frame
     */
    public static void ping(Call call, ProtocolControlFrame pingFrame) {
        CallCommandSendFacade.pong(call, pingFrame);
    }
    
    /**
     * Handling a pong by sending an ack
     * @param call the call from wich the pong was received
     * @param pongFrame the pong frame
     */
	public static void pong(Call call, ProtocolControlFrame pongFrame) {
        int [] repliedSubclasses = FrameUtil.getReplySubclasses((pongFrame.getSubclass()));
        for (int i=0; i<repliedSubclasses.length; i++)    
            call.repliedFrame(repliedSubclasses[i]);
        CallCommandSendFacade.ack(call, pongFrame);
	}
    
    /**
     * Handling a proceeding by sending an ack
     * @param call the call from wich the proceeding was received
     * @param proceedingFrame the proceeding frame
     */
	public static void proceeding(Call call, ControlFrame proceedingFrame) {
        CallCommandSendFacade.ack(call, proceedingFrame);
	}

    /**
     * Handling a reject by sending an ack
     * @param call the call from wich the reject was received
     * @param rejectFrame the reject frame
     */
	public static void reject(Call call, ProtocolControlFrame rejectFrame) {
        int [] repliedSubclasses = FrameUtil.getReplySubclasses((rejectFrame.getSubclass()));
        for (int i=0; i<repliedSubclasses.length; i++)    
            call.repliedFrame(repliedSubclasses[i]);
        CallCommandSendFacade.ack(call, rejectFrame);
	}

     /**
     * Handling a ringing by sending an ack
     * @param call the call from wich the ringing was received
     * @param ringingFrame the ringing frame
     */
	public static void ringing(Call call, ControlFrame ringingFrame) {
        call.ringingCall();
        CallCommandSendFacade.ack(call, ringingFrame);
	}
    
    /**
     * Handling an unsupported frame
     * @param call the call from which the unsupport was received
     * @param unsupportFrame the unsupported frame
     */
    public static void unsupport(Call call, ProtocolControlFrame unsupportFrame) {
        CallCommandSendFacade.ack(call, unsupportFrame);
    }

    /**
     * Handling a voice full frame by write the audio data and sending an ack
     * @param call the call from wich the voice full frame was received
     * @param voiceFrame the voice full frame
     */
	public static void voiceFullFrame(Call call, VoiceFrame voiceFrame) {
		call.writeAudioIn(voiceFrame.getTimestamp(), voiceFrame.getData(), true);
        CallCommandSendFacade.ack(call, voiceFrame);
	}

	public static void dtmfFullFrame(Call call, DTMFFrame dtmfFrame) {
		if (dtmfFrame.getType() == Frame.DTMFFRAME_T){
	   		System.out.println("dtmfFrame.getSubclass:" + dtmfFrame.getSubclass());
			call.setDTMFCode(String.valueOf(new Character((char)dtmfFrame.getSubclass())));
		}
//		call.writeAudioIn(voiceFrame.getTimestamp(), voiceFrame.getData(), true);
        CallCommandSendFacade.ack(call, dtmfFrame);
	}

    /**
     * Handling a voice mini frame by write the audio data
     * @param call the call from wich the voice mini frame was received
     * @param miniFrame voice the mini frame
     */
	public static void voiceMiniFrame(Call call, MiniFrame miniFrame) {
		call.writeAudioIn(miniFrame.getTimestamp(), miniFrame.getData(), false);
	}
}