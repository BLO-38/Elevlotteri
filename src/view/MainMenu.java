package view;

import databasen.DatabaseHandler2;
import databasen.NameListGetters;
import databasen.SelectHandler;
import databasen.Student;
import model.*;
import offlineHandling.OfflineHandler;
import view.chokladhjulet.ChoclateWheel;
import view.rast_timer.RastTimer;
import view.rast_timer.TimerMenu;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.TreeMap;

public class MainMenu {

	private final JFrame sourceFrame;
	private ButtonGroup bgr;
	private JFrame actionFrame;
	private static JFrame mainMenu;
	private boolean offlineControlActive = false, someThingActive = false;
	private OfflineHandler offlineHandler;
	private static final Dimension buttDims = new Dimension(200, 20);

	public MainMenu() {
		boolean dataBaseActive = DatabaseHandler2.isDbActive();
		LinkedList<String> classes = null;
		if (dataBaseActive) {
			classes = DatabaseHandler2.getClasses();
//			if (classes.isEmpty()) {
//				JOptionPane.showMessageDialog(null, "Obs inga klasser fanns i databasen");
//			}
		}

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
		header.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
		header.setForeground(Color.BLUE);
		headerPanel.add(header);
		p1.add(headerPanel);

		JPanel classPanel = new JPanel();
		classPanel.setLayout(new FlowLayout());
		p1.add(classPanel);

		JPanel groupPanel = new JPanel();
		groupPanel.setLayout(new GridLayout(3, 1));

		JPanel extraFeaturesPanel = new JPanel();
		extraFeaturesPanel.setLayout(new BoxLayout(extraFeaturesPanel, BoxLayout.Y_AXIS));
		extraFeaturesPanel.setBorder(new LineBorder(new Color(0x4D0303), 4));
		mainPanel.add(extraFeaturesPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel header2Panel = new JPanel(new GridBagLayout());
		JLabel header2 = new JLabel("Andra aktiviteter");
		header2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
		header2.setForeground(new Color(0x4D0303));
		header2Panel.add(header2);
		extraFeaturesPanel.add(header2Panel);

		TreeMap<String, ActionListener> featureButtonActions = new TreeMap<>();
		featureButtonActions.put("Manuellt lotteri", e -> System.out.println("Manuellllt"));
		featureButtonActions.put("Gammal bordsplacering", e -> new OldSeatingStarter(OldSeatingStarter.LOAD_CLASSROOM));
		featureButtonActions.put("Timer", e -> new TimerMenu());

		for (String key : featureButtonActions.keySet()) {
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.CENTER));
			JButton button = new JButton(key);
			button.addActionListener(featureButtonActions.get(key));
			button.setPreferredSize(buttDims);
			panel.add(button);
			extraFeaturesPanel.add(panel);
		}

		JPanel p3 = new JPanel();
		p3.setLayout(new BorderLayout());
		mainPanel.add(Box.createRigidArea(new Dimension(0,12)));
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
		manualButton2.addActionListener(e -> new ManualLottery());

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
		p3.add(exitButton,BorderLayout.EAST);

		p3.add(settingsButton,BorderLayout.WEST);

		sourceFrame.add(mainPanel);
		sourceFrame.pack();
		sourceFrame.setLocationRelativeTo(null);
		sourceFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		sourceFrame.setVisible(true);

	}


	private void chooseAction(String chosenClass, int grp) {

		LinkedList<String> lotteryModes = Instructions.getInstructionsNames();
		Collections.sort(lotteryModes);

		int modesSize = lotteryModes.size();
		actionFrame = new JFrame();
		actionFrame.setLayout(new BorderLayout());
		JPanel actionButtPanel = new JPanel();
		JPanel infoButtPanel = new JPanel();
		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		actionButtPanel.setLayout(new GridLayout(modesSize, 1, 0, 10));
		infoButtPanel.setLayout(new GridLayout(modesSize, 1, 0, 10));
		ImageIcon icon = (ImageIcon) UIManager.getIcon("OptionPane.informationIcon");
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(13, 13, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);

		for (int i = 0; i < modesSize; i++) {
			String mode = lotteryModes.pollFirst();
			JButton b1 = new JButton(mode);
			JButton b2 = new JButton(icon);
			b2.setMargin(new Insets(3, 0, 0, 0));
			b2.setVerticalAlignment(SwingConstants.CENTER);
			b2.setActionCommand(mode);

			b1.setActionCommand(mode);
			// Gör om med arv
			b1.addActionListener(e -> {
				actionFrame.dispose();
				Lottery lottery3 = null;
				String m = e.getActionCommand();
                switch (m) {
                    case "Lotteri med alla" -> lottery3 = new RegularLottery(chosenClass, grp, true);
                    case "Prioriterat lotteri" -> lottery3 = new RegularLottery(chosenClass, grp, false);
                    case "Prisutdelning" -> {
                        lottery3 = new CandyLottery(chosenClass, grp);
                        if (lottery3.getStartNames() == null || lottery3.getStartNames().isEmpty()) return;
                    }
                    case "Chokladhjul" -> {
                        new ChoclateWheel(chosenClass, grp);
                        return;
                    }
                    case "Kontrollfrågor" -> lottery3 = new ControlQuestions(chosenClass, grp);
                    case "Bordsplacering" -> {
                        LinkedList<String> names = NameListGetters.getNamesRegular(chosenClass, grp);
                        new SeatingMenu(names, chosenClass);
                        return;
                    }
                    case "Gruppindelning alla" -> {
                        LinkedList<Student> elever = SelectHandler.getStudents(chosenClass, grp);
                        new GroupMenuExtra(elever);
                        return;
                    }
                    case "Utse elevgrupp" -> {
                        new SingleGroupWindow(chosenClass, grp);
                        return;
                    }
                    case "Offlinekontroll" -> {
                        if (offlineControlActive) return;
                        offlineControlActive = true;
                        offlineHandler = new OfflineHandler(chosenClass, grp, this);
                        return;
                    }
                }

				if(lottery3 != null) new LotteryMenu(lottery3);
			});

			b2.addActionListener(e -> JOptionPane.showMessageDialog(null, Instructions.getInfo(e.getActionCommand())));
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
