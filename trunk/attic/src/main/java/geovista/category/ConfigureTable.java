/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 * The table serves only for configuring purpose. It means the table will hold only a small number of row and column.
 * @author: jin Chen
 * @date: Aug 13, 2003$
 * 
 */
package geovista.category;


import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

public class ConfigureTable extends JTable{
    //protected TableModel dataModel;


    public ConfigureTable() {
       this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
    }
    public void setModel(TableModel dataModel) {
        super.setModel(dataModel) ;
        if (dataModel!=null){
            this.dataModel= dataModel;
        }
       
    }
    public void setData(Object[][] data, String[] columnNames) {
        dataModel=new ConfigureTableModel(data,columnNames);
    }
    /**
     * Move a record up one row. The moving is actually a physical movement on dataModel
     * Assume the table hold a small data set, the movement won't have big performance problem
     *
     * @param row
     */
    public void moveRowUp(int row) {
         this.moveRow(row,true);
    }
    public void moveRowDown(int row) {
        this.moveRow(row,false);
    }
    private void moveRow(int row,boolean up) {
        int delta;
        if(up)delta=-1;
        else delta=1;
        Object[] rowData=this.getRowData(row);
       this.removeRow(row);
       this.insertRow(row+delta,rowData);
       this.setRowSelectionInterval(row+delta,row+delta);
    }
    public void addRow(Object[] rowData) {
        int lastRow=dataModel.getRowCount() ;
        insertRow(lastRow,rowData);
    }
    public void insertRow(int row, Object[] rowData) {
        ((ConfigureTableModel)this.dataModel).insertRow(row,rowData);
    }
    public void removeRow(int row) {
          ((ConfigureTableModel)this.dataModel).removeRow(row);
    }
    public void setValueAt(Object aValue, int row, String columnName) {

            int col=((ConfigureTableModel)this.getModel()).getColumnIndex(columnName);
            this.setValueAt(aValue,row,col);
    }
    public Object getValueAt(int row,String columnName){
        int col=((ConfigureTableModel)this.getModel()).getColumnIndex(columnName);
        return this.getValueAt(row,col);
    }
    /**
     * The order of element in array reflect the order the elements are located in the column
     * @param columnName
     * @return
     */
    public Object[] getSortValueAtColumn(String columnName){
          int numRow=this.getRowCount() ;
          Object[] obs=new Object[numRow];
          for (int i=0;i<obs.length ;i++){
              obs[i]=this.getValueAt(i,columnName);
          }
          return obs;
    }
    public Object[] getRowData(int row){
        return ((ConfigureTableModel)this.getModel()).getRowData(row);
    }
    /**
     * See if a record already contained in the table
     * @param pkColumnName the name of the column which serve as primary key
     * @param value        the primary key value of given record
     * @return
     */
    public boolean isRecordContained(String pkColumnName,String value){
        for (int i=0;i<this.getRowCount() ;i++){
            String pk=(String) this.getValueAt(i,pkColumnName);
            if(value.equals(pk) )
                return true;
        }
        return false;
    }





}
