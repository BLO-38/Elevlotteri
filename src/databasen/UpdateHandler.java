package databasen;
import view.ClassChooser2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.*;

public class UpdateHandler {

	private static Student student;
	private static String cls;

	public UpdateHandler() {
	}
		
	public static void updateStudent() {
		String name = JOptionPane.showInputDialog("Elevens namn?");
		cls = SelectHandler.getClassIfOnlyOne(name);

		if(cls==null) new ClassChooser2(null,response -> cls = response);

		while(true) {
			student = SelectHandler.getStudent(cls, name);
			if(student == null) {
				JOptionPane.showMessageDialog(null, "Fanns ej.");
				return;
			}
			String[] choices = {"Byt namn","Byt klass","Byt grupp","Ändra godis","Ändra kontrollfrågor","Ta bort elev","Ändra deltagande","Grupparbete?","Tillbaka"};
		
			int result = JOptionPane.showOptionDialog(null,
										   student.toString(),
										   "Hantera elev", 
										   JOptionPane.DEFAULT_OPTION, 
										   JOptionPane.QUESTION_MESSAGE, 
										   null, 
										   choices, 
										   null);
			System.out.println(result);
			if (result == 0) {
				String newName = setNewName();
				name = newName == null ? name : newName;
			}
			else if (result == 1) {
				String newClass = setNewClass();
				cls = newClass == null ? cls : newClass;
			}

			else if (result == 2) setNewGroup();
			else if (result == 3) updateCandy();
			else if (result == 4) changeCQ_ever();
			else if (result == 5) {
				if(DeleteHandler.deleteStudent(student)) {
					student = null;
					return;
				}
			}
			else if (result == 6) 	changeTotal();
			else if (result == 7) 	changeGroupWork();
			else if (result == 8 || result == -1) 	return;
		}
	}

	private static void changeGroupWork() {
		String[] options = {"Ja","Nej aldrig"};
		String questiion = "Ska " + student.getName() + " kunna bli utsedd till en grupp?";
		int choice = JOptionPane.showOptionDialog(null, questiion, "Gruppval",
			JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,null);

		if (choice < 0) return;
		int status = choice == 0 ? DatabaseHandler2.AVAILABLE : DatabaseHandler2.NEVER_PARTICIPATE;
		String query = "UPDATE student SET group_active = ? WHERE class = ? and name = ?";
		executeInt(query, status);
	}

	private static void changeTotal() {
		String[] options = {"Ja","Nej"};
		String questiion = "Ska " + student.getName() + " kunna få frågor i prioriterat lotteri?";
		int choice = JOptionPane.showOptionDialog(null, questiion, "Gruppval",
				JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,null);

		if(choice < 0) return;

		int studentScore = -1;

		if (choice == 0) {
			LinkedList<Integer> scores = getDistinctScores();
			System.out.println("Size " + scores.size());
			if (!scores.isEmpty()) {
				int sum = 0;
				for (int score : scores) sum += score;
				System.out.println("SUmma " + sum);
				studentScore = sum / scores.size();
			}
			else studentScore = 0;
		}
		System.out.println(studentScore);
		String query = "UPDATE student SET total = ? WHERE class = ? and name = ?";
		executeInt(query, studentScore);

	}

	private static LinkedList<Integer> getDistinctScores() {

		LinkedList<Integer> list = new LinkedList<>();
		StringBuilder build1 = new StringBuilder("SELECT DISTINCT total FROM student WHERE class = ?");
		build1.append(" AND grp = ? ORDER BY total");

		try {
			ResultSet resultSet;
			PreparedStatement prep = DatabaseHandler2.getConnection().prepareStatement(build1.toString());
			prep.setString(1, student.getKlass());
			prep.setInt(2, student.getGroup());
			resultSet = prep.executeQuery();
			while (resultSet.next()) {
				int score = resultSet.getInt("total");
				System.out.println("Hämtad score " + score);
				if(score != -1) list.add(score);
			}
			prep.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Fel i getRegLOwest nr 1 " + e.getMessage());
		}
		System.out.println("Skårlistan blev: " + list);
		return list;


	}


	private static int executeString(String query, String newData) {

		int result = -1;
		try {
			PreparedStatement prep = DatabaseHandler2.getConnection().prepareStatement(query);
			prep.setString(1, newData);
			prep.setString(2, student.getKlass());
			prep.setString(3, student.getName());
			result = prep.executeUpdate();
			prep.close();
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Fel vid uppdatering i databas: " + ex.getMessage());
		}
		return result;
	}
	
	private static int executeInt(String query, int newData) {
		int result = -1;
		try {
			PreparedStatement prep = DatabaseHandler2.getConnection().prepareStatement(query);
			prep.setInt(1, newData);
			prep.setString(2, student.getKlass());
			prep.setString(3, student.getName());
			result = prep.executeUpdate();
			prep.close();
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Fel vid uppdatering i databas: " + ex.getMessage());
		}
		return result;
	}
	
