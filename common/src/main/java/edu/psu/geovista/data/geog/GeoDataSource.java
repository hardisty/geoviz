/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class GeoDataSetSource
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: jmacgill $
 $Id: GeoDataSource.java,v 1.1 2004/12/03 19:27:04 jmacgill Exp $
 $Date: 2004/12/03 19:27:04 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.data.geog;


/**
 * Interface implemented by sources that can return a DataSetForApps
 */
public interface GeoDataSource {

 public DataSetForApps getDataForApps();
}