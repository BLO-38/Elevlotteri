package model;

import java.util.Collections;
import java.util.LinkedList;

import filer.FileHandler;

public class FileLottery extends Lottery {

	public FileLottery(){
		super("Lotteri",0,"F");
		startNames = FileHandler.readStudents();
		if(startNames == null) System.exit(0);
		Collections.shuffle(startNames);
	}

	@Override
	public LinkedList<String> reloadNames() {
		Collections.shuffle(startNames);
		return new LinkedList<>(startNames);
	}

	@Override
	public void updateDatabase(String studentName, int answer){}

}
