package view;

import databasen.DatabaseHandler;
import databasen.InsertHandler;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class ClassRoom3 implements Room{

    private final JPanel benchesPanel;
    private final int rows, columns;
    private Bench previousBench;
    private final Bench[] benches;
    private final LinkedList<Integer> corridors;
    protected static final int corridorhWidth = 30;


    public ClassRoom3(String[] names,
                      String[] corrs,
                      int rows, int columns) {

        this.rows = rows;
        this.columns = columns;

        corridors = new LinkedList<>();

        for (String s : corrs) {
            String c = s.trim();
            try {
                int corr = Integer.parseInt(c);
                if (corr <= columns && corr >= 0) corridors.add(corr);
            } catch (NumberFormatException e) {
                System.out.println(e);
            }
        }

        Collections.sort(corridors);

        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout(0, 10));
        benchesPanel = new JPanel(new GridLayout(rows, columns));

        benches = new Bench[rows*columns];

        JButton button = new JButton("Ny placering");
        button.addActionListener(e -> newPlacing());

        JButton saveButton = new JButton("Spara placeringen");
        saveButton.addActionListener(e -> {
            StringBuilder sb = new StringBuilder(rows+"#"+columns+"qqq");
            for(int c : this.corridors) sb.append(c).append("#");
            sb.append("qqq");
            for(Bench b : benches) sb.append(b.getBenchName()).append("#");
            InsertHandler.saveBenches(sb.toString());
        });
        JButton saveNeighborsButton = new JButton("Spara grannar");
        saveNeighborsButton.addActionListener(e ->  saveNeighbors() );

        JPanel buttPanel = new JPanel(new FlowLayout());
        JPanel wbPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel whiteboard = new JLabel("W H I T E B O A R D");
        whiteboard.setFont(new Font(Font.MONOSPACED, Font.BOLD, 34));
        wbPanel.add(whiteboard);
        frame.add(wbPanel, BorderLayout.NORTH);
        buttPanel.add(button);
        buttPanel.add(saveButton);
        buttPanel.add(saveNeighborsButton);

        int benchNr = 0;
        for (int i = 0; i < rows; i++) {
            LinkedList<Integer> tempCorr = new LinkedList<>(corridors);
            JPanel benchRow = new JPanel();
            benchRow.setLayout(new BoxLayout(benchRow, BoxLayout.X_AXIS));

            int nextCorr = tempCorr.size()>0 ? tempCorr.pop() : -1;
            for (int j = 0; j < columns; j++) {
                Bench b = new Bench(this);
                benches[benchNr] = b;
                benchNr++;
                if(j==0)
                    while (nextCorr == 0) {
                        benchRow.add(new CorridorSpace());
                        nextCorr = tempCorr.pop();
                    }

                benchRow.add(b);
                while (j+1 == nextCorr) {
                    benchRow.add(new CorridorSpace());
                    nextCorr = tempCorr.size() > 0 ? tempCorr.pop() : -1;
                }


            }
            benchesPanel.add(benchRow);
        }

        benchNr = 0;
        if(names != null) {
            for (String name : names) {
                benches[benchNr].setName(name);
                benchNr++;
            }
        }

        frame.add(benchesPanel, BorderLayout.CENTER);
        frame.add(buttPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setVisible(true);
    }

    private void newPlacing() {
        LinkedList<String> names = new LinkedList<>();
        for (Bench b : benches) {
            if (b.getStatus() != Bench.NORMAL || b.getBenchName().equals("")) continue;
            names.add(b.getBenchName());
        }

        Collections.shuffle(names);

        for (int i = 0; i < rows*columns; i++) {
            if (benches[i].getStatus() != Bench.NORMAL) continue;
            String nextname = names.poll();
            if(nextname == null) nextname = "";
            benches[i].setName(nextname);

        }
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
        }
    }

    public int[] getBenchDimensions() {
        int bWidth = (benchesPanel.getWidth()-corridors.size()*corridorhWidth)/columns;
        int bHeight = benchesPanel.getHeight()/rows;
        return new int[] {bWidth,bHeight};
    }


    private void saveNeighbors() {
        LinkedList<String[]> neighbors = new LinkedList<>();
        for (int plats = 0; plats < rows*columns; plats++) {
            if(plats%columns == columns-1) continue;
            if(benches[plats].getBenchName().length() < 2) continue;
            if(benches[plats+1].getBenchName().length() < 2) continue;
            if(corridors.contains(plats%columns+1)) continue;
            String[] pair = {benches[plats].getBenchName(),benches[plats+1].getBenchName()};
            neighbors.add(pair);
        }
        boolean result = InsertHandler.insertNeighbors(neighbors);
        String mess = result ? "Bra" : "Dåligt";
        System.out.println("Det gick " + mess);
    }
}
