package databasen;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.LinkedList;

public class DatabaseHandler2 {

	// Följande gäller 20240407:
	private static final int numberOfTables = 6;
	private static final String controlTable = "benches";
	// ----> hit

	private static final String baseURL="jdbc:sqlite:C:/sqlite/";
	private static final String settingsPath ="C:/sqlite/settings.txt";
	private static Connection connection = null;
	private static String dbName;
	private static boolean dbActive = false;
	public static final int CORRECT = 1;
	public static final int WRONG = 2;
	public static final int ABSENT = 3;


	public static void startDatabase() {
		dbName = null;
		String dbNameFromFile = readSettings();
		if(dbNameFromFile == null) {
			dbActive = false;
			JOptionPane.showMessageDialog(null,"Ingen databas startades.\nFixa det under \"Inställningar\" om du vill");
		} else {
			dbActive = connectDataBase(dbNameFromFile, true);
			if(dbActive) dbName = dbNameFromFile;
		}
		System.out.println("start klar");
		System.out.println(dbName);
		System.out.println(dbActive);
	}

	public static Connection getConnection() {
		return connection;
	}

	public static String getDbName() {
		return dbName;
	}

	public static boolean isDbActive() {
		return dbActive;
	}

	public static void closeDatabase() {
		if (connection == null) return;
		try {
			connection.close();
			connection = null;
			dbActive = false;
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Fel vid stängning av databasen!!");
		}
	}

