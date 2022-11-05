package databasen;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.LinkedList;

public class GroupDialog {
    private final JDialog dialog;
    private final LinkedList<Student> students;
    private final LinkedList<ButtonGroup> buttonGroups;
    private String klass;
    private JPanel lastLeft,right;
    private final boolean useTwoColumns;
    private final int twinLimit = 10;

    public GroupDialog(JFrame parent) {
        getKlass();
        students = DatabaseHandler.getStudents(klass,0);
        useTwoColumns = students.size() > twinLimit;
        buttonGroups = new LinkedList<>();
        dialog = new JDialog(parent);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(),BoxLayout.Y_AXIS));

        JPanel p1 = new JPanel(new FlowLayout());
        JPanel p2 = namePanel4();
        JPanel p3 = new JPanel(new FlowLayout());

        JLabel text = new JLabel("Välj grupp");
        text.setFont(new Font(null, Font.PLAIN,24));
        p1.add(text);

        JButton finishButton = new JButton("Verkställ");
        finishButton.setBackground(new Color(27, 104, 5));
        finishButton.addActionListener(e -> updateGroups());
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
        if(useTwoColumns && students.size() %2 == 1) {
            System.out.println("Vi lägger till en tom ruta");
            right.add(Box.createRigidArea(new Dimension(10, lastLeft.getHeight())));
            dialog.pack();
        } else {
            System.out.println("INGEN tom ruta!");
        }

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

        JButton button = new JButton("Klart");
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

    private void updateGroups() {
        for(int i=0 ; i< students.size() ; i++) {
            int newGroup = Integer.parseInt(buttonGroups.get(i).getSelection().getActionCommand());
            students.get(i).setGroup(newGroup);
        }
        boolean result = UpdateHandler.setNewGroups(students);
        if(!result) JOptionPane.showMessageDialog(null,"Funkade inte tyvärr. Felkod updategroups");
        dialog.dispose();
    }

    private JPanel namePanel4() {
        Color backColor = new Color(80,80,80);
        int width = 150, height = 12;

        // Hela mittpanelen:
        JPanel container = new JPanel(new GridLayout(1,2));
        JPanel[] twins = new JPanel[2];

        for (int i = 0; i < 2; i++) {
            JPanel bigPanel = new JPanel();
            bigPanel.setBorder(new LineBorder(Color.BLACK,2));
            bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.Y_AXIS));

            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            titlePanel.setBackground(backColor);
            titlePanel.add(Box.createRigidArea(new Dimension(5, 0)));
            JLabel title = new JLabel("Elev");
            title.setPreferredSize(new Dimension(width, height));
            title.setForeground(Color.WHITE);
            title.setFont(new Font(null, Font.BOLD, 12));
            titlePanel.add(title);

            JLabel numbers1 = new JLabel("1");
            JLabel numbers2 = new JLabel("2");
            JLabel numbers3 = new JLabel("Ingen");
            numbers3.setForeground(Color.WHITE);
            numbers1.setForeground(Color.WHITE);
            numbers2.setForeground(Color.WHITE);
            JPanel pn1 = new JPanel(new GridBagLayout());
            JPanel pn2 = new JPanel(new GridBagLayout());
            JPanel pn3 = new JPanel(new GridBagLayout());
            pn3.setBackground(backColor);
            pn2.setBackground(backColor);
            pn1.setBackground(backColor);
            pn1.setPreferredSize(new Dimension(21, height));
            pn2.setPreferredSize(new Dimension(20, height));
            pn1.add(numbers1);
            pn2.add(numbers2);
            pn3.add(numbers3);
            titlePanel.add(pn1);
            titlePanel.add(pn2);
            titlePanel.add(pn3);
            bigPanel.add(titlePanel);
            twins[i] = bigPanel;
        }
        boolean left = true;

        for(Student student : students) {
            JPanel studentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            studentPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, backColor));
            studentPanel.add(Box.createRigidArea(new Dimension(5,0)));

            JLabel name = new JLabel(student.getName());
            name.setPreferredSize(new Dimension(width,height));
            studentPanel.add(name);
            name.setToolTipText(student.getName());

            ButtonGroup buttonGroup = new ButtonGroup();

            for (int i = 1; i < 4; i++) {
                JRadioButton r = new JRadioButton();
                int group = i%3;
                r.setActionCommand(String.valueOf(group));
                if (student.getGroup() == group) r.setSelected(true);
                buttonGroup.add(r);
                JPanel prb = new JPanel(new GridBagLayout());
                prb.add(r);
                studentPanel.add(prb);
            }
            buttonGroups.add(buttonGroup);
            if(left) {
                twins[0].add(studentPanel);
                lastLeft = studentPanel;
            }
            else twins[1].add(studentPanel);
            if(useTwoColumns) left = !left;
        }
        container.add(twins[0]);
        if(useTwoColumns) {
            container.add(twins[1]);
            right = twins[1];
        }
        return container;
    }
}


