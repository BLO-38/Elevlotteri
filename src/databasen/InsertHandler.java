package databasen;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import filer.FileHandler;
import view.ClassChooser2;

public class InsertHandler {
	
	private static String errorMess = " fanns redan.";
	private static String classChioce;

	public static void setNewClass() {
		String cl = JOptionPane.showInputDialog("Skriv vad klassen ska heta i databasen.");
		String mess = "";
		if (cl == null || cl.length() == 0) return;
		for(String s : DatabaseHandler.getClasses()) {
			if (s.equals(cl)) {
				mess = """
						Klassen finns redan.
						Om du fortsätter kommer du få felmeddelande för varje namn som redan finns,
						men de kommer varken tas bort eller bli dubbletter.
						De som inte fanns läggs till.
						""";
				errorMess = " fanns redan.";
				break;
			}
			else if (s.equalsIgnoreCase(cl)) {
				mess = """
						Klassen finns redan fast med skillnad på versalerna.
						Om du fortsätter kommer du få en helt ny klass
						med exakt det namn du skrev nu, dvs""" + " " + cl;
			}
		}
		if(mess.length() > 0) {
			String[] ops = {"Fortsätt","Avbryt"};
			int res = JOptionPane.showOptionDialog(null, mess, "Problem!",
					JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE,null,ops,null);
			if (res != 0) return;
		}
		LinkedList<String> list = FileHandler.readStudents();
		if(list == null)
			JOptionPane.showMessageDialog(null, "Avbrott! (Eller nåt konstigt fel)");
		else if(list.size() == 0)
			JOptionPane.showMessageDialog(null, "Klassen hittades men var tom");
		else {
			String[] options = {"Ingen grupp","1","2"};
			int resp = JOptionPane.showOptionDialog(null,"Välj grupp", "Gruppval",
					JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,null);
			if (resp == JOptionPane.CLOSED_OPTION) return;

			for(String name : list) insertStudent(name, cl, resp);

			JOptionPane.showMessageDialog(null, "Klassen införd och klar");
		}
	}
	
	public static void setNewStudent() {
		String name = JOptionPane.showInputDialog("Vilket namn har eleven?");
		if(name == null) return;
		if(name.length() < 2) {
			JOptionPane.showMessageDialog(null,"Ogiltigt namn");
			return;
		}


		new ClassChooser2(null,response -> classChioce = response);
		if(classChioce == null) return;

		String[] options = {"Ingen grupp","1","2"};
		int resp = JOptionPane.showOptionDialog(null,"Välj grupp för " + name, "Gruppval",
				JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,null);
		if (resp == JOptionPane.CLOSED_OPTION) return;
		insertStudent(name, classChioce, resp);
	}
	
	private static void insertStudent(String name, String cl, int gr) {
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
			if(e.getErrorCode() == 19)
				JOptionPane.showMessageDialog(null, "Fel! " + name + errorMess);
			else
				JOptionPane.showMessageDialog(null, "Oväntat fel: " + e.getMessage());
			System.out.println(e.getErrorCode());
			System.out.println(e.getSQLState());

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
		String chosenTime = JOptionPane.showInputDialog(null,"Skriv en valfri markering, tex tid: ",hour+":"+minutes);
		int day = date.getDayOfWeek().getValue();
		String lesson = dayNames[day] + " " + date.getDayOfMonth() + "/" + date.getMonthValue() + " " + chosenTime;

		System.out.println("Dag " + day);
		String cl = DatabaseHandler.getCurrentClass();
		if (cl == null) cl = JOptionPane.showInputDialog("Välj ett namn på klassen:");

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

	public static boolean insertNeighbors(LinkedList<String[]> neighbors) {
		boolean success = true;
		String query = "INSERT INTO neighbors (class,student1,student2) VALUES (?,?,?)";
		try {
			PreparedStatement prep = DatabaseHandler.getConnection().prepareStatement(query);
			for (String[] pair : neighbors) {
				prep.setString(1, DatabaseHandler.getCurrentClass());
				prep.setString(2, pair[0]);
				prep.setString(3, pair[1]);
				prep.execute();
			}
			prep.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Fel! vid sparning av grannar: " +  e.getMessage());
			success = false;
		}
		return success;
	}

}
