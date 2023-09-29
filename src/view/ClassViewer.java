package view;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import databasen.Student;

public class ClassViewer {
	
	
	public static void showClass(LinkedList<Student> students){

		Collections.sort(students);
		
		JFrame nameFrame = new JFrame();
		JPanel panel = new JPanel();
		JPanel buttpanel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		buttpanel.setLayout(new GridBagLayout());
		nameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		nameFrame.setLayout(new FlowLayout());

		JButton butt = new JButton("Tillbaka");
		int rows = students.size();
		String[][] data = new String[rows][4];
		int i = 0;
		for(Student e : students) {
			data[i][0] = e.getName(); 
			data[i][1] = String.valueOf(e.getTotal());
//			data[i][2] = String.valueOf(e.getCorrect());
//			data[i][3] = String.valueOf(e.getWrong());
			i++;
		}
		String[] columnNames = {"Namn","Totalt","Rätt","Fel"};
		JTable t = new JTable(data, columnNames);
		t.getColumn("Totalt").setMaxWidth(50);
		t.getColumn("Rätt").setMaxWidth(50);
		t.getColumn("Fel").setMaxWidth(40);
		t.getColumn("Namn").setMaxWidth(100);
		t.setRowHeight(20);
		DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
		cr.setHorizontalAlignment(JLabel.CENTER);
		t.getColumnModel().getColumn(1).setCellRenderer(cr);
		t.getColumnModel().getColumn(2).setCellRenderer(cr);
		t.getColumnModel().getColumn(3).setCellRenderer(cr);

		JScrollPane scr = new JScrollPane(t);
		scr.setPreferredSize(new Dimension(200, rows*20+24));

		butt.addActionListener(e -> nameFrame.setVisible(false));

		panel.add(scr);
		buttpanel.add(butt);
		panel.add(buttpanel);
		nameFrame.add(panel);
		nameFrame.pack();
		nameFrame.setLocationRelativeTo(null);
		nameFrame.setVisible(true);
	}
}
