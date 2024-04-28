package databasen;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Resetters {

	public Resetters() {}
	
	public static void resetCandy(int grp, String cl) {
		String query = "update student set candy_active = ? where class = ?";
		if (grp>0) query += " AND grp = ?";
		try {
			PreparedStatement prep = DatabaseHandler2.getConnection().prepareStatement(query);
			prep.setString(1, "y");
			prep.setString(2, cl);
			if(grp > 0) prep.setInt(3, grp);
			prep.executeUpdate();
			prep.close();
		}
		catch (SQLException ex) {
			System.out.println("Fel 300");
			JOptionPane.showMessageDialog(null, "Fel vid uppdatering i databas (candy): " + ex.getMessage());
		}
	}

	public static void resetGroupActive(String cl, int grp) {
		String query = "UPDATE student SET group_active = ? where class = ? AND group_active = ?";
		if (grp>0) query += " AND grp = ?";
		try {
			PreparedStatement prep = DatabaseHandler2.getConnection().prepareStatement(query);
			prep.setInt(1, DatabaseHandler2.AVAILABLE);
			prep.setString(2, cl);
			prep.setInt(3, DatabaseHandler2.UNAVAILABLE);
			if(grp > 0) prep.setInt(4, grp);
			prep.executeUpdate();
			prep.close();
		}
		catch (SQLException ex) {
			System.out.println("Fel 300");
			JOptionPane.showMessageDialog(null, "Fel vid uppdatering i databas (candy): " + ex.getMessage());
		}
	}
}
