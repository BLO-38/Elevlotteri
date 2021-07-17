package model;
// import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import view.LotteryMenu;
import view.BPLWindow;
import view.DynamicNameViewer;
import view.LotteryWindow;
import databasen.DatabaseHandler;
import filer.InitializationHandler;

public class MainHandler {
	// use database
	// Skriv "inga elever" om listan �r tom
	// kolla settingsfilen
	// S�tt antal sessions = en siffra om grupp 2 inneh�ller 0 st
	// Todo:
	// Namnet p� databasen ska synas
	// Fyll p� listan enbart med nya namn efter kontrollfr�gorna
	// Borsplacering
	
	private LinkedList<String> currentNames = new LinkedList<>();
	private boolean showNumber = true, showTakenNames = false;
	boolean isCQ = false;
	private static int isAbcCQ = 38;
	private DatabaseHandler databaseHandler = null;
	private LotteryWindow wind;
	private final boolean useDatabase;
	private final InitializationHandler ih;
	private LotteryType lottery;


	public MainHandler() {
		ih = new InitializationHandler();
		useDatabase = ih.useDataBase();
		LinkedList<String> classes = null;
		
		if(useDatabase){
			databaseHandler = new DatabaseHandler(ih.getDBName());
			if(databaseHandler.connect())
				classes = databaseHandler.getClasses();
			else {
				int ans = JOptionPane.showConfirmDialog(null, "Kunde inte ansluta till databasen. Vill du g�ra nya inst�llningar?");
				if(ans == 0) ih.newInitialazation();
				System.exit(0);
			}
		}
		
		LotteryMenu lg = new LotteryMenu(this,useDatabase);
		lg.startUp(classes);
	}
	
	public void pickNext(int answer) {
		String newName = currentNames.poll();
	
		if(newName == null) {
			System.out.println("Fanns inget namn kvar...");
			if(isCQ) lottery.updateDatabase(null, answer);
			currentNames = lottery.getStartList();
			//if(!isCQ) Collections.shuffle(currentNames);
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
		if(useDatabase) return databaseHandler.getNamesTemporary(className, gr);
		else return null;
	}
		
	public void startStatsHandling() {
		databaseHandler.showMenu();
	}
	
	public void startLottery(LotteryType lott) {
		System.out.println("Startar lotteriet");
		lottery = lott;
		if(useDatabase)
			databaseHandler.setCurrentClass(lottery.getClassName(), lottery.getGroup());
		lottery.setDataBaseHandler(databaseHandler);	
		currentNames = lottery.getStartList();
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
		if(useDatabase) databaseHandler.closeDatabase();
	}
	public void setNewSettings(){
		ih.newInitialazation();
	}
	public String getDbName(){
		return ih.getDBName();
	}
}
