/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ClassColorEvent
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Xiping Dai
 $Author: xpdai $
 $Id: ClassColorEvent.java,v 1.1 2004/03/03 18:17:25 xpdai Exp $
 $Date: 2004/03/03 18:17:25 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.common.event;

import java.awt.Color;
import java.util.EventObject;


/**
 * An ClassColorEvent signals that a single observation has been singled out.
 * This is often because the user has "moused over" that observation.
 *
 *
 */
public class ClassColorEvent extends EventObject {

  private transient String classLabel;
  private transient Color classColor;


  /**
  * The constructor is the same as that for EventObject, except that the
  * indication value is indicated.
  */

  public ClassColorEvent(Object source, Color classColor){
    super(source);
    this.classColor = classColor;
  }

  public ClassColorEvent(Object source, String classLabel, Color classColor){
    super(source);
    this.classColor = classColor;
    this.classLabel = classLabel;
  }
    //begin accessors
    public Color getClassColor () {
      return this.classColor;
    }

    public String getClassLabel(){
      return this.classLabel;
    }

    //end accessors

}