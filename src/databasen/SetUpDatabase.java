package databasen;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import java.sql.Connection;

public class SetUpDatabase {
	
	private static Connection connection;
	
	public static boolean setUp(String dbBaseURL, String dbName){
		String dbUrl = dbBaseURL + dbName + ".db";
		if(!connect(dbUrl)) return false;
		if(!createTables()) return false;
		return closeDatabase();
	}
	
	private static boolean connect(String dbURL) {
		// Kolla om den redan finns och isf använd
		try {
			connection = DriverManager.getConnection(dbURL);
			System.out.println("Connectat och klart!");
			return true;
		}
		catch (SQLException s) {
			System.out.println("Kunde inte ansluta till databasen. " + s.getMessage());
		}
		JOptionPane.showMessageDialog(null, "Kunde inte ansluta till databasen");
		return false;
	}
	
	private static boolean closeDatabase() {
		boolean status = false;
		try {
			connection.close();
			System.out.println("Databasen stängd utan problem.");
			status = true;
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Fel vid stängning av databasen!!");
		}
		return status;
	}

	private static boolean createTables() {
		boolean status = false;

		String q1 = "CREATE TABLE student (" +
					"name  TEXT, " +
					"class TEXT, " +
					"grp INTEGER DEFAULT -1, " +
					"total INTEGER DEFAULT 0, " +
					"candy_active TEXT DEFAULT 'y', " +
					// "CQ_ever TEXT DEFAULT 'y', " +
					"CQ_SCORE INTEGER DEFAULT 0, " +
					// "CQ_active TEXT DEFAULT 'y', " +
					"PRIMARY KEY (name,class))";

		String q2 = "CREATE TABLE regular_session (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"class TEXT, " +
					"grp INTEGER DEFAULT -1, " +
					"sessiondate TEXT DEFAULT (datetime('now','localtime')))";

		String q3 = "CREATE TABLE candy (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"sessiondate TEXT DEFAULT (datetime('now','localtime')), " +
					"name  TEXT, " +
					"class TEXT)";

		String q4 = "CREATE TABLE CQ_result (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"name  TEXT, " +
					"class TEXT, " +
					"sessiondate TEXT DEFAULT (datetime('now','localtime')), " +
					"topic TEXT, " +
					"question INTEGER, " +
					"correct TEXT)";
		
		try{
			System.out.println("Då försöker vi skapa tabeller!");
			Statement statement = connection.createStatement();
			statement.executeUpdate(q1);
			statement.executeUpdate(q2);
			statement.executeUpdate(q3);
			statement.executeUpdate(q4);
			statement.close();
			status = true;
			System.out.println("Verkar ha funkat bra att göra tabeller!");
		}
		catch(SQLException s){
			System.out.println(s.toString());
			JOptionPane.showMessageDialog(null, "Funkade inte att införa tabeller konstigt nog.");
		}			
		return status;
	}
}

