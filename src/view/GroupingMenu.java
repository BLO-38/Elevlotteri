package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class GroupingMenu {
    private  final JTextField sizeInput, groupCountInput, removeInput, enemyInput;
    private final JLabel allNames;
    private final JFrame frame;
    private final JCheckBox numberCheckBox;
    private final LinkedList<String> names;
    private final Color myRed = new Color(247, 212, 212);
    private final ButtonGroup bgr;
    private final ButtonGroup bgrResize;
    //TODO
    // Tooltip för radioknapparna

    public GroupingMenu(LinkedList<String> names) {
        this.names = names;
        frame = new JFrame();
        frame.setLayout(new GridLayout(10, 1));
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setPreferredSize(new Dimension(300, 50));
        JPanel namesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel groupSizePanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel groupSizePanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel removePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel enemyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel showNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel uniqueTwoGroupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel resizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel header = new JLabel("Gruppindelning");
        header.setFont(new Font(Font.MONOSPACED, Font.BOLD,24));
        headerPanel.add(header);

        allNames = new JLabel();
        allNames.setFont(new Font("arial", Font.PLAIN,10));
        setAllNames();
        namesPanel.add(allNames);

        bgr = new ButtonGroup();
        JRadioButton sizeButton = new JRadioButton("Max antal elever per grupp");
        sizeButton.setActionCommand("1");
        JRadioButton countButton = new JRadioButton("Antal grupper");
        countButton.setActionCommand("2");
        JRadioButton uniqueTwoButton = new JRadioButton("Unika två-grupper");
        uniqueTwoButton.setActionCommand("3");
        countButton.setSelected(true);
        bgr.add(sizeButton);
        bgr.add(countButton);
        bgr.add(uniqueTwoButton);

        uniqueTwoButton.setToolTipText("Ett tips från Lars");
        System.out.println(ToolTipManager.sharedInstance().getInitialDelay());
        ToolTipManager.sharedInstance().setInitialDelay(0);
        ToolTipManager.sharedInstance().setDismissDelay(1000);
        groupSizePanel1.add(sizeButton);
        sizeInput = new JTextField(5);
        sizeInput.setText("2");
        groupSizePanel1.add(sizeInput);
        sizeInput.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                System.out.println("Fokus in");
                sizeButton.setSelected(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("Fokus lost");
            }
        });

        groupSizePanel2.add(countButton);
        groupCountInput = new JTextField(5);
        groupCountInput.setText("3");
        groupSizePanel2.add(groupCountInput);
        groupCountInput.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                countButton.setSelected(true);
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });

        uniqueTwoGroupPanel.add(uniqueTwoButton);

        removePanel.add(new JLabel("Vilka elever ska bort?"));
        removeInput = new JTextField(50);
        removePanel.add(removeInput);

        enemyPanel.add(new JLabel("Vilka ska ej vara i samma grupp?"));
        enemyInput = new JTextField(40);
        enemyPanel.add(enemyInput);

        numberCheckBox = new JCheckBox("Visa gruppnummer");
        showNumberPanel.add(numberCheckBox );
        numberCheckBox.setSelected(true);


        bgrResize = new ButtonGroup();
        JRadioButton sButton = new JRadioButton("Litet");
        sButton.setActionCommand("2");
        JRadioButton mButton = new JRadioButton("Mellan");
        mButton.setActionCommand("3");
        JRadioButton lButton = new JRadioButton("Stort");
        lButton.setActionCommand("5");
        mButton.setSelected(true);
        bgrResize.add(sButton);
        bgrResize.add(mButton);
        bgrResize.add(lButton);
        JLabel resizeInfo = new JLabel("Storlek på fönstret");
        resizePanel.add(resizeInfo);
        resizePanel.add(sButton);
        resizePanel.add(mButton);
        resizePanel.add(lButton);


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
        frame.add(groupSizePanel2);
        frame.add(groupSizePanel1);
        frame.add(uniqueTwoGroupPanel);
        frame.add(removePanel);
        frame.add(enemyPanel);
        frame.add(showNumberPanel);
        frame.add(resizePanel);
        frame.add(buttonPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void tryFinish() {
        System.out.println("Visa? " + numberCheckBox.isSelected());
        boolean pickUniqueGroups = false;
        int groupType = Integer.parseInt(bgr.getSelection().getActionCommand());
        int scale = Integer.parseInt(bgrResize.getSelection().getActionCommand());
        System.out.println("Typ: " + groupType);
        boolean success = true;

        removeInput.setBackground(Color.WHITE);
        enemyInput.setBackground(Color.WHITE);
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
            JOptionPane.showMessageDialog(frame, "Hittade inte alla namn som skulle bort");
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
                    JOptionPane.showMessageDialog(frame, "Det blir olämpligt antal grupper!");
                    sizeInput.setBackground(myRed);
                    return;
                }
            } else {
                sizeInput.setBackground(myRed);
                return;
            }
        } else if(groupType == 2) {
            Scanner scanner = new Scanner(groupCountInput.getText());
            if (scanner.hasNextInt()) {
                groupCount = scanner.nextInt();
                if (groupCount < 2 || groupCount > names.size()) {
                    JOptionPane.showMessageDialog(frame, "Det blir olämpligt antal grupper!");
                    groupCountInput.setBackground(myRed);
                    return;
                }
            } else {
                sizeInput.setBackground(myRed);
                return;
            }
        } else if (groupType == 3) {
            pickUniqueGroups = true;
            groupCount = names.size()/2;
        } else {
            System.out.println("SKA ALDRIG SKRIVAS, FEL PÅ RADION");
        }
        System.out.println("Vi börjar med ovännerna:");

        // Ny version
        boolean removeSuccess = true;
        int enemyCount = 0;
        StringBuilder notFound = new StringBuilder("Följande hittades ej: ");
        String[] enemies = enemyInput.getText().split(",");
        for (String enemyRaw : enemies) {
            String enemy = enemyRaw.trim();
            if(enemy.length() > 0) {
                if (names.remove(enemy)) {
                    names.addFirst(enemy);
                    enemyCount++;
                } else {
                    notFound.append(enemy).append(",");
                    removeSuccess = false;
                }
            }
        }
        if(!removeSuccess) {
            enemyInput.setBackground(myRed);
            JOptionPane.showMessageDialog(frame, notFound.toString());
            return;
        }


        /*
        boolean removeSuccess = true, pairSuccess = true;
        Scanner sc1 = new Scanner(enemyInput.getText());
        sc1.useDelimiter(",");
        StringBuilder notFound = new StringBuilder("Följande hittades ej: ");
        int enemyCount = 0;
        while(sc1.hasNext()) {
            Scanner sc2 = new Scanner(sc1.next());
            int counting = 0;
            while (sc2.hasNext()) {
                counting++;
                String name = sc2.next();
                if(names.remove(name)) {
                    names.addFirst(name);
                    enemyCount++;
                }
                else {
                    notFound.append(name).append(",");
                    removeSuccess = false;
                }
            }
            if(counting == 1) pairSuccess = false;

            System.out.println("Names nu: " + names);
        }

        if(!removeSuccess) {
            enemyInput.setBackground(myRed);
            JOptionPane.showMessageDialog(frame, notFound.toString());
            return;
        }
        if(!pairSuccess) {
            enemyInput.setBackground(myRed);
            JOptionPane.showMessageDialog(frame, "Det måste vara minst två personer.");
            return;
        }

         */

        System.out.println("Klart!");
        System.out.println("Antal grupper: " + groupCount);
        System.out.println("Antal elever: " + names.size());
        System.out.println("Listan: " + names);
        frame.setVisible(false);
        new GroupsFrame(names, groupCount, numberCheckBox.isSelected(), enemyCount, pickUniqueGroups, scale);
    }
    private void setAllNames() {
        StringBuilder sb = new StringBuilder("<html>");
        int count = 0;
        LinkedList<String> tempList = new LinkedList<>(names);
        Collections.sort(tempList);
        for (String s : tempList) {
            sb.append(s).append(",");
            count++;
            if(count % 15 == 0) sb.append("<br>");
        }
        sb.append("</html>");
        allNames.setText(sb.toString());
    }
}
