package view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Bench extends JPanel {
    private String benchName;
    private final ClassRoom2 classRoom;
    private int xPos;
    private final int center = 60;
    // private Color nameColor = Color.WHITE;
    private boolean exists = true;
    private JLabel nameLabel;
    private static int width = ClassRoom2.benchWidth;
    private static int height = ClassRoom2.benchHeight;

    public Bench(String name, ClassRoom2 cl) {
        classRoom = cl;
        setLayout(new GridBagLayout());
        if(name.equals("-")) exists = false;
        benchName = name;
        setPreferredSize(new Dimension(width, height));
        setBackground(new Color(224,215,196));
        Bench thisBench = this;
        if(exists) {
            // xPos = center - benchName.length() * 5;
            // if (xPos < 10) xPos = 10;

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        benchName = JOptionPane.showInputDialog(thisBench,"Välj nytt namn:", benchName);
                        thisBench.repaint();
                    } else if(e.getButton() == MouseEvent.BUTTON1) {
                        Bench clickedBench = (Bench) e.getSource();
                        classRoom.benchClicked(clickedBench);
                    }
                }
            });
            addMouseListener(new MouseAdapter() {
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
