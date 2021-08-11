package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class GroupsFrame {
    // private Bench bench;

    public GroupsFrame(LinkedList<String> names, int groups) {
        final int COLUMNS = 5;
        JFrame frame = new JFrame();
        double grp = groups;
        int rows = (int) Math.ceil(grp / COLUMNS);
        frame.setLayout(new GridLayout(rows, COLUMNS, 4, 4));
        System.out.println("Rader: " + rows);
        System.out.println("Grupper: " + groups);
        ArrayList<LinkedList<String>> groupNames = new ArrayList<>();
        for (int i = 0; i<groups; i++) {
            groupNames.add(new LinkedList<>());
        }
        int count = 0;
        // int group = 0;
        for (String name : names) {
            int group = count % groups;
            groupNames.get(group).add(name);
            count++;
        }
        for(String s : groupNames.get(0)) {
            System.out.print(s + " ");
        }
        System.out.println();
        for(String s : groupNames.get(1)) {
            System.out.print(s + " ");
        }

//        for (String name : names) {
//            frame.add(new Bench(name));
//        }
//      Link
//        LinkedList<String> l = new LinkedList<>();
//        l.add("Lars");
//        l.add("Stina");
//        l.add("Olle");
        int height = groupNames.get(0).size() * 20 + 70;
        // Shuffla arraylistan
        // Shuffla sen listan i konstruktorn i studentgroup.
        for (int j=0; j<groups; j++) {
            frame.add(new StudentGroup(groupNames.get(j), height));
        }
        // StudentGroup sg = new StudentGroup(groupNames.get(2), height);
        // frame.add(sg);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // sg.addName("Erika");

        // if (overFlow) JOptionPane.showMessageDialog(frame, "Ooops, du tog bort för många bänkar. Alla fick inte plats!");
    }
}
