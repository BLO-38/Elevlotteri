package offlineHandling;

import databasen.DatabaseHandler2;
import databasen.NameListGetters;
import model.MainHandler;
import view.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;

public class OfflineHandler extends Thread {

    private final String klass;
    private final int grp;
    private final LinkedList<StudentPanel> students = new LinkedList<>();
    private final String fileName;
    private DatagramSocket socket;

    private BufferedWriter bw;
    private String currentIP;
    private boolean soundActive = false;
    private JButton soundButton;
    private boolean doSave = true;
    private final MainMenu mainMenu;
    private JFrame frame;
    //TODO
    // Alfabetisk lista som uppdateras
    // Knapp för visa IP-adress
    // Ska JAG avsluta deras kasnke??
    // kolla om tiden stämmer
    // Knapp för avbruten session
    // till historiken: ta ej med likadana


    public OfflineHandler(String klass, int grp, MainMenu mm) {
        mainMenu = mm;
        try {
            currentIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }
        fileName = JOptionPane.showInputDialog("Ange filnamn för sparade data.\nLämna tomt om du ej behöver spara.");
        if(fileName == null || fileName.isEmpty()) doSave = false;
        this.klass = klass;
        this.grp = grp;
        if(!startWindow()) return;
        start();
    }

    @Override
    public void run() {
        startListening();
    }

    private void startListening() {
        int port = 6789;
        try {
            socket = new DatagramSocket(port);
            if(doSave) {
                FileWriter fw = new FileWriter(DatabaseHandler2.baseURL + "offlinefiler/" + fileName + ".txt");
                bw = new BufferedWriter(fw);
                LocalDateTime dateTime = LocalDateTime.now();
                bw.write(dateTime.toString());
                bw.newLine();
                bw.flush();
                System.out.println("Skrivit!");
            }

            System.out.println(currentIP);
            JOptionPane.showMessageDialog(null, "Min IP: " + currentIP);

            while (true) {
                byte[] buf = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buf, 1024);
                System.out.println("Före");
                System.out.println(Thread.currentThread().getName());
                socket.receive(dp);
                InetSocketAddress sa = (InetSocketAddress) dp.getSocketAddress();
                System.out.println(sa.getAddress().toString() + "  " + sa.getPort());
                System.out.println("Efter");
//                String str = new String(dp.getData()).trim();
                System.out.println();
                System.out.println(buf.length);
                String str = new String(buf).trim();
//                String str = new String(dp.getData());
//                String str = new String(dp.getData(), 0, dp.getLength());
                System.out.println(str);
                boolean success = false;
                if (str.startsWith("ssttaarrtt")) {
                    int endInd = str.indexOf(" ");
                    String newName = str.substring(10,endInd);
                    for (StudentPanel studentPanel : students) {
                        if (studentPanel.getStudentName().equals(newName)) {
                            studentPanel.setAddress(sa);
                            success = true;
                        }
                    }
                    sendKvitto(sa,success);
                }
                else if(str.startsWith("qquuiitt")) {
                    int endInd = str.indexOf(" ");
                    String tempName = str.substring(8,endInd);
                    for (StudentPanel studentPanel : students) {
                        if (studentPanel.getStudentName().equals(tempName)) {
                            studentPanel.studentFinished(str.substring(8));
                            sendQuitConfirm(sa);
                            break;
                        }
                    }
                }
                else {
                    int endInd = str.indexOf(" ");
                    String tempName = str.substring(0,endInd);
                    for (StudentPanel studentPanel : students) {
                        if (studentPanel.getStudentName().equals(tempName)) {
                            studentPanel.heyONLINE(tempName);
                            break;
                        }
                    }
                }
                if(doSave) {
                    bw.write(str);
                    bw.newLine();
                    bw.flush();
                    System.out.println("flashat");
                }
            }
        } catch (SocketException sex) {
            System.out.println("Closad");
        } catch (Exception e) {
            System.out.println("FEL 3");
            System.out.println(e.getMessage());
        }
    }

    public void quit() {
        int ans = JOptionPane.showConfirmDialog(frame,"Vill du verkligen avsluta detta??");
        if(ans != JOptionPane.OK_OPTION) return;
        System.out.println("Avslutas");
        try {
            if(doSave) bw.close();
            socket.close();
            frame.dispose();
        } catch (Exception e) {
            System.out.println("FEL 2");
        }
        mainMenu.offlineFinished();
    }


    private void sendKvitto(InetSocketAddress address, boolean success) {
        try {
            String mess = success ? "YES" : "NO";
            byte[] arr = mess.getBytes();
            DatagramPacket packet = new DatagramPacket(arr,arr.length,address);
            socket.send(packet);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void sendQuitConfirm(InetSocketAddress address) {
        try {
            String mess = "overandout";
            byte[] arr = mess.getBytes();
            DatagramPacket packet = new DatagramPacket(arr,arr.length,address);
            Thread.sleep(3000);
            socket.send(packet);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private boolean startWindow() {
        LinkedList<String> names = NameListGetters.getNamesRegular(klass, grp);
        if (names == null || names.isEmpty()) return false;

        frame = new JFrame("Offlinekontroll " + klass + "  IP: " + currentIP);
        frame.setLayout(new BorderLayout());
        JPanel controlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel allStudentsPanel = new JPanel(new GridLayout((names.size() + 1) / 2, 2));
        frame.add(controlButtons, BorderLayout.NORTH);
        frame.add(allStudentsPanel, BorderLayout.CENTER);

        Collections.sort(names);
        for (String name : names) {
            StudentPanel studentPanel = new StudentPanel(name);
            students.add(studentPanel);
            allStudentsPanel.add(studentPanel);
        }
        JButton endButt = new JButton("Avsluta");
        endButt.setForeground(Color.WHITE);
        endButt.setBackground(MainHandler.MY_RED);
        controlButtons.add(endButt);
        endButt.addActionListener(e -> quit());
        soundButton = new JButton();
        soundButton.setFocusPainted(false);
        soundButton.setForeground(Color.WHITE);
        soundButton.addActionListener(e -> toggleSound());
        soundButton.doClick();
        controlButtons.add(soundButton);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        return true;

    }

    private void toggleSound() {
        soundActive = !soundActive;
        Color c = soundActive ? MainHandler.MY_GREEN : Color.BLACK;
        String text = soundActive ? "Ljudet är på" : "Ljudet är av";
        StudentPanel.setSoundActive(soundActive);
        soundButton.setText(text);
        soundButton.setBackground(c);
    }
}
