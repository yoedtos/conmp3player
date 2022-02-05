package net.yoedtos.player.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.log4j.Logger;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.tritonus.share.sampled.TAudioFormat;
import org.tritonus.share.sampled.file.TAudioFileFormat;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


public class Mp3Player {

	private static final Logger LOGGER = Logger.getLogger(Mp3Player.class);

	private Player player; 
	private long duration;
	private ExecutorService service;

	private static Mp3Player instance;

	private Mp3Player() { }

	public static Mp3Player getInstance() {
		if(instance == null) {
			instance = new Mp3Player();
		}
		return instance;
	}

	public void close() {
		if (player != null) player.close();
	}

	public void play(File file) throws PlayerException {
		showId3Tag(file);
		showMp3Info(file);

		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			player = new Player(bis);
		} catch (FileNotFoundException e) {
			throw new PlayerException("File Not Found " + file.getName());
		} catch (JavaLayerException e) {
			LOGGER.debug("Error: " + e.getMessage());
			throw new RuntimeException();
		}
	
		service = Executors.newFixedThreadPool(3);
		service.execute(new PlayThread(player));
		service.execute(new ProgressBar(duration*1e-3));
		service.execute(new TimeCounter(player));
		service.shutdown();
		try {
			service.awaitTermination(duration, TimeUnit.MICROSECONDS);
		} catch (InterruptedException e) {
			LOGGER.debug("Error: " + e.getMessage());
			throw new RuntimeException();
		}
	}
		
	private void showMp3Info(File file) throws PlayerException {
		try {
			AudioFileFormat aff = AudioSystem.getAudioFileFormat(file);
			AudioFormat af = aff.getFormat();

			if (aff instanceof TAudioFileFormat) {
				Map<String, Object> p1 = aff.properties();
				duration = (Long)p1.get("duration");
				System.out.print("Duration: ");
				System.out.printf("%.2f", duration*1e-6);
				System.out.print(" s\n");

			}

			if (af instanceof TAudioFormat) {
				Map<String, Object> p2 = af.properties();
				String key = "bitrate";
				Integer i = (Integer) p2.get(key);
				System.out.println("BitRate: " + i + " bps\n");
			}
		} catch (UnsupportedAudioFileException | IOException e) {
			throw new PlayerException("File not supported!");
		}
	}

	private void showId3Tag(File file) throws PlayerException {

		MP3File mp3File = null;

		try {
			mp3File = (MP3File) AudioFileIO.read(file);
		} catch (CannotReadException | IOException | TagException
				| ReadOnlyFileException | InvalidAudioFrameException e) {
			throw new PlayerException("Can't read metadata!");
		}
		
		if (mp3File.hasID3v2Tag()) {

			System.out.println("\nID3 Tag version 2");

			ID3v24Tag tag = mp3File.getID3v2TagAsv24();
			String track = tag.getFirst(ID3v24Frames.FRAME_ID_TRACK);
			String artist = tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST);
			String album = tag.getFirst(ID3v24Frames.FRAME_ID_ALBUM);
			String year = tag.getFirst(ID3v24Frames.FRAME_ID_YEAR);
			String title = tag.getFirst(ID3v24Frames.FRAME_ID_TITLE);
			String genre = tag.getFirst(ID3v24Frames.FRAME_ID_GENRE);
			String lyric = tag.getFirst(ID3v24Frames.FRAME_ID_UNSYNC_LYRICS);

			if(!album.isEmpty()) {
				System.out.println("Album: " + album);	
			}
			if(!track.isEmpty()) {
				System.out.println("Track: " + track);	
			}
			if(!artist.isEmpty()) {
				System.out.println("Artist: " + artist);	
			}
			if(!title.isEmpty()) {
				System.out.println("Title: " + title);	
			}
			if(!year.isEmpty()) {
				System.out.print("Year: " + year + " ");	
			}
			if(!genre.isEmpty()) {
				System.out.println("Genre: " + genre);	
			}
			if(!lyric.isEmpty()) {
				System.out.println("Lyric: " + lyric);	
			}

		} else if (mp3File.hasID3v1Tag()) {

			System.out.println("\nID3 Tag version 1");

			Tag tag = mp3File.getID3v1Tag();

			String artist = tag.getFirst(FieldKey.ARTIST);
			String album = tag.getFirst(FieldKey.ALBUM);
			String title = tag.getFirst(FieldKey.TITLE);
			String track = tag.getFirst(FieldKey.TRACK);
			String year = tag.getFirst(FieldKey.YEAR);
			String genre = tag.getFirst(FieldKey.GENRE);

			if(!album.isEmpty()) {
				System.out.println("Album: " + album);	
			}
			if(!track.isEmpty()) {
				System.out.println("Track: " + track);	
			}
			if(!artist.isEmpty()) {
				System.out.println("Artist: " + artist);	
			}
			if(!title.isEmpty()) {
				System.out.println("Title: " + title);	
			}
			if(!year.isEmpty()) {
				System.out.print("Year: " + year + " ");	
			}
			if(!genre.isEmpty()) {
				System.out.println("Genre: " + genre);	
			}
		} 
	}
}

