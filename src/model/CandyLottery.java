package model;

import java.util.Collections;
import java.util.LinkedList;

import databasen.DatabaseHandler;
import databasen.LiveUpdateHandler;
import databasen.Resetters;

public class CandyLottery extends LotteryType {
	public CandyLottery(String name, int gr) {
		super(name, gr, "G");
		startNames = DatabaseHandler.getCandyList();
		if (startNames.size() == 0) {
			startNames = reloadNames();
		} else {
			Collections.shuffle(startNames);
		}
	}

	@Override
	public LinkedList<String> reloadNames() {
		//getDataBaseHandler().resetCandy();
		Resetters.resetCandy();
		LinkedList<String> newNames = DatabaseHandler.getCandyList();
		Collections.shuffle(newNames);
		System.err.println("SHUFFLA SHUFFLA! nya namn");
		return newNames;
	}


	@Override
	public void updateDatabase(String studentName, int answer){
		//getDataBaseHandler().updateCandy(studentName);
		LiveUpdateHandler.updateCandy(studentName);
	}
}
