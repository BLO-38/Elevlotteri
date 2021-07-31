package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
//import javax.swing.JTextField;

import databasen.DatabaseHandler;
import model.BPLLottery;
import model.CandyLottery;
import model.ControlQuestions;
import model.FileLottery;
import model.LotteryType;
import model.MainHandler;
import model.ManualLottery;
import model.RegularLottery;

public class LotteryMenu {
	//TODO:
	// Småfönster i samma
	// Remove
	// Bordsplaceringen


	// private JFrame frame;
	private JFrame sourceFrame, featuresFrame;
	private JCheckBox checkBoxShowTaken, checkBoxShowNr, checkBoxPreview, checkBoxCandy, checkBoxCQ, checkBoxBPL;
	private ButtonGroup bgr;
	//private JTextField textField;
	private final MainHandler lotteryHandler;
	private final boolean isDataBaseActive;
	private JTextField removeTextField;
	
	
	public LotteryMenu(MainHandler sh, boolean db) {
		lotteryHandler = sh;
		isDataBaseActive = db;
	}
	
	public void startUp(LinkedList<String> classList) {

		sourceFrame = new JFrame();
		sourceFrame.setSize(1100, 250);
		sourceFrame.setLayout(new GridLayout(2, 1));

		JPanel classPanel = new JPanel();
		classPanel.setLayout(new FlowLayout());

		JPanel otherButtonsPanel = new JPanel();
		otherButtonsPanel.setLayout(new GridLayout(3, 1));
		JPanel manualPanel = new JPanel();
		manualPanel.setLayout(new FlowLayout());//GridBagLayout());
		// manualPanel.setLayout(new GridBagLayout());
		JPanel filePanel = new JPanel();
		filePanel.setLayout(new FlowLayout());
		JPanel settingsPanel = new JPanel();
		settingsPanel.setLayout(new FlowLayout());

		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new FlowLayout());

		JPanel groupPanel = new JPanel();
		groupPanel.setLayout(new GridLayout(3, 1));


