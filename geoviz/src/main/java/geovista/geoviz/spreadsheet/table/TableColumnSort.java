package geovista.geoviz.spreadsheet.table;

/*
 * Description: A toolkit for sorting table based on columns
 * Date: Mar 20, 2003
 * Time: 3:38:43 PM
 * @author Jin Chen
 */

import java.awt.Point;
import java.util.Vector;

import geovista.geoviz.spreadsheet.formula.Cell;
import geovista.geoviz.spreadsheet.util.Debug;

public class TableColumnSort {
    //private JTable table;
    private boolean tableSort;
    private SSTableModel tm;
    /**
     * Contain vectors, each of which is for sorting a individual column
     * nth vector map to nth Column in Model
     * Except vector(0), which is for sorting whole table
     */
    private Vector sorts;

    private SortRange sortRange; //The range to be sorted
    public TableColumnSort(SSTableModel tm) {
        //this.table = table;
        this.tm =tm;
        this.createSort() ;

    }
    private void createSort() {
        sorts=new Vector();
        this.sortRange =new SortRange();
        int numOfColumn=this.tm.getColumnCount() ;
        for (int i=0;i<numOfColumn;i++){ //??? Need add more if adding column
            sorts.add(null);
        }
        Debug.println("sorts' size:"+sorts.size() );

    }


    public SortRange getSortRange() {
        return sortRange;
    }

    /**
     * Do different sorting for  table selection and a given column:
     *  - if select a column: do the individual column sort
     *  - if select a entire table: do entire table sort based on a selected column
     *  - if select serveral rows(contigent or not: do sort only for those rows based on a selected column
     *  - if select a range: do sort only for the range based on a selected column
     *  sort whole table based on a column
     *  @param col the index of the column based on which the table is sorted
     */

    /** !!!
     *  create a vector in sorts(Vector).
     *  It is a map in between Model index and View index
     *  @param  column in View
     */
    private void createSortMap (int col){
        this.setTableSort(true);
        int rowCount = tm.getRowCount();
        if (col==0){//creat sort map for entire table
             //tablesort, store map between sort index to row index for all columns
            Vector ts=this.createSortVector(rowCount) ;
            System.out.println("ts"+ts.elementAt(2));
            if (sorts.size() ==0) {
                sorts.add(ts);
            } else {
                sorts.setElementAt(ts, 0);//default vector for sorting whole table
            }
        }
        else {
            /*
            Vector colsort;
            int size=sorts.size();
            colsort=this.getColumnSort(col);
            if (colsort==null){
                colsort=this.createSortVector(rowCount) ;
            }
            sorts.setElementAt(colsort,col);    */

            //*  replaced by above on 04032003
            Vector colsort=this.createSortVector(rowCount) ;
            int size=sorts.size();

            //int mcol=geovista.geoviz.spreadsheet.formula.Cell.transColViewToModel(col);//Model index
            int mcol=col;
            if (mcol<size){//Vector.setElementAt(colsort,mcol); won't throw geovista.geoviz.spreadsheet.exception
                sorts.setElementAt(colsort,mcol);
            }
            else{//Vector.setElementAt(colsort,mcol); will throw geovista.geoviz.spreadsheet.exception, MUST fill with null element
                int needCol=mcol-size+1;
                for (int i=0;i<needCol;i++){
                    sorts.add(null);//For those columns no need sort, just maintain a null for them
                }
                sorts.setElementAt(colsort,mcol);
            }
        }//else
    }
    /**
     *  Return the sort vector for the column with given index(in model)
     *  if not vector exist for the index, set null for that index in sorts
     *  it means that column is not sorted(thus no corresponding vector in sorts)
     */
    public Vector getColumnSort(int col){
         int size=sorts.size();
         if (col>=size){
         //Vector.setElementAt(colsort,mcol); will throw geovista.geoviz.spreadsheet.exception,
          //MUST fill with null element
                int needCol=col-size+1;
                for (int i=0;i<needCol;i++){
                    sorts.add(null);//For those columns no need sort, just maintain a null for them
                }

         }
         return (Vector)sorts.elementAt(col);
    }

