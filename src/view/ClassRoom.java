package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;

public class ClassRoom {

    private static JFrame frame;
    private JPanel benchesPanel;
    private final LinkedList<String> regularNames, enemies, friends, firstRow;
    private final LinkedList<Integer> emptyBenches;
    private final int rows, columns, startPos;
    private static Bench previousBench;

    public ClassRoom (LinkedList<String> regularNames, LinkedList<String> enemyNames, LinkedList<String> friendNames, LinkedList<Integer> emptyBenches, LinkedList<String> firstRowNames, int rows, int columns, int startPosition) {
        this.regularNames = regularNames;
        this.rows = rows;
        this.columns = columns;
        enemies = enemyNames;
        friends = friendNames;
        this.emptyBenches = emptyBenches;
        firstRow = firstRowNames;
        startPos = startPosition;

        System.out.println("Till Classroom:");
        System.out.println("Vanliga namn: " + regularNames);
        System.out.println("Ovänner: " + enemies);
        System.out.println("Tomma bänkar: " + emptyBenches);
        System.out.println("Vänner: " + friends);
        System.out.println("Första raden: " + firstRow);

        frame = new JFrame();
        frame.setLayout(new BorderLayout(0, 10));
//        frame.getContentPane().setBackground(Color.BLACK);

        JButton button = new JButton("Ny placering");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(benchesPanel);
                setNewBenches();
                frame.revalidate();
            }
        });
        JPanel buttPanel = new JPanel(new FlowLayout());
        buttPanel.add(button);
        setNewBenches();
        frame.add(buttPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setVisible(true);
    }
    private void setNewBenches() {
        benchesPanel = new JPanel(new GridLayout(rows, columns));
        LinkedList<String> tempNames = new LinkedList<>(regularNames);
        Collections.shuffle(tempNames);

        int firstRowEnemis = 0;
        // int firstRowFriends = 0;
        for (String firstRowStudent : firstRow) {
            if(enemies.remove(firstRowStudent)) {
                enemies.addFirst(firstRowStudent);
                firstRowEnemis++;
            }
        }
        if (enemies.size() != 0) {
            int spacing = (tempNames.size() - friends.size()) / enemies.size();
            System.out.println("Spacing blev " + spacing);
            int insertIndex = 1;
            System.out.println("Efter Vi kör nu med insertindex = " + insertIndex);
            if (spacing < 1) {
                JOptionPane.showMessageDialog(frame, "För många som ska separeras. Om detta problem dyker upp ofta, kontakta Lars.");
                return;
            }
            if (firstRowEnemis == 0) {
                Collections.shuffle(enemies);
                insertIndex = (int) (Math.random() * spacing);
            }
            for (String enemy : enemies) {
                tempNames.add(insertIndex, enemy);
                System.out.println("Insertade " + enemy + " på plats " + insertIndex);
                if (firstRowEnemis > 0) {
                    insertIndex += 2;
                    firstRowEnemis--;
                    System.out.println("Fanns firstrowenemy kvar så vi insertade på  " + insertIndex + ". Antal kvar nu: " + firstRowEnemis);
                } else {
                    System.out.println("Insertade med bräknad spacing");
                    insertIndex += spacing+1;
                }
            }
        }

        // Nu kompisar. Får inte vara på
        LinkedList<String> tempFriends = new LinkedList<>(friends);
        while (tempFriends.size() > 1) {
            String f1 = tempFriends.pollFirst();
            String f2 = tempFriends.pollFirst();
            if (firstRow.contains(f1) || firstRow.contains(f2)){
                tempNames.addFirst(f1);
                tempNames.addFirst(f2);
                System.out.println("Insetade två vänner i början");
            } else {
                int ind = (int) Math.abs(Math.random()*tempNames.size() - 2.0);
                tempNames.add(ind, f1);
                tempNames.add(ind+1, f2);
                System.out.println("Insertade två vänner på " + ind + " och " + (ind+1));

            }
        }

        Collections.reverse(firstRow);
        for (String firstRowName : firstRow) {
            if(tempNames.remove(firstRowName)) tempNames.add(startPos, firstRowName);
        }
        // Collections.sort(emptyBenches);
        for (int ind : emptyBenches) {
            if(ind > 0 && ind <= tempNames.size()) tempNames.add((ind - 1), "");
        }
        while (tempNames.size() < (rows*columns)) {
            tempNames.add("");
        }

        for (String name : tempNames) {
            JPanel p = new JPanel(new FlowLayout());
            p.add(new Bench(name));
            benchesPanel.add(p);
        }

        frame.add(benchesPanel, BorderLayout.CENTER);
    }
    public static void ritaom() {
        frame.revalidate();
        frame.repaint();
    }
    public static void benchClicked(Bench bench) {
        if (previousBench == null) {
            previousBench = bench;
            previousBench.setSpecialColor(true);
        } else {
            String clickedName = bench.getStudentName();
            bench.setName(previousBench.getStudentName());
            previousBench.setName(clickedName);
            previousBench.setSpecialColor(false);
            bench.repaint();
            previousBench = null;
        }
    }
}
