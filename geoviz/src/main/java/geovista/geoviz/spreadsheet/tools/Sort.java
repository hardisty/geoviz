package geovista.geoviz.spreadsheet.tools;

/*
 * Description:
 * Date: Mar 20, 2003
 * Time: 10:27:10 PM
 * @author Jin Chen
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import geovista.geoviz.spreadsheet.table.SSTable;
import geovista.geoviz.spreadsheet.table.SSTableModel;

public class Sort implements ActionListener{
        public static final int CLEAR=0;
        public static final int ASCEND=1;
        public static final int DESCEND=2;

        int action;
        SSTableModel tbm;
        SSTable tb;
        public Sort(SSTable tb,int action) {
            this.tb=tb;
            tbm=(SSTableModel)tb.getModel();
            //tbm=(SSTableModel) SSTableModel.getInstance() ;
            this.action =action;

        }

        public void actionPerformed (ActionEvent e){
            if (this.action ==DESCEND) {    //0
                //tb.sortTable(col,false);
                tb.sort(false);
            }
            else if (this.action ==ASCEND){//1
                //tb.sortTable(col,true);
                tb.sort(true) ;
            }
            else if(this.action ==CLEAR){
                tb.clearSort() ;
            }
            else{
                tb.sort(false);
            }

        }
}
