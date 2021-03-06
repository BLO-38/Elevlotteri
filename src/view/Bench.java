package view;

import javax.swing.*;
import java.awt.*;

public class Bench extends JPanel {
    private final String studentName;
    private int xPos;
    public Bench(String name) {
        FlowLayout flow = (FlowLayout) this.getLayout(); // setLayout(new GridLayout());
        flow.setHgap(0);
        flow.setVgap(0);
        studentName = name;
        setPreferredSize(new Dimension(140, 120));
        setBackground(new Color(224,215,196));
        int center = 60;
        xPos = center - studentName.length()*5;
        if (xPos < 20) xPos = 20;
    }
    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(10, 20, 148));
        // g.fillRoundRect(20, 20, 100, 80, 25, 25);
        g.fillRoundRect(10, 10, 120, 100, 25, 25);
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        g.drawString(studentName, xPos, 67);

    }

}
