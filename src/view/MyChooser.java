package view;

import model.MainHandler;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class MyChooser extends JDialog {

    private final ChooserListener asker;
    public final int BUTTON_WIDTH = 120;
    public final int BUTTON_HEIGHT = 30;
    private final LinkedList<JButton> buttons = new LinkedList<>();

    public MyChooser(LinkedList<String> options, JFrame owner, String header, boolean modal, ChooserListener a) {
        super(owner, header);
        asker = a;
        setLayout(new FlowLayout());
        setModal(modal);
        getContentPane().setBackground(MainHandler.MY_BEIGE);
        int width = getCustomWidth(options);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        for (String label : options) {
            JPanel p = new JPanel(new GridBagLayout());
            p.setOpaque(false);
            JButton button = new JButton(label);
            button.setPreferredSize(new Dimension(width, BUTTON_HEIGHT));
            p.setPreferredSize(new Dimension(width + 10, BUTTON_HEIGHT + 10));
            p.add(button);
            panel.add(p);
            button.addActionListener(e -> {
                asker.recieveResponse(e.getActionCommand());
                if(isModal()) setVisible(false);
            });
            buttons.add(button);
        }

        add(panel);
        pack();
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(owner);
    }

    private int getCustomWidth(LinkedList<String> words) {
        int longest = 0;
        for (String word : words) {
            if (word.length() > longest) longest = word.length();
        }
        return   (int) Math.ceil(longest/10.0)*BUTTON_WIDTH;
    }

    public void setColors(Color text, Color back, Font font) {
        for (JButton button : buttons) {
            button.setBackground(back);
            button.setForeground(text);
            if(font != null) button.setFont(font);
        }

    }
    public void start() {
        setVisible(true);
    }

}
