package databasen;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

import view.ClassChooser2;

public class InsertHandler {
	private static String resp;
	private static String classChioce;
	public static final int OK = 0;
	public static final int FANNS_REDAN = 1;
	public static final int ANNAT_FEL = 2;

	public static void setNewClass() {

		JFrame newClassFrame = new JFrame();
		newClassFrame.setLayout(new BorderLayout());

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.Y_AXIS));

		JPanel topp1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel l1 = new JLabel("Klass:");
		JTextField textField1 = new JTextField();
		textField1.setPreferredSize(new Dimension(150,25));
		JButton pickExistingButton = new JButton("Välj befintlig");
		pickExistingButton.addActionListener(e -> {
			new ClassChooser2(newClassFrame,response -> resp = response);
			textField1.setText(resp);
		});
		topp1.add(l1);
		topp1.add(textField1);
		topp1.add(Box.createRigidArea(new Dimension(20,0)));
		topp1.add(pickExistingButton);

		JPanel topp2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ButtonGroup group = new ButtonGroup();
		String[] buttTexts = {"Ingen grupp","Grupp 1","Grupp 2"};
		for (int i = 0; i < 3; i++) {
			JRadioButton rb = new JRadioButton(buttTexts[i]);
			group.add(rb);
			topp2.add(rb);
			topp2.add(Box.createRigidArea(new Dimension(20,0)));
			rb.setActionCommand(String.valueOf(i));
			if(i==0) rb.setSelected(true);
		}

		JPanel topp3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel l2 = new JLabel("Klistra in namnen, ett per rad:");
		topp3.add(l2);

		topPanel.add(topp1);
		topPanel.add(topp2);
		topPanel.add(topp3);

		newClassFrame.add(topPanel,BorderLayout.NORTH);

		JTextArea textArea = new JTextArea();
		textArea.setBackground(new Color(0xF6EFC9));
		textArea.setFont(new Font(null,Font.PLAIN,14));
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(300,400));
		newClassFrame.add(scrollPane,BorderLayout.CENTER);


		JButton finishKlass = new JButton("Verktäll!");
		finishKlass.addActionListener(e -> {
			String klass = textField1.getText().trim();
			int studentGroup = Integer.parseInt(group.getSelection().getActionCommand());
			String[] pastedNames = textArea.getText().split("\n");

			if(klass.isEmpty()) return;
			LinkedList<String> trimmedNames = new LinkedList<>();
			for (String n : pastedNames) {
				String n2 = n.trim();
				if(!n2.isEmpty()) trimmedNames.add(n2);
			}
			if(trimmedNames.isEmpty()) return;

			System.out.println("Du valde grupp " + studentGroup);
			System.out.println("Klass: " + klass);
			System.out.println("Antal namn: " + trimmedNames.size());
			System.out.println("Namnen: " + trimmedNames);

			for(String nameToInsert : trimmedNames) insertStudent(nameToInsert, klass, studentGroup);
			JOptionPane.showMessageDialog(null, "Klassen införd och klar");
			newClassFrame.dispose();
		});
		JPanel bottPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
		newClassFrame.add(bottPanel,BorderLayout.SOUTH);
		bottPanel.add(finishKlass);
		newClassFrame.pack();
		newClassFrame.setLocationRelativeTo(null);
		newClassFrame.setVisible(true);
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
	
	public static int insertStudent(String n, String cl, int gr) {
		String name = n.replaceAll("[.,]", "");
		String query = "INSERT INTO student (name,class,grp) VALUES (?,?,?)";
		try {
			PreparedStatement prep = DatabaseHandler2.getConnection().prepareStatement(query);
			prep.setString(1, name.trim());
			prep.setString(2, cl);
			prep.setInt(3, gr);
			int resultCount = prep.executeUpdate();
			System.out.println(resultCount);
			prep.close();
			return OK;
		}
		catch (SQLException e) {
			System.out.println(e.getErrorCode());
			System.out.println(e.getSQLState());
			System.out.println(e.getMessage());
			if(e.getErrorCode() == 19) return FANNS_REDAN;
			else return ANNAT_FEL;
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
			PreparedStatement prep = DatabaseHandler2.getConnection().prepareStatement(query);
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

	public static boolean insertNeighbors(LinkedList<String[]> neighbors, String klass) {
		boolean success = true;
		String query = "INSERT INTO neighbors (class,student1,student2) VALUES (?,?,?)";
		try {
			PreparedStatement prep = DatabaseHandler2.getConnection().prepareStatement(query);
			for (String[] pair : neighbors) {
				prep.setString(1, klass);
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

	public static void setSession(String klass, int grp) {
		try {
			String query1 = "INSERT INTO regular_session (class,grp) VALUES (?,?)";
			PreparedStatement prep = DatabaseHandler2.getConnection().prepareStatement(query1);
			prep.setString(1, klass);
			prep.setInt(2, grp);
			prep.execute();
			prep.close();
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Ett fel uppstod vid skapande av ny session: " + e.getMessage());
		}
	}

}
