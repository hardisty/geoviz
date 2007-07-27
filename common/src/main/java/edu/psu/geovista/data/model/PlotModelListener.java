package edu.psu.geovista.data.model;

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

public interface PlotModelListener extends java.util.EventListener{
    public void plotChanged(edu.psu.geovista.data.model.PlotModelEvent e);
}
