/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 * Description:
 *    This is a wrapper class for all objects in the table.
 *    A cell has a value which is either text or a number.
 *
 * Date: Apr 2, 2003
 * Time: 10:42:12 PM
 * @author Jin Chen
 */

package edu.psu.geovista.app.spreadsheet.formula;


import java.awt.Point;
import java.util.Vector;

import edu.psu.geovista.app.spreadsheet.exception.NoReferenceException;
import edu.psu.geovista.app.spreadsheet.exception.ParserException;
import edu.psu.geovista.app.spreadsheet.table.SSTable;
import edu.psu.geovista.app.spreadsheet.table.SSTableModel;
import edu.psu.geovista.app.spreadsheet.util.Debug;



public class Cell {

    /** Value Type ***/
    public static final int NULL = 0;

    /** The integer code that denotes the cell holds text. */
    public static final int TEXT = 1;

    /** The integer code that denotes the cell holds numbers. */
    public static final int NUMBER = 2;

    /** The integer code that denotes the cell holds a edu.psu.geovista.app.spreadsheet.formula. */
   // public static final int FORMULA = 2;

    public static final int UNKNOWN = 3;

    private int valueType=0;

    /** Value of the cell. In a edu.psu.geovista.app.spreadsheet.formula, value holds the evaluated value. */
    private Object value;

    /**
     * This expression contains the string and internal representation
     * of the edu.psu.geovista.app.spreadsheet.formula if it exists.
     */
    private Formula expression;

    //Jin
    private boolean isformula=false;//
    private boolean deleted=false;//if the cell has been deleted
    private SSTableModel dataModel;
    private Vector container;// the row vector that contain the cell object


    /**
     * This constructor constructs a cell that will not have cells
     * referencing it and are not formulas. Basically only used for creating
     * the row and column labels.
     * <P>
     * <b>Warning:</b> Do not use this with Formulas
     * @param datum the value of the "label" cell (not a edu.psu.geovista.app.spreadsheet.formula.Formula)
     */
    public Cell(Vector _container,SSTableModel tm) {
        this.container =_container;
        this.dataModel=tm;
        expression = null;
    }
   /**
    * protect against loop reference
    */
   public boolean addOwner(Cell cell) {
       Debug.println("this address:"+this.getViewAddress() );
       Debug.println("cell address:"+cell.getViewAddress() );
       return this.getFormula().addOwner(cell);
   }

   public void evaluate(){
     if (this.isFormula() ){

         Object value=null;
         try {
             Formula form=this.getFormula() ;
             if (form.isBad() ){ //invalid edu.psu.geovista.app.spreadsheet.formula
                this.setValue("#NAME?");
             }
             else{
                 //SSTableModel.getInstance().get

                 //Debug.println(" A1:"+SpreadSheetBean4.getTableInstance().getValueAtIndex(1,1));
                value = form.evaluate();
/*Debug.println("edu.psu.geovista.app.spreadsheet.formula.Cell.evaluate():"+value);
                Debug.println("Cell.getViewAddress()"+this.getViewAddress() );
                Thread.dumpStack() ;    */
                this.setValue(value);
             }
         } catch (ParserException e) {

             this.setValue(e.toString() );
             //SpreadSheetBean.getTableInstance().revaluate() ;//remove the loop dialog box
             this.getDataModel().getTable().revaluate() ;
             //e.printStackTrace() ;
         } catch(NoReferenceException e){
             //e.printStackTrace() ;//only for debug
             this.setValue("#REF!");
         }

     } //if
   }

    /**
     * This method returns the edu.psu.geovista.app.spreadsheet.formula associated with the cell or null if it
     * does not exist.
     *
     * @return the edu.psu.geovista.app.spreadsheet.formula (string and internal object) or null if does not
     * exist
     */
    public Formula getFormula() {
        if (this.deleted ) return null;
        else return expression;
    }
     public void setFormula(Formula form) {
        expression = form;
        //this.setAsFormula(true);

    }







    /**
     * If it is a data cell, it returns the data of the cell. If it is a
     * edu.psu.geovista.app.spreadsheet.formula, it returns the previously evaluated value of the edu.psu.geovista.app.spreadsheet.formula.
     *
     * @return the value (data or evaluated) of the cell
     */
    public Object getValue() {
        //Debug.println("Cell.getValue:"+value+" at"+this.getViewAddress() );
        /*if (this.isFormula() ){
            this.evaluate() ;
        }  */

        if (this.isDeleted() ) {
            return "#REF!";
        }
        else return value;
    }


    /**
     * Sets the value field of the cell.
     *
     * @param datum the object to set the value of cell to
     */
    public void setValue(Object datum) {
        /*Point p=this.getViewAddress() ;
        if (p.getX() ==1&&p.getY() ==0){
            Debug.println("view(1,0)="+this.getValue() );
        }      */

        if (datum==null){
             this.setValueType(Cell.NULL );
        }
        else if(datum!=null){
            if (datum instanceof String ){
                String s=(String)datum;
                this.setValueType(Cell.TEXT);
                if (s.trim().equals("")){
                    datum=null;
                    this.setValueType(Cell.NULL );
                }
            }
            else if (datum instanceof Number){
                this.setValueType(Cell.NUMBER );
            }
            else{
                this.setValueType(Cell.UNKNOWN );
            }
        }

        value = datum;
        //this.setAsFormula(false);

    }



