package iax.audio;

/**
 * Audio Player.
 *
 */
public abstract class Player implements Runnable{


	/**
	 * Implements Simple BUFFER
	 */
	public final static int SIMPLE_BUFFER = 0;
	/**
	 * Implements Jitter BUFFER
	 */
	public final static int JITTER_BUFFER = 1;
    

	/**
	 * Constructor. Initializes the player.
	 * @throws PlayerException
	 */
	public Player(int bufferType) throws PlayerException {}

	/**
	 * Starts playing.
	 */
	 public void play() {}

	 /**
	  * Stops playing. 
	  */
	 public void stop() {}

	 /**
	  * Writes audio data in player audio buffer.
	  * @param timestamp Timestamp of the received audio package.
	  * @param data Audio data.
	  * @param absolute if the timestamp absolute or not
	  */
	 public void write(long timestamp, byte[] data, boolean absolute) {}

	 public void run() {}
     
}
