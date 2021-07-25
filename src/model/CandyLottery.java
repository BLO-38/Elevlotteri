package model;

import java.util.Collections;
import java.util.LinkedList;

import databasen.DatabaseHandler;
import databasen.LiveUpdateHandler;
import databasen.Resetters;

public class CandyLottery extends LotteryType {
	public CandyLottery(String name, int gr) {
		super(name, gr, "G");
		startNames = this.reloadNames();
	}

	@Override
	public LinkedList<String> reloadNames() {
		
		LinkedList<String> names = DatabaseHandler.getCandyList(); //  getDataBaseHandler().getCandyList();
		
		if(names.size() == 0){
			//getDataBaseHandler().resetCandy();
			Resetters.resetCandy();
			names = DatabaseHandler.getCandyList();
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
