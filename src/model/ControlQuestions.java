package model;

import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import databasen.DatabaseHandler;
import databasen.LiveUpdateHandler;
import databasen.Resetters;

public class ControlQuestions extends LotteryType {
	
	private int count = 1, countAll = 0;
	private int studentsLeft;
	private String previousName = null;
	private String topic = null;
	private String mess = "Vad handlar frågorna om?";
	private boolean first;

	public ControlQuestions(String cl, int grp) {
		super(cl, grp, "K");
		setCQ(true);
		while(true){
			topic = JOptionPane.showInputDialog(mess);
			if(topic == null || topic.length()<20) break;
			else mess = "För långt, försök igen";
		}
		first = true;
	}

	
	@Override
	public LinkedList<String> reloadNames() {
		countAll = 0;
		LinkedList<String> names = DatabaseHandler.getCQList(true);
		studentsLeft = names.size();
		if(first) {
			JOptionPane.showMessageDialog(null, "Antal aktiva: " + studentsLeft);
			first = false;
		}
		if(names.size() == 0){
			//getDataBaseHandler().resetCQ();
			Resetters.resetCQ();
			JOptionPane.showMessageDialog(null, "FEL! Detta ska aldrig skrivas. GetStartList i Controlquestions.");
		}
		
		LinkedList<String> allNames = DatabaseHandler.getCQList(false);
		
		for(String n : names){
			if(!allNames.remove(n))
				JOptionPane.showMessageDialog(null, "Ska ej skrivas. Försökte ta bort en som inte fanns");
		}
		
		Collections.shuffle(names);
		Collections.shuffle(allNames);
		for(String s3 : allNames)
			names.add(s3);
		System.out.print("Listan blev: ");
		for(String s1 : names)
			System.out.print(s1 + ",");
		System.out.println();
		
		return names;
	}

	@Override
	public void updateDatabase(String studentName, int answer) {
		countAll++;
		if(previousName != null) {
			if(answer != DatabaseHandler.ABSENT)
				LiveUpdateHandler.updateCQ(previousName, count, answer, topic);
				//getDataBaseHandler().updateCQ(previousName, count, answer, topic);
			if(answer == DatabaseHandler.CORRECT) count++;
		}
		if(countAll == (studentsLeft+1)) {
			//getDataBaseHandler().resetCQ();
			Resetters.resetCQ();
		}
		previousName = studentName;
		return;
	}
}