/*
Skrotade försök:
private JPanel namePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(students.size()+1,1));
        JPanel p1 = new JPanel(new GridLayout(1,2));
        JLabel title = new JLabel("Elev");
        p1.add(title);
        JLabel numbers = new JLabel("1      2   Ingen");
        JPanel p2 = new JPanel(new FlowLayout());
        p2.add(numbers);
        p1.add(p2);
        panel.add(p1);
        for(Student student : students) {
            JPanel p = new JPanel();
            p.setLayout(new GridLayout(1,2));
            JLabel name = new JLabel(student.getName());
            p.add(name);
            JPanel radioPanel = new JPanel(new FlowLayout());
            JRadioButton r1 = new JRadioButton();
            r1.setActionCommand("1");
            JRadioButton r2 = new JRadioButton();
            r2.setActionCommand("2");
            JRadioButton r3 = new JRadioButton();
            r3.setActionCommand("0");
            ButtonGroup gr = new ButtonGroup();
            System.out.println("Grupp " + student.getGroup());
            if(student.getGroup() == 1) r1.setSelected(true);
            else if(student.getGroup() == 2) r2.setSelected(true);
            else r3.setSelected(true);
            gr.add(r1);gr.add(r2);gr.add(r3);
            radioPanel.add(r1);radioPanel.add(r2);radioPanel.add(r3);
            buttonGroups.add(gr);
            p.add(radioPanel);
            panel.add(p);
        }
        return panel;

    }

    private JPanel namePanel2 () {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        int rows = students.size();
        JRadioButton[][] data = new JRadioButton[rows][5]; // 3
        // String[][] data = new String[rows][5]; // 3
        int i = 0;
        for(Student e : students) {

            data[i][0] = e.getName();
            data[i][1] = String.valueOf(e.getTotal());
            data[i][2] = String.valueOf(e.getCorrect());
            data[i][3] = String.valueOf(e.getWrong());


            data[i][2] = new JRadioButton();
                    i++;
                    }
                    String[] columnNames = {"Namn","Totalt","Rätt","Fel","Frv"};
                    JTable t = new JTable(data, columnNames);
                    t.getColumn("Totalt").setMaxWidth(50);
                    t.getColumn("Rätt").setMaxWidth(50);
                    t.getColumn("Fel").setMaxWidth(40);
                    t.getColumn("Namn").setMaxWidth(100);
                    t.getColumn("Frv").setMaxWidth(40);
                    DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
                    cr.setHorizontalAlignment(JLabel.CENTER);
                    t.getColumnModel().getColumn(1).setCellRenderer(cr);
                    t.getColumnModel().getColumn(2).setCellRenderer(cr);
                    t.getColumnModel().getColumn(3).setCellRenderer(cr);
                    t.getColumnModel().getColumn(4).setCellRenderer(cr);
                    JScrollPane scr = new JScrollPane(t);
                    scr.setPreferredSize(new Dimension(200, 500));
                    panel.add(scr);

                    return panel;

                    }
    private JPanel namePanel3() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,2));
        JPanel pLeft = new JPanel(new GridLayout(students.size()+1,1));
        JPanel pRight = new JPanel(new GridLayout(students.size()+1,3));
        JLabel title = new JLabel("Elev");
        title.setForeground(Color.YELLOW);
        title.setFont(new Font(null,Font.BOLD,12));
        JPanel pTitle = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTitle.setBackground(Color.BLACK);
        pTitle.add(Box.createRigidArea(new Dimension(5,0)));
        pTitle.add(title);
        pLeft.add(pTitle);
        JLabel numbers1 = new JLabel("1");
        JLabel numbers2 = new JLabel("2");
        JLabel numbers3 = new JLabel("Ingen");
        numbers3.setForeground(Color.YELLOW);
        numbers1.setForeground(Color.YELLOW);
        numbers2.setForeground(Color.YELLOW);
        JPanel pn1 = new JPanel(new GridBagLayout());
        JPanel pn2 = new JPanel(new GridBagLayout());
        JPanel pn3 = new JPanel(new GridBagLayout());
        pn3.setBackground(Color.BLACK);
        pn2.setBackground(Color.BLACK);
        pn1.setBackground(Color.BLACK);

        pn1.add(numbers1);
        pn2.add(numbers2);
        pn3.add(numbers3);
        pRight.add(pn1);
        pRight.add(pn2);
        pRight.add(pn3);

        for(Student student : students) {
        JLabel name = new JLabel(student.getName());
        System.out.println(student.getName());
        JPanel pname = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pname.add(Box.createRigidArea(new Dimension(5,0)));
        pname.add(name);
        pname.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GREEN));
        pLeft.add(pname);
        ButtonGroup gr = new ButtonGroup();

        for (int i = 0; i < 3; i++) {
        JRadioButton r = new JRadioButton();
        r.setActionCommand(String.valueOf(i));
        if (student.getGroup() == i) r.setSelected(true);
        gr.add(r);
        JPanel prb = new JPanel(new GridBagLayout());
        prb.add(r);
        //prb.setBorder(new LineBorder(Color.RED,1));
        prb.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GREEN));
        pRight.add(prb);
        }
        buttonGroups.add(gr);
        }
        panel.add(pLeft);
        panel.add(pRight);
        return panel;

        }

        */