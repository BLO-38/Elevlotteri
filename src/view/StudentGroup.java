package view;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;

public class StudentGroup extends JPanel {
    private final int height, yOffset, scale;
    private final LinkedList<String> names;
    public StudentGroup(LinkedList<String> names, int height, boolean showGroupNumbers, int scale) {
        this.names = names;
        Collections.shuffle(this.names);
        this.height = height;
        this.scale = scale;

        // yOffset = showGroupNumbers ? 0 : 20;
        yOffset = showGroupNumbers ? 0 : 10*scale;

        // setPreferredSize(new Dimension(200, (height+yOffset)));
        setPreferredSize(new Dimension(80*scale, (height+yOffset)));

        setBackground(new Color(224,215,196));
    }
    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(10, 20, 148));

        // g.fillRoundRect(20, yOffset, 160, (height - 20), 25, 25);
        g.fillRoundRect(10*scale, yOffset, 60*scale, (height - 10*scale), 30, 30);

        g.setColor(Color.WHITE);

        // g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setFont(new Font("Arial", Font.BOLD, 8*scale));

        // int yPos = 28 + yOffset;
        int yPos = 14*scale + yOffset;

        for(String name : names) {
            // int xPos = 30;
            int xPos = 15*scale;

            g.drawString(name, xPos, yPos);
            // yPos+=20;
            yPos+=10*scale;

        }
    }
}
