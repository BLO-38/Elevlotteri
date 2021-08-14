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
    private LinkedList<String> names;
    private final int rows, columns;

    public ClassRoom (LinkedList<String> names, int rows, int columns) {
        this.names = names;
        this.rows = rows;
        this.columns = columns;

        frame = new JFrame();
        frame.setLayout(new BorderLayout(0, 10));
        // frame.setLayout(new GridLayout(rows, columns, 4, 4));

        JButton button = new JButton("Ny placering");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Collections.shuffle(names.subList(noShuffleCount, names.size()));
                frame.remove(benchesPanel);
                Collections.shuffle(names);
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
        for (String name : names) {
            JPanel p = new JPanel(new FlowLayout());
            p.add(new Bench(name));
            benchesPanel.add(p);
        }
        frame.add(benchesPanel, BorderLayout.CENTER);
    }
}
