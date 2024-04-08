package model;
import java.awt.*;
import java.util.LinkedList;

import databasen.DatabaseHandler2;
import view.*;

public class MainHandler {
	// Den 31 mars 2024
	public static final String version = "BLO 4.3";
	// Radera klassrum
	// Teacher view

	public static final Color MY_GREEN = new Color(27, 104, 5);
	public static final Color MY_RED = new Color(0x950606);
	public static final Color MY_BEIGE = new Color(0xE5D496);


	// Sätt antal sessions = en siffra om grupp 2 innehåller 0 st
	//Todo:
	// Felsvar på kf i haNTERA ELEV
	// Ändra namn på klass
	// Varannat kön på BPL
	// Hantera databs bättre
	// Fikalisten
	// Startrutsan alltid framme
	// Ändra så inte klassen är bestämd för hela projektet
	// Flytta "Hantera databasen" till inställningar
	// Kvar: spara EN av getStudents



	private LinkedList<String> currentNames = new LinkedList<>();
	private boolean showTakenNames = false;
	boolean isCQ = false;
	private LotteryWindow wind;
	private Lottery lottery;


	// Nya konstr:
	public MainHandler(String hälsning) {
		System.out.println(hälsning);
		DatabaseHandler2.startDatabase();
		new MainMenu(this);
	}


//	public MainHandler() {
//		InitializationHandler.readSettings();
//		useDatabase = InitializationHandler.useDataBase();
//
//		LinkedList<String> classes = null;
//
//		if(useDatabase){
//			DatabaseHandler.setDatabaseName(InitializationHandler.getDBName());
//			if(DatabaseHandler.connect()) {
//				classes = DatabaseHandler.getClasses();
//				if (classes.isEmpty()) {
//					JOptionPane.showMessageDialog(null, "Obs inga klasser fanns i databasen");
//				}
//			} else {
//				JOptionPane.showMessageDialog(null, "Märkligt, kunde inte ansluta till databasen. Prova att göra nya databasinställningar. Annars kontakta Lars.");
//				useDatabase = false;
//			}
//			System.out.println("Startat. Med db? " + useDatabase);
//		}
//
//		LotteryMenu2 lg = new LotteryMenu2(this,useDatabase);
//		lg.startUp(classes);
//	}
//
	public void pickNext(int answer) {
		String newName = currentNames.poll();
	
		if(newName == null) {
			System.out.println("Fanns inget namn kvar...");
			if(isCQ) lottery.updateDatabase(null, answer);
			currentNames = lottery.reloadNames();
			newName = currentNames.poll();
		}

		if(showTakenNames) DynamicNameViewer.addName(newName);
		lottery.updateDatabase(newName, answer);
		wind.update(newName,currentNames.size());
		System.out.println("Antal kvar nu: " + currentNames.size());
	}
	
	public static void main(String[] args) {
		System.out.println(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		System.out.println(Toolkit.getDefaultToolkit().getScreenSize().getHeight());
//		DatabaseHandler2 ddd = DatabaseHandler2.getInstance();


		new MainHandler("Hej");
	}

	public void startLottery(Lottery lott) {
		lottery = lott;
		currentNames = lottery.getStartNames();

		boolean showNumber = lottery.doShowCount();
		showTakenNames = lottery.doSaveNames();
		if(showTakenNames)	DynamicNameViewer.showDynamicList();
		isCQ = lottery.isControlQuestions();
		wind = new LotteryWindow(this, currentNames.size(), showNumber, lottery.getClassName(), isCQ, lottery.getType(), lottery.getScale());
	}
	
	public void closeDatabase(){
		DatabaseHandler2.closeDatabase();
	}
}
