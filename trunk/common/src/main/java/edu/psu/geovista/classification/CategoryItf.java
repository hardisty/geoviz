package edu.psu.geovista.classification;

import java.util.HashSet;
import java.util.Set;

/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Reserved.
 *
 *
 * @author: jin Chen 
 * @date: Jan 3, 2005$
 * @version: 1.0
 */

public interface CategoryItf {
    /**
     *
     * @return a ID. getID().toString() must also unique
     */
    public Object getID();
    public void setID(Object ID);
    public HashSet getMemberIds();
    public void setMemberIds(Set memberIds);
    public int getMemberIdSize();
    public Range getRange();
    public void setRange(Range range) ;
    public String getName() ;
    public void setName(String name);
    public String getVariableName();
    public void setVariableName(String variableName);
    public VisualInfo getVisualInfo();
    public void setVisualInfo(VisualInfo vif);





}
