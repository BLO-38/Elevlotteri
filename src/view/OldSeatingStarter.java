package view;

import databasen.DeleteHandler;
import databasen.SelectHandler;
import model.MainHandler;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class OldSeatingStarter extends JFrame {
    private int startInDatabase;
    private final int ANTAL_BUTTONS = 10;
    private JButton[] buttons;
    private JLabel[] lessons;
    private final JButton nextListButton;
    private final JButton previousListButton;
    private final int mode;
    public static final int DELETE_CLASSROOMS = 0;
    public static final int LOAD_CLASSROOM = 1;
    private final String messageEnding;
    private final String cl;

    public OldSeatingStarter(int mode) {
        this(mode,null);
    }

    public OldSeatingStarter(int mode, String cl) {
        this.mode = mode;
        messageEnding = cl == null ? "alla klasser" : cl;
        this.cl = cl;
        startInDatabase = 0;
        setLayout(new FlowLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(ANTAL_BUTTONS +3,1));

        if(mode == LOAD_CLASSROOM) setupLoadButtons(mainPanel);
        if(mode == DELETE_CLASSROOMS) setupDeleteButtons(mainPanel);

        nextListButton = new JButton("Hämta fler");
        nextListButton.setBackground(Color.GREEN);
        nextListButton.addActionListener(e -> updateButtons(ANTAL_BUTTONS));
        mainPanel.add(nextListButton);

        previousListButton = new JButton("Föregående 10");
        previousListButton.setBackground(Color.RED);
        previousListButton.addActionListener(e -> updateButtons(-ANTAL_BUTTONS));
        mainPanel.add(previousListButton);

        updateButtons(0);
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupDeleteButtons(JPanel panel) {
        panel.add(new JLabel("Radera bordsplaceringar (" + messageEnding + ")"));

        buttons = new JButton[ANTAL_BUTTONS];
        lessons = new JLabel[ANTAL_BUTTONS];

        for (int i = 0; i < ANTAL_BUTTONS; i++) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT,10,2));
            p.setBorder(new LineBorder(Color.BLACK,1));
            JButton b = new JButton("X");
            b.setBackground(MainHandler.MY_RED);
            b.setForeground(Color.WHITE);
            b.setMargin(new Insets(2,4,2,4));
            buttons[i] = b;
            p.add(b);
            b.addActionListener(e -> {
                try {
                    int id = Integer.parseInt(e.getActionCommand());
                    DeleteHandler.deleteSeats(id);
                    updateButtons(0);
                } catch (Exception exep) {
                    JOptionPane.showMessageDialog(null,"Fel nummer 1288");
                }
            });
            lessons[i] = new JLabel();
            p.add(lessons[i]);

            panel.add(p);
        }
    }

    private void setupLoadButtons(JPanel panel) {
        panel.add(new JLabel("Välj en placering ("+ messageEnding + ")"));
        buttons = new JButton[ANTAL_BUTTONS];
        for (int i = 0; i < ANTAL_BUTTONS; i++) {
            buttons[i] = new JButton();
            buttons[i].addActionListener(e -> {
                String loadedBenchData = e.getActionCommand();
                createClassroom(loadedBenchData);
            });
            panel.add(buttons[i]);
        }
    }

    private void updateButtons(int deltaLimit) {
        int prevValue = startInDatabase;
        startInDatabase += deltaLimit;
        if(startInDatabase < 0) startInDatabase = 0;
        previousListButton.setEnabled(startInDatabase > 0);
        String[][] buttonDataTable = SelectHandler.getBenches(cl, ANTAL_BUTTONS, startInDatabase);
        if(buttonDataTable[0][2] == null) {
            JOptionPane.showMessageDialog(null,"Fanns inga fler");
            startInDatabase = prevValue;
            nextListButton.setEnabled(false);
            return;
        }
        if (mode == LOAD_CLASSROOM) {
            for (int i = 0; i < ANTAL_BUTTONS; i++) {
                buttons[i].setText(buttonDataTable[i][0]);
                buttons[i].setActionCommand(buttonDataTable[i][1]);
                buttons[i].setEnabled(buttonDataTable[i][0] != null);
            }
        }
        if (mode == DELETE_CLASSROOMS) {
            for (int i = 0; i < ANTAL_BUTTONS; i++) {
                buttons[i].setBackground(MainHandler.MY_RED);
                buttons[i].setActionCommand(buttonDataTable[i][2]);
                String text = buttonDataTable[i][0];
                buttons[i].setEnabled(text != null);
                lessons[i].setText(text);
            }
        }
        nextListButton.setEnabled(buttonDataTable[ANTAL_BUTTONS-1][2] != null);
        pack();
        repaint();
    }

    private void createClassroom(String data) {
        String[] dataParts = data.split("qqq");

        String[] roomDimensions = dataParts[0].split("#");
        int rows = Integer.parseInt(roomDimensions[0]);
        int columns = Integer.parseInt(roomDimensions[1]);

        String[] corridors = dataParts[1].split("#");
        LinkedList<Integer> corrList = new LinkedList<>();
        System.out.println(corridors.length);
        System.out.println(Arrays.toString(corridors));
        if(!(corridors.length == 1 && corridors[0].isEmpty()))
            for (String c : corridors) corrList.add(Integer.parseInt(c));

        String[] names1 = dataParts[2].split("#");
        LinkedList<String> allNames = new LinkedList<>();
        Collections.addAll(allNames,names1);

        LinkedList<String> friendList = new LinkedList<>();
        if(dataParts.length > 3) {
            String[] friends = dataParts[3].split("#");
            Collections.addAll(friendList,friends);
        }

        LinkedList<String> firstRowList = new LinkedList<>();
        if(dataParts.length > 4) {
            String[] firstRow = dataParts[4].split("#");
            Collections.addAll(firstRowList, firstRow);
        }
        if(!friendList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String n : friendList) sb.append(n).append(",");
            String currentFriendsToShow = sb.toString();
            NewFriendLoop:
            while (true) {
                currentFriendsToShow = JOptionPane.showInputDialog(null, "Det fanns bänkkompisar enligt texten i rutan. Behåll eller ändra.", currentFriendsToShow);
                if(currentFriendsToShow == null) break;
                if(currentFriendsToShow.isEmpty()) {
                    friendList = new LinkedList<>();
                    break;
                }

                String[] newFriends = currentFriendsToShow.split(",");
                for (String n : newFriends) {
                    if (!allNames.contains(n)) {
                        JOptionPane.showMessageDialog(null, n + " fanns ej eller var felstavat, försök igen.");
                        continue NewFriendLoop;
                    }
                }
                friendList = new LinkedList<>();
                Collections.addAll(friendList,newFriends);
                System.out.println("Nya vänner blev: " + friendList);
                break;
            }

        }
        new ClassRoom4(allNames,corrList,friendList,firstRowList,null,null,rows,columns,false);
    }

}
