package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class GroupsFrame {

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

        int height = groupNames.get(0).size() * 20 + 70;

        Collections.shuffle(groupNames);
        for (int j=0; j<groups; j++) {
            frame.add(new StudentGroup(groupNames.get(j), height));
        }

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
