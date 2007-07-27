package edu.psu.geovista.group;

import java.util.Set;

/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 * Its element can be  either a MapGroup or a IndexSet. For now, only support IndexSet.
 * @author: jin Chen 
 * @date: May 22, 2004$
 * @version: 1.0
 */

public interface MapGroup  {
    public void add(IndexSet idset);
    public IndexSet getIndexSet(Object id); 
    /**
     *
     * @param groupindexs  index of each IndexSet in the group
     * @return   a set of index of all individual records contained in group
     *           e.g.: if indexSet represent a state data and it contain individual records represent conuty data
     *                 and if  <groupindexs> contain indexs for NC and PA, then the method return index of all counties in the 2 states
     */
    public Set getIndice(int[] groupindexs);


    public  Set getAllIndice();

}
