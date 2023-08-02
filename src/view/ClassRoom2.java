package view;

import databasen.InsertHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;

public class ClassRoom2 {

    private static JFrame frame;
    private JPanel benchesPanel;
    private final LinkedList<String> names;
    private LinkedList<String> enemies;
    private final int rows, columns;
    private static Bench previousBench;
    private final static LinkedList<Bench> benches = new LinkedList<>();

    public ClassRoom2(LinkedList<String> names,
                      LinkedList<Integer> corridors,
                      int rows, int columns) {
        this.names = names;
        this.rows = rows;
        this.columns = columns;

        frame = new JFrame();
        frame.setLayout(new BorderLayout(0, 10));

        JButton button = new JButton("Ny placering");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(benchesPanel);
                setNewBenches();
                frame.revalidate();
            }
        });
        JButton saveButton = new JButton("Spara placeringen");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder sb = new StringBuilder(rows+"#"+columns+"qqq");
                for(Bench b : benches) sb.append(b.getBenchName()).append("#");
                InsertHandler.saveBenches(sb.toString());
            }
        });


        JPanel buttPanel = new JPanel(new FlowLayout());
        JPanel wbPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel whiteboard = new JLabel("W H I T E B O A R D");
        whiteboard.setFont(new Font(Font.MONOSPACED, Font.BOLD, 34));
        wbPanel.add(whiteboard);
        frame.add(wbPanel, BorderLayout.NORTH);
        buttPanel.add(button);
        buttPanel.add(saveButton);
        setNewBenches();
        frame.add(buttPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setVisible(true);
    }


    private void setNewBenches() {

        benchesPanel = new JPanel(new GridLayout(rows, columns));

        LinkedList<String> benchNames = new LinkedList<>(names);
        // Collections.shuffle(benchNames);





        while (benchNames.size() < (rows*columns)) {
            benchNames.add("");
        }

        // Till sist: korridorer
        LinkedList<Integer> korre = new LinkedList<>();
        LinkedList<Integer> korreAlla = new LinkedList<>();
        korre.add(2);
        korre.add(5);
        int count = 0;
        for(int j=0 ; j<korre.size() ; j++){
            int a = korre.get(j);
            while (count < rows*columns) {
                korreAlla.add(a);
                count += columns;
            }
        }

//        int ko = columns + korre.size();
//
//        benchesPanel = new JPanel(new GridLayout(rows, columns + korre.size()));
//        System.out.println("Benchezz " + ko);
//        for (int j=0 ; j<rows*korre.size() ; j++) {
//            benchNames.add("Kalle");
//        }

        for (String name : benchNames) {
            Bench b = new Bench(name);
            benchesPanel.add(b);
            benches.add(b);
            System.out.println(name);
        }
        frame.add(benchesPanel, BorderLayout.CENTER);
        for (Bench bb : benches) System.out.println("# " + bb.getBenchName());
    }

    public static void benchClicked(Bench bench) {
        if (previousBench == null) {
            previousBench = bench;
            previousBench.toggleRedName(true);

        } else {
            String clickedName = bench.getBenchName();
            bench.setName(previousBench.getBenchName());
            previousBench.setName(clickedName);
            previousBench.toggleRedName(false);
            bench.repaint();
            previousBench = null;
            for (Bench bb : benches) System.out.println("## " + bb.getBenchName());
        }
    }
}
