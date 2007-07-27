package edu.psu.geovista.app.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class MyTools {

    private MyTools() {
    }

    public static void setActualPreferredColumnWidths(JTable table) {
        int columnCount = table.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            TableColumn c = table.getColumnModel().getColumn(i);
            int w = (getActualPreferredColumnWidth(c, table) * 10) / 9;
            c.setMinWidth(w / 100);
            c.setMaxWidth(10 * w);
            c.setPreferredWidth(w);
            c.setWidth(w);
            c.setResizable(true);
            table.setAutoResizeMode(0);
        }
    }

    public static int getActualPreferredColumnWidth(TableColumn col, JTable table) {
        int hw = columnHeaderWidth(col, table);
        int cw = widestCellInColumn(col, table);
        return hw <= cw ? cw : hw;
    }

    public static int columnHeaderWidth(TableColumn col, JTable table) {
        TableCellRenderer renderer = col.getHeaderRenderer();
        Component comp = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);
        return comp.getPreferredSize().width;
    }

    public static int widestCellInColumn(TableColumn col, JTable table) {
        int c = col.getModelIndex();
        int width = 0;
        int maxw = 0;
        for(int r = 0; r < table.getRowCount(); r++) {
            TableCellRenderer renderer = table.getCellRenderer(r, c);
            Component comp = renderer.getTableCellRendererComponent(table, table.getValueAt(r, c), false, false, r, c);
            width = comp.getPreferredSize().width;
            maxw = width <= maxw ? maxw : width;
        }
		
        return maxw;
    }
}
