package view;

import model.Lottery;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;

public class RemoveDialog {
    private final JDialog dialog;
    private final LinkedList<String> allNames;
    private final LinkedList<JCheckBox> checkBoxes;
    private Lottery lottery = null;
    private final boolean useTwo, isLottery;



    public RemoveDialog(JFrame parent, Lottery l, LinkedList<String> nameList) {
        lottery = l;
        isLottery = lottery != null;
        allNames = isLottery ? lottery.getStartNames() : nameList;
        useTwo = allNames.size() > 12;
        Collections.sort(allNames);
        checkBoxes = new LinkedList<>();
        dialog = new JDialog(parent);
        dialog.setModal(true);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(),BoxLayout.Y_AXIS));

        setup();
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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

        p2.add(leftPanelOuter);
        leftPanelOuter.add(leftPanelInner);
        leftPanelOuter.add(Box.createRigidArea(new Dimension(30,0)));
        if(useTwo) p2.add(rightPanel);

        boolean left = true;
        for(String name : allNames) {
             JCheckBox c = new JCheckBox(name,true);
             checkBoxes.add(c);
             if(left) leftPanelInner.add(c);
             else rightPanel.add(c);
             if(useTwo) left = !left;
        }
        dialog.add(p2);

        JButton button = new JButton("Klart");
        button.setBackground(new Color(27, 104, 5));
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> {
            for(JCheckBox cb : checkBoxes) {
                if(!cb.isSelected()) {
                    // Fixa här
                    if(lottery == null) allNames.remove(cb.getActionCommand());
                    else lottery.removeName(cb.getActionCommand());
                }
            }
            dialog.setVisible(false);
        });
        JPanel p3 = new JPanel(new GridBagLayout());
        p3.setPreferredSize(new Dimension(100,50));
        p3.add(button);
        dialog.add(p3);
    }
}
