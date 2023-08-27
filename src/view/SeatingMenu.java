package view;

import databasen.DatabaseHandler;
import databasen.SelectHandler;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.*;

public class SeatingMenu {
    private final JTextField rowInput, columnInput, enemyInput;
    private final JTextField forbiddenBenchesInput, korridorInput, emptyBenchesInput;
    private final JTextField firstRowInput, friendInput, firstRowNumberInput;
    private final JLabel allNames;
    private final JFrame frame;
    private final LinkedList<String> names;
    private final Color myRed = new Color(247, 212, 212);
    private String loadedBenchData = null;
    private int startInDatabase = 0;
    private JButton previous10button, next10button;
    private JButton[] buttons;
    private final int antalButtons = 10;

    public SeatingMenu(LinkedList<String> names) {
        this.names = names;
        frame = new JFrame();
        frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS));

        Dimension textFieldDimension = new Dimension(300,18);
        int textFieldRows = 9;

        String [] questions = new String[textFieldRows];
        questions[0] = "Antal bänkrader i klassrummet:";
        questions[1] = "Antal bänkar per rad:";
        questions[2] = "Vilka ska sitta bredvid varandra?";
        questions[3] = "Vilka ska INTE sitta bredvid varandra?";
        questions[4] = "Elever på första raden:";
        questions[5] = "Elever på första raden börjar på bänk nr:";
        questions[6] = "Bänkar som saknas i klassrummet:";
        questions[7] = "Bänkar som ej används:";
        questions[8] = "Efter vilka bänkar på rad 1 finns gångväg?";

        JTextField[] textFields = new JTextField[textFieldRows];
        rowInput = new JTextField("5");
        textFields[0] = rowInput;
        columnInput = new JTextField("10");
        textFields[1] = columnInput;
        friendInput = new JTextField();
        textFields[2] = friendInput;
        enemyInput = new JTextField();
        textFields[3] = enemyInput;
        firstRowInput = new JTextField();
        textFields[4] = firstRowInput;
        firstRowNumberInput = new JTextField();
        textFields[5] = firstRowNumberInput;
        forbiddenBenchesInput = new JTextField();
        textFields[6] = forbiddenBenchesInput;
        emptyBenchesInput = new JTextField();
        textFields[7] = emptyBenchesInput;
        korridorInput = new JTextField();
        textFields[8] = korridorInput;

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBorder(new LineBorder(Color.BLACK));
        JLabel header = new JLabel("Bordsplacering");
        header.setFont(new Font(Font.MONOSPACED, Font.BOLD,24));
        headerPanel.add(header);
        headerPanel.setPreferredSize(new Dimension(300, 40));

        JPanel namesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namesPanel.setBorder(new LineBorder(Color.GREEN));
        allNames = new JLabel();
        allNames.setFont(new Font("arial", Font.PLAIN,10));
        setAllNames();
        namesPanel.add(allNames);

        JPanel questionsPanel = new JPanel();
        questionsPanel.setLayout(new GridLayout(textFieldRows, 1));
        questionsPanel.setBorder(new LineBorder(Color.BLUE));

        for(int i=0 ; i<textFieldRows ; i++) {
            JPanel panel = new JPanel(new BorderLayout());
            JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            left.add(new JLabel(questions[i]));
            right.add(textFields[i]);
            textFields[i].setPreferredSize(textFieldDimension);
            panel.add(left, BorderLayout.WEST);
            panel.add(right, BorderLayout.EAST);
            questionsPanel.add(panel);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(new LineBorder(Color.RED));
        JButton finishButton = new JButton("Skapa bordsplacering");
        buttonPanel.add(finishButton);
        finishButton.addActionListener(e -> tryFinish());

        JButton loadButton = new JButton("Ladda gammal placering");

        loadButton.addActionListener(e -> {
            chooseLesson();
            String[] dataParts = loadedBenchData.split("qqq");

            String[] roomDimensions = dataParts[0].split("#");
            int rows = Integer.parseInt(roomDimensions[0]);
            int columns = Integer.parseInt(roomDimensions[1]);

            String[] corridors = dataParts[1].split("#");

            String[] names1 = dataParts[2].split("#");

            new ClassRoom3(names1, corridors, rows, columns);
        });
        buttonPanel.add(loadButton);


        frame.add(headerPanel);
        frame.add(namesPanel);
        frame.add(questionsPanel);
        frame.add(buttonPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void chooseLesson() {
        startInDatabase = 0;
        JDialog dialog = new JDialog(frame);
        dialog.setModal(true);
        dialog.setLayout(new FlowLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(antalButtons+2,1));

        buttons = new JButton[antalButtons];
        for (int i = 0; i < antalButtons; i++) {
            buttons[i] = new JButton();
            buttons[i].addActionListener(e -> {
                loadedBenchData = e.getActionCommand();
                dialog.setVisible(false);
            });
            mainPanel.add(buttons[i]);
        }

        next10button = new JButton("Hämta fler");
        next10button.setBackground(Color.GREEN);
        next10button.addActionListener(e -> updateButtons(antalButtons));
        mainPanel.add(next10button);

        previous10button = new JButton("Föregående 10");
        previous10button.setBackground(Color.RED);
        previous10button.addActionListener(e -> updateButtons(-antalButtons));
        mainPanel.add(previous10button);

        updateButtons(0);

        dialog.add(mainPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void updateButtons(int deltaLimit) {
        startInDatabase += deltaLimit;
        previous10button.setEnabled(startInDatabase > 0);
        String[][] buttonDataTable = SelectHandler.getBenches(DatabaseHandler.getCurrentClass(), antalButtons, startInDatabase);

        for (int i = 0; i < antalButtons; i++) {
            buttons[i].setText(buttonDataTable[i][0]);
            buttons[i].setActionCommand(buttonDataTable[i][1]);
            buttons[i].setEnabled(buttonDataTable[i][0] != null);
        }
        next10button.setEnabled(buttons[antalButtons-1].getText() != null);
    }


    private void tryFinish() {
        System.out.println("Startlista: " + names);
        enemyInput.setBackground(Color.WHITE);
        columnInput.setBackground(Color.WHITE);
        rowInput.setBackground(Color.WHITE);
        firstRowInput.setBackground(Color.WHITE);

        // =============== Ta bort elever:
        boolean success = true;
        setAllNames();

        // =============== Förwsta raden
        LinkedList<String> firstRowNames = new LinkedList<>();
        String[] nameArr = firstRowInput.getText().split(",");
        StringBuilder notFound = new StringBuilder("Följande hittades ej: ");
        for (String n1 : nameArr) {
            String n2 = n1.trim();
            if (n2.length() == 0) continue;
            if (names.contains(n2)) firstRowNames.add(n2);
            else {
                notFound.append(n2).append(",");
                success = false;
            }
        }
        if(!success) {
            firstRowInput.setBackground(myRed);
            JOptionPane.showMessageDialog(frame, notFound.toString());
            return;
        }

        // =============== Sätt rader och kolumner:
        int tables, rows = 1, columns = 1;
        Scanner scanner = new Scanner(columnInput.getText());
        if (scanner.hasNextInt()) {
            columns = scanner.nextInt();
        } else {
            success = false;
            columnInput.setBackground(myRed);
        }
        scanner = new Scanner(rowInput.getText());
        if (scanner.hasNextInt()) {
            rows = scanner.nextInt();
        } else {
            success = false;
            rowInput.setBackground(myRed);
        }
        if (!success) return;
        tables = rows * columns;
        if (tables < names.size()) {
            JOptionPane.showMessageDialog(frame, "Alla får inte plats");
            return;
        }


        // =============== Gör lista med ovänner:
        LinkedList<String> regularNames = new LinkedList<>(names);
        LinkedList<String> enemies = new LinkedList<>();
        String[] enemyArr = enemyInput.getText().split(",");
        notFound = new StringBuilder("Följande hittades ej: ");
        for(String enemy : enemyArr) {
            String trimmedEnemy = enemy.trim();
            if (trimmedEnemy.length() == 0) continue;
            if (regularNames.remove(trimmedEnemy)) enemies.add(trimmedEnemy);
            else {
                notFound.append(trimmedEnemy).append(",");
                success = false;
            }
        }
        if(!success) {
            enemyInput.setBackground(myRed);
            JOptionPane.showMessageDialog(frame, notFound.toString());
            return;
        }

        // =============== Gör lista med bänkkompisatr:
        LinkedList<String> friends = new LinkedList<>();
        String[] friendArr = friendInput.getText().split(",");
        notFound = new StringBuilder("Följande hittades ej: ");
        for(String friend : friendArr) {
            String trimmedFriend = friend.trim();
            if (trimmedFriend.length() == 0) continue;
            if (regularNames.remove(trimmedFriend)) friends.add(trimmedFriend);
            else {
                notFound.append(trimmedFriend).append(",");
                success = false;
            }
        }
        if(!success) {
            friendInput.setBackground(myRed);
            JOptionPane.showMessageDialog(frame, notFound.toString());
            return;
        }

        int firstRowStartPosition = 0;
        try {
            firstRowStartPosition = Integer.parseInt(firstRowNumberInput.getText()) - 1;
            if(firstRowStartPosition < 0 || firstRowStartPosition > 10) firstRowStartPosition = 0;
        } catch (Exception e) {
            System.out.println("Fel på parseInt i seatingmenu");
        }
        String korrar = korridorInput.getText().trim();
        String[] corridors = korrar.equals("") ? new String[0] : korrar.split(",");


        String[] benches = new String[rows*columns];
        Arrays.fill(benches,"");
        int notAvailable = 0;

        // Tomma
        String[] empties = emptyBenchesInput.getText().split(",");
        for(String emptyBench : empties) {
            try {
                int index = Integer.parseInt(emptyBench);
                if (index > 0 && index <= benches.length) {
                    benches[index - 1] = "x";
                    notAvailable++;
                }
            } catch (NumberFormatException n) {
                if(emptyBench.length()>0) JOptionPane.showMessageDialog(null,"Du skrev nåt konstigt");
            }
        }


        // Bänkar som inte finns
        String[] fbb = forbiddenBenchesInput.getText().split(",");
        for(String fb : fbb) {
            try {
                int index = Integer.parseInt(fb);
                if (index > 0 && index <= benches.length) {
                    benches[index - 1] = "-";
                    notAvailable++;
                }
            } catch (NumberFormatException n) {
                if(fb.length()>0) JOptionPane.showMessageDialog(null,"Du skrev nåt konstigt");
            }
        }
        if ((regularNames.size() + notAvailable > tables)) {
            JOptionPane.showMessageDialog(frame, "Du tog bort för många bänkar. Alla får inte plats längre.");
            return;
        }
        // Sätt ut namnen
        int benchNr = 0;
        for (String name : regularNames) {
            while (benches[benchNr].equals("-") || benches[benchNr].equals("x")) benchNr++;
            benches[benchNr] = name;
            benchNr++;
        }

        new ClassRoom3(benches,corridors, rows, columns);
    }
    private void setAllNames() {
        StringBuilder sb = new StringBuilder("<html>");
        int count = 0;
        LinkedList<String> sortedCopy = new LinkedList<>(names);
        Collections.sort(sortedCopy);
        for (String s : sortedCopy) {
            sb.append(s).append(",");
            count++;
            if(count % 15 == 0) sb.append("<br>");
        }
        sb.append("</html>");
        allNames.setText(sb.toString());
    }
}
