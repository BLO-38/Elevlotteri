package offlineHandling;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class StudentPanel extends JPanel {

    public final static int ABSENT = 0;
    public final static int OFFLINE = 1;
    public final static int ONLINE = 2;
    public final static int HAS_BEEN_ONLINE = 3;


    private final String studentName;
    private final JLabel nameLabel, statusLabel;
    private int status, prevStatus;
    private final JButton resetButton, leftButton, historyButton;
    private Color[] colors = {Color.WHITE,Color.RED,Color.GREEN,Color.ORANGE};
    private String[] info = {"Frånvarande","Offline","Online","Varit Online"};
    private InetSocketAddress address;
    private LinkedList<String> history = new LinkedList<>();
    private int timeCount = 0;
    private OnlineTimer timer;
    private boolean finished = false;

    public void setAddress(InetSocketAddress adr) {
        address = adr;
        status = ONLINE;
        changeDisaplay();
        history.add(adr.toString());
        history.add("Start: " + LocalDateTime.now().toString());
        statusLabel.setForeground(Color.BLACK);
        finished = false;
        timer = null;
    }

    public StudentPanel(String name) {
        status = ABSENT;
        prevStatus = ONLINE;
        this.studentName = name;
        nameLabel = new JLabel(name);
        statusLabel = new JLabel(info[status]);
        statusLabel.setBackground(colors[status]);
        statusLabel.setOpaque(true);
        resetButton = new JButton("Återställ offline");
        resetButton.addActionListener(e -> {
            status = OFFLINE;
            changeDisaplay();
        });
        leftButton = new JButton("Gått hem");
        leftButton.addActionListener(e -> {

//            status = ABSENT;
//            address = null;
//            changeDisaplay();
        });
        historyButton = new JButton("Onlinehistorik");
        historyButton.addActionListener(e -> {
            StringBuilder sb = new StringBuilder(name + "\n");
            for (String s : history) {
                sb.append(s + "\n");
            }
            JOptionPane.showMessageDialog(null,sb.toString());
        });

        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(nameLabel);
        add(statusLabel);
        add(resetButton);
        add(leftButton);
        add(historyButton);
        setBorder(new LineBorder(Color.BLACK,1));
        statusLabel.setPreferredSize(new Dimension(230,30));
    }

    public String getStudentName() {
        return studentName;
    }

    private void changeDisaplay() {
        statusLabel.setBackground(colors[status]);
        statusLabel.setText(info[status]);
    }

    public void heyONLINE(String mess) {
        if(finished) return;
        LocalDateTime dt = LocalDateTime.now();
        String time = dt.getHour() + ":" + dt.getMinute();
        history.add(mess + " " + time);
        status = ONLINE;
        changeDisaplay();
        if(timer == null)  {
            timer = new OnlineTimer(this);
            timeCount = 0;
            Toolkit.getDefaultToolkit().beep();

        } else {
            timer.resetTimer();
        }
    }

    public void tick(boolean stillOnline) {
        if(finished) return;
        System.out.println("Kvar? " + stillOnline);
        statusLabel.setText(info[status] + " ping at " + ((timeCount++)*10) + " s");
    }

    public void timerFinished() {
        if(finished) return;
        System.out.println("FINNISCH");
        timer = null;
        status = HAS_BEEN_ONLINE;
        changeDisaplay();
    }

    public void studentFinished(String mess) {
        System.out.println("Inkommande " + mess);
        history.add(mess);
        finished = true;
        statusLabel.setText(mess);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBackground(Color.BLACK);


    }
}
