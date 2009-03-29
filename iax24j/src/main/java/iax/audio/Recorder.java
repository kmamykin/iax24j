package iax.audio;


/**
 * Audio recorder.
 */
public abstract class Recorder implements Runnable {

	/**
	 * Constructor. Initializes the recorder.
	 * @throws RecorderException
	 */
	public Recorder() throws RecorderException {}

	/**
	 * Starts recording.
	 * @param al Object that is going to process the recorded audio data.
	 */
	public void record(AudioListener al) {}

	/**
	 * Stops recording.
	 */
	public void stop()	{}

	public void run() {}
}
