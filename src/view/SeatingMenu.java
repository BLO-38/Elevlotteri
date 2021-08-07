package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Scanner;

public class SeatingMenu {
    private  final JTextField rowInput, columnInput, removeInput, enemyInput;
    private JLabel allNames;
    private final LinkedList<String> names;
    private Color myRed = new Color(247, 212, 212);

    public SeatingMenu(LinkedList<String> names) {
        this.names = names;
        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(7, 1));
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setPreferredSize(new Dimension(300, 50));
        JPanel namesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel columnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel removePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel enemyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel header = new JLabel("Bordsplacering");
        header.setFont(new Font(Font.MONOSPACED, Font.BOLD,24));
        headerPanel.add(header);

        allNames = new JLabel();
        allNames.setFont(new Font("arial", Font.PLAIN,10));
        setAllNames();
        namesPanel.add(allNames);

        namesPanel.add(allNames);

        rowPanel.add(new JLabel("Antal bänkrader i klassrummet:"));
        rowInput = new JTextField(5);
        rowPanel.add(rowInput);

        columnPanel.add(new JLabel("Antal bänkar på varje rad klassrummet:"));
        columnInput = new JTextField(5);
        columnPanel.add(columnInput);

        removePanel.add(new JLabel("Vilka elever ska bort?"));
        removeInput = new JTextField(50);
        removePanel.add(removeInput);

        enemyPanel.add(new JLabel("Vilka ska ej sitta nära varandra?"));
        enemyInput = new JTextField(50);
        enemyPanel.add(enemyInput);

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
        frame.add(enemyPanel);
        frame.add(buttonPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void tryFinish() {
        boolean success = true;
        removeInput.setBackground(Color.WHITE);

        String [] namesToRemove = removeInput.getText().split(",");
        StringBuilder failNames = new StringBuilder();
        for (String n1 : namesToRemove) {
            String n2 = n1.trim();
            System.out.println("Vi kollar " + n2);
            if(n2.length() != 0 && !names.remove(n2)) {
                failNames.append(n2).append(",");
                success = false;
            }
        }
        if (!success) {
            removeInput.setBackground(myRed);
            removeInput.setText(failNames.toString());
        }

        columnInput.setBackground(Color.WHITE);
        rowInput.setBackground(Color.WHITE);
        int tables = 0, rows = 1, columns = 1;
        boolean sizeCalculationSuccess = true;
        Scanner scanner = new Scanner(columnInput.getText());
        if (scanner.hasNextInt()) {
            columns = scanner.nextInt();
        } else {
            sizeCalculationSuccess = false;
            columnInput.setBackground(myRed);
        }
        scanner = new Scanner(rowInput.getText());
        if (scanner.hasNextInt()) {
            rows = scanner.nextInt();
        } else {
            sizeCalculationSuccess = false;
            rowInput.setBackground(myRed);
        }

        tables = rows * columns;

        if (tables < names.size()) {
            sizeCalculationSuccess = false;

        }
        setAllNames();
        // Ta hand om enemies

        /*String resp = JOptionPane.showInputDialog(null, "Skriv numren på de platser som ska lämnas tomma:");
        scanner = new Scanner(resp);
        LinkedList<Integer> benchesToAvoid = new LinkedList<>();
        while (scanner.hasNextInt()) {
            benchesToAvoid.add(scanner.nextInt());
        }
        for(int ii : benchesToAvoid) {
            System.out.println("Undvik bänk " + ii);
        }*/


    }
    private void setAllNames() {
        StringBuilder sb = new StringBuilder();
        for (String s : names) {
            sb.append(s).append(",");
        }
        allNames.setText(sb.toString());
    }
}
