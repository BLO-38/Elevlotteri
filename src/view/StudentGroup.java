package view;

import javax.swing.*;
import java.awt.*;

public class StudentGroup extends JPanel {
    // private JLabel studentName;
    private final String studentName;
    private final int xPos = 30;
    public StudentGroup(String name) {
        studentName = name;
        setPreferredSize(new Dimension(200, 200));
        setBackground(new Color(224,215,196));
    }
    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(10, 20, 148));
        g.fillRoundRect(20, 20, 100, 80, 25, 25);
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        g.drawString(studentName, xPos, 67);
    }

    public void addName(String nextName) {
        System.out.println("Addname");
    }

}
