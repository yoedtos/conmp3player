package net.yoedtos.player;

import java.io.File;
import java.util.List;
import org.apache.log4j.Logger;

import net.yoedtos.player.core.Mp3Player;
import net.yoedtos.player.core.PlayerException;


public class MainApp {

	private static final Logger LOGGER = Logger.getLogger(MainApp.class);


	public static void main(String[] args) throws PlayerException {	

		if(args.length == 0) {
			About.printHelp();
		} else {
			LOGGER.debug("arg0: " + args[0]);
			String source = args[0];
			About.printBanner();
			Mp3Player player = Mp3Player.getInstance();
			PlayList playList = PlayList.getinstance(new File(source));
			List<File> list = playList.getList();
			
			for (int i = 0; i < list.size(); i++) {
				player.play(list.get(i));
			}
		}
	}

}
