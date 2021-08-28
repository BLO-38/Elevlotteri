package model;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ManualLottery extends LotteryType {

	public ManualLottery(JFrame frame){

		super("Lotteri",0,"M");
		JDialog dialog = new JDialog(frame, "Skriv namnen", true);
		dialog.getContentPane().setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
		JScrollPane p = new JScrollPane();//  JPanel(new FlowLayout(FlowLayout.CENTER, 50, 50));
		// JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 50));
		p.setForeground(Color.CYAN);
		p.setBackground(Color.YELLOW);
		JTextArea textArea = new JTextArea();
		textArea.setForeground(Color.BLUE);
		textArea.setBackground(new Color(255, 255, 255));
		// textArea.setBounds(10,10, 200,200);
		textArea.setPreferredSize(new Dimension(200, 400));
		textArea.setBorder(new EmptyBorder(20, 20, 20, 20));
		p.add(textArea);
		dialog.add(p);
		// dialog.add(textArea);
		textArea.setLineWrap(true);

		JButton finishBButton = new JButton("Klar");
		 finishBButton.addActionListener(e -> {
			 startNames = new LinkedList<>();
			 String allNames = textArea.getText();
			 String [] nameArray = allNames.split(",");
			 for(String name : nameArray) {
				 String trimmedName = name.trim();
				 if (trimmedName.length() != 0) {
					 startNames.add(trimmedName);
				 }
			 }
			 Collections.shuffle(startNames);
			 dialog.setVisible(false);
		 });
		dialog.add(finishBButton);
		dialog.pack();
		// dialog.setSize(500, 500);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	}

	@Override
	public LinkedList<String> reloadNames() {
		Collections.shuffle(startNames);
		System.out.println("Namnen har shufflats.");
		return new LinkedList<>(startNames);
	}
	
	@Override
	public void updateDatabase(String studentName, int answer){
	};

}
