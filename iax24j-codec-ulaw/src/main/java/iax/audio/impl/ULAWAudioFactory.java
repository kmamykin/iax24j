package iax.audio.impl;

import iax.audio.AudioFactory;
import iax.audio.Player;
import iax.audio.PlayerException;
import iax.audio.Recorder;
import iax.audio.RecorderException;

public class ULAWAudioFactory implements AudioFactory {
    /**
     * ULAW codec.
     */
    public static final long ULAW_V = 4;
    /**
     * Voice frame with codec ULAW
     */
    public static final int ULAW_SC = 4;

	public Player createPlayer() throws PlayerException {
		return new ULAWPlayer();
	}

	public Recorder createRecorder() throws RecorderException {
		return new ULAWRecorder();
	}

	public long getCodec() {
		return ULAW_V;
	}

	public int getCodecSubclass() {
		return ULAW_SC;
	}

}
