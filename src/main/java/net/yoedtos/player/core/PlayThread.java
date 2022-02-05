package net.yoedtos.player.core;

import org.apache.log4j.Logger;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class PlayThread implements Runnable {
	
	private static final Logger LOGGER = Logger.getLogger(PlayThread.class);
	private static final String THREAD_NAME = "PlayerThread";
	
	private Player player;
	
	public PlayThread(Player player) {
		this.player = player;
	}
	
	@Override
	public void run() {
		
		System.out.println("Now Playing...");
		try {
			player.play();
		} catch (JavaLayerException e) {
			LOGGER.debug(THREAD_NAME + " : " + e.getMessage());
			throw new RuntimeException();
		}
		LOGGER.debug(THREAD_NAME + "Terminated");
	}	
}
