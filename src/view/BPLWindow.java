package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BPLWindow {
	private JFrame frame;
	private JPanel panel;
	private int size, columns = 1, rows;
	
	private LinkedList<String> names = null;
	private LinkedList<JLabel> labels = null;
	private String classname;
	private JLabel temp;
	private int[] seats = new int[22];

	public BPLWindow(String c, LinkedList<String> n) {
		names = n;
		classname = c;
		size = names.size();
		labels = new LinkedList<JLabel>();
		setSeats();
		setUpWindowSimple();
	}
	
	private void setUpWindow() {
		//if(size > 22) columns = 2;
		
		rows = 44;
		frame = new JFrame("Bordsplacering");
		//panel = new JPanel();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setSize(200, 700);
		System.out.println("Rader: " + rows);
		System.out.println("Kolumner: " + columns);
		System.out.println("Antal namn: " + size);
		frame.setLayout(new GridLayout(rows,columns));
		frame.getContentPane().setBackground(Color.BLACK);
		
		for(int i=0; i<names.size(); i++) {
			temp = new JLabel();
			temp.setFont(new Font(null, Font.BOLD, 15));
			temp.setHorizontalAlignment(JLabel.LEFT);
			temp.setForeground(Color.YELLOW);
			labels.add(temp);
			frame.add(temp);
		}
		
		
		frame.setVisible(true);
		/*
		for(String s : names) {
			temp = labels.poll();
			temp.setText("  " + s);
			
			
			try        
			{
			    System.out.println("Pausen");
				Thread.sleep(1000);
			} 
			catch(InterruptedException ex) 
			{
			    Thread.currentThread().interrupt();
			}
		}
		
		
		*/
	}
		
	private void setUpWindowSimple() {
		
		rows = 48;
		frame = new JFrame("Bordsplacering");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(200, 700);
		frame.setLayout(new GridLayout(rows,columns));
		frame.getContentPane().setBackground(Color.BLACK);
		
		int seat=1;
		int row=0;
		
		for(String s : names) {	
			if(seat == 1) {
				temp = new JLabel("  RAD " + (row+1));
				temp.setFont(new Font(null, Font.BOLD, 18));
				temp.setHorizontalAlignment(JLabel.LEFT);
				temp.setForeground(Color.WHITE);
				frame.add(temp);
			}
			temp = new JLabel("     " + s);
			temp.setFont(new Font(null, Font.BOLD, 15));
			temp.setHorizontalAlignment(JLabel.LEFT);
			temp.setForeground(Color.YELLOW);
			frame.add(temp);
			if(seat == seats[row]) {
				row++;
				seat=1;
			}
			else seat++;
			
		}
		
		JOptionPane.showMessageDialog(null, "Klicka ok!");
		frame.setVisible(true);
		/*
		for(String s : names) {
			temp = labels.poll();
			temp.setText("  " + s);
			
			
			try        
			{
			    System.out.println("Pausen");
				Thread.sleep(1000);
			} 
			catch(InterruptedException ex) 
			{
			    Thread.currentThread().interrupt();
			}
		}
		
		
		*/
		
	
	
	
	
		
		
	}
	
	private void setSeats() {
		int n;
		int i=0;
		String text;
		while(true) {
			text = JOptionPane.showInputDialog("Skriv antal bänkar på rad nr " + (i+1) + ":");
			if(text == null || text.length() == 0) break;
			try {
				n = Integer.parseInt(text);
				if(n == 0) break;
				seats[i] = n;
				i++;
			}
			catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Du skrev inte en siffra");
			}
			
		}
		
		int k=0;
		while(seats[k] > 0) {
			System.out.println("Rad nr " + (k+1) + ": " + seats[k] + " st platser.");
			k++;
		}
			
		
	}
	
	


}
