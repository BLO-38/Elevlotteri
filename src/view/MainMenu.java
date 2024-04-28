package view;

import databasen.DatabaseHandler2;
import databasen.NameListGetters;
import databasen.SelectHandler;
import databasen.Student;
import model.*;
import offlineHandling.OfflineHandler;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.LinkedList;

public class MainMenu {
	// detta är före detta LotteryMenu

	private final JFrame sourceFrame;
	private ButtonGroup bgr;
	private final boolean dataBaseActive;
	private JFrame actionFrame;
	private static JFrame mainMenu;
	private boolean offlineControlActive = false, someThingActive = false;
	private OfflineHandler offlineHandler;

	public MainMenu() {
		dataBaseActive = DatabaseHandler2.isDbActive();
		LinkedList<String> classes = null;
		if (dataBaseActive) {
			classes = DatabaseHandler2.getClasses();
			if (classes.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Obs inga klasser fanns i databasen");
			}
		}

		Dimension buttDims = new Dimension(150, 40);
		sourceFrame = new JFrame(MainHandler.version);
		mainMenu = sourceFrame;
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		sourceFrame.setLayout(new FlowLayout());

		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
		p1.setBorder(new LineBorder(Color.BLUE, 4));
		mainPanel.add(p1);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel headerPanel = new JPanel(new GridBagLayout());
		JLabel header = new JLabel("Välj klass och grupp:");
		header.setFont(new Font(null, Font.BOLD, 30));
		header.setForeground(Color.BLUE);
		headerPanel.add(header);
		p1.add(headerPanel);

		JPanel classPanel = new JPanel();
		classPanel.setLayout(new FlowLayout());
		p1.add(classPanel);

		JPanel groupPanel = new JPanel();
		groupPanel.setLayout(new GridLayout(3, 1));

		JPanel p2 = new JPanel();
		p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
		p2.setBorder(new LineBorder(new Color(0x4D0303), 4));
		mainPanel.add(p2);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel header2Panel = new JPanel(new GridBagLayout());
		JLabel header2 = new JLabel("Manuellt engångslotteri");
		header2.setFont(new Font(null, Font.PLAIN, 20));
		header2.setForeground(new Color(0x4D0303));
		header2Panel.add(header2);
		p2.add(header2Panel);

		JPanel manualPanel = new JPanel();
		manualPanel.setLayout(new GridBagLayout());
		manualPanel.setPreferredSize(buttDims);
		p2.add(manualPanel);

		JPanel tablesPanel = new JPanel();
		JPanel tableHeaderPanel = new JPanel(new GridBagLayout());
		JLabel tableHeader = new JLabel("Bordsplacering");
		tableHeader.setFont(new Font(null, Font.PLAIN, 20));
		tableHeaderPanel.add(tableHeader);
		tablesPanel.add(tableHeaderPanel);
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		tablesPanel.setBorder(new LineBorder(new Color(0x4D0303), 4));

		JPanel tableButtonPanel1 = new JPanel(new GridBagLayout());
		tableButtonPanel1.setPreferredSize(buttDims);
		tablesPanel.add(tableButtonPanel1);
		JButton tableButton = new JButton("Ladda bordsplacering");
		tableButtonPanel1.add(tableButton);
		mainPanel.add(tablesPanel);

		tableButton.addActionListener(e -> new OldSeatingStarter(OldSeatingStarter.LOAD_CLASSROOM));

		JPanel p3 = new JPanel();
		p3.setLayout(new GridBagLayout());
		p3.setPreferredSize(buttDims);
		mainPanel.add(p3);

		String dataBaseMessText;
		if (dataBaseActive) {
			dataBaseMessText = "Aktiv databas: " + DatabaseHandler2.getDbName();
			JLabel dataBaseMess = new JLabel(dataBaseMessText);
			Border marg = new EmptyBorder(0, 0, 0, 50);
			dataBaseMess.setBorder(marg);
			dataBaseMess.setForeground(new Color(0x0DAD07));
			dataBaseMess.setOpaque(true);
			classPanel.add(dataBaseMess);

			if (!classes.isEmpty()) {
				for (String n : classes) {
					JButton b = new JButton(n);
					b.addActionListener(a -> {
						someThingActive = true;
						String className = a.getActionCommand();
						int group = Integer.parseInt(bgr.getSelection().getActionCommand());
						chooseAction(className, group);
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
			Lottery lottery = new ManualLottery();
		});

		JButton settingsButton = new JButton("Inställningar");
		settingsButton.addActionListener(e -> {
			if(someThingActive) {
				JOptionPane.showMessageDialog(null,"Avsluta aktiviteten och starta om programmet\nom du vill ändra inställningar.");
				return;
			}
			sourceFrame.dispose();
			new SettingsMenu();
		});

		JButton exitButton = new JButton("Avsluta");
		exitButton.setBackground(MainHandler.MY_RED);
		exitButton.setForeground(Color.WHITE);
		exitButton.addActionListener(e -> {
			if(offlineControlActive) {
				int check = JOptionPane.showConfirmDialog(sourceFrame,"Offlinekontroll pågår. Vill du verkligen avsluta?");
				if(check == JOptionPane.YES_OPTION) {
					offlineHandler.quit();
				} else return;
			}
			System.exit(0);
		});
		p3.add(exitButton);

		manualPanel.add(manualButton2);
		p3.add(settingsButton);

		sourceFrame.add(mainPanel);
		sourceFrame.pack();
		sourceFrame.setLocationRelativeTo(null);
		sourceFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		sourceFrame.setVisible(true);
//		sourceFrame.addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowClosing(WindowEvent event) {
//				DatabaseHandler2.closeDatabase();
//				System.out.println("Nu avslutas programmet på rätt sätt");
//				System.exit(0);
//			}
//		});

	}

	@SuppressWarnings("all")
	private void chooseAction(String chosenClass, int grp) {
//		String[] lotteryModes = {"Lotteri med alla", "Prioriterat lotteri", "Slumpmässig belöning", "Kontrollfrågor", "Bordsplacering", "Gruppindelning"};
		String[] lotteryModes = {"Lotteri med alla", "Prioriterat lotteri", "Slumpmässig belöning", "Kontrollfrågor", "Bordsplacering", "Gruppindelning alla", "Utse elevgrupp","Offlinekontroll"};
		actionFrame = new JFrame();
		actionFrame.setLayout(new BorderLayout());
		JPanel actionButtPanel = new JPanel();
		JPanel infoButtPanel = new JPanel();
		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		actionButtPanel.setLayout(new GridLayout(lotteryModes.length, 1, 0, 10));
		infoButtPanel.setLayout(new GridLayout(lotteryModes.length, 1, 0, 10));
		ImageIcon icon = (ImageIcon) UIManager.getIcon("OptionPane.informationIcon");
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(13, 13, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);

		for (int i = 0; i < lotteryModes.length; i++) {
			JButton b1 = new JButton(lotteryModes[i]);
			JButton b2 = new JButton(icon);
			b2.setMargin(new Insets(3, 0, 0, 0));
			b2.setVerticalAlignment(SwingConstants.CENTER);
			b2.setActionCommand(String.valueOf(i));

			b1.setActionCommand(String.valueOf(i));
			// Gör om med arv
			b1.addActionListener(e -> {
				actionFrame.dispose();
				Lottery lottery3 = null;
				int result = Integer.parseInt(e.getActionCommand());
				if (result == 0) lottery3 = new RegularLottery(chosenClass, grp, true);
				else if (result == 1) lottery3 = new RegularLottery(chosenClass, grp, false);
				else if (result == 2) lottery3 = new CandyLottery(chosenClass, grp);
				else if (result == 3) lottery3 = new ControlQuestions(chosenClass, grp);
				else if (result == 4) {
					LinkedList<String> names = NameListGetters.getNamesRegular(chosenClass, grp);
					new SeatingMenu(names, chosenClass);
					return;
				}
				else if (result == 5) {
					LinkedList<Student> elever = SelectHandler.getStudents(chosenClass, grp);
					new GroupMenuExtra(elever);
					return;
				}
				else if (result == 6) {
					new SingleGroupWindow(chosenClass,grp);
					return;
				}
				else if (result == 7) {
					if(offlineControlActive) return;
					offlineControlActive = true;
					offlineHandler = new OfflineHandler(chosenClass,grp,this);
					return;
				}

				new LotteryMenu(lottery3);
			});

			b2.addActionListener(e -> JOptionPane.showMessageDialog(null, Instructions.getInfo(Integer.parseInt(e.getActionCommand()))));
			actionButtPanel.add(b1);
			infoButtPanel.add(b2);
		}

		JPanel headerPanel = new JPanel(new FlowLayout());
		JLabel header = new JLabel("Välj:");
		header.setForeground(new Color(0x091352));
		header.setFont(new Font(null, Font.BOLD, 35));
		headerPanel.add(header);
		actionFrame.add(headerPanel, BorderLayout.NORTH);
		actionFrame.add(Box.createRigidArea(new Dimension(20, 0)), BorderLayout.WEST);
		actionFrame.add(Box.createRigidArea(new Dimension(20, 0)), BorderLayout.EAST);
		centerPanel.add(actionButtPanel);
		centerPanel.add(infoButtPanel);
		actionFrame.add(centerPanel, BorderLayout.CENTER);


		JPanel bottom = new JPanel();
		bottom.setLayout(new GridBagLayout());
		JButton infoButton = new JButton("Instruktioner");
		bottom.add(infoButton);
		infoButton.addActionListener(e -> {

		});
		infoButton.setBackground(MainHandler.MY_GREEN);
		infoButton.setForeground(Color.WHITE);

		actionFrame.add(bottom, BorderLayout.SOUTH);
		bottom.setPreferredSize(new Dimension(200, 70));
		actionFrame.pack();
		int h = actionButtPanel.getHeight();
		System.out.println("Höjd: " + h + " och " + infoButtPanel.getHeight());
		infoButtPanel.setPreferredSize(new Dimension(20, h));
		System.out.println("Höjd: " + h + " och " + infoButtPanel.getHeight());

		actionFrame.pack();

		actionFrame.setLocationRelativeTo(null);
		actionFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		actionFrame.setVisible(true);
	}

	public void offlineFinished() {
		offlineControlActive = false;
		offlineHandler = null;
	}
	public static void minimize() {
		mainMenu.setState(Frame.ICONIFIED);
	}
}