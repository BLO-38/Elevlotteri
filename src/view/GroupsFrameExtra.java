package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class GroupsFrameExtra {
    private final int groups, rows;
    private final LinkedList<String> regularNames, friends, enemies;
    private LinkedList<String> names;
    private final boolean showGroupNumbers, makeUnique, gender;
    private JPanel groupsPanel;
    private final JFrame frame;
    private final int COLUMNS = 5;
    private final int scale, enemiesSize;
    // public GroupsFrame(LinkedList<String> names, int groups, boolean showGroupNumbers, int noShuffleCount, boolean uniqueGroups, int scale) {
    public GroupsFrameExtra(LinkedList<String> regularNames, LinkedList<String> enemies, LinkedList<String> friends, int groups, boolean showGroupNumbers, boolean uniqueGroups, int scale, boolean gender) {

        this.regularNames = regularNames;
        this.friends = friends;
        this.enemies = enemies;
        this.gender = gender;

        enemiesSize = enemies.size();
        System.out.println("Skala: " + scale);
        this.groups = groups;
        this.scale = scale;
        this.showGroupNumbers = showGroupNumbers;
        makeUnique = uniqueGroups;

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
                if(makeUnique) {
                    System.out.println("VI FIXAR UNIKA GRUPPER");
                    System.out.println("Nu ska vi göra nytt med förra listan som var:" + names);
                    // System.out.println("Alla efter popp: " + names);
                    boolean odd = false;
                    int halfSize = names.size() / 2;
                    if (names.size() % 2 != 0) {
                        System.out.println("UDDA ANTAL!");
                        halfSize++;
                        odd = true;
                    }
                    System.out.println("Halfsize: " + halfSize);
                    LinkedList<String> tail = new LinkedList<>();
                    for(int i=0; i<halfSize; i++) {
                        // String s = names.pollLast();
                        tail.push(names.pollLast());
                    }
                    System.out.println("Names efter att tagit bort hälften:" + names);
                    System.out.println("Tailen blev:" + tail);
                    tail.push(tail.pollLast());
                    if (odd) {
                        tail.push(tail.pollLast());
                        tail.push(tail.pollLast());
                        names.push(names.pollLast());
                    }
                    System.out.println("Tail efter ploopush:" + tail);
                    System.out.println("Första halvan efter ploopush:" + names);
                    names.addAll(tail);

                } else {
                    System.out.println("OBS VI SLUMPAR VANLIGT!");
                    // Collections.shuffle(names.subList(enemiesSize, names.size()));
                    setNameList();

                }
                System.out.println("Färdigt att skickas: " + names);
                frame.remove(groupsPanel);
                setNewGroups();
                frame.revalidate();
            }
        });

        setNameList();
        setNewGroups();

        JPanel buttPanel = new JPanel(new FlowLayout());
        buttPanel.add(button);
        frame.add(buttPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JOptionPane.showMessageDialog(null, "Skapa grupper");
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    // shaffla i knappmetod
    // Fyll med tomma först, ta bort svansen sist
    private void setNameList() {

        Collections.shuffle(regularNames);
        if(gender) Collections.shuffle(friends);


        names = new LinkedList<>(enemies);
        names.addAll(new LinkedList<>(regularNames));

        // Placerar in kompisar så de kommer tillsammans
        // tex på platserna 4,5,6 och 14,15,16 om man har 10 grupper och 4 ovänner och 6 kompisar (3 par)
        int position = 0, lap = 1;
        boolean tooManyGroups = groups > friends.size()/2;
        System.out.println("Too many " + tooManyGroups);
        for (int i=0; i< friends.size(); i++) {
            names.add(position,friends.get(i));
            System.out.println("Lägger till första kompisen på köplats: " + position);
            position++;
            if(tooManyGroups && i % (friends.size()/2) == friends.size()/2-1) position = groups*lap++;
        }

//        for (int i=0; i<2; i++) {
//            int index = i * groups + enemies.size(), count = 0;
//            for (String friend : friends) {
//                if (i == 0 && count % 2 == 0) {
//                    System.out.println("Lägger till första kompisen på köplats: " + (index + 1));
//                    names.add(index, friend);
//                    index++;
//                } else if (i == 1 && count % 2 == 1) {
//                    System.out.println("Lägger till ANDRA kompisen på köplats: " + (index + 1));
//                    names.add(index, friend);
//                    index++;
//                }
//                count++;
//            }
//        }
        System.out.println("Efter setlist: " + names);
    }

    private void setNewGroups() {
        System.out.println("Nya grupper!");
        groupsPanel = new JPanel(new GridLayout(rows, COLUMNS, 4, 4));
        ArrayList<LinkedList<String>> nameGroups = new ArrayList<>();
        for (int i = 0; i<groups; i++) {
            nameGroups.add(new LinkedList<>());
        }
        int count = 0;
        // Collections.shuffle(names);
        for (String name : names) {
            int group = count % groups;
            nameGroups.get(group).add(name);
            count++;
        }
// HIT
        // Det var det här med positioneran om nummer ska visas
        // int height = groupNames.get(0).size() * 20 + 50;
        int height = nameGroups.get(0).size() * 10 + 25;
        height = height*scale;
        Collections.shuffle(nameGroups);

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
            pWhole.add(new StudentGroup(nameGroups.get(j), height, showGroupNumbers, scale), BorderLayout.CENTER);
            groupsPanel.add(pWhole);
        }
        frame.add(groupsPanel,BorderLayout.CENTER);
    }
}
