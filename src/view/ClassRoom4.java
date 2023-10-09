package view;

import databasen.InsertHandler;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class ClassRoom4 implements Room{

    private final JPanel benchesPanel;
    private final int rows, columns;
    private Bench previousBench;
    private final Bench[] benches;
    protected static final int corridorhWidth = 30;
    private LinkedList<String> allNnames = new LinkedList<>();
    private LinkedList<String> remainingNames;
    private final String[] benchFriends;
    private final LinkedList<String> firstRowNames = new LinkedList<>();
    private LinkedList<Integer> missingBenches = new LinkedList<>();
    private LinkedList<Integer> forbiddenBenches = new LinkedList<>();
    private final LinkedList<Integer> corridors = new LinkedList<>();
    private int[] corridorWidths;
    private LinkedList<Integer> availablePairSeats = new LinkedList<>();
    private int totalCorridors;


    public ClassRoom4(String[] names, Integer[] corrs, String[] friends, String[] frontRow, Integer[] forbidden, Integer[] missing, int rows, int columns) {

        // Gör listor:
        Collections.addAll(allNnames,names);
        benchFriends = friends;
        Collections.addAll(firstRowNames,frontRow);
        Collections.addAll(missingBenches,missing);
        Collections.addAll(forbiddenBenches,forbidden);

        this.rows = rows;
        this.columns = columns;

        // Fixa korridorer:
        // Gör korridorer till en sorterad lista med godkända tal
        // och sätt bredden:
        corridorWidths = new int[columns+1];
        // Först och sist har vi korridor (=vägg)
        corridorWidths[0] = 1;
        corridorWidths[columns] = 1;
        // trimma parsa kolla område lägg till
        for (int corr : corrs) {
            if (corr <= columns && corr >= 0) {
                corridors.add(corr);
                corridorWidths[corr]++;
                totalCorridors++;
            }
        }
        System.out.println("Totalcorridors: " + totalCorridors);
        System.out.println("Samma? " + corridors.size());
        Collections.sort(corridors);
        System.out.println(Arrays.toString(corridorWidths));


        System.out.println("Antal platser: " + rows*columns);
        System.out.println("Antal namn tot: " + names.length);
        System.out.println("Saknas: " + missingBenches);
        System.out.println("Ej använda: " + forbiddenBenches);
        System.out.println("korridorer: " + corridors);
        System.out.println("Kompisar: " + Arrays.toString(benchFriends));
        System.out.println("Alla namn: " + allNnames);
        System.out.println("Första raden: " + firstRowNames);


        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout(0, 10));
        benchesPanel = new JPanel(new GridLayout(rows, columns));
        benches = new Bench[rows*columns+1];

        JButton button = new JButton("Ny placering");
        button.addActionListener(e -> {
            if (previousBench != null) return;
            newPlacing2();
        });

        JButton saveButton = new JButton("SPL");
        saveButton.addActionListener(e -> {
            // Flytta till dahandler
            StringBuilder sb = new StringBuilder(rows+"#"+columns+"qqq");
            // for(int c : this.corridors) sb.append(c).append("#");
            sb.append("qqq");
            for(Bench b : benches) sb.append(b.getBenchName()).append("#");
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

        // Vi placerar ut alla bänkar, utan namn:
        int benchNr = 1;
        benches[0] = null;
        for (int i = 0; i < rows; i++) {
            //LinkedList<Integer> tempCorr = new LinkedList<>(corridors);
            JPanel benchRow = new JPanel();
            benchRow.setLayout(new BoxLayout(benchRow, BoxLayout.X_AXIS));

            //int nextCorr = tempCorr.size() > 0 ? tempCorr.pop() : -1;
            for (int j = 0; j < columns; j++) {
                Bench b = new Bench(this, benchNr);
                benches[benchNr] = b;
                benchNr++;
                // Vi sätter korridor innan första raden
                if(j==0)
                    for (int k = 0; k < corridorWidths[0]; k++)
                        benchRow.add(new CorridorSpace());


//                    while (nextCorr == 0) {
//                        benchRow.add(new CorridorSpace());
//                        nextCorr = tempCorr.pop();
//                    }

                benchRow.add(b);
                // Vi sätter korridor efter varje rad
                for (int k = 0; k < corridorWidths[j+1]; k++)
                    benchRow.add(new CorridorSpace());

//                while (j+1 == nextCorr) {
//                    benchRow.add(new CorridorSpace());
//                    nextCorr = tempCorr.size() > 0 ? tempCorr.pop() : -1;
//                }
            }
            benchesPanel.add(benchRow);
        }

        // Tar bort vissa:
        for (int f : forbiddenBenches) benches[f].setStatus(Bench.FORBIDDEN);
        for (int m : missingBenches) benches[m].setStatus(Bench.MISSING);

        // Och sätter ut alla namn
        remainingNames = new LinkedList<>(allNnames);
        if(!putOutFriends()) return;
        placeAllRemainingNames();


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

    private void newPlacing2() {
        System.out.println("2an!");
        allNnames = new LinkedList<>();
        for (Bench b : benches) {
            if(b == null) continue;;
            if (b.getStatus() == Bench.OCCUPIED) {
                allNnames.add(b.getBenchName());
                b.setStatus(Bench.FREE);
            }
        }

        Collections.shuffle(allNnames);

        for (int i = 1; i <benches.length; i++) {
            if (benches[i].getStatus() != Bench.FREE) continue;
            String nextname = allNnames.poll();
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
        int bWidth = (benchesPanel.getWidth()-totalCorridors*corridorhWidth)/columns;
        int bHeight = benchesPanel.getHeight()/rows;
        return new int[] {bWidth,bHeight};
    }


    private void saveNeighbors() {
        LinkedList<String[]> neighbors = new LinkedList<>();
        for (int benchNr = 1; benchNr < benches.length; benchNr++) {
            if(benchNr%columns == 0) continue; // Vid högra väggen?
            if(corridorWidths[benchNr%columns] > 0) continue; // Korridor till höger?
            if(benches[benchNr].getStatus() != Bench.OCCUPIED) continue; // Ingen sitter?
            if(benches[benchNr+1].getStatus() != Bench.OCCUPIED) continue; // Ingen bredvid?
            String[] pair = {benches[benchNr].getBenchName(),benches[benchNr+1].getBenchName()};
            neighbors.add(pair);
        }
        boolean result = InsertHandler.insertNeighbors(neighbors);
        if(result)
            JOptionPane.showMessageDialog(null, "Nuvarande grannar sparade!", "Resultat", JOptionPane.INFORMATION_MESSAGE);
    }


    private void collectBenchStatus() {
        forbiddenBenches = new LinkedList<>();
        missingBenches = new LinkedList<>();
        for (int i = 1; i < benches.length; i++) {
            if(benches[i].getStatus() == Bench.MISSING) missingBenches.add(i);
            if(benches[i].getStatus() == Bench.FORBIDDEN) forbiddenBenches.add(i);
        }
    }

    private void placeAllRemainingNames() {
        LinkedList<Integer> firstRowAvailabeSeats = new LinkedList<>();
        for (int i = 1; i <= columns; i++) {
            if(benches[i].getStatus() == Bench.FREE) firstRowAvailabeSeats.add(i);
        }
        Collections.shuffle(firstRowAvailabeSeats);
        for(String firstRowName : firstRowNames){
            if(firstRowAvailabeSeats.isEmpty()) break;
            int seat = firstRowAvailabeSeats.pop();
            remainingNames.remove(firstRowName);
            benches[seat].setName(firstRowName);
        }

        int count = 1;
        Collections.shuffle(remainingNames);
        while (!remainingNames.isEmpty() && count<benches.length) {
            if(benches[count].getStatus() == Bench.FREE) benches[count].setName(remainingNames.pop());
            count++;
        }
    }

    private boolean putOutFriends() {
        // 1 Hur många rader används i klassrummet?
        int missingAmongStudents = 0;
        for (int missing : missingBenches) if (missing <= allNnames.size() + missingAmongStudents) missingAmongStudents++;
        int forbiddenAmongStudents = 0;
        for (int forbidden : forbiddenBenches) if (forbidden <= allNnames.size()+missingAmongStudents+forbiddenAmongStudents) forbiddenAmongStudents++;
        int maxRows = (allNnames.size() + missingAmongStudents + forbiddenAmongStudents)/columns;

        // 2 Hitta alla lediga dubbelbänkar
        LinkedList<Integer> availableDoubleSeats = new LinkedList<>();
        int inRow = 0;
        // Se upp för korridorerna:
        for (int i = 1; i < corridorWidths.length; i++) {
            if(inRow == 1) {
                for (int row = 0; row < rows; row++) {
                    int benchNumber = i - 1 + row * columns;
                    if(benches[benchNumber].getStatus() == Bench.FREE && benches[benchNumber+1].getStatus() == Bench.FREE)
                        availableDoubleSeats.add(benchNumber);
                }
                inRow = 0;
            }
            else if (corridorWidths[i] == 0) inRow++;
        }

        // 3 Kolla att alla kompisar får plats
        if(benchFriends.length/2 > availableDoubleSeats.size()) {
            int answer = JOptionPane.showConfirmDialog(null,"Dubbelplatserna räcker inte." +
                " Ändra dina val om du vill eller så kommer några ej tillsammans." +
                "Klicka OK för att fortsätta som det är.");
            if(answer != JOptionPane.OK_OPTION) return false;
        }

        // 4 Vi kollar hur många dubbelplatser vi ska använda
        Collections.sort(availableDoubleSeats);
          // Hur många ligger inom önskat område?
        int availableInStudentArea = 0;
        int benchNr = availableDoubleSeats.get(0);
        while (benchNr <= maxRows*columns) {
            availableInStudentArea++;
            benchNr = availableDoubleSeats.get(availableInStudentArea);
        }
          // Vi väljer antal:
        int maxAllowedPairs = benchFriends.length/2;
        int amountDoublesToUse = Math.max(benchFriends.length / 2, availableInStudentArea);
        LinkedList<Integer> doublesToUse = new LinkedList<>();
        for (int i = 0; i < amountDoublesToUse; i++) {
            doublesToUse.add(availableDoubleSeats.pop());
            if(availableDoubleSeats.isEmpty()) {
                maxAllowedPairs = doublesToUse.size();
                break;
            }
        }

        // Dubblar på rad 1:
        boolean hasFirstRow = !firstRowNames.isEmpty();
        LinkedList<Integer> firstRowDoubles = new LinkedList<>();
        Collections.sort(doublesToUse);

        if(hasFirstRow) {
            while (doublesToUse.getFirst() <= columns)
                firstRowDoubles.add(doublesToUse.pop());
        }

        // 4 Sätt ut bänkkompisarna:
        for (int i = 0; i < maxAllowedPairs*2; i+=2) {
            String f1 = benchFriends[i];
            String f2 = benchFriends[i+1];
            int offeredBench;
            // Om någon finns bland firstrow:
            if (hasFirstRow && (firstRowNames.contains(f1) || firstRowNames.contains(f2))) {
                firstRowNames.remove(f1); // Båda ska bort eftersom de nu placeras
                firstRowNames.remove(f2);

                if(firstRowDoubles.isEmpty()) offeredBench = doublesToUse.pop();
                else offeredBench = firstRowDoubles.pop();

            } else {
                offeredBench = doublesToUse.pop();
                firstRowDoubles.remove(Integer.valueOf(offeredBench));
                // Om vi råkar ta slut på bakre så endast förstaradare finns kvar:
                if(doublesToUse.isEmpty())
                    while (!firstRowDoubles.isEmpty())
                        doublesToUse.add(firstRowDoubles.pop());
            }

            if(!(remainingNames.remove(f1) && remainingNames.remove(f2)))
                JOptionPane.showMessageDialog(null,"Oväntat fel, en bänkkompis fanns inte");
            else {
                benches[offeredBench].setName(f1);
                benches[offeredBench+1].setName(f2);
            }
        }

        // checkaLIka();
        return true;
    }

    public static void main(String[] args) {
        String[] n = {"Ove","Tom","Janne","Per","Honken","Anton","Masken","Greg","Jakob","Calle","Elias L","AA","BB","CC","DD","EE","FF"};
        // String[] friend = {"Ove", "Janne"};
        String[] friend = {"Ove", "Janne","Anton","Greg","Honken","Masken","AA","BB","EE","FF","CC","DD"};
        //String[] co = {"2","4","5","6","1","7"};
        Integer[] corrs = {4,7,7,7,7};
        Integer[] misssi = {31};
        Integer[] forbidd = {30,13,14,15,9,10,1,3,7};
        String[] front = {"Masken","Per","Ove","AA","Janne"};

        new ClassRoom4(n, corrs, friend,front,forbidd,misssi,4,8);
    }
    private void checkaLIka () {
        boolean lika = false;
        StringBuilder sb = new StringBuilder();
        for(int i=1 ; i<benches.length ; i++) {
            if(benches[i].getBenchName().length()>1 && !benches[i].getBenchName().equals("Förbjuden")) {
                for (int j = i + 1; j < benches.length; j++) {
                    String nextName = benches[j].getBenchName();
                    if (nextName.length() > 1) {
                        if (nextName.equals(benches[i].getBenchName())) {
                            lika = true;
                            sb.append(nextName).append(", ");
                        }
                    }
                }
            }
        }
        String mess = lika ? "Dubbletter: " + sb.toString() : "Inga lika!!";
        JOptionPane.showMessageDialog(null, mess);
    }
}
// 514 rader