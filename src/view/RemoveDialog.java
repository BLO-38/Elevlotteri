package view;

import model.LotteryType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;

public class RemoveDialog {
    private JDialog dialog;
    private LinkedList<String> allNames;
    private LinkedList<JCheckBox> checkBoxes;
    private LotteryType lottery;

    public RemoveDialog(JFrame parent, LotteryType l) {
        lottery = l;
        allNames = l.getStartNames();
        checkBoxes = new LinkedList<>();
        dialog = new JDialog(parent);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(),BoxLayout.Y_AXIS));
        //dialog.setPreferredSize(new Dimension(300,499));
        setup();
        dialog.pack();
        dialog.setVisible(true);
    }

    private void setup() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(allNames.size(), 1));

        JLabel label = new JLabel("Välj elever:");
        dialog.add(label);

        for(String name : allNames) {
             JCheckBox c = new JCheckBox(name);
             c.setSelected(true);
             checkBoxes.add(c);
             panel.add(c);
        }
        dialog.add(panel);
        JButton button = new JButton("Klart");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(JCheckBox cb : checkBoxes) {
                    if(!cb.isSelected()) lottery.removeName(cb.getActionCommand());
                }
                System.out.println(lottery.getStartNames().toString());

                dialog.setVisible(false);
            }

        });
        dialog.add(button);
    }

}