	// Funkar:
	private static boolean connectDataBase(String name, boolean shouldAlreadyExist) {
		try {
			if(connection != null) connection.close();
			connection = DriverManager.getConnection(baseURL + name + ".db");
			if(shouldAlreadyExist) {
				DatabaseMetaData md = connection.getMetaData();
				ResultSet rs = md.getTables(null, null, "%", null);
				int count = 0;
				boolean hasBenchesTable = false;
				while (rs.next()) {
					count++;
					if(rs.getString(3).equals(controlTable)) hasBenchesTable = true;
				}
				if(count != numberOfTables || !hasBenchesTable) {
					String mess = name + " kan ej användas.\nTroligen för gammal.";
					JOptionPane.showMessageDialog(null,mess);
					connection.close();
					return false;
				}
			}
			System.out.println("Connectat och klart!");
		}
		catch (SQLException s) {
			System.out.println("Kunde inte ansluta till databasen. " + s.getMessage());
			JOptionPane.showMessageDialog(null,"Oväntat fel vid anslutning till databas");
			if(connection != null) {
				try {
					connection.close();
					connection = null;
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
			return false;
		}
		return true;
	}


	public static void createNewDB() {
		dbActive = false;
		String newDBname = "";
		while (newDBname.isEmpty()) {
			newDBname = JOptionPane.showInputDialog(null, "Välj ett namn till den nya databasen.");
			if(newDBname == null) return;
			newDBname = newDBname.trim();
			if(newDBname.contains(".")) newDBname = "";
		}
		boolean hasConnected = connectDataBase(newDBname, false);
		if(hasConnected) dbActive = SetUpDatabase2.createTables(connection);
		if(dbActive) {
			createSettingsFile(newDBname);
			JOptionPane.showMessageDialog(null,"Ny databas klar!","Hurra",JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			String mess = "OBS: Ingen ny har skapats!\nDatabasen fanns troligen redan.\nProva annat namn.";
			JOptionPane.showMessageDialog(null,mess,"Se upp!",JOptionPane.ERROR_MESSAGE);
			closeDatabase();
		}
	}

	// Funkar:
	private static void createSettingsFile(String newDBname) {
		try {
			FileWriter fileWriter = new FileWriter(settingsPath);
			BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
			boolean useDB = newDBname != null && !newDBname.isEmpty();
			bufferWriter.write("USE_DATABASE=" + useDB);
			bufferWriter.newLine();
			if(useDB) bufferWriter.write("DB_NAME=" + newDBname);
			bufferWriter.newLine();
			bufferWriter.close();
			fileWriter.close();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "Något gick fel i filhanteringen");
		}
	}


	public static LinkedList<String> getClasses() {
		LinkedList<String> classList = new LinkedList<>();
		try {
			String query = "SELECT DISTINCT class FROM student";
			PreparedStatement prep = connection.prepareStatement(query);
			ResultSet resultSet = prep.executeQuery();
			while(resultSet.next()) {
				String cl = resultSet.getString("class");
				classList.add(cl);
			}
			prep.close();
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Ett fel uppstod vid inläsning av klasser: " + e.getMessage());
		}
		return classList;
	}

	private static String readSettings() {
		String nameFromFile = null;
		try {
			File settingsFile = new File(settingsPath);
			if (!settingsFile.exists()) return null;
			FileReader fileReader = new FileReader(settingsFile);
			BufferedReader b = new BufferedReader(fileReader);
			String line = b.readLine();
			if (line.substring(13).equals("true")) {
				line = b.readLine();
				nameFromFile = line.substring(8);
			}
		} catch (FileNotFoundException f) {
			System.out.println("Ingen settingsfil fanns");
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "Oväntat fel i readsettings i init.handler");
		} catch (Exception e) {
			System.out.println("FEeeel");
		}
		return nameFromFile;
	}

	public static void switchDB() {
		// Hämta namnen:
		File dbFolder = new File("C:/sqlite/");
		if(!dbFolder.exists()) {
			JOptionPane.showMessageDialog(null,"SQLite-mappen är ej skapad");
			return;
		}
		File[] databases = dbFolder.listFiles((dir, name) -> name.endsWith(".db"));
		if(databases == null || databases.length == 0) {
			JOptionPane.showMessageDialog(null,"Inga databaser hittades");
			return;
		}
		String[] dbNames = new String[databases.length];
		for (int i = 0; i < databases.length; i++) {
			dbNames[i] = databases[i].getName().substring(0, databases[i].getName().indexOf("."));
		}
		// Grafiken:
		JFrame dbFrame = new JFrame();
		JLabel header = new JLabel("Välj en av dina databaser:");
		header.setFont(new Font(null, Font.BOLD, 20));
		dbFrame.add(header);
		JPanel radios = new JPanel();
		radios.setLayout(new BoxLayout(radios, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(radios);
		dbFrame.setLayout(new BoxLayout(dbFrame.getContentPane(), BoxLayout.Y_AXIS));
		ButtonGroup group = new ButtonGroup();
        for (String name : dbNames) {
            JRadioButton r = new JRadioButton(name);
            r.setActionCommand(name);
            group.add(r);
            radios.add(r);
        }
		JPanel bp = new JPanel(new GridBagLayout());
		JButton finishButton = new JButton("Klar");
		finishButton.addActionListener(e -> {
			ButtonModel choice = group.getSelection();
			if(choice == null) {
				JOptionPane.showMessageDialog(null, "Gör ett val tack!");
				return;
			}
			String newDBname = choice.getActionCommand();
			if(newDBname == null) return;
			boolean success = DatabaseHandler2.connectDataBase(newDBname, true);
			if(success) {
				createSettingsFile(newDBname);
				dbActive = true;
				dbFrame.dispose();
			}
		});
		bp.add(finishButton);
		dbFrame.add(scrollPane);
		dbFrame.add(bp);
		int yMax = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		dbFrame.pack();
		if (dbFrame.getHeight() > ((int) (0.7 * yMax))) {
			dbFrame.setSize(dbFrame.getWidth(), (int) (yMax * 0.7));
		}
		dbFrame.setLocationRelativeTo(null);
		dbFrame.setVisible(true);
		dbFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	public static void disconnectDB() {
		closeDatabase();
		dbActive = false;
		createSettingsFile(null);
	}

}