/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class SpatialExtentEvent
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: SpatialExtentEvent.java,v 1.2 2003/05/05 17:34:40 hardisty Exp $
 $Date: 2003/05/05 17:34:40 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.common.event;

import java.awt.geom.Rectangle2D;
import java.util.EventObject;


/**
 * An SpatialExtentEvent signals that a set of observations has been singled out.
 * This is often because the user has somehow indicated that these observations
 * are of interest
 *
 * The integers represents the indexes of that observation in the overall data set.
 *
 */
public class SpatialExtentEvent extends EventObject {

  private transient Rectangle2D spatialExtent;

  /**
  * The constructor is the same as that for EventObject, except that the
  * spatialExtent values are indicated.
  */

  public SpatialExtentEvent(Object source, Rectangle2D spatialExtent){
    super(source);
    this.spatialExtent = spatialExtent;
  }

    //begin accessors
    public void setSpatialExtent (Rectangle2D spatialExtent) {
      this.spatialExtent = spatialExtent;
    }
    public Rectangle2D getSpatialExtent () {
      return this.spatialExtent;
    }
    //end accessors

}