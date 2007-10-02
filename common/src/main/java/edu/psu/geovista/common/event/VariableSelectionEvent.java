/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class VariableSlectionEvent
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Xiping Dai
 $Author: xpdai $
 $Id: VariableSelectionEvent.java,v 1.1 2004/07/28 15:34:13 xpdai Exp $
 $Date: 2004/07/28 15:34:13 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.common.event;

import java.util.EventObject;



/**
 * An VariableSelectionEvent signals that there is index of a variable available for each observation.
 * This is often because the user has somehow indicated that these observations
 * are of interest
 *
 * The integers represents the indexes of that observation in the overall data set.
 *
 */
public class VariableSelectionEvent extends EventObject {

  private transient int variableIndex;

  /**
  * The constructor is the same as that for EventObject, except that the
  * variableIndex values are indicated.
  */

  public VariableSelectionEvent(Object source, int variableIndex){
    super(source);
    this.variableIndex = variableIndex;
  }

    //begin accessors
    public void setVariableIndex(int variableIndex) {
      this.variableIndex = variableIndex;
    }
    public int getVariableIndex() {
      return this.variableIndex;
    }



    //end accessors

}
