package edu.psu.geovista.app.spreadsheet.table;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author jin chen
 * @version 1.0
 */
import java.awt.Point;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import edu.psu.geovista.app.spreadsheet.event.SSTableModelEvent;
import edu.psu.geovista.app.spreadsheet.exception.ParserException;
import edu.psu.geovista.app.spreadsheet.formula.Cell;
import edu.psu.geovista.app.spreadsheet.formula.Formula;
import edu.psu.geovista.app.spreadsheet.util.Debug;

public class SSTableModel extends DefaultTableModel{

  public static final int  NUM_ROW=40;//initial number of rows
  public static final int  NUM_COL=10;//initial number of columns
  public static final int  BASE_ROW=0;// The number of rows that not count by index
  public static final int  BASE_COL=1; //The number of columns that not count by index

 // private static  SSTableModel tbm;
  private TableColumnSort sortModel;
  private SSTable table;

  public SSTableModel() {
      super ((NUM_ROW+ BASE_ROW),(NUM_COL+BASE_COL));
      sortModel=new  TableColumnSort(this);


  }
  public void  removeColumn(int colIndex){
      int rowSize=dataVector.size() ;
      for(int i = 0; i < rowSize; i++) {
		  Vector row = (Vector)dataVector.elementAt(i);
          Cell c=(Cell)row.elementAt(colIndex);
          if (c!=null){ //
            c.setDeleted(true);
          }
          else{
            //Can't garantee each Cell is not-null since Cell is initial by Model.getValueAt()
            // It is called only when cell (is visible) is painted. Those hidden Cell not painted at beginning
            Debug.println("Cell:"+i+","+colIndex+" is null");
          }
		  row.remove(colIndex);
	  }
      fireTableStructureChanged(); //talbe.tableChanged() will act

  }


  public Object getRealModelValueAt( int row, int column){
      System.out.println("getValueAt:"+row+","+column);

         Object o= super.getValueAt(row,column) ;
         Cell cell;
          if (o==null||!(o instanceof Cell) ){
               cell=this.processValueAt(o,row,column);

         }
         else{
                cell=(Cell)o;
          }


         return cell;
  }
  public Object getValueAt( int srow, int scolumn){
       //Can't garantee each Cell is not-null since Cell is initial by Model.getValueAt()
        // It is called only when cell (is visible) is painted. Those hidden Cell not painted at beginning
       int row,column;// row, column index in real edu.psu.geovista.app.spreadsheet.model
       //sort->
       if (this.sortModel.isTableSort() ){
            Point cellP=sortModel.getCellPointAt(srow,scolumn);
            row=(int)cellP.getX() ;
            column=(int)cellP.getY();
       }
        else{
           row=srow;column=scolumn;
       }
       //Now row,column are index of real model
       Object o= super.getValueAt(row,column) ;
         Cell cell;
         if (o==null||!(o instanceof Cell) ){
               cell=this.processValueAt(o,row,column);

         }
         else{
                cell=(Cell)o;
          }
         return cell;
  }
  /**
   *  @param    row  index in Model(for outside point view)
   */
  public void setValueAt(Object data, int row, int col){
       if (this.sortModel.isTableSort() ){
            Point cellP=sortModel.getCellPointAt(row,col);  //return address in real model
            row=(int)cellP.getX() ;
            col=(int)cellP.getY();

        }
      // Transfer row, col to indexs of real Model
      //wrapper everything(edu.psu.geovista.app.spreadsheet.formula/number/string) in cell object
       Cell cell= processValueAt(data,row,col);
       super.setValueAt(cell,row,col);

  }

