/*------------------------------------------------------------------------------
* GeoVISTA Center, Penn State Geography Deptartment*
* Copyright (c), 2002, GeoVISTA Center
* All Rights Researved.
*
* Original Authors: Bonan Li
* $Author: jmacgill $
*
* $Date: 2004/03/10 20:24:12 $
*
* $Id: layerChangeListener.java,v 1.1 2004/03/10 20:24:12 jmacgill Exp $
*
* To Do:
*
 ------------------------------------------------------------------------------*/

package edu.psu.geovista.app.map;

import java.util.EventListener;

public interface layerChangeListener extends EventListener {
  public void layerChanged(layerChangeEvent e);
}