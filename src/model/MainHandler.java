package model;
import java.awt.*;

import databasen.DatabaseHandler2;
import offlineHandling.OfflineHandler;
import view.*;

public class MainHandler {
	// Den 9 april 2024
	public static final String version = "BLO 5.0";
	// Vers 4:
	// Radera klassrum
	// Teacher view

	// Vers 5:
	// Total databese make over!
	public static final Color MY_GREEN = new Color(27, 104, 5);
	public static final Color MY_RED = new Color(0x950606);
	public static final Color MY_BEIGE = new Color(0xE5D496);


	//Todo:
	// Sätt antal sessions = en siffra om grupp 2 innehåller 0 st
	// Felsvar på kf i haNTERA ELEV
	// Varannat kön på BPL
	// Hantera databs bättre
	// Fikalisten
	// Startrutsan alltid framme
	// Ändra så inte klassen är bestämd för hela projektet
	// Flytta "Hantera databasen" till inställningar
	// Kvar: spara EN av getStudents
	// Sätt rubriker på de olika fönstrena
	// Hur är det med session??
	// Alla EXIT ON CLOSE??
	// Gör om med arv i mainmenu
	// Lägga till elever på offlinen??

	public static void main(String[] args) {
		DatabaseHandler2.startDatabase();
		new MainMenu();
	}
}
