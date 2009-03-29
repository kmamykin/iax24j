package iax.phone;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


public class DTMFGenerator {
    private int NUM_BANDS = 10;

    private float frequencyArray[] = {
            697.0F, 770.0F, 852.0F, 941.0F, 1209.0F, 1336.0F, 1477.0F, 1633.0F, 440.0F, 523.25F};
	
    private float Q1[] = new float[NUM_BANDS];
    private float Q2[] = new float[NUM_BANDS];
    private float freqCoeffValueArray[] = new float[NUM_BANDS];

	public void init(float sps){
		for(int i = 0;i < NUM_BANDS;++i){
			Q1[i] = (float)java.lang.Math.sin(0.0F);
			Q2[i] = (float)java.lang.Math.sin(2.0F * java.lang.Math.PI * frequencyArray[i] / sps);
			freqCoeffValueArray[i] = (float) (2*java.lang.Math.cos(2.0F * java.lang.Math.PI * frequencyArray[i] / sps));
		}
	}
	
	public float getLow(String chr){
		int idx = 0;
		if ("1".equals(chr)){
			idx = 0; 
		}else if ("2".equals(chr)){
			idx = 0; 
		}else if ("3".equals(chr)){
			idx = 0; 
		}else if ("4".equals(chr)){
			idx = 1; 
		}else if ("5".equals(chr)){
			idx = 1; 
		}else if ("6".equals(chr)){
			idx = 1; 
		}else if ("7".equals(chr)){
			idx = 2; 
		}else if ("8".equals(chr)){
			idx = 2; 
		}else if ("9".equals(chr)){
			idx = 2; 
		}else if ("*".equals(chr)){
			idx = 3; 
		}else if ("0".equals(chr)){
			idx = 3; 
		}else if ("#".equals(chr)){
			idx = 3; 
		}else if ("Ring".equals(chr)){
			idx = 8; 
		}
		float g;
		g   = Q1[idx];
		Q1[idx] = Q2[idx];
		Q2[idx] = freqCoeffValueArray[idx] * Q1[idx] - g;
		return g;
	}
	
	public float getHigh(String chr){
		int idx = 0;
		if ("1".equals(chr)){
			idx = 4; 
		}else if ("2".equals(chr)){
			idx = 5;
		}else if ("3".equals(chr)){
			idx = 6;
		}else if ("4".equals(chr)){
			idx = 4;
		}else if ("5".equals(chr)){
			idx = 5;
		}else if ("6".equals(chr)){
			idx = 6;
		}else if ("7".equals(chr)){
			idx = 4;
		}else if ("8".equals(chr)){
			idx = 5;
		}else if ("9".equals(chr)){
			idx = 6;
		}else if ("*".equals(chr)){
			idx = 4;
		}else if ("0".equals(chr)){
			idx = 5;
		}else if ("#".equals(chr)){
			idx = 6;
		}else if ("Ring".equals(chr)){
			idx = 9; 
		}
		float g;
		g   = Q1[idx];
		Q1[idx] = Q2[idx];
		Q2[idx] = freqCoeffValueArray[idx] * Q1[idx] - g;
		return g;
	}
	
	public float getDTMF(String chr){
		return getLow(chr) + getHigh(chr);
	}
	
	public static void main(String arg[]){
		DTMFGenerator generator = new DTMFGenerator();
		generator.init(8000.0F);
		AudioFormat pcmFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0F, 8, 1, 1, 8000.0F, true);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class,pcmFormat);
		try {
			SourceDataLine sourceDataLine = (SourceDataLine)AudioSystem.getLine(info);
	        // ターゲットデータラインをオープンする
	        sourceDataLine.open(pcmFormat);
	        sourceDataLine.start();
	        byte buffer[] = new byte[160];
	        String buttons[] = new String[]{"1","2","3","4","5","6","7","8","9","*","0","#"};
	        for(int k = 0;k < buttons.length;++k){
		        for(int j = 0; j < 10;++j){
		        	int available = sourceDataLine.available();
		        	if (available < 1600){
		        		Thread.sleep(200);
		        	}else{
		        		for(int i = 0;i < 160;++i){
		        			buffer[i] = (byte)((generator.getDTMF(buttons[k]) * 128.0F) * 0.1);
		        		}
		        		sourceDataLine.write(buffer, 0, buffer.length);
		        	}
		        }
	        }
	        for(int j = 0; j < 50;++j){
	        	int available = sourceDataLine.available();
	        	if (available < 1600){
	        		Thread.sleep(200);
	        	}else{
	        		if ((j % 2) == 0){
		        		for(int i = 0;i < 160;++i){
		        			buffer[i] = (byte)((generator.getLow("Ring") * 128.0F) * 0.1);
		        		}
	        		}else{
		        		for(int i = 0;i < 160;++i){
		        			buffer[i] = (byte)((generator.getHigh("Ring") * 128.0F) * 0.1);
		        		}
	        		}
	        		sourceDataLine.write(buffer, 0, buffer.length);
	        	}
	        }
	        sourceDataLine.stop();
	        sourceDataLine.close();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
