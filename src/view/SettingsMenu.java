package view;

import databasen.*;
import filer.InitializationHandler;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class SettingsMenu {
    private final JFrame frame;
    private final JPanel panel;
    private LinkedList<JButton> buttons;
    private final String[] labels = {"Ny klass","Ny elev","Hantera elev","Kolla klass","Elevsvar","Hantera grupper","Hantera kön","Kolla grannar","Hantera databasen","Tillbaka"};

    public SettingsMenu(JFrame previousWindow) {
        int[] buttonSize = {150,20};
        frame = new JFrame("Inställningar");
        frame.setLayout(new FlowLayout());
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        buttons = new LinkedList<>();

        for (int i=0 ; i<labels.length ; i++) {
            JPanel p = new JPanel(new GridBagLayout());
            JButton button = new JButton(labels[i]);
            button.setPreferredSize(new Dimension(buttonSize[0], buttonSize[1]));
            p.setPreferredSize(new Dimension(buttonSize[0]+10, buttonSize[1]+10));
            p.add(button);
            buttons.add(i,button);
            panel.add(p);
        }

        buttons.get(0).addActionListener(e -> InsertHandler.setNewClass());
        buttons.get(1).addActionListener(e -> InsertHandler.setNewStudent());
        buttons.get(2).addActionListener(e -> UpdateHandler.updateStudent());
        buttons.get(3).addActionListener(e -> {
            ClassChooser chooser = new ClassChooser();
            String cl = chooser.getChosenClass();
            if (cl == null) return;
            LinkedList<Student> students = DatabaseHandler.getStudents(cl, 0);
            if(students.size() == 0) JOptionPane.showMessageDialog(null, "Inga elever hittades");
            else ClassViewer.showClass(students);
        });
        buttons.get(4).addActionListener(e -> System.out.println("Nu ska student viewre startas"));
        buttons.get(5).addActionListener(e -> new GroupDialog(frame));
        buttons.get(6).addActionListener(e -> new GenderDialog(frame));
        buttons.get(7).addActionListener(e -> {
            ClassChooser chooser = new ClassChooser();
            String cl = chooser.getChosenClass();
            if (cl == null) return;
            new NeighborViewer(cl);
        });
        buttons.get(8).addActionListener(e -> InitializationHandler.newInitialazation(frame));
        buttons.get(9).addActionListener(e -> {
            frame.setVisible(false);
            previousWindow.setVisible(true);
        });

        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}
