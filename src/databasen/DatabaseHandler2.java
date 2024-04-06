package databasen;

import javax.swing.*;
import java.sql.*;
import java.util.Collections;
import java.util.LinkedList;

public class DatabaseHandler2 {

	private final String baseURL="jdbc:sqlite:C:/sqlite/";
	private Connection connection = null;
	private String dbName;

	private static DatabaseHandler2 instance;

	private DatabaseHandler2() {

	}

	public static DatabaseHandler2 getInstance () {
		if(instance == null) {
			instance = new DatabaseHandler2();
		}
		return instance;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public Connection getConnection() {
		return connection;
	}

	public void closeDatabase() {
		if (connection == null) return;
		try {
			connection.close();
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Fel vid stängning av databasen!!");
		}
	}

	public boolean setDataBase(String name) {
		try {
			if(connection != null) connection.close();
			connection = DriverManager.getConnection(baseURL + name + ".db");
			System.out.println("Connectat och klart!");
			dbName = name;
			return true;
		}
		catch (SQLException s) {
			System.out.println("Kunde inte ansluta till databasen. " + s.getMessage());
		}
		return false;

	}

}