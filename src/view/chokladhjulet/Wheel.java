package view.chokladhjulet;

import view.WheelLottery;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Wheel extends JPanel {
    private final Color [] colors =
        {Color.RED,Color.ORANGE,Color.GREEN,Color.BLUE,Color.YELLOW,Color.DARK_GRAY,Color.WHITE};
    private final int sections;
    private final int size,fontHeight;
    private double angle = 0.0;
    private double deltaAngle = 0.0;
    private boolean isStopping = true;
    private final double twoPI = 2*Math.PI;
    public final double toRAD = Math.PI/180;
    private final String[] nameArray;
    private final int offsetAngle;
    private final Font font;
    private final Timer timer;
    private final WheelLottery lottery;
    private int usedColors = 7;

    // Angle: radianer
    // DeltaAngle: radianer
    // Offset: grader



    public Wheel(LinkedList<String> names, int panelHeight, WheelLottery l) {
        if (names.size() % 6 == 0) usedColors = 6;
        else if(names.size() % 5 == 0) usedColors = 5;
        else if(names.size() % 4 == 0) usedColors = 4;
        else if(names.size() % 3 == 0) usedColors = 3;
        else if(names.size() % 2 == 0) usedColors = 2;

        lottery = l;
        nameArray = new String[names.size()];
        for (int i=0; i<names.size(); i++)
            nameArray[i] = names.get(i);

        size = panelHeight;
        sections = names.size();
        offsetAngle = 180/sections;
        fontHeight = Math.min(50,3* size / sections/2);
        font = new Font("Arial", Font.PLAIN, fontHeight);
        setPreferredSize(new Dimension(size,size));
        setBackground(Color.BLACK);
        timer = new Timer(20,e -> {
            if(isStopping) deltaAngle *= 0.99;
            angle += deltaAngle;
            if(angle>twoPI) angle = 0.0;
            if(deltaAngle < 0.003) finishLottery();
            else repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.rotate(angle,size/2.0,size/2.0);
        for (int i = 0; i <sections; i++) {
            g2.setColor(colors[i%usedColors]);
            g2.fillArc(0,0, size, size,i*360/sections + offsetAngle-180,360/sections+1);
        }
        g2.setColor(Color.BLACK);
        for (int i = 0; i <sections; i++) {
            g2.rotate(2*Math.PI/sections,size/2.0, size/2.0);
            g2.setFont(font);
            g2.drawString(nameArray[i], 10, size/2+fontHeight/3);
        }
    }

    /**
     * Startar / stoppar hjulet
     * @return Falskt om den startar inbromsning, sant om den börjar rotera
     */
    public boolean toggleWheelRun() {
        if(!isStopping) {
            isStopping = true;
            return false;
        }

        deltaAngle = 0.08 + 0.3/sections+ Math.random()*0.07;
//        deltaAngle = 0.07 + Math.random()*0.05;
        System.out.println("Deltaangle: " + deltaAngle);
        isStopping = false;
        timer.start();
        return true;
    }

    private void finishLottery() {
        timer.stop();
        System.out.println("Vinkeln blev: " + angle);

        // Justera med halv tårtbit:
        double corrWithOffs = angle + offsetAngle*toRAD;
        System.out.println("Corr med Offs: " + corrWithOffs);

        // Backa ett varv om vi kom förbi:
        if(corrWithOffs > twoPI) corrWithOffs -= twoPI;
        System.out.println("Ny corrWithOff: " + corrWithOffs);

        // Andel av ett varv avgör vilket namn:
        // (med liten justering av vinkeln (0.04), vet ej riktigt varför)
        int nameNr = (int) (sections * (corrWithOffs+0.04) / twoPI);

        // Hjulet snurrar åt motsatt håll jmftr med hur namnen ritas:
        int correctNameNr = nameArray.length-nameNr-1;
        System.out.println("Vinnande nummer: " + nameNr + " dvs " + nameArray[correctNameNr]);

        lottery.handleWinner(nameArray[correctNameNr]);
    }

}
