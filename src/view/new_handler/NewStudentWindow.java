package view.new_handler;

import databasen.DatabaseHandler2;
import databasen.InsertHandler;
import model.MainHandler;
import view.MyColors;
import view.myIcons.MyIcons;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class NewStudentWindow {

    private static final String font = "Monospaced";
    private JTextField textField;
    private ButtonGroup buttonGroup;
    private JComboBox<String> klasses;
    private JFrame frame;
    private JPanel buttonPanel;

    public NewStudentWindow() {
        frame = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        mainPanel.setBackground(MainHandler.MY_BEIGE);

        JLabel h = new JLabel("Ny elev");
        h.setFont(new Font(font,Font.BOLD,28));
        JPanel ph = new JPanel(new FlowLayout());
        ph.setOpaque(false);
        ph.add(h);
        mainPanel.add(ph);

        JPanel pText = new JPanel(new FlowLayout());
        pText.setOpaque(false);
        JLabel nameLabel = new JLabel("Namn:");
        nameLabel.setFont(new Font(font,Font.PLAIN,14));
        pText.add(nameLabel);
        textField = new JTextField();
        textField.setPreferredSize(new Dimension(200,20));
        pText.add(textField);
        mainPanel.add(pText);

        JPanel klassPanel = new JPanel(new FlowLayout());
        klassPanel.setOpaque(false);
        JLabel klassLabel = new JLabel("Välj klass och grupp");
        klassLabel.setFont(new Font(font,Font.BOLD,16));
        klassPanel.add(klassLabel);
        mainPanel.add(klassPanel);

        JPanel klassOptionsPanel = new JPanel(new FlowLayout());
        klassOptionsPanel.setOpaque(false);
        klasses = new JComboBox<>(DatabaseHandler2.getClasses().toArray(new String[0]));
        klassOptionsPanel.add(klasses);
        mainPanel.add(klassOptionsPanel);

        buttonGroup = new ButtonGroup();
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel,BoxLayout.Y_AXIS));
        radioPanel.setOpaque(false);
        String[] groups = {"Ingen grupp","1","2"};
        for (String g : groups) {
            String text = g.length() == 1 ? "Grupp " + g : g;
            JRadioButton rb = new JRadioButton(text);
            rb.setOpaque(false);
            rb.setActionCommand(g);
            buttonGroup.add(rb);
            radioPanel.add(rb);
            if(text.startsWith("Ing")) rb.setSelected(true);
        }
        JPanel bigRadioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bigRadioPanel.setOpaque(false);
        mainPanel.add(bigRadioPanel);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        JButton saveButton = new JButton("Spara");
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(MyColors.BUTTON_GREEN);
        saveButton.addActionListener(e -> saveStudent());
        JButton cancelButton = new JButton("Avbryt");
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBackground(MyColors.BUTTON_RED);
        cancelButton.addActionListener(e -> frame.dispose());
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        mainPanel.add(buttonPanel);

        frame.add(mainPanel);
        mainPanel.setBorder(new LineBorder(Color.BLACK,5));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setUndecorated(true);
        frame.pack();
        bigRadioPanel.add(Box.createRigidArea(new Dimension(frame.getWidth()/2-50,0)));
        bigRadioPanel.add(radioPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private void saveStudent() {
        System.out.println("Spara");
        String newName = textField.getText();
        if(newName == null) return;
        newName = newName.trim();
        if(newName.isEmpty()) return;
        String gr = buttonGroup.getSelection().getActionCommand();
        int group;
        try {
            group = Integer.parseInt(gr);
        } catch (NumberFormatException nfe) {
            group = 0;
        }
        String klass = (String) klasses.getSelectedItem();
        boolean success = InsertHandler.insertStudent(newName,klass,group);
        String groupMess = group == 0 ? "" : ", grupp " + group;
        String mess = success ? newName+", "+klass+groupMess+" införd!" : "Misslyckades (kanske redan fanns?)";
        Icon icon = success ? MyIcons.CHECK_OK : MyIcons.ERROR;
        int type = success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        JLabel label = new JLabel(MyIcons.CHECK_OK);
        buttonPanel.add(label);
        buttonPanel.revalidate();
        buttonPanel.repaint();
        JOptionPane.showMessageDialog(frame, mess, null, type, icon);

        if(success) frame.dispose();
    }
}
