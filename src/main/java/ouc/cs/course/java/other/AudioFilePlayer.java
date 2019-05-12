package ouc.cs.course.java.other;

import java.io.File;  
import java.io.IOException;  
  
import javax.sound.sampled.AudioFormat;  
import javax.sound.sampled.AudioInputStream;  
import javax.sound.sampled.AudioSystem;  
import javax.sound.sampled.DataLine;  
import javax.sound.sampled.SourceDataLine;
import javax.swing.SwingUtilities; 

public class AudioFilePlayer {
	private boolean stopped = true;
	private boolean isplaying = false;

	Thread playThread; 
	
	int posreadcount = 0;
	
	private OnProgressChangeListener onProgressChangeListener;
	private OnPlayFinishListener onPlayFinishListener;
	
//	public static void main(String[] args) {
//		final AudioFilePlayer player = new AudioFilePlayer();
//		player.play("D:/xiaomi.ogg");
//	}

	public void play(final String filePath) {
		synchronized (this) {
			stop();
		}
		
		stopped = false;
		
		playThread = new Thread(new Runnable() {
			
			public void run() {
				final File file = new File(filePath);
				float length = file.length();
				
				try {
					AudioInputStream in = AudioSystem.getAudioInputStream(file);

					final AudioFormat outFormat = getOutFormat(in.getFormat());
					final DataLine.Info info = new DataLine.Info(SourceDataLine.class, outFormat);

					final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

					if (line != null) {
						line.open(outFormat);
						line.start();
						in = AudioSystem.getAudioInputStream(outFormat, in);
						
						AudioFormat audioFormat = in.getFormat();
						int frameSize = audioFormat.getFrameSize();
						float frameRate = audioFormat.getFrameRate();
						float lengthInSeconds = ((in.getFrameLength()/frameSize)/frameRate);
						int audioLength = (int) lengthInSeconds;
						
						posreadcount = 0;
//						stream(AudioSystem.getAudioInputStream(outFormat, in), line);
						byte[] buffer = new byte[65536];
						isplaying = true;
						boolean isAborted = false;
						for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
//							System.out.println(n);
							posreadcount = (int) (line.getMicrosecondPosition() / 1000 / 1000);
							line.write(buffer, 0, n);
							if (stopped){
								isAborted = true;
								break;
							}
							SwingUtilities.invokeLater(new Runnable() {
								
								public void run() {
									
									if(onProgressChangeListener != null){
										onProgressChangeListener.onProgress(posreadcount, 0);
									}
								}
							});
						}
						isplaying = false;
						
						if(!isAborted){
							SwingUtilities.invokeLater(new Runnable() {
								
								public void run() {
									if(onPlayFinishListener != null){
										onPlayFinishListener.onPlayFinish();
									}
								}
							});
						}
						
						line.drain();
						line.stop();
					}

				} catch (Exception e) {
					throw new IllegalStateException(e);
				}
			}
		});
		playThread.start();

	}
	
	public void stop(){
		stopped = true;
		while(isplaying){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		if(playThread != null){
//			playThread.interrupt();
//		}
	}

	private AudioFormat getOutFormat(AudioFormat inFormat) {
		final int ch = inFormat.getChannels();
		final float rate = inFormat.getSampleRate();
		return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
	}

	private void stream(final AudioInputStream in, final SourceDataLine line) throws IOException {
//		System.out.println("Audioplayer stream");
//		byte[] buffer = new byte[65536];
//		for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
//			System.out.println(n);
//			line.write(buffer, 0, n);
//			if (stopped){
//				break;
//			}
//		}
//		
//		Thread playThread = new Thread(new Runnable() {
//			
//			public void run() {
//				try {
//
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//		playThread.start();
	}
	
	public boolean isIsplaying() {
		return isplaying;
	}

	public void setIsplaying(boolean isplaying) {
		this.isplaying = isplaying;
	}
	
	public OnProgressChangeListener getOnProgressChangeListener() {
		return onProgressChangeListener;
	}

	public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
		this.onProgressChangeListener = onProgressChangeListener;
	}

	public OnPlayFinishListener getOnPlayFinishListener() {
		return onPlayFinishListener;
	}

	public void setOnPlayFinishListener(OnPlayFinishListener onPlayFinishListener) {
		this.onPlayFinishListener = onPlayFinishListener;
	}

	public interface OnProgressChangeListener{
		public void onProgress(int pos, long posmax);
	}
	
	public interface OnPlayFinishListener{
		public void onPlayFinish();
	}
}
