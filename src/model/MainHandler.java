package model;
// import java.util.Collections;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.*;

import databasen.DatabaseHandler;
import filer.InitializationHandler;
import view.*;

public class MainHandler {
	// use database
	// Skriv "inga elever" om listan är tom
	// kolla settingsfilen
	// Sätt antal sessions = en siffra om grupp 2 innehåller 0 st
	// Shuffla gör man inne i getstartlist OCH konstruktorn så att varje avgör själv hur det ska va
	//Todo:
	// Fyll på listan enbart med nya namn efter kontrollfrågorna
	// Remove, lägg i LotteryType, specifik override för vissa
	// Overrida eventieellt remove för vissa
	// databasurlen generisk
	// Flera fönster samtidigt
	// Kontrollfrågestatisktik
	// Kom alla med i CQ-quiz??? trots att jag vald ebort? KOlla också att cqnever funkar
	// Gender färdig
	// Använd classchooser för fler saker
	// cqscore funkar ej
	// Fixa bordsplaceringsvillkor med prövning
	// Eller prövning
	// Ta bort klass



	
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
	
	public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// String className = getLookAndFeelClassName("Nimbus");
		// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// JFrame.setDefaultLookAndFeelDecorated(true);

		new MainHandler();
	}
	
	public LinkedList<String> getNames(String className, int gr) {
		if(useDatabase) return DatabaseHandler.getNamesTemporary(className, gr);
		else return null;
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
