package model;

import view.LotteryMenu;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.UndoableEditListener;

public class ManualLottery extends Lottery {

	public ManualLottery(){

		super("Lotteri",0,"M");
		JFrame manualFrame = new JFrame("Skriv namnen");
		manualFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		manualFrame.setLayout(new BoxLayout(manualFrame.getContentPane(), BoxLayout.Y_AXIS));
		JLabel lab = new JLabel("Skriv alla namn med komma emellan:");
		JPanel p1 = new JPanel(new FlowLayout());
		p1.add(lab);
		manualFrame.add(p1);
		JTextArea textArea = new JTextArea();
		textArea.setForeground(Color.BLUE);
		textArea.setBackground(new Color(244, 228, 115));

		textArea.setPreferredSize(new Dimension(300, 400));
		textArea.setBorder(new EmptyBorder(20, 20, 20, 20));
		manualFrame.add(textArea);
		textArea.setLineWrap(true);
		startNames = new LinkedList<>();

		JButton finishBButton = new JButton("Klar");
		 finishBButton.addActionListener(e -> {
			 String allNames = textArea.getText();
			 if(allNames.trim().isEmpty()) return;
			 String [] nameArray = allNames.split(",");
			 for(String name : nameArray) {
				 String trimmedName = name.trim();
				 if (!trimmedName.isEmpty()) {
					 startNames.add(trimmedName);
				 }
			 }
			 Collections.shuffle(startNames);
			 if(!startNames.isEmpty()) new LotteryMenu(this);
			 manualFrame.setVisible(false);
		 });
		 JPanel panel = new JPanel();
		 panel.setLayout(new FlowLayout());
		 panel.add(finishBButton);
		 manualFrame.add(panel);
		 manualFrame.pack();
		 manualFrame.setLocationRelativeTo(null);
		 manualFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		 manualFrame.setVisible(true);
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
