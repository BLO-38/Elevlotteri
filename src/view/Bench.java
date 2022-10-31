package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Bench extends JPanel {
    private String benchName;
    private int xPos;
    private Bench thisBench;
    private final int center = 60;
    // private Color nameColor = Color.WHITE;
    private boolean exists = true;
    private JLabel nameLabel;

    public Bench(String name) {
        setLayout(new GridBagLayout());
        if(name.equals("-")) exists = false;
        benchName = name;
        setPreferredSize(new Dimension(140, 120));
        setBackground(new Color(224,215,196));
        if(exists) {
            // xPos = center - benchName.length() * 5;
            // if (xPos < 10) xPos = 10;
            thisBench = this;
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    Bench clickedBench = (Bench) e.getSource();
                    ClassRoom.benchClicked(clickedBench);
                    // ClassRoom.benchClicked(thisBench);
                }
            });

            nameLabel = new JLabel(benchName);
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setFont(new Font("arial narrow", Font.PLAIN,30));
            add(nameLabel);
        }
    }
    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        if(exists) {
            g.setColor(new Color(10, 20, 148));
            // g.fillRoundRect(20, 20, 100, 80, 25, 25);
            g.fillRoundRect(6, 6, 128, 108, 25, 25);
            // g.fillRoundRect(8, 8, 124, 104, 25, 25);
            // g.setColor(nameColor);
            // g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
            // g.drawString(studentName, xPos, 67);
            // Nytt
            nameLabel.setText(benchName);
        }

    }
    public void setName(String name) {
        benchName = name;
        // xPos = center - benchName.length()*5;
        // if (xPos < 10) xPos = 10;
    }

    public String getBenchName() {
        return benchName;
    }

    public void toggleRedName(boolean switchToRed){
        // if (setSpecial) nameColor = Color.RED;
        if (switchToRed) nameLabel.setForeground(Color.RED);
        // else nameColor= Color.WHITE;
        else nameLabel.setForeground(Color.WHITE);
        repaint();
    }
}