  /**************************************************************************
    *   Process value
    ************************************************************************/
    private Cell createCell(int row, int col) {
             Vector rowV=(Vector)this.dataVector.elementAt(row);
             Cell cell=new Cell(rowV,this);
             super.setValueAt(cell,row,col);
             return cell;
    }
    /** This method sets the value of the cell specified with these coordinates
     * to aValue. It does the parsing of string objects to see if they are
     * numbers or formulas. If you do not want any parsing at all, use
     * setCellAt.
     * @param aValue value to set cell to
     * @param row index of REAL model, not sort model
     * @param col index of REAL model, not sort model
     */
    private Cell processValueAt(Object aValue, int row, int col) {
        //Wrapper raw data in Cell, make sure each obj in table's cell is a Cell obj
        if (aValue == null){
             Cell cell=this.createCell(row,col);


             super.setValueAt(cell,row,col);
             return cell;
           //super.getValueAt(row,col);//set a edu.psu.geovista.app.spreadsheet.formula.Cell with null value
            //aValue = new String("");
            //super.setValueAt(aValue,row,col);
            //return null;
         }
        //assert (aValue !=null ): "Null input found";
        Cell cell;
        Object o=super.getValueAt(row,col);
        if (o instanceof Cell){
            cell=(Cell)o;
        }
        else{
            cell=this.createCell(row,col);
        }
        //Wrapper raw data in Cell<--


        if (aValue instanceof String) {//Always String ???

            String input = (String)aValue;
            /* try making it a edu.psu.geovista.app.spreadsheet.formula */
            /*if(input.trim().equals("")){
            }
            else */
            if (input.startsWith("=")) {//1. edu.psu.geovista.app.spreadsheet.formula.Formula
                    Formula form = null;
                    try { //create edu.psu.geovista.app.spreadsheet.formula and its value and put in a cell

                        form = new Formula(cell,input.substring(1),
                                   row, col); //may throw edu.psu.geovista.app.spreadsheet.exception.ParserException if get invalid edu.psu.geovista.app.spreadsheet.formula

                        cell.setFormula(form);
                        cell.setAsFormula(true); //Must call it explicitly, can't include in setFormula()
                        //cell.evaluate();//
                    }
                    catch (ParserException e) { //if enter a invalid edu.psu.geovista.app.spreadsheet.formula
                        // Still keep the fomula, so user can correct it later no parsing
                        form = new Formula(input.substring(1), row, col,e);
                        cell.setFormula(form);
                        cell.setValue(e);  //used for disply error message
                        cell.setAsFormula(true); //still it is a edu.psu.geovista.app.spreadsheet.formula, but invalid(e.g.:#REF!)

                    }



             }
             else { //2. Number
                try{
                    Float number=new Float(input);
                    cell.setValue(number);
                    cell.setAsFormula(false);

                }catch (NumberFormatException e2) {
                    /* 3. all else fails treat as string */
                    //setCellAt(aValue, aRow, aColumn);
                     cell.setValue(input)  ;
                     cell.setAsFormula(false);
                }
             }//if

        }
        else{ //integer, Float ....
            cell.setValue(aValue);
        }
        return cell;

    }   //method

    /**
     * Jin: Even you use table.setDefaultEditor(edu.psu.geovista.app.spreadsheet.formula.Cell.class, new CellFomulaEditor(new JTextField()));
     *      It still not work since JTable.getDefaultEditor(Class columnClass) is passed in a Object.class, not edu.psu.geovista.app.spreadsheet.formula.Cell.class
     *      This is how getDefaultEditor() called:
     *                          editor = getDefaultEditor(getColumnClass(column));
     *      By default(in AbstractTableModel):getColumnClass return a Object.class. you MUST overide it and return a edu.psu.geovista.app.spreadsheet.formula.Cell.class
     *      This is same for setting edu.psu.geovista.app.spreadsheet.formula.Cell Render
     *
     * JTable uses this method to determine the default renderer
     * editor for each cell. This method tells JTable to use
     * CellRender and CellFormulaEditor.
     *
     * @param c the column for which we need to determine the class
     * @return edu.psu.geovista.app.spreadsheet.formula.Cell class
     */
    public Class getColumnClass(int c) {

        /* only cell objects in this TableModel */
        return Cell.class;
    }



    /**
     *  All Cells other than those in column 0( which is index) are editable.
     *
     * @param row the row coordinate
     * @param column the column coordinate
     * @return true if cell is editable
     */
    public boolean isCellEditable(int row, int column) {
        //        return !((row == 0) || (column == 0));
        return column != 0;
        //return true;
    }

    public String getColumnName(int col) {
        //Debug.println("name of column " + col);
        //String viewIndex=Cell.translateColumn(col);
        SSTable table=this.getTable();
        String modelIndex=null;
        String cn=null;

        if (col < BASE_COL)
            return "";
        else   {
            if(table!=null){
                       boolean useDefaultName=false;
                       modelIndex=SSTable.translateVVColumn(col);
                       cn=super.getColumnName(col-BASE_COL);
                       if(cn.equalsIgnoreCase("y")   )
                            Debug.println("cn=Y");
                       if (modelIndex.equalsIgnoreCase(cn)){
                           useDefaultName=true;//means no customerized column name, thus use system-created column name
                       }

                        if (cn!=null&&cn.trim().length() >0){
                            int viewIndex=this.getTable().transColModelToView(col);
                            String colName=SSTable.translateVVColumn(viewIndex);
                            if (useDefaultName){

                                return colName;
                            }
                            else{
                                cn=super.getColumnName(col-BASE_COL+1);
                                return colName+" : "+cn;
                            }
                        }
                        else{
                            return "";//String.valueOf(edu.psu.geovista.app.spreadsheet.formula.Cell.translateColumn(col));
                        }
             }
            else{
                return "";
            }

        }
    }

