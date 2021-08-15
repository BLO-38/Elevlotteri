package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;

public class ClassRoom {

    private final JFrame frame;
    private JPanel benchesPanel;
    private final LinkedList<String> regularNames, enemyNames;
    private final LinkedList<Integer> emptyBenches;
    private final int rows, columns;

    public ClassRoom (LinkedList<String> regularNames, LinkedList<String> enemyNames, LinkedList<Integer> emptyBenches, int rows, int columns) {
        this.regularNames = regularNames;
        this.rows = rows;
        this.columns = columns;
        this.enemyNames = enemyNames;
        this.emptyBenches = emptyBenches;
        System.out.println("Till Classroom:");
        System.out.println("Vanliga namn: " + regularNames);
        System.out.println("Ovänner: " + enemyNames);
        System.out.println("Tomma bänkar: " + emptyBenches);
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    private void setNewBenches() {
        benchesPanel = new JPanel(new GridLayout(rows, columns, 4, 4));
        LinkedList<String> tempNames = new LinkedList<>(regularNames);
        Collections.shuffle(tempNames);

        if (enemyNames.size() != 0) {
            int spacing = tempNames.size() / enemyNames.size();
            System.out.println("Spacing blev " + spacing);
            int insertIndex = (int) (Math.random() * spacing);
            System.out.println("Efter Vi kör nu med insertindex = " + insertIndex);
            if (spacing < 1) {
                JOptionPane.showMessageDialog(frame, "För många som ska separeras. Om detta problem dyker upp ofta, kontakta Lars.");
                return;
            }
            Collections.shuffle(enemyNames);
            for (String enemy : enemyNames) {
                tempNames.add(insertIndex, enemy);
                System.out.println("Insertade på plats " + insertIndex);
                insertIndex += spacing+1;
            }
        }
        Collections.sort(emptyBenches);
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
}
