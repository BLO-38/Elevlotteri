package view;

import databasen.DatabaseHandler;
import model.MainHandler;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SeatingMenu {
    private final JTextField rowInput, columnInput, enemyInput;
    private final JTextField missingBenchesInput, korridorInput, forbiddenBenchesInput;
    private final JTextField forbiddenRowsInput, forbiddenColumnsInput;
    private final JTextField firstRowInput, friendInput;
    private final JLabel allNamesText;
    private final JFrame frame;
    private final LinkedList<String> names;
    private final Color myRed = new Color(247, 212, 212);

    public SeatingMenu(LinkedList<String> names) {
        this.names = names;
        frame = new JFrame();
        frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS));

        Dimension longTextFieldDimension = new Dimension(300,18);
        Dimension shorttextFieldDimension = new Dimension(30,18);
        Dimension mediumTextFieldDimension = new Dimension(100,18);
        int textFieldRows = 10;
        int questionNr = 0;

        String [] questions = new String[textFieldRows];
        JTextField[] textFields = new JTextField[textFieldRows];

        questions[questionNr] = "Antal bänkrader i klassrummet:";
        rowInput = new JTextField("4");
        rowInput.setPreferredSize(shorttextFieldDimension);
        textFields[questionNr] = rowInput;
        questionNr++;

        questions[questionNr] = "Antal bänkar per rad:";
        columnInput = new JTextField("8");
        columnInput.setPreferredSize(shorttextFieldDimension);
        textFields[questionNr] = columnInput;
        questionNr++;

        questions[questionNr] = "Bänkar som saknas i klassrummet:";
        missingBenchesInput = new JTextField();
        missingBenchesInput.setPreferredSize(mediumTextFieldDimension);
        textFields[questionNr] = missingBenchesInput;
        questionNr++;

        questions[questionNr] = "Bänkar som ej används:";
        forbiddenBenchesInput = new JTextField();
        forbiddenBenchesInput.setPreferredSize(mediumTextFieldDimension);
        textFields[questionNr] = forbiddenBenchesInput;
        questionNr++;

        questions[questionNr] = "Rader som ej används:";
        forbiddenRowsInput = new JTextField();
        forbiddenRowsInput.setPreferredSize(mediumTextFieldDimension);
        textFields[questionNr] = forbiddenRowsInput;
        questionNr++;

        questions[questionNr] = "Bänkkolonner som ej används:";
        forbiddenColumnsInput = new JTextField();
        forbiddenColumnsInput.setPreferredSize(mediumTextFieldDimension);
        textFields[questionNr] = forbiddenColumnsInput;
        questionNr++;

        questions[questionNr] = "Efter vilka bänkar på rad 1 finns gångväg?";
        korridorInput = new JTextField();
        korridorInput.setPreferredSize(mediumTextFieldDimension);
        textFields[questionNr] = korridorInput;
        questionNr++;

        questions[questionNr] = "Vilka ska sitta bredvid varandra?";
        friendInput = new JTextField();
        friendInput.setPreferredSize(longTextFieldDimension);
        textFields[questionNr] = friendInput;
        questionNr++;

        questions[questionNr] = "Elever på första raden:";
        firstRowInput = new JTextField();
        firstRowInput.setPreferredSize(longTextFieldDimension);
        textFields[questionNr] = firstRowInput;
        questionNr++;

        questions[questionNr] = "Vilka ska INTE sitta bredvid varandra?";
        enemyInput = new JTextField("Ej klart");
        enemyInput.setPreferredSize(longTextFieldDimension);
        textFields[questionNr] = enemyInput;
        enemyInput.setEnabled(false);





        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel header = new JLabel("Bordsplacering");
        header.setFont(new Font(Font.MONOSPACED, Font.BOLD,28));
        headerPanel.add(header);
        headerPanel.setPreferredSize(new Dimension(300, 40));

        JPanel loadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loadButton = new JButton("Ladda gammal placering");
        loadPanel.add(loadButton);

        JPanel header2Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel header2 = new JLabel("Ny placering");
        header2.setFont(new Font(Font.MONOSPACED, Font.BOLD,18));
        header2Panel.add(header2);
        header2Panel.setPreferredSize(new Dimension(300, 30));


        JPanel namesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        allNamesText = new JLabel();
        allNamesText.setFont(new Font("arial", Font.PLAIN,10));
        setAllNamesText();
        namesPanel.add(allNamesText);

        JPanel questionsPanel = new JPanel();
        questionsPanel.setLayout(new GridLayout(textFieldRows, 1));

        for(int i=0 ; i<textFieldRows ; i++) {
            JPanel panel = new JPanel(new BorderLayout());
            JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel right = new JPanel(new FlowLayout(FlowLayout.LEFT));
            left.add(new JLabel(questions[i]));
            right.add(textFields[i]);
            right.setPreferredSize(new Dimension(longTextFieldDimension.width+20,longTextFieldDimension.height+4));
            panel.add(left, BorderLayout.WEST);
            panel.add(right, BorderLayout.EAST);
            questionsPanel.add(panel);
        }

        JPanel buttonPanel = new JPanel(new BorderLayout());

        JButton finishButton = new JButton("Skapa bordsplacering");
        finishButton.setBackground(MainHandler.MY_GREEN);
        finishButton.setForeground(Color.WHITE);
        JPanel pfb = new JPanel(new FlowLayout(FlowLayout.CENTER,15,10));
        pfb.add(finishButton);
        buttonPanel.add(pfb, BorderLayout.EAST);
        finishButton.addActionListener(e -> tryFinish());

        JButton removeButton = new JButton("Ta bort namn");
        removeButton.setBackground(MainHandler.MY_RED);
        removeButton.setForeground(Color.WHITE);
        JPanel prb = new JPanel(new FlowLayout(FlowLayout.CENTER,15,10));
        prb.add(removeButton);
        buttonPanel.add(prb,BorderLayout.WEST);
        removeButton.addActionListener(e -> {
            new RemoveDialog(frame,null,names);
            setAllNamesText();
        });

        loadButton.addActionListener(e -> new OldSeatingStarter(OldSeatingStarter.LOAD_CLASSROOM, DatabaseHandler.getCurrentClass()));

        frame.add(headerPanel);
        frame.add(Box.createRigidArea(new Dimension(0, 15)));
        frame.add(loadPanel);
        frame.add(Box.createRigidArea(new Dimension(0, 20)));
        frame.add(header2Panel);
        frame.add(namesPanel);
        frame.add(questionsPanel);
        frame.add(buttonPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
            if (n2.isEmpty()) continue;
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
        /*
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

         */

        // =============== Gör lista med bänkkompisatr:
        LinkedList<String> friends = new LinkedList<>();
        String[] friendArr = friendInput.getText().split(",");
        if(friendArr.length == 0 || (friendArr.length==1 && friendArr[0].isEmpty())) {
            System.out.println("Inga vänner");
        } else {
            if(friendArr.length%2 != 0) {
                System.out.println("FRARR " + friendArr.length);
                System.out.println("FRARR " + friendArr.length % 2);
                System.out.println(Arrays.toString(friendArr));
                JOptionPane.showMessageDialog(null, "Måste vara jämnat antal vänner!");
                return;
            }
        }
        notFound = new StringBuilder("Följande hittades ej: ");
//        success = true;
        for(String friend : friendArr) {
            System.out.println("Friendvarv!");
            String trimmedFriend = friend.trim();
            if (trimmedFriend.isEmpty()) continue;
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
        System.out.println(friends.size());
        OuterLoop:
        for (int i = 0; i < friends.size(); i++) {
            for (int j = i+1; j < friends.size(); j++) {
                if(friends.get(i).equals(friends.get(j))) {
                    success = false;
                    System.out.println("Dubbelkoll failade");
                    break OuterLoop;
                }
            }
        }

        if(!success) {
            friendInput.setBackground(myRed);
            JOptionPane.showMessageDialog(frame, "Du skrev samma fler gånger");
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
                if(!corre.isEmpty()) {
                    JOptionPane.showMessageDialog(null,"Du skrev nåt konstigt i mellanrumsrutan");
                    return;
                }
            }
        }

        // Tomma
        LinkedList<Integer> forbiddenBenches = new LinkedList<>();
        String[] empties = forbiddenBenchesInput.getText().split(",");

        for(String emptyBench : empties) {
            try {
                int index = Integer.parseInt(emptyBench);
                if (index > 0 && index <= tables) {
                    if(!forbiddenBenches.contains(index)) forbiddenBenches.add(index);
                }
            } catch (NumberFormatException n) {
                if(!emptyBench.isEmpty()) {
                    JOptionPane.showMessageDialog(null,"Du skrev nåt konstigt på bänkar som inte används");
                    return;
                }
            }
        }

        String[] forbiddenRows = forbiddenRowsInput.getText().split(",");
        for(String forbiddenRow : forbiddenRows) {
            try {
                int forbRow = Integer.parseInt(forbiddenRow);
                if (forbRow > 0 && forbRow <= rows) {
                    for(int nr = (forbRow-1)*columns + 1 ; nr < forbRow*columns+1 ; nr++ )
                        if(!forbiddenBenches.contains(nr)) forbiddenBenches.add(nr);
                }
            } catch (NumberFormatException n) {
                if(!forbiddenRow.isEmpty()) {
                    JOptionPane.showMessageDialog(null,"Du skrev nåt konstigt på bänkrader som inte används");
                    return;
                }
            }
        }

        String[] forbiddenCols = forbiddenColumnsInput.getText().split(",");
        for(String forbiddenCol : forbiddenCols) {
            try {
                int forbCol = Integer.parseInt(forbiddenCol);
                if (forbCol > 0 && forbCol <= columns) {
                    for(int nr = forbCol; nr <= rows*columns ; nr+=columns )
                        if(!forbiddenBenches.contains(nr)) forbiddenBenches.add(nr);
                }
            } catch (NumberFormatException n) {
                if(!forbiddenCol.isEmpty()) {
                    JOptionPane.showMessageDialog(null,"Du skrev nåt konstigt på bänkrader som inte används");
                    return;
                }
            }
        }


        // Bänkar som inte finns
        LinkedList<Integer> missingBenches = new LinkedList<>();
        String[] missings = missingBenchesInput.getText().split(",");
        for(String missing : missings) {
            try {
                int index = Integer.parseInt(missing);
                if (index > 0 && index <= tables) {
                    if(!missingBenches.contains(index)) missingBenches.add(index);
                }
            } catch (NumberFormatException n) {
                if(!missing.isEmpty()) JOptionPane.showMessageDialog(null,"Du skrev nåt konstigt på saknade bänkar");
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
        new ClassRoom4(names,corrar,friends,firstRowNames,forbiddenBenches,missingBenches,rows,columns,true,DatabaseHandler.getCurrentClass());
    }
    private void setAllNamesText() {
        StringBuilder sb = new StringBuilder("<html>");
        int count = 0;
        LinkedList<String> sortedCopy = new LinkedList<>(names);
        Collections.sort(sortedCopy);
        for (String s : sortedCopy) {
            sb.append(s).append(",");
            count++;
            if(count % 15 == 0) sb.append("<br>");
        }
        sb.append(" Antal: ").append(sortedCopy.size());
        sb.append("</html>");
        allNamesText.setText(sb.toString());
    }
}
