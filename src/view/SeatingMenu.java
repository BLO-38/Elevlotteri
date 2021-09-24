package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class SeatingMenu {
    private final JTextField rowInput, columnInput, removeInput, enemyInput, forbiddenBenchesInput;
    private final JTextField firstRowInput, friendInput, firstRowNumberInput;
    private  final JCheckBox checkBoxShuffleEnemies;//, checkBoxShuffleFriends;
    private final JLabel allNames;
    private final JFrame frame;
    private final LinkedList<String> names;
    private final Color myRed = new Color(247, 212, 212);

    //TODO
    // Fokusen på radioknapparna
    // personer som man SKA sitte bredvid/i grupp med
    // spara i databas (bordsplac)
    // Fixa mellanslag resp kommatecken

    public SeatingMenu(LinkedList<String> names) {
        this.names = names;
        frame = new JFrame();
        frame.setLayout(new GridLayout(11, 1));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel namesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel columnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel removePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel enemyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel friendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel firstRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel firstRowNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel forbiddenBenchesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        headerPanel.setPreferredSize(new Dimension(300, 50));
        JLabel header = new JLabel("Bordsplacering");
        header.setFont(new Font(Font.MONOSPACED, Font.BOLD,24));
        headerPanel.add(header);

        allNames = new JLabel();
        allNames.setFont(new Font("arial", Font.PLAIN,10));
        setAllNames();
        namesPanel.add(allNames);

        rowPanel.add(new JLabel("Antal bänkrader i klassrummet:"));
        rowInput = new JTextField(5);
        rowInput.setText("5");
        rowPanel.add(rowInput);

        columnPanel.add(new JLabel("Antal bänkar på varje rad klassrummet:"));
        columnInput = new JTextField(5);
        columnInput.setText("10");
        columnPanel.add(columnInput);

        removePanel.add(new JLabel("Vilka elever ska bort?"));
        removeInput = new JTextField(50);
        removePanel.add(removeInput);

        enemyPanel.add(new JLabel("Vilka ska ej sitta nära varandra?"));
        enemyInput = new JTextField(50);
        enemyPanel.add(enemyInput);
        checkBoxShuffleEnemies = new JCheckBox("Blanda", false);
        enemyPanel.add(checkBoxShuffleEnemies);

        friendPanel.add(new JLabel("Vilka ska sitta bredvid varandra?"));
        friendInput = new JTextField(50);
        friendPanel.add(friendInput);

        firstRowPanel.add(new JLabel("På första raden:"));
        firstRowInput = new JTextField(50);
        firstRowPanel.add(firstRowInput);

        firstRowNumberPanel.add(new JLabel("De på första raden börjar på bänk nr:"));
        firstRowNumberInput = new JTextField(5);
        firstRowNumberPanel.add(firstRowNumberInput);
        firstRowNumberInput.setText("3");

        forbiddenBenchesPanel.add(new JLabel("Vilka bänkar ska ej användas?"));
        forbiddenBenchesInput = new JTextField(5);
        forbiddenBenchesPanel.add(forbiddenBenchesInput);

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
        frame.add(rowPanel);
        frame.add(columnPanel);
        frame.add(removePanel);
        frame.add(friendPanel);
        frame.add(enemyPanel);
        frame.add(firstRowPanel);
        frame.add(firstRowNumberPanel);
        frame.add(forbiddenBenchesPanel);
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
        boolean removeSuccess = true;
        LinkedList<String> firstRowNames = new LinkedList<>();
        String[] nameArr = firstRowInput.getText().split(",");
        StringBuilder notFound = new StringBuilder("Följande hittades ej: ");
        for (String n1 : nameArr) {
            String n2 = n1.trim();
            if (n2.length() == 0) continue;;
            if (names.contains(n2)) firstRowNames.add(n2);
            else {
                notFound.append(n2).append(",");
                removeSuccess = false;
            }
        }
        if(!removeSuccess) {
            firstRowInput.setBackground(myRed);
            JOptionPane.showMessageDialog(frame, notFound.toString());
            return;
        }

        // =============== Sätt rader och kolumner:
        int tables, rows = 1, columns = 1;
        boolean hasSizes = true;
        Scanner scanner = new Scanner(columnInput.getText());
        if (scanner.hasNextInt()) {
            columns = scanner.nextInt();
        } else {
            hasSizes = false;
            columnInput.setBackground(myRed);
        }
        scanner = new Scanner(rowInput.getText());
        if (scanner.hasNextInt()) {
            rows = scanner.nextInt();
        } else {
            hasSizes = false;
            rowInput.setBackground(myRed);
        }
        if(hasSizes) {
            tables = rows * columns;
            if (tables < names.size()) {
                JOptionPane.showMessageDialog(frame, "Alla får inte plats");
                return;
            }
        } else return;

        // =============== Gör lista med ovänner:
        removeSuccess = true;
        LinkedList<String> tempNames = new LinkedList<>(names);
        LinkedList<String> enemies = new LinkedList<>();
        String[] enemyArr = enemyInput.getText().split(",");
        notFound = new StringBuilder("Följande hittades ej: ");
        for(String enemy : enemyArr) {
            String trimmedEnemy = enemy.trim();
            if (trimmedEnemy.length() == 0) continue;;
            if (tempNames.remove(trimmedEnemy)) enemies.add(trimmedEnemy);
            else {
                notFound.append(trimmedEnemy).append(",");
                removeSuccess = false;
            }
        }
        if(!removeSuccess) {
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
            if (trimmedFriend.length() == 0) continue;;
            if (tempNames.remove(trimmedFriend)) friends.add(trimmedFriend);
            else {
                notFound.append(trimmedFriend).append(",");
                removeSuccess = false;
            }
        }
        if(!removeSuccess) {
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
        int firstRowStartPosition = Integer.parseInt(firstRowNumberInput.getText()) - 1;

        new ClassRoom(tempNames, enemies, friends, benchesToAvoid, firstRowNames, rows, columns, firstRowStartPosition);
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
