package databasen;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import filer.FileHandler;

public class InsertHandler {
	
	public static void setNewClass() {
		String cl = JOptionPane.showInputDialog("Skriv vad klassen ska heta i databasen.");
		LinkedList<String> list = FileHandler.readStudents();
		if(list == null)
			JOptionPane.showMessageDialog(null, "Klassen hittades inte");
		else if(list.size() == 0)
			JOptionPane.showMessageDialog(null, "Klassen hittades men var tom");
		else {
			int grp;
			String grpString = JOptionPane.showInputDialog("Ange grupp 1 eller 2. Lämna tomt om det bara finns en grupp.");
			if(grpString == null || grpString.length() == 0) grp = 0;
			else grp = Integer.parseInt(grpString);
			for(String name : list) 
				insertStudent(name, cl, grp);
			JOptionPane.showMessageDialog(null, "Klassen införd och klar");
		}
	}
	
	public static void setNewStudent() {
		int gr;
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


	public static void saveBenches (String benchString) {
		String[] dayNames = {"Oj fel på rad 67 i inserthandler ","Måndag","Tisdag","Onsdag","Torsdag","Fredag","Lördag","Sömdag"};
		String query = "INSERT INTO benches (class,lesson,benchdata) VALUES (?,?,?)";
		System.out.println("I saveBenches");
		System.out.println(benchString);
		LocalDateTime date = LocalDateTime.now();
		int hour = date.getHour();
		int minutes = date.getMinute();
		String chosenTime = JOptionPane.showInputDialog(null,"Välj en tidsmarkering:",hour+":"+minutes);
		int day = date.getDayOfWeek().getValue();
		String lesson = dayNames[day] + " " + date.getDayOfMonth() + "/" + date.getMonthValue() + " kl " + chosenTime;

		System.out.println("Dag " + day);
		String cl = DatabaseHandler.getCurrentClass();
		if (cl == null) cl = JOptionPane.showInputDialog("Välj ett namn på klassen:");


		//if(benchString.length() > 4) return false;
		try {
			PreparedStatement prep = DatabaseHandler.getConnection().prepareStatement(query);
			prep.setString(1, cl);
			prep.setString(2, lesson);
			prep.setString(3, benchString);
			prep.execute();
			prep.close();
			JOptionPane.showMessageDialog(null,"Bordsplacering sparad");
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Ett fel uppstod: " + e.getMessage());
		}

	}

}
