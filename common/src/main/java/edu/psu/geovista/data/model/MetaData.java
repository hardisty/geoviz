package edu.psu.geovista.data.model;

import java.util.Hashtable;
import java.util.Set;

/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Reserved.
 *
 *
 * @Original Author: jin Chen
 * @date: May 23, 2005$
 * @version: 1.0
 */

public interface MetaData {
    /**
     *
     * @return description of each column
     */
    public Hashtable getColumndescription();
    public String getColumnDescription(String colID);

   /**
     *  It is only for MetaData in table format, hence need to remove from this generic interface
     *              *
     * @param keyColumnName      the name of column in this meta data, whose elements will serve as key in the mapping
     * @param valueColumnName    the name of column in this meta data, whose elements will serve as value in the mapping
     * @return
     */
   // public Hashtable getMappingForColumn(String keyCol,String valueCol);
    public void add(MetaData meta);

    /**
     * @param value
     */
    public void setColumnValue(String colID, float value) ;
    public Hashtable getColumnValues();
    //public List getTimeValues(String[] columnNames);

    //public Range getColumnValuesExtreme();
     Object clone() ;

    /**
     * @return  meta data model
     */
    public  Set<BasicMetatdataModel> getModel();
    public void setModel(BasicMetatdataModel datamodel);

}
