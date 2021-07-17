package view;

import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class DynamicNameViewer {
	
	private static int count = 0, maxNames = 10;
	private static JFrame nameFrame;
	private static JLabel[] labels;
	
	
	public static void showDynamicList() {
		nameFrame = new JFrame();
		nameFrame.setLayout(new GridLayout(maxNames, 1));
		labels = new JLabel[maxNames];
		
		for(int i=0; i<maxNames; i++) {
			JLabel l = new JLabel("Nr " + (i+1) + ":");
			labels[i] = l;
			nameFrame.add(l);
		}
		nameFrame.setSize(200, 400);
		nameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		nameFrame.setVisible(true);
		
	}
	
	public static void addName(String name) {
		if(count == maxNames) return;
		labels[count].setText("Nr " + (count+1) + ":    " + name);
		count++;
	}
	
	public static void reset() {
		count = 0;
	}
	
	public static void setMaxCount(int max) {
		maxNames = max;
	}
}



/*

public static void showClass(ArrayList<Student> list, DatabaseHandler handler) {
	
	JButton butt = new JButton("Tilbaka");
	int rows = list.size();
	String[][] data = new String[rows][3];
	int i = 0;
	for(Student e : list) {
		data[i][0] = e.getName(); 
		data[i][1] = String.valueOf(e.getTotal());
		data[i][2] = String.valueOf(e.getFirstPlaces());
		i++;
	}
	String[] columnNames = {"Namn","Totalt","1a"};
	
	JFrame frame = new JFrame();
	frame.setLayout(new FlowLayout());
	JTable t = new JTable(data, columnNames);
	
	t.getColumn("Totalt").setMaxWidth(50);
	t.getColumn("1a").setMaxWidth(40);
	t.getColumn("Namn").setMaxWidth(100);
	DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
	cr.setHorizontalAlignment(JLabel.CENTER);
	t.getColumnModel().getColumn(1).setCellRenderer(cr);
	t.getColumnModel().getColumn(2).setCellRenderer(cr);
	JScrollPane scr = new JScrollPane(t);
	scr.setPreferredSize(new Dimension(200, 500));
	frame.add(scr);
	frame.add(butt);
	frame.setSize(250, 600);
	//frame.pack();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
	
	butt.addActionListener(new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			frame.setVisible(false);
			handler.showMenu();
		}
	});
}
*/