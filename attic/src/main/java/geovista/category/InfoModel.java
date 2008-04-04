package geovista.category;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

class InfoModel extends AbstractTableModel {
    protected static int NUM_COLUMNS = 4;
    protected static int START_NUM_ROWS = 5;
    protected int nextEmptyRow = 0;
    protected int numRows = 0;

    static final public String userNameStr = "UserInfo";
    static final public String preferedColorStr = "PreferedColor";
    static final public String recordsStr = "Records";
    static final public String exampleTypeStr = "ExampleType";

    protected Vector data = null;

    public InfoModel() {
        data = new Vector();
    }

    public String getColumnName(int column) {
	switch (column) {
	  case 0:
	    return userNameStr;
	  case 1:
	    return preferedColorStr;
	  case 2:
	    return recordsStr;
	  case 3:
	    return exampleTypeStr;
	}
	return "";
    }

    //XXX Should this really be synchronized?
    public synchronized int getColumnCount() {
        return NUM_COLUMNS;
    }

    public synchronized int getRowCount() {
        /*if (numRows < START_NUM_ROWS) {
            return START_NUM_ROWS;
        } else {
            return numRows;
        }*/
		return data.size();
    }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 1) {
                return false;
            } else {
                return true;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {

            if (((Vector)data.elementAt(0)).get(col) instanceof Integer
                    && !(value instanceof Integer)) {
                try {
                    //data[row][col] = new Integer(value.toString());
					//((Vector)data.elementAt(row)).set(col, new Integer(value.toString()));
                    //fireTableCellUpdated(row, col);
                } catch (NumberFormatException e) {
                    //JOptionPane.showMessageDialog(CategoryRecords.this,
                    //    "The \"" + getColumnName(col)
                    //   + "\" column accepts only integer values.");
                }
            } else {
                //data[row][col] = value;
				((Vector)data.elementAt(row)).set(col, value);
                fireTableCellUpdated(row, col);
            }
        }

    public synchronized Object getValueAt(int row, int column) {
			try {
            Vector p = (Vector)data.elementAt(row);
						return p.get(column);
			} catch (Exception e) {
			}
			return "";
    }

	public void deleteRows (int[] rowsSelected){
		if (rowsSelected == null){
			return;
		}
		int[] rowsDeleted = rowsSelected;
		for (int i=0; i < rowsDeleted.length; i++){
			data.removeElementAt(rowsDeleted[i]);
			fireTableRowsDeleted(rowsDeleted[i], rowsDeleted[i]);
		}
	}

    public synchronized void updateRecord(Vector selectionRecord) {
        int index = -1;
        boolean found = false;
		if (found) { //update old player
	    data.setElementAt(selectionRecord, index);
        } else { //add new player
	    if (numRows <= nextEmptyRow) {
		//add a row
                numRows++;
            }
            index = nextEmptyRow;
	    data.addElement(selectionRecord.clone());
	}

        nextEmptyRow++;

	    fireTableRowsInserted(index, index);
    }

    public synchronized void clear() {
	int oldNumRows = numRows;

        numRows = START_NUM_ROWS;
	data.removeAllElements();
        nextEmptyRow = 0;

	if (oldNumRows > START_NUM_ROWS) {
	    fireTableRowsDeleted(START_NUM_ROWS, oldNumRows - 1);
	}
	fireTableRowsUpdated(0, START_NUM_ROWS - 1);
    }
}
