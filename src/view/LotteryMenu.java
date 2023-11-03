package view;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import databasen.DatabaseHandler;
import filer.InitializationHandler;
import model.CandyLottery;
import model.ControlQuestions;
import model.FileLottery;
import model.Lottery;
import model.MainHandler;
import model.ManualLottery;
import model.RegularLottery;

public class LotteryMenu {

	private JFrame sourceFrame, featuresFrame;

	private JCheckBox checkBoxShowTaken, checkBoxShowNr;
	private ButtonGroup bgr, sizeGroup;
	private final MainHandler lotteryHandler;
	private final boolean isDataBaseActive;


	public LotteryMenu(MainHandler sh, boolean db) {
		lotteryHandler = sh;
		isDataBaseActive = db;
	}
	
	public void startUp(LinkedList<String> classList) {
		Dimension buttDims = new Dimension(150,40);
		sourceFrame = new JFrame("Elevlotteri 3.0");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		sourceFrame.setLayout(new FlowLayout());

		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1,BoxLayout.Y_AXIS));
		p1.setBorder(new LineBorder(Color.BLUE,4));
		mainPanel.add(p1);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel headerPanel = new JPanel(new GridBagLayout());
		JLabel header = new JLabel("Välj klass och grupp:");
		header.setFont(new Font(null,Font.BOLD,30));
		header.setForeground(Color.BLUE);
		headerPanel.add(header);
		p1.add(headerPanel);

		JPanel classPanel = new JPanel();
		classPanel.setLayout(new FlowLayout());
		p1.add(classPanel);

		JPanel groupPanel = new JPanel();
		groupPanel.setLayout(new GridLayout(3, 1));

		JPanel p2 = new JPanel();
		p2.setLayout(new BoxLayout(p2,BoxLayout.Y_AXIS));
		p2.setBorder(new LineBorder(new Color(0x4D0303),4));
		mainPanel.add(p2);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel header2Panel = new JPanel(new GridBagLayout());
		JLabel header2 = new JLabel("Eller ett engångslotteri utan sparning:");
		header2.setFont(new Font(null,Font.PLAIN,20));
		header2.setForeground(new Color(0x4D0303));
		header2Panel.add(header2);
		p2.add(header2Panel);

		JPanel manualPanel = new JPanel();
		manualPanel.setLayout(new GridBagLayout());
		manualPanel.setPreferredSize(buttDims);
		p2.add(manualPanel);

		JPanel filePanel = new JPanel();
		filePanel.setLayout(new GridBagLayout());
		filePanel.setPreferredSize(buttDims);
		p2.add(filePanel);

		JPanel p3 = new JPanel();
		p3.setLayout(new GridBagLayout());
		p3.setPreferredSize(buttDims);
		if (isDataBaseActive) mainPanel.add(p3);

		JPanel p4 = new JPanel();
		p4.setLayout(new GridBagLayout());
		p4.setPreferredSize(buttDims);
		mainPanel.add(p4);


		String dataBaseMessText;
		if(isDataBaseActive) {
			dataBaseMessText = "Aktiv databas: " + lotteryHandler.getDbName();
			JLabel dataBaseMess = new JLabel(dataBaseMessText);
			Border marg = new EmptyBorder(0, 0, 0, 50);
			dataBaseMess.setBorder(marg);
			dataBaseMess.setForeground(new Color(0x0DAD07));
			dataBaseMess.setOpaque(true);
			classPanel.add(dataBaseMess);

			if(classList != null) {
				for(String n : classList) {
					JButton b = new JButton(n);
					b.addActionListener(a -> {
						sourceFrame.setVisible(false);
						String className = a.getActionCommand();
						int group = Integer.parseInt(bgr.getSelection().getActionCommand());
						System.out.println("Du vlade " + className + ", grupp " + group);
						DatabaseHandler.setCurrentClass(className, group);
						Lottery lottery;
						String[] lotteryModes = {"Lotteri med alla","Prioriterat lotteri","Godis","Kontrollfrågor","Bordsplacering","Gruppindelning"};
						int result = JOptionPane.showOptionDialog(sourceFrame,
							"Välj typ av lotteri",
							null,
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,
							lotteryModes,
							null);
						if (result == 0) lottery = new RegularLottery(className, group, true);
						else if (result == 1) lottery = new RegularLottery(className, group, false);
						else if (result == 2) lottery = new CandyLottery(className, group);
						else if (result == 3) lottery = new ControlQuestions(className,group,sourceFrame);
						else if (result == 4) {
							lottery = new RegularLottery(className, group, true);
							new SeatingMenu(lottery.getStartNames());
							return;
						} else if (result == 5) {
							lottery = new RegularLottery(className, group, true);
							new GroupMenuExtra(lottery.getStudents());
							return;
						} else {
							lottery = null;
							System.exit(0);
						}
						nextMenu(lottery);
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
			dataBaseMess.setFont(new Font("arial", Font.PLAIN, 18));
			dataBaseMess.setForeground(Color.RED);
			dataBaseMess.setOpaque(true);
			dataBaseMess.setBackground(Color.WHITE);
			classPanel.add(dataBaseMess);
		}

		JButton manualButton2 = new JButton("Skriv in namn");
		manualButton2.addActionListener(e -> {
			System.out.println(e.toString());
			System.out.println(e.getActionCommand());
			System.out.println(e.getSource());
			System.out.println("x");
			sourceFrame.setVisible(false);
			Lottery lottery = new ManualLottery(sourceFrame);
			nextMenu(lottery);
		});

		JButton fromFileButton2 = new JButton("Namn från fil");
		fromFileButton2.addActionListener(e -> {
			sourceFrame.setVisible(false);
			Lottery lottery = new FileLottery();
			nextMenu(lottery);
		});

		JButton settingsButton = new JButton("Klasser och elever");
		settingsButton.addActionListener(e -> {
			sourceFrame.setVisible(false);
			new SettingsMenu();

		});
		JButton databaseButton = new JButton("Välj databas");
		databaseButton.addActionListener(e -> {
			sourceFrame.setVisible(false);
			InitializationHandler.newInitialazation(sourceFrame);
		});

		manualPanel.add(manualButton2);
		filePanel.add(fromFileButton2);
		p3.add(settingsButton);
		p4.add(databaseButton);

		sourceFrame.add(mainPanel);
		sourceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sourceFrame.pack();
		sourceFrame.setLocationRelativeTo(null);
		sourceFrame.setVisible(true);
	}

	private void nextMenu(Lottery lottery) {
		featuresFrame = new JFrame();
		featuresFrame.setLayout(new BoxLayout(featuresFrame.getContentPane(),BoxLayout.Y_AXIS));
		JPanel namePanel = new JPanel(new FlowLayout());
		JPanel featuresPanel = new JPanel();
		JPanel buttonPanel1 = new JPanel();
		JPanel buttonPanel2 = new JPanel();
		JPanel removePanel = new JPanel();
		JPanel sizingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

		featuresPanel.setLayout(null);
		featuresPanel.setPreferredSize(new Dimension(0,80));
		featuresPanel.setBorder(new LineBorder(Color.RED));
		buttonPanel1.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel2.setLayout(new FlowLayout(FlowLayout.CENTER));
		removePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		JLabel allNamesLabel = new JLabel(getAllNames(lottery));
		allNamesLabel.setFont(new Font("arial", Font.PLAIN,10));
		namePanel.add(allNamesLabel);


		JButton startButton = new JButton("Starta lotteri");
		startButton.setBackground(new Color(27, 104, 5));
		startButton.setForeground(Color.WHITE);
		startButton.addActionListener(e -> {
			lottery.setScale(Integer.parseInt(sizeGroup.getSelection().getActionCommand()));
			lottery.setShowCount(checkBoxShowNr.isSelected());
			lottery.setSaveNames(checkBoxShowTaken.isSelected());
			featuresFrame.setVisible(false);
			lotteryHandler.startLottery(lottery);
		});
		JButton backButton = new JButton("Tillbaka");
		backButton.addActionListener(e -> {
			System.out.println("Tillbaka");
			System.out.println("Metoden");
			featuresFrame.setVisible(false);
			sourceFrame.setVisible(true);
		});

		buttonPanel1.add(backButton);
		buttonPanel2.add(startButton);

		JLabel headerText = new JLabel("Extraval:");
		headerText.setFont(new Font(null, Font.BOLD, 14));
		headerText.setBorder(new EmptyBorder(0, 0, 0, 40));
		checkBoxShowTaken = new JCheckBox("Visa alla som lottats fram     ", false);
		checkBoxShowTaken.setBounds(100,20,200,20);
		featuresPanel.add(checkBoxShowTaken);
		checkBoxShowNr = new JCheckBox("Visa antal kvar     ", false);
		checkBoxShowNr.setBounds(100,45,200,20);
		featuresPanel.add(checkBoxShowNr);

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

		JButton removeButton = new JButton("Ta bort namn");
		removeButton.setBackground(new Color(239, 196, 196));
		removeButton.addActionListener(e -> {
			new RemoveDialog(featuresFrame, lottery, null);
			allNamesLabel.setText(getAllNames(lottery));
			System.out.println("Tebax");
			featuresFrame.pack();
		});
		removePanel.add(removeButton);

		JButton addButton = new JButton("Lägg till namn");
		addButton.setBackground(new Color(185, 241, 190));
		addButton.addActionListener(e -> {
			String extraName = JOptionPane.showInputDialog(featuresFrame,"Ange namn:" );
			if(extraName == null || extraName.length() == 0) return;
			lottery.addName(extraName);
			if (lottery instanceof RegularLottery) lottery.shuffleStartnames();
			allNamesLabel.setText(getAllNames(lottery));
			featuresFrame.pack();
		});
		removePanel.add(addButton);

		JPanel messPanel = new JPanel();
		messPanel.setLayout(new FlowLayout());
		messPanel.add(new JLabel("Fönsterstorlek:"));

		featuresFrame.add(namePanel);
		featuresFrame.add(featuresPanel);
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

	private String getAllNames(Lottery lottery) {
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
