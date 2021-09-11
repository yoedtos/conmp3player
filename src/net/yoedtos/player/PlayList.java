package net.yoedtos.player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayList {

	private List<File> list;
	private int counter;
	
	private static PlayList instance;
	
	
	private PlayList(File source) {
		list = new ArrayList<>();
	
		if(source.isDirectory()) {
			scanInside(source);
		} else {
			if(checkFileExtension(source.getName())) {
				list.add(source);
			}
		}
	}
	
	public static PlayList getinstance(File file) {
		if(instance == null) {
			instance = new PlayList(file);
		}
		return instance;
	}
	
	public List<File> getList() {
		Collections.sort(list);
		return list;
	}
	
	private boolean checkFileExtension(String name) {
		if(name.endsWith(".mp3")) {
			return true;	
		} else {
			return false;
		}
	}
	
	private void scanInside(File source) {
		
		for(File file : source.listFiles()) {
			if(file.isFile()) {
				String name = file.getName().toLowerCase();
				if(checkFileExtension(name)) {
					list.add(file);
					counter++;
					System.out.print("\rAdded " + counter + " files");
				}
			} else if(file.isDirectory()) {
				scanInside(file);
			}
		}
	}
}
