package model;
import java.awt.*;
import java.util.LinkedList;

import javax.swing.*;

import databasen.DatabaseHandler;
import filer.InitializationHandler;
import view.*;

public class MainHandler {
	// Den 20 feb 2024
	public static final String version = "BLO 4.0";

	public static final Color MY_GREEN = new Color(27, 104, 5);
	public static final Color MY_RED = new Color(0x950606);
	public static final Color MY_BEIGE = new Color(0xE5D496);


	// Sätt antal sessions = en siffra om grupp 2 innehåller 0 st
	//Todo:
	// Felsvar på kf i haNTERA ELEV
	// Resten kanske varannat kön på bordsplaceringen
	// Deleta klass
	// Ändra namn på klass
	// Varannat kön på BPL
	// Fixa instruktionerna
	// Hantera databs bättre
	// Ny klasss med instruktioner

	private LinkedList<String> currentNames = new LinkedList<>();
	private boolean showTakenNames = false;
	boolean isCQ = false;
	private LotteryWindow wind;
	private boolean useDatabase;
	private Lottery lottery;



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
		
		LotteryMenu2 lg = new LotteryMenu2(this,useDatabase);
		lg.startUp(classes);
	}
	
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
	
	public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
//		 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		 //String className = UIManager.getLookAndFeelClassName("Nimbus");
//		 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		UIManager.setLookAndFeel(
//			UIManager.getCrossPlatformLookAndFeelClassName());
//		UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
//		 JFrame.setDefaultLookAndFeelDecorated(true);
		System.out.println("Kör");
		new MainHandler();
	}

	public void startLottery(Lottery lott) {
		System.out.println("Startar lotteriet");
		lottery = lott;
		currentNames = lottery.getStartNames();

		boolean showNumber = lottery.doShowCount();
		showTakenNames = lottery.doSaveNames();
		if(showTakenNames)	DynamicNameViewer.showDynamicList();
		isCQ = lottery.isControlQuestions();
		wind = new LotteryWindow(this, currentNames.size(), showNumber, lottery.getClassName(), isCQ, lottery.getType(), lottery.getScale());
	}
	
	public void closeDatabase(){
		if(useDatabase) DatabaseHandler.closeDatabase();
	}

	public String getDbName(){
		return InitializationHandler.getDBName();
	}
}
