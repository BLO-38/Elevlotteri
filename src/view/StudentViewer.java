package view;

/*
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import databasen.Student;
*/
import java.util.LinkedList;
//import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.GridLayout;

public class StudentViewer {
	
	
	public static void showClass(LinkedList<String> textRows){//, int gr1, int gr2) {
		
		JFrame nameFrame = new JFrame();
		nameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		nameFrame.setLayout(new GridLayout(textRows.size() + 2, 1));
		
		for(String s : textRows) {
			nameFrame.add(new JLabel(s));
		}
			
		//JTextArea ta = new JTextArea();
		//nameFrame.add(ta);
		JLabel lab = new JLabel("Hej");
		nameFrame.add(lab);
		//ta.append("Hej");
		
		/*
		JDialog dia = new JDialog(nameFrame, true);
		dia.setSize(260, 620); // 250
		dia.setLayout(new FlowLayout());
		
		//JLabel sessmess = new JLabel("Antal lotterier:  Gr1: " + 99 + "    Gr2: " + 100);
		
		JButton butt = new JButton("Tillbaka");
		int rows = textRows.size();
		String[][] data = new String[rows][5]; // 3
		int i = 0;
		for(Student e : students) {
			data[i][0] = e.getName(); 
			data[i][1] = String.valueOf(e.getTotal());
			data[i][2] = String.valueOf(e.getCorrect());
			data[i][3] = String.valueOf(e.getWrong());
			data[i][4] = String.valueOf(e.getAbsent());
			i++;
		}
		String[] columnNames = {"Namn","Totalt","R�tt","Fel","Frv"};
		JTable t = new JTable(data, columnNames);
		t.getColumn("Totalt").setMaxWidth(50);
		t.getColumn("R�tt").setMaxWidth(50);
		t.getColumn("Fel").setMaxWidth(40);
		t.getColumn("Namn").setMaxWidth(100);
		t.getColumn("Frv").setMaxWidth(40);
		DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
		cr.setHorizontalAlignment(JLabel.CENTER);
		t.getColumnModel().getColumn(1).setCellRenderer(cr);
		t.getColumnModel().getColumn(2).setCellRenderer(cr);
		t.getColumnModel().getColumn(3).setCellRenderer(cr);
		t.getColumnModel().getColumn(4).setCellRenderer(cr);
		JScrollPane scr = new JScrollPane(t);
		scr.setPreferredSize(new Dimension(200, 500));

		butt.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				nameFrame.setVisible(false);
			}
		});
		
		dia.add(scr);
		dia.add(sessmess);
		dia.add(butt);
		*/
		nameFrame.setSize(260, 620); // 250
		nameFrame.setVisible(true);
		//dia.setVisible(true);
		//nameFrame.setVisible(false);
	}
}
