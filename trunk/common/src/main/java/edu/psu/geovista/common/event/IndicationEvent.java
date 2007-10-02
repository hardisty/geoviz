/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class IndicationEvent
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: dxg231 $
 $Id: IndicationEvent.java,v 1.3 2004/03/12 20:05:43 dxg231 Exp $
 $Date: 2004/03/12 20:05:43 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.common.event;

import java.util.EventObject;


/**
 * An IndicationEvent signals that a single observation has been singled out.
 * This is often because the user has "moused over" that observation.
 *
 * The "indication" represents the index of that observation in the overall data set.
 *
 * This event can optionally contain information about the X and Y class
 * of the observation's classification.
 *
 */
public class IndicationEvent extends EventObject {

  private  int indication;
  private  int xClass = -1;
  private  int yClass = -1;
  private  int highLevelIndication = -1;


  /**
  * The constructor is the same as that for EventObject, except that the
  * indication value is indicated.
  */

  public IndicationEvent(Object source, int indication){
    super(source);
    this.indication = indication;
  }

  public IndicationEvent(Object source, int indication, int highLevelInd){
    super(source);
    this.indication = indication;
    this.highLevelIndication = highLevelInd;
  }

  public int getHighLevelIndication(){
    return this.highLevelIndication;
  }

  public IndicationEvent(Object source, int indication, int xClass, int yClass){
    super(source);
    this.indication = indication;
    this.xClass = xClass;
    this.yClass = yClass;
  }
    //begin accessors
    public int getIndication () {
      return this.indication;
    }

    public int getXClass(){
      return this.xClass;
    }

    public int getYClass(){
      return this.yClass;
    }

    //end accessors

}