package databasen;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class SelectHandler {
	
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
	

	public static String getClassIfOnlyOne(String name) {
		String query = "SELECT * FROM student WHERE name = ?";
		String cl = null;
		int count = 0;
		try {
			ResultSet resultSet;
			PreparedStatement prep = DatabaseHandler2.getConnection().prepareStatement(query);
			prep.setString(1, name);
			resultSet = prep.executeQuery();

			while(resultSet.next()) {
				count++;
				cl = resultSet.getString("class");
			}
			prep.close();
		}
		catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Fel i getList(): " + e.getMessage());
		}
		return count == 1 ? cl : null;
	}
	private static LinkedList<Student> getStudents(String cl, int grp, String name) {

		LinkedList<Student> list = new LinkedList<>();
		String query;
		boolean onlyOne = name != null;
		if(onlyOne) query = "SELECT * FROM student WHERE class = ? AND name = ?";
		else {
			query = "SELECT * FROM student WHERE class = ?";
			if (grp>0) query += " AND grp = ?";
		}
		
		try {
			ResultSet resultSet;
			PreparedStatement prep = DatabaseHandler2.getConnection().prepareStatement(query);
			prep.setString(1, cl);
			if(onlyOne) prep.setString(2, name);
			else if(grp>0) prep.setInt(2, grp);
			
			resultSet = prep.executeQuery();
			while(resultSet.next()) {
				int dbGrp = resultSet.getInt("grp");
				String candy = resultSet.getString("candy_active");
				String gender = resultSet.getString("gender");
				String dbName = resultSet.getString("name");
				String dbClass = resultSet.getString("class");
				int cqScore = resultSet.getInt("CQ_score");
				int grAct = resultSet.getInt("group_active");
				int tot = resultSet.getInt("total");
				int[] ans = getStudentResults(name, cl);
				list.add(0,new Student(dbName,dbClass,dbGrp,tot,candy,cqScore,gender, ans[0],ans[1],grAct));
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
			PreparedStatement prep2 = DatabaseHandler2.getConnection().prepareStatement(query2);
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
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(null, "Fel i getList(): " + e.getMessage());
		}
		return new int[]{corr,wrong};
	}

	public static String[][] getBenches (String cl, int count, int from) {
		boolean hasClass = cl != null;
		// Följande kommer innehålla:
		// namn på lektion (0), data (1), id på lektion (2), klassnamn (3)
		String[][] data = new String[count][4];
		String queryWithClass = "SELECT * FROM benches WHERE class = ? ORDER BY id DESC LIMIT ?,?";
		String queryNoClass = "SELECT * FROM benches ORDER BY id DESC LIMIT ?,?";
		String query = hasClass ?  queryWithClass : queryNoClass;
		int position = 1;
		try {
			ResultSet resultSet;
			PreparedStatement prep = DatabaseHandler2.getConnection().prepareStatement(query);
			if(hasClass) prep.setString(position++, cl);
			prep.setInt(position++, from);
			prep.setInt(position, count);
			resultSet = prep.executeQuery();
			int index = 0;
			while(resultSet.next() && index < count) {
				data[index][0] = resultSet.getString("lesson");
				data[index][1] = resultSet.getString("benchdata");
				data[index][2] = String.valueOf(resultSet.getInt("id"));
				data[index][3] = resultSet.getString("class");
				index++;
			}
			prep.close();
		}
		catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Fel i hämta  bänkdata" + e.getMessage());
			System.out.println(e.getMessage());
		}
		return data;

	}
	public static String[][] getNeighbors(String cl) {
		LinkedList<String> names = NameListGetters.getNamesTemporary(cl,0);
		Collections.sort(names);
		int size = names.size();
		String[][] matrix = new String[size+1][size+1];
		int[][] points = new int[size][size];
		for (int i=0 ; i<size ; i++) {
			matrix[i][0] = names.get(i);
			matrix[size][i+1] = names.get(i);
		}

		String query = "SELECT * FROM neighbors WHERE class = ? AND student1 = ?";
		try {
			ResultSet resultSet;
			PreparedStatement prep = DatabaseHandler2.getConnection().prepareStatement(query);
			prep.setString(1, cl);
			for (int i = 0; i < names.size(); i++) {
				prep.setString(2, names.get(i));
				resultSet = prep.executeQuery();
				while (resultSet.next()) {
					String neighbor = resultSet.getString("student2");
					int index = names.indexOf(neighbor);
					if(index == -1)
						JOptionPane.showMessageDialog(null,"Hade bänkgranne som ej går i klassen, konstigt");
					else points[i][index]++;
				}
			}
			prep.close();

		} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Nåt gick snett när bänkgrannar skulle hämtas" + e.getMessage());
				System.out.println(e.getMessage());
		}
		// Vi adderar samma kompisar från övre högra halvan till nedre vänstra
		for (int rad = 0; rad < points.length - 1; rad++) {
			for (int kol = rad+1; kol < points.length; kol++) {
				points[kol][rad] += points[rad][kol];
				points[rad][kol] = -1;
			}
		}
		// Och tar bort diagonalen:
		for (int i = 0; i < points.length; i++) points[i][i] = -1;

		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points.length; j++) {
				matrix[i][j+1] = points[i][j] == -1 ? "" : String.valueOf(points[i][j]);
			}
		}
		return matrix;
	}

}
