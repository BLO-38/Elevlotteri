package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Scanner;

public class GroupingMenu {
    private  final JTextField sizeInput, groupCountInput, removeInput, enemyInput;
    private JLabel allNames;
    private final JFrame frame;
    private final LinkedList<String> names;
    private Color myRed = new Color(247, 212, 212);
    private ButtonGroup bgr;

    public GroupingMenu(LinkedList<String> names) {
        this.names = names;
        frame = new JFrame();
        frame.setLayout(new GridLayout(8, 1));
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setPreferredSize(new Dimension(300, 50));
        JPanel namesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel groupSizePanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel groupSizePanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel removePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel enemyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel header = new JLabel("Gruppindelning");
        header.setFont(new Font(Font.MONOSPACED, Font.BOLD,24));
        headerPanel.add(header);

        allNames = new JLabel();
        allNames.setFont(new Font("arial", Font.PLAIN,10));
        setAllNames();
        namesPanel.add(allNames);

        // groupSizePanel1.add(new JLabel("Antal elever per grupp:"));

        // groupSizePanel2.add(new JLabel("Antal grupper:"));

        bgr = new ButtonGroup();
        JRadioButton sizeButton = new JRadioButton("Max antal elever per grupp");
        sizeButton.setActionCommand("1");
        JRadioButton countButton = new JRadioButton("Antal grupper");
        countButton.setActionCommand("2");
        sizeButton.setSelected(true);
        bgr.add(sizeButton);
        bgr.add(countButton);

        groupSizePanel1.add(sizeButton);
        sizeInput = new JTextField(5);
        sizeInput.setText("2");
        groupSizePanel1.add(sizeInput);

        groupSizePanel2.add(countButton);
        groupCountInput = new JTextField(5);
        groupCountInput.setText("10");
        groupSizePanel2.add(groupCountInput);

        removePanel.add(new JLabel("Vilka elever ska bort?"));
        removeInput = new JTextField(50);
        removePanel.add(removeInput);

        enemyPanel.add(new JLabel("Vilka ska ej sitta n�ra varandra?"));
        enemyInput = new JTextField(50);
        enemyPanel.add(enemyInput);

        JButton finishButton = new JButton("Skapa grupper");
        buttonPanel.add(finishButton);
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Gruppskapning!");
                tryFinish();
            }
        });

        frame.add(headerPanel);
        frame.add(namesPanel);
        frame.add(groupSizePanel1);
        frame.add(groupSizePanel2);
        frame.add(removePanel);
        frame.add(enemyPanel);
        frame.add(buttonPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void tryFinish() {
        int groupType = Integer.parseInt(bgr.getSelection().getActionCommand());
        System.out.println("Typ: " + groupType);
        boolean success = true;

        removeInput.setBackground(Color.WHITE);
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
            return;
        }

        sizeInput.setBackground(Color.WHITE);
        groupCountInput.setBackground(Color.WHITE);
        int groupCount = 1;
        if (groupType == 1) {
            Scanner scanner = new Scanner(sizeInput.getText());
            if (scanner.hasNextInt()) {
                double groupSize = scanner.nextInt();
                groupCount = (int) Math.ceil(names.size() / groupSize);
                if (groupCount < 2 || groupCount > names.size()) {
                    JOptionPane.showMessageDialog(frame, "Det blir ol�mpligt antal grupper!");
                    sizeInput.setBackground(myRed);
                    return;
                }
            } else {
                sizeInput.setBackground(myRed);
                return;
            }
        } else if(groupType == 2){
            Scanner scanner = new Scanner(groupCountInput.getText());
            if (scanner.hasNextInt()) {
                groupCount = scanner.nextInt();
                if (groupCount < 2 || groupCount > names.size()) {
                    JOptionPane.showMessageDialog(frame, "Det blir ol�mpligt antal grupper!");
                    groupCountInput.setBackground(myRed);
                    return;
                }
            } else {
                sizeInput.setBackground(myRed);
                return;
            }
        } else {
            System.out.println("SKA ALDRIG SKRIVAS, FEL P� RADION");
        }


        System.out.println("Klart!");
        System.out.println("Antal grupper: " + groupCount);

        // frame.setVisible(false);
        // new ClassRoom(benchNames, rows, columns);
        /*String resp = JOptionPane.showInputDialog(null, "Skriv numren p� de platser som ska l�mnas tomma:");
        scanner = new Scanner(resp);
        LinkedList<Integer> benchesToAvoid = new LinkedList<>();
        while (scanner.hasNextInt()) {
            benchesToAvoid.add(scanner.nextInt());
        }
        for(int ii : benchesToAvoid) {
            System.out.println("Undvik b�nk " + ii);
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
