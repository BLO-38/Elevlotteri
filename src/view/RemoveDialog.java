package view;

import model.LotteryType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;

public class RemoveDialog {
    private final JDialog dialog;
    private final LinkedList<String> allNames;
    private final LinkedList<JCheckBox> checkBoxes;
    private final LotteryType lottery;

    public RemoveDialog(JFrame parent, LotteryType l) {
        lottery = l;
        allNames = l.getStartNames();
        Collections.sort(allNames);
        checkBoxes = new LinkedList<>();
        dialog = new JDialog(parent);
        dialog.setModal(true);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(),BoxLayout.Y_AXIS));

        setup();
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    private void setup() {

        JPanel p1 = new JPanel(new GridBagLayout());
        JLabel label = new JLabel("Välj elever:");
        p1.setPreferredSize(new Dimension(0,40));
        p1.add(label);
        dialog.add(p1);

        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(1,2));
        p2.setBorder(new LineBorder(Color.BLUE,5));
        JPanel leftPanelOuter = new JPanel();
        JPanel leftPanelInner = new JPanel();
        JPanel rightPanel = new JPanel();
        leftPanelOuter.setLayout(new BoxLayout(leftPanelOuter,BoxLayout.X_AXIS));
        leftPanelInner.setLayout(new BoxLayout(leftPanelInner,BoxLayout.Y_AXIS));
        rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.Y_AXIS));
//        leftPanelOuter.setBorder(new LineBorder(Color.RED,2));
//        leftPanelInner.setBorder(new LineBorder(Color.YELLOW,2));
//        rightPanel.setBorder(new LineBorder(Color.GREEN,2));
        p2.add(leftPanelOuter);
        leftPanelOuter.add(leftPanelInner);
        leftPanelOuter.add(Box.createRigidArea(new Dimension(30,0)));
        p2.add(rightPanel);
        // p2.setLayout(new GridLayout(allNames.size(), 1));
        boolean left = true;

        for(String name : allNames) {
             JCheckBox c = new JCheckBox(name,true);
             checkBoxes.add(c);
             if(left) leftPanelInner.add(c);
             rightPanel.add(c);
             left = !left;
        }
        dialog.add(p2);

        JButton button = new JButton("Klart");
        button.setBackground(new Color(27, 104, 5));
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> {
            for(JCheckBox cb : checkBoxes) {
                if(!cb.isSelected()) lottery.removeName(cb.getActionCommand());
            }
            dialog.setVisible(false);
        });
        JPanel p3 = new JPanel(new GridBagLayout());
        p3.setPreferredSize(new Dimension(100,50));
        p3.add(button);
        dialog.add(p3);
    }

}
