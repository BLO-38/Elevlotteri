package view;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedList;

public class StudentGroup extends JPanel {
    private final int height, yOffset;
    private final LinkedList<String> names;
    public StudentGroup(LinkedList<String> names, int height, boolean showGroupNumbers) {
        this.names = names;
        Collections.shuffle(this.names);
        this.height = height;


        // setPreferredSize(new Dimension(200, height));


        // int panelHeight = showGroupNumbers ? (height - 20) : height;
        yOffset = showGroupNumbers ? 0 : 20;
        setPreferredSize(new Dimension(200, (height+yOffset)));

        setBackground(new Color(224,215,196));
    }
    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(10, 20, 148));
        g.fillRoundRect(20, yOffset, 160, (height - 20), 25, 25); // Första var 20
        g.setColor(Color.WHITE);
        // Font f = new Font(Font.MONOSPACED, Font.BOLD, 20);
        g.setFont(new Font("Arial", Font.BOLD, 16));

        int yPos = 28 + yOffset;
        for(String name : names) {
            int xPos = 30;
            g.drawString(name, xPos, yPos);
            yPos+=20;
        }
    }
}
