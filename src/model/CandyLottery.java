package model;

import java.util.Collections;
import java.util.LinkedList;

import databasen.LiveUpdateHandler;
import databasen.NameListGetters;
import databasen.Resetters;

public class CandyLottery extends Lottery {
	public CandyLottery(String name, int gr) {
		super(name, gr, "G");
		startNames = NameListGetters.getCandyList(name , gr);
		if (startNames.isEmpty()) {
			startNames = reloadNames();
		} else {
			Collections.shuffle(startNames);
		}
	}

	@Override
	public LinkedList<String> reloadNames() {
		Resetters.resetCandy(groupNr, className);
		LinkedList<String> newNames = NameListGetters.getCandyList(className,groupNr);
		Collections.shuffle(newNames);
		System.err.println("SHUFFLA SHUFFLA! nya namn");
		return newNames;
	}


	@Override
	public void updateDatabase(String studentName, int answer){
		LiveUpdateHandler.updateCandy(studentName, className);
	}
}
