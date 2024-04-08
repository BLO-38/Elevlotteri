package databasen;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Resetters {

	public Resetters() {}
	
	public static void resetCandy(int grp, String cl) {
		// int grp = DatabaseHandler.getCurrentGroup();
		String query = "update student set candy_active = ? where class = ?";
		if (grp>0) query += " AND grp = ?";
		try {
			PreparedStatement prep = DatabaseHandler2.getConnection().prepareStatement(query);
			prep.setString(1, "y");
			prep.setString(2, cl);
//			prep.setString(2, DatabaseHandler.getCurrentClass());
			if(grp > 0) prep.setInt(3, grp);
			prep.executeUpdate();
			prep.close();
		}
		catch (SQLException ex) {
			System.out.println("Fel 300");
			JOptionPane.showMessageDialog(null, "Fel vid uppdatering i databas (candy): " + ex.getMessage());
		}
	}

	
}
