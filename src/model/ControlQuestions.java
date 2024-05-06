package model;

import java.util.Collections;
import java.util.LinkedList;
import javax.swing.*;
import databasen.DatabaseHandler2;
import databasen.LiveUpdateHandler;
import databasen.NameListGetters;

public class ControlQuestions extends Lottery {
	
	private int count = 1;
	private String previousName = null;
	private String topic;

	public ControlQuestions(String cl, int grp) {
		super(cl, grp, "K");
		setCQ(true);
		String mess = "Vad handlar frågorna om?";
		while(true){
			topic = JOptionPane.showInputDialog(null, mess);
			if(topic == null || topic.length()<20) break;
			else mess = "För långt, försök igen";
		}
		startNames = NameListGetters.getCQList2(cl, grp);
	}

	@Override
	public LinkedList<String> reloadNames() {
		LinkedList<String> list = getStartNames();
		Collections.shuffle(list);
		return list;
	}

	@Override
	public void updateDatabase(String studentName, int answer) {
		if(previousName != null) {
			LiveUpdateHandler.updateCQ(previousName, className, count, answer, topic);
			if(answer == DatabaseHandler2.CORRECT) count++;
		}
		previousName = studentName;
	}
}
