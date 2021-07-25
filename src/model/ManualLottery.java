package model;

import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class ManualLottery extends LotteryType {
	
	private boolean first = true;
	private LinkedList<String> names = null;
	
	public ManualLottery(){
		super("Lotteri",0,"M");
	}

	@Override
	public LinkedList<String> reloadNames() {
		if(first){
			first = false;
			names = new LinkedList<String>();
			String inp;
			while(true)	{
				inp = JOptionPane.showInputDialog("Skriv namn");	
				if(inp == null || inp.length()<1) break;
				names.add(inp);
			} 		
			if(names.size() == 0) { 
				JOptionPane.showMessageDialog(null, "Inga namn. Programmet avslutas.");
				System.exit(0);
			}
		}
		Collections.shuffle(names);
		System.out.println("Namnen har shufflats.");
		return new LinkedList<String>(names);
	}
	
	@Override
	public void updateDatabase(String studentName, int answer){
		return;
	};

}
