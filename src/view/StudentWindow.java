package view;

import databasen.Student;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.TreeMap;

public class StudentWindow {
    private final Student student;
    public StudentWindow(Student stud) {
        student = stud;
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout());
        JLabel header = new JLabel(student.getName() + " " + student.getKlass());
        header.setFont(new Font("Monospaced",Font.BOLD,26));
        headerPanel.add(header);
        headerPanel.setBackground(Color.WHITE);
        frame.add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel,BoxLayout.X_AXIS));

        frame.add(contentPanel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(500,100));
        infoPanel.setBorder(new LineBorder(Color.BLACK,2));
        infoPanel.setLayout(new BoxLayout(infoPanel,BoxLayout.Y_AXIS));
        TreeMap<String,String> info = new TreeMap<>();
        info.put("Grupp:",String.valueOf(student.getGroup()));
        info.put("Antal lottningar:",String.valueOf(student.getGroup()));
        info.put("Kan vinna pris:",String.valueOf(student.getGroup()));
        info.put("Kontrollfrågor:",String.valueOf(student.getGroup()));
        info.put("Deltar i prioriterat lotteri:",String.valueOf(student.getGroup()));
        info.put("Kan bli vald till grupp:",String.valueOf(student.getGroup()));
        info.put("Vanligaste bänkkompisar:",String.valueOf(student.getGroup()));
        for (String key : info.keySet()) {
            JLabel left = new JLabel(key);
            JLabel right = new JLabel(info.get(key));
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
            p.add(left);p.add(right);
            infoPanel.add(p);
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
        TreeMap<String, ActionListener> buttons = new TreeMap<>();
//        "Byt namn","Byt klass","Byt grupp","Ändra godis",
//                "Ändra kontrollfrågor","Ta bort elev","Ändra deltagande","Grupparbete?","Tillbaka"};
        buttons.put("Byt klass",e -> System.out.println("Byt klass"));
        buttons.put("Byt grupp",e -> System.out.println("Byt grupp"));
        buttons.put("Belöning?",e -> System.out.println("Få godis?"));
        buttons.put("Kontrollfrågor",e -> System.out.println("KOntrollfrågor"));
        buttons.put("Ta bort elev",e -> System.out.println("Radera"));
        buttons.put("Ändra deltagande",e -> System.out.println("Ändra delt"));
        buttons.put("Grupparbete?",e -> System.out.println("Grupparb?"));
        buttons.put("Tillbaka",e -> {
            frame.dispose();
            new SettingsMenu();
        });
        for (String key : buttons.keySet()) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER,30,5));
            JButton button = new JButton(key);
            button.setPreferredSize(new Dimension(350,20));
            button.addActionListener(buttons.get(key));
            button.setForeground(Color.WHITE);
            button.setBackground(Color.BLUE);
            button.setFocusPainted(false);
            p.add(button);
            buttonPanel.add(p);
            p.setBackground(new Color(0xCBFFA9));
        }
        contentPanel.add(infoPanel);
        contentPanel.add(buttonPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        /*
        Antal lottningar
        Kan få pris
        Kontrollfrågor
        Deltar i priot
        Kna bli vald till grupp
        Suttit flest gånger bredvid: (4 pers)
         */

    }
}
