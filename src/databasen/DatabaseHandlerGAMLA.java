package databasen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import javax.swing.*;

public class DatabaseHandlerGAMLA {

	//private static final String baseURL="jdbc:sqlite:C:\\Program Files\\Sqlite3\\";
	 private static final String baseURL="jdbc:sqlite:C:/sqlite/";
	private static Connection connection = null;
	private static String currentClass = null;
	private static int currentGroup;
	// private static final String[] choices1 = {"Ny klass","Ny elev","Hantera elev","Kolla klass","Elevsvar","Hantera databasen","Hantera grupper","Hantera kön","Avsluta","Kolla grannar"};
	private static String dbName;
	public static final int CORRECT = 1;
	public static final int WRONG = 2;
	public static final int ABSENT = 3;

	public static void setDatabaseName(String n) {
		dbName = n;
	}

	public static String getBaseURL() {
		return baseURL;
	}

	public static void setCurrentClass(String cl, int gr) {
		currentClass = cl;
		currentGroup = gr;
//		LiveUpdateHandler.setClass(currentClass);
	}

	public static String getCurrentClass() {
		return currentClass;
	}

	public static int getCurrentGroup() {
		return currentGroup;
	}

	public static boolean connect() {
		try {
			connection = DriverManager.getConnection(baseURL + dbName + ".db");
			System.out.println("Connectat och klart!");
			return true;
		}
		catch (SQLException s) {
			System.out.println("Kunde inte ansluta till databasen. " + s.getMessage());
		}
		return false;
	}

	public static Connection getConnection() {
		return connection;
	}

	public static void closeDatabase() {
		if (connection == null) return;
		try {
			connection.close();
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Fel vid stängning av databasen!!");
		}
	}

	public static LinkedList<String> getNamesTemporary(String c, int g) {
		currentClass = c;
		currentGroup = g;
		return getNamesRegular();
	}

	public static LinkedList<String> getNamesRegularLowestOrder () {

		LinkedList<String> finalList = new LinkedList<>();
		LinkedList<Integer> scores = new LinkedList<>();

		// Först vilka poäng finns?:
		StringBuilder build1 = new StringBuilder("SELECT DISTINCT total FROM student WHERE class = ?");
		if (currentGroup > 0) build1.append(" AND grp = ?");
		build1.append(" ORDER BY total");

		try {
			ResultSet resultSet;
			PreparedStatement prep = connection.prepareStatement(build1.toString());
			prep.setString(1, currentClass);
			if (currentGroup > 0) prep.setInt(2, currentGroup);
			resultSet = prep.executeQuery();
			while (resultSet.next()) {
				int score = resultSet.getInt("total");
				scores.add(score);
				System.out.println("Vi lägger till " + score + " i scores");
			}
			prep.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Fel i getRegLOwest nr 1 " + e.getMessage());
		}

		System.out.println("Antal olika poäng efter kontroll: " + scores.size());
		System.out.println("...och de är " + scores);

		StringBuilder buildQuery = new StringBuilder("SELECT name FROM student WHERE class = ?");
		buildQuery.append(" AND total = ?");
		if (currentGroup > 0) buildQuery.append(" AND grp = ?");

		for (int score : scores) {

			if (score == -1) continue;

			LinkedList<String> scoreNames = new LinkedList<>();
			try {
				ResultSet resultSet;
				PreparedStatement prep = connection.prepareStatement(buildQuery.toString());
				prep.setString(1, currentClass);
				prep.setInt(2, score);
				if (currentGroup > 0) prep.setInt(3, currentGroup);

				resultSet = prep.executeQuery();
				while (resultSet.next()) {
					String name = resultSet.getString("name");
					scoreNames.add(name);
				}
				prep.close();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Fel i get regular --2: " + e.getMessage());
			}

			Collections.shuffle(scoreNames);
			finalList.addAll(scoreNames);
			System.out.println("Finalen är nu: " + finalList);
		}
		return finalList;

	}

