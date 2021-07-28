package model;

import databasen.DatabaseHandler;

import java.util.LinkedList;

public class BPLLottery extends LotteryType {

	public BPLLottery(String cl, int grp) {
		super(cl, grp, "BP");
		System.out.println("Hall���� i konstruktorn!!");
		super.setBPL(true);
	}

	@Override
	public LinkedList<String> reloadNames() {
		
		LinkedList<String> names = DatabaseHandler.getNamesRegular();
		return names;
		
	}

	@Override
	public void updateDatabase(String studentName, int answer) {
		System.out.println("Inget att uppdatera. Detta b�r aldrig skrivas ut. Vi �r i BPLLottery");
	}

}
