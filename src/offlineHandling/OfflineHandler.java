package offlineHandling;

import databasen.NameListGetters;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;

public class OfflineHandler {

    private String klass;
    private int grp;
    private LinkedList<StudentPanel> students = new LinkedList<>();
    private String fileName;
    private DatagramSocket socket;

    private FileWriter fw;
    private BufferedWriter bw;
    String currentIP;
    //TODO
    // knapp för ljud av/på
    // Alfabetisk lista som uppdateras
    // Knapp för visa IP-adress
    // Ska JAG avsluta deras kasnke??


    public static void main(String[] args) {
        new OfflineHandler("ABC",0);
    }

    public OfflineHandler(String klass, int grp) {
        try {
            currentIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }
        fileName = JOptionPane.showInputDialog("Ange filnamn för sparade data");
        this.klass = klass;
        this.grp = grp;
        startWindow();
        startListening();
    }

    private void startListening() {
        int port = 6789;
        try {
            socket = new DatagramSocket(port);
            fw = new FileWriter("filer/" + fileName + ".txt");
            bw = new BufferedWriter(fw);
            LocalDateTime dateTime = LocalDateTime.now();
            bw.write(dateTime.toString());
            bw.newLine();
            bw.flush();
            System.out.println("Skrivit!");

            System.out.println(currentIP);
            JOptionPane.showMessageDialog(null, "Min IP: " + currentIP);

            while (true) {
                byte[] buf = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buf, 1024);
                System.out.println("Före");

                socket.receive(dp);
                InetSocketAddress sa = (InetSocketAddress) dp.getSocketAddress();
                System.out.println(sa.getAddress().toString() + "  " + sa.getPort());
                System.out.println("Efter");
//                String str = new String(dp.getData()).trim();
                System.out.println();
                System.out.println(buf.length);
                String str = new String(buf);
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
                bw.write(str);
                bw.newLine();
                bw.flush();
                System.out.println("flashat");
            }
        } catch (Exception e) {
            System.out.println("FEL 3");
            System.out.println(e.getStackTrace());
        }
    }




    public void quit() {
        System.out.println("Avslutas");
        try {
            bw.close();
            fw.close();
            socket.close();
            System.exit(0);
        } catch (Exception e) {
            System.out.println("FEL 2");
        }
    }


    private void sendKvitto(InetSocketAddress address, boolean success) {
        try {
            String mess = success ? "YES" : "NO";
            byte[] arr = mess.getBytes();
            DatagramPacket packet = new DatagramPacket(arr,arr.length,address);
            socket.send(packet);
        } catch (Exception e) {
            System.out.println(e);
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
            System.out.println(e);
        }

    }

    private boolean startWindow() {
        JFrame frame = new JFrame("Offlinekontroll " + klass + " IP: " + currentIP);
        LinkedList<String> names = NameListGetters.getNamesRegular(klass, grp);
        if (names == null || names.isEmpty()) return false;
        Collections.sort(names);
        frame.setLayout(new GridLayout((names.size() + 2) / 2, 2));
        System.out.println("222");
        for (String name : names) {
            StudentPanel studentPanel = new StudentPanel(name);
            students.add(studentPanel);
            frame.add(studentPanel);
            if (name.equals("Leon")) new TestButtons(studentPanel);
        }
        JButton endButt = new JButton("Avsluta");
        frame.add(endButt);
        endButt.addActionListener(e -> quit());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        return true;

    }
}
