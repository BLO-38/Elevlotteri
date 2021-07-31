package model;
// import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import databasen.DatabaseHandler;
import view.LotteryMenu;
import view.BPLWindow;
import view.DynamicNameViewer;
import view.LotteryWindow;
// import static databasen.DatabaseHandler.*;
import filer.InitializationHandler;

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
	
	private LinkedList<String> currentNames = new LinkedList<>();
	private boolean showNumber = true, showTakenNames = false;
	boolean isCQ = false;
	private static int isAbcCQ = 38;
	// private DatabaseHandler databaseHandler = null;
	private LotteryWindow wind;
	private final boolean useDatabase;
	private final InitializationHandler ih;
	private LotteryType lottery;


	public MainHandler() {
		ih = new InitializationHandler();
		useDatabase = ih.useDataBase();
		LinkedList<String> classes = null;
		// ih.newInitialazation();
		
		if(useDatabase){
			DatabaseHandler.setDatabaseName(ih.getDBName());
			// databaseHandler = new DatabaseHandler(ih.getDBName());
			if(DatabaseHandler.connect())
				classes = DatabaseHandler.getClasses();
			else {
				int ans = JOptionPane.showConfirmDialog(null, "Kunde inte ansluta till databasen. Vill du göra nya inställningar?");
				if(ans == 0) ih.newInitialazation();
				System.exit(0);
			}
			System.out.println("Startat");
			System.out.println(DatabaseHandler.getBaseURL());
			System.out.println(DatabaseHandler.getCurrentClass());
			// System.out.println(DatabaseHandler.get);
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
		System.out.println(isAbcCQ == 0 ?  "Ja godis" : "Nej inte godis");
		new MainHandler();
	}
	
	public LinkedList<String> getNames(String className, int gr) {
		if(useDatabase) return DatabaseHandler.getNamesTemporary(className, gr);
		else return null;
	}
		
	public void startStatsHandling() {
		DatabaseHandler.showMenu();
	}
	
	public void startLottery(LotteryType lott) {
		System.out.println("Startar lotteriet");
		lottery = lott;
		// if(useDatabase)
			// DatabaseHandler.setCurrentClass(lottery.getClassName(), lottery.getGroup());
		// lottery.setDataBaseHandler(DatabaseHandler);
		currentNames = lottery.getStartNames();
		// Collections.shuffle(currentNames);
		// currentNames = lottery.getStartList();
		//NameRemover.typeNames(currentNames);
		
		
		showNumber = lottery.doShowCount();
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
			wind = new LotteryWindow(this, currentNames.size(), showNumber, lottery.getClassName(), isCQ, lottery.getType());
	}
	
	public void closeDatabase(){
		if(useDatabase) DatabaseHandler.closeDatabase();
	}
	public void setNewSettings(){
		ih.newInitialazation();
	}
	public String getDbName(){
		return ih.getDBName();
	}
}
