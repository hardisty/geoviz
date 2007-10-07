/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ClassificationEvent
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Xiping Dai
 $Author: xpdai $
 $Id: ClassificationResultEvent.java,v 1.2 2004/10/07 21:25:47 xpdai Exp $
 $Date: 2004/10/07 21:25:47 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.common.event;

import java.util.EventObject;

import geovista.common.classification.ClassificationResult;


/**
 * An ClassificationEvent signals that the data has been classified into n
 * classes.
 *
 * The integers represents the class of each observation in the overall data
 * set.
 *
 */
public class ClassificationResultEvent extends EventObject {

  private transient ClassificationResult classification;

  /**
  * The constructor is the same as that for EventObject, except that the
  * classification values are indicated.
  */

  public ClassificationResultEvent(Object source, ClassificationResult classification){
    super(source);
    this.classification = classification;
  }

    //begin accessors
    public void setClassificationResult (ClassificationResult classification) {
      this.classification = classification;
    }
    public ClassificationResult getClassificationResult () {
      return this.classification;
    }

    //end accessors

}
