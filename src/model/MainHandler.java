package model;
import java.awt.*;

import databasen.DatabaseHandler2;
import view.*;

public class MainHandler {
	// Den 9 april 2024
	public static final String version = "BLO 6.0";
	// Vers 4:
	// Radera klassrum
	// Teacher view

	// Vers 5:
	// Total databese make over!
	// Vers 6 den 1 maj 2024
	// Korridorknappar, gruppindelning, offlinekontrollen

	public static final Color MY_GREEN = new Color(27, 104, 5);
	public static final Color MY_RED = new Color(0x950606);
	public static final Color MY_BEIGE = new Color(0xE5D496);


	//Todo:
	// Sätt antal sessions = en siffra om grupp 2 innehåller 0 st
	// Felsvar på kf i haNTERA ELEV
	// Varannat kön på BPL
	// Fikalisten
	// Ändra så inte klassen är bestämd för hela projektet
	// Flytta "Hantera databasen" till inställningar
	// Kvar: spara EN av getStudents
	// Sätt rubriker på de olika fönstrena
	// Hur är det med session??
	// Gör om med arv i mainmenu
	// Lägga till elever på offlinen??

	public static void main(String[] args) {
		DatabaseHandler2.startDatabase();
		new MainMenu();
	}
}
