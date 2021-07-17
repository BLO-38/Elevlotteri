package model;

import java.util.Collections;
import java.util.LinkedList;

import databasen.LiveUpdateHandler;
import databasen.Resetters;

public class CandyLottery extends LotteryType {
	public CandyLottery(String name, int gr) {
		super(name, gr, "G");
	}

	@Override
	public LinkedList<String> getStartList() {
		
		LinkedList<String> names;
		names = getDataBaseHandler().getCandyList();
		
		if(names.size() == 0){
			//getDataBaseHandler().resetCandy();
			Resetters.resetCandy();
			names = getDataBaseHandler().getCandyList();
		}
		Collections.shuffle(names);
		System.err.println("SHUFFLA SHUFFLA!");
		return names;
	}
	
	@Override
	public void updateDatabase(String studentName, int answer){
		//getDataBaseHandler().updateCandy(studentName);
		LiveUpdateHandler.updateCandy(studentName);
	}
}