    /**
     * This method sets the cell to be a edu.psu.geovista.app.spreadsheet.formula cell. It puts the edu.psu.geovista.app.spreadsheet.formula
     * object into the expression field. The Table of Cells is responsible
     * for recalculating and setting the appropriate value in the value
     * field of this cell.
     *
     * @param form the internal representation of edu.psu.geovista.app.spreadsheet.formula to set this cell to
     */
    public void setAsFormula(boolean isf) {

        this.isformula =isf;
    }



    /**
     * This method is useful for determining what information a cell holds. To
     * check if a cell holds a certain type just see if
     * getType() == edu.psu.geovista.app.spreadsheet.formula.Cell.CODE where CODE is any of the cell constants.
     *
     * @return the integer code of the type of data this cell holds
     */
    public int getValueType() {
        if (value==null) valueType= Cell.NULL ;
        ///if (expression != null) return edu.psu.geovista.app.spreadsheet.formula.Cell.FORMULA; //jin: no use
        else if (value instanceof Number) valueType= Cell.NUMBER;
        else if(value instanceof String)valueType= Cell.TEXT;
        else valueType= Cell.UNKNOWN ;

        return valueType;
    }

    public void setValueType(int t){
        this.valueType =t;
    }

    /**
     * This method returns true IFF it is a edu.psu.geovista.app.spreadsheet.formula cell
     *
     * @return true iff a edu.psu.geovista.app.spreadsheet.formula cell
     */
    public boolean isFormula() {
        return this.isformula ;
    }

    /**
     *  If the column or row contain the cell is deleted, the cell is set "deleted"
     *
     */
    public boolean isDeleted(){
        return this.deleted ;
    }

    public void setDeleted(boolean d){
        this.deleted =d;
    }

    public SSTableModel getDataModel() {
        return dataModel;
    }

    /**
     * This method determines a cell is a edu.psu.geovista.app.spreadsheet.formula cell that has a error.
     *
     * @return true if cell is an error cell

    public boolean isErrorCell() {
        return ((isFormula()) && (value instanceof ParserException));
    }  */



    public Vector getContainer() {
        return container;
    }
    /**
     * toString() method return same data as getValue(), but in different format
     * toString() return String format for table.getValue() to paintCell()
     * getValue() return  Number for evaluate edu.psu.geovista.app.spreadsheet.formula
     * called when paint cell
     */
    public String toString() {
          /*if (this.isFormula() )
                return "="+expression.toString();
        else*/
/* Because all values stored in TableModel is in form of edu.psu.geovista.app.spreadsheet.formula.Cell object,
         * TableModel.getValueAt() will call object.toString() to get actual value
         *, so toString() only return the evaluted value, not edu.psu.geovista.app.spreadsheet.formula expression*/
        if (this.isDeleted() ){
            return "#REF!";
        }
        if (this.isFormula()&&!this.getFormula().isBad() ){
            this.evaluate() ;
        }
        if(value !=null)
            return value.toString();
        else
            return null; //means empty value""
   }
   /**
    *  not finish yet, just for treeset
    *  String: if can't convert to number, alway on the top ."" is set to null in cell
    *  null<"": alway at the bottom
    *  number: always in middle
    *  @return a negative integer, zero, or a positive integer as
    *          this object is less than, equal to, or greater than
    *          the specified object.
    */
   public int compareTo( Cell x, boolean ascend){
        final int over=2000000; //doesnot matter if ascend or descend, this cell always over the given cell
        final int under=-2000000; //doesnot matter if ascend or descend, this cell always under the given cell
        //Note:??? Use big number cause compareToIgnoreCase() will return some small number(like 2,11,20...), which may cause bug

        int r;//return
         //if this is blank which is lowest value
        assert (x!=null);
        int xt=x.getValueType();//x's value type
        int tt=this.getValueType() ;//this value type

        Object tvalue=this.value;
        Object xvalue=x.getValue() ;
        if (tt==Cell.NULL ){
            //r= under;
            return 1;
        }
        if (xt==Cell.NULL ){
            //r= over;
            return -1;
        }

        if (tt==Cell.TEXT ){
            String tvs=(String)tvalue;
            if (xt==Cell.TEXT ){
                String xvs=(String)xvalue;
                r= tvs.compareToIgnoreCase(xvs);
                if (tvs!=null&&xvs!=null)
                Debug.println( tvs +" compareToIgnoreCase "+xvs+ " = "+r);
            }
            else if(xt==Cell.NUMBER ){

                try {
                    Number xvn=(Number)xvalue;
                    double xvd=xvn.doubleValue() ;
                    double tvd=Double.parseDouble(tvs); //??throw NumberFormatException
                    if(tvd>xvd) r= 1;
                    else if (tvd<xvd)r= -1;
                    else r= 0;
                } catch (NumberFormatException e) {//String can't convert to number
                    r= over;
                }
            }
            else if (xt==Cell.UNKNOWN ){
                Comparable tvc=(Comparable)tvs;
                Comparable xvc=(Comparable)xvalue;
                r= tvc.compareTo(xvc);  //??? throw ClassCastException

            }
            else{ //xt=edu.psu.geovista.app.spreadsheet.formula.Cell.Null, should never reach, just for compile purpose
                assert false;
                r= over;
            }

        }
        else if(tt==Cell.NUMBER){
            Number tvn=(Number)tvalue;
            if (xt==Cell.TEXT ){
                try {
                    String xvs=(String)xvalue;
                    double xvd=Double.parseDouble(xvs); //??throw NumberFormatException
                    double tvd=tvn.doubleValue() ;
                    if(tvd>xvd) r= 1;
                    else if (tvd<xvd)r= -1;
                    else r= 0;
                } catch (NumberFormatException e) {
                    r=under;
                }
            }
            else if(xt==Cell.NUMBER ){

                Number xvn=(Number)xvalue;
                double tvd=tvn.doubleValue() ;
                double xvd=xvn.doubleValue() ;
                if(tvd>xvd) r= 1;
                else if (tvd<xvd)r= -1;
                else r= 0;
            }
            else if (xt==Cell.UNKNOWN ){
                r= 0  ;

                //throw new ClassCastException();
            }
            else{ //xt =edu.psu.geovista.app.spreadsheet.formula.Cell.Null, should never reach, just for compile purpose
                assert false;
                r= over;
            }

        }
        else {    //unknow type
                Debug.println("this cell value type:"+tt);
                Comparable tvc=(Comparable)tvalue;
                Comparable xvc=(Comparable)xvalue;
                r= tvc.compareTo(xvc);  //??? throw ClassCastException

        }

        if (r==over){
            return -1;
        }
        else if (r==under){
            return 1;
        }
        else{
            if (ascend){
                return r;
            }
            else{
                return -r;
            }
        }


   }

