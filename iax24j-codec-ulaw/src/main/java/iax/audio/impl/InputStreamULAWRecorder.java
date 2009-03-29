package iax.audio.impl;

import iax.audio.AudioListener;
import iax.audio.RecorderException;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * GSM audio recorder.
 */
public class InputStreamULAWRecorder {

	/**
	 * Constructor. Initializes recorder.
	 * @throws RecorderException
	 */
	public InputStreamULAWRecorder() {
	}

	public void sendVoice(AudioListener al,InputStream is) {
		try {
			AudioFormat ulawFormat = new AudioFormat(AudioFormat.Encoding.ULAW, 8000.0F, 8, 1, 1, 8000.0F, true);
	        AudioInputStream ais = AudioSystem.getAudioInputStream(is);
	        AudioInputStream linearStream = AudioSystem.getAudioInputStream(ulawFormat, ais);
			int buffer_size = 160;
			byte buffer[] = new byte[buffer_size];
			int count;
			do{
				count = linearStream.read(buffer, 0, buffer.length);
				if(count > 0) {
					al.listen(buffer,0,count);
				}
				Thread.sleep(15);
			}while(count > 0);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
