package view;

import javax.swing.*;
import java.awt.*;

public class CorridorSpace extends JPanel {
    private final static int width = ClassRoom2.corridorhWidth;
    private final static int height = ClassRoom2.benchHeight;

    public CorridorSpace() {
        setPreferredSize(new Dimension(width, height));
        setBackground(new Color(224,215,196));
    }
}
