package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import databasen.DatabaseHandler;
import model.CandyLottery;
import model.ControlQuestions;
import model.FileLottery;
import model.LotteryType;
import model.MainHandler;
import model.ManualLottery;
import model.RegularLottery;

public class LotteryMenu {

	private JFrame sourceFrame, featuresFrame;

	private JCheckBox checkBoxShowTaken, checkBoxShowNr;
	private ButtonGroup bgr, sizeGroup;
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
		manualPanel.setLayout(new FlowLayout());
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
							String[] lotteryModes = {"Regular","Godis","Kontrollisfrågor","Bordsplacering","Gruppindelning"};
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
							} else if (result == 3) {
								System.out.println("BPL");
								sourceFrame.setVisible(false);
								lottery = new RegularLottery(className, group);
								new SeatingMenu(lottery.getStartNames());
								return;
								// BPL
							} else if (result == 4) {
								System.out.println("Grupper");
								sourceFrame.setVisible(false);
								lottery = new RegularLottery(className, group);
								new GroupingMenu(lottery.getStartNames());
								return;
								// BPL
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
				System.out.println(e.toString());
				System.out.println(e.getActionCommand());
				System.out.println(e.getSource());
				System.out.println("x");
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

		JButton settingsButton = new JButton("Ändra inställningar");
		settingsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sourceFrame.setVisible(false);
				DatabaseHandler.showMenu(sourceFrame);
				// System.exit(0);
			}
		});

		manualPanel.add(manualButton2); //, new GridBagConstraints());
		// manualPanel.setBorder(new EmptyBorder(30, 0, 30, 0));
		filePanel.add(fromFileButton2); //, new GridBagConstraints());
		settingsPanel.add(settingsButton);
		otherButtonsPanel.add(manualPanel);
		otherButtonsPanel.add(filePanel);
		otherButtonsPanel.add(settingsPanel);
		sourceFrame.add(classPanel);
		sourceFrame.add(otherButtonsPanel);
		sourceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sourceFrame.setLocationRelativeTo(null);
		sourceFrame.setVisible(true);
	}

	private void nextMenu(LotteryType lottery) {
		sourceFrame.setVisible(false);
		System.out.println("Next menu");
		featuresFrame = new JFrame();
		//featuresFrame.setLayout(new GridLayout(5, 1));
		featuresFrame.setLayout(new BoxLayout(featuresFrame.getContentPane(),BoxLayout.Y_AXIS));
		// JPanel messagePanel = new JPanel();
		JPanel namePanel = new JPanel(new FlowLayout());
		JPanel featuresPanel1 = new JPanel();
		// JPanel featuresPanel2 = new JPanel();
		JPanel buttonPanel1 = new JPanel();
		JPanel buttonPanel2 = new JPanel();
		JPanel removePanel = new JPanel();
		JPanel sizingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

		// messagePanel.setLayout(new FlowLayout());
		//featuresPanel1.setLayout(new FlowLayout(FlowLayout.CENTER));
		featuresPanel1.setLayout(null);
		featuresPanel1.setPreferredSize(new Dimension(0,80));
		featuresPanel1.setBorder(new LineBorder(Color.RED));
		// ----> featuresPanel2.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel1.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel2.setLayout(new FlowLayout(FlowLayout.CENTER));
		removePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		JLabel allNamesLabel = new JLabel(getAllNames(lottery));
		allNamesLabel.setFont(new Font("arial", Font.PLAIN,10));
		namePanel.add(allNamesLabel);


		JButton startButton = new JButton("Starta lotteri");
		startButton.setBackground(Color.GREEN);
		startButton.setForeground(Color.WHITE);
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Du valde storlek: " + sizeGroup.getSelection().getActionCommand());
				lottery.setScale(Integer.parseInt(sizeGroup.getSelection().getActionCommand()));
				lottery.setShowCount(checkBoxShowNr.isSelected());
				lottery.setSaveNames(checkBoxShowTaken.isSelected());
				/*
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

				 */
				System.out.println("Visa nr: " + checkBoxShowNr.isSelected());
				System.out.println("Visa tagna: " + checkBoxShowTaken.isSelected());
				// lottery.setScale(scaleChooser());

				featuresFrame.setVisible(false);
				lotteryHandler.startLottery(lottery);
			}
		});
		/*
		JButton seatingButton = new JButton("Bordsplacering");

		seatingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Bordsplacering");
				featuresFrame.setVisible(false);
				// new ClassRoom(lottery.getStartNames());
				new SeatingMenu(lottery.getStartNames());
			}
		});
		 */
		JButton backButton = new JButton("Tillbaka");
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Tillbaka");
				System.out.println("Metoden");
				featuresFrame.setVisible(false);
				sourceFrame.setVisible(true);
			}
		});