    /********************************************************************
     *                  Address                                         *
     ********************************************************************/

        /**
         *  return the address of the cell in Table in form of (x,y)
         *  Need translate. e.g. Row1 in Table is Row0 in TableModel
         */
        public Point getViewAddress(){
            //Point address=this.getModelAddress() ;
            //SSTableModel tm=SSTableModel.getInstance() ;
            Point address=this.getDataModel().getCellAddress(this) ;  //address in TableModel
            if (address==null) return null;
            int x=(int)address.getX() ; //edu.psu.geovista.app.spreadsheet.model index (sort edu.psu.geovista.app.spreadsheet.model if sort)
            int y=(int)address.getY();  //edu.psu.geovista.app.spreadsheet.model index (sort edu.psu.geovista.app.spreadsheet.model if sort)




            //For now Model row is same as table row. May not the case if implement Sort on row
            int row=SSTable.transRowTableToView(x);
            //int col=Cell.transColModelToView(y);
            int col=this.getDataModel().getTable().transColModelToView(y);
            //String col=edu.psu.geovista.app.spreadsheet.formula.Node.translateColumn(y );
            return new Point(row,col);


        }
       public String getViewAddressText(){
            String addrs=null;
            Point address=getViewAddress() ;  //address in Table
            if (address!=null){
                    int x=(int)address.getX() ;
                    int y=(int)address.getY();
                //String row=edu.psu.geovista.app.spreadsheet.formula.Node.translateRow(x );
                    String row=Integer.toString(x);
                    String col=SSTable.translateVVColumn(y);//translate to String. e.g. 1=>A
                    addrs=col+row;

            }
            return addrs;
       }
        /**
      *  Given a cell's address in View(table) return relative address in String format
      */
     public  static String getRelCellAddress(Point p){
         StringBuffer address = new StringBuffer();
         String addr = null;
         int x = (int) p.getX();
         int y = (int) p.getY();
         addr = SSTable.translateVVColumn(y);
         address.append(addr);
         addr = String.valueOf(x);
         address.append(addr);//e.g.: A1
         String a=address.toString();
         Debug.println("Cell address :" + a);
         return a;

     }
      /**
      *  Given a cell's address in View(table) return absolute address in String format
      */
     public  static String getAbsCellAddress(Point p){
         StringBuffer address = new StringBuffer();
         String addr = null;
         int x = (int) p.getX();
         int y = (int) p.getY();
         addr = SSTable.translateVVColumn(y);
         address.append("$");
         address.append(addr);
         addr = String.valueOf(x);
         address.append("$");
         address.append(addr);//e.g.: A1
         String a=address.toString();
         Debug.println("Cell address :" + a);
         return a;

     }

}

