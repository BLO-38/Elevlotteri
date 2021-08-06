package view;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Scanner;

public class ClassRoom {
    private JFrame frame;
    // private Bench bench;
    public ClassRoom (LinkedList<String> names) {
        int tables = 0, rows = 1, columns = 1;
        String mess = "Skriv antal rader och antal bänkar per rad:";
        if(names.contains("Olle")) {
            System.out.println("Ja Olle finns");
        } else {
            System.out.println("Nej ingen Olle");
        }
        do {
            String inp = JOptionPane.showInputDialog(null, mess);
            Scanner scanner = new Scanner(inp);
            rows = scanner.nextInt();
            columns = scanner.nextInt();
            tables = rows * columns;
            mess = "Alla får inte plats";
        } while (tables < names.size());

        String resp = JOptionPane.showInputDialog(null, "Skriv numren på de platser som ska lämnas tomma:");
        Scanner scanner = new Scanner(resp);
        LinkedList<Integer> benchesToAvoid = new LinkedList<>();
        while (scanner.hasNextInt()) {
            benchesToAvoid.add(scanner.nextInt());
        }
        for(int ii : benchesToAvoid) {
            System.out.println("Undvik bänk " + ii);
        }

        frame = new JFrame();
        frame.setLayout(new GridLayout(rows, columns, 4, 4));
        int count = 1;
        int emptyCount = 0;
        boolean overFlow = false;
        for (String name : names) {
            if(count > tables) {
                overFlow = true;
                break;
            }
            while (benchesToAvoid.contains(count)) {
                frame.add(new Bench(""));
                count++;
                emptyCount++;
            }
            frame.add(new Bench(name));
            count++;
        }
        for (int i= names.size(); i < (tables-emptyCount); i++) {
            System.out.println("Vi lägger i efterhand till tomt bord nummer " + i);
            frame.add(new Bench(""));
            count++;
        }
        System.out.println("Totalt alntal bänkar utlagda: " + (count-1));
//        frame.add(new Bench("Emil"));
//        frame.add(new Bench("Erika"));
//        frame.add(new Bench("Hilma"));
//        frame.add(new Bench("Lars"));
//        frame.add(new Bench("Olle"));
//        frame.add(new Bench("Stina Blomgren"));
//        frame.add(new Bench(""));
//        frame.add(new Bench(""));

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        if (overFlow) JOptionPane.showMessageDialog(frame, "Ooops, du tog bort för många bänkar. Alla fick inte plats!");
    }
}
