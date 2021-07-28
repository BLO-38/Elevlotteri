package model;

import databasen.DatabaseHandler;

import java.util.LinkedList;

public class BPLLottery extends LotteryType {

	public BPLLottery(String cl, int grp) {
		super(cl, grp, "BP");
		System.out.println("Hallåååå i konstruktorn!!");
		super.setBPL(true);
	}

	@Override
	public LinkedList<String> reloadNames() {
		
		LinkedList<String> names = DatabaseHandler.getNamesRegular();
		return names;
		
	}

	@Override
	public void updateDatabase(String studentName, int answer) {
		System.out.println("Inget att uppdatera. Detta bör aldrig skrivas ut. Vi är i BPLLottery");
	}

}
