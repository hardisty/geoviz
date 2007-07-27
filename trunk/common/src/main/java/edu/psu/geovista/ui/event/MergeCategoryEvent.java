/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ClassColorEvent
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Xiping Dai
 $Author: xpdai $
 $Id: MergeCategoryEvent.java,v 1.1 2004/03/03 18:15:40 xpdai Exp $
 $Date: 2004/03/03 18:15:40 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.ui.event;

import java.util.EventObject;


/**
 * An ClassColorEvent signals that a single observation has been singled out.
 * This is often because the user has "moused over" that observation.
 *
 *
 */
public class MergeCategoryEvent extends EventObject {

  private transient String mergedClassLabel;
  //private transient Color classColor;


  /**
  * The constructor is the same as that for EventObject, except that the
  * indication value is indicated.
  */

//  public ClassColorEvent(Object source, Color classColor){
//    super(source);
//    this.classColor = classColor;
//  }

  public MergeCategoryEvent(Object source, String mergedClassLabel){
    super(source);
    //this.classColor = classColor;
    this.mergedClassLabel = mergedClassLabel;
  }
    //begin accessors
//    public Color getClassColor () {
//      return this.classColor;
//    }

    public String getClassLabel(){
      return this.mergedClassLabel;
    }

    //end accessors

}