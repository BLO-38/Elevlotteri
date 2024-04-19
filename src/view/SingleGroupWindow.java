package view;

import databasen.NameListGetters;
import databasen.UpdateHandler;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

//TODO
// Ladda gamla funkar ej om ingen finns

public class SingleGroupWindow {
    private final JSpinner spinner;
    private final JCheckBox saveCheck;
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
        JLabel header = new JLabel("Välj en grupp elever");
        header.setFont(new Font(null, Font.BOLD, 25));
        headerPanel.add(header);
        menu.add(headerPanel);

        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        saveCheck = new JCheckBox("Spara att de blivit valda");
        saveCheck.setSelected(true);
        savePanel.add(saveCheck);
        menu.add(savePanel);

        JPanel spinnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel spinnText = new JLabel("Antal elever:");
        spinnText.setFont(new Font(null, Font.PLAIN, 20));
        spinnPanel.add(spinnText);

        SpinnerNumberModel model = new SpinnerNumberModel(5, 1, 20, 1);
        spinner = new JSpinner(model);

        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setFocusable(false);
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setEditable(false);
        spinner.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        spinner.setForeground(Color.BLACK);
        spinner.setFont(new Font(null, Font.BOLD, 30));
        spinnPanel.add(spinner);
        menu.add(spinnPanel);

        JPanel buttPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton startButton = new JButton("Skapa grupp");
        startButton.addActionListener(e -> {
            LinkedList<String> l = NameListGetters.getSingleGroup(klass, grp, (int) spinner.getValue(), saveCheck.isSelected());
            if(l != null && !l.isEmpty()) showWindow(l);
            System.out.println(l);
        });
        buttPanel.add(startButton);
        menu.add(buttPanel);

        menu.pack();
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);


//        LinkedList<String> names = new LinkedList<>();
//        names.add("Lars");
//        names.add("Lars");
//        names.add("Lars");
//        names.add("Lars");
//        names.add("Stina");
//        names.add("Stina");
//        names.add("Tord");
//        names.add("Tord");
//        names.add("Gunnel");
//        names.add("Gunnel");
//        names.add("Gunnel");
//        names.add("Gunnel");
//        names.add("Gunnel");
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
