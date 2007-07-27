package edu.psu.geovista.group;

import java.util.Set;

/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 * Store a set of index(int type)
 * @author: jin Chen 
 * @date: May 22, 2004$
 * @version: 1.0 
 */

public interface IndexSet {
    //id of the indexset
    public String getId() ;
    public void setId(String id) ;

    public void add(int id);
    public void remove(int id);

    public void setIndice(int[] ids);
    public int[] getIndice() ;
    //each element should be a integer type
    public Set getIdAsSet();

    /*******************************************************************************************************
     *    IndexSet values:
     *       - some value calculatd based on the values of individual data whose index contained in the set
     *       - e.g.:
     *              it can be median value of all data whose index contain in the set
     *       - it is the responsibility of the component that generate the group the set the value.
     *       - The value is used on display the IndexSet as a individual data in PCP when in group(or summary mode)
     *
     *******************************************************************************************************/
    /**
     *
     * @return
     */
    public float [] getValues();
    public void setValues(float[] v);


}
