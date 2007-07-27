/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 *
 * @Original Author: jin Chen
 * @date: Aug 21, 2003$
 * @version: 1.0
 */
package edu.psu.geovista.data.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class ColumnList extends Vector{
    public ColumnList(int initialCapacity, int capacityIncrement) {
        super(initialCapacity, capacityIncrement);
    }

    public ColumnList(int initialCapacity) {
        super(initialCapacity);
    }

    public ColumnList() {
    }

    public ColumnList(Collection c) {
        super(c);
    }

    public List getColumnNameList(){
        List l=new ArrayList();
        for (Iterator col = this.iterator(); col.hasNext();) {
            Column o = (Column) col.next();
            l.add(o.getId() );

        }
        return l;
    }

    
    /**
     *
     * @param id
     * @return  Any column with id = given id
     */
    public  Column getColumn(String id){
        //int index=0;
        for (Iterator iterator = this.iterator(); iterator.hasNext();) {
            Column column = (Column) iterator.next();
            if(column.getID().equals(id) ){
                return  column;
            }
        }
        return null;
    }
    /**
     * Instead of letting each column maintain its index, calculate it upon request. Thus save the task to
     * override all the add/remove method by adding updateColumnIndex()
     * Application must listen to Model change event and update index of column by calling this method
     * @param id
     * @return
     */
    public  int getColumnIndex(String id){
        int index=0;
        for (Iterator iterator = this.iterator(); iterator.hasNext();index++) {
            edu.psu.geovista.data.model.Column column = (Column) iterator.next();
            if(column.getID().equals(id) ){
                return  index;
            }
        }
        return -1;
    }

}

