package databasen;

public class Student {

	private int group;
	private String name, klass, cqActive, cqEver, candyActive,gender;
	private int total, correct, wrong;
	
	
	public Student() {
		
	}
	// public Student(String n, String k, int gr, int tot, int corr, int wr, String cqa, String cqe, String candy) {
	public Student(String n, String k, int gr, int tot, int corr, int wr, String candy, String gend) {

		name = n;
		klass = k;
		group = gr;
		total = tot;
		correct = corr;
		wrong = wr;
		// cqActive = cqa;
		// cqEver = cqe;
		candyActive = candy;
		gender = gend;

	}
	
	public String getName() {
		return name;
	}
	
	public int getTotal() {
		return total;
	}
	
	public int getCorrect() {
		return correct;
	}
	
	public int getWrong() {
		return wrong;
	}
	
	public String getKlass() {
		return klass;
	}
	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	@Override
	public String toString(){
		return "Elev: " + name + "      " + klass + ", grupp " + group + 
				"\nTot: " + total + "    Rätt: " + correct + "    Fel: " + wrong +
				"\nCandy_active: " + candyActive;
				// "\nCandy_active: " + candyActive + "\nCQ_active: " + cqActive + "      CQ_ever: " + cqEver;
	}

    public String getGender() {
		return gender;
    }

	public void setGender(String newGender) {
		gender = newGender;
	}
}
