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
	private static String info = """
  		Innan du går vidare: Se tlll att alla namn finns i en txt-fil med ett namn per rad.
  		Två elever får inte ha samma namn.
  		Enklast är om du gör en fil per halvklass. Det går
  		även bra att sätta grupper efteråt men det tar ju lite mer
  		tid förstås... 
  		
  		För att skapa filen med namn kan du till exempel öppna Anteckningar 
  		(eller notepad då) i windows och skriva namnen själv eller klistra in en 
  		kolumn från excel. Spara denna fil var som helst, du kan ta bort den efter 
  		att klassen är införd för då ligger namnen i programmets databas.
		""";
	private final static String namePrompt = """
    	Skriv klassens namn.
  		Ta inte med något gruppnummer, det kommer i nästa steg.
  		""";

	private final static String chillPrompt = """
  		OBS det kan ta ett litet tag innan nästa ruta (där du ska
  		välja din fil) dyker upp.
  		Så chilla lite när du tryckt OK tack!!!
		Gissar att det tar 5-10 s.
		""";

	public static void setNewClass() {
		JOptionPane.showMessageDialog(null,info);
		String cl = JOptionPane.showInputDialog(namePrompt);
		String mess = "";
		if (cl == null || cl.isEmpty()) return;
		for(String s : DatabaseHandler.getClasses()) {
			if (s.equals(cl)) {
				mess = """
						Klassen finns redan!
						De namn som inte redan finns kommer läggas till.
						Varje namn som redan finns kommer ge ett felmeddelande.
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
		if(!mess.isEmpty()) {
			String[] ops = {"Fortsätt","Avbryt"};
			int res = JOptionPane.showOptionDialog(null, mess, "Problem!",
					JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE,null,ops,null);
			if (res != 0) return;
		}
		// Gruppnr:
		String[] options = {"Ingen grupp","1","2"};
		int resp = JOptionPane.showOptionDialog(null,"Välj grupp", "Gruppval",
				JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,null);
		if (resp == JOptionPane.CLOSED_OPTION) return;
		JOptionPane.showMessageDialog(null,chillPrompt);
		LinkedList<String> list = FileHandler.readStudents();
		if(list == null)
			JOptionPane.showMessageDialog(null, "Avbrott! (Eller nåt konstigt fel)");
		else if(list.isEmpty())
			JOptionPane.showMessageDialog(null, "Klassen hittades men var tom");
		else {
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

		classChioce = null;
		new ClassChooser2(null,response -> classChioce = response);
		if(classChioce == null) return;

		String[] options = {"Ingen grupp","1","2"};
		int resp = JOptionPane.showOptionDialog(null,"Välj grupp för " + name, "Gruppval",
				JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,null);
		if (resp == JOptionPane.CLOSED_OPTION) return;
		insertStudent(name, classChioce, resp);
	}
	
	private static void insertStudent(String n, String cl, int gr) {
		String name = n.replaceAll("[.,]", "");
		String query = "INSERT INTO student (name,class,grp) VALUES (?,?,?)";
		try {
			PreparedStatement prep = DatabaseHandler.getConnection().prepareStatement(query);
			prep.setString(1, name.trim());
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


	public static boolean saveBenches (String benchString, String className) {
		String[] dayNames = {"Oj fel på rad 67 i inserthandler ","Måndag","Tisdag","Onsdag","Torsdag","Fredag","Lördag","Söndag"};
		String query = "INSERT INTO benches (class,lesson,benchdata) VALUES (?,?,?)";

		LocalDateTime date = LocalDateTime.now();
		int hour = date.getHour();
		int minutes = date.getMinute();
		int day = date.getDayOfWeek().getValue();

		String suggestion = dayNames[day] + " " + date.getDayOfMonth() + "/" + date.getMonthValue() + " " + hour+":"+minutes;
		String chosenMess = JOptionPane.showInputDialog(null,"Skriv en valfri markering, tex:",suggestion);
		if(chosenMess == null || chosenMess.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null,"Inget sparades.");
			return false;
		}

		if (className == null || className.isEmpty()) className = JOptionPane.showInputDialog("Välj ett namn på klassen:");

		try {
			PreparedStatement prep = DatabaseHandler.getConnection().prepareStatement(query);
			prep.setString(1, className);
			prep.setString(2, chosenMess.trim());
			prep.setString(3, benchString);
			prep.execute();
			prep.close();
			JOptionPane.showMessageDialog(null,"Bordsplacering sparad");
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Ett fel uppstod: " + e.getMessage());
			return false;
		}
		return true;

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
