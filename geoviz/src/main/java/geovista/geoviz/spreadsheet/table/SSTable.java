package geovista.geoviz.spreadsheet.table;

/*
 * Description:
 * Date: Mar 13, 2003
 * Time: 1:12:36 PM
 * @author Jin Chen
 */

import java.awt.Color;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

//import geovista.geoviz.spreadsheet.formula.FormulaEditor;
import geovista.geoviz.spreadsheet.SpreadSheetBean;
import geovista.geoviz.spreadsheet.event.SSTableModelEvent;
import geovista.geoviz.spreadsheet.formula.Cell;
import geovista.geoviz.spreadsheet.functions.FunctionManager;
import geovista.geoviz.spreadsheet.util.Debug;

public class SSTable extends JTable {

    //private FormulaEditor fe; //on tool bar
    private Object bean;//
    private FunctionManager funManager;

    //geovista.geoviz.spreadsheet.model.TableColumnSort tcs;
    public SSTable(SSTableModel tm) {
        super(tm);
        tm.setTable(this);
        funManager=new FunctionManager(this);

        //tcs=new geovista.geoviz.spreadsheet.model.TableColumnSort(this);
    }
    /********************************************************************
     *                Event handler
     ********************************************************************/

    /**
     * called by BasicTableUI$MouseInputHandler: table.changeSelection(row, column, e.isControlDown(), e.isShiftDown());
     * @param  rowIndex   affects the selection at <code>row</code>
     * @param  columnIndex  affects the selection at <code>column</code>
     */

    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {

        super.changeSelection(rowIndex,columnIndex,toggle,extend);  //MUST at beginning
        if (!toggle&&!extend){//normal selecting without holding Ctrl and Shift
            //Debug.println("select:"+rowIndex+","+columnIndex);

            Cell cell=(Cell)this.getValueAt(rowIndex,columnIndex);
            if(cell.isFormula() ){
                //this.fe.getFormulaField().setText("="+cell.getFormula().toString() );
            }
            else{
                 //this.fe.getFormulaField().setText(cell.toString() );
            }


        }
        if (columnIndex<SSTableModel.BASE_COL&&rowIndex>=0) {
            if (!toggle&&!extend){
                 this.setColumnSelectionInterval(0,this.getColumnCount()-1 );
                 this.setRowSelectionInterval(rowIndex,rowIndex);
                 return;
            }
            int selectedRow=this.getSelectedRow() ;
            if (extend){
                setRowSelectionInterval(selectedRow , rowIndex);
                this.setColumnSelectionInterval(0,this.getColumnCount()-1 );
            }
            //Not need worry about toggle since if you have selected a row, the next selection would be a row
            //this.setCellSelectionEnabled(false);
            /*setColumnSelectionInterval(0, getColumnCount()-1);
            setRowSelectionInterval(selectedRow, selectedRow);   */
            /*setColumnSelectionAllowed(false);
            setRowSelectionAllowed(true);
            setCellSelectionEnabled(false);  */
        }
        else{
            //this.setCellSelectionEnabled(true);
        }


    }

    public void setSelectedRow(int rowIndex){
        this.setColumnSelectionInterval(0,this.getColumnCount()-1 );
        this.setRowSelectionInterval(rowIndex,rowIndex);

    }
    public void addSelectedRow(int rowIndex){
        addRowSelectionInterval(rowIndex,rowIndex);
        setColumnSelectionInterval(0,getColumnCount()-1 );

    }
    public void tableChanged(TableModelEvent e) {
        if (e.getType()==SSTableModelEvent.RESET_DATA ){
            clearSelection();
            //rowModel = null;
            createDefaultColumnsFromModel();
            reSetRowHeader() ;
            reSetColumnHeader() ;
            return;
        }
        else{
            super.tableChanged(e)     ;
        }

    }

