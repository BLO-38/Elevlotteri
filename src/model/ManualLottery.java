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
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
		JLabel lab = new JLabel("Skriv alla namn med komma emellan:");
		JPanel p1 = new JPanel(new FlowLayout());
		p1.add(lab);
		dialog.add(p1);
		JTextArea textArea = new JTextArea();
		textArea.setForeground(Color.BLUE);
		textArea.setBackground(new Color(244, 228, 115));

		textArea.setPreferredSize(new Dimension(300, 400));
		textArea.setBorder(new EmptyBorder(20, 20, 20, 20));
		dialog.add(textArea);
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
		 JPanel panel = new JPanel();
		 panel.setLayout(new FlowLayout());
		 panel.add(finishBButton);
		 dialog.add(panel);
		 dialog.pack();
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
	}

}
