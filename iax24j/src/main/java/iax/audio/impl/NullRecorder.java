package iax.audio.impl;

import iax.audio.AudioListener;
import iax.audio.Recorder;
import iax.audio.RecorderException;

/**
 * GSM audio recorder.
 */
public class NullRecorder extends Recorder {

	/**
	 * Constructor. Initializes recorder.
	 * @throws RecorderException
	 */
	public NullRecorder() throws RecorderException {
	}

	public void stop(){
	}

	public void record(AudioListener al) {
	}
}
