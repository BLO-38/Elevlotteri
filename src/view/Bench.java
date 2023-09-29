package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class Bench extends JPanel {
    public final static int NORMAL = 0;
    public final static int EMPTY = 1;
    public final static int NO_BENCH = 2;
    public final static int MARKED = 3;
    private String benchName;
    private final Room classRoom;
    private int status = NORMAL;
    private final JLabel nameLabel;
    private int xDim = 0;

    public Bench(Room cl) {
        this ( "", cl);
    }


    public Bench(String name, Room cl) {
        int DEFAULT_WIDTH = 140, DEFAULT_HEIGHT = 120;
        classRoom = cl;
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setBackground(new Color(224,215,196));
        // Bench thisBench = this;



        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Bench clickedBench = (Bench) e.getSource();
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (status == MARKED) JOptionPane.showMessageDialog(clickedBench, "Hörrö, du kan inte byta namn på en markerad bänk");
                    else {
                        String inp = JOptionPane.showInputDialog(clickedBench, "Välj nytt namn (ta bort bänk med -, gör oanvänd med x)", benchName);
                        if (inp != null) {
                            clickedBench.setName(inp);
                            clickedBench.repaint();
                        }
                    }
                }
                else if(e.getButton() == MouseEvent.BUTTON1 && clickedBench.status != NO_BENCH && clickedBench.status != EMPTY) {
                    classRoom.benchClicked(clickedBench);
                }
            }
        });

        nameLabel = new JLabel();
        nameLabel.setForeground(Color.WHITE);
        setName(name);
        add(nameLabel);

    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        if(status != NO_BENCH) {
            g.setColor(new Color(10, 20, 148));
            int[] dims = classRoom.getBenchDimensions();
            g.fillRoundRect(6, 6, dims[0]-12, dims[1]-12, 25, 25);
            if (xDim != dims[0]) {
                nameLabel.setFont(new Font("arial narrow", Font.PLAIN,dims[0]/5));
                xDim = dims[0];
            }
        }
    }

    public void setName(String name) {
        benchName = name;
        nameLabel.setText(benchName);
        if(name.equals("-")) status = NO_BENCH;
        else if(name.equals("x")) status = EMPTY;
        else status = NORMAL;
        nameLabel.setVisible(status == NORMAL);
    }

    public String getBenchName() {
        return benchName;
    }

    public void setMarked(boolean marked){
        if (marked) nameLabel.setForeground(Color.RED);
        else nameLabel.setForeground(Color.WHITE);
        status = marked ? MARKED : NORMAL;
        repaint();
    }

    public int getStatus() {
        return status;
    }
}
