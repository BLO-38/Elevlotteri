package view;

import javax.swing.*;
import java.awt.*;

public class CorridorSpace extends JPanel {
    private final static int width = ClassRoom4.corridorhWidth;
    private final static int height = Bench.DEFAULT_HEIGHT;

    public CorridorSpace() {
        setPreferredSize(new Dimension(width, height));
        setBackground(Bench.BENCH_NORMAL_BACKGROUND);
    }

}
