package view;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class ClassRoom {

    public ClassRoom (LinkedList<String> names, int rows, int columns) {

        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(rows, columns, 4, 4));

        for (String name : names) {
            JPanel p = new JPanel(new BorderLayout());
            JLabel gruopName = new JLabel("Gruppnummer");
            p.add(gruopName, BorderLayout.NORTH);
            p.add(new Bench(name), BorderLayout.CENTER);
            frame.add(p);
            // frame.add(new Bench(name));
        }

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