    public void reCalHeader(){
        System.out.println("Column Name");
        for (int i=1;i< columnIdentifiers.size();i++){
            //String colName=Cell.translateColumn(i) ;
            String colName=this.getTable().translateColumn(i) ;
            System.out.print(colName+" ");

            try {
                columnIdentifiers.set(i,colName );
            } catch (Exception e) {
                System.out.println("edu.psu.geovista.app.spreadsheet.exception happen i="+i);
                e.printStackTrace() ;
            }

        }
        System.out.println("Column Name");
        fireTableStructureChanged();
    }

    public void addColumn(Object columnName, Vector columnData) {
        super.addColumn(columnName,  columnData)     ;
    }

    public void addColumn(Object columnName) {
        addColumn(columnName, (Vector)null);
    }

    /********************************************************************
     *            sort                                                  *
     ********************************************************************/
     /**
      *  sort entire table
      */
     public void sortTable(int col,boolean ascending){

         this.sortModel.sortTable(col,ascending) ;

     }
     /**
      *  sort rows
      *
      */
     public void sortRows(int col,int[] rows, boolean ascending){

         this.sortModel.sortRows(col,rows,ascending);

     }

     public void sortColumns(int col,int[] cols,boolean ascending){
        this.sortModel.sortColumns(col,cols,ascending);
     }
     public void sortRange(int col,int[] rows,int[] cols,boolean ascending){
            this.sortModel.sortRange(col,rows, cols,ascending) ;
     }
     public void insertRow(int row, Vector rowData) {
         //super.insertRow(row,rowData);
         this.insertSortModel(row,rowData);

     }
     public void insertRow(int row, Object[] rowData) {
         this.insertSortModel(row,convertToVector(rowData));
     }
     /**
      *  Insert a row
      */
     private void insertSortModel(int row, Vector rowData) {
         if (!sortModel.isTableSort()){//no sort
            super.insertRow(row,rowData);
            return;
         }
         System.out.println("test");
         if (row==0){
             super.insertRow(row,rowData);
             Vector sorts=sortModel.getSorts();
             for (int i=0;i<sorts.size() ;i++){
                 Vector column=(Vector)sorts.elementAt(i);
                 if (column!=null){
                    column.insertElementAt(new Integer(row),row);
                    for (int j=row+1;j<column.size();j++){
                          int mx=((Integer)column.elementAt(j)).intValue() ;
                          mx++;
                          Integer newIndex=new Integer(mx);//updated edu.psu.geovista.app.spreadsheet.model index
                          column.setElementAt(newIndex,j);
                    }
                     //edu.psu.geovista.app.spreadsheet.model.TableColumnSort.showVector(column);
                    System.out.println("");
                 }
             }
         }
         else{
             Vector sorts=sortModel.getSorts();
             Vector sortColumn=null;//a column in SortModel that contain sorting info(index) for a column in real edu.psu.geovista.app.spreadsheet.model
             boolean foundSort=false;
             //1. find any valid (not null) column  !!! May not work for multi-level sort
             for (int k=0;k<sorts.size() ;k++){
                 sortColumn=(Vector)sorts.elementAt(k); //column before a row is insert to real edu.psu.geovista.app.spreadsheet.model
                 if (sortColumn!=null){
                     foundSort=true;
                     //2. Get and the reference to real edu.psu.geovista.app.spreadsheet.model rows, store the reference.
                     Vector tempCol=(Vector)sortColumn.clone() ;//new Vector(sortColumn.size() );
                     for (int i=0;i<sortColumn.size() ;i++){
                         int mx=((Integer)sortColumn.elementAt(i)).intValue() ;//row's index in real edu.psu.geovista.app.spreadsheet.model

                         Vector mrow=(Vector)this.getDataVector().elementAt(mx);//the row vector
                         tempCol.setElementAt(mrow,i);
                     }

                     //3. insert row to real edu.psu.geovista.app.spreadsheet.model
                     super.insertRow(row,rowData);

                     //4.re-evaluate the index of those rows(which obtained in step 2)  in real edu.psu.geovista.app.spreadsheet.model after inserting
                     for (int i=0;i<tempCol.size() ;i++){
                         Vector mrow=(Vector)tempCol.elementAt(i);
                         int newX=this.getDataVector().indexOf(mrow);//re-evaluted index
                         Integer newRow=new Integer(newX);
                         sortColumn.setElementAt(newRow,i);
                     }

                     //5. Insert a row to sortColumn. Doesn't matter do it before step 4 or not
                     sortColumn.insertElementAt(new Integer(row),row) ;

                 }


             }
             if (!foundSort){
                    assert false: "SortModel does not contain a not-null column";
                    return;
             }


         }//else
     }
    /**
     *  @param  row  edu.psu.geovista.app.spreadsheet.model's row index
     *              if sort, it will be the index for sort edu.psu.geovista.app.spreadsheet.model.
     *              real edu.psu.geovista.app.spreadsheet.model index is transparent to outside
     */
    public void removeRow(int row) {
        if (!sortModel.isTableSort()){//no sort
            super.removeRow(row);
            return;
         }
             Vector sorts=sortModel.getSorts();
             Vector sortColumn=null;//a column in SortModel that contain sorting info(index) for a column in real edu.psu.geovista.app.spreadsheet.model
             boolean foundSort=false;
            //1. find a valid (not null) column
             for (int k=0;k<sorts.size() ;k++){//!!! May not work for multi-level sort
                 sortColumn=(Vector)sorts.elementAt(k); //column before a row is insert to real edu.psu.geovista.app.spreadsheet.model
                 if (sortColumn!=null){
                         foundSort=true;
                     //2. Get and the reference to real edu.psu.geovista.app.spreadsheet.model rows, store the reference.
                         Vector tempCol=(Vector)sortColumn.clone() ;//new Vector(sortColumn.size() );
                         for (int i=0;i<sortColumn.size() ;i++){
                             int mx=((Integer)sortColumn.elementAt(i)).intValue() ;//row's index in real edu.psu.geovista.app.spreadsheet.model

                             Vector mrow=(Vector)this.getDataVector().elementAt(mx);//the row vector
                             tempCol.setElementAt(mrow,i);
                         }
                         int mx=((Integer)sortColumn.elementAt(row)).intValue() ;//real edu.psu.geovista.app.spreadsheet.model row index
                         super.removeRow(mx);
                         sortColumn.remove(row);
                         tempCol.remove(row);
                     //4.re-evaluate the index of those rows(which obtained in step 2)  in real edu.psu.geovista.app.spreadsheet.model after inserting
                         for (int i=0;i<tempCol.size() ;i++){
                             Vector mrow=(Vector)tempCol.elementAt(i);
                             int newX=this.getDataVector().indexOf(mrow);//re-evaluted index
                             if (newX==-1){//
                                 assert false:"All tempCol's element  should be in real Model ";
                             }
                             else{
                                Integer newRow=new Integer(newX);
                                sortColumn.setElementAt(newRow,i);
                             }
                         } //for
                 } //if

             } //for
             if (!foundSort){
                    assert false: "SortModel does not contain a not-null column";
                    return;
             }


    }
     /**
      * return a cell's edu.psu.geovista.app.spreadsheet.model address.
      * If table is sort, only return the sort edu.psu.geovista.app.spreadsheet.model address
      * real edu.psu.geovista.app.spreadsheet.model address is transparent to outside
      */
     public Point getCellAddress(Cell cell) {
         Vector container=cell.getContainer() ;
         //SSTableModel tmd=SSTableModel.getInstance() ;
         int mrow=getDataVector().indexOf(container ); //real edu.psu.geovista.app.spreadsheet.model x
         int mcol=container.indexOf(cell);//real edu.psu.geovista.app.spreadsheet.model y
         int smrow=mrow;//sort edu.psu.geovista.app.spreadsheet.model's row index
         if (mrow<0||mcol<0){
             //!!! happen when you delete a column which is referenced by other columns
            return null;
         }
         if (this.sortModel.isTableSort() ){
            Vector sorts=this.sortModel.getSorts() ;
            //Vector columnSort=(Vector)sorts.elementAt(mcol);    */
             Vector columnSort=this.sortModel.getColumnSort(mcol);

            if (columnSort==null){ //no individual column sort on the column, use table sort
                columnSort=(Vector)sorts.elementAt(0);
                if (columnSort==null){
                //No entire-table sort, only have individual-column sort on other column
                    smrow=mrow;

                }
                else{//entire-table sort
                    Debug.showVector(columnSort, "columnSort "+mrow) ;
                    smrow=columnSort.indexOf(new Integer(mrow));
                }

            }
            else{ //individual column sort on the column
              Debug.showVector(columnSort, "columnSort "+mcol) ;
               smrow=columnSort.indexOf(new Integer(mrow));

            }
         }

         return new Point(smrow,mcol);

     }

    /********************************************************************
     *                Set data
     ********************************************************************/
    public void setDataVector(Vector dataVector, Vector columnIdentifiers) {



        if (Debug.isDebug() ){
            Debug.showVector(columnIdentifiers, "columnIdentifiers");
        }
        super.setDataVector(dataVector,columnIdentifiers);
        Vector fst=(Vector)dataVector.elementAt(0) ;
        Object o=fst.elementAt(fst.size()-1);
        Debug.println("o:"+o);
        if (o!=null)
        Debug.println("o type:"+o.getClass().getName() );
        //!!! make Table update view and create corresponding tableColumns for Model column
        fireTableChanged(new SSTableModelEvent(this, SSTableModelEvent.RESET_DATA ));
    }

    public void clearSort() {
        this.sortModel.clearSort() ;
    }

    public SSTable getTable() {
        return table;
    }

    public void setTable(SSTable table) {
        this.table = table;
    }
}