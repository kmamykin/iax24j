package iax.protocol.call.command.send;

import iax.protocol.call.Call;
import iax.protocol.frame.MiniFrame;
import iax.protocol.frame.VoiceFrame;

/**
 * Sends a voice frame (mini frame or full frame) in GSM format
 */
public class SendVoice implements CallCommandSend {

    // Call for sending the frame
    private Call call;
    // Audio data to send
    private byte[] audioBuffer;

    /**
     * Constructor
     * @param call call for sending the frame
     * @param audioBuffer audio data to send in GSM format
     */
    public SendVoice(Call call, byte[] audioBuffer) {
        this.call = call;
        this.audioBuffer = audioBuffer;
    }

    public void execute() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        // If is the firs voice frame, set that the first voice frame was sended through and send a full frame
        if (call.isFirstVoiceFrameSended()) {
            call.firstVoiceFrameWasSended();
            call.handleSendFrame(new VoiceFrame(call.getSrcCallNo(), false, call.getDestCallNo(), call.getTimestamp(), 
                    call.getOseqno(), call.getIseqno(), false, call.getAudioFactory().getCodecSubclass(), audioBuffer));
        } else {
            // If is not the first voice frame gets the mini frame timestamp
            long timestampMiniFrame = call.getTimestampMiniFrame();
            // If the mini frame timestamp is greater than the maximun for the overflow, reset it and send a full frame
            if (timestampMiniFrame >= Call.TIMESTAMP_MINIFRAME_RESET) {
                call.resetTimestampMiniFrame();
                call.handleSendFrame(new VoiceFrame(call.getSrcCallNo(), false, call.getDestCallNo(), call.getTimestamp(), 
                    call.getOseqno(), call.getIseqno(), false, call.getAudioFactory().getCodecSubclass(), audioBuffer));
            } else {
                // If the mini frame timestamp isn't greater, send a mini frame
                call.handleSendFrame(new MiniFrame(call.getSrcCallNo(), (int)timestampMiniFrame, audioBuffer));
            }
        }
    }

}
