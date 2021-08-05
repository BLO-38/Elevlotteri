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
	// Skriv "inga elever" om listan är tom
	// kolla settingsfilen
	// Sätt antal sessions = en siffra om grupp 2 innehåller 0 st
	// Shuffla gör man inne i getstartlist OCH konstruktorn så att varje avgör själv hur det ska va
	//Todo:
	// Namnet på databasen ska synas
	// Fyll på listan enbart med nya namn efter kontrollfrågorna
	// Borsplacering
	// Remove, lägg i LotteryType, specifik override för vissa
	// Overrida eventieellt remove för vissa
	// Fixa att manuellt kunna ändra CQ (metoden changeCQ)
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
				JOptionPane.showMessageDialog(null, "Märkligt, kunde inte ansluta till databasen. Prova att göra nya databasinställningar. Annars kontakta Lars.");
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
