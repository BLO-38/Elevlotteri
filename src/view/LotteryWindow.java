package view;

import java.awt.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import databasen.DatabaseHandler2;
import model.Lottery;

@SuppressWarnings("FieldCanBeLocal")
public class LotteryWindow {
	private final JLabel nameLabel = new JLabel();
	private final JLabel countLabel = new JLabel();
	private int height = 90, rows = 3;// 900
	private final boolean showCount;
	private final int scale; // 7 e nån slags default
	private int fontHeight = 7;
	private final Lottery lottery;

	public LotteryWindow(Lottery l, int total, boolean showNumbers, String currentClass, boolean cq, String title, int scaleParam) {
		lottery = l;
		JFrame frame = new JFrame(title);
		scale = scaleParam;
		showCount = showNumbers;
		countLabel.setText("Antal elever: " + total);
		nameLabel.setText(currentClass);
		height *= scale;
		fontHeight *= scale;
		if (!showCount) {
			height = height * 2 / 3;
			rows = 2;
		}

		if (cq) rows = 2;
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		System.out.println("Skala: " + scale);
		if (scale == 12)
			frame.setSize(frame.getMaximumSize());
		else
			frame.setSize(120 * scale, height);

		frame.setLayout(new GridLayout(rows, 1));
		frame.add(nameLabel);

		JButton nextButton = new JButton("N ä s t a");
		JButton nextButtonPart1 = new JButton();
		JButton nextButtonPart3 = new JButton();
		if (cq) {
			JPanel nextButtonsPanel = new JPanel();
			nextButtonsPanel.setLayout(new GridLayout(1, 3));
			nextButtonsPanel.setPreferredSize(new Dimension(50 * scale, 10 * scale));
			nextButton.setFont(new Font(null, Font.BOLD, fontHeight / 2));
			nextButtonPart1.setBorderPainted(false);
			nextButton.setBorderPainted(false);
			nextButtonPart3.setBorderPainted(false);
			nextButtonPart1.setHorizontalAlignment(SwingConstants.RIGHT);
			nextButton.setHorizontalAlignment(SwingConstants.CENTER);
			nextButtonsPanel.add(nextButtonPart1);
			nextButtonsPanel.add(nextButton);
			nextButtonsPanel.add(nextButtonPart3);
			frame.add(nextButtonsPanel, BorderLayout.CENTER);
		} else {
			if (showCount) frame.add(countLabel);
			frame.add(nextButton);
			nextButton.setFont(new Font(null, Font.BOLD, fontHeight / 2));
			countLabel.setFont(new Font(null, Font.BOLD, fontHeight));
			countLabel.setForeground(Color.CYAN);
			countLabel.setHorizontalAlignment(JLabel.CENTER);
		}

		nameLabel.setFont(new Font(null, Font.BOLD, fontHeight * 2));
		nameLabel.setHorizontalAlignment(JLabel.CENTER);
		nameLabel.setForeground(Color.YELLOW);
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		nextButton.addActionListener(arg0 -> lottery.pickNext(DatabaseHandler2.ABSENT));
		nextButtonPart1.addActionListener(arg0 -> lottery.pickNext(DatabaseHandler2.CORRECT));
		nextButtonPart3.addActionListener(arg0 -> lottery.pickNext(DatabaseHandler2.WRONG));
	}

	public void update(String newName, int count) {
		nameLabel.setText(newName);
		if(showCount) countLabel.setText("Antal kvar: " + count);
	}

}
