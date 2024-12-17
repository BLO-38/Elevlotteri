package model;

import java.util.Collections;
import java.util.LinkedList;

import databasen.LiveUpdateHandler;
import databasen.NameListGetters;
import databasen.Resetters;

import javax.swing.*;

public class CandyLottery extends Lottery {
	public CandyLottery(String name, int gr) {
		super(name, gr, "G");
		startNames = NameListGetters.getCandyList(name , gr);
		if(startNames == null) {
			JOptionPane.showMessageDialog(null,"Inga namn hittades");
			return;
		}
//		if (startNames.isEmpty()) {
//			startNames = reloadNames();
//		} else {
//			Collections.shuffle(startNames);
//		}
			Collections.shuffle(startNames);
	}

	@Override
	public LinkedList<String> reloadNames() {
//		Resetters.resetCandy(groupNr, className);
        return NameListGetters.getCandyList(className,groupNr);
	}

	@Override
	public void updateDatabase(String studentName, int answer){
		LiveUpdateHandler.updateCandy(studentName, className);
	}
}
