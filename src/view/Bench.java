package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Bench extends JPanel {
    private String studentName;
    private int xPos;
    private final Bench thisBench;
    private final int center = 60;
    private Color nameColor = Color.WHITE;
    public Bench(String name) {
        FlowLayout flow = (FlowLayout) this.getLayout(); // setLayout(new GridLayout());
        flow.setHgap(0);
        flow.setVgap(0);
        studentName = name;
        setPreferredSize(new Dimension(140, 120));
        setBackground(new Color(224,215,196));
        xPos = center - studentName.length()*5;
        if (xPos < 10) xPos = 10;
        thisBench = this;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ClassRoom.benchClicked(thisBench);
            }
        });
    }
    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(10, 20, 148));
        // g.fillRoundRect(20, 20, 100, 80, 25, 25);
        g.fillRoundRect(10, 10, 120, 100, 25, 25);
        g.setColor(nameColor);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        g.drawString(studentName, xPos, 67);

    }
    public void setName(String name) {
        studentName = name;
        xPos = center - studentName.length()*5;
        if (xPos < 10) xPos = 10;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setSpecialColor (boolean setSpecial){
        if (setSpecial) nameColor = Color.RED;
        else nameColor= Color.WHITE;
        repaint();
    }
}
