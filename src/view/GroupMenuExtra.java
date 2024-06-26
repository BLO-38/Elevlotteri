package view;

import databasen.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class GroupMenuExtra {
    private final JTextField sizeInput, minSizeInput, groupCountInput, removeInput, enemyInput, friendInput;
    private final JLabel allNames;
    private final JFrame frame;
    private final JCheckBox numberCheckBox, genderCheckBox;
    private LinkedList<String> names;
    private final Color myRed = new Color(247, 212, 212);
    private final ButtonGroup bgr;
    private final ButtonGroup bgrResize;
    private LinkedList<String> girls,boys;
    private LinkedList<Student> students;

    public GroupMenuExtra(LinkedList<Student> st) {
        students = st;
        System.out.println("Antal stud: " + students.size());
        /// this.names = names;
        names = new LinkedList<>();
        for(Student s : students)
            names.add(s.getName());
        frame = new JFrame();
        frame.setLayout(new GridLayout(13, 1));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel namesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel groupSizePanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel groupSizePanel1b = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel groupSizePanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel removePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel enemyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel friendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel showNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel uniqueTwoGroupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel resizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JPanel bgrPanel = new JPanel();
        BoxLayout b = new BoxLayout(bgrPanel, BoxLayout.Y_AXIS);
        bgrPanel.setLayout(b);

        headerPanel.setPreferredSize(new Dimension(300, 50));
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
        JRadioButton minSizeButton = new JRadioButton("Minst antal elever per grupp");
        minSizeButton.setActionCommand("2");
        JRadioButton countButton = new JRadioButton("Antal grupper");
        countButton.setActionCommand("3");
        JRadioButton uniqueTwoButton = new JRadioButton("Unika två-grupper");
        uniqueTwoButton.setActionCommand("4");
        countButton.setSelected(true);
        bgr.add(sizeButton);
        bgr.add(minSizeButton);
        bgr.add(countButton);
        bgr.add(uniqueTwoButton);

        groupSizePanel1.add(sizeButton);
        // bgrPanel.add(sizeButton);
        sizeInput = new JTextField(5);
        sizeInput.setText("2");
        groupSizePanel1.add(sizeInput);
        sizeInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                sizeButton.setSelected(true);
            }
        });

        groupSizePanel1b.add(minSizeButton);
        minSizeInput = new JTextField(5);
        minSizeInput.setText("4");
        groupSizePanel1b.add(minSizeInput);
        minSizeInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                sizeButton.setSelected(true);
            }
        });

        groupSizePanel2.add(countButton);
        groupCountInput = new JTextField(5);
        groupCountInput.setText("3");
        groupSizePanel2.add(groupCountInput);
        groupCountInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                countButton.setSelected(true);
            }
        });

        uniqueTwoButton.setToolTipText("Ett tips från Lars");
        System.out.println(ToolTipManager.sharedInstance().getInitialDelay());
        ToolTipManager.sharedInstance().setInitialDelay(0);
        ToolTipManager.sharedInstance().setDismissDelay(1000);
        uniqueTwoGroupPanel.add(uniqueTwoButton);

        removePanel.add(new JLabel("Vilka elever ska bort?"));
        removeInput = new JTextField(50);
        removePanel.add(removeInput);

        enemyPanel.add(new JLabel("Vilka ska ej vara i samma grupp?"));
        enemyInput = new JTextField(40);
        enemyPanel.add(enemyInput);

        friendPanel.add(new JLabel("Vilka ska vara i samma grupp?"));
        friendInput = new JTextField(40);
        friendPanel.add(friendInput);

        numberCheckBox = new JCheckBox("Visa gruppnummer");
        showNumberPanel.add(numberCheckBox );
        numberCheckBox.setSelected(true);

        genderCheckBox = new JCheckBox("Bra fördelning pojkar/flickor (minst 4 per grupp)");
        genderPanel.add(genderCheckBox);


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
                tryFinish();
            }
        });

        frame.add(headerPanel);
        frame.add(namesPanel);
        frame.add(groupSizePanel2);
        frame.add(groupSizePanel1);
        frame.add(groupSizePanel1b);
        frame.add(uniqueTwoGroupPanel);
        frame.add(removePanel);
        frame.add(enemyPanel);
        frame.add(friendPanel);
        frame.add(showNumberPanel);
        frame.add(genderPanel);
        frame.add(resizePanel);
        frame.add(buttonPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void tryFinish() {
        System.out.println("############ TRY FINISH BÖRJAR ##############");
        System.out.println("Visa? " + numberCheckBox.isSelected());
        System.out.println("Namn vid start: " + names);
        boolean pickUniqueGroups = false;
        int groupingMethod = Integer.parseInt(bgr.getSelection().getActionCommand());
        int scale = Integer.parseInt(bgrResize.getSelection().getActionCommand());
        System.out.println("Metod för gruppindelning: " + groupingMethod);
        boolean success = true;

        removeInput.setBackground(Color.WHITE);
        enemyInput.setBackground(Color.WHITE);
        sizeInput.setBackground(Color.WHITE);
        groupCountInput.setBackground(Color.WHITE);
        friendInput.setBackground(Color.WHITE);

        String [] namesToRemove = removeInput.getText().split(",");
        StringBuilder failNames = new StringBuilder();
        for (String n1 : namesToRemove) {
            String n2 = n1.trim();
            if(n2.length() != 0 && !names.remove(n2)) {
                failNames.append(n2).append(",");
                success = false;
            }
        }
        setAllNames(); // För info-utskrift
        removeInput.setText(failNames.toString());
        if (!success) {
            removeInput.setBackground(myRed);
            JOptionPane.showMessageDialog(frame, "Hittade inte alla namn som skulle bort");
            return;
        }

        int groupCount = 1;
        if (groupingMethod == 1) {
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
        } else if(groupingMethod == 2) {
            System.out.println("MINSIZE");
            Scanner scanner = new Scanner(minSizeInput.getText());
            if (scanner.hasNextInt()) {
                int groupSize = scanner.nextInt();
                groupCount = names.size() / groupSize;
                if (groupCount < 2 || groupCount > names.size()) {
                    JOptionPane.showMessageDialog(frame, "Det blir olämpligt antal grupper!");
                    sizeInput.setBackground(myRed);
                    return;
                }
            } else {
                sizeInput.setBackground(myRed);
                return;
            }
        } else if(groupingMethod == 3) {
            Scanner scanner = new Scanner(groupCountInput.getText());
            if (scanner.hasNextInt()) {
                groupCount = scanner.nextInt();
                if (groupCount < 2 || groupCount > names.size()) {
                    JOptionPane.showMessageDialog(frame, "Det blir olämpligt antal grupper!");
                    groupCountInput.setBackground(myRed);
                    return;
                }
            } else {
                sizeInput.setBackground(myRed); // Denna verkar konstig
                return;
            }
        } else if (groupingMethod == 4) {
            pickUniqueGroups = true;
            groupCount = names.size()/2;
        } else {
            System.out.println("SKA ALDRIG SKRIVAS, FEL PÅ RADION");
        }

        System.out.println("Vi börjar med ovännerna:");
        boolean enemySuccess = true;
        int enemyCount = 0;
        StringBuilder notFound = new StringBuilder("Följande hittades ej: ");
        LinkedList<String> enemiesList = new LinkedList<>();
        String[] enemies = enemyInput.getText().split(",");
        for (String enemyRaw : enemies) {
            String enemy = enemyRaw.trim();
            if(enemy.length() == 0) continue;
            if(names.remove(enemy)) {
                enemiesList.add(enemy);
            } else {
                enemySuccess = false;
                enemyInput.setBackground(myRed);
                notFound.append(enemy).append(",");
            }
//            String enemy = enemyRaw.trim();
//            if(enemy.length() > 0) {
//                if (names.remove(enemy)) {
//                    names.addFirst(enemy);
//                    enemyCount++;
//                } else {
//                    notFound.append(enemy).append(",");
//                    removeSuccess = false;
//                }
//            }
        }
        if(!enemySuccess) {
            enemyInput.setBackground(myRed);
            JOptionPane.showMessageDialog(frame, notFound.toString());
            return;
        }
        // Vännerna:
        LinkedList<String> friends = new LinkedList<>();

        // NYTT:
        if (genderCheckBox.isSelected()) {
            if (groupCount < 4) {
                JOptionPane.showMessageDialog(null, "Kön och gruppstorlek funkar inte");
                return;
            } else {
                girls = new LinkedList<>();
                boys = new LinkedList<>();
                for(String s : namesToRemove) {
                    int plats = -1;
                    for (int i = 0; i < students.size(); i++) {
                        if(students.get(i).getName().equals(s)) plats = i;
                    }
                    if(plats != -1) students.remove(plats);
                }
                for (Student student : students) {
                    if (student.getGender().equals("k")) girls.add(student.getName());
                    else boys.add(student.getName());
                }
                System.out.println("Tjejer: " + girls);
                System.out.println("killar " + boys);

                new GroupsFrameExtra(boys, new LinkedList<>(), girls, groupCount, numberCheckBox.isSelected(), false, scale,genderCheckBox.isSelected());
            }
        } else {

            String[] friendsFromInput = friendInput.getText().split(",");
            StringBuilder badNames = new StringBuilder("Hittade inte ");
            boolean friendsSuccess = true;
            for (String sRaw : friendsFromInput) {
                String friend = sRaw.trim();
                if (friend.length() == 0) continue;
                if (names.remove(friend)) {
                    friends.add(friend);
                } else {
                    friendsSuccess = false;
                    friendInput.setBackground(myRed);
                    badNames.append(friend).append(",");
                }
            }
            if (!friendsSuccess) {
                JOptionPane.showMessageDialog(frame, badNames.toString());
                return;
            }
            int firstFriendIndex = (int) (Math.ceil(1.0 * enemyCount / groupCount)) * groupCount;
            System.out.println("Första friend index: " + firstFriendIndex);


            System.out.println("Klart!");
            System.out.println("Antal grupper: " + groupCount);
            System.out.println("Antal elever: " + names.size());
            System.out.println("Listan: " + names);

//        frame.setVisible(false);
//        new GroupsFrame(names, groupCount, numberCheckBox.isSelected(), enemyCount, pickUniqueGroups, scale);
            new GroupsFrameExtra(names, enemiesList, friends, groupCount, numberCheckBox.isSelected(), pickUniqueGroups, scale,genderCheckBox.isSelected());
        }
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
