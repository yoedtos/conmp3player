package net.yoedtos.player.core;

import org.apache.log4j.Logger;

public class ProgressBar implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(ProgressBar.class);

	private static final int BAR_SIZE = 24;
	private static final String THREAD_NAME = "BarThread";
	
	private long sleepTime;

	public ProgressBar(double time) {
		this.sleepTime = (long) (time/BAR_SIZE);
	}

	@Override
	public void run() {
		
		String bar = "========================";
		int count = 0;

		while (count < BAR_SIZE) {
			System.out.print("\r" + bar.substring(0, count++ % bar.length()) + ">");
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				LOGGER.debug(THREAD_NAME + " : " + e.getMessage());
				throw new RuntimeException();
			};
		}
		LOGGER.debug(THREAD_NAME + "Terminated");
	}
}

