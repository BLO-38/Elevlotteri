package view.chokladhjulet;

import javax.swing.*;
import java.awt.*;

public class Arrow extends JPanel {
    private static final int extraSpace = 20;
    private static final int arrowLength = 300;
    private static final int arrowHead = 100;
    private static final int arrowWidth = 10;
    private static final int fontHeight = 80;
    private static final int fastDelay = 70, slowDelay = 300;
    private int count = 0;
    private int maxColors = 2;
    private final int[] xp;
    private final int[] yp;
    private final JLabel label;
    private Timer timer;
    private Color[] colors;
    private Color[] winnerColors = {Color.RED,Color.YELLOW};
    private Color[] stoppingColors = {Color.BLUE,Color.WHITE};

    public Arrow(int sizeY) {
        colors = stoppingColors;
        setLayout(null);
        label = new JLabel(" Lotteri!");
        label.setForeground(Color.YELLOW);
        label.setFont(new Font("Chiller",Font.PLAIN,fontHeight));
        add(label);
        label.setBounds(extraSpace*2,sizeY/4,arrowLength+arrowHead,fontHeight+10);
        int screenX = arrowLength + arrowHead + extraSpace * 2;
        xp = new int[] {extraSpace, extraSpace + arrowLength,
            extraSpace + arrowLength, extraSpace + arrowLength + arrowHead,
            extraSpace + arrowLength, extraSpace + arrowLength,
            extraSpace, extraSpace};
        yp = new int[] {sizeY/2-arrowWidth, sizeY/2-arrowWidth, sizeY/2-2*arrowWidth,
            sizeY/2, sizeY/2+2*arrowWidth, sizeY/2+arrowWidth, sizeY/2+arrowWidth, sizeY/2-arrowWidth};

        setPreferredSize(new Dimension(screenX, sizeY));
        setBackground(Color.BLACK);
        timer = new Timer(fastDelay,e -> {
            label.setForeground(colors[count]);
            count = count == 0 ? 1 : 0;
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.fillPolygon(new Polygon(xp,yp,8));
    }
    public void showWinner(String name) {
        label.setText(name);
        colors = winnerColors;
        timer.setDelay(slowDelay);
        timer.start();
    }
    public void showSpinning() {
        timer.stop();
        label.setText("Vi snurrar...");
        label.setForeground(Color.GREEN);
    }
    public void showStopping() {
        label.setText("Stoppar");
        colors = stoppingColors;
        timer.setDelay(fastDelay);
        timer.start();
    }
}
