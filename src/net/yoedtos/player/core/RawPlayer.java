package net.yoedtos.player.core;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.log4j.Logger;


public class RawPlayer {

	private static final Logger LOGGER = Logger.getLogger(RawPlayer.class);
	
	public void play(String filename) throws PlayerException {
			
		File file = new File(filename);
			try {
				AudioInputStream input = AudioSystem.getAudioInputStream(file);
				AudioFormat baseFormat = input.getFormat();
				AudioFormat decodedFormat = new AudioFormat(
						AudioFormat.Encoding.PCM_SIGNED,
						baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
						baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
						false);
				AudioInputStream audioStream = AudioSystem.getAudioInputStream(decodedFormat, input);
				// Play now.
				rawplay(decodedFormat, audioStream);
				input.close();
			} catch (UnsupportedAudioFileException | IOException
					| LineUnavailableException e) {
				LOGGER.debug("Error: " + e.getMessage());
				throw new PlayerException("Unknow Error occurred!");
			}
	}

	private void rawplay(AudioFormat targetFormat, AudioInputStream audioInput)
			throws IOException, LineUnavailableException {

		byte[] buffer = new byte[4096];
		SourceDataLine sourceLine = getLine(targetFormat);
		if (sourceLine != null) {
			sourceLine.start();
			int bytesRead = 0, bytesWritten = 0;
			
			while (bytesRead != -1) {
				bytesRead = audioInput.read(buffer, 0, buffer.length);
				if (bytesRead != -1)
					bytesWritten = sourceLine.write(buffer, 0, bytesRead);
			}
			
			sourceLine.drain();
			sourceLine.stop();
			sourceLine.close();
			audioInput.close();
		}
	}

	private SourceDataLine getLine(AudioFormat audioFormat)
			throws LineUnavailableException {
		
		SourceDataLine source = null;
		DataLine.Info dataInfo = new DataLine.Info(SourceDataLine.class,
				audioFormat);
		source = (SourceDataLine) AudioSystem.getLine(dataInfo);
		source.open(audioFormat);
		return source;
	}
}
