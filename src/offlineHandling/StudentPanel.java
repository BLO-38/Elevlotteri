package offlineHandling;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.util.LinkedList;

//@SuppressWarnings("FieldCanBeLocal")
public class StudentPanel extends JPanel {

    public final static int ABSENT = 0;
    public final static int OFFLINE = 1;
    public final static int ONLINE = 2;
    public final static int HAS_BEEN_ONLINE = 3;
    public final static int ABORTED = 4;
    private static boolean soundActive = false;

    public static void setSoundActive(boolean active) {
        soundActive = active;
    }

    private final String studentName;
    private final JLabel nameLabel, statusLabel;
    private int status;
    private final JButton resetButton, leftButton, historyButton;
    private Color[] colors = {Color.WHITE,Color.RED,Color.GREEN,Color.ORANGE,Color.lightGray};
    private String[] info = {"Frånvarande","Offline","Online","Varit Online","Avbrutit"};
    private InetSocketAddress address;
    private LinkedList<String> history = new LinkedList<>();
    private int timeCount = 0;
    private OnlineTimer timer;
    private boolean finished = false;
    private int[] startTime, stopTime;
    private String lastTime = "";
    private String prevText = "";
    private int offlineCount = 0;


    public StudentPanel(String name) {
        startTime = new int[2];
        stopTime = new int[2];
        status = ABSENT;
        this.studentName = name;
        nameLabel = new JLabel(name);
        nameLabel.setPreferredSize(new Dimension(100,30));
        statusLabel = new JLabel(info[status]);
        statusLabel.setBackground(colors[status]);
        statusLabel.setOpaque(true);
        resetButton = new JButton("Återställ offline");
        resetButton.addActionListener(e -> {
            if(status == ABSENT) return;
            status = OFFLINE;
            changeDisaplay();
        });
        leftButton = new JButton("Avbrott");
        leftButton.addActionListener(e -> {
            int ans = JOptionPane.showConfirmDialog(null,"Är du saker?","Varning",JOptionPane.YES_NO_OPTION);
            if(ans != JOptionPane.YES_OPTION) return;
            status = ABORTED;
            address = null;
            changeDisaplay();
        });
        historyButton = new JButton("Historik");
        historyButton.addActionListener(e -> {
            StringBuilder sb = new StringBuilder(name + "\n");
            for (String s : history) {
                sb.append(s).append("\n");
            }
            JOptionPane.showMessageDialog(this.getTopLevelAncestor(),sb.toString());
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

    public void setAddress(InetSocketAddress adr) {
        finished = false;
        timer = null;
        startTime[0] = LocalDateTime.now().getHour();
        startTime[1] = LocalDateTime.now().getMinute();
        address = adr;
        status = ONLINE;
        changeDisaplay();
        history.add(address.getAddress().toString() + "\n");
        String mins1 = startTime[1] < 10 ? "0" + startTime[1] : "" + startTime[1];
        history.add("Starttid " + startTime[0] + ":" + mins1 + "\n");
        statusLabel.setForeground(Color.BLACK);
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
        int min = dt.getMinute();
        String mins1 = min < 10 ? "0" + min : "" + min;

        String time = dt.getHour() + ":" + mins1;
        if(!time.equals(lastTime)) {
            history.add("Online " + time);
            lastTime = time;
        }

        status = ONLINE;
        statusLabel.setBackground(colors[status]);
        if(timer == null)  {
            timeCount = 0;
            timer = new OnlineTimer(this);
            if(soundActive) Toolkit.getDefaultToolkit().beep();
        } else {
            timer.setStillOnline();
        }
    }

    public void tick(boolean stillOnline) {
        if(finished) return;
        System.out.println("Kvar? " + stillOnline);
        String newText = "Online i " + (timeCount*5) + " s";
        timeCount++;

        if(stillOnline) {
            prevText = "";
            offlineCount = 0;
        }
        else {
            offlineCount++;
            if(offlineCount>2) prevText = prevText + ".";
        }
        statusLabel.setText(newText + prevText);
    }

    public void timerFinished() {
        if(finished) return;
        timer = null;
        System.out.println("FINNISCH");
        status = HAS_BEEN_ONLINE;
        changeDisaplay();
    }

    public void studentFinished(String mess) {
        finished = true;
        stopTime[0] = LocalDateTime.now().getHour();
        stopTime[1] = LocalDateTime.now().getMinute();

        System.out.println("Inkommande " + mess);
        history.add(mess + "\n");
        String mins1 = stopTime[1] < 10 ? "0" + stopTime[1] : "" + stopTime[1];
        history.add("Sluttid: " + stopTime[0] + ":" + mins1 + "\n");
        int minDiff = stopTime[1] - startTime[1];
        int hrDiff = stopTime[0] - startTime[0];
        if(minDiff<0) {
            hrDiff--;
            minDiff+=60;
        }
        String mins = minDiff < 10 ? "0" + minDiff : "" + minDiff;
        history.add("Tidsdiff: " + hrDiff + ":" + mins + "\n");

        statusLabel.setText(mess);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBackground(Color.BLACK);


    }
}
