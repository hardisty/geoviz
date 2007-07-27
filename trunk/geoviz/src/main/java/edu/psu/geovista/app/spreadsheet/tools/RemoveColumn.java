package edu.psu.geovista.app.spreadsheet.tools;


/*
 * Description:
 * Date: Mar 18, 2003
 * Time: 2:15:21 PM
 * @author Jin Chen
 */

    import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import edu.psu.geovista.app.spreadsheet.table.SSTable;
import edu.psu.geovista.app.spreadsheet.table.SSTableModel;

    /**
     *  For now just delete 1 column
     */
    public class RemoveColumn  implements ActionListener{
            int NumOfInsert=1;//Number of Column to be inserted. for now just insert one column each time
            SSTableModel tbm;
            SSTable tb;
            public RemoveColumn(SSTable tb) {
                this.tb=tb;
                tbm=(SSTableModel)tb.getModel();
                //tbm=(SSTableModel) SSTableModel.getInstance() ;

            }

            public void actionPerformed (ActionEvent e){

                 TableColumnModel tcm = tb.getColumnModel();
                 SSTableModel tm=(SSTableModel)tb.getModel() ;
                 int viewCol=tb.getSelectedColumn() ;
                 TableColumn  selectedColumn;
                 if(viewCol<=0){//can't delete
                     //JOptionPane.showInternalMessageDialog(tb,"Can't insert a column before the first column");
                     return;

                 }
                 else{//Select no column, by default select the last column
                     selectedColumn = tcm.getColumn(viewCol);
                 }
                int modelCol=selectedColumn.getModelIndex() ;
                tm.removeColumn(modelCol);
                tcm.removeColumn(selectedColumn);
                int numOfDelete=1;//number of column to be deleted
                //Reset ModelIndex
                for (int i=1;i< tcm.getColumnCount() ;i++){
                    TableColumn tbcol=tcm.getColumn(i);
                    int modelIndex=tbcol.getModelIndex() ;
                    if (modelIndex>modelCol){
                       tbcol.setModelIndex(modelIndex-numOfDelete);
                    }
                }

                //Reset Header
                for (int i=1;i< tcm.getColumnCount() ;i++){
                    TableColumn tbcol=tcm.getColumn(i);
                    //tbcol.setHeaderValue(Cell.translateColumn(i) );
                    tbcol.setHeaderValue(this.tb.translateColumn(i) );
                }

            }

}
