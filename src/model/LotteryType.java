package model;

import java.util.LinkedList;
import databasen.DatabaseHandler;

public abstract class LotteryType {
	
	private boolean saveNames = false;
	private boolean showCounting = false;
	private boolean controlQuestions = false;
	private boolean bpl = false;
	private String className;
	private String type;
	private int groupNr = 0;
	private DatabaseHandler dbHandler = null;
	protected LinkedList<String> startNames = null;
	
	public LotteryType(String cl, int grp, String t){
		System.out.println("Abstrakt konstr " + t);
		className = cl;
		groupNr = grp;
		type = t;
	}
	
	public void setSaveNames(boolean save){
		saveNames = save;
	}
	public void setShowCount(boolean show){
		showCounting = show;
	}
	public void setCQ(boolean cq){
		controlQuestions = cq;
	}
	public boolean isControlQuestions(){
		return controlQuestions;
	}

	public LinkedList<String> getStartNames() {
		return new LinkedList<>(startNames);
	}

	public void setBPL(boolean b){
		System.out.println("Inne i setbpl");
		bpl = b;
	}
	public boolean isBPL(){
		return bpl;
	}
	
	public abstract LinkedList<String> reloadNames();
	// public abstract void prepareNames();

	public abstract void updateDatabase(String studentName, int answer);
	
	public String getClassName(){
		return className;
	}
	public int getGroup(){
		return groupNr;
	}
	public boolean doSaveNames(){
		return saveNames;
	}
	public boolean doShowCount(){
		return showCounting;
	}
	public String getType() {
		return type;
	}
}
