package view;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class ClassRoom {

    public ClassRoom (LinkedList<String> names, int rows, int columns) {

        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(rows, columns, 4, 4));

        for (String name : names) {
            frame.add(new Bench(name));
        }

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
