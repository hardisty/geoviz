/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 *
 * @Original Author: jin Chen
 * @date: Jul 21, 2003$
 * @version: 1.0
 */
package edu.psu.geovista.data.model;



public class PlotModelEvent extends java.util.EventObject {
    /** Identifies the addtion of new rows or columns. */
    public static final int INSERT =  1;
    /** Identifies a change to existing data. */
    public static final int UPDATE =  0;
    /** Identifies the removal of rows or columns. */
    public static final int DELETE = -1;

    /** Identifies the header row. */
    public static final int HEADER_ROW = -1;

    /** Specifies all columns in a row or rows. */
    public static final int ALL_COLUMNS = -1;

//
//  Instance Variables
//

    protected int       type;
    protected int	firstRow;
    protected int	lastRow;
    protected int	column;

    public PlotModelEvent(Object source) {
        this(source, 0, Integer.MAX_VALUE, ALL_COLUMNS, UPDATE);
    }

    public PlotModelEvent(Object source, int row) {
        this(source, row, row, ALL_COLUMNS, UPDATE);
    }

    public PlotModelEvent(Object source, int firstRow, int lastRow) {
       this(source, firstRow, lastRow, ALL_COLUMNS, UPDATE);
    }

    public PlotModelEvent(Object source, int firstRow, int lastRow, int column) {
        this(source, firstRow, lastRow, column, UPDATE);
    }

    public PlotModelEvent(Object source, int firstRow, int lastRow, int column, int type) {
        super(source);
	this.firstRow = firstRow;
	this.lastRow = lastRow;
	this.column = column;
	this.type = type;
    }
    /** Returns the first row that changed.  HEADER_ROW means the meta data,
     * ie. names, types and order of the columns.
     */
    public int getFirstRow() { return firstRow; }

    /** Returns the last row that changed. */
    public int getLastRow() { return lastRow; }

    /**
     *  Returns the column for the event.  If the return
     *  value is ALL_COLUMNS; it means every column in the specified
     *  rows changed.
     */
    public int getColumn() { return column; }

    /**
     *  Returns the type of event - one of: INSERT, UPDATE and DELETE.
     */
    public int getType() { return type; }
}