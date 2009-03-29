package iax.audio;

/**
 * Shapes an AudioListener. Classes that receive audio from Recorder
 * must implement this interface.
 *
 */
public interface AudioListener {
	
	/**
	 * Recorder tells listener that has some data to it.
	 * 
	 * @param bytes The data.
	 * @param pos Beginning position of the valid data.
	 * @param length Length of the data array.
	 */
	public void listen(byte[] bytes, int pos, int length);

}
