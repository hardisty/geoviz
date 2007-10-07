/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class SpatialExtentListener
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: SpatialExtentListener.java,v 1.2 2003/05/05 17:34:40 hardisty Exp $
 $Date: 2003/05/05 17:34:40 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package geovista.common.event;

import java.util.EventListener;


/**
 * This interface enables listening to senders of SpatialExtentEvents.
 *
 * This interface also enables "fireSpatialExtentChanged" methods in classes
 * that generate and broadcast SpatialExtentEvents.
 *
 */
public interface SpatialExtentListener extends EventListener {


  public void spatialExtentChanged(SpatialExtentEvent e);


}