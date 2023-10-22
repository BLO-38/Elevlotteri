package view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Bench extends JPanel implements Comparable<Bench> {
    public final static int FREE = 0;
    public final static int OCCUPIED = 1;
    public final static int MISSING = 2;
    public final static int FORBIDDEN = 3;
    public final static int MARKED = 4;
    private String benchName;
    private final Room classRoom;
    private int status = FREE;
    private final JLabel nameLabel;
    private int xDim = 0;
    private final int benchNr;
    public static final Color BENCH_NORMAL_BACKGROUND = new Color(224,215,196);
    public static final Color BENCH_MISSING_BACKGROUND = Color.RED;
    private static boolean isShowingMissings = false;

    public Bench(Room cl, int nr) {
        this ( "", nr, cl);
    }


    public Bench(String name, int nr, Room cl) {
        int DEFAULT_WIDTH = 140, DEFAULT_HEIGHT = 120;
        benchNr= nr;
        classRoom = cl;
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setBackground(BENCH_NORMAL_BACKGROUND);



        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Bench clickedBench = (Bench) e.getSource();
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (status == MARKED) JOptionPane.showMessageDialog(clickedBench, "Hörrö, du kan inte byta namn på en markerad bänk");
                    else {
                        String inp = JOptionPane.showInputDialog(clickedBench, "Välj nytt namn (ta bort bänk med - eller gör oanvänd med x)", benchName);
                        if (inp != null) {
                            clickedBench.setName(inp);
                            clickedBench.repaint();
                        }
                    }
                }
                else if(e.getButton() == MouseEvent.BUTTON1 && clickedBench.status != MISSING && clickedBench.status != FORBIDDEN) {
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
        if(status != MISSING) {
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
        if(name.equals("-")) status = MISSING;
        else if(name.equals("x")) status = FORBIDDEN;
        else if(name.length() > 1) status = OCCUPIED;
        else {
            status = FREE;
            benchName = "";
        }
        nameLabel.setVisible(status == OCCUPIED);
        if(status == FORBIDDEN && isShowingMissings)
            setBackground(BENCH_MISSING_BACKGROUND);
        if(status!=FORBIDDEN) setBackground(BENCH_NORMAL_BACKGROUND);
    }

    public String getBenchName() {
        return benchName;
    }

    public void setMarked(boolean marked){
        if (marked) {
            nameLabel.setForeground(Color.RED);
            setBorder(new LineBorder(Color.RED,2));
            status = MARKED;
        }
        else {
            nameLabel.setForeground(Color.WHITE);
            setBorder(null);
            status = benchName.length() > 1 ? OCCUPIED : FREE;
        }
        repaint();
    }

    public int getStatus() {
        return status;
    }

    public int getBenchNr() {
        return benchNr;
    }

    public static void setIsShowingMissings(boolean doShow) {
        isShowingMissings = doShow;
    }

    @Override
    public int compareTo(Bench o) {
        return benchNr - o.getBenchNr();
    }
}
