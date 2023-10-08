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
    private final String[] frontRowNames;
    private LinkedList<Integer> missingBenches = new LinkedList<>();
    private LinkedList<Integer> forbiddenBenches = new LinkedList<>();
    private final LinkedList<Integer> corridors = new LinkedList<>();
    private int[] corridorWidths;
    private LinkedList<Integer> availablePairSeats = new LinkedList<>();


    public ClassRoom4(String[] names, String[] corrs, String[] friends, String[] frontRow, Integer[] forbidden, Integer[] missing, int rows, int columns) {

        // Gör listor:
        Collections.addAll(allNnames,names);
        benchFriends = friends;
        frontRowNames = frontRow;
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
        for (String s : corrs) {
            String c = s.trim();
            try {
                int corr = Integer.parseInt(c);
                if (corr <= columns && corr >= 0) {
                    corridors.add(corr);
                    corridorWidths[corr]++;
                }
            } catch (NumberFormatException e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null,"Det fanns fel i mellanrumslistan");
            }
        }
        Collections.sort(corridors);
        System.out.println(Arrays.toString(corridorWidths));


        System.out.println("Antal platser: " + rows*columns);
        System.out.println("Antal namn tot: " + names.length);
        System.out.println("Saknas: " + missingBenches);
        System.out.println("Ej använda: " + forbiddenBenches);
        System.out.println("korridorer: " + corridors);
        System.out.println("Kompisar: " + Arrays.toString(benchFriends));
        System.out.println("Alla namn: " + allNnames);
        System.out.println("Första raden: " + Arrays.toString(frontRowNames));


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
            for(int c : this.corridors) sb.append(c).append("#");
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
            LinkedList<Integer> tempCorr = new LinkedList<>(corridors);
            JPanel benchRow = new JPanel();
            benchRow.setLayout(new BoxLayout(benchRow, BoxLayout.X_AXIS));

            int nextCorr = tempCorr.size() > 0 ? tempCorr.pop() : -1;
            for (int j = 0; j < columns; j++) {
                Bench b = new Bench(this, benchNr);
                benches[benchNr] = b;
                benchNr++;
                // Vi sätter korridor innan första raden
                if(j==0)
                    while (nextCorr == 0) {
                        benchRow.add(new CorridorSpace());
                        nextCorr = tempCorr.pop();
                    }

                benchRow.add(b);
                // Vi sätter korridor efter varje rad
                while (j+1 == nextCorr) {
                    benchRow.add(new CorridorSpace());
                    nextCorr = tempCorr.size() > 0 ? tempCorr.pop() : -1;
                }
            }
            benchesPanel.add(benchRow);
        }

        // Tar bort vissa:
        for (int f : forbiddenBenches) benches[f].setStatus(Bench.FORBIDDEN);
        for (int m : missingBenches) benches[m].setStatus(Bench.MISSING);

        // Och sätter ut alla namn
        remainingNames = new LinkedList<>(allNnames);
        putOutFriends();
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
        for (String name : allNnames) if(name.length() > 1) namesLeft.add(name);
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
        if(benchFriends.length/2 > totalPairCapacity) {
            JOptionPane.showMessageDialog(null,"Dubbelplatserna räcker inte. Ändra dina val");
            return;
        }

        // Listor för rätt namn på rätt ställe
        LinkedList<String> singleNames = new LinkedList<>();
        LinkedList<String> pairNames = new LinkedList<>();
        // Lägg dit kompisarna:
