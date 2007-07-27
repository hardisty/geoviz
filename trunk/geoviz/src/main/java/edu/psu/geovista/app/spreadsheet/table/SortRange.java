package edu.psu.geovista.app.spreadsheet.table;

/*
 * The range to be sorted:
 *  - entire table
 *  - a column/serveral column
 *  - a row/serveral rows
 *  - a range
 * User: jchen
 * Date: Mar 23, 2003
 * Time: 10:50:29 AM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */

import java.util.HashSet;


public class SortRange {
    public static final int ENTIRE_TABLE=0;
    public static final int COLUMNS=1;
    public static final int ROWS=2;
    public static final int RANGE=3;

    private int type=3;      //range type
    private HashSet rows;//the rows' index (in Model)that are in the edu.psu.geovista.app.spreadsheet.tools.Sort Range
    private HashSet columns;//the columns' index that are in the edu.psu.geovista.app.spreadsheet.tools.Sort Range

    public SortRange() {
       rows=new HashSet();
       columns=new HashSet();
    }
    //private boolean entireTable;//true if sortrange is entire table

    /** See if cell(x,y) in sortRange
     *  assume x, y is within the table
     */
    public boolean containCell(int x, int y){
        if (this.getType() ==SortRange.ENTIRE_TABLE ){
            return true;
        }
        else{
            Integer row=new Integer(x);
            Integer col=new Integer(y);
            if (this.getRows().contains(row)&&
                this.getColumns().contains(col)){
                return true;
            }
            else{
                return false;
            }
        }
    }
    /** See if rowX in sortRange
     *  assume x is within the table
     */
    public boolean containRow(int x){
        if (this.getType() ==SortRange.ENTIRE_TABLE ){
            return true;
        }
        else{
            Integer row=new Integer(x);
            if (this.getRows().contains(row)){
                return true;
            }
            else{
                return false;
            }
        }
    }
    /** See if cell(x,y) in sortRange
     *  assume y is within the table
     */
    public boolean containColumn(int y){
        if (this.getType() ==SortRange.ENTIRE_TABLE ){
            return true;
        }
        else{

            Integer col=new Integer(y);
            if ( this.getColumns().contains(col)){
                return true;
            }
            else{
                return false;
            }
        }
    }



    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public HashSet getColumns() {
        return columns;
    }
    /**
     *
     */
    public void setColumn(int y) {
        Integer col=new Integer(y);
        this.columns.add(col);

    }
    /**
     * true if the SortRange contains a column index
     */
    public boolean contains(int y){
        Integer col=new Integer(y);
        return this.columns.contains(col);
    }

    public HashSet getRows() {
        return rows;
    }

    public void setRow(int x) {
        Integer row=new Integer(x);
        this.rows.add(row);
    }
}

