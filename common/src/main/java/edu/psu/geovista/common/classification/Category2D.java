package edu.psu.geovista.common.classification;

/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Reserved.
 * Store information for 2 Dimensional category
 *
 * @author: jin Chen 
 * @date: Jan 3, 2005$
 * @version: 1.0
 */

public interface Category2D extends CategoryItf{
    public Range getXRange();
    public Range getYRange();
    public void setXRange(Range r);
    public void setYRange(Range r);
    //will call getVariableName() to get value
    public String getXVariableName();
    public String  getYVariableName();
    //will call setVariableName() to set the value
    public void setXVariableName(String vn);
    public void  setYVariableName(String vn);

}
