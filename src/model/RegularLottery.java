package model;

import java.util.Collections;
import java.util.LinkedList;
import databasen.LiveUpdateHandler;
import databasen.NameListGetters;
import databasen.SelectHandler;

public class RegularLottery extends Lottery {

	private boolean firstName = true;
	private final boolean totalRandom;

	public RegularLottery(String cl, int grp, boolean totalRandomWithAll){
		super(cl, grp, totalRandomWithAll ? "NA" : "NP");
		totalRandom  = totalRandomWithAll;
		startNames = totalRandomWithAll ? NameListGetters.getNamesRegular(cl,grp) : NameListGetters.getNamesRegularLowestOrder(cl,grp);
		//students = SelectHandler.getStudents(cl,0);
		currentNames = new LinkedList<>(startNames);
		System.out.println("Regularobjekt skapas med " + startNames.size() + " namn");
	}

	@Override
	public LinkedList<String> reloadNames() {
		System.out.println("Listan laddas om och blandas!");
		LinkedList<String> newList = new LinkedList<>(startNames);
		Collections.shuffle(newList);
		return newList;
	}

	@Override
	public void updateDatabase(String studentName, int answer){
		System.out.println("HALLÅ?");
		if (totalRandom) return;
		LiveUpdateHandler.updateTotal(studentName, firstName, className, groupNr);
		firstName = false;
	}
}