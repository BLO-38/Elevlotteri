package model;
// import java.util.Collections;
import java.util.LinkedList;

import javax.swing.*;

import databasen.DatabaseHandler;
import filer.InitializationHandler;
import view.LotteryMenu;
import view.BPLWindow;
import view.DynamicNameViewer;
import view.LotteryWindow;

public class MainHandler {
	// use database
	// Skriv "inga elever" om listan �r tom
	// kolla settingsfilen
	// S�tt antal sessions = en siffra om grupp 2 inneh�ller 0 st
	// Shuffla g�r man inne i getstartlist OCH konstruktorn s� att varje avg�r sj�lv hur det ska va
	//Todo:
	// Namnet p� databasen ska synas
	// Fyll p� listan enbart med nya namn efter kontrollfr�gorna
	// Borsplacering
	// Remove, l�gg i LotteryType, specifik override f�r vissa
	// Overrida eventieellt remove f�r vissa
	// Fixa att manuellt kunna �ndra CQ (metoden changeCQ)
	// databasurlen generisk
	
	private LinkedList<String> currentNames = new LinkedList<>();
	private boolean showTakenNames = false;
	boolean isCQ = false;
	private LotteryWindow wind;
	private boolean useDatabase;
	private LotteryType lottery;


	public MainHandler() {
		InitializationHandler.readSettings();
		useDatabase = InitializationHandler.useDataBase();

		LinkedList<String> classes = null;

		if(useDatabase){
			DatabaseHandler.setDatabaseName(InitializationHandler.getDBName());
			if(DatabaseHandler.connect()) {
				classes = DatabaseHandler.getClasses();
				if (classes.size() == 0) {
					JOptionPane.showMessageDialog(null, "Obs inga klasser fanns i databasen");
				}
			} else {
				JOptionPane.showMessageDialog(null, "M�rkligt, kunde inte ansluta till databasen. Prova att g�ra nya databasinst�llningar. Annars kontakta Lars.");
				useDatabase = false;
			}
			System.out.println("Startat. Med db? " + useDatabase);
		}
		
		LotteryMenu lg = new LotteryMenu(this,useDatabase);
		lg.startUp(classes);
	}
	
	public void pickNext(int answer) {
		String newName = currentNames.poll();
	
		if(newName == null) {
			System.out.println("Fanns inget namn kvar...");
			if(isCQ) lottery.updateDatabase(null, answer);

			currentNames = lottery.reloadNames();
//			if(!isCQ) Collections.shuffle(currentNames);
			newName = currentNames.poll();
		}

		if(showTakenNames) DynamicNameViewer.addName(newName);
		lottery.updateDatabase(newName, answer);
		wind.update(newName,currentNames.size());
		System.out.println("Antal kvar nu: " + currentNames.size());
	}
	
	public static void main(String[] args) {
		new MainHandler();
	}
	
	public LinkedList<String> getNames(String className, int gr) {
		if(useDatabase) return DatabaseHandler.getNamesTemporary(className, gr);
		else return null;
	}

	public void startLottery(LotteryType lott) {
		System.out.println("Startar lotteriet");
		lottery = lott;
		currentNames = lottery.getStartNames();

		boolean showNumber = lottery.doShowCount();
		showTakenNames = lottery.doSaveNames();
		if(showTakenNames)	DynamicNameViewer.showDynamicList();
		if(lottery.isControlQuestions()) {
			isCQ = true;
		}
		if(lottery.isBPL()) {
			System.out.println("Inne i BPL");
			closeDatabase();
			new BPLWindow(lottery.getClassName(), currentNames);
		}

		else
			wind = new LotteryWindow(this, currentNames.size(), showNumber, lottery.getClassName(), isCQ, lottery.getType(), lottery.getScale());
	}
	
	public void closeDatabase(){
		if(useDatabase) DatabaseHandler.closeDatabase();
	}

	public String getDbName(){
		return InitializationHandler.getDBName();
	}
}
