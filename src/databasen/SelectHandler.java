package databasen;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class SelectHandler {
		// Nyast efer bänk-db
	
	public SelectHandler() {
		System.out.println("Selecthandler konstruktor");
	}
	
	public static Student getStudent(String cl, String name) {
		LinkedList<Student> students = getStudents(cl, 0, name);
		return students.poll();
	}
	
	public static LinkedList<Student> getStudents(String cl, int grp) {
		return getStudents(cl, grp, null);
	}
	
	private static LinkedList<Student> getStudents(String cl, int grp, String name) {
		
		LinkedList<Student> list = new LinkedList<Student>();
		String query;
		boolean onlyOne = name != null;

		if(onlyOne) query = "SELECT * FROM student WHERE class = ? AND name = ?";
		else {
			query = "SELECT * FROM student WHERE class = ?";
			if (grp>0) query += " AND grp = ?";
		}
		
		try {
			ResultSet resultSet;
			PreparedStatement prep = DatabaseHandler.getConnection().prepareStatement(query);
			prep.setString(1, cl);
			if(onlyOne) prep.setString(2, name);
			else if(grp>0) prep.setInt(2, grp);
			
			resultSet = prep.executeQuery();
			
			while(resultSet.next()) {
				
				int dbGrp = resultSet.getInt("grp");
				String candy = resultSet.getString("candy_active");
				String dbName = resultSet.getString("name");
				String dbClass = resultSet.getString("class");
				// String cqEver = resultSet.getString("CQ_ever");
				String cqScore = resultSet.getString("CQ_score");
				// String cqActive = resultSet.getString("CQ_active");
				int tot = resultSet.getInt("total");
				int[] ans = getStudentResults(name, cl);
				list.add(new Student(dbName, dbClass, dbGrp, tot, ans[0], ans[1], candy));
				// list.add(new Student(dbName, dbClass, dbGrp, tot, ans[0], ans[1], cqActive, cqEver, candy));
			}
			prep.close();
		}
		catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Fel i getList(): " + e.getMessage());
		}
		return list;
	}
	
	private static int[] getStudentResults(String name, String klass) {
		String query2 = "SELECT correct,COUNT(correct) AS tot FROM CQ_result WHERE name = ? AND class = ? GROUP BY correct";
		
		int corr = 0, wrong = 0;
		
		try {
			ResultSet resultSet;
			PreparedStatement prep2 = DatabaseHandler.getConnection().prepareStatement(query2);
			prep2.setString(1, name);
			prep2.setString(2, klass);
			resultSet = prep2.executeQuery();
			while(resultSet.next()) {
				String answ = resultSet.getString("correct");
				int n = resultSet.getInt("tot");
				if(answ.equals("n")) wrong = n;
				else if(answ.equals("y")) corr = n;
				else {
					JOptionPane.showMessageDialog(null, "Ska aldrig visas. Fel i hämtning av rätt&fel. Programmet avslutas.");
					System.exit(0);
				}
			}
			prep2.close();
		}
		catch (SQLException e){
			System.out.println(e);
			JOptionPane.showMessageDialog(null, "Fel i getList(): " + e.getMessage());
		}
		return new int[]{corr,wrong};
	}


	public static String[][] getBenches (String cl, int count, int from) {

		String[][] data = new String[count][2];
		String query = "SELECT * FROM benches WHERE class = ? ORDER BY id DESC LIMIT ?,?";

		try {

			ResultSet resultSet;
			PreparedStatement prep = DatabaseHandler.getConnection().prepareStatement(query);
			prep.setString(1, cl);
			prep.setInt(2, from);
			prep.setInt(3, count);
			resultSet = prep.executeQuery();
			int index = 0;
			while(resultSet.next() && index < count) {
				data[index][0] = resultSet.getString("lesson");
				data[index][1] = resultSet.getString("benchdata");
				index++;
			}
			System.out.println("Index slutade på " + index);
			prep.close();
		}
		catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Fel i hämta  bänkdata" + e.getMessage());
			System.out.println(e);
		}
//		System.out.println("Vi skriver ut det vi hämtat:");
//		for (int i = 0; i < 10; i++) {
//			System.out.println(Arrays.toString(data[i]));
//		}
		return data;

	}



}
