package filer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

import databasen.DatabaseHandler;
import databasen.SetUpDatabase;

import static databasen.DatabaseHandler.*;
// import static databasen.DatabaseHandler.getBaseURL;

public class InitializationHandler {
	
	private FileReader fileReader;
	private FileWriter fileWriter;
	private BufferedWriter bufferWriter;
	private boolean useDB = false;
	private String pwd;
	private final String fileName = "settings.txt";
	private String dbName = null;
	
	public InitializationHandler() {
		int res = readSettingsFromFile();
		if(res == -1) 	createNewFile();
		else if (res == 1) JOptionPane.showMessageDialog(null, 
				"Inställningar fanns men något gick fel. Ny konfiguration rekommenderas.");
	}
	
	private int readSettingsFromFile() {
		int result = 0;
		try {
			fileReader = new FileReader(fileName);
			BufferedReader b = new BufferedReader(fileReader);
			
			String line = b.readLine();
			
			if(line.substring(13).equals("true")) {
				System.out.println("Ja, databas");
				useDB = true;
				// line = b.readLine();
				// pwd = line.substring(9);
				// System.out.println("Ja, databas. Lösenord: ---" + pwd + "---");
				line = b.readLine();
				dbName = line.substring(8);
				System.out.println("Läste namn: " + dbName);
			}
			else {
				useDB = false;
				System.out.println("Nej, ingen databas");
			}
		}
		catch (FileNotFoundException f){
			result = -1;
		}
		catch (IOException ioe){
			result = 1;
		}
		return result;
	}
	
	
	private void createNewFile() {
		int a = JOptionPane.showConfirmDialog(null, "Kommer du använda en databas?");
		System.out.println("a= " + a);
		if(a == -1 || a == 2) System.exit(0);
		try {
			fileWriter = new FileWriter(fileName);
			bufferWriter = new BufferedWriter(fileWriter);
			
			if(a == 0) {
				useDB = true;
				// pwd = JOptionPane.showInputDialog("Vilket lösenord har du till databasen?");
				do {
					dbName = JOptionPane.showInputDialog("Vilket namn har databasen? Om den inte hittas skapas en ny.");
				} while (dbName == null || dbName.length() == 0);
			}
			else useDB = false;
			bufferWriter.write("USE_DATABASE="+useDB);
			bufferWriter.newLine();
			bufferWriter.write("DB_NAME="+dbName);
			bufferWriter.newLine();
			bufferWriter.close();
			fileWriter.close();
		}
		catch(IOException ioe) {
			JOptionPane.showMessageDialog(null, "Något gick fel i filhanteringen");
		}
	}

	public boolean useDataBase() {
		return useDB;
	}
	
	public void newInitialazation() {
		createNewFile();
		// String baseURL = getBaseURL();
		String baseURL = DatabaseHandler.getBaseURL();
		System.out.println("Basen belv: " + baseURL);
		if(useDB) {
			int b = JOptionPane.showConfirmDialog(null, "Vill du installera nya tabeller?");
			if(b==0) {
				if(SetUpDatabase.setUp(baseURL, dbName))
					JOptionPane.showMessageDialog(null, "Allt gick bra. Databasen är klar.");
			}
		}
	}
	
	public String getDBName(){
		return dbName;
	}

}
