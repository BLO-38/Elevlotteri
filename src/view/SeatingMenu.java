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
    private final JTextField firstRowInput, friendInput;
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
        int textFieldRows = 8;

        String [] questions = new String[textFieldRows];
        questions[0] = "Antal bänkrader i klassrummet:";
        questions[1] = "Antal bänkar per rad:";
        questions[2] = "Vilka ska sitta bredvid varandra?";
        questions[3] = "Vilka ska INTE sitta bredvid varandra?";
        questions[4] = "Elever på första raden:";
        questions[5] = "Bänkar som saknas i klassrummet:";
        questions[6] = "Bänkar som ej används:";
        questions[7] = "Efter vilka bänkar på rad 1 finns gångväg?";

        JTextField[] textFields = new JTextField[textFieldRows];
        rowInput = new JTextField("4");
        textFields[0] = rowInput;
        columnInput = new JTextField("8");
        textFields[1] = columnInput;
        friendInput = new JTextField();
        textFields[2] = friendInput;
        enemyInput = new JTextField();
        textFields[3] = enemyInput;
        enemyInput.setText("Ej klart");
        enemyInput.setEnabled(false);
        firstRowInput = new JTextField();
        textFields[4] = firstRowInput;
        forbiddenBenchesInput = new JTextField();
        textFields[5] = forbiddenBenchesInput;
        emptyBenchesInput = new JTextField();
        textFields[6] = emptyBenchesInput;
        korridorInput = new JTextField();
        textFields[7] = korridorInput;


        enemyInput.setEnabled(false);

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

        JButton removeButton = new JButton("Ta bort namn");
        removeButton.addActionListener(e -> {
            new RemoveDialog(frame,null,names);
            setAllNames();
        });

        JButton loadButton = new JButton("Ladda gammal placering");
        loadButton.addActionListener(e -> {
            // Vi kör:
            // 0 rad & col
            // 1 korridorer
            // 2 namnen
            loadedBenchData = null;
            chooseLesson();
            if(loadedBenchData == null) return;
            String[] dataParts = loadedBenchData.split("qqq");

            String[] roomDimensions = dataParts[0].split("#");
            int rows = Integer.parseInt(roomDimensions[0]);
            int columns = Integer.parseInt(roomDimensions[1]);

            String[] corridors = dataParts[1].split("#");
            LinkedList<Integer> corrList = new LinkedList<>();
            for (String c : corridors) corrList.add(Integer.parseInt(c));

            String[] names1 = dataParts[2].split("#");

            LinkedList<Integer> friendList = new LinkedList<>();
            if(dataParts.length > 3) {
                String[] friends = dataParts[3].split("#");
                for (String f : friends) friendList.add(Integer.parseInt(f));
            }

            LinkedList<Integer> firstRowList = new LinkedList<>();
            if(dataParts.length > 4) {
                String[] firstRow = dataParts[4].split("#");
                for (String fr : firstRow) firstRowList.add(Integer.parseInt(fr));
            }

            // Behöver helan namnstringen, corrs, friends, fiirstrow
            new ClassRoom3(names1, corridors, rows, columns);
        });

        buttonPanel.add(removeButton);
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
        enemyInput.setBackground(Color.WHITE);
        columnInput.setBackground(Color.WHITE);
        rowInput.setBackground(Color.WHITE);
        firstRowInput.setBackground(Color.WHITE);
        friendInput.setBackground(Color.WHITE);

        boolean success = true;

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
        LinkedList<String> enemies = new LinkedList<>();
        String[] enemyArr = enemyInput.getText().split(",");
        notFound = new StringBuilder("Följande hittades ej: ");
        for(String enemy : enemyArr) {
            String trimmedEnemy = enemy.trim();
            if (trimmedEnemy.length() == 0) continue;
            if (names.contains(trimmedEnemy)) enemies.add(trimmedEnemy);
            else {
                notFound.append(trimmedEnemy).append(",");
                success = false;
            }
        }
        if(!success) {
            enemyInput.setBackground(myRed);
            System.out.println("Ohittade fiiender: " + notFound);
            // JOptionPane.showMessageDialog(frame, notFound.toString());
            //return;
        }

        // =============== Gör lista med bänkkompisatr:
        LinkedList<String> friends = new LinkedList<>();
        String[] friendArr = friendInput.getText().split(",");
        if(friendArr.length%2 != 0) {
            JOptionPane.showMessageDialog(null,"Måste vara jämnat antal vänner!");
            return;
        }
        notFound = new StringBuilder("Följande hittades ej: ");
        success = true;
        for(String friend : friendArr) {
            String trimmedFriend = friend.trim();
            if (trimmedFriend.length() == 0) continue;
            if (names.contains(trimmedFriend)){
                friends.add(trimmedFriend);
            }
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


        // Korrar
        LinkedList<Integer> corrar = new LinkedList<>();
        String[] corrArr = korridorInput.getText().split(",");
        for(String corre : corrArr) {
            try {
                int index = Integer.parseInt(corre);
                if (index >= 0 && index <= columns) corrar.add(index);
            } catch (NumberFormatException n) {
                if(corre.length()>0) {
                    JOptionPane.showMessageDialog(null,"Du skrev nåt konstigt i mellanrumsrutan");
                    return;
                }
            }
        }

        // Tomma
        LinkedList<Integer> forbiddenBenches = new LinkedList<>();
        String[] empties = emptyBenchesInput.getText().split(",");
        for(String emptyBench : empties) {
            try {
                int index = Integer.parseInt(emptyBench);
                if (index > 0 && index <= tables) {
                    if(!forbiddenBenches.contains(index)) forbiddenBenches.add(index);
                }
            } catch (NumberFormatException n) {
                if(emptyBench.length()>0) {
                    JOptionPane.showMessageDialog(null,"Du skrev nåt konstigt på bänkar som inte används");
                    return;
                }
            }
        }


        // Bänkar som inte finns
        LinkedList<Integer> missingBenches = new LinkedList<>();
        String[] missings = forbiddenBenchesInput.getText().split(",");
        for(String missing : missings) {
            try {
                int index = Integer.parseInt(missing);
                if (index > 0 && index <= tables) {
                    if(!missingBenches.contains(index)) missingBenches.add(index);
                }
            } catch (NumberFormatException n) {
                if(missing.length()>0) JOptionPane.showMessageDialog(null,"Du skrev nåt konstigt på saknade bänkar");
            }
        }
        if ((names.size() + missingBenches.size() + forbiddenBenches.size() > tables)) {
            JOptionPane.showMessageDialog(frame, "Du tog bort för många bänkar. Alla får inte plats längre.");
            return;
        }
        System.out.println("ALla " + names);
        System.out.println("forbj " + forbiddenBenches);
        System.out.println("Saknas " + missingBenches);
        System.out.println("Föörsta raden " + firstRowNames);
        System.out.println("Kompisar "+ friends);
        System.out.println("korr " + corrar);
        //new ClassRoom3(benches,corridors, rows, columns);
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