		String dataBaseMessText;
		if(isDataBaseActive) {
			dataBaseMessText = "Aktiv databas: " + lotteryHandler.getDbName();
			JLabel dataBaseMess = new JLabel(dataBaseMessText);
			Border marg = new EmptyBorder(0, 0, 0, 50);
			dataBaseMess.setBorder(marg);
			dataBaseMess.setForeground(Color.BLUE);
			dataBaseMess.setOpaque(true);
			// dataBaseMess.setBackground(Color.WHITE);
			classPanel.add(dataBaseMess);

			if(classList != null) {
				for(String n : classList) {
					JButton b = new JButton(n);
					b.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent a) {
							String className = a.getActionCommand();
							int group = Integer.parseInt(bgr.getSelection().getActionCommand());
							System.out.println("Du vlade " + className + ", grupp " + group);
							DatabaseHandler.setCurrentClass(className, group);
							LotteryType lottery = null;
							String[] lotteryModes = {"Regular","Godis","Kontrollisfrågor"};
							int result = JOptionPane.showOptionDialog(sourceFrame,
								"Välj typ av lotteri",
								null,
								JOptionPane.DEFAULT_OPTION,
								JOptionPane.QUESTION_MESSAGE,
								null,
								lotteryModes,
								null);
							if (result == 0) {
								System.out.println("Regular!");
								lottery = new RegularLottery(className, group);
							} else if (result == 1) {
								lottery = new CandyLottery(className, group);
								System.out.println("GOdis!");
							} else if (result == 2) {
								System.out.println("QC");
								lottery = new ControlQuestions(className,group,sourceFrame);
							} else {return;}
							sourceFrame.setVisible(false);
							nextMenu(lottery);
						}
					});
					classPanel.add(b);
				}
				bgr = new ButtonGroup();
				JRadioButton gr1button = new JRadioButton("Grupp 1");
				gr1button.setActionCommand("1");
				JRadioButton gr2button = new JRadioButton("Grupp 2");
				gr2button.setActionCommand("2");
				JRadioButton allButton = new JRadioButton("Helklass");
				allButton.setActionCommand("0");
				allButton.setSelected(true);
				bgr.add(allButton);
				bgr.add(gr1button);
				bgr.add(gr2button);
				groupPanel.add(allButton);
				groupPanel.add(gr1button);
				groupPanel.add(gr2button);
				groupPanel.setBorder(new EmptyBorder(0, 30, 0, 0));
				classPanel.add(groupPanel);
			}

		} else {
			dataBaseMessText = "Ingen databas aktiv";
			JLabel dataBaseMess = new JLabel(dataBaseMessText);
			dataBaseMess.setFont(new Font("arial", 0, 18));
			dataBaseMess.setForeground(Color.RED);
			dataBaseMess.setOpaque(true);
			dataBaseMess.setBackground(Color.WHITE);
			classPanel.add(dataBaseMess);
		}

		JButton manualButton2 = new JButton("Mata in namn");
		manualButton2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LotteryType lottery = new ManualLottery(sourceFrame);
				nextMenu(lottery);
			}
		});

		JButton fromFileButton2 = new JButton("Hämta från fil");
		fromFileButton2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LotteryType lottery = new FileLottery();
				nextMenu(lottery);
			}
		});

		JButton settingsButton2 = new JButton("Ändra inställningar");
		settingsButton2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sourceFrame.setVisible(false);
				lotteryHandler.startStatsHandling();
				// System.exit(0);
			}
		});

		manualPanel.add(manualButton2); //, new GridBagConstraints());
		// manualPanel.setBorder(new EmptyBorder(30, 0, 30, 0));
		filePanel.add(fromFileButton2); //, new GridBagConstraints());
		settingsPanel.add(settingsButton2);
		otherButtonsPanel.add(manualPanel);
		otherButtonsPanel.add(filePanel);
		otherButtonsPanel.add(settingsPanel);
		sourceFrame.add(classPanel);
		sourceFrame.add(otherButtonsPanel);
		sourceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sourceFrame.setVisible(true);
	}

	private void nextMenu(LotteryType lottery) {
		sourceFrame.setVisible(false);
		System.out.println("Next menu");
		featuresFrame = new JFrame();
		featuresFrame.setSize(1000, 200);
		featuresFrame.setLayout(new GridLayout(4, 1));

		// JPanel messagePanel = new JPanel();
		JPanel featuresPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		JPanel removePanel = new JPanel();
		// messagePanel.setLayout(new FlowLayout());
		featuresPanel.setLayout(new FlowLayout());
		buttonsPanel.setLayout(new FlowLayout());
		removePanel.setLayout(new FlowLayout());

		JButton startButton = new JButton("Starta lotteri");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lottery.setShowCount(checkBoxShowNr.isSelected());
				lottery.setSaveNames(checkBoxShowTaken.isSelected());
				String [] namesToRemove = removeTextField.getText().split(",");
				boolean success = true;
				StringBuilder failNames = new StringBuilder("Det gick inte att ta bort ");
				for (String n1 : namesToRemove) {
					String n2 = n1.trim();
					System.out.println("Vi kollar " + n2);
					if(n2.length() != 0 && !lottery.removeName(n2)) {
						failNames.append(n2);
						failNames.append(" ");
						success = false;
					}
				}
				if (!success) {
					JOptionPane.showMessageDialog(featuresFrame, failNames + ". Försök igen.", "Fel!", JOptionPane.ERROR_MESSAGE);
					removeTextField.setText("");
					return;
				}
				System.out.println("Visa nr: " + checkBoxShowNr.isSelected());
				System.out.println("Visa tagna: " + checkBoxShowTaken.isSelected());
				featuresFrame.setVisible(false);
				lotteryHandler.startLottery(lottery);
				// sourceFrame.setVisible(false);
				// lotteryHandler.startStatsHandling();
			}
		});
		JButton seatingButton = new JButton("Bordsplacering");
		seatingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Bordsplacering");
				// frame.setVisible(false);
				// lotteryHandler.startStatsHandling();
			}
		});
		JButton backButton = new JButton("Tillbaka");
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Tillbaka");
				System.out.println("Metoden");
				featuresFrame.setVisible(false);
				sourceFrame.setVisible(true);
				// lotteryHandler.startStatsHandling();
			}
		});
		JButton previewButton = new JButton("Tjuvtitta på namnen");
		previewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// showPeek(lottery.getClassName(), lottery.getGroup());
				showPeek2(lottery);
			}
		});
		buttonsPanel.add(backButton);
		buttonsPanel.add(previewButton);
		buttonsPanel.add(seatingButton);
		buttonsPanel.add(startButton);

		JLabel headerText = new JLabel("Extraval:");
		headerText.setFont(new Font(null, Font.BOLD, 14));
		headerText.setBorder(new EmptyBorder(0, 0, 0, 40));
		featuresPanel.add(headerText);
		checkBoxShowTaken = new JCheckBox("Visa alla som lottats fram     ", false);
		featuresPanel.add(checkBoxShowTaken);
		checkBoxShowNr = new JCheckBox("Visa antal kvar     ", false);
		featuresPanel.add(checkBoxShowNr);


		JLabel removeText = new JLabel("Namn att ta bort:");
		removePanel.add(removeText);
		removeTextField = new JTextField(40);
		removePanel.add(removeTextField);

		featuresFrame.add(featuresPanel);
		featuresFrame.add(removePanel);
		featuresFrame.add(buttonsPanel);
		featuresFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		featuresFrame.setVisible(true);

	}

	private void showPeek(String className, int gr) {
		String groupText;
		if(gr == 0)	groupText = " helklass: ";
		else		groupText = " grupp " + gr + ": ";
			
		StringBuilder sb = new StringBuilder(className + groupText);

		for(String n : lotteryHandler.getNames(className, gr))
			sb.append(n + ", ");

		JOptionPane.showMessageDialog(featuresFrame, sb.toString());
	}
	private void showPeek2(LotteryType lottery) {
		System.out.println("Nya peeken");
		String groupText;
		if(lottery.getGroup() == 0)	groupText = " helklass: ";
		else		groupText = " grupp " + lottery.getGroup() + ": ";

		StringBuilder sb = new StringBuilder(lottery.getClassName() + groupText);

		for(String n : lottery.getStartNames())
			sb.append(n + ", ");

		JOptionPane.showMessageDialog(featuresFrame, sb.toString());
	}
}
//		classPanelGroups.add(allButton);
//		classPanelGroups.add(gr2button);
//		classPanelGroups.add(gr1button);


		/*if(isDataBaseActive) optionsPanel2.add(groupPanel);
		String dataBaseMessText;
		if(isDataBaseActive) {
			dataBaseMessText = "Databas aktiv: " + lotteryHandler.getDbName();
			JLabel dataBaseMess = new JLabel(dataBaseMessText);
			dataBaseMess.setForeground(Color.BLUE);
			dataBaseMess.setOpaque(true);
			dataBaseMess.setBackground(Color.WHITE);
			optionsPanel2.add(dataBaseMess);
		}
*/
		/*JButton statsButton = new JButton("Statistik och nya personer");
		statsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				lotteryHandler.startStatsHandling();
			}
		});
*/
		/*JButton manualButton = new JButton("Mata in namn");
		manualButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LotteryType lottery = new ManualLottery();
				lottery.setShowCount(checkBoxShowNr.isSelected());
				lottery.setSaveNames(checkBoxShowTaken.isSelected());

				frame.setVisible(false);
				lotteryHandler.startLottery(lottery);
				// doAfterChoice(lottery, "Manuell");
			}
		});
*/
// Ny 2a

/*
		JButton fromFileButton = new JButton("Hämta från fil");
		fromFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LotteryType lottery = new FileLottery();
				lottery.setShowCount(checkBoxShowNr.isSelected());
				lottery.setSaveNames(checkBoxShowTaken.isSelected());
				frame.setVisible(false);
				// doAfterChoice(lottery, "Fil");
				lotteryHandler.startLottery(lottery);
			}
		});
*/
/*
		JButton settingsButton = new JButton("Ändra inställningar");
		settingsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				lotteryHandler.setNewSettings();
				System.exit(0);
			}
		});
*/
/*

		checkBoxShowTaken = new JCheckBox("Visa alla som lottats fram     ", false);
		optionsPanel2.add(checkBoxShowTaken);
		checkBoxShowNr = new JCheckBox("Visa antal kvar     ", false);
		optionsPanel2.add(checkBoxShowNr);

		checkBoxCandy = new JCheckBox("Godis     ", false);
		optionsPanel2.add(checkBoxCandy);

		checkBoxBPL = new JCheckBox("Bordsplacering     ", false);
		optionsPanel2.add(checkBoxBPL);

		checkBoxCQ = new JCheckBox("Kontrollfrågor     ", false);
		optionsPanel2.add(checkBoxCQ);

 */