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
    private final LinkedList<String> regularNames, friends, firstRowNames;
    private LinkedList<String> enemies;
    private final LinkedList<Integer> emptyBenches;
    private final int rows, columns, startPos;
    private static Bench previousBench;
    private final boolean enemiesOnFirstRow;

    public ClassRoom (LinkedList<String> regularNames,
                      LinkedList<String> enemyNames,
                      LinkedList<String> friendNames,
                      LinkedList<Integer> emptyBenches,
                      LinkedList<String> firstRowNames,
                      int rows, int columns, int startPosition,
                      boolean enemiesOnFirstRow) {
        this.regularNames = regularNames;
        this.rows = rows;
        this.columns = columns;
        enemies = enemyNames;
        friends = friendNames;
        this.emptyBenches = emptyBenches;
        this.firstRowNames = firstRowNames;
        startPos = startPosition;
        this.enemiesOnFirstRow = enemiesOnFirstRow;

        System.out.println("Till Classroom:");
        System.out.println("Vanliga namn: " + regularNames);
        System.out.println("Ovänner: " + enemies);
        System.out.println("Tomma bänkar: " + emptyBenches);
        System.out.println("Vänner: " + friends);
        System.out.println("Första raden: " + this.firstRowNames);

        frame = new JFrame();
        frame.setLayout(new BorderLayout(0, 10));

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

    /**
     * Första raden-elever finns även bland regular, enemies eller friends
     *
     * 1. Blanda enemies och regular
     * 2. Börja med regular
     * 3. Hämta fiender från första raden och sätt först i enemies, first row har nu inga enemies
     * 4. Placera första rad-elever på rad
     * 5. Placera enemies
     * 6. Placera kompisar, om nån av dem är med i firstrow, inserta bredvid
     * 7. Om kompisindex medelvärde = radlängd: gör om
     * BUGG KVAR: Om två vänner båda står på rad 1
     */
    private void setNewBenches() {
        benchesPanel = new JPanel(new GridLayout(rows, columns));
        LinkedList<String> benchNames = new LinkedList<>(regularNames);
        LinkedList<String> tempFirstRow = new LinkedList<>(firstRowNames);
        Collections.shuffle(benchNames);
        Collections.shuffle(enemies);

        if (enemies.size() != 0) {
            LinkedList<String> sortedEnemies = new LinkedList<>(enemies);
            for (String n : enemies) {
                if (tempFirstRow.remove(n)) {
                    boolean result = sortedEnemies.remove(n);
                    if (!result) System.err.println("NEJ FEL PÅ ENEMIES SORTING ======================");
                    else System.err.println("JA ENEMIES SORTING Funkar");
                    sortedEnemies.addFirst(n);
                }
            }
            enemies = sortedEnemies;
        }

        Collections.reverse(tempFirstRow);
        for (String firstRowName : tempFirstRow) {
            if(benchNames.remove(firstRowName)) benchNames.add(startPos, firstRowName);
            if(friends.contains(firstRowName)) benchNames.add(startPos, firstRowName);
        }

        int insertIndex = 5;
        if (enemies.size() != 0) {
            int spacing = enemiesOnFirstRow ? 1 : (benchNames.size() - friends.size()) / enemies.size();
            System.out.println("Spacing blev " + spacing);
            insertIndex = enemiesOnFirstRow ? 0 : (int) (Math.random() * spacing);
            System.out.println("Efter Vi kör nu med insertindex = " + insertIndex);
            if (spacing < 1) {
                JOptionPane.showMessageDialog(frame, "För många som ska separeras. Om detta problem dyker upp ofta, kontakta Lars.");
                return;
            }
            for (String enemy : enemies) {
                benchNames.add(insertIndex, enemy);
                System.out.println("Insertade " + enemy + " på plats " + insertIndex);
                insertIndex += spacing+1;
            }
        }

        if (!enemiesOnFirstRow) insertIndex = firstRowNames.size() + 2 + startPos;
        if (insertIndex < firstRowNames.size() + startPos) insertIndex = firstRowNames.size() + 2 + startPos;
        LinkedList<String> tempFriends = new LinkedList<>(friends);
        while (tempFriends.size() > 1) {
            String f1 = tempFriends.pollFirst();
            String f2 = tempFriends.pollFirst();

            int indexOfFirstFriend = benchNames.indexOf(f1);
            int indexOf2ndFriend = benchNames.indexOf(f2);

            if (indexOfFirstFriend == -1 && indexOf2ndFriend == -1) {
                if (insertIndex%columns + 1 == 0) insertIndex++;
                benchNames.add(insertIndex, f1);
                benchNames.add(insertIndex, f2);
                System.out.println("Insertade två vänner på " + insertIndex);
            } else {
                if (indexOfFirstFriend != -1) {
                    if (indexOf2ndFriend == -1) {
                        benchNames.add(indexOfFirstFriend, f2);
                        insertIndex++;
                    } else {
                        System.out.println("Båda fanns, fixa på nåt sätt");
                    }
                }
                if (indexOf2ndFriend != -1 && indexOfFirstFriend == -1) {
                    benchNames.add(indexOf2ndFriend, f1);
                    insertIndex++;
                }
            }
        }

        // Collections.sort(emptyBenches);
        for (int ind : emptyBenches) {
            if(ind > 0 && ind <= benchNames.size()) benchNames.add((ind - 1), "");
        }
        while (benchNames.size() < (rows*columns)) {
            benchNames.add("");
        }

        for (String name : benchNames) {
            JPanel p = new JPanel(new FlowLayout());
            p.add(new Bench(name));
            benchesPanel.add(p);
        }
        frame.add(benchesPanel, BorderLayout.CENTER);
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
