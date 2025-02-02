package view.rast_timer;

import view.MyColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class RastTimer {
    private boolean isRunning = false;
    private int mins, secs;
    private final Timer timer;
    private final JLabel clock;
    private Color lastColor;
    private final Color startColor = new Color(158, 212, 246);
    private final JButton startKnapp;
    private Font digitFont;
    private final int windHeight;


    public RastTimer(int min, int sec) {
        mins = min;
        secs = sec;
        String title = min + " minuter";
        if(secs > 0) title += " " + sec + " sekunder";
        JFrame frame = new JFrame(title);
        frame.setLayout(new BorderLayout());
        JPanel clockPanel = new JPanel();

        clock = new JLabel(min + ":" + (secs<10?"0"+secs:secs));
        clockPanel.add(clock);
        clock.setForeground(startColor);
        lastColor = min < 1 ? Color.ORANGE : Color.GREEN;
        clockPanel.setBackground(Color.BLACK);
        digitFont = new Font("OCR A Extended",Font.PLAIN,300);
        clock.setFont(digitFont);
        startKnapp = new JButton("Start/Stopp");
        startKnapp.setForeground(Color.WHITE);
        startKnapp.setFocusPainted(false);
        startKnapp.setBackground(MyColors.BUTTON_GREEN);
        JButton resetKnapp = new JButton("Reset");
        resetKnapp.setBackground(MyColors.BUTTON_RED);
        resetKnapp.setForeground(Color.WHITE);
        resetKnapp.setFocusPainted(false);
        frame.add(clockPanel,BorderLayout.CENTER);

        JPanel buttPanel = new JPanel(new FlowLayout());
        buttPanel.add(startKnapp);
        buttPanel.add(resetKnapp);

        frame.add(buttPanel,BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        windHeight = frame.getHeight();
        timer = new Timer(100,e -> {
            if (secs == 0) {
                if(mins == 0) {
                    finish();
                    return;
                }
                secs += 60;
                if(mins == 1) {
                    lastColor = Color.ORANGE;
                    clock.setForeground(lastColor);
                }
                mins--;
            }
            secs--;
            String text = mins + ":" + (secs < 10 ? "0"+secs : secs);
            clock.setText(text);
        });
        startKnapp.addActionListener(e -> {
            if(isRunning) {
                timer.stop();
                clock.setForeground(startColor);
            } else {
                timer.start();
                clock.setForeground(lastColor);
            }
            isRunning = !isRunning;
        });
        resetKnapp.addActionListener(e -> {
            timer.stop();
            mins = min;
            secs = sec;
            clock.setText(min + ":" + (secs<10?"0"+secs:secs));
            isRunning = false;
            startKnapp.setEnabled(true);
            lastColor = min < 1 ? Color.ORANGE : Color.GREEN;
            clock.setForeground(startColor);
        });
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                int newHeight = 300 + frame.getHeight() - windHeight;
                digitFont = new Font("OCR A Extended",Font.PLAIN,newHeight);
                clock.setFont(digitFont);
            }
        });
    }

    private void finish() {
        timer.stop();
        clock.setForeground(Color.RED);
        startKnapp.setEnabled(false);
    }

}
