package iax.audio.impl;


import iax.audio.AudioListener;
import iax.audio.Recorder;
import iax.audio.RecorderException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 * GSM audio recorder.
 */
public class GSMRecorder extends Recorder {
	AudioListener al;
	boolean recording = true;
	int buffer_size;
	Thread captureThread;
	AudioFormat pcmFormat;
	AudioFormat gsmFormat;
	AudioFormat.Encoding gsmEncoding;
	TargetDataLine targetDataLine;
	AudioInputStream linearStream;

	/**
	 * Constructor. Initializes recorder.
	 * @throws RecorderException
	 */
	public GSMRecorder() throws RecorderException {
		pcmFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2, 8000.0F, false);
		gsmEncoding = new AudioFormat.Encoding("GSM0610");
		openTargetDataLine();
	}

	private void openTargetDataLine() {
        // ターゲットデータラインを取得する
        DataLine.Info info = new DataLine.Info(TargetDataLine.class,pcmFormat);
		try {
			targetDataLine = (TargetDataLine)AudioSystem.getLine(info);
	        // ターゲットデータラインをオープンする
	        targetDataLine.open(pcmFormat);
			targetDataLine.start();
	        AudioInputStream ais = new AudioInputStream(targetDataLine);
			linearStream = AudioSystem.getAudioInputStream(gsmEncoding, ais);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
        
	}

	public void stop(){
		recording = false;
	}

	public void record(AudioListener al) {
		System.out.println("record called");
		this.al = al;
		buffer_size = 33;
//		buffer_size -= buffer_size % 2;

		recording = true;
		captureThread = new Thread(new CaptureThread());
		captureThread.start();
		
	}

	class CaptureThread extends Thread {
		public void run() {
			byte buffer[] = new byte[buffer_size];

			try{
				while(recording) {
					int count;
					count = linearStream.read(buffer, 0, buffer.length);
					
					if(count > 0) {
						al.listen(buffer,0,count);
					}
					else if (count == 0){
						Thread.sleep(15);
					}
				}
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