    public void valueChanged(ListSelectionEvent e) {
        super.valueChanged(e);
        if (bean instanceof SpreadSheetBean){
            ((SpreadSheetBean)bean).stateChanged(null);

        }
    }
    /********************************************************************
     *                Sort
     ********************************************************************/
    public void sortTable(int col, boolean ascend){
        //tcs.sortRows(col,ascend);
    }
    public void clearSort(){
        ((SSTableModel)this.getModel()).clearSort() ;
         this.revaluate() ;
         this.reSetRowHeader(0);//do it from the every beginning
    }
    public void sort(boolean ascend){

        int[] rows= this.getSelectedRows() ;
        int[] cols= this.getSelectedColumns() ;

        int rs=rows.length ;  // row  selected
        int cs=cols.length ;
        int rc=this.getRowCount() ;//row count
        int cc=this.getColumnCount() ;//col count
        SSTableModel tm=(SSTableModel)this.getModel() ;

        if (rs<=0||cs<=0) return; //Not select sorting range yet
        if (rs==rc&&cs==cc){//select entire table
            Debug.println("select entire table");

            int col=this.showSortDialog() ;
            if (col<0) return;//cancel sort
            /*

            this.sortRows(col,false);
            */
            int mcol=this.convertColumnIndexToModel(col);
            tm.sortTable(mcol,ascend);
        }
         else if (cs==cc&&rs >0){
            // rs >0&&rs<rc
            //select some rows
/*for (int i=0;i<rows.length ;i++){
                int row=rows[i];
                sr.setRow(row);
            }
            sr.setType(geovista.geoviz.spreadsheet.model.SortRange.ROWS );  */
            int col=this.showSortDialog() ;
            if (col<0) return;//cancel sort
            /*this.createSortMap(0);//use table sort
            this.sortRows(col,false);   */
            int mcol=this.convertColumnIndexToModel(col);
            tm.sortRows(mcol,rows,ascend);
            Debug.println("select some rows");
        }
        else if (rs==rc&&cs>0){//select some columns
/*for (int i=0;i<cols.length ;i++){
                int col=cols[i];//selected column in table view
                sr.setColumn(col);
            }
            sr.setType(geovista.geoviz.spreadsheet.model.SortRange.COLUMNS ); */
            int col=this.showSortDialog() ;
            if (col<0) return;//cancel sort
            /*
            int[] selCols=table.getSelectedColumns() ;
            for (int i=0;i<selCols.length ;i++){
               this.createSortMap(selCols[i]);

            }       */
            int mcol=this.convertColumnIndexToModel(col);
            int[] mcols=new int[cols.length ];
            for (int i=0;i<mcols.length ;i++){
                mcols[i]=this.convertColumnIndexToModel(cols[i]);
            }
            tm.sortColumns(mcol,mcols,ascend);
            Debug.println("select some columns");
        }
        else{

            if (this.isArrayContinue(rows)&&
                this.isArrayContinue(cols)){//a rectangle range
                int col=this.showSortDialog() ;
                if (col<0) return;//cancel sort
                int mcol=this.convertColumnIndexToModel(col);
                int[] mcols=new int[cols.length ];
                for (int i=0;i<mcols.length ;i++){
                    mcols[i]=this.convertColumnIndexToModel(cols[i]);
                }
                int[]mrows=new int[rows.length ];
                for (int i=0;i<mrows.length ;i++){
/*//mrows[i]=Cell.transRowViewToTable(rows[i]);//
                    //From table perspective, table' row index=Model's row index
                    //(which may be the sort geovista.geoviz.spreadsheet.model instead of real geovista.geoviz.spreadsheet.model)
                      Now rows[i] is table's row index=Model row index
                    */
                    mrows[i]=rows[i];
                }
                tm.sortRange(mcol,mrows, mcols,ascend);
                Debug.println("select a range");


            }
            else{
                //JOptionPane.showConfirmDialog(SpreadSheetBean.getTableInstance(),"Unable deal with the range");
                JOptionPane.showConfirmDialog(this,"Unable deal with the range");
            }

            //select a range
/*
            for (int i=0;i<cols.length ;i++){
                int col=cols[i];//selected column in table view
                sr.setColumn(col);
            }
            for (int i=0;i<rows.length ;i++){
                int row=rows[i];
                sr.setRow(row);

            }
            sr.setType(geovista.geoviz.spreadsheet.model.SortRange.RANGE );
            util.Debug.println("select a range");  */
        }
        this.revaluate() ;
        this.reSetRowHeader(0);//do it from the every beginning

    }
    /**
     *  True if the element of array meet a[i+1]=a[i]+1;
     */
    private boolean isArrayContinue(int[] a){
        if (a.length <=0)return false;
        int ai=a[0];
        for (int i=1;i<a.length ;i++){
            if ((a[i]-ai)!=1){
                return false;
            }
            ai=a[i];

        }
        return true;

    }