    public void sortRows(int col, int[] rows, boolean ascending){
        this.sortRange.setType(SortRange.ROWS );
        for (int i=0;i<rows.length ;i++){
                int row=rows[i];
                this.sortRange.setRow(row);
        }
        this.createSortMap(0);//use table sort
        this.sortRows(col,false);
    }
     //sort entire table
    public void sortTable(int col, boolean ascending){
         this.sortRange.setType(SortRange.ENTIRE_TABLE );
         this.sortRows(col,ascending);
    }
    //sort rows
    private void sortRows(int col, boolean ascending){

      System.out.println("********* geovista.geoviz.spreadsheet.tools.Sort ->");
      this.createSortMap(0);

      int rowCount = tm.getRowCount();
      int sortType=this.sortRange.getType() ;
      if(sortType==SortRange.ENTIRE_TABLE
         || sortType==SortRange.ROWS ){
         Vector sortMap=(Vector)this.sorts.get(0);
          TableColumnSort.showVector(sortMap);
          for(int i=0; i < rowCount; i++) {
             //ith is the current element
             for(int j = i+1; j < rowCount; j++) {
                //(i+1)th is the next elements
                int indexi=((Integer)(sortMap.elementAt(i))).intValue() ;
                int indexj=((Integer)(sortMap.elementAt(j))).intValue() ;
                if(compare(indexi, indexj, col, ascending) < 0) {
                   /*if (this.sortRange.containRow(i)&&
                       this.sortRange.containRow(j)){   */
                       swap(i,j,sortMap);

                       System.out.println("Swap "+i+" and "+j);
                  // } //if2
                }//if 1

             }  //for2
          }//for
          TableColumnSort.showVector(sortMap);

      }
      else{
          assert false:"sortRows() can sort it";
      }
    }
    /**  sort column
     *   @param  col based on which sorting is done
     *   @param  cols  sorting apply only to those columns
     *
     */
    public void sortColumns(int col, int[] cols,boolean ascending){
      System.out.println("********* geovista.geoviz.spreadsheet.tools.Sort ->");
      for (int i=0;i<cols.length ;i++){
                int scol=cols[i];//selected column in table view
                this.sortRange.setColumn(scol);
                this.createSortMap(scol);
      }
      this.sortRange.setType(SortRange.COLUMNS);

      int rowCount = tm.getRowCount();
      int sortType=this.sortRange.getType() ;
      if (sortType==SortRange.COLUMNS ){
         for (int k=0;k<cols.length ;k++){//do sort for each column
             //int mcol=geovista.geoviz.spreadsheet.formula.Cell.transColViewToModel(cols[k]);
            int mcol=cols[k];
            Vector sortMap=(Vector)this.sorts.get(mcol);
                  for(int i=0; i < rowCount; i++) {
                     //ith is the current element
                     for(int j = i+1; j < rowCount; j++) {
                        //(i+1)th is the next elements
                        //if (SortRange.
                        int indexi=((Integer)(sortMap.elementAt(i))).intValue() ;
                        int indexj=((Integer)(sortMap.elementAt(j))).intValue() ;
                        if(compare(indexi, indexj, col, ascending) < 0) {

                               swap(i,j,sortMap);

                               System.out.println("Swap "+i+" and "+j);

                        }//if 1
                     }  //for2
                  }//for1
             Debug.showVector(sortMap, "Show SortMap "+mcol) ;
         }//for0

      }
      else if (sortType==SortRange.RANGE ){
      }
      else{
          assert false:"sortColumns() can sort it";
      }

      System.out.println("******************************************* geovista.geoviz.spreadsheet.tools.Sort <-");
    }//sortTable

   /**   sort Range
     *   @param  col based on which sorting is done
     *   @param  cols  sorting apply only to those columns
     *
     */
    public void sortRange(int col,int[] rows,int[] cols,boolean ascending){
      System.out.println("********* geovista.geoviz.spreadsheet.tools.Sort ->");
      for (int i=0;i<cols.length ;i++){
                int scol=cols[i];//selected column in table view
                if (!sortRange.contains(scol)){
                           this.sortRange.setColumn(scol);
                           this.createSortMap(scol);
                }
      }
      this.sortRange.setType(SortRange.RANGE );

      int rowCount = tm.getRowCount();
      int sortType=this.sortRange.getType() ;
      if (sortType==SortRange.RANGE ){
         for (int k=0;k<cols.length ;k++){//do sort for each column
             //int mcol=geovista.geoviz.spreadsheet.formula.Cell.transColViewToModel(cols[k]);
            int mcol=cols[k];

            Vector sortMap=(Vector)this.sorts.get(mcol);
                  for(int i=0; i < rowCount; i++) {
                     //ith is the current element
                     for(int j = i+1; j < rowCount; j++) {
                        //(i+1)th is the next elements
                        //if (SortRange.
                        int indexi=((Integer)(sortMap.elementAt(i))).intValue() ;
                        int indexj=((Integer)(sortMap.elementAt(j))).intValue() ;
                        if (rows[0]<=indexi&& indexi<=rows[rows.length -1]&&
                            rows[0]<=indexj&& indexj<=rows[rows.length -1]){
                                if(compare(indexi, indexj, col, ascending) < 0) {
                                       swap(i,j,sortMap);
                                       System.out.println("Swap "+i+" and "+j);
                                }//if 1
                        }
                     }  //for2
                  }//for1
             Debug.showVector(sortMap, "Show SortMap "+mcol) ;
         }//for0

      }
      else if (sortType==SortRange.RANGE ){
      }
      else{
          assert false:"sortColumns() can sort it";
      }

      System.out.println("******************************************* geovista.geoviz.spreadsheet.tools.Sort <-");
    }//sortTable

