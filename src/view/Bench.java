package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Bench extends JPanel {
    private String benchName;
    private final Room classRoom;
    private boolean exists = true;
    private JLabel nameLabel;

    public Bench(Room cl) {
        this ( "", cl);
    }


    public Bench(String name, Room cl) {
        int DEFAULT_WIDTH = 140, DEFAULT_HEIGHT = 120;
        classRoom = cl;
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setBackground(new Color(224,215,196));
        Bench thisBench = this;

        if(exists) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);

                    if (e.getButton() == MouseEvent.BUTTON3) {
                        String inp = JOptionPane.showInputDialog(thisBench,"Välj nytt namn:", benchName);
                        thisBench.setName(inp);
                        thisBench.repaint();
                    } else if(thisBench.exists && e.getButton() == MouseEvent.BUTTON1) {
                        Bench clickedBench = (Bench) e.getSource();
                        classRoom.benchClicked(clickedBench);
                    }
                }
            });

            nameLabel = new JLabel();
            nameLabel.setForeground(Color.WHITE);
            setName(name);
            add(nameLabel);
        }
    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        if(exists) {
            g.setColor(new Color(10, 20, 148));

            // GAMLA: g.fillRoundRect(6, 6, 128, 108, 25, 25);
            int[] dims = classRoom.getBenchDimensions();
            g.fillRoundRect(6, 6, dims[0]-12, dims[1]-12, 25, 25);
            nameLabel.setFont(new Font("arial narrow", Font.PLAIN,dims[0]/5));
        }
    }

    public void setName(String name) {
        benchName = name;
        nameLabel.setText(benchName);
        exists = !name.equals("-");
        nameLabel.setVisible(exists);
    }

    public String getBenchName() {
        return benchName;
    }

    public void toggleRedName(boolean switchToRed){
        if (switchToRed) nameLabel.setForeground(Color.RED);
        else nameLabel.setForeground(Color.WHITE);
        repaint();
    }

    public boolean doExist() {
        return exists;
    }
}
