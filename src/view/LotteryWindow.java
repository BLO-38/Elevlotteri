package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import databasen.DatabaseHandler;
import model.MainHandler;

public class LotteryWindow {
	private JFrame frame;// = new JFrame("Lottning");
	private JPanel nextButtonsPanel;
	private JLabel nameLabel = new JLabel();
	private JLabel countLabel = new JLabel();
	private JButton nextButton = new JButton("N ä s t a");
	private JButton nextButtonPart1 = new JButton();
	private JButton nextButtonPart3 = new JButton();
	private int height = 90, rows = 3;// 900
	private boolean showCount = true;
	private MainHandler handler;
	private boolean isCQ = false;
	private int scale = 7; 
	private int fontHeight = 7;

	public LotteryWindow(MainHandler sh, int total, boolean showNumbers, String currentClass, boolean cq, String title, int scaleParam) {
		frame = new JFrame(title);
		scale = scaleParam;
		isCQ = cq;
		showCount = showNumbers;
		handler = sh;
		countLabel.setText("Antal elever: " + total);
		nameLabel.setText(currentClass);
		height *= scale;
		fontHeight *= scale;
		if(!showCount) {
			height = height*2/3;
			rows = 2;
		}
		
		if(isCQ) rows = 2;
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(120*scale, height);
		frame.setLayout(new GridLayout(rows,1));
		frame.add(nameLabel);
		
		if(isCQ){
			nextButtonsPanel = new JPanel();
			nextButtonsPanel.setLayout(new GridLayout(1, 3));
			nextButtonsPanel.setPreferredSize(new Dimension(50*scale, 10*scale));
			//nextButtonPart1.setFont(new Font(null, Font.BOLD, fontHeight));
			nextButton.setFont(new Font(null, Font.BOLD, fontHeight/2));
			//nextButtonPart3.setFont(new Font(null, Font.BOLD, fontHeight));
			nextButtonPart1.setBorderPainted(false);
			nextButton.setBorderPainted(false);
			nextButtonPart3.setBorderPainted(false);
			nextButtonPart1.setHorizontalAlignment(SwingConstants.RIGHT);
			nextButton.setHorizontalAlignment(SwingConstants.CENTER);
			nextButtonsPanel.add(nextButtonPart1);
			nextButtonsPanel.add(nextButton);
			nextButtonsPanel.add(nextButtonPart3);
			frame.add(nextButtonsPanel, BorderLayout.CENTER);
		}
		else {
			if(showCount) frame.add(countLabel);
			frame.add(nextButton);
			nextButton.setFont(new Font(null, Font.BOLD, fontHeight/2));
			countLabel.setFont(new Font(null, Font.BOLD, fontHeight));
			countLabel.setForeground(Color.CYAN);
			countLabel.setHorizontalAlignment(JLabel.CENTER);
		}
		
		nameLabel.setFont(new Font(null, Font.BOLD, fontHeight*2));
		nameLabel.setHorizontalAlignment(JLabel.CENTER);
		nameLabel.setForeground(Color.YELLOW);
		frame.getContentPane().setBackground(Color.BLACK);
		
		frame.setVisible(true);
		
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				handler.pickNext(DatabaseHandler.ABSENT);
			}
		});
		
		
		nextButtonPart1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				handler.pickNext(DatabaseHandler.CORRECT);
			}
		});
		
		
		nextButtonPart3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				handler.pickNext(DatabaseHandler.WRONG);
			}
		});
		
		
	    frame.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent event) {
	        	handler.closeDatabase();
	        	System.out.println("Nu avslutas programmet");
	            System.exit(0);
	        }
	    });
	}
	
//	private int getScale() {
//		String[] sizes = {"XS","S","M","L","XL","Full"};
//		int result = JOptionPane.showOptionDialog(null,
//									   "Vlj storlek p lotterifnstret",
//									   null,
//									   JOptionPane.DEFAULT_OPTION,
//									   JOptionPane.QUESTION_MESSAGE,
//									   null,
//									   sizes,
//									   sizes[2]);
//		int sc = result*2 + 1;
//		if (result == 0) sc++;
//		return sc;
//	}

	public void update(String newName, int count) {
		nameLabel.setText(newName);
		if(showCount) countLabel.setText("Antal kvar: " + count);
	}

}
