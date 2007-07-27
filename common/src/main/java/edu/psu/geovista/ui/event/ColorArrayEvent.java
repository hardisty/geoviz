/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ColorArrayEvent
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: jmacgill $
 $Id: ColorArrayEvent.java,v 1.3 2004/12/03 19:27:10 jmacgill Exp $
 $Date: 2004/12/03 19:27:10 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.ui.event;

import java.awt.Color;
import java.util.EventObject;



/**
 * An ColorArrayEvent signals that there are colors available for each observation.
 * This is often because the user has somehow indicated that these observations
 * are of interest
 *
 * The integers represents the indexes of that observation in the overall data set.
 *
 */
public class ColorArrayEvent extends EventObject {

  private transient Color[] colors;

  /**
  * The constructor is the same as that for EventObject, except that the
  * colors values are indicated.
  */

  public ColorArrayEvent(Object source, Color[] colors){
    super(source);
    this.colors = colors;
  }

    //begin accessors
    public void setColors(Color[] colors) {
      this.colors = colors;
    }
    public Color[] getColors() {
      return this.colors;
    }



    //end accessors

}