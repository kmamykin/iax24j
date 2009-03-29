package iax.audio;

public interface AudioFactory {
	public Player createPlayer() throws PlayerException;
	public Recorder createRecorder() throws RecorderException;
	public long getCodec();
	public int getCodecSubclass();
}
