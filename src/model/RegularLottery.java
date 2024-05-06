package model;

import java.util.Collections;
import java.util.LinkedList;
import databasen.LiveUpdateHandler;
import databasen.NameListGetters;

public class RegularLottery extends Lottery {

	private boolean firstName = true;
	private final boolean totalRandom;

	public RegularLottery(String cl, int grp, boolean totalRandomWithAll){
		super(cl, grp, totalRandomWithAll ? "NA" : "NP");
		totalRandom  = totalRandomWithAll;
		startNames = totalRandomWithAll ? NameListGetters.getNamesRegular(cl,grp) : NameListGetters.getNamesRegularLowestOrder(cl,grp);
		currentNames = new LinkedList<>(startNames);
		System.out.println("Regularobjekt skapas med " + startNames.size() + " namn");
	}

	@Override
	public LinkedList<String> reloadNames() {
		LinkedList<String> newList = new LinkedList<>(startNames);
		Collections.shuffle(newList);
		return newList;
	}

	@Override
	public void updateDatabase(String studentName, int answer){
		if (totalRandom) return;
		LiveUpdateHandler.updateTotal(studentName, firstName, className, groupNr);
		firstName = false;
	}
}