package databasen;

import javax.swing.*;
import java.sql.*;
import java.util.Collections;
import java.util.LinkedList;

public class NameListGetters {

	public static LinkedList<String> getNamesTemporary(String klass, int grp) {
		return getNamesRegular(klass,grp);
	}

	public static LinkedList<String> getNamesRegularLowestOrder (String klass, int grp) {
		Connection connection = DatabaseHandler2.getConnection();
		if(connection == null) return null;
		
		LinkedList<String> finalList = new LinkedList<>();
		LinkedList<Integer> scores = new LinkedList<>();

		// Först vilka poäng finns?:
		StringBuilder build1 = new StringBuilder("SELECT DISTINCT total FROM student WHERE class = ?");
		if (grp > 0) build1.append(" AND grp = ?");
		build1.append(" ORDER BY total");

		try {
			ResultSet resultSet;
			PreparedStatement prep = connection.prepareStatement(build1.toString());
			prep.setString(1, klass);
			if (grp > 0) prep.setInt(2, grp);
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
		if (grp > 0) buildQuery.append(" AND grp = ?");

		for (int score : scores) {

			if (score == -1) continue;

			LinkedList<String> scoreNames = new LinkedList<>();
			try {
				ResultSet resultSet;
				PreparedStatement prep = connection.prepareStatement(buildQuery.toString());
				prep.setString(1, klass);
				prep.setInt(2, score);
				if (grp > 0) prep.setInt(3, grp);

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

	public static LinkedList<String> getNamesRegular(String klass, int grp) {
		Connection connection = DatabaseHandler2.getConnection();
		if(connection == null) return null;

		System.out.println("Klas " + klass + " grupp " + grp);
		StringBuilder buildQuery = new StringBuilder("SELECT name FROM student WHERE class = ?");
		if (grp > 0) buildQuery.append(" AND grp = ?");

		LinkedList<String> regNames = new LinkedList<>();
		try {
			ResultSet resultSet;
			PreparedStatement prep = connection.prepareStatement(buildQuery.toString());
			prep.setString(1, klass);
			if (grp > 0) prep.setInt(2, grp);

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
		Connection connection = DatabaseHandler2.getConnection();
		if(connection == null) return null;

		LinkedList<Student> list = new LinkedList<>();
		String query = "SELECT * FROM student WHERE class = ?";
		if (group>0) query += " AND grp = ?";

		try {
			ResultSet resultSet;
			PreparedStatement prep = connection.prepareStatement(query);
			prep.setString(1, className);
			if(group > 0) prep.setInt(2, group);
			resultSet = prep.executeQuery();
			prep.close();
			while(resultSet.next()) {
				String n = resultSet.getString("name");
				int gr = resultSet.getInt("grp");
				String gender = resultSet.getString("gender");
				String candy = resultSet.getString("candy_active");
				int cq = resultSet.getInt("cq_score");
				int grAct = resultSet.getInt("group_active");
				int tot = resultSet.getInt("total");
				int[] ans = getResults(n, className);
				Student next = new Student(n, className, gr, tot, candy, cq, gender, ans, grAct);
				list.add(next);
			}
		}
		catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Fel i getList(): " + e.getMessage());
		}
		return list;
	}

	private static int[] getResults(String name, String klass) {
		Connection connection = DatabaseHandler2.getConnection();
		if(connection == null) return null;

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


	public static LinkedList<String> getCandyList(String klass, int grp) {
		LinkedList<String> list = new LinkedList<>();
		Connection connection = DatabaseHandler2.getConnection();
		if(connection == null) return list;

		String query = "SELECT name FROM student WHERE class = ? AND candy_active = ?";
		if (grp>0) query += " AND grp = ?";

		try {
			ResultSet resultSet;

			PreparedStatement prep = connection.prepareStatement(query);
			prep.setString(1, klass);
			prep.setString(2, "y");
			if(grp > 0) prep.setInt(3, grp);
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

	public static LinkedList<String> getCQList(boolean onlyActive, String klass, int grp){
		Connection connection = DatabaseHandler2.getConnection();
		if(connection == null) return null;

		String query = "SELECT name FROM student WHERE class = ?";
		query += " AND CQ_ever = ?";
		if(onlyActive)	query += " AND CQ_active = ?";
		if (grp>0)  query += " AND grp = ?";

		LinkedList<String> list = new LinkedList<>();
		try {
			int position = 3;
			ResultSet resultSet;
			PreparedStatement prep = connection.prepareStatement(query);
			prep.setString(1, klass);
			prep.setString(2, "y");
			if(onlyActive) {
				prep.setString(3, "y");
				position = 4;
			}
			if(grp > 0) prep.setInt(position, grp);
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

	public static LinkedList<String> getCQList2(String klass, int grp){
		Connection connection = DatabaseHandler2.getConnection();
		if(connection == null) return null;

		LinkedList<String> finalList = new LinkedList<>();

		String query1 = "SELECT DISTINCT CQ_SCORE FROM student WHERE class = ?";
		if (grp>0)  query1 += " AND grp = ?";
		query1 += " ORDER BY cq_score";

		LinkedList<Integer> scores = new LinkedList<>();
		try {
			ResultSet resultSet;
			PreparedStatement prep = connection.prepareStatement(query1);
			prep.setString(1, klass);
			if(grp > 0) prep.setInt(2, grp);
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
				if (grp > 0) query2 += " AND grp = ?";
				LinkedList<String> scoreNames = new LinkedList<>();
				try {
					ResultSet resultSet;
					PreparedStatement prep = connection.prepareStatement(query2);
					prep.setString(1, klass);
					prep.setInt(2, score);
					if (grp > 0) prep.setInt(3, grp);
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
	}

	public synchronized static LinkedList<String> getSingleGroup(String klass, int grp, int size, boolean save) {
		int whichStudents = save ? DatabaseHandler2.AVAILABLE : DatabaseHandler2.ALL;
		LinkedList<String> list = fetchGroupActiveNames(klass, grp, whichStudents);
		LinkedList<String> finalList = new LinkedList<>();
		if(list.size() < size) {
			finalList.addAll(list);
			LinkedList<String> notActive = fetchGroupActiveNames(klass,grp,DatabaseHandler2.UNAVAILABLE);
			if(notActive.size() + finalList.size() < size) {
				JOptionPane.showMessageDialog(null,"Det finns inte tillräckligt med elever för denna gruppstorlek");
				return null;
			}
			Collections.shuffle(notActive);
			for (int i = 0; i < size - list.size(); i++) {
				finalList.add(notActive.pop());
			}
			Resetters.resetGroupActive(klass,grp);
		} else {
			Collections.shuffle(list);
			for (int i = 0; i < size; i++) {
				finalList.add(list.pop());
			}
		}
		if(save) {
			UpdateHandler.updateGroupActive(finalList,klass,DatabaseHandler2.UNAVAILABLE);
		}
		// Sätt tillbaka de som ska ha never
		return finalList;
	}

	private static LinkedList<String> fetchGroupActiveNames(String klass, int grp, int status) {
		LinkedList<String> list = new LinkedList<>();
		Connection connection = DatabaseHandler2.getConnection();
		if(connection == null) return list;

		String query1 = "SELECT name FROM student WHERE class = ? AND group_active = ?";
		String query2 = "SELECT name FROM student WHERE class = ? AND group_active > ?";
		String query;
		int statusValue;
		if (status == DatabaseHandler2.ALL) {
			query = query2;
			statusValue = DatabaseHandler2.NEVER_PARTICIPATE;
		} else {
			query = query1;
			statusValue = status;
		}
		if (grp>0) query += " AND grp = ?";

		try {
			ResultSet resultSet;
			PreparedStatement prep = connection.prepareStatement(query);
			prep.setString(1, klass);
			prep.setInt(2, statusValue);
			if(grp > 0) prep.setInt(3, grp);
			resultSet = prep.executeQuery();
			while(resultSet.next()) {
				String name = resultSet.getString("name");
				list.add(name);
			}
			System.out.println();
			prep.close();
		}
		catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Fel i getList(): " + e.getMessage());
		}
		return list;
	}
}