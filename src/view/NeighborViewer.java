package view;

import databasen.SelectHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class NeighborViewer {
    private final String cl;
    public NeighborViewer(String cl) {
        this.cl = cl;
        setupWindow();
    }

    public void setupWindow() {
        int numberWidth = 30, height = 20, nameWidth = 70;
        String[][] matrix = SelectHandler.getNeighbors(cl);
        String[] headers = new String[matrix.length];
        JFrame frame = new JFrame();
        DefaultTableModel model = new DefaultTableModel(matrix,headers);
        JTable table = new JTable(model);
        table.setTableHeader(null);
        table.setRowHeight(height);
        TableColumnModel columnModel = table.getColumnModel();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        columnModel.getColumn(0).setPreferredWidth(nameWidth);
        for (int i = 1; i < matrix.length; i++) {
            columnModel.getColumn(i).setPreferredWidth(numberWidth);
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        int paneWidth = (matrix.length-1)*numberWidth + nameWidth;
        int paneHeight = matrix.length*height + 5;
        scrollPane.setPreferredSize(new Dimension(paneWidth,paneHeight));
        frame.add(scrollPane);
        frame.pack();
        frame.setVisible(true);
    }
}
