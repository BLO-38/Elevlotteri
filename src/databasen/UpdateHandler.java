package databasen;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class UpdateHandler {
	
	/**
	 * ÄNDRA FÄRG PÅ MENUN
	 */
	
	private static Student student;

	public UpdateHandler() {
	}
		
	public static void updateStudent() {	
		String name = JOptionPane.showInputDialog("Elevens namn?");
		String cl = JOptionPane.showInputDialog("Vilken klass?");
		
		while(true) {
			student = SelectHandler.getStudent(cl,name);
			if(student == null) {
				JOptionPane.showMessageDialog(null, "Fanns ej.");
				return;
			}
			String[] choices = {"Byt namn","Byt klass","Byt grupp","Ändra candy","Ändra CQ-ever","Ta bort elev","Tillbaka"};
		
			int result = 0;
			result = JOptionPane.showOptionDialog(null, 
										   student.toString(),
										   "Hantera elev", 
										   JOptionPane.DEFAULT_OPTION, 
										   JOptionPane.QUESTION_MESSAGE, 
										   null, 
										   choices, 
										   null);
			System.out.println(result);
			if (result == 0) {
				String temp = setNewName();
				if (temp != null) name = temp;
			}
			else if (result == 1) {
				String temp = setNewClass();
				if (temp != null) cl = temp;
			}
			else if (result == 2) setNewGroup();
			else if (result == 3) updateCandy();
			else if (result == 4) changeCQ_ever();
			else if (result == 5) {
				if(deleteStudent()) return;
			}
			else if (result == 6) 	return;
		}
	}
		

	private static boolean executeString(String query, String newData) {
		boolean succeed = true;
		try {
			PreparedStatement prep = DatabaseHandler.getConnection().prepareStatement(query);
			prep.setString(1, newData);
			prep.setString(2, student.getKlass());
			prep.setString(3, student.getName());
			int i = prep.executeUpdate();
			JOptionPane.showMessageDialog(null, i + " st elever ändrade");
			prep.close();
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Fel vid uppdatering i databas: " + ex.getMessage());
			succeed = false;
		}
		return succeed;
	}
	
	private static boolean executeInt(String query, int newData) {
		boolean succeed = true;
		try {
			PreparedStatement prep = DatabaseHandler.getConnection().prepareStatement(query);
			prep.setInt(1, newData);
			prep.setString(2, student.getKlass());
			prep.setString(3, student.getName());
			int i = prep.executeUpdate();
			JOptionPane.showMessageDialog(null, i + " st elever ändrade");
			prep.close();
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Fel vid uppdatering i databas: " + ex.getMessage());
			succeed = false;
		}
		return succeed;
	}
	
	private static String setNewClass() {
		String newClass = JOptionPane.showInputDialog("Ange ny klass för " + student.getName() + ":");
		if(newClass == null || newClass.length() == 0) {
			JOptionPane.showMessageDialog(null, "Inget ändrades");
			return null;
		}			
		String query = "UPDATE student SET class = ? WHERE class = ? and name = ?";
		if(executeString(query,newClass)) return newClass;
		else return null;
	}

	private static void updateCandy() {
		String ans = null;
		while(true){
			ans = JOptionPane.showInputDialog("Ska godis vara aktiv för " + student.getName() + "? (y/n)");
			if(ans == null) return;
			else if(ans.length() == 1){
				if(ans.equals("y") || ans.equals("n"))
					break;
			}
		}		
		String query = "UPDATE student SET candy_active = ? WHERE class = ? and name = ?";
		executeString(query, ans);
	}

	private static void changeCQ_ever() {
		String cq = null;
		int score;
		while(true){
			cq = JOptionPane.showInputDialog("Ska " + student.getName() + " kunna få kontrollfrågor? (y/n)");
			if(cq == null) return;
			if(cq.equals("y")) {
				score = 0;
				break;
			} else if(cq.equals("n")) {
				score = -1;
				break;
			}
		}		
		String query = "UPDATE student SET CQ_score = ? WHERE class = ? and name = ?";
		executeInt(query, score);
	}

	private static boolean deleteStudent() {
		String query = "DELETE FROM student WHERE class = ? and name = ?";
		int j = JOptionPane.showConfirmDialog(null, "Är du säker på att du ska radera " + student.getName() + "?");
		if(j!=0) return false;
		String sure = JOptionPane.showInputDialog("Helt säker? Skriv JA isåfall");
		if(sure == null) return false;
		if(sure.equals("JA")) {
			boolean succeed = false;
			try {
				PreparedStatement prep = DatabaseHandler.getConnection().prepareStatement(query);
				prep.setString(1, student.getKlass());
				prep.setString(2, student.getName());
				int i = prep.executeUpdate();
				JOptionPane.showMessageDialog(null, i + " st elever borttagna");
				prep.close();
				succeed = true;
			}
			catch (SQLException ex) {
				JOptionPane.showMessageDialog(null, "Fel vid radering i databas: " + ex.getMessage());
				succeed = false;
			}
			return succeed;
		}
		return false;
	}

	private static String setNewName() {
		String newName = JOptionPane.showInputDialog("Ange nytt namn för " + student.getName() + ":");
		if(newName == null || newName.length() == 0) {
			JOptionPane.showMessageDialog(null, "Inget ändrades");
			return null;
		}
		String query = "UPDATE student SET name = ? WHERE class = ? and name = ?";
		if(executeString(query,newName)) return newName;
		else return null;
	}
	
	private static void setNewGroup() {
		String grpText = null;
		while(true){
			grpText = JOptionPane.showInputDialog("Ange ny grupp för " + student.getName() + "? (0/1/2)");
			if(grpText == null) return;
			else if(grpText.length() == 1){
				if(grpText.equals("0") || grpText.equals("1") || grpText.equals("2"))
					break;
			}
		}
		int grp = Integer.parseInt(grpText);
		String query = "UPDATE student SET grp = ? WHERE class = ? and name = ?";
		executeInt(query, grp);
	}
}