	public static LinkedList<String> getNamesRegular() {
		System.out.println("Klas " + currentClass + " grupp " + currentGroup);
		StringBuilder buildQuery = new StringBuilder("SELECT name FROM student WHERE class = ?");
		if (currentGroup > 0) buildQuery.append(" AND grp = ?");

		LinkedList<String> regNames = new LinkedList<>();
		try {
			ResultSet resultSet;
			PreparedStatement prep = connection.prepareStatement(buildQuery.toString());
			prep.setString(1, currentClass);
			if (currentGroup > 0) prep.setInt(2, currentGroup);

			resultSet = prep.executeQuery();
			while (resultSet.next()) {
				String name = resultSet.getString("name");
				regNames.add(name);
			}
			prep.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Fel i get regular --2: " + e.getMessage());
		}

		Collections.shuffle(regNames);
		return regNames;
	}

	public static LinkedList<Student> getStudents(String className, int group) {


		String query = "SELECT * FROM student WHERE class = ?";
		if (currentGroup>0) query += " AND grp = ?";
		LinkedList<Student> list = new LinkedList<>();

		try {
			ResultSet resultSet;

			PreparedStatement prep = connection.prepareStatement(query);
			prep.setString(1, className);
			if(group > 0) prep.setInt(2, group);
			resultSet = prep.executeQuery();
			while(resultSet.next()) {

				String n = resultSet.getString("name");
				String gender = resultSet.getString("gender");
				String candy = resultSet.getString("candy_active");
				int gr = resultSet.getInt("grp");
				int tot = resultSet.getInt("total");
				int cq = resultSet.getInt("cq_score");
				int[] ans = getResults(n, className);
				Student next = new Student(n, className, gr, tot, candy, cq, gender, ans[0], ans[1]);
				//Student next = new Student(n, className, gr, tot, candy, cq, ans[0], ans[1],candy,gender);
				list.add(next);
			}
			prep.close();
		}
		catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Fel i getList(): " + e.getMessage());
		}
		return list;
	}

	private static int[] getResults(String name, String klass) {
		String query2 = "SELECT correct,COUNT(correct) AS tot FROM CQ_result WHERE name = ? AND class = ? GROUP BY correct";

		int corr = 0, wrong = 0, absent = 0;

		try {
			ResultSet resultSet;
			PreparedStatement prep2 = connection.prepareStatement(query2);
			prep2.setString(1, name);
			prep2.setString(2, klass);
			resultSet = prep2.executeQuery();
			while(resultSet.next()) {
				String answ = resultSet.getString("correct");
				int n = resultSet.getInt("tot");
				switch (answ) {
					case "n" -> wrong = n;
					case "y" -> corr = n;
					case "a" -> absent = n;
					default -> {
						JOptionPane.showMessageDialog(null, "Ska aldrig visas. Fel i hämtning av rätt&fel. Programmet avslutas.");
						System.exit(0);
					}
				}
			}
			prep2.close();
		}
		catch (SQLException e){
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(null, "Fel i getList(): " + e.getMessage());
		}
		return new int[]{corr,wrong,absent};
	}

	public static void setSession() {
		System.out.println("setSESSION");
		System.out.println("... med klasss " + currentClass);
		try {
			String query1 = "INSERT INTO regular_session (class,grp) VALUES (?,?)";
			PreparedStatement prep = connection.prepareStatement(query1);
			prep.setString(1, currentClass);
			prep.setInt(2, currentGroup);
			prep.execute();
			prep.close();
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Ett fel uppstod vid skapande av ny session: " + e.getMessage());
		}
	}

	public static LinkedList<String> getClasses() {
		LinkedList<String> classList = new LinkedList<>();
		try {
			String query = "SELECT DISTINCT class FROM student";
			PreparedStatement prep = connection.prepareStatement(query);
			ResultSet resultSet = prep.executeQuery();
			while(resultSet.next()) {
				String cl = resultSet.getString("class");
				classList.add(cl);
			}
			prep.close();
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Ett fel uppstod vid inläsning av klasser: " + e.getMessage());
		}
		return classList;
	}

	public static LinkedList<String> getCandyList() {

		String query = "SELECT name FROM student WHERE class = ? AND candy_active = ?";
		if (currentGroup>0) query += " AND grp = ?";

		LinkedList<String> list = new LinkedList<>();
		try {
			ResultSet resultSet;

			PreparedStatement prep = connection.prepareStatement(query);
			prep.setString(1, currentClass);
			prep.setString(2, "y");
			if(currentGroup > 0) prep.setInt(3, currentGroup);
			resultSet = prep.executeQuery();
			while(resultSet.next()) {
				String name = resultSet.getString("name");
				list.add(name);
			}
			prep.close();
		}
		catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Fel i getList(): " + e.getMessage());
		}
		return list;
	}

	public static LinkedList<String> getCQList(boolean onlyActive){

		String query = "SELECT name FROM student WHERE class = ?";
		query += " AND CQ_ever = ?";
		if(onlyActive)	query += " AND CQ_active = ?";
		if (currentGroup>0)  query += " AND grp = ?";

		LinkedList<String> list = new LinkedList<>();
		try {
			int position = 3;
			ResultSet resultSet;
			PreparedStatement prep = connection.prepareStatement(query);
			prep.setString(1, currentClass);
			prep.setString(2, "y");
			if(onlyActive) {
				prep.setString(3, "y");
				position = 4;
			}
			if(currentGroup > 0) prep.setInt(position, currentGroup);
			resultSet = prep.executeQuery();
			while(resultSet.next()) {
				String name = resultSet.getString("name");
				list.add(name);
			}
			prep.close();
		}
		catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Fel i get CQ List(): " + e.getMessage());
		}
		return list;
	}

	public static LinkedList<String> getCQList2(){
		LinkedList<String> finalList = new LinkedList<>();

		String query1 = "SELECT DISTINCT CQ_SCORE FROM student WHERE class = ?";
		if (currentGroup>0)  query1 += " AND grp = ?";
		query1 += " ORDER BY cq_score";

		LinkedList<Integer> scores = new LinkedList<>();
		try {
			ResultSet resultSet;
			PreparedStatement prep = connection.prepareStatement(query1);
			prep.setString(1, currentClass);
			if(currentGroup > 0) prep.setInt(2, currentGroup);
			resultSet = prep.executeQuery();
			while(resultSet.next()) {
				int score = resultSet.getInt("cq_score");
				scores.add(score);
			}
			prep.close();
		}
		catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Fel i get CQ List2() - 1 " + e.getMessage());
		}

		for (int score : scores) {
			if (score != -1) {
				System.out.println("Nu jobbar vi med " + score);
				String query2 = "SELECT name FROM student WHERE class = ?";
				query2 += " AND cq_score = ?";
				if (currentGroup > 0) query2 += " AND grp = ?";
				LinkedList<String> scoreNames = new LinkedList<>();
				try {
					ResultSet resultSet;
					PreparedStatement prep = connection.prepareStatement(query2);
					prep.setString(1, currentClass);
					prep.setInt(2, score);
					if (currentGroup > 0) prep.setInt(3, currentGroup);
					resultSet = prep.executeQuery();
					while (resultSet.next()) {
						String name = resultSet.getString("name");
						scoreNames.add(name);
					}
					prep.close();
				}
				catch (SQLException e){
					JOptionPane.showMessageDialog(null, "Fel i get CQ List2() - 2: " + e.getMessage());
				}

				Collections.shuffle(scoreNames);
				finalList.addAll(scoreNames);
				System.out.println("Finalen är nu: " + finalList);
			} else {
				System.out.println("Det var -1 så vi gör inget");
			}
		}
		return finalList;
		//TODO
		// Kommentara att de som väljs i rutan startar om på noll
		// och avbrytning av cq sparning?
		// Ev ta bort -1:or i reload på CQ
	}
}