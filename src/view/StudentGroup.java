package view;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.Charset;
import java.util.LinkedList;

public class StudentGroup extends JPanel {
    private final int height;
    private final LinkedList<String> names;
    public StudentGroup(LinkedList<String> names, int height) {
        this.height = height;
        this.names = names;
        // studentName = name;
        setPreferredSize(new Dimension(200, height));
        setBackground(new Color(224,215,196));
    }
    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(10, 20, 148));
        g.fillRoundRect(20, 20, 160, (height - 40), 25, 25);
        g.setColor(Color.WHITE);
        // Font f = new Font(Font.MONOSPACED, Font.BOLD, 20);
        // Charset.availableCharsets()
        g.setFont(new Font("Arial", Font.BOLD, 16));

        int yPos = 45;
        for(String name : names) {
            // private JLabel studentName;
            // private final String studentName;
            int xPos = 30;
            g.drawString(name, xPos, yPos);
            yPos+=20;
        }
    }
}
