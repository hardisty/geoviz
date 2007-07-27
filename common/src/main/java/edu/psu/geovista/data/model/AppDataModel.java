/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c) 2002, GeoVISTA Center
 * All Rights Reserved.
 *
 * @original author: jin Chen 
 * @author: jin Chen 
 * @date: Feb 15, 2007$
 * @version: 1.0
 */
package edu.psu.geovista.data.model;

import java.awt.Shape;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.event.TableModelListener;

public interface AppDataModel  {
    public static final int DTYPE_NONE = -1; //data type
    public static final int DTYPE_STRING = 0;
    public static final int DTYPE_DOUBLE = 1;
    public static final int DTYPE_INTEGER = 2;
    public static final int DTYPE_BOOLEAN = 3;
    public static final int DTYPE_FLOAT = 4;

    public static final int SPATIAL_TYPE_NONE = -1;
    public static final int SPATIAL_TYPE_POINT = 0;
    public static final int SPATIAL_TYPE_LINE = 1;
    public static final int SPATIAL_TYPE_POLYGON = 2;
    public static final int SPATIAL_TYPE_RASTER = 3;

    public static final int NULL_INT = Integer.MIN_VALUE;
    
    void addTableModelListener(TableModelListener l); 
    void removeTableModelListener(TableModelListener l);
    
    void feedData(Object[] rawData);

    Object[] getGeoVistaRawData() throws Exception;

    Object[] getRaw();
    
    public void setRaw(Object[] raw);

    void addRow(float values[], Object id);

    void addRow(float values[]);

    float[] getRowValueAsFloat(int recordnum);

    double[] getRowValueAsDouble(int recordnum);

    String getRowName(int num);

    int[] getAllRowIndexs();

    String[] getRecordAsString(int row);

    int getRowCount();
    public int getColumnCount();
    public Object getId(int index);
    void setIds(Vector<Object> ids);
    Vector<Object> getIds();
    /*void setIds(Vector<Object> ids);
    Vector<Object> getIds();
    Object getId(int index);*/
    public void setId2Index(Map id2Index);
    public int getIndexById(Object id);
    public boolean setIdColumn ( String columnName);
    //void setRowNames(String[] rowNameCol, Vector rowNames);
    // set the columns that make the row name. e.g.: the elements of 2 columns(State_name, county_name) can be used to make a row name
    public void setRowNameColumns(String[] rowNameColumns);
    public void setRowNames(Vector rowNames);
    Vector getRowNames();
    public String[] getRownames();
    public String[] getRowNames(String[] columns,String delimit);
    String[] getRowNameColumns();

    void clearNumericValues();

    void addColumn(String columnName, float[] columnData);

    void addColumn(String columnName, double[] columnData);

    void addColumn(String columnName, java.util.List columnData);

    void removeColumn(String name);

    void removeColumn(int col);

    float getValue(int row, int column);

    double[] getColumnValueAsDouble(int column);

    float[] getColumnValueAsFloat(int column);

    float getColumnMaxValue(int column);

    float getColumnMinValue(int column);

    String getColumnName(int column);

    int getColumnIndexByName(String colName);

    List getColumnNameList();

    String[] getColumnNames();

    String [] getAllColumnNames();

    void setColumnNames(String colnames[]);

    ColumnList getColumns();

    void setColumns(ColumnList columns);

    void addDataModel(AppDataModel dataModel);

    float[] convertTofloat(double[] da);

    void addNonNumericRow(Object[] record);

    void addNonNumericData(String name, Object[] nonNumeric);

    void removeNonNumericColumn(int index);

    public ColumnList getNonNumericColumns();

    String [] getNonNumericColumnNames();

    void setNonNumericColumns(ColumnList ncolumns);

    void setNonNumericColumns(String[] ncolumns);

    Object[] getNonNumericRecord(int row);

    Object[] getNonNumericColumnData(String colName);

    Object[] getNonNumericColumnData(int column);

    int getNonNumericColumnIndexByName(String colName);
    public Vector getNonNumericValues();


    Shape[] getShpData();

    void setShpData(Shape[] shpData);

    MetaData getMeta();

    void setMeta(MetaData meta);

    void simplePrint();

    String getStringForNaN();
    public void setStringForNaN(String stringForNaN);


}

