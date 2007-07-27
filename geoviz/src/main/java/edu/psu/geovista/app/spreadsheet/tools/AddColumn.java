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
 *  For now just insert 1 column
 */
public class AddColumn implements ActionListener{
        int NumOfInsert=1;//Number of Column to be inserted. for now just insert one column each time
        SSTableModel tbm;
        SSTable tb;
        public AddColumn(SSTable tb) {
            this.tb=tb;
            tbm=(SSTableModel)tb.getModel();
            //tbm=(SSTableModel) SSTableModel.getInstance() ;

        }

        public void actionPerformed (ActionEvent e){

             TableColumnModel tcm = tb.getColumnModel();
             int col=tb.getSelectedColumn() ;
             TableColumn  selectedColumn;
             int lastCol=tb.getColumnCount()-1;
             if (col==-1){
                   selectedColumn = tcm.getColumn(lastCol);

             }
             else if(col==0){//can't insert before the first column
                 //JOptionPane.showInternalMessageDialog(tb,"Can't insert a column before the first column");
                 return;

             }
             else{//Select no column, by default select the last column
                 selectedColumn = tcm.getColumn(col);
             }


             for (int i=0;i<NumOfInsert;i++){
                 int  newColIndex=lastCol+i+1;
                 TableColumn newCol=new TableColumn(newColIndex,selectedColumn.getWidth() );
                 tcm.addColumn(newCol);
                 tbm.addColumn(newCol.getHeaderValue()) ;
                 if (col!=-1){ //select a column
                    lastCol=tb.getColumnCount()-1;
                    this.tb.moveColumn(lastCol,col);  //This a TABLE do moveColumn, NOT tablModel
                 }
             }
            tb.reSetColumnHeader() ;
            //Reset Header
            //TableColumnModel tcmd=tb.getColumnModel() ;
            /*
            for (int i=1;i< tcm.getColumnCount() ;i++){
                TableColumn tbcol=tcm.getColumn(i);
                tbcol.setHeaderValue(Cell.translateColumn(i) );
            }        */

        }

    }
