package model;

import java.util.LinkedList;
import javax.swing.JOptionPane;

public class NameRemover {
	
	public static LinkedList<String> typeNames(LinkedList<String> names){
		
		StringBuilder sb = new StringBuilder("Vill du ta bort någon? ");
		
		for(String n : names) 
			sb.append(n + ","); 

		String input = JOptionPane.showInputDialog(null, sb.toString());
		if(input != null && input.length() > 0) {
		
			String[] namesToRemove = input.split(",");	
			for(int j=0; j<namesToRemove.length; j++) 
				namesToRemove[j]=namesToRemove[j].trim();
			
			if(namesToRemove != null && namesToRemove.length >0) {
				boolean show = false;
				StringBuilder build = new StringBuilder("Borttagna: ");
				String temp;
				
				nameloop:
				for(String n : namesToRemove) {
					temp = n;
					if(temp.length() == 0) continue nameloop;
					temp = temp.substring(0, 1).toUpperCase() + temp.substring(1);
					
					while(!names.remove(temp)) {
						temp = JOptionPane.showInputDialog(temp + " kunde inte tas bort. Prova annan stavning. Var noga med stor bokstav.");
						if(temp == null || temp.length() == 0) continue nameloop;
					}
					build.append(temp + ",");
					show = true;
				}
				if(show) JOptionPane.showMessageDialog(null, build.toString());
			}
		}
		while(true) {
			String newName = JOptionPane.showInputDialog("Ska någon mer vara med?");
			if(newName == null || newName.length()==0) break;
			names.add(newName);
		}
	
		return names;
	}
}
