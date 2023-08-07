package view;

import databasen.InsertHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;

public class ClassRoom3 implements Room{

    private final JFrame frame;
    private final JPanel benchesPanel;
    // private LinkedList<String> enemies;
    private final int rows, columns;
    private Bench previousBench;
    private final Bench[] benches;
    private final String[] corridors;
    protected static final int benchHeight = 120;
    protected static final int benchWidth = 140;
    protected static final int corridorhWidth = 30;


    public ClassRoom3(LinkedList<String> names,
                      String[] corridors,
                      int[] missingBenches,
                      int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.corridors = corridors;



        System.out.println("Classroom startar med antal: " + names.size());
        frame = new JFrame();
        frame.setLayout(new BorderLayout(0, 10));
        benchesPanel = new JPanel(new GridLayout(rows, columns));

        benches = new Bench[rows*columns];

        JButton button = new JButton("Ny placering");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newPlacing();
            }
        });
        JButton saveButton = new JButton("Spara placeringen");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Klickat spara!");
                System.out.println("Antal bänkar: " + benches.length);
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

        // Skapa alla bänkar
        // Markera de som ska bort
        // Sätt in alla namn på existerande bänkar
        int benchNr = 0;
        for (int i = 0; i < rows; i++) {
            JPanel benchRow = new JPanel();
            benchRow.setLayout(new BoxLayout(benchRow, BoxLayout.X_AXIS));
            for (int j = 0; j < columns; j++) {
                Bench b = new Bench(this);
                benches[benchNr] = b;
                benchNr++;
                benchRow.add(b);
                for (String corr : corridors) {
                    if (corr.length() == 0) continue;
                    if ((j + 1) == Integer.parseInt(corr)) benchRow.add(new CorridorSpace());
                }
            }
            benchesPanel.add(benchRow);
        }
        System.out.println("Bänknummer efter skapande av bänkar: " + benchNr);
        if(missingBenches != null) {
            for(int i : missingBenches) benches[i-1].setName("-");
        }
        System.out.println("SKA SYNAS NU");
        int missing = missingBenches == null ? 0 : missingBenches.length;
        if(names.size() > rows * columns - missing)
            JOptionPane.showMessageDialog(frame,"Alla får inte plats.");

        benchNr = 0;
        for (String name : names) {
            while (!benches[benchNr].doExist()) benchNr++;
            benches[benchNr].setName(name);
            benchNr++;
        }
        System.out.println("Bänknummer efter utplacering av namn: " + benchNr);
        for (Bench bench : benches) System.out.print(bench.doExist() + " - ");
        System.out.println();
        frame.add(benchesPanel);
        frame.add(buttPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setVisible(true);
    }

    private void newPlacing() {
        for (Bench bench : benches) System.out.print(bench.doExist() + " - ");
        System.out.println();
        LinkedList<String> names = new LinkedList<>();
        for (Bench b : benches) {
            if (b.getBenchName().equals("-") || b.getBenchName().equals("")) continue;
            names.add(b.getBenchName());
        }
        System.out.println(names.toString());
        System.out.println(names.size());
        Collections.shuffle(names);
        for (int i = 0; i < rows*columns; i++) {
            if (!benches[i].doExist()) continue;
            String nextname = names.poll();
            if(nextname == null) nextname = "";
            benches[i].setName(nextname);

        }

        for (Bench bench : benches) System.out.print(bench.doExist() + " - ");
        System.out.println();
    }


    private void setNewBenches() {
        LinkedList<String> benchNames = new LinkedList<>();


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


        System.out.println("KLart förb av klassrum");
        System.out.println("Bänkar: " + benchNames.size());
        System.out.println("benchnames: " + benchNames.size());

        frame.add(benchesPanel, BorderLayout.CENTER);
        //for (Bench bb : benches) System.out.println("# " + bb.getBenchName());
    }

    public void benchClicked(Bench bench) {
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
