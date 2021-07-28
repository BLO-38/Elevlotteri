package model;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.*;

public class ManualLottery extends LotteryType {

	public ManualLottery(JFrame frame){

		super("Lotteri",0,"M");
		JDialog dialog = new JDialog(frame, "Skriv namnen", true);
		dialog.setLayout(new FlowLayout());
		JTextArea textArea = new JTextArea();
		textArea.setBounds(10,10, 200,200);
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
		dialog.add(finishBButton);
		dialog.setSize(500, 500);
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
