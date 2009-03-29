package iax.audio.impl;

import iax.audio.Player;
import iax.audio.PlayerException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * GSM audio player.
 */
public class ULAWPlayer extends Player {

	SourceDataLine sourceDataLine;

	AudioFormat pcmFormat;
	AudioFormat ulawFormat;

	/**
	 * Constructor. Initializes player.
	 * @throws PlayerException
	 */
	public ULAWPlayer() throws PlayerException {
		super(Player.JITTER_BUFFER);
		ulawFormat = new AudioFormat(AudioFormat.Encoding.ULAW, 8000.0F, 8, 1, 1, 8000.0F, false);
		// リニアPCM 8000Hz 16bit モノラル 符号付き リトルエンディアン
		pcmFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2, 8000.0F, false);
		openSourceDataLine();
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
			InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
			AudioInputStream ais = new AudioInputStream(byteArrayInputStream,ulawFormat,AudioSystem.NOT_SPECIFIED);
			AudioInputStream realNetStream = AudioSystem.getAudioInputStream(pcmFormat, ais);

			synchronized (sourceDataLine) {
				int len = 160;
				int cnt;
				byte tempBuffer[] = new byte[len];

				while((cnt = realNetStream.read(tempBuffer,
								0,tempBuffer.length)) != -1) {
					if(cnt > 0){
						sourceDataLine.write(tempBuffer, 0, cnt);
					} else if (cnt == 0){
						Thread.sleep(15);
					} else
						break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
