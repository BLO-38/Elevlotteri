package model;

import java.util.Collections;
import java.util.LinkedList;
import databasen.DatabaseHandler;
import databasen.LiveUpdateHandler;

public class RegularLottery extends LotteryType {
	
	//private String[] namesToRemove = null;
	private boolean firstName = true;
	// private LinkedList<String> names = null;
	
	//public RegularLottery(String cl, int grp, String[] toRemove){		      aug2018
	public RegularLottery(String cl, int grp){
		super(cl, grp, "N");
		// Prepare names
		startNames = DatabaseHandler.getNamesRegular();
		Collections.shuffle(startNames);
		// NameRemover.typeNames(startNames);
		System.out.println("Ny lista!");
		System.out.println(startNames.size());
		//namesToRemove = toRemove;											  aug2018
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
		//getDataBaseHandler().updateTotal(studentName, firstName);
		LiveUpdateHandler.updateTotal(studentName, firstName);
		firstName = false;
	}

}


/*
if(namesToRemove != null && namesToRemove.length >0) {
	boolean show = false;
	StringBuilder build = new StringBuilder("Borttagna: ");
	String temp;
	
	nameloop:
	for(String n : namesToRemove) {
		temp = n;
		if(temp.length() == 0) continue nameloop;
		temp = temp.substring(0, 1).toUpperCase() + temp.substring(1);
		//Höj till stor bokstav efter mellanslag
		
		while(!names.remove(temp)) {
			temp = JOptionPane.showInputDialog(temp + " kunde inte tas bort. Prova annan stavning. Var noga med stor bokstav.");
			if(temp == null || temp.length() == 0) continue nameloop;
		}
		build.append(temp + ",");
		show = true;
	}
	if(show) JOptionPane.showMessageDialog(null, build.toString());
}
*/