package databasen;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import filer.FileHandler;

public class InsertHandler {

	public InsertHandler() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static void setNewClass() {
		String cl = JOptionPane.showInputDialog("Skriv vad klassen ska heta i databasen.");
		LinkedList<String> list = FileHandler.readStudents();
		if(list == null)
			JOptionPane.showMessageDialog(null, "Klassen hittades inte");
		else if(list != null && list.size() == 0)
			JOptionPane.showMessageDialog(null, "Klassen hittades men var tom");
		else {
			int grp = 0;
			String grpString = JOptionPane.showInputDialog("Ange grupp 1 eller 2. Lämna tomt om det bara finns en grupp.");
			if(grpString == null || grpString.length() == 0) grp = 0;
			else grp = Integer.parseInt(grpString);
			for(String name : list) 
				insertStudent(name, cl, grp);
			JOptionPane.showMessageDialog(null, "Klassen införd och klar");
		}
	}
	
	public static void setNewStudent() {
		int gr = 1;
		String name = JOptionPane.showInputDialog("Vilket namn?");
		String cl = JOptionPane.showInputDialog("Vilken klass?");
		String grString = JOptionPane.showInputDialog("Vilken grupp?");
		try{
			gr = Integer.parseInt(grString);
		}
		catch(NumberFormatException n){
			System.out.println("Gruppnummerformatfel");
			gr = 100;
		}
		if(gr == 100)
			JOptionPane.showMessageDialog(null, "Gruppnumret funkade ej. Inget införs.");
		else
			insertStudent(name, cl, gr);
	}
	
	public static void insertStudent(String name, String cl, int gr) {
		String query = "INSERT INTO student (name,class,grp) VALUES (?,?,?)";
		try {
			PreparedStatement prep = DatabaseHandler.getConnection().prepareStatement(query);
			prep.setString(1, name);
			prep.setString(2, cl);
			prep.setInt(3, gr);
			prep.execute();
			prep.close();
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Ett fel uppstod: " + e.getMessage());
		}
	}
}