//        for (String[] pair : benchFriends) {
//            String nextFriends = pair[0] + "," + pair[1] + ",";
//            pairNames.add(nextFriends);
//            if(!(namesLeft.remove(pair[0]) && namesLeft.remove(pair[1]))) JOptionPane.showMessageDialog(null,"Oväntat fel");
//        }

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

    private void collectBenchStatus() {
        forbiddenBenches = new LinkedList<>();
        missingBenches = new LinkedList<>();
        for (int i = 1; i < benches.length; i++) {
            if(benches[i].getStatus() == Bench.MISSING) missingBenches.add(i);
            if(benches[i].getStatus() == Bench.FORBIDDEN) forbiddenBenches.add(i);
        }
    }

    private void placeFriends() {

    }

    private void placeFrontRow() {

    }

    private void insertEnemies() {

    }

    private void placeAllRemainingNames() {
        int count = 1;
        while (!remainingNames.isEmpty() && count<benches.length) {
            if(benches[count].getStatus() == Bench.FREE) benches[count].setName(remainingNames.pop());
            count++;
        }
    }

    private void putOutFriends() {
        // 1 Hur många rader används i klassrummet?
        int missingAmongStudents = 0;
        for (int missing : missingBenches) if (missing <= allNnames.size() + missingAmongStudents) missingAmongStudents++;
        int forbiddenAmongStudents = 0;
        for (int forbidden : forbiddenBenches) if (forbidden <= allNnames.size()+missingAmongStudents+forbiddenAmongStudents) forbiddenAmongStudents++;
        int maxRows = (allNnames.size() + missingAmongStudents + forbiddenAmongStudents)/columns;




        System.out.println("Antal tot: " + allNnames.size());
        System.out.println("Förbjudna inom klassplaceringen: " + forbiddenAmongStudents);
        System.out.println("Saknas inom klassplaceringen: " + missingAmongStudents);
        System.out.println("Antal rader för par: " + maxRows);

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
            JOptionPane.showMessageDialog(null,"Dubbelplatserna räcker inte." +
                " Ändra dina val om du vill eller så kommer några ej tillsammans");
        }

        // 4 Vi kollar hur många dubbelplatser vi ska använda
        Collections.sort(availableDoubleSeats);
        System.out.println("Lediga dubbelplatser, alla efter sortering: " + availableDoubleSeats);
          // Hur många ligger inom önskat område?
        int availableInStudentArea = 0;
        int benchNr = availableDoubleSeats.get(0);
        while (benchNr <= maxRows*columns) {
            availableInStudentArea++;
            benchNr = availableDoubleSeats.get(availableInStudentArea);
        }
        System.out.println("Antal lediga dubbelplatser på de " + maxRows + " första raderna: " + availableInStudentArea);
          // Vi väljer antal:
        int amountDoublesToUse = Math.max(benchFriends.length / 2, availableInStudentArea);
        LinkedList<Integer> doublesToUse = new LinkedList<>();
        for (int i = 0; i < amountDoublesToUse; i++) {
            doublesToUse.add(availableDoubleSeats.get(i));
        }


        System.out.println("Dubbelbänkar som används: " + doublesToUse + "    (antal "+ doublesToUse.size() + ") ");
        System.out.println("(Ska va alla som är med på de närmsta raderan eller lika som antal som behövs)");
        Collections.shuffle(doublesToUse);
        // Flytta upp

        // 4 Sätt ut bänkkompisarna:
        for (int i = 0; i < benchFriends.length; i+=2) {
            String f1 = benchFriends[i];
            String f2 = benchFriends[i+1];
            int offeredBench = doublesToUse.pop();
            if(!(remainingNames.remove(f1) && remainingNames.remove(f2)))
                JOptionPane.showMessageDialog(null,"Oväntat fel, en bänkkompis fanns inte");
            else {
                benches[offeredBench].setName(f1);
                benches[offeredBench+1].setName(f2);
            }
        }
    }

    public static void main(String[] args) {
        String[] n = {"Ove","Tom","Janne","Per","Honken","Anton","Masken","Greg","Jakob","Calle","Elias L"};
        String[] friend = {"Ove", "Janne","Anton","Greg","Honken","Masken"};
        //String[] co = {"2","4","5","6","1","7"};
        String[] corrs = {"17"};
        Integer[] misssi = {31};
        Integer[] forbidd = {30};
        String[] front = {"Per"};

        new ClassRoom4(n, corrs, friend,front,forbidd,misssi,4,8);
    }
}
