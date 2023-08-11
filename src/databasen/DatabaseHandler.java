package databasen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import javax.swing.*;

import filer.InitializationHandler;
import view.ClassViewer;
import view.StudentViewer;

public class DatabaseHandler {

	//private static final String baseURL="jdbc:sqlite:C:\\Program Files\\Sqlite3\\";
	 private static final String baseURL="jdbc:sqlite:C:/sqlite/";
	private static Connection connection = null;
	private static String currentClass = null;
	private static int currentGroup;
	private static final String[] choices1 = {"Ny klass","Ny elev","Hantera elev","Kolla klass","Elevsvar","Hantera databasen","Hantera grupper","Avsluta"};
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
		LiveUpdateHandler.setClass(currentClass);
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
	
	public static void showMenu(JFrame frame)	{
		while(true) {
			int result = JOptionPane.showOptionDialog(frame,
										   "Vad vill du göra?",
										   "Hantera databasen",
										   JOptionPane.DEFAULT_OPTION,
										   JOptionPane.QUESTION_MESSAGE,
										   null,
										   choices1,
										   null);
			if (result == 0) 	  InsertHandler.setNewClass();
			else if (result == 1) InsertHandler.setNewStudent();
			else if (result == 2) UpdateHandler.updateStudent();
			else if (result == 3) showClass();
			else if (result == 4) showStudent();
			else if (result == 5) InitializationHandler.newInitialazation(frame);
			else if (result == 6) {new GroupDialog(null);}
			else if (result == 7) {closeDatabase();System.exit(0);}
			else break;
		}
		System.exit(0);
	}
	
	
	private static void showClass() {
		String cl = chooseClass();
		LinkedList<Student> students = getStudents(cl, 0);
		if(students.size() == 0) {
			JOptionPane.showMessageDialog(null, "Inga elever hittades");
			showMenu(null); // Behövs??
		}
		else ClassViewer.showClass(students);
		showMenu(null);
	}
	
	public static LinkedList<String> getNamesTemporary(String c, int g) {
		System.out.println("Inne i nya");
		//if()
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
				int gr = resultSet.getInt("grp");
				int tot = resultSet.getInt("total");
				int[] ans = getResults(n, className);
				Student next = new Student(n, className, gr, tot, ans[0], ans[1],null);
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

	private static String chooseClass() {
		LinkedList<String> list = getClasses();
		String[] options = list.toArray(new String[list.size()]);
		int res = JOptionPane.showOptionDialog(null, 
				   "Välj klass (nyy egen metod)", 
				   "Statistik", 
				   JOptionPane.DEFAULT_OPTION, 
				   JOptionPane.QUESTION_MESSAGE, 
				   null, 
				   options, 
				   null);
		return options[res];
	}
	
	public static void setSession() {
		System.out.println("setSESSION");
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
		System.out.println("2 " + query1);
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
				System.out.println("Vi lägger till " + score + " i scores");
			}
			prep.close();
		}
		catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Fel i get CQ List2() - 1 " + e.getMessage());
		}
		System.out.println("Antal olika scores: " + scores.size());
		for(int i : scores) {
			System.out.println(i);
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
	 
	private static void showStudent(){
		System.out.println("Nu ska elevsvar visas");
		LinkedList<String> list = new LinkedList<>();
		list.add("Lars");
		list.add("Erika");
		list.add("Olle");
		list.add("Stina");
		StudentViewer.showClass(list);
		//showMenu();
	}
}	
	/*
	public Student getStudent(String name, String cl) {
		
		Student stud = null;
		String query = "SELECT * FROM student WHERE class = ? AND name = ?";		
		
		try {
			ResultSet resultSet;
			PreparedStatement prep = connection.prepareStatement(query);
			prep.setString(1, cl);
			prep.setString(2, name);
			
			resultSet = prep.executeQuery();
			
			while(resultSet.next()) {
				
				int gr = resultSet.getInt("grp");
				String candy = resultSet.getString("candy_active");
				String cqEver = resultSet.getString("CQ_ever");
				String cqActive = resultSet.getString("CQ_active");
				
				int tot = resultSet.getInt("total");
				
				int[] ans = getResults(name, cl);				
				for(int j : ans)
					System.out.print(j + ", ");
				System.out.println();
				stud = new Student(name, cl, gr, tot, ans[0], ans[1], ans[2], cqActive, cqEver, candy);
			}
			prep.close();
		}
		catch (SQLException e){
			JOptionPane.showMessageDialog(null, "Fel i getList(): " + e.getMessage());
		}
		return stud;
	}
*/
	

