package view;

import model.Lottery;
import model.MainHandler;
import model.RegularLottery;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;

public class LotteryMenu {
    
    private JFrame lotteryFrame;
    private JCheckBox checkBoxShowTaken, checkBoxShowNr;
    private ButtonGroup sizeGroup;
    private final MainHandler lotteryHandler;

    public LotteryMenu (Lottery lottery, MainHandler mainHandler, JFrame mainFrame) {
        lotteryHandler = mainHandler;
        lotteryFrame = new JFrame();
        lotteryFrame.setLayout(new BoxLayout(lotteryFrame.getContentPane(),BoxLayout.Y_AXIS));
        JPanel namePanel = new JPanel(new FlowLayout());
        JPanel featuresPanel = new JPanel();
        JPanel buttonPanel1 = new JPanel();
        JPanel buttonPanel2 = new JPanel();
        JPanel removePanel = new JPanel();
        JPanel sizingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        featuresPanel.setLayout(null);
        featuresPanel.setPreferredSize(new Dimension(0,80));
        featuresPanel.setBorder(new LineBorder(Color.RED));
        buttonPanel1.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel2.setLayout(new FlowLayout(FlowLayout.CENTER));
        removePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel allNamesLabel = new JLabel(getAllNames(lottery));
        allNamesLabel.setFont(new Font("arial", Font.PLAIN,10));
        namePanel.add(allNamesLabel);

        JButton startButton = new JButton("Starta lotteri");
        startButton.setBackground(new Color(27, 104, 5));
        startButton.setBackground(MainHandler.MY_GREEN);
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(e -> {
            lottery.setScale(Integer.parseInt(sizeGroup.getSelection().getActionCommand()));
            lottery.setShowCount(checkBoxShowNr.isSelected());
            lottery.setSaveNames(checkBoxShowTaken.isSelected());
            lotteryFrame.setVisible(false);
            lotteryHandler.startLottery(lottery);
        });
        JButton backButton = new JButton("Tillbaka");
        backButton.addActionListener(e -> {
            System.out.println("Tillbaka");
            System.out.println("Metoden");
            lotteryFrame.setVisible(false);
            mainFrame.setVisible(true);
        });

        buttonPanel1.add(backButton);
        buttonPanel2.add(startButton);

        JLabel headerText = new JLabel("Extraval:");
        headerText.setFont(new Font(null, Font.BOLD, 14));
        headerText.setBorder(new EmptyBorder(0, 0, 0, 40));
        checkBoxShowTaken = new JCheckBox("Visa alla som lottats fram     ", false);
        checkBoxShowTaken.setBounds(100,20,200,20);
        featuresPanel.add(checkBoxShowTaken);
        checkBoxShowNr = new JCheckBox("Visa antal kvar     ", false);
        checkBoxShowNr.setBounds(100,45,200,20);
        featuresPanel.add(checkBoxShowNr);

        sizeGroup = new ButtonGroup();
        JRadioButton xSmallButt = new JRadioButton("XS");
        xSmallButt.setActionCommand("2");
        JRadioButton smallButt = new JRadioButton("S");
        smallButt.setActionCommand("4");
        JRadioButton medButt = new JRadioButton("M", true);
        medButt.setActionCommand("6");
        JRadioButton largeButt = new JRadioButton("L");
        largeButt.setActionCommand("8");
        JRadioButton xLargeButt = new JRadioButton("XL");
        xLargeButt.setActionCommand("10");
        JRadioButton fullButt = new JRadioButton("Full");
        fullButt.setActionCommand("12");
        sizeGroup.add(xSmallButt);
        sizeGroup.add(smallButt);
        sizeGroup.add(medButt);
        sizeGroup.add(largeButt);
        sizeGroup.add(xLargeButt);
        sizeGroup.add(fullButt);
        sizingPanel.add(xSmallButt);
        sizingPanel.add(smallButt);
        sizingPanel.add(medButt);
        sizingPanel.add(largeButt);
        sizingPanel.add(xLargeButt);
        sizingPanel.add(fullButt);

        JButton removeButton = new JButton("Ta bort namn");
        removeButton.setBackground(MainHandler.MY_RED);
        removeButton.setForeground(Color.WHITE);
        removeButton.addActionListener(e -> {
            new RemoveDialog(lotteryFrame, lottery, null);
            allNamesLabel.setText(getAllNames(lottery));
            System.out.println("Tebax");
            lotteryFrame.pack();
        });
        removePanel.add(removeButton);

        JButton addButton = new JButton("Lägg till namn");
        addButton.setBackground(new Color(185, 241, 190));
        addButton.addActionListener(e -> {
            String extraName = JOptionPane.showInputDialog(lotteryFrame,"Ange namn:" );
            if(extraName == null || extraName.isEmpty()) return;
            lottery.addName(extraName);
            if (lottery instanceof RegularLottery) lottery.shuffleStartnames();
            allNamesLabel.setText(getAllNames(lottery));
            lotteryFrame.pack();
        });
        removePanel.add(addButton);

        JPanel messPanel = new JPanel();
        messPanel.setLayout(new FlowLayout());
        messPanel.add(new JLabel("Fönsterstorlek:"));

        lotteryFrame.add(namePanel);
        lotteryFrame.add(featuresPanel);
        lotteryFrame.add(removePanel);
        lotteryFrame.add(messPanel);
        lotteryFrame.add(sizingPanel);
        lotteryFrame.add(buttonPanel1);
        lotteryFrame.add(buttonPanel2);
        lotteryFrame.pack();
        lotteryFrame.setLocationRelativeTo(null);
        lotteryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lotteryFrame.setVisible(true);
    }

    private String getAllNames(Lottery lottery) {
        StringBuilder sb = new StringBuilder("<html>");
        int count = 0;
        LinkedList<String> tempList = lottery.getStartNames();
        Collections.sort(tempList);
        for (String s : tempList) {
            sb.append(s).append(",");
            count++;
            if(count % 15 == 0) sb.append("<br>");
        }
        sb.append("</html>");
        return sb.toString();
    }
}
