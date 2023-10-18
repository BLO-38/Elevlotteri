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
    private LinkedList<String> allNnames;
    private LinkedList<String> remainingNames;
    private final LinkedList<String > benchFriends;
    private LinkedList<String> firstRowNames;
    private final LinkedList<String> firstRowNamesOrigin;
    private LinkedList<Integer> missingBenches;
    private LinkedList<Integer> forbiddenBenches;
    private final LinkedList<Integer> corridors = new LinkedList<>();
    private final int[] corridorWidths;
    private int totalCorridors;
    private int originalAntalBenchFriends, totalForbidMiss;
    private boolean messageShown = false;


    public ClassRoom4(LinkedList<String> names, LinkedList<Integer> corrs, LinkedList<String> friends, LinkedList<String> frontRow, LinkedList<Integer> forbidden, LinkedList<Integer> missing, int rows, int columns, boolean randomize) {

        firstRowNamesOrigin = new LinkedList<>(frontRow);
        allNnames=names;
        benchFriends = friends;
        firstRowNames = frontRow;
        missingBenches = missing == null ? new LinkedList<>() : new LinkedList<>(missing);
        forbiddenBenches = forbidden == null ? new LinkedList<>() : new LinkedList<>(forbidden);
        originalAntalBenchFriends = benchFriends.size();
        if(originalAntalBenchFriends > 0 && originalAntalBenchFriends %2 == 1) originalAntalBenchFriends--;
        this.rows = rows;
        this.columns = columns;
        totalForbidMiss = missingBenches.size() + forbiddenBenches.size();
        if(!randomize) {
            totalForbidMiss = 0;
            for (String nn : allNnames)
                if (nn.equals("-") || nn.equals("x"))
                    totalForbidMiss++;
        }
        System.out.println("totforbidmiss vid start: " + totalForbidMiss);

        // Fixa korridorer:
        // Gör korridorer till en sorterad lista med godkända tal
        // och sätt bredden:
        corridorWidths = new int[columns+1];
        // Först och sist har vi korridor (=vägg)
        corridorWidths[0] = 1;
        corridorWidths[columns] = 1;
        // trimma parsa kolla område lägg till
        totalCorridors = 2;
        for (int corr : corrs) {
            if (corr <= columns && corr >= 0) {
                corridors.add(corr);
                corridorWidths[corr]++;
                totalCorridors++;
            }
        }

        Collections.sort(corridors);

        System.out.println("Totalcorridors: " + totalCorridors);
        System.out.println("Samma? " + corridors.size());
        System.out.println(Arrays.toString(corridorWidths));
        System.out.println("Antal platser: " + rows*columns);
        System.out.println("Antal namn tot: " + allNnames.size());
        System.out.println("Saknas: " + missingBenches);
        System.out.println("Ej använda: " + forbiddenBenches);
        System.out.println("korridorer: " + corridors);
        System.out.println("Kompisar: " + benchFriends);
        System.out.println("Alla namn: " + allNnames);
        System.out.println("Första raden: " + firstRowNames);


        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout(0, 10));
        benchesPanel = new JPanel(new GridLayout(rows, columns));
        benches = new Bench[rows*columns+1];

        JButton button = new JButton("Ny placering");
        button.addActionListener(e -> {
            if (previousBench != null) return;
            collectBenchStatus();
            remainingNames = new LinkedList<>(allNnames);
            firstRowNames = new LinkedList<>(firstRowNamesOrigin);
            firstRowNames.removeIf(n-> (!allNnames.contains(n)));
            putOutFriends();
            placeAllRemainingNames();
        });

        JButton saveButton = new JButton("Spara");
        saveButton.addActionListener(e -> {
            StringBuilder sb = new StringBuilder(rows+"#"+columns+"qqq");
            for(int c : this.corridors) sb.append(c).append("#");
            sb.append("qqq");
            for (int i = 1; i < benches.length; i++)
                sb.append(benches[i].getBenchName()).append("#");
            sb.append("qqq");
            for(String fr : benchFriends) sb.append(fr).append("#");
            sb.append("qqq");
            for(String firstR : firstRowNames) sb.append(firstR).append("#");

            InsertHandler.saveBenches(sb.toString());

        });

        JButton saveNeighborsButton = new JButton("Spara BG");
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

        // Vi placerar ut alla bänkar, med eller utan namn:
        int benchNr = 1;
        benches[0] = null;
        for (int i = 0; i < rows; i++) {
            JPanel benchRow = new JPanel();
            benchRow.setLayout(new BoxLayout(benchRow, BoxLayout.X_AXIS));

            for (int j = 0; j < columns; j++) {
                String tempName = !randomize && benchNr <= allNnames.size() ? allNnames.get(benchNr-1) : "";
                Bench b = randomize ? new Bench(this, benchNr) : new Bench(tempName,benchNr,this);
                benches[benchNr] = b;
                benchNr++;
                if(j==0)
                    for (int k = 0; k < corridorWidths[0]; k++)
                        benchRow.add(new CorridorSpace());

                benchRow.add(b);
                // Vi sätter korridor efter varje rad
                for (int k = 0; k < corridorWidths[j+1]; k++)
                    benchRow.add(new CorridorSpace());
            }
            benchesPanel.add(benchRow);
        }

        if(randomize) {
            // Tar bort vissa:
            for (int f : forbiddenBenches) benches[f].setName("x");
            for (int m : missingBenches) benches[m].setName("-");

            // Och sätter ut alla namn
            remainingNames = new LinkedList<>(allNnames);
            if (!putOutFriends()) return;
            placeAllRemainingNames();
        }

        frame.add(benchesPanel, BorderLayout.CENTER);
        frame.add(buttPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setVisible(true);
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
        allNnames = new LinkedList<>();

        for (int i = 1; i < benches.length; i++) {
            if (benches[i].getStatus() == Bench.MISSING) missingBenches.add(i);
            else if (benches[i].getStatus() == Bench.FORBIDDEN) forbiddenBenches.add(i);
            else if (benches[i].getStatus() == Bench.OCCUPIED || benches[i].getStatus() == Bench.MARKED) {
                allNnames.add(benches[i].getBenchName());
                benches[i].setName("");
            }
        }
        messageShown = forbiddenBenches.size() + missingBenches.size() == totalForbidMiss;
        totalForbidMiss = forbiddenBenches.size() + missingBenches.size();
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
        // 0 Hur många bänkkompisar? (Nån kan ha tagits bort)
        int currentAntalFriendPairs = 0;
        for (int i = 0; i < originalAntalBenchFriends; i+=2) {
            if(remainingNames.contains(benchFriends.get(i)) && remainingNames.contains(benchFriends.get(i+1))) currentAntalFriendPairs++;
        }

        // 1 Hur många rader används i klassrummet?
        int missingAmongStudents = 0;
        for (int missing : missingBenches) if (missing <= allNnames.size() + missingAmongStudents) missingAmongStudents++;
        int forbiddenAmongStudents = 0;
        for (int forbidden : forbiddenBenches) if (forbidden <= allNnames.size()+missingAmongStudents+forbiddenAmongStudents) forbiddenAmongStudents++;
        int maxRows = (allNnames.size() + missingAmongStudents + forbiddenAmongStudents)/columns;
        maxRows = Math.max(1,maxRows);

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
        if(!messageShown && currentAntalFriendPairs > availableDoubleSeats.size()) {
            messageShown = true;
            int answer = JOptionPane.showConfirmDialog(null,"Dubbelplatserna räcker inte." +
                " Ändra dina val om du vill eller så kommer några ej tillsammans." +
                "Klicka Yes för att fortsätta som det är.");
            if(answer != JOptionPane.OK_OPTION) return false;
        }
        if(availableDoubleSeats.isEmpty()) return true;

        // 4 Vi kollar hur många dubbelplatser vi önskar välja bland
        Collections.sort(availableDoubleSeats);
          // Hur många ligger inom önskat område?
        int antalAvailableInStudentArea = 0;
        int benchNr = availableDoubleSeats.get(0);
        while (benchNr <= maxRows*columns) {
            antalAvailableInStudentArea++;
            benchNr = availableDoubleSeats.get(antalAvailableInStudentArea);
        }

        // 5 Vi väljer antal och skapar en lista med de dubbelbänkar vi ska använda:
        int amountDoublesToUse = Math.max(currentAntalFriendPairs, antalAvailableInStudentArea);
        LinkedList<Integer> doublesToUse = new LinkedList<>();
        for (int i = 0; i < amountDoublesToUse; i++) {
            doublesToUse.add(availableDoubleSeats.pop());
            if(availableDoubleSeats.isEmpty()) break;
        }

        // Dubblar på rad 1:
        boolean hasFirstRowNames = !firstRowNames.isEmpty();
        LinkedList<Integer> firstRowDoubles = new LinkedList<>();
        Collections.sort(doublesToUse);
        if(hasFirstRowNames)
            while (!doublesToUse.isEmpty() && doublesToUse.getFirst() <= columns)
                firstRowDoubles.add(doublesToUse.pop());

            // 4 Sätt ut bänkkompisarna:
        Collections.shuffle(doublesToUse);
        Collections.shuffle(firstRowDoubles);

        for (int i = 0; i < originalAntalBenchFriends; i+=2) {
            String f1 = benchFriends.get(i);
            String f2 = benchFriends.get(i+1);
            String nextName1 = remainingNames.remove(f1) ? f1 : "";
            String nextName2 = remainingNames.remove(f2) ? f2 : "";
            // Om någon inte fanns kvar, sätt tillbaka den andra och avbryt varvet
            if(nextName1.length()==0 || nextName2.length()==0) {
                if(nextName1.length()>0) remainingNames.add(nextName1);
                if(nextName2.length()>0) remainingNames.add(nextName2);
                continue;
            }

            int offeredBench;
            // Om någon finns bland firstrow:
            if (hasFirstRowNames && (firstRowNames.contains(f1) || firstRowNames.contains(f2))) {
                firstRowNames.remove(f1); // Båda ska bort eftersom de nu placeras
                firstRowNames.remove(f2);

                if(firstRowDoubles.isEmpty()) offeredBench = doublesToUse.pop();
                else offeredBench = firstRowDoubles.pop();

            } else {
                if(doublesToUse.isEmpty()) offeredBench = firstRowDoubles.pop();
                else offeredBench = doublesToUse.pop();
                System.out.println("Alltid falsk? ---------- >  " + firstRowDoubles.remove(Integer.valueOf(offeredBench)));
                // Om vi råkar ta slut på bakre så endast förstaradare finns kvar:
                /*
                Detta kan väl ändå inte behövas????
                if(doublesToUse.isEmpty()) {
                    while (!firstRowDoubles.isEmpty())
                        doublesToUse.add(firstRowDoubles.pop());
                    Collections.shuffle(doublesToUse);
                }

                 */
            }

            LinkedList<String> fr = new LinkedList<>();
            fr.add(f1);fr.add(f2);Collections.shuffle(fr);
            benches[offeredBench].setName(fr.pop());
            benches[offeredBench + 1].setName(fr.pop());
            if(doublesToUse.isEmpty() && firstRowDoubles.isEmpty()) break;
        }
        return true;
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
        String mess = lika ? "Dubbletter: " + sb : "Inga lika!!";
        JOptionPane.showMessageDialog(null, mess);
    }
}
// 514 rader
// Nya namn ej kvar...
// Funkar int att trycka ok om dubbelplatserna inte räcker
// Fel på förstaradsbänkar om man tagir bort några kompisar
// Borttagen kompis kommer tillbaka???'
// Ove och janne kommer tillbaka hela tin