package edu.psu.geovista.app.spreadsheet.tools;

/*
 * Date: Mar 19, 2003
 * Time: 10:48:33 PM
 * @author Jin Chen
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import edu.psu.geovista.app.spreadsheet.table.SSTable;

public class RemoveRow implements ActionListener{
    DefaultTableModel tbm;
        JTable tb;
        public RemoveRow(JTable tb) {
            this.tb=tb;
            this.tbm =(DefaultTableModel)tb.getModel()   ;

        }
        public void actionPerformed (ActionEvent e){
            int row=this.tb.getSelectedRow();
             if (row!=-1){
                 tbm.removeRow(row);
             }
            reSetRowHeader( row)  ;
        }

        private void reSetRowHeader(int row){
            //TableColumnModel tcmd=tb.getColumnModel() ;
            //TableColumn rowHeader=tcmd.getColumn(0);
            for(int i=row;i<tb.getRowCount() ;i++){
                int viewRow=i+1;
                Integer v=new Integer(viewRow);
                tb.setValueAt(v,SSTable.transRowViewToTable(viewRow) ,0);
            }
        }
}
