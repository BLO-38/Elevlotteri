package model;

import java.util.Collections;
import java.util.LinkedList;
import databasen.DatabaseHandler;
import databasen.LiveUpdateHandler;

public class RegularLottery extends LotteryType {

	private boolean firstName = true;
	private final boolean totalRandom;

	public RegularLottery(String cl, int grp, boolean totalRandomWithAll){
		super(cl, grp, totalRandomWithAll ? "NA" : "NP");
		totalRandom  = totalRandomWithAll;
		startNames = totalRandomWithAll ? DatabaseHandler.getNamesRegular() : DatabaseHandler.getNamesRegularLowestOrder();
		students = DatabaseHandler.getStudents(cl,0);
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
		if (totalRandom) return;
		LiveUpdateHandler.updateTotal(studentName, firstName);
		firstName = false;
	}
}