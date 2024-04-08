package filer;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import javax.swing.*;
import databasen.DatabaseHandler2;

public class InitializationHandler {

	private static boolean useDB = false;
	private static final String fileName = "settings.txt";
	private static String dbName = null;

	public InitializationHandler() {
	}

//	public static void readSettings() {
//		System.out.println("Låser settings");
//		useDB = false;
//		try {
//			FileReader fileReader = new FileReader(fileName);
//			BufferedReader b = new BufferedReader(fileReader);
//
//			String line = b.readLine();
//
//			if (line.substring(13).equals("true")) {
//
//				line = b.readLine();
//				dbName = line.substring(8);
//				System.out.println("Läste namn: " + dbName);
//				if (dbName.length() > 0) {
//					System.out.println("Ja, databas med namn");
//					useDB = true;
//				}
//			} else {
//				System.out.println("Nej, ingen databas");
//			}
//		} catch (FileNotFoundException f) {
//			System.out.println("Ingen settingsfil fanns");
//			newInitialazation(null);
//		} catch (IOException ioe) {
//			JOptionPane.showMessageDialog(null, "Oväntat fel i readsettings i init.handler");
//		} catch (Exception e) {
//			System.out.println("FEeeel");
//		}
//	}
//
//	private static void createNewFile(JFrame frame) {
//		int a = JOptionPane.showConfirmDialog(frame, "Vill du använda en databas?");
//
//		if (a == -1 || a == 2) System.exit(0);
//		try {
//			FileWriter fileWriter = new FileWriter(fileName);
//			BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
//			if (a == 0) {
//				useDB = true;
//				do {
//					dbName = JOptionPane.showInputDialog(frame, "Vilket namn har databasen? Om den inte finns skapas en ny.");
//				} while (dbName == null || dbName.length() == 0);
//			} else useDB = false;
//			bufferWriter.write("USE_DATABASE=" + useDB);
//			bufferWriter.newLine();
//			bufferWriter.write("DB_NAME=" + dbName);
//			bufferWriter.newLine();
//			bufferWriter.close();
//			fileWriter.close();
//		} catch (IOException ioe) {
//			JOptionPane.showMessageDialog(frame, "Något gick fel i filhanteringen");
//		}
//	}
//
//	public static boolean useDataBase() {
//		return useDB;
//	}
//
//	public static void newInitialazation(JFrame frame) {
//		createNewFile(frame);
//		String baseURL = DatabaseHandler.getBaseURL();
//
//		System.out.println("Basen belv: " + baseURL);
//		if (useDB) {
//			String mess = SetUpDatabase.setUp(baseURL, dbName) ? "Allt gick bra. Databasen är klar att använda nästa gång." : "Inga nya tabeller. Troligen för att databasen redan fanns.";
//			mess += " Programmet avslutas...";
//			JOptionPane.showMessageDialog(frame, mess);
//		} else
//			JOptionPane.showMessageDialog(null, "Du kör vidare utan databas tydligen. Men du måste starta om programmet!");
//		System.exit(0);
//	}
//
//	private static void disconnectDB() {
//	}
//
//	private static void createNewDB() {
//		String newName = JOptionPane.showInputDialog(null, "Vilket namn har databasen?\n(Om den redan finns kommer den öppnas).");
//		if(newName == null || newName.isEmpty()) return;
////		DatabaseHandler2.getInstance().setDataBase(newName);
//		// Skapa tabeller!!
//		createSettingsFile(newName);
//	}
//
//	public static String getDBName() {
//		return dbName;
//	}
//
//	private static void createSettingsFile(String newDBname) {
//		try {
//			FileWriter fileWriter = new FileWriter(fileName);
//			BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
//			useDB = newDBname != null && !newDBname.isEmpty();
//			bufferWriter.write("USE_DATABASE=" + useDB);
//			bufferWriter.newLine();
//			bufferWriter.write("DB_NAME=" + newDBname);
//			bufferWriter.newLine();
//			bufferWriter.close();
//			fileWriter.close();
//		} catch (IOException ioe) {
//			JOptionPane.showMessageDialog(null, "Något gick fel i filhanteringen");
//		}
//	}

}







