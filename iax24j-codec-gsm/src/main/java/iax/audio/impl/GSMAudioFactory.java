package iax.audio.impl;


import iax.audio.AudioFactory;
import iax.audio.Player;
import iax.audio.PlayerException;
import iax.audio.Recorder;
import iax.audio.RecorderException;

public class GSMAudioFactory implements AudioFactory {
    /**
     * GSM codec.
     */
    public static final long GSM_V = 2;
    /**
     * Voice frame with codec GSM
     */
	public static final int GSM_SC = 2;

	public Player createPlayer() throws PlayerException {
		return new GSMPlayer();
	}

	public Recorder createRecorder() throws RecorderException {
		return new GSMRecorder();
	}

	public long getCodec() {
		return GSM_V;
	}

	public int getCodecSubclass() {
		return GSM_SC;
	}

}
