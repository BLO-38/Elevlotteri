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
    private final String[] names;
    private final LinkedList<Integer> corridors;
    protected static final int corridorhWidth = 30;
    private LinkedList<String[]> pairs;
    private LinkedList<Integer> missingBenches;
    private LinkedList<Integer> forbiddenBenches;


    public ClassRoom3(String[] names,
                      String[] corrs,
                      LinkedList<String[]> pairs,
                      LinkedList<String> frontRowNames,
                      int rows, int columns) {
        this(names,corrs,rows,columns);
        this.pairs = pairs;
        newSeatingWithFriends();

    }
    public ClassRoom3(String[] names,
                      String[] corrs,
                      int rows, int columns) {
        this.names = names;
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

        missingBenches = new LinkedList<>();
        forbiddenBenches = new LinkedList<>();
        System.out.println(rows*columns);
        System.out.println(names.length);
        for (int i=0 ; i<names.length ; i++){
            if(names[i].equals("x")) forbiddenBenches.add(i);
            else if(names[i].equals("-")) missingBenches.add(i);
        }
        System.out.println("Miss: " + missingBenches);
        System.out.println("Ej använd: " + forbiddenBenches);

        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout(0, 10));
        benchesPanel = new JPanel(new GridLayout(rows, columns));

        benches = new Bench[rows*columns];

        JButton button = new JButton("Ny placering");
        button.addActionListener(e -> newPlacing());

        JButton saveButton = new JButton("SPL");
        saveButton.addActionListener(e -> {
            // Till clasroom4
            StringBuilder sb = new StringBuilder(rows+"#"+columns+"qqq");
            for(int c : this.corridors) sb.append(c).append("#");
            sb.append("qqq");
            for(Bench b : benches) sb.append(b.getBenchName()).append("#");
            sb.append("qqq");
            /*
            for(String fr : friiends) sb.append(fr).append("#");
            sb.append("qqq");
            for(String firstR : firstRownames) sb.append(firstR).append("#");

             */

            InsertHandler.saveBenches(sb.toString());
        });
        JButton saveNeighborsButton = new JButton("SG");
        saveNeighborsButton.addActionListener(e ->  saveNeighbors() );

        JPanel buttPanel = new JPanel(new FlowLayout());
        JPanel wbPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel whiteboard = new JLabel("W H I T E B O A R D");
        whiteboard.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
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
                Bench b = new Bench(this,0);
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
            if (b.getStatus() == Bench.OCCUPIED) names.add(b.getBenchName());
        }

        Collections.shuffle(names);

        for (int i = 0; i < rows*columns; i++) {
            if (benches[i].getStatus() != Bench.FREE) continue;
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
        if(result)
            JOptionPane.showMessageDialog(null, "Nuvarande grannar sparade!", "Resultat", JOptionPane.INFORMATION_MESSAGE);
    }
    private void newSeatingWithFriends() {

        LinkedList<String> namesLeft = new LinkedList<>();
        for (String name : names) if(name.length() > 1) namesLeft.add(name);
        int antalNamnTotalt = namesLeft.size();

        // Var finns korridorer? (för att ta bort dubbletter i korridorlistan)
        boolean[] corrPositions = new boolean[columns+1];
        for (int c : corridors) if( c < columns ) corrPositions[c] = true;

        // Vilka ordningsnummer i klassrummer har enkel- resp dubbelrader?
        LinkedList<Integer> singleColumnsNumbers = new LinkedList<>();
        LinkedList<Integer> pairColumnsNumbers = new LinkedList<>();
        int inRow = 0;
        int columnCount = 0;
        for (int i = 1; i < corrPositions.length; i++) {
            if(corrPositions[i] || inRow == 1) {
                if(inRow==0) singleColumnsNumbers.add(columnCount++);
                else pairColumnsNumbers.add(columnCount++);
                inRow = 0;
            } else inRow++;
        }

        int totalPairCapacity = rows*pairColumnsNumbers.size();
        int totalSingleCapacity = rows*singleColumnsNumbers.size();
        if(pairs.size() > totalPairCapacity) {
            JOptionPane.showMessageDialog(null,"Dubbelplatserna räcker inte. Ändra dina val");
            return;
        }

        // Listor för rätt namn på rätt ställe
        LinkedList<String> singleNames = new LinkedList<>();
        LinkedList<String> pairNames = new LinkedList<>();
        // Lägg dit kompisarna:
        for (String[] pair : pairs) {
            String nextFriends = pair[0] + "," + pair[1] + ",";
            pairNames.add(nextFriends);
            if(!(namesLeft.remove(pair[0]) && namesLeft.remove(pair[1]))) JOptionPane.showMessageDialog(null,"Oväntat fel");
        }

        // Vi ska inte använda fler singelbänkar än nödvändigt:
        int usedSingles = antalNamnTotalt/columns*singleColumnsNumbers.size();

        Collections.shuffle(namesLeft);
        for (int i = 0; i < usedSingles; i++) {
            singleNames.add(namesLeft.pop() + ",");
            if(namesLeft.isEmpty()) break;
        }

        // Vi fyller på övriga, helst i dubbelbänkar om det finns sådana kvar:
        String prevName = null;
        while (!namesLeft.isEmpty()) {
            if(pairNames.size() == totalPairCapacity) singleNames.add(namesLeft.pop() + ",");
            else if(prevName == null) prevName = namesLeft.pop();
            else {
                pairNames.add(prevName + "," + namesLeft.pop() + ",");
                prevName = null;
            }
        }
        if(prevName != null) singleNames.add(prevName + ",");
        Collections.shuffle(pairNames);

        // Vilka kolumner är dubbelbänkar?
        boolean[] isPairColumns = new boolean[singleColumnsNumbers.size()+pairColumnsNumbers.size()];
        for(int col : pairColumnsNumbers) isPairColumns[col] = true;

        // Dags att skapa hela klassrumslistan:
        StringBuilder finalNames = new StringBuilder();
        int columnSetCount = 0;
        while (!singleNames.isEmpty() || !pairNames.isEmpty()) {
            String toAdd;
            if(isPairColumns[columnSetCount % isPairColumns.length])
                toAdd = pairNames.isEmpty() ? ", ," : pairNames.pop();
            else toAdd = singleNames.isEmpty() ? ", " : singleNames.pop();
            finalNames.append(toAdd);
            columnSetCount++;
        }

        String[] finalNameArray = finalNames.toString().split(",");
        int i;
        for( i=0;i<benches.length;i++) {
            String temp = i< finalNameArray.length ? finalNameArray[i] : "";
            benches[i].setName(temp);
        }
    }
}