    /**
     *  create a Vector contain same number of element as the row count
     */
    private Vector createSortVector(int numOfRow){


        Vector ts=new Vector(numOfRow);
         for (int i=0;i<numOfRow;i++){
            Integer index=new Integer(i);
            ts.add(index);
         }
         return ts;

    }
    /**
     *  swap ith and jth element in column
     */
    private void swap(int i, int j, Vector ts) {
      //Vector ts=(Vector)this.sorts.elementAt(0);//tablesort

      int indexi=((Integer)(ts.elementAt(i))).intValue() ;
      int indexj=((Integer)(ts.elementAt(j))).intValue() ;

      ts.setElementAt(new Integer(indexj), i);
      ts.setElementAt(new Integer(indexi),j);
      /*int tmp = indexes[i];
      indexes[i] = indexes[j];
      indexes[j] = tmp; */
    }
    /**
     * compare ith element and jth element of tableModel's column
     * @param   column  column index in table geovista.geoviz.spreadsheet.model
     */
    private int compare(int i, int j, int column, boolean ascending) {
      //int modelCol=table.convertColumnIndexToModel(column);
      int modelCol=column;
      SSTableModel realModel =tm;
      Cell ic =(Cell) realModel.getRealModelValueAt(i,modelCol);
      Cell jc =(Cell) realModel.getRealModelValueAt(j,modelCol);

      int c = jc.compareTo(ic,ascending);//ascending
      if (ic!=null&&jc!=null){
        if(ic.getValue() !=null&&jc.getValue()!=null )
            System.out.println("Compare "+j+"th("+jc.getValue() +") to"+i+"th("+ic.getValue() +") ="+c);
            //Descend if A>B => c<0; A<B => c>0
            //ascend if  A>B => c>0; A<B => c<0
      }

      return c;
      //return (c < 0) ? -1 : ((c > 0) ? 1 : 0);
   }
    /**
     *  Given a cell point in sortModel, return it position in real Model
     *  @param  vx  sortModel 's x . For table view, it is just Model x
     */
    public Point getCellPointAt(int vx, int vy){
        int mx,my;//geovista.geoviz.spreadsheet.model x, geovista.geoviz.spreadsheet.model y
        my=vy;
        int sortSize=this.sorts.size() ;
        Vector columnIndex;
        if(sortSize==0 ){//The table is not sort
            mx=vx;
            return  new Point(mx,my);

        }
        else if ( this.sorts.elementAt(0)!=null){// entire table is sort  also may have individual column sorts
             if (my>=sorts.size() ){//the column is not individually sort
                  columnIndex=(Vector)this.sorts.elementAt(0); // table sort index
                  mx=((Integer)columnIndex.elementAt(vx)).intValue(); //only apply table sort
             }
             else{// the column may be individually sort
                  columnIndex=(Vector)this.sorts.elementAt(my);
                  if  (columnIndex==null){ //the column is not individually sort
                        columnIndex=(Vector)this.sorts.elementAt(0); //use table sort index
                        mx=((Integer)columnIndex.elementAt(vx)).intValue();

                  }
                  else {//The individual column is sort
                       Vector tableSort=(Vector)this.sorts.elementAt(0); //table sort index
                       mx=((Integer)tableSort.elementAt(vx)).intValue();  //apply table sort
                  }
             }



        }
        else{ //no entire-table sort, may have individual column sort
            if (my<sorts.size() &&(columnIndex=(Vector)this.sorts.elementAt(my))!=null ){
                //only individual sort apply
                mx=((Integer)columnIndex.elementAt(vx)).intValue();
            }
            else{//the column is not individually sort
                  //Not any sort at all
                  mx=vx;
                  //return  new Point(mx,my);
            }

        }
        return  new Point(mx,my);
        /*
        else if (my>this.sorts.size()){  //The column is not selected for sorting
        }
        Vector columnIndex=(Vector)this.sorts.elementAt(my);
        if (columnIndex!=null){//The individual column is indexed
            mx=((Integer)columnIndex.elementAt(vx)).intValue() ;
        }
        else{
            columnIndex=(Vector)this.sorts.elementAt(0); //use table sort index
            if (columnIndex!=null){
                mx=((Integer)columnIndex.elementAt(vx)).intValue() ;
            }
            else{ // no table sort index
                mx=vx;
            }
        }
        return new Point(mx,my);    */

    }






    /**
     *  Select a column
     */


    public static void showVector(Vector v) {
        System.out.println("Show Vector -->");
        for (int i=0;i<v.size() ;i++){
            System.out.println(v.elementAt(i).toString() );
        }
        System.out.println("Show Vector <--");
    }

    public Vector getSorts() {
        return sorts;
    }


    /**
     *  return true if sort apply to any range of the table
     */
    public boolean isTableSort(){
         return tableSort;
    }

    public void setTableSort(boolean tableSort) {
        this.tableSort = tableSort;
    }
    /**
     *  clear all sort of the table
     */
    public void clearSort() {
        this.setTableSort(false);
        this.createSort() ;
    }
}

