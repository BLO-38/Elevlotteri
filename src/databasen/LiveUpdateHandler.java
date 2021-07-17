package databasen;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class LiveUpdateHandler {
	private static String currentClass = null;

	public LiveUpdateHandler() {
		// TODO Auto-generated constructor stub
	}
	
	public static void setClass(String cl) {
		currentClass = cl;
		System.out.println("Nu har vi satt klassen till  --" + currentClass + "--");
	}
	
	public static void updateCandy(String name) {
		String query1 = "update student set candy_active = ? where name = ? and class = ?";
		String query2 = "INSERT INTO candy (name,class) VALUES (?,?)";
		try {
			PreparedStatement prep1 = DatabaseHandler.getConnection().prepareStatement(query1);
			prep1.setString(1, "n");
			prep1.setString(2, name);
			prep1.setString(3, currentClass);
			prep1.executeUpdate();
			prep1.close();
			
			PreparedStatement prep2 = DatabaseHandler.getConnection().prepareStatement(query2);
			prep2.setString(1, name);
			prep2.setString(2, currentClass);
			prep2.execute();
			prep2.close();
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Fel vid uppdatering i databas (candy): " + ex.getMessage());
		}
	}
	
	
	public static void updateCQ(String name, int number, int answer, String topic) {
		String query1 = "UPDATE student SET CQ_active = ? WHERE name = ? and class = ?";
		String query2 = "INSERT INTO CQ_result (name,class,question,correct,topic) VALUES (?,?,?,?,?)";
		
		String ans;
		if(answer == DatabaseHandler.CORRECT) ans = "y";
		else if(answer == DatabaseHandler.WRONG) ans = "n";
		else ans = "a";
		
		try {
			if(answer != DatabaseHandler.ABSENT) {//denna iffen är ny
				PreparedStatement prep1 = DatabaseHandler.getConnection().prepareStatement(query1);
				prep1.setString(1, "n");
				prep1.setString(2, name);
				prep1.setString(3, currentClass);
				prep1.executeUpdate();
				prep1.close();
			}
						
			PreparedStatement prep2 = DatabaseHandler.getConnection().prepareStatement(query2);
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
	
	
	public static void updateTotal(String name, boolean first) {
		String query1 = "update student set total = total + 1 where name = ? and class = ?";
		
		if(first) DatabaseHandler.setSession();

		try {
			// Varje gång ska elevens totala uppdateras:
			PreparedStatement prep1 = DatabaseHandler.getConnection().prepareStatement(query1);
			prep1.setString(1, name);
			prep1.setString(2, currentClass);
			prep1.executeUpdate();
			prep1.close();
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Fel vid uppdatering i databas: " + ex.getMessage());
		}
	}
}