//		JButton previewButton = new JButton("Tjuvtitta på namnen");
//		previewButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				showPeek(lottery);
//			}
//		});
		buttonPanel1.add(backButton);
		// buttonsPanel.add(previewButton);
		// buttonsPanel.add(seatingButton);
		buttonPanel2.add(startButton);

		JLabel headerText = new JLabel("Extraval:");
		headerText.setFont(new Font(null, Font.BOLD, 14));
		headerText.setBorder(new EmptyBorder(0, 0, 0, 40));
		// featuresPanel.add(headerText);
		checkBoxShowTaken = new JCheckBox("Visa alla som lottats fram     ", false);
		checkBoxShowTaken.setBounds(100,20,200,20);
		featuresPanel1.add(checkBoxShowTaken);
		checkBoxShowNr = new JCheckBox("Visa antal kvar     ", false);
		checkBoxShowNr.setBounds(100,45,200,20);
		featuresPanel1.add(checkBoxShowNr);

		sizeGroup = new ButtonGroup();
		JRadioButton xSmallButt = new JRadioButton("XS");
		xSmallButt.setActionCommand("2");
		JRadioButton smallButt = new JRadioButton("S");
		smallButt.setActionCommand("4");
		JRadioButton medButt = new JRadioButton("M", true);
		medButt.setActionCommand("6");
		JRadioButton largeButt = new JRadioButton("L");
		largeButt.setActionCommand("8");
		JRadioButton xLargeButt = new JRadioButton("XL");
		xLargeButt.setActionCommand("10");
		JRadioButton fullButt = new JRadioButton("Full");
		fullButt.setActionCommand("12");
		sizeGroup.add(xSmallButt);
		sizeGroup.add(smallButt);
		sizeGroup.add(medButt);
		sizeGroup.add(largeButt);
		sizeGroup.add(xLargeButt);
		sizeGroup.add(fullButt);
		sizingPanel.add(xSmallButt);
		sizingPanel.add(smallButt);
		sizingPanel.add(medButt);
		sizingPanel.add(largeButt);
		sizingPanel.add(xLargeButt);
		sizingPanel.add(fullButt);


		//JLabel removeText = new JLabel("Namn att ta bort:");
		//removePanel.add(removeText);
		//removeTextField = new JTextField(40);
		//removePanel.add(removeTextField);

		JButton removeButton = new JButton("Ta bort namn");
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new RemoveDialog(featuresFrame, lottery);
				System.out.println("Nu fortsätter vi efter remove dialog");
				allNamesLabel.setText(getAllNames(lottery));
			}
		});
		removePanel.add(removeButton);
		JPanel messPanel = new JPanel();
		messPanel.setLayout(new FlowLayout());
		messPanel.add(new JLabel("Fönsterstorlek:"));

		featuresFrame.add(namePanel);
		featuresFrame.add(featuresPanel1);
		// ---> featuresFrame.add(featuresPanel2);
		featuresFrame.add(removePanel);
		featuresFrame.add(messPanel);
		featuresFrame.add(sizingPanel);
		featuresFrame.add(buttonPanel1);
		featuresFrame.add(buttonPanel2);
		featuresFrame.pack();
		featuresFrame.setLocationRelativeTo(null);
		featuresFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		featuresFrame.setVisible(true);

	}

//	private void showPeek(LotteryType lottery) {
//
//		String groupText = lottery.getGroup() == 0 ? " helkXlass: " : " grXupp " + lottery.getGroup() + ": ";
//		StringBuilder sb = new StringBuilder(lottery.getClassName() + groupText);
//
//		LinkedList <String> temp = new LinkedList<>(lottery.getStartNames());
//		temp.sort(Comparator.comparing(String::toString));
//		for(String n : temp)
//			sb.append(n).append(", ");
//
//		JOptionPane.showMessageDialog(featuresFrame, sb.toString());
//	}

//	private int scaleChooser() {
//		String[] sizes = {"XS","S","M","L","XL","Full"};
//		int result = JOptionPane.showOptionDialog(featuresFrame,
//			"Välj storlek på lotterifönstret",
//			null,
//			JOptionPane.DEFAULT_OPTION,
//			JOptionPane.QUESTION_MESSAGE,
//			null,
//			sizes,
//			sizes[2]);
//		int sc = result*2 + 1;
//		if (result == 0) sc++;
//		return sc;
//	}
	private String getAllNames(LotteryType lottery) {
		StringBuilder sb = new StringBuilder("<html>");
		int count = 0;
		LinkedList<String> tempList = lottery.getStartNames();
		Collections.sort(tempList);
		for (String s : tempList) {
			sb.append(s).append(",");
			count++;
			if(count % 15 == 0) sb.append("<br>");
		}
		sb.append("</html>");
		return sb.toString();
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
		JButton settingsButton = new JButton("ändra inställningar");
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