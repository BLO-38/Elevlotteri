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

public class ClassRoom2 implements Room{

    private final JFrame frame;
    private final JPanel benchesPanel;
    private final LinkedList<String> names;
    // private LinkedList<String> enemies;
    private final int rows, columns;
    private Bench previousBench;
    private final LinkedList<Bench> benches = new LinkedList<>();
    private final String[] corridors;
    protected static final int benchHeight = 120;
    protected static final int benchWidth = 140;
    protected static final int corridorhWidth = 30;


    public ClassRoom2(LinkedList<String> names,
                      String[] corridors,
                      int rows, int columns) {
        this.names = names;
        this.rows = rows;
        this.columns = columns;
        this.corridors = corridors;

        System.out.println("Classroom startar med antal: " + names.size());
        frame = new JFrame();
        frame.setLayout(new BorderLayout(0, 10));
        benchesPanel = new JPanel(new GridLayout(rows, columns));

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
                System.out.println("Klickat spara!");
                System.out.println("Antal bänkar: " + benches.size());
                StringBuilder sb = new StringBuilder(rows+"#"+columns+"qqq");
                for(String c : corridors) sb.append(c).append("#");
                sb.append("qqq");
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
        LinkedList<String> benchNames = new LinkedList<>(names);


        System.out.println("Benchnames vid start av setNewBencjes: " + benchNames.size());

        while (benchNames.size() < (rows*columns)) {
            benchNames.add("");
        }
        System.out.println("Benchnames efter påfyllning : " + benchNames.size());

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

//        for (String name : benchNames) {
//            Bench b = new Bench(name);
//            benchesPanel.add(b);
//            benches.add(b);
//            System.out.println(name);
//        }
        System.out.println("SIZE " + benchNames.size());

        for (int i=0 ; i<benchNames.size() ; i++) {
            // System.out.println("i = " + i);
            JPanel benchRow = new JPanel();
            benchRow.setLayout(new BoxLayout(benchRow,BoxLayout.X_AXIS));

            for (int j = 0; j < columns; j++) {
                Bench b = new Bench(benchNames.poll(),0, this);
                benchRow.add(b);
                benches.add(b);

                for(String corr : corridors) {
                    if(corr.length() == 0) continue;
                    if((j+1) == Integer.parseInt(corr)) benchRow.add(new CorridorSpace());
                }

            }
            benchesPanel.add(benchRow);
        }
        System.out.println("KLart förb av klassrum");
        System.out.println("Bänkar: " + benchNames.size());
        System.out.println("benchnames: " + benchNames.size());

        frame.add(benchesPanel, BorderLayout.CENTER);
        //for (Bench bb : benches) System.out.println("# " + bb.getBenchName());
    }

    public void benchClicked(Bench bench) {
        if (previousBench == null) {
            previousBench = bench;
            previousBench.setMarked(true);

        } else {
            String clickedName = bench.getBenchName();
            bench.setName(previousBench.getBenchName());
            previousBench.setName(clickedName);
            previousBench.setMarked(false);
            bench.repaint();
            previousBench = null;
            //for (Bench bb : benches) System.out.println("## " + bb.getBenchName());
        }
    }

    public int[] getBenchDimensions() {
        int bWidth = (benchesPanel.getWidth()-corridors.length*corridorhWidth)/columns;
        int bHeight = benchesPanel.getHeight()/rows;
        return new int[] {bWidth,bHeight};
    }

    private void prepareNames() {

    }
}
