package databasen;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class SetUpDatabase2 {

	public static boolean createTables(Connection connection) {
		boolean status = false;
		LinkedList<String> queries = new LinkedList<>();

		queries.add("CREATE TABLE student (" +
					"name  TEXT, " +
					"class TEXT, " +
					"grp INTEGER DEFAULT -1, " +
					"total INTEGER DEFAULT 0, " +
					"candy_active TEXT DEFAULT 'y', " +
					"group_active INTEGER DEFAULT 1, " +
					"cq_score INTEGER DEFAULT 0, " +
					"gender TEXT DEFAULT 'm', " +
					"PRIMARY KEY (name,class))");

		queries.add("CREATE TABLE regular_session (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"class TEXT, " +
					"grp INTEGER DEFAULT -1, " +
					"sessiondate TEXT DEFAULT (datetime('now','localtime')))");

		queries.add("CREATE TABLE candy (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"sessiondate TEXT DEFAULT (datetime('now','localtime')), " +
					"name  TEXT, " +
					"class TEXT)");

		queries.add("CREATE TABLE CQ_result (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"name  TEXT, " +
					"class TEXT, " +
					"sessiondate TEXT DEFAULT (datetime('now','localtime')), " +
					"topic TEXT, " +
					"question INTEGER, " +
					"correct TEXT)");

		queries.add("CREATE TABLE benches (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"class TEXT, " +
					"lesson TEXT, " +
					"benchdata TEXT)");

		queries.add("CREATE TABLE neighbors (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"class TEXT, " +
					"student1 TEXT, " +
					"student2 TEXT)");

		try {
			System.out.println("Då försöker vi skapa tabeller!");
			Statement statement = connection.createStatement();
			for(String q : queries) statement.executeUpdate(q);
			statement.close();
			status = true;
			System.out.println("Verkar ha funkat bra att göra tabeller!");
		}
		catch(SQLException s){
			System.out.println(s.getMessage());
			System.out.println(s.getErrorCode());
			System.out.println(s.getSQLState());
			System.err.println("Funkade INTE att införa tabeller.");
		}			
		return status;
	}
}

