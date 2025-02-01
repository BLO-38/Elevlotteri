package view.rast_timer;

import view.MyColors;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TimerMenu {
    private int count = 0;
    private boolean isRunning = false;
    private static Color backgrCol = new Color(0xBAF599);
    public static void main(String[] args) {
        new TimerMenu();
    }

    public TimerMenu() {
        JFrame optionsFrame = new JFrame();
        JPanel bigPanel = new JPanel(new FlowLayout());
        optionsFrame.add(bigPanel);
        bigPanel.setBackground(backgrCol);
        bigPanel.setBorder(new LineBorder(Color.BLACK,7,true));
        bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.Y_AXIS));
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,30));
        headerPanel.setOpaque(false);
        JLabel header = new JLabel("Välj tid");
        header.setFont(new Font("Monospaced",Font.BOLD,30));
        headerPanel.add(header);
        bigPanel.add(headerPanel);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        inputPanel.setOpaque(false);
        JTextField minutesField = new JTextField("5");
        minutesField.setBorder(new LineBorder(Color.BLACK,3,true));
        JTextField secondsField = new JTextField("0");
        secondsField.setBorder(new LineBorder(Color.BLACK,3,true));
        minutesField.setPreferredSize(new Dimension(50, 30));
        secondsField.setPreferredSize(new Dimension(50, 30));
        secondsField.setFont(new Font("OCR A Extended",Font.PLAIN,24));
        minutesField.setFont(new Font("OCR A Extended",Font.PLAIN,24));
        JLabel minLabel = new JLabel("min");
        JLabel secLabel = new JLabel("sek");
        inputPanel.add(minutesField);
        inputPanel.add(minLabel);
        inputPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        inputPanel.add(secondsField);
        inputPanel.add(secLabel);
        bigPanel.add(inputPanel);

        JPanel buttPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
        buttPanel.setOpaque(false);
        JButton startButton = new JButton("Fortsätt");
        startButton.setForeground(Color.WHITE);
        JButton abortButton = new JButton("Avbryt");
        abortButton.setForeground(Color.WHITE);
        startButton.setBackground(MyColors.BUTTON_GREEN);
        abortButton.setBackground(MyColors.BUTTON_RED);
        startButton.addActionListener(e -> {
            int min = checkTime(minutesField.getText());
            int sec = checkTime(secondsField.getText());
            if (min == -1 || sec == -1) {
                JOptionPane.showMessageDialog(null,"Ogiltig tid!");
                return;
            }
            optionsFrame.dispose();
            new RastTimer(min, sec);
        });
        abortButton.addActionListener(e -> optionsFrame.dispose());
        buttPanel.add(abortButton);
        buttPanel.add(startButton);

        bigPanel.add(buttPanel);

        optionsFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        optionsFrame.setUndecorated(true);
        optionsFrame.pack();
        optionsFrame.setLocationRelativeTo(null);
        optionsFrame.setVisible(true);

    }
    private int checkTime(String t) {
        try {
            int i = Integer.parseInt(t);
            if(i<0 || i>59) return -1;
            return i;
        } catch (Exception e) {
            return -1;
        }
    }
}
