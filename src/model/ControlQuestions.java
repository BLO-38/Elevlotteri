package model;

import java.util.Collections;
import java.util.LinkedList;
import javax.swing.*;
import databasen.DatabaseHandler;
import databasen.LiveUpdateHandler;

public class ControlQuestions extends Lottery {
	
	private int count = 1;
	private String previousName = null;
	private String topic;

	public ControlQuestions(String cl, int grp, JFrame frame) {
		super(cl, grp, "K");
		setCQ(true);
		String mess = "Vad handlar frågorna om?";
		while(true){
			topic = JOptionPane.showInputDialog(frame, mess);
			if(topic == null || topic.length()<20) break;
			else mess = "För långt, försök igen";
		}
		startNames = DatabaseHandler.getCQList2();
	}

	@Override
	public LinkedList<String> reloadNames() {
		System.out.println("Vi returnerar hela klassen reguljärt");
		LinkedList<String> list = DatabaseHandler.getNamesRegular();
		Collections.shuffle(list);
		return list;
	}

	@Override
	public void updateDatabase(String studentName, int answer) {
		if(previousName != null && answer != DatabaseHandler.ABSENT) {
			LiveUpdateHandler.updateCQ(previousName, count, answer, topic);
			count++;
		}
		previousName = studentName;
	}
}
