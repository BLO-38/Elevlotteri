package view;

import databasen.NameListGetters;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

//TODO
// Ladda gamla funkar ej om ingen finns

public class SingleGroupWindow {
    private final JSpinner spinner;
    private final JCheckBox saveCheck, featureCheck;
    private final int grp;
    private final String klass;

    public static void main(String[] args) {
        new SingleGroupWindow("Gubbar",1);
    }

    public SingleGroupWindow(String kl, int gr) {
        klass = kl;
        grp = gr;
        JFrame menu = new JFrame();
        menu.setLayout(new BoxLayout(menu.getContentPane(), BoxLayout.Y_AXIS));


        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel header = new JLabel("Slumpa fram en grupp elever");
        header.setFont(new Font(null, Font.BOLD, 25));
        headerPanel.add(header);
        menu.add(headerPanel);
        menu.add(Box.createRigidArea(new Dimension(0,20)));

        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        saveCheck = new JCheckBox("Spara att de blivit valda  ");
        saveCheck.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        saveCheck.setFont(new Font(null,Font.PLAIN,20));
        saveCheck.setSelected(true);
        saveCheck.setFocusPainted(false);
        savePanel.add(saveCheck);
        menu.add(savePanel);
        menu.add(Box.createRigidArea(new Dimension(0,20)));

        JPanel spinnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel spinnText = new JLabel("Antal elever:");
        spinnText.setFont(new Font(null, Font.PLAIN, 20));
        spinnPanel.add(spinnText);

        SpinnerNumberModel model = new SpinnerNumberModel(5, 1, 20, 1);
        spinner = new JSpinner(model);

        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setFocusable(false);
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setBackground(Color.WHITE);
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setEditable(false);
        spinner.setForeground(Color.BLACK);
        spinner.setFont(new Font(null, Font.BOLD, 25));

        spinnPanel.add(spinner);
        menu.add(spinnPanel);
        menu.add(Box.createRigidArea(new Dimension(0,20)));

        JPanel featurePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        featureCheck = new JCheckBox("Starta med separat knapp  ");
        featureCheck.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        featureCheck.setFont(new Font(null,Font.PLAIN,20));
        featureCheck.setSelected(false);
        featureCheck.setFocusPainted(false);
        featurePanel.add(featureCheck);
        menu.add(featurePanel);
        menu.add(Box.createRigidArea(new Dimension(0,20)));


        JPanel buttPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton startButton = new JButton("Skapa grupp");
        startButton.addActionListener(e -> {
            LinkedList<String> l = NameListGetters.getSingleGroup(klass, grp, (int) spinner.getValue(), saveCheck.isSelected());
            if(l != null && !l.isEmpty()) {
                if(featureCheck.isSelected()) {
                    MainMenu.minimize();
                    menu.setExtendedState(Frame.ICONIFIED);
                    JOptionPane.showMessageDialog(null,"Skapa grupp!");
                }
                showWindow(l);
            }
        });
        buttPanel.add(startButton);
        menu.add(buttPanel);
        menu.add(Box.createRigidArea(new Dimension(0,20)));

        menu.pack();
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
    }
    private void showWindow(LinkedList<String> names) {
        JFrame frame = new JFrame();
        int scale = 5;
        int h = (names.size()+1)*12*scale;
        StudentGroup sg = new StudentGroup(names, h,false, scale);
        frame.add(sg);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}
