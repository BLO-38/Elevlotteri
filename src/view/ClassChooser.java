package view;

import databasen.DatabaseHandler;

import javax.swing.*;
import java.awt.*;

public class ClassChooser extends JDialog {

    private String chosenClass = null;
    private JFrame owner = null;

    public ClassChooser(JFrame owner) {
        super(owner);
        this.owner = owner;
        setUp();
    }

    public ClassChooser() {
        setUp();
    }

    private void setUp() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setModal(true);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        JLabel label = new JLabel("Välj klass:");
        infoPanel.setPreferredSize(new Dimension(0, 40));
        infoPanel.add(label);
        add(infoPanel);

        JPanel outerRadioPanel = new JPanel(new GridBagLayout());
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel,BoxLayout.Y_AXIS));
        ButtonGroup group = new ButtonGroup();
        for(String name : DatabaseHandler.getClasses()) {
            JRadioButton r = new JRadioButton(name);
            r.setActionCommand(name);
            group.add(r);
            radioPanel.add(r);
        }
        outerRadioPanel.add(radioPanel);
        add(outerRadioPanel);

        JButton button = new JButton("Fortsätt");
        button.setBackground(new Color(27, 104, 5));
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> {
            if (group.getSelection() == null) return;
            chosenClass = group.getSelection().getActionCommand();
            setVisible(false);
        });

        JPanel finishPanel = new JPanel(new GridBagLayout());
        finishPanel.setPreferredSize(new Dimension(100,50));
        finishPanel.add(button);
        add(finishPanel);
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    public String getChosenClass() {
        return chosenClass;
    }
}