package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class SeatingMenu {
    private final JTextField rowInput, columnInput, removeInput, enemyInput, forbiddenBenchesInput, korridorInput;
    private final JTextField firstRowInput, friendInput, firstRowNumberInput;
    // private  final JCheckBox checkBoxEnemiesOnFirstRow;
    private final JLabel allNames;
    private final JFrame frame;
    private final LinkedList<String> names;
    private final Color myRed = new Color(247, 212, 212);

    //TODO
    // spara i databas (bordsplac)
    // Fixa mellanslag resp kommatecken

    public SeatingMenu(LinkedList<String> names) {
        this.names = names;
        frame = new JFrame();
        frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS));

        Dimension textFieldDimension = new Dimension(300,18);
        int textFieldRows = 9;

        String [] questions = new String[textFieldRows];
        questions[0] = "Antal bänkrader i klassrummet:";
        questions[1] = "Antal bänkar per rad:";
        questions[2] = "Elever som ska bort:";
        questions[3] = "Vilka ska sitta bredvid varandra?";
        questions[4] = "Vilka ska INTE sitta bredvid varandra?";
        questions[5] = "Elever på första raden:";
        questions[6] = "Elever på första raden börjar på bänk nr:";
        questions[7] = "Bänkar som saknas i klassrummet:";
        questions[8] = "Efter vilka bänkar på rad 1 finns gångväg?";

        JTextField[] textFields = new JTextField[textFieldRows];
        rowInput = new JTextField("5");
        textFields[0] = rowInput;
        columnInput = new JTextField("10");
        textFields[1] = columnInput;
        removeInput = new JTextField();
        textFields[2] = removeInput;
        friendInput = new JTextField();
        textFields[3] = friendInput;
        enemyInput = new JTextField();
        textFields[4] = enemyInput;
        firstRowInput = new JTextField();
        textFields[5] = firstRowInput;
        firstRowNumberInput = new JTextField();
        textFields[6] = firstRowNumberInput;
        forbiddenBenchesInput = new JTextField();
        textFields[7] = forbiddenBenchesInput;
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
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Bordsplacering");
                tryFinish();
            }
        });

        frame.add(headerPanel);
        frame.add(namesPanel);
        frame.add(questionsPanel);
        frame.add(buttonPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void tryFinish() {
        System.out.println("Startlista: " + names);
        enemyInput.setBackground(Color.WHITE);
        removeInput.setBackground(Color.WHITE);
        columnInput.setBackground(Color.WHITE);
        rowInput.setBackground(Color.WHITE);
        firstRowInput.setBackground(Color.WHITE);

        // =============== Ta bort elever:
        boolean success = true;
        String [] namesToRemove = removeInput.getText().split(",");
        StringBuilder failNames = new StringBuilder();
        for (String n1 : namesToRemove) {
            String n2 = n1.trim();
            if(n2.length() != 0 && !names.remove(n2)) {
                failNames.append(n2).append(",");
                success = false;
            }
        }
        setAllNames();
        removeInput.setText(failNames.toString());
        if (!success) {
            removeInput.setBackground(myRed);
            JOptionPane.showMessageDialog(frame, "Följande hittades inte: " + failNames);
            return;
        }
        System.out.println("Efter borttagning: " + names);

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
        // boolean hasSizes = true;
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

        // =============== Gör lista med bänkar att undvika
        scanner = new Scanner(forbiddenBenchesInput.getText());
        scanner.useDelimiter(",");
        LinkedList<Integer> benchesToAvoid = new LinkedList<>();
        while (scanner.hasNextInt()) {
            benchesToAvoid.add(scanner.nextInt());
        }
        System.out.println("Benches ta: " + benchesToAvoid);

        if ((names.size() + benchesToAvoid.size() > tables)) {
            JOptionPane.showMessageDialog(frame, "Du tog bort för många bänkar. Alla får inte plats längre.");
            return;
        }
        Collections.sort(benchesToAvoid);
        int firstRowStartPosition = 0;
        try {
            firstRowStartPosition = Integer.parseInt(firstRowNumberInput.getText()) - 1;
            if(firstRowStartPosition < 0 || firstRowStartPosition > 10) firstRowStartPosition = 0;
        } catch (Exception e) {
            System.out.println("Fel på parseInt i seatingmenu");
        }

        new ClassRoom(regularNames, enemies, friends, benchesToAvoid, firstRowNames, null, rows, columns, firstRowStartPosition, false);//checkBoxEnemiesOnFirstRow.isSelected());
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
