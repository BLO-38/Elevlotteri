package databasen;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteHandler {
    public static boolean deleteKlass(String clName) {
        String query = "DELETE FROM student WHERE class = ?";
        return execute(query,clName);
    }
    public static boolean deleteStudent(Student student) {
        String query = "DELETE FROM student WHERE class = ? and name = ?";
        return execute(query,student);
    }
    private static boolean execute(String query, Object obj) {
        boolean isStudent = false;
        String studentName = "xxx", klassName, messName;

        if (obj instanceof String) {
            klassName = (String) obj;
            messName = klassName;
        }
        else if (obj instanceof Student) {
            isStudent = true;
            studentName = ((Student) obj).getName();
            klassName = ((Student) obj).getKlass();
            messName = studentName;
        }
        else {
            JOptionPane.showMessageDialog(null,"Oväntat fel, kontrollnummer 666");
            return false;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "Är du säker på att du ska radera " + messName + "?");
        if (confirm != 0) return false;

        String sure = JOptionPane.showInputDialog("Detta går ej att återställa. Är du helt säker? Skriv JA isåfall");
        if(sure == null) return false;
        if(sure.equals("JA")) {
            boolean succeed = false;
            try {
                PreparedStatement prep = DatabaseHandler.getConnection().prepareStatement(query);
                prep.setString(1, klassName);
                if (isStudent) prep.setString(2, studentName);
                int i = prep.executeUpdate();
                JOptionPane.showMessageDialog(null, i + " st elever borttagna");
                prep.close();
                succeed = true;
            }
            catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Fel vid radering i databas: " + ex.getMessage());
            }
            return succeed;
        }
        return false;
    }
}
