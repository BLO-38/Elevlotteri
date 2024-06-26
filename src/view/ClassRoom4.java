package view;

import databasen.InsertHandler;
import model.MainHandler;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class ClassRoom4 implements Room{
    private final String className;
    private final JPanel centerPanel;
    private final JPanel wbPanel;
    private JPanel benchesPanel;
    private final int rows, columns;
    private Bench previousBench;
    private final Bench[] benches;
    public static final int corridorhWidth = 30;
    private LinkedList<String> allNnames;
    private LinkedList<String> remainingNames;
    private final LinkedList<String > benchFriends;
    private LinkedList<String> firstRowNames;
    private final LinkedList<String> firstRowNamesOrigin;
    private LinkedList<Integer> missingBenches;
    private LinkedList<Integer> forbiddenBenches;
    private final LinkedList<Integer> corridors = new LinkedList<>();
    private final JButton saveNeighborsButton;
    private final JButton saveButton;
    private final int[] corridorWidths;
    private int totalCorridorSpaces;
    private int originalAntalBenchFriends, totalForbidMiss;
    private boolean messageShown = false;
    private boolean isShowingRed = false;
    private boolean isTeacherView = false;
    private final LinkedList<CorridorSpace> spaces = new LinkedList<>();


    public ClassRoom4(LinkedList<String> names,
                      LinkedList<Integer> corrs,
                      LinkedList<String> friends,
                      LinkedList<String> frontRow,
                      LinkedList<Integer> forbidden,
                      LinkedList<Integer> missing,
                      int rows, int columns, boolean randomize, String cl) {
        className = cl;
        Bench.setIsShowingMissings(false);
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
        totalCorridorSpaces = 2;
        for (int corr : corrs) {
            if (corr <= columns && corr >= 0) {
                corridors.add(corr);
                corridorWidths[corr]++;
                totalCorridorSpaces++;
            }
        }

        Collections.sort(corridors);

        System.out.println("Totalcorridors: " + totalCorridorSpaces);
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


        JFrame frame = new JFrame(MainHandler.version);
        frame.setLayout(new BorderLayout(0, 10));
        benchesPanel = new JPanel(new GridLayout(rows, columns));
        benches = new Bench[rows*columns+1];

        // Centern:
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Bench.BENCH_NORMAL_BACKGROUND);
        // DEN BLIR MINDRE Å MINDRE NU...???

        saveButton = new JButton("Spara");
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

            if(InsertHandler.saveBenches(sb.toString(),className)) {
                saveButton.setEnabled(false);
            }
        });

        saveNeighborsButton = new JButton("Spara BG");
        saveNeighborsButton.addActionListener(e ->  saveNeighbors() );

        JButton teacherVieWButton = new JButton("Växla teacher view");
        teacherVieWButton.addActionListener(e -> {
            isTeacherView = !isTeacherView;
            paintClassRoom(isTeacherView);
        });
        
        JButton newSeatsButton = new JButton("Ny placering");
        newSeatsButton.addActionListener(e -> {
            if (previousBench != null) return;
            collectBenchStatus();
            remainingNames = new LinkedList<>(allNnames);
            firstRowNames = new LinkedList<>(firstRowNamesOrigin);
            firstRowNames.removeIf(n-> (!allNnames.contains(n)));
            putOutFriends();
            placeAllRemainingNames();
            saveButton.setEnabled(true);
            saveNeighborsButton.setEnabled(true);
        });
        newSeatsButton.setBackground(MainHandler.MY_RED);
        newSeatsButton.setForeground(Color.WHITE);

        JButton showRed = new JButton("Visa ej använda");
        showRed.addActionListener(e -> {
            for (Bench b : benches) {
                if(b==null) continue;
                if(isShowingRed) b.setBackground(Bench.BENCH_NORMAL_BACKGROUND);
                else if(b.getStatus() == Bench.FORBIDDEN) b.setBackground(Bench.BENCH_MISSING_BACKGROUND);
            }
            isShowingRed = !isShowingRed;
            Bench.setIsShowingMissings(isShowingRed);
        });


        wbPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wbPanel.setBackground(Bench.BENCH_NORMAL_BACKGROUND);
        JLabel whiteboardText = new JLabel("  W H I T E B O A R D  ");
        whiteboardText.setOpaque(true);
        whiteboardText.setBackground(Color.WHITE);
        whiteboardText.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
        whiteboardText.setBorder(new LineBorder(Color.BLACK,2));
        wbPanel.add(whiteboardText);
        frame.add(centerPanel, BorderLayout.CENTER);
        centerPanel.add(wbPanel,BorderLayout.NORTH);

        JPanel allButtPanel = new JPanel(new BorderLayout());
        JPanel buttPanelLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel buttPanelRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton.setBackground(MainHandler.MY_GREEN);
        saveButton.setForeground(Color.WHITE);
        buttPanelLeft.add(newSeatsButton);
        buttPanelLeft.add(showRed);
        buttPanelLeft.add(teacherVieWButton);
        buttPanelRight.add(saveButton);
        buttPanelRight.add(saveNeighborsButton);
        allButtPanel.add(buttPanelLeft,BorderLayout.WEST);
        allButtPanel.add(buttPanelRight,BorderLayout.EAST);
        // Vi placerar ut alla bänkar, med eller utan namn:
        int benchNr = 1;
        benches[0] = new Bench(this,-1);
        for (int i = 0; i < rows; i++) {
            JPanel benchRow = new JPanel();
            benchRow.setBackground(Bench.BENCH_NORMAL_BACKGROUND);

            benchRow.setLayout(new BoxLayout(benchRow, BoxLayout.X_AXIS));

            for (int j = 0; j < columns; j++) {
                String tempName = !randomize && benchNr <= allNnames.size() ? allNnames.get(benchNr-1) : "";
                Bench b = randomize ? new Bench(this, benchNr) : new Bench(tempName,benchNr,this);
                benches[benchNr] = b;
                benchNr++;
                if(j==0)
                    for (int k = 0; k < corridorWidths[0]; k++) {
                        CorridorSpace cs = new CorridorSpace();
                        benchRow.add(cs);
                        spaces.add(cs);
                    }

                benchRow.add(b);
                // Vi sätter korridor efter varje rad
                for (int k = 0; k < corridorWidths[j+1]; k++) {
                    CorridorSpace cs = new CorridorSpace();
                    benchRow.add(cs);
                    spaces.add(cs);
                }
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

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                benchCalculations(benchesPanel.getWidth(),benchesPanel.getHeight());
            }

        });
        centerPanel.add(benchesPanel, BorderLayout.CENTER);
        frame.add(allButtPanel, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        benchCalculations(benchesPanel.getWidth(),benchesPanel.getHeight());
        frame.setVisible(true);
    }

    private void paintClassRoom(boolean setTeacherView) {
        // Sudda bort:
        centerPanel.removeAll();

        // Förb:
        String wbPosition = setTeacherView ? BorderLayout.SOUTH : BorderLayout.NORTH;
        int benchIndex = setTeacherView ? benches.length-1 : 1;
        int delta = setTeacherView ? -1 : 1;
        int firstCorrIndex = setTeacherView ? corridorWidths.length-1 : 0;
        int xx = benchesPanel.getWidth();
        int yy = benchesPanel.getHeight();

        // Sätt ut bänkarna i nytt klassrum:
        benchesPanel = new JPanel(new GridLayout(rows, 1));
        for (int i = 0; i < rows; i++) {
            JPanel benchRow = new JPanel();
            benchRow.setBackground(Bench.BENCH_NORMAL_BACKGROUND);
            benchRow.setLayout(new BoxLayout(benchRow, BoxLayout.X_AXIS));
            int currentCorr = firstCorrIndex;
            for (int j = 0; j < columns; j++) {
                if(j==0)
                    for (int k = 0; k < corridorWidths[firstCorrIndex]; k++) {
                        CorridorSpace cs = new CorridorSpace();
                        benchRow.add(cs);
                        spaces.add(cs);
                    }
                benchRow.add(benches[benchIndex]);
                benchIndex += delta;
                // Vi sätter korridor efter varje rad
                currentCorr += delta;
                for (int k = 0; k < corridorWidths[currentCorr]; k++) {
                    CorridorSpace cs = new CorridorSpace();
                    benchRow.add(cs);
                    spaces.add(cs);
                }
            }
            benchesPanel.add(benchRow);
        }
        centerPanel.add(wbPanel, wbPosition);
        centerPanel.add(benchesPanel,BorderLayout.CENTER);
        benchCalculations(xx, yy);
        centerPanel.revalidate();
    }

    private void benchCalculations(int benchPanelX, int benchPanelY) {
        double antalBenchSpacesInklCorrs = 1.0*columns + totalCorridorSpaces/5.0;
        int newBenchWitdth = (int) (benchPanelX/antalBenchSpacesInklCorrs);
        for (Bench b : benches) b.setPreferredSize(new Dimension(newBenchWitdth,benchPanelY/rows));
        for (CorridorSpace cs : spaces) cs.setPreferredSize(new Dimension(newBenchWitdth/5,benchPanelY/rows));
    }

    public void benchClicked(Bench bench) {
        if (previousBench == null) {
            previousBench = bench;
            previousBench.setMarked(true);
        } else {
            String clickedName = bench.getBenchName();
            if(!clickedName.equals(previousBench.getBenchName())) {
                saveButton.setEnabled(true);
                saveNeighborsButton.setEnabled(true);
            }
            bench.setName(previousBench.getBenchName());
            previousBench.setName(clickedName);
            previousBench.setMarked(false);
            bench.repaint();
            previousBench = null;
        }
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
        boolean result = InsertHandler.insertNeighbors(neighbors, className);
        if(result) {
            JOptionPane.showMessageDialog(null, "Nuvarande grannar sparade!", "Resultat", JOptionPane.INFORMATION_MESSAGE);
            saveNeighborsButton.setEnabled(false);
        }
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
        if(benchFriends.isEmpty()) return true;

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
        // (Hur många ligger inom önskat område?)
        Collections.sort(availableDoubleSeats);
        int antalAvailableInStudentArea = 0;
        int benchNr;
        while (antalAvailableInStudentArea<availableDoubleSeats.size()) {
            benchNr = availableDoubleSeats.get(antalAvailableInStudentArea);
            if (benchNr > maxRows*columns) break;
            antalAvailableInStudentArea++;
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
            if(nextName1.isEmpty() || nextName2.isEmpty()) {
                if(!nextName1.isEmpty()) remainingNames.add(nextName1);
                if(!nextName2.isEmpty()) remainingNames.add(nextName2);
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

}
// Nya namn ej kvar...
// Funkar int att trycka ok om dubbelplatserna inte räcker
// Fel på förstaradsbänkar om man tagir bort några kompisar
// Borttagen kompis kommer tillbaka???
// Ove och janne kommer tillbaka hela tin