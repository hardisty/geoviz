/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ClassificationEvent
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: ClassificationEvent.java,v 1.2 2003/05/05 17:34:39 hardisty Exp $
 $Date: 2003/05/05 17:34:39 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.ui.event;

import java.util.EventObject;


/**
 * An ClassificationEvent signals that the data has been classified into n
 * classes.
 *
 * The integers represents the class of each observation in the overall data
 * set.
 *
 */
public class ClassificationEvent extends EventObject {

  private transient int[] classification;

  /**
  * The constructor is the same as that for EventObject, except that the
  * classification values are indicated.
  */

  public ClassificationEvent(Object source, int[] classification){
    super(source);
    this.classification = classification;
  }

    //begin accessors
    public void setClassification (int[] classification) {
      this.classification = classification;
    }
    public int[] getClassification () {
      return this.classification;
    }
    //end accessors

}