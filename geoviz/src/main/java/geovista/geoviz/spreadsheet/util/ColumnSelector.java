package geovista.geoviz.spreadsheet.util;

/*
 * Description:
 * Date: Mar 22, 2003
 * Time: 2:01:56 PM
 * @author Jin Chen
 */

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.table.TableColumnModel;

import geovista.geoviz.spreadsheet.table.SSTable;
import geovista.geoviz.spreadsheet.table.SSTableModel;

public class ColumnSelector extends MouseAdapter
                            implements MouseMotionListener{
    private SSTable table;

    public ColumnSelector(SSTable table) {

        super();
        this.table =table;
    }


    public void mouseClicked(MouseEvent e) {
	    TableColumnModel colModel =
		table.getColumnModel();
	    int mcol =
		colModel.getColumn(colModel.getColumnIndexAtX(e.getX())).getModelIndex();
        //int col=Cell.transColModelToView(mcol);//column index in view
        int col=this.table.transColModelToView(mcol);

	    int rowCount = table.getRowCount();
	    table.setRowSelectionInterval(SSTableModel.BASE_ROW , rowCount - 1);

	    if (col < 1) {
            table.setColumnSelectionInterval(0, table.getColumnCount()-1);
        }
	    else   {
            table.setColumnSelectionInterval(col, col);
        }
	}

    public void mouseDragged(MouseEvent e){
       
    }
    public void mouseMoved(MouseEvent e){}
}

