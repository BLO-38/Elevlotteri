package model;

import databasen.Student;

import java.util.Collections;
import java.util.LinkedList;

public abstract class Lottery {
	
	private boolean saveNames = false;
	private boolean showCounting = false;
	private boolean controlQuestions = false;
	private final String className;
	private final String type;
	private final int groupNr;
	private int scale = 1;
	protected LinkedList<String> startNames = null;
	protected LinkedList<Student> students = null;
	
	public Lottery(String cl, int grp, String t){
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
	public LinkedList<Student> getStudents() {
		return students;
	}

	public void removeName(String name) {
		startNames.remove(name);
	}
	public void addName(String name) {startNames.add(name);}

	public int getScale() {
		return scale;
	}
	public void setScale(int sc) {
		scale = sc;
	}
	public abstract LinkedList<String> reloadNames();

	public abstract void updateDatabase(String studentName, int answer);
	
	public String getClassName(){
		return className;
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
	public void shuffleStartnames() {
		Collections.shuffle(startNames);
	}
}
