package iax.audio.impl;


import iax.audio.Player;
import iax.audio.PlayerException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.tritonus.lowlevel.gsm.GSMDecoder;

/**
 * GSM audio player.
 */
public class GSMPlayer extends Player {

	AudioFormat pcmFormat;
	SourceDataLine sourceDataLine;
	GSMDecoder decoder;
	/**
	 * Constructor. Initializes player.
	 * @throws PlayerException
	 */
	public GSMPlayer() throws PlayerException {
		super(Player.JITTER_BUFFER);
		pcmFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2, 8000.0F, false);
		openSourceDataLine();
		decoder = new GSMDecoder();
	}
	private void openSourceDataLine() {
        // ターゲットデータラインを取得する
        DataLine.Info info = new DataLine.Info(SourceDataLine.class,pcmFormat);
		try {
			sourceDataLine = (SourceDataLine)AudioSystem.getLine(info);
	        // ターゲットデータラインをオープンする
	        sourceDataLine.open(pcmFormat);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
        
	}

	public void play() {
		sourceDataLine.start();
	}

	public void stop() {
		sourceDataLine.stop();
	}
	public void write(long timestamp, byte[] audioData, boolean absolute) {
		try{
			synchronized (sourceDataLine) {
				int len = 320;
				byte tempBuffer[] = new byte[len];
				decoder.decode(audioData, 0, tempBuffer, 0, false);
				sourceDataLine.write(tempBuffer, 0, len);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