    /**
     *  Ask to select a  column based on which do sorting
     *  return the column be selected in Table View
     *              -1 mean cancel
     */
    private int showSortDialog() {
        int selectedCol;
        int numOfCol=this.getColumnCount();
        String[] options=new String[numOfCol] ;
        for (int i=0;i<options.length ;i++){
            String colName=translateVVColumn(i);
            options[i]=colName;
        }
        //JFrame mf=(JFrame)SwingUtilities.getAncestorOfClass(JFrame.class,SpreadSheetBean.getTableInstance() );
        JFrame mf=(JFrame)SwingUtilities.getAncestorOfClass(JFrame.class,this );
        String selectedValue =(String)
           JOptionPane.showInputDialog(mf ,"Sort by", "Sort",
                   JOptionPane.INFORMATION_MESSAGE ,null,options,options[1]) ;
        if (selectedValue!=null){
            selectedCol=translateVVColumn(selectedValue);
        }
        else{
            selectedCol=-1;
        }

        Debug.println("selected column:"+selectedCol);
        return selectedCol;

    }
    public void reSetRowHeader(int row){
            //TableColumnModel tcmd=tb.getColumnModel() ;
            //TableColumn rowHeader=tcmd.getColumn(0);
            for(int i=row;i<getRowCount() ;i++){
                int viewRow=i+1;
                Integer v=new Integer(viewRow);
                setValueAt(v,transRowViewToTable(viewRow) ,0);
            }
    }
    public void reSetRowHeader(){
               TableColumnModel tcmd=getColumnModel() ;
               TableColumn rowHeader=tcmd.getColumn(0);
               rowHeader.setHeaderValue("");

               rowHeader.setPreferredWidth(30);

               DefaultTableCellRenderer rhRender=new DefaultTableCellRenderer();
               rhRender.setBackground(Color.LIGHT_GRAY );
               rowHeader.setCellRenderer(rhRender);
               for (int i=0;i<getRowCount() ;i++){
                   Integer v=new Integer(i+1);
                   setValueAt(v,i,0);
               }
       }

    /**
     *  Ruturn value at cell in view(what you see )
     */
    public Object getValueAtIndex(int row, int column) {
        int rowindex=transRowViewToTable(row);
        return super.getValueAt(rowindex,column);

    }
     /**
     *  Ruturn value at cell in view(what you see )
     */
    public void setValueAtIndex(Object value,int row, int column) {
        int rowindex=transRowViewToTable(row);
        super.setValueAt(value,rowindex,column);

    }
    //auto update geovista.geoviz.spreadsheet.formula value
    public void editingStopped(ChangeEvent e) {
         Debug.println("table.editingStopped(ChangeEvent e) ");
         super.editingStopped(e);
         revaluate() ;
     }

    /********************************************************************
     *                Temp for testing event
     *******************************************************************
    public void tableChanged(TableModelEvent e) {
        Debug.println("tableChanged.e.getType():"+e.getType());
        super.tableChanged(e);
    }
     public void valueChanged(ListSelectionEvent e) {
        Debug.println(" valueChanged "+e.toString());
        super.valueChanged(e);
     }
     public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Debug.println("row,column"+row+","+column);
        return super.prepareRenderer(renderer,row,column) ;
     }
     public void columnSelectionChanged(ListSelectionEvent e) {
     }       */
//    public FormulaEditor getFormulaEditor() {
//        return fe;
//    }
//
//    public void setFormulaEditor(FormulaEditor fe) {
//        this.fe = fe;
//    }

    public void reSetColumnHeader() {
                    //Reset Header
            TableColumnModel tcm=getColumnModel() ;
            SSTableModel tm=(SSTableModel)this.getModel() ;
            for (int i=1;i< tcm.getColumnCount() ;i++){
                TableColumn tbcol=tcm.getColumn(i);
                //String colIndex= Cell.translateColumn(i);
                //String colIndex= translateColumn(i);
                //String colname=tm.getColumnName(Cell.transColViewToModel(i)) ;
                String colname=tm.getColumnName(transColViewToModel(i)) ;
                tbcol.setHeaderValue(colname );
                /*
                if (colname!=null&&colname.trim().length() >0&&
                    !colIndex.equalsIgnoreCase(colname) ){
                    String name="("+colIndex+")"+colname;
                    tbcol.setHeaderValue(name );
                }
                else{
                     tbcol.setHeaderValue(colIndex );
                }     */
            }
    }


