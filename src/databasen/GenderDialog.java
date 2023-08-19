package databasen;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.LinkedList;

public class GenderDialog {
    private final JDialog dialog;
    private final LinkedList<Student> students;
    private final LinkedList<ButtonGroup> buttonGroups;
    private String klass;
    private JPanel lastLeft;
    private int nameColumns;
    private JPanel[] columnPanels;

    public GenderDialog(JFrame parent) {
        getKlass();
        students = DatabaseHandler.getStudents(klass,0);
        int maxPerColumn = 10;
        nameColumns = students.size() / maxPerColumn + 1;
        if (nameColumns > 3) nameColumns = 3;
        System.out.println("Kolumner: " + nameColumns);
        buttonGroups = new LinkedList<>();
        dialog = new JDialog(parent);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(),BoxLayout.Y_AXIS));

        JPanel p1 = new JPanel(new FlowLayout());
        JPanel p2 = namePanel();
        JPanel p3 = new JPanel(new FlowLayout());

        JLabel text = new JLabel("Sätt kön:");
        text.setFont(new Font(null, Font.PLAIN,24));
        p1.add(text);

        JButton finishButton = new JButton("Verkställ");
        finishButton.setBackground(new Color(27, 104, 5));
        finishButton.addActionListener(e -> updateGender());
        JButton abortButton = new JButton("Avbryt");
        abortButton.setBackground(Color.RED);
        abortButton.setForeground(Color.WHITE);
        finishButton.setForeground(Color.WHITE);
        abortButton.addActionListener(e -> dialog.dispose());
        p3.add(abortButton);
        p3.add(finishButton);

        dialog.add(p1);
        dialog.add(p2);
        dialog.add(Box.createRigidArea(new Dimension(0,10)));
        dialog.add(p3);
        dialog.pack();
        if(nameColumns == 2 && students.size() %2 == 1) {
            columnPanels[1].add(Box.createRigidArea(new Dimension(10, lastLeft.getHeight())));
        } else if (nameColumns == 3){
            if(students.size() % 3 > 0) {
                columnPanels[2].add(Box.createRigidArea(new Dimension(10, lastLeft.getHeight())));
            }
            if(students.size() % 3 == 1){
                columnPanels[1].add(Box.createRigidArea(new Dimension(10, lastLeft.getHeight())));
            }
        }
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private void getKlass() {
        JDialog dialog1 = new JDialog();
        dialog1.setLayout(new BoxLayout(dialog1.getContentPane(),BoxLayout.Y_AXIS));
        dialog1.setModal(true);

        JPanel p1 = new JPanel(new GridBagLayout());
        JLabel label = new JLabel("Välj klass:");
        p1.setPreferredSize(new Dimension(0,40));
        p1.add(label);
        dialog1.add(p1);

        JPanel p2 = new JPanel(new GridBagLayout());
        JPanel p2B = new JPanel();
        p2B.setLayout(new BoxLayout(p2B,BoxLayout.Y_AXIS));
        ButtonGroup group = new ButtonGroup();
        for(String name : DatabaseHandler.getClasses()) {
            JRadioButton r = new JRadioButton(name);
            r.setActionCommand(name);
            group.add(r);
            p2B.add(r);
        }
        p2.add(p2B);
        dialog1.add(p2);

        JButton button = new JButton("Fortsätt");
        button.setBackground(new Color(27, 104, 5));
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> {
            if (group.getSelection() == null) return;
            klass = group.getSelection().getActionCommand();
            dialog1.setVisible(false);
        });

        JPanel p3 = new JPanel(new GridBagLayout());
        p3.setPreferredSize(new Dimension(100,50));
        p3.add(button);
        dialog1.add(p3);
        dialog1.pack();
        dialog1.setLocationRelativeTo(null);
        dialog1.setVisible(true);
    }

    private void updateGender() {
        for(int i=0 ; i< students.size() ; i++) {
            String newGender = buttonGroups.get(i).getSelection().getActionCommand();
            students.get(i).setGender(newGender);
        }
        boolean result = UpdateHandler.setNewGender(students);
        if(!result) JOptionPane.showMessageDialog(null,"Funkade inte tyvärr. Felkod updategroups");
        dialog.dispose();
    }

    private JPanel namePanel() {
        Color backColor = new Color(80,80,80);
        int width = 80, height = 12;

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container,BoxLayout.X_AXIS));
        columnPanels = new JPanel[3];

        for (int i = 0; i < nameColumns; i++) {
            JPanel columnPanel = new JPanel();
            columnPanel.setBorder(new LineBorder(Color.BLACK,2));
            columnPanel.setLayout(new BoxLayout(columnPanel, BoxLayout.Y_AXIS));

            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            titlePanel.setBackground(backColor);
            titlePanel.add(Box.createRigidArea(new Dimension(5, 0)));
            JLabel title = new JLabel("Elev");
            title.setPreferredSize(new Dimension(width, height));
            title.setForeground(Color.WHITE);
            title.setFont(new Font(null, Font.BOLD, 12));
            titlePanel.add(title);

            JLabel numbers1 = new JLabel("M");
            JLabel numbers2 = new JLabel("K");

            numbers1.setForeground(Color.WHITE);
            numbers2.setForeground(Color.WHITE);
            JPanel pn1 = new JPanel(new GridBagLayout());
            JPanel pn2 = new JPanel(new GridBagLayout());
            pn2.setBackground(backColor);
            pn1.setBackground(backColor);
            pn1.setPreferredSize(new Dimension(21, height));
            pn2.setPreferredSize(new Dimension(20, height));
            pn1.add(numbers1);
            pn2.add(numbers2);
            titlePanel.add(pn1);
            titlePanel.add(pn2);
            columnPanel.add(titlePanel);
            columnPanels[i] = columnPanel;
        }

        int columnNr = 0;
        for(Student student : students) {
            JPanel studentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            studentPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, backColor));
            studentPanel.add(Box.createRigidArea(new Dimension(5,0)));

            JLabel name = new JLabel(student.getName());
            name.setPreferredSize(new Dimension(width,height));
            studentPanel.add(name);
            name.setToolTipText(student.getName());

            ButtonGroup buttonGroup = new ButtonGroup();

            for (int i = 0; i < 2; i++) {
                JRadioButton r = new JRadioButton();
                String gender = i == 0 ? "m" : "k";
                r.setActionCommand(gender);
                if (student.getGender().equals(gender)) r.setSelected(true);
                buttonGroup.add(r);
                JPanel prb = new JPanel(new GridBagLayout());
                prb.add(r);
                studentPanel.add(prb);
            }
            buttonGroups.add(buttonGroup);
            columnPanels[columnNr].add(studentPanel);
            if(columnNr == 0) lastLeft = studentPanel;
            if(nameColumns > 1) {
                columnNr = (columnNr +1) % nameColumns;
            }
        }
        for(int i=0 ; i<nameColumns ; i++) {
            container.add(columnPanels[i]);
        }

        return container;
    }
}
