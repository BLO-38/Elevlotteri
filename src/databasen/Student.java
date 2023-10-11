package databasen;

public class Student implements Comparable<Student>{

	private int group;
	private String name, klass, gender;
	private int total, cqScore; // Om lika med -1: deltar ej
	private boolean candyActive;
	private int correct, wrong;
	
	
	public Student() { }

	public Student(String n, String k, int gr, int tot, String candy, int cq, String gend, int c, int w) {

		name = n;
		klass = k;
		group = gr;
		total = tot;
		cqScore = cq;
		candyActive = candy.equals("y");
		gender = gend;
		correct = c;
		wrong = w;

	}
	
	public String getName() { return name; }
	public int getTotal() { return total; }
	public String getKlass() { return klass; }
	public int getGroup() { return group; }
	public void setGroup(int group) { this.group = group; }
    public String getGender() { return gender; }
	public void setGender(String newGender) { gender = newGender; }

	@Override
	public String toString(){
		String qkMess;
		if (cqScore == -1) qkMess = "Deltar ej";
		else qkMess = correct + " rätt, " + wrong + " fel";
		return  "Elev: " + name + "\nKlass: " + klass + ",  grupp " + group +
				"\nLottad: " + (total==-1?"-":total+" ggr") +
				"\nKan få godis: " + (candyActive ? "Ja" : "Nej") +
				"\nKontrollfrågor:  " + qkMess +
				"\nDeltar i prioriterat lotteri: " + (total== -1 ? "Nej": "Ja");
	}

	@Override
	public int compareTo(Student o) {
		return name.compareTo(o.getName());
	}

	public int getCorrect() {
		return correct;
	}

	public int getWrong() {
		return wrong;
	}
}