    public void revaluate(){
        super.repaint() ;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }
    /********************************************************************
     *                Model <=> View
     ********************************************************************/
    public int transColModelToView(int modelCol){
            //JTable tb=SpreadSheetBean.getTableInstance() ;
            //JTable tb=this.getDataModel().getTable() ;
            //TableColumnModel tbm=tb.getColumnModel();
            TableColumnModel tbm=getColumnModel();
            Enumeration e=tbm.getColumns() ;
            int i=0;
            while(e.hasMoreElements() ){
                TableColumn tbcol=(TableColumn)e.nextElement() ;
                if (tbcol.getModelIndex() ==modelCol){
                   return i;
                }
                i++;
            }
            return i;
            //return modelCol- geovista.geoviz.spreadsheet.model.SSTableModel.BASE_COL +1 ;//jin
    }

    /**
         *  Translate Column address from view(Table) to Model(TableModel)
         *  e.g. Table's row 1 => TableModel's row 0 . 1=>0
         */
        public int transColViewToModel(int  viewCol){
                //JTable tb=SpreadSheetBean.getTableInstance() ;
                //JTable tb=this.getDataModel().getTable() ;
                //TableColumnModel tbm=tb.getColumnModel();
                TableColumnModel tbm=getColumnModel();
                TableColumn tbcol=tbm.getColumn(viewCol);
                int col=tbcol.getModelIndex() ;
                return col;
            //return  viewCol + geovista.geoviz.spreadsheet.model.SSTableModel.BASE_COL -1 ;//jin
        }
        /**
         * This translates the string form of column into column number ('A' -> 1)
         * View->Model
         * @param column the string value of the column to be converted
         * @return the int column address in Model(TableModel)
         */
        public  int translateColumn(String column) {
            int col=SSTable.translateVVColumn(column);
            return  transColViewToModel(col);
        }
       /**
         * This translates the int form of column into column string (1 -> 'A')
         *
         * @param column the col address in Model
         * @return the string value of the column
         */
        public String translateColumn(int column) {
            return  SSTable.translateVVColumn(column);
        }
    /**
         *  Translate Row address from Model(TableModel) to view(Table)
         *  e.g. TableModel's row 0 => Table's row0=> Table's row index 1 . 1=>0
         */
        public static int transRowTableToView(int modelRow){
            return modelRow- SSTableModel.BASE_ROW+1 ;//jin

        }

    /**
         *  Translate Row address from view(Index) to Table
         *  e.g. Table's row index 1=> Table's row 0
         */
        public static int transRowViewToTable(int  viewRow){
                return  viewRow + SSTableModel.BASE_ROW-1 ;//jin
        }

    /**
     * This translates the string form of column into column number ('A' -> 1)
     *
     * @param column the string value of the column to be converted
     * @return the int column address in view(Table)
     */
    public static int translateVVColumn(String column) {
        int col = 0;

        for (int i = 0; i < column.length(); i++) {
            col  = col * 26 + (column.charAt(i) - 'A' + 1);
        }
        return col;
        //return col + baseCol -1;
        //return col+geovista.geoviz.spreadsheet.model.SSTableModel.BASE_COL ;
    }

    /**
     * This translates the int form of column into column string (1 -> 'A')
     *
     * @param column the col address in view
     * @return the string value of the column
     */
    public static String translateVVColumn(int column) {

	//column = column - baseCol +1;
        //column = column - geovista.geoviz.spreadsheet.model.SSTableModel.BASE_COL + 1;
        if (column < 1)
            return null;

        StringBuffer buf = new StringBuffer();

        int div = 1;

        while (div > 0) {
            div = (column - 1) / 26;
            buf.insert(0, (char) ('A' + (column - 1 - div * 26)));
            column = div;
        }

        return buf.toString();
    }

    public FunctionManager getFunManager() {
        return funManager;
    }

    public void setFunManager(FunctionManager funManager) {
        this.funManager = funManager;
    }
}
