package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Scanner;

public class SeatingMenu {
    private  final JTextField rowInput, columnInput, removeInput, enemyInput, forbiddenBenchesInput;
    private JLabel allNames;
    private final JFrame frame;
    private final LinkedList<String> names;
    private Color myRed = new Color(247, 212, 212);
    //TODO
    // Fixa spacing och posistion så att den börjar på 1a eller 2a
    // Fokusen på radioknapparna
    // En "Gör om"-knapp
    // Numrering av grupper

    public SeatingMenu(LinkedList<String> names) {
        this.names = names;
        frame = new JFrame();
        frame.setLayout(new GridLayout(8, 1));
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setPreferredSize(new Dimension(300, 50));
        JPanel namesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel columnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel removePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel enemyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel forbiddenBenchesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel header = new JLabel("Bordsplacering");
        header.setFont(new Font(Font.MONOSPACED, Font.BOLD,24));
        headerPanel.add(header);

        allNames = new JLabel();
        allNames.setFont(new Font("arial", Font.PLAIN,10));
        setAllNames();

        namesPanel.add(allNames);

        rowPanel.add(new JLabel("Antal bänkrader i klassrummet:"));
        rowInput = new JTextField(5);
        rowPanel.add(rowInput);

        columnPanel.add(new JLabel("Antal bänkar på varje rad klassrummet:"));
        columnInput = new JTextField(5);
        columnPanel.add(columnInput);

        removePanel.add(new JLabel("Vilka elever ska bort?"));
        removeInput = new JTextField(50);
        removePanel.add(removeInput);

        enemyPanel.add(new JLabel("Vilka ska ej sitta nära varandra?"));
        enemyInput = new JTextField(50);
        enemyPanel.add(enemyInput);

        forbiddenBenchesPanel.add(new JLabel("Vilka bänkar ska ej användas?"));
        forbiddenBenchesInput = new JTextField(5);
        forbiddenBenchesPanel.add(forbiddenBenchesInput);

        JButton finishButton = new JButton("Skapa bordsplacering");
        buttonPanel.add(finishButton);
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Bordsplacering");
                tryFinish();
            }
        });

        frame.add(headerPanel);
        frame.add(namesPanel);
        frame.add(rowPanel);
        frame.add(columnPanel);
        frame.add(removePanel);
        frame.add(enemyPanel);
        frame.add(forbiddenBenchesPanel);
        frame.add(buttonPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void tryFinish() {
        System.out.println("Startlista: " + names);
        // =============== Ta bort elever:
        boolean success = true;
        removeInput.setBackground(Color.WHITE);
        String [] namesToRemove = removeInput.getText().split(",");
        StringBuilder failNames = new StringBuilder();
        for (String n1 : namesToRemove) {
            String n2 = n1.trim();
            if(n2.length() != 0 && !names.remove(n2)) {
                failNames.append(n2).append(",");
                success = false;
            }
        }
        setAllNames();
        removeInput.setText(failNames.toString());
        if (!success) {
            removeInput.setBackground(myRed);
            JOptionPane.showMessageDialog(frame, "Följande hittades inte: " + failNames.toString());
            return;
        }
        System.out.println("Efter borttagning: " + names);

        // =============== Sätt bänkraderna:
        columnInput.setBackground(Color.WHITE);
        rowInput.setBackground(Color.WHITE);
        int tables = 0, rows = 1, columns = 1;
        boolean hasSizes = true;
        Scanner scanner = new Scanner(columnInput.getText());
        if (scanner.hasNextInt()) {
            columns = scanner.nextInt();
        } else {
            hasSizes = false;
            columnInput.setBackground(myRed);
        }
        scanner = new Scanner(rowInput.getText());
        if (scanner.hasNextInt()) {
            rows = scanner.nextInt();
        } else {
            hasSizes = false;
            rowInput.setBackground(myRed);
        }
        if(hasSizes) {
            tables = rows * columns;
            if (tables < names.size()) {
                JOptionPane.showMessageDialog(frame, "Alla får inte plats");
                return;
            }
        } else return;

        // =============== Ovännerna:
        boolean removeSuccess = true;
        LinkedList<String> tempNames = new LinkedList<>(names);
        LinkedList<String> removedNames = new LinkedList<>();
        LinkedList<String> modifiedNames = new LinkedList<>();

        Scanner sc1 = new Scanner(enemyInput.getText());
        sc1.useDelimiter(",");
        StringBuilder notFound = new StringBuilder("Följande hittades ej: ");
        while(sc1.hasNext()) {
            String name = sc1.next().trim();
            if(name.length() != 0) {
                if (tempNames.remove(name)) removedNames.add(name);
                else {
                    System.out.println("Npoe " + name);
                    notFound.append(name).append(",");
                    removeSuccess = false;
                }
            }
        }
        if(!removeSuccess) {
            enemyInput.setBackground(myRed);
            JOptionPane.showMessageDialog(frame, notFound.toString());
            return;
        }
        if(removedNames.size() > 0) {
            int spacing = names.size() / removedNames.size() - 1;
            if (spacing < 1) {
                JOptionPane.showMessageDialog(frame, "För många som ska separeras. Om detta problem dyker upp ofta, kontakta Lars.");
                return;
            }
            int position = 0;
            if (spacing > 3) {
                position = (int) -(Math.random() * spacing);
            }
            System.out.println("Spacing blev " + spacing);
            System.out.println("Startpos blev " + position);

            do {
                System.out.println("While: " + position % spacing);
                modifiedNames.add(tempNames.pop());
                position++;
                if (position % spacing == 0 && removedNames.size() != 0) {
                    modifiedNames.add(removedNames.pop());
                    System.out.println("Japp la till från rem");
                }
            } while (tempNames.size() != 0);
        } else modifiedNames = tempNames;
        System.out.println("Måste alltid va sant: (listan före och efter ovänner lika stora: " + (modifiedNames.size() == names.size()));
        System.out.println("Efter ovänner: " + modifiedNames);

        // =============== Ta bort bänkar och gör ny namnlista med luckor
        System.out.println("Steg 3, förbjudna bänkar.");
        scanner = new Scanner(forbiddenBenchesInput.getText());

        LinkedList<Integer> benchesToAvoid = new LinkedList<>();
        while (scanner.hasNextInt()) {
            benchesToAvoid.add(scanner.nextInt());
        }
        System.out.println("Undvik bänkar nr: " + benchesToAvoid);
        if ((names.size() + benchesToAvoid.size() > tables)) {
            JOptionPane.showMessageDialog(frame, "Du tog bort för många bänkar. Alla får inte plats längre.");
            return;
        }
        LinkedList<String> benchNames = new LinkedList<>();
        int count = 1;
        for (String name : modifiedNames) {
            while (benchesToAvoid.contains(count)) {
                benchNames.add("");
                count++;
            }
            benchNames.add(name);
            count++;
        }
        while (benchNames.size() < tables) {
            benchNames.add("");
        }
        System.out.println("Klart! benches:" + benchNames);



        System.out.println("Bord: " + tables);
        System.out.println("Bänknamn storlek: " + benchNames.size());

        System.out.println();
//        for(String ss : benchNames) {
//            System.out.println("-> " + ss);
//        }
        // Ta hand om enemies

        // frame.setVisible(false);
        new ClassRoom(benchNames, rows, columns);
        /*String resp = JOptionPane.showInputDialog(null, "Skriv numren på de platser som ska lämnas tomma:");
        scanner = new Scanner(resp);
        LinkedList<Integer> benchesToAvoid = new LinkedList<>();
        while (scanner.hasNextInt()) {
            benchesToAvoid.add(scanner.nextInt());
        }
        for(int ii : benchesToAvoid) {
            System.out.println("Undvik bänk " + ii);
        }*/


    }
    private void setAllNames() {
        StringBuilder sb = new StringBuilder();
        for (String s : names) {
            sb.append(s).append(",");
        }
        allNames.setText(sb.toString());
    }
}
