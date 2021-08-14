package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class GroupsFrame {

    public GroupsFrame(LinkedList<String> names, int groups, boolean showGropNumbers) {
        final int COLUMNS = 5;
        JFrame frame = new JFrame();
        int rows = (int) Math.ceil(groups * 1.0 / COLUMNS);
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

        // Det var det här med positioneran om nummer ska visas
        int height = groupNames.get(0).size() * 20 + 50;
        Collections.shuffle(groupNames);
        // int height = groupNames.get(0).size() * 20 + 50 + yOffset;
        for (int j=0; j<groups; j++) {
            JPanel pWhole = new JPanel(new BorderLayout());
            if(showGropNumbers) {
                JPanel pHeader = new JPanel(new FlowLayout());
                JLabel gruopNr = new JLabel("Grupp " + (j+1));
                gruopNr.setFont(new Font(Font.MONOSPACED, Font.BOLD,18));
                pHeader.add(gruopNr);
                pHeader.setBackground(new Color(224,215,196));
                pWhole.add(pHeader, BorderLayout.NORTH);
            }
            pWhole.add(new StudentGroup(groupNames.get(j), height, showGropNumbers), BorderLayout.CENTER);
            frame.add(pWhole);
            //frame.add(new StudentGroup(groupNames.get(j), height));
        }

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