/*
public void updateTotal(String name, boolean first) {
	String query1 = "update student set total = total + 1 where name = ? and class = ?";
	
	if(first) setSession();

	try {
		// Varje gång ska elevens totala uppdateras:
		PreparedStatement prep1 = connection.prepareStatement(query1);
		prep1.setString(1, name);
		prep1.setString(2, currentClass);
		prep1.executeUpdate();
		prep1.close();
	}
	catch (SQLException ex) {
		JOptionPane.showMessageDialog(null, "Fel vid uppdatering i databas: " + ex.getMessage());
	}
}
*/
/*
public void upppdateCandy(String name) {
	String query1 = "update student set candy_active = ? where name = ? and class = ?";
	String query2 = "INSERT INTO candy (name,class) VALUES (?,?)";
	try {
		PreparedStatement prep1 = connection.prepareStatement(query1);
		prep1.setString(1, "n");
		prep1.setString(2, name);
		prep1.setString(3, currentClass);
		prep1.executeUpdate();
		prep1.close();
		
		PreparedStatement prep2 = connection.prepareStatement(query2);
		prep2.setString(1, name);
		prep2.setString(2, currentClass);
		prep2.execute();
		prep2.close();
	}
	catch (SQLException ex) {
		JOptionPane.showMessageDialog(null, "Fel vid uppdatering i databas (candy): " + ex.getMessage());
	}
}



public void updateCQ(String name, int number, int answer, String topic) {
	String query1 = "UPDATE student SET CQ_active = ? WHERE name = ? and class = ?";
	String query2 = "INSERT INTO CQ_result (name,class,question,correct,topic) VALUES (?,?,?,?,?)";
	System.out.println("Mottagen siffra: " + answer);
	String ans;
	if(answer == CORRECT) ans = "y";
	else if(answer == WRONG) ans = "n";
	else ans = "a";
	System.out.println("Svaret är " + ans);
	try {
		if(answer != ABSENT) {//denna iffen är ny
			PreparedStatement prep1 = connection.prepareStatement(query1);
			prep1.setString(1, "n");
			prep1.setString(2, name);
			prep1.setString(3, currentClass);
			prep1.executeUpdate();
			prep1.close();
		}
					
		PreparedStatement prep2 = connection.prepareStatement(query2);
		prep2.setString(1, name);
		prep2.setString(2, currentClass);
		prep2.setInt(3, number);
		prep2.setString(4, ans);
		prep2.setString(5, topic);
		prep2.executeUpdate();
		prep2.close();
	}
	catch (SQLException ex) {
		JOptionPane.showMessageDialog(null, "Fel vid uppdatering i CQ databas: " + ex.getMessage());
	}
}
*/
/*
public void resetCQ(){
	//FIXA & baka ihop med andra reset
	System.out.println("Viåterställer CQ");
	String query = "UPDATE student SET CQ_active = ? WHERE class = ?";
	if (currentGroup>0) query += " AND grp = ?";
	try {
		PreparedStatement prep = connection.prepareStatement(query);
		prep.setString(1, "y");
		prep.setString(2, currentClass);
		if(currentGroup > 0) prep.setInt(3, currentGroup);
		prep.executeUpdate();
		prep.close();
	}
	catch (SQLException ex) {
		JOptionPane.showMessageDialog(null, "Fel vid uppdatering i databas (candy): " + ex.getMessage());
	}
}


private void changeCQ(){
	String name = JOptionPane.showInputDialog("Namn:");
	String kl = JOptionPane.showInputDialog("Klass:");
	String cq = null;
	while(true){
		cq = JOptionPane.showInputDialog("Ska kontrollfrågor vara aktiva för " + name + "? (y/n)");
		if(cq == null) System.exit(0);
		if(cq.length() == 1){
			if(cq.equals("y") || cq.equals("n"))
				break;
		}
	}		
	String query = "UPDATE student SET CQ_ever = ? WHERE class = ? and name = ?";
	try {
		PreparedStatement prep = connection.prepareStatement(query);
		prep.setString(1, cq);
		prep.setString(2, kl);
		prep.setString(3, name);
		int i = prep.executeUpdate();
		JOptionPane.showMessageDialog(null, i + " st elever ändrade");
		prep.close();
	}
	catch (SQLException ex) {
		JOptionPane.showMessageDialog(null, "Fel vid uppdatering i databas (candy): " + ex.getMessage());
	}
	
	showMenu();
}

*/
/*
public void resetCandy(){
	String query = "update student set candy_active = ? where class = ?";
	if (currentGroup>0) query += " AND grp = ?";
	try {
		PreparedStatement prep = connection.prepareStatement(query);
		prep.setString(1, "y");
		prep.setString(2, currentClass);
		if(currentGroup > 0) prep.setInt(3, currentGroup);
		prep.executeUpdate();
		prep.close();
	}
	catch (SQLException ex) {
		JOptionPane.showMessageDialog(null, "Fel vid uppdatering i databas (candy): " + ex.getMessage());
	}
}
*/

/*
private void setNewClass() {
String cl = JOptionPane.showInputDialog("Skriv vad klassen ska heta i databasen.");
LinkedList<String> list = FileHandler.readStudents();

if(list != null && list.size() == 0)
	JOptionPane.showMessageDialog(null, "Klassen hittades men var tom");
else {
	int grp = 0;
	String grpString = JOptionPane.showInputDialog("Ange grupp 1 eller 2. Lämna tomt om det bara finns en grupp.");
	if(grpString == null || grpString.length() == 0) grp = 0;
	else grp = Integer.parseInt(grpString);
	for(String name : list) 
		insertName(name, cl, grp);
	JOptionPane.showMessageDialog(null, "Klassen införd och klar");
}
showMenu();
}

private void setNewStudent() {
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
	insertName(name, cl, gr);
	
showMenu();
}

private void insertName(String name, String cl, int gr) {
String query = "INSERT INTO student (name,class,grp) VALUES (?,?,?)";
try {
	PreparedStatement prep = connection.prepareStatement(query);
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
*/