	private static String setNewClass() {

		cls = null;
		new ClassChooser2(null,response -> cls = response);
		if(cls == null) return null;

		String query = "UPDATE student SET class = ? WHERE class = ? and name = ?";
		if(executeString(query,cls) >= 0)  return cls;
		return null;
	}

	private static void updateCandy() {

		String[] options = {"Ja","Nej","Avbryt"};
		String questiion = "Ska " + student.getName() + " kunna få godis?";
		int choice = JOptionPane.showOptionDialog(null, questiion, "Godisutdelning",
			JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,null);

		if(choice < 0 || choice > 1) {
			System.out.println("Avbröt");
			return;
		}

		String newValue = choice == 0 ? "y" : "n";
		String query = "UPDATE student SET candy_active = ? WHERE class = ? and name = ?";
		executeString(query, newValue);
	}

	private static void changeCQ_ever() {

		String[] options = {"Ja","Nej","Avbryt"};
		String questiion = "Ska " + student.getName() + " kunna få kontrollfrågor?";
		int choice = JOptionPane.showOptionDialog(null, questiion, "Kontrollfrågor",
			JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,null);

		if(choice < 0 || choice > 1) {
			System.out.println("Avbröt");
			return;
		}
		int newValue = choice == 0 ? 0 : -1;

		String query = "UPDATE student SET CQ_score = ? WHERE class = ? and name = ?";
		executeInt(query, newValue);
	}

	private static String setNewName() {
		String newName = JOptionPane.showInputDialog("Ange nytt namn för " + student.getName() + ":");
		if(newName == null || newName.isEmpty()) return null;
		String query = "UPDATE student SET name = ? WHERE class = ? and name = ?";
		return executeString(query,newName) != -1 ? newName : null;
	}
	
	private static void setNewGroup() {
		String[] options = {"Ingen grupp","1","2"};
		int resp = JOptionPane.showOptionDialog(null,"Välj grupp för " + student.getName(), "Gruppval",
				JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,null);
		if(resp == JOptionPane.CLOSED_OPTION) return;
		String query = "UPDATE student SET grp = ? WHERE class = ? and name = ?";
		executeInt(query, resp);
	}
	public static boolean setNewGroups(LinkedList<Student> students) {
		String query = "UPDATE student SET grp = ? WHERE class = ? and name = ?";
		for(Student s : students) {
			student = s;
			if (executeInt(query, s.getGroup()) == -1) {
				JOptionPane.showMessageDialog(null,"Fel uppstod för " + s.getName());
				return false;
			}
		}
		System.out.println("Allt verkar gått bra med nya grupper!");
		return true;
	}

	public static boolean setNewGender(LinkedList<Student> students) {
		String query = "UPDATE student SET gender = ? WHERE class = ? and name = ?";
		int count = 0;
		for(Student s : students) {
			student = s;
			int result = executeString(query, s.getGender());
			if (result == -1) JOptionPane.showMessageDialog(null,"Fel uppstod för " + s.getName());
			else count += result;
		}
		JOptionPane.showMessageDialog(null, "Kön infört på " + count + " elever");
		return true;
	}

	public static void updateGroupActive(String name, String klass, int status) {
		LinkedList<String> list = new LinkedList<>();
		list.add(name);
		updateGroupActive(list,klass,status);
	}
	public static void updateGroupActive(LinkedList<String> names, String klass, int status) {
		String query1 = "update student set group_active = ? where name = ? and class = ?";
		// Spara gruppen?
//		String query2 = "INSERT INTO candy (name,class) VALUES (?,?)";
		try {
			PreparedStatement prep1 = DatabaseHandler2.getConnection().prepareStatement(query1);
			prep1.setInt(1, status);
			prep1.setString(3, klass);
			for (String name : names) {
				prep1.setString(2, name);
				prep1.executeUpdate();
			}
			prep1.close();
//
//			PreparedStatement prep2 = DatabaseHandler2.getConnection().prepareStatement(query2);
//			prep2.setString(1, name);
//			prep2.setString(2, klass);
//			prep2.execute();
//			prep2.close();
		}
		catch (SQLException ex) {
			System.out.println("Fel 100");
			JOptionPane.showMessageDialog(null, "Fel vid uppdatering i databas grp-act: " + ex.getMessage());
		}
	}


	public static int setNewKlassName(String newName, String oldName) {
		String query = "UPDATE student SET class = ? WHERE class = ?";
		int rowCount = -1;
		try {
			PreparedStatement prep = DatabaseHandler2.getConnection().prepareStatement(query);
			prep.setString(1, newName);
			prep.setString(2, oldName);
			rowCount = prep.executeUpdate();
			prep.close();
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Fel vid uppdatering i databas: " + ex.getMessage());
		}
		return rowCount;
	}
}
