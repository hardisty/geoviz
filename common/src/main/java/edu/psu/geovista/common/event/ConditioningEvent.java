/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ConditioningEvent
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: ConditioningEvent.java,v 1.3 2003/07/18 14:05:43 hardisty Exp $
 $Date: 2003/07/18 14:05:43 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.common.event;

import java.util.EventObject;


/**
 * An ConditioningEvent signals that a set of observations has been singled out.
 * This is often because the user has somehow indicated that these observations
 * are of interest
 *
 * The integers signal if the given observation is in range or not.
 *
 * 0 = in current conditioning
 * -1 = not in current conditioning
 *
 */
public class ConditioningEvent extends EventObject {

  private transient int[] conditioning;

  /**
  * The constructor is the same as that for EventObject, except that the
  * conditioning values are indicated.
  */

  public ConditioningEvent(Object source, int[] conditioning){
    super(source);
    this.conditioning = conditioning;
  }

    //begin accessors
    public void setConditioning (int[] conditioning) {
      this.conditioning = conditioning;
    }
    public int[] getConditioning () {
      return this.conditioning;
    }
    //end accessors

}