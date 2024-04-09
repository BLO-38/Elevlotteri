package view;

import databasen.*;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class SettingsMenu {
    private final JFrame frame;
    private String cls;

    public SettingsMenu() {
        frame = new JFrame("Inställningar");
        frame.setLayout(new FlowLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        LinkedList<JButton> buttons = new LinkedList<>();
        LinkedList<JPanel> panels = new LinkedList<>();

        String[] labels = {"Ny klass", "Ny elev", "Hantera elev",
            "Kolla klass", "Elevsvar", "Hantera grupper",
            "Hantera kön", "Kolla grannar", "Ta bort klass",
            "Radera bordsplaceringar", "Hantera databasen", "Tillbaka"};
        for (int i = 0; i< labels.length ; i++) {
            JPanel p = new JPanel(new GridBagLayout());
            JButton button = new JButton(labels[i]);
            p.add(button);
            buttons.add(i,button);
            panels.add(i,p);
            panel.add(p);
        }

        buttons.get(0).addActionListener(e -> InsertHandler.setNewClass());
        buttons.get(1).addActionListener(e -> InsertHandler.setNewStudent());
        buttons.get(2).addActionListener(e -> UpdateHandler.updateStudent());
        buttons.get(3).addActionListener(e -> {
            new ClassChooser2(frame,response -> cls = response);
            if (cls == null) return;
            LinkedList<Student> students = SelectHandler.getStudents(cls, 0);
            if(students.isEmpty()) JOptionPane.showMessageDialog(null, "Inga elever hittades");
            else ClassViewer.showClass(students);
        });
        buttons.get(4).addActionListener(e -> JOptionPane.showMessageDialog(null,"men chilla, detta är inte klart"));
        buttons.get(5).addActionListener(e -> new GroupDialog(frame));
        buttons.get(6).addActionListener(e -> new GenderDialog(frame));
        buttons.get(7).addActionListener(e -> {
            new ClassChooser2(frame,response -> cls = response);
            if (cls == null) return;
            new NeighborViewer(cls);
        });
        buttons.get(8).addActionListener(e -> removeKlass());
        buttons.get(9).addActionListener(e -> new OldSeatingStarter(OldSeatingStarter.DELETE_CLASSROOMS));
        buttons.get(10).addActionListener(e -> handleDB());
        buttons.get(11).addActionListener(e -> {
            frame.setVisible(false);
            new MainMenu();
        });

        frame.add(panel);
        frame.pack();
        int maxWidth = 0;
        for (JButton b : buttons) {
            int w = b.getWidth();
            if(w > maxWidth) maxWidth = w;
        }
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setPreferredSize(new Dimension(maxWidth,20));
            panels.get(i).setPreferredSize(new Dimension(maxWidth+10,30));
        }

        panel.revalidate();
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void removeKlass() {
        boolean hasRemoved = false;
        cls = null;
        new ClassChooser2(frame,response -> cls = response);
        if (cls != null) hasRemoved = DeleteHandler.deleteKlass(cls);
        if (!hasRemoved) JOptionPane.showMessageDialog(null,"Inget raderat");
    }
    private void handleDB() {
        String[] options = {"Byta till annan befintlig", "Skapa ny", "Koppla från"};
        int ans = JOptionPane.showOptionDialog(null, "Vad vill du göra med databasen?", "Fråga:", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, 0);
        if (ans == 0) DatabaseHandler2.switchDB();
        if (ans == 1) DatabaseHandler2.createNewDB();
        if (ans == 2) DatabaseHandler2.disconnectDB();
    }
}
