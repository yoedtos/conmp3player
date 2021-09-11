package net.yoedtos.player.core;

import org.apache.log4j.Logger;

import javazoom.jl.player.Player;

public class TimeCounter implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(TimeCounter.class);
	private static final String THREAD_NAME = "CounterThread";
	
	private Player player;
	
	public TimeCounter(Player player) {
		this.player = player;
	}

	@Override
	public void run() {

		while(!player.isComplete()) {
			Integer i = player.getPosition();
			System.out.print("\r\t\t\tElapsed Time: ");
			System.out.printf("%.2f", i*1e-3);
			System.out.print(" s");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				LOGGER.debug(THREAD_NAME + " : " + e.getMessage());
				throw new RuntimeException();
			}
		}
		LOGGER.debug(THREAD_NAME + "Terminated");
	}
}
