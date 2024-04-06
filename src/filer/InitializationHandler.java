package filer;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import databasen.DatabaseHandler;
import databasen.DatabaseHandler2;
import databasen.SetUpDatabase;
import jdk.swing.interop.SwingInterOpUtils;

public class InitializationHandler {

	private static boolean useDB = false;
	private static final String fileName = "settings.txt";
	private static String dbName = null;

	public InitializationHandler() {
	}

	public static void readSettings() {
		System.out.println("Låser settings");
		useDB = false;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader b = new BufferedReader(fileReader);

			String line = b.readLine();

			if (line.substring(13).equals("true")) {

				line = b.readLine();
				dbName = line.substring(8);
				System.out.println("Läste namn: " + dbName);
				if (dbName.length() > 0) {
					System.out.println("Ja, databas med namn");
					useDB = true;
				}
			} else {
				System.out.println("Nej, ingen databas");
			}
		} catch (FileNotFoundException f) {
			System.out.println("Ingen settingsfil fanns");
			newInitialazation(null);
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "Oväntat fel i readsettings i init.handler");
		} catch (Exception e) {
			System.out.println("FEeeel");
		}
	}

	private static void createNewFile(JFrame frame) {
		int a = JOptionPane.showConfirmDialog(frame, "Vill du använda en databas?");

		if (a == -1 || a == 2) System.exit(0);
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
			if (a == 0) {
				useDB = true;
				do {
					dbName = JOptionPane.showInputDialog(frame, "Vilket namn har databasen? Om den inte finns skapas en ny.");
				} while (dbName == null || dbName.length() == 0);
			} else useDB = false;
			bufferWriter.write("USE_DATABASE=" + useDB);
			bufferWriter.newLine();
			bufferWriter.write("DB_NAME=" + dbName);
			bufferWriter.newLine();
			bufferWriter.close();
			fileWriter.close();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(frame, "Något gick fel i filhanteringen");
		}
	}

	public static boolean useDataBase() {
		return useDB;
	}

	public static void newInitialazation(JFrame frame) {
		createNewFile(frame);
		String baseURL = DatabaseHandler.getBaseURL();

		System.out.println("Basen belv: " + baseURL);
		if (useDB) {
			String mess = SetUpDatabase.setUp(baseURL, dbName) ? "Allt gick bra. Databasen är klar att använda nästa gång." : "Inga nya tabeller. Troligen för att databasen redan fanns.";
			mess += " Programmet avslutas...";
			JOptionPane.showMessageDialog(frame, mess);
		} else
			JOptionPane.showMessageDialog(null, "Du kör vidare utan databas tydligen. Men du måste starta om programmet!");
		System.exit(0);
	}

	public static void handleDbUsage() {
		if (useDB) {
			String[] options = {"Byta till annan befintlig", "Skapa ny", "Koppla från"};
			int ans = JOptionPane.showOptionDialog(null, "Vad vill du göra med databasen?", "Fråga:", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, 0);
			if (ans == 0) switchDB();
			if (ans == 1) createNewDB();
			if (ans == 2) disconnectDB();
		}
	}

	private static void disconnectDB() {
	}

	private static void createNewDB() {
		String newName = JOptionPane.showInputDialog(null, "Vilket namn har databasen?\n(Om den redan finns kommer den öppnas).");
		if(newName == null || newName.isEmpty()) return;
		DatabaseHandler2.getInstance().setDataBase(newName);
		// Skapa tabeller!!
		createSettingsFile(newName);
	}

	public static String getDBName() {
		return dbName;
	}

	private static void switchDB() {
		File dbFolder = new File("C:/sqlite/");
		System.out.println(dbFolder.isDirectory());
		System.out.println(Arrays.toString(dbFolder.listFiles()));
		File[] databases = dbFolder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".db");
			}
		});
		String[] dbNames = new String[databases.length];
		for (int i = 0; i < databases.length; i++) {
			dbNames[i] = databases[i].getName().substring(0, databases[i].getName().indexOf("."));
		}
		JFrame dbFrame = new JFrame();
		JLabel header = new JLabel("Välj en av dina databaser:");
		header.setFont(new Font(null, Font.BOLD, 20));
		dbFrame.add(header);
		JPanel radios = new JPanel();
		radios.setLayout(new BoxLayout(radios, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(radios);
		dbFrame.setLayout(new BoxLayout(dbFrame.getContentPane(), BoxLayout.Y_AXIS));
		ButtonGroup group = new ButtonGroup();
		for (int i = 0; i < dbNames.length; i++) {
			JRadioButton r = new JRadioButton(dbNames[i]);
			r.setActionCommand(dbNames[i]);
			group.add(r);
			radios.add(r);
		}
		JPanel bp = new JPanel(new GridBagLayout());
		JButton finishButton = new JButton("Klar");
		finishButton.addActionListener(e -> {
			String newDBname = group.getSelection().getActionCommand();
			System.out.println("Du valde " + newDBname);

			DatabaseHandler2.getInstance().setDataBase(newDBname);
			createSettingsFile(newDBname);
			dbFrame.dispose();
		});
		System.out.println(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		int xMax = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		System.out.println(Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		int yMax = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		System.out.println(xMax + " : " + yMax);
		dbFrame.setMaximumSize(new Dimension(xMax, yMax));
		bp.add(finishButton);
		dbFrame.add(scrollPane);
		dbFrame.add(bp);
		dbFrame.pack();
		if (dbFrame.getHeight() > ((int) (0.7 * yMax))) {
			dbFrame.setSize(dbFrame.getWidth(), (int) (yMax * 0.7));
		}
		dbFrame.setLocationRelativeTo(null);
		dbFrame.setVisible(true);
		dbFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	private static void createSettingsFile(String newDBname) {
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
			useDB = newDBname != null && !newDBname.isEmpty();
			bufferWriter.write("USE_DATABASE=" + useDB);
			bufferWriter.newLine();
			bufferWriter.write("DB_NAME=" + newDBname);
			bufferWriter.newLine();
			bufferWriter.close();
			fileWriter.close();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "Något gick fel i filhanteringen");
		}
	}

}







