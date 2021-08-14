package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class GroupsFrame {
    private final int groups, rows;
    private final LinkedList<String> names;
    private final boolean showGroupNumbers;
    private JPanel groupsPanel;
    private final JFrame frame;
    private final int COLUMNS = 5;
    public GroupsFrame(LinkedList<String> names, int groups, boolean showGroupNumbers, int noShuffleCount) {
        System.out.println("LITE NYTT NU MED BORDERS MM noshuff:" + noShuffleCount);
        this.groups = groups;
        this.names = names;
        this.showGroupNumbers = showGroupNumbers;

        frame = new JFrame();
        rows = (int) Math.ceil(groups * 1.0 / COLUMNS);
        frame.setLayout(new BorderLayout(0, 10));
        System.out.println("Rader: " + rows);
        System.out.println("Grupper: " + groups);
        System.out.println("Startlista i gruppframe: " + names);

        JButton button = new JButton("Nya grupper");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Collections.shuffle(names.subList(noShuffleCount, names.size()));
                System.out.println("Delvis shufflad lista: " + names);
                frame.remove(groupsPanel);
                setNewGroups();
                frame.revalidate();
            }
        });
        JPanel buttPanel = new JPanel(new FlowLayout());
        buttPanel.add(button);

        setNewGroups();

        frame.add(buttPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void setNewGroups() {
        System.out.println("Nya grupper!");
        groupsPanel = new JPanel(new GridLayout(rows, COLUMNS, 4, 4));
        ArrayList<LinkedList<String>> groupNames = new ArrayList<>();
        for (int i = 0; i<groups; i++) {
            groupNames.add(new LinkedList<>());
        }
        int count = 0;
        // Collections.shuffle(names);
        for (String name : names) {
            int group = count % groups;
            groupNames.get(group).add(name);
            count++;
        }

        // Det var det här med positioneran om nummer ska visas
        int height = groupNames.get(0).size() * 20 + 50;
        Collections.shuffle(groupNames);

        for (int j=0; j<groups; j++) {
            JPanel pWhole = new JPanel(new BorderLayout());
            if(showGroupNumbers) {
                JPanel pHeader = new JPanel(new FlowLayout());
                JLabel gruopNr = new JLabel("Grupp " + (j+1));
                gruopNr.setFont(new Font(Font.MONOSPACED, Font.BOLD,18));
                pHeader.add(gruopNr);
                pHeader.setBackground(new Color(224,215,196));
                pWhole.add(pHeader, BorderLayout.NORTH);
            }
            pWhole.add(new StudentGroup(groupNames.get(j), height, showGroupNumbers), BorderLayout.CENTER);
            groupsPanel.add(pWhole);
        }
        frame.add(groupsPanel,BorderLayout.CENTER);
    }
}
