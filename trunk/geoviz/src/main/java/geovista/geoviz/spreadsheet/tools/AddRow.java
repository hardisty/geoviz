package geovista.geoviz.spreadsheet.tools;

/*
 * Description:
 * Date: Mar 18, 2003
 * Time: 2:16:09 PM
 * @author Jin Chen
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import geovista.geoviz.spreadsheet.table.SSTable;

public class AddRow implements ActionListener{
        DefaultTableModel tbm;
        SSTable tb;
        public AddRow(SSTable tb) {
            this.tb=tb;
            this.tbm =(DefaultTableModel)tb.getModel()   ;

        }

        public void actionPerformed (ActionEvent e){
             int row=this.tb.getSelectedRow();
             //int lastRow=tb.getRowCount()-1;//the index the newly added column
             System.out.println("");
             if (row!=-1){
                this.tbm.insertRow(row,new Vector());
                //reSetRowHeader( row)  ;
                tb.reSetRowHeader( 0)  ;
             }
             else{
                this.tbm.addRow(new Vector());
                int i=tb.getRowCount()-1 ;
                Integer v=new Integer(i+1);
                tb.setValueAt(v,i ,0);
             }




        }



    }

