package view.chokladhjulet;

import databasen.LiveUpdateHandler;
import databasen.NameListGetters;
import view.WheelLottery;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class ChoclateWheel extends JPanel implements WheelLottery {
    private JFrame frame;
    private String className;
    private int grp;
    private JButton[] buttons = new JButton[2];
    private Arrow arrow;

    public ChoclateWheel(String cl, int gr) {
        className = cl;
        grp = gr;

        LinkedList<String> startNames = NameListGetters.getCandyList(className, grp);
        if (startNames == null) return;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println(screenSize);
        System.out.println("Antal: " + startNames.size());
        int screenY = screenSize.height - 150;


        frame = new JFrame();
        frame.setLayout(new BorderLayout(0, 0));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        arrow = new Arrow(screenY);
        frame.add(arrow, BorderLayout.WEST);
        Wheel wheel = new Wheel(startNames, screenY, this);
        frame.add(wheel, BorderLayout.CENTER);
        JPanel buttPanel = new JPanel(new FlowLayout());

        buttPanel.setBackground(Color.BLACK);
        frame.add(buttPanel, BorderLayout.SOUTH);

        buttons[0] = new JButton("Starta!");
        buttons[1] = new JButton("En gång till!");
        buttons[1].setEnabled(false);
        buttons[1].setBackground(Color.GREEN);

        for (JButton b : buttons) {
            b.setPreferredSize(new Dimension(300, 50));
            b.setFont(new Font("Chiller", Font.BOLD, 40));
            b.setFocusPainted(false);
            buttPanel.add(b);
        }

        buttons[0].addActionListener(e -> {
            boolean isRunning = wheel.toggleWheelRun();
            String text = isRunning ? "STOPP" : "Starta!";
            Color buttonBackground = isRunning ? Color.RED : Color.LIGHT_GRAY;
            buttons[0].setBackground(buttonBackground);
            buttons[0].setText(text);
            if(isRunning) arrow.showSpinning();
            else arrow.showStopping();
        });

        buttons[1].addActionListener(e -> {
            frame.dispose();
            new ChoclateWheel(className,grp);
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    @Override
    public void handleWinner(String name) {
        arrow.showWinner(name);
        LiveUpdateHandler.updateCandy(name, className);
        buttons[0].setEnabled(false);
        buttons[1].setEnabled(true);
    }
}