package model;

import java.util.Collections;
import java.util.LinkedList;

import filer.FileHandler;

public class FileLottery extends LotteryType {
	private LinkedList<String> names = null;
	boolean first = true;
	
	public FileLottery(){
		super("Lotteri",0,"F");
	}

	@Override
	public LinkedList<String> reloadNames() {
		if(first){
			first = false;
			names = FileHandler.readStudents();
			if(names == null) System.exit(0);
		}
		Collections.shuffle(names);
		return new LinkedList<String>(names);
	}

	@Override
	public void updateDatabase(String studentName, int answer){
		return;
	}

}
