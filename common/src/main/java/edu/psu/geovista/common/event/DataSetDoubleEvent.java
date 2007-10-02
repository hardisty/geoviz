/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class DataSetEvent
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: xpdai $
 $Id: DataSetDoubleEvent.java,v 1.1 2004/10/29 17:30:07 xpdai Exp $
 $Date: 2004/10/29 17:30:07 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.common.event;

import java.util.EventObject;


/**
 * An DataSetEvent signals that a new data set is available.
 *
 */
public class DataSetDoubleEvent extends EventObject {

  private transient double[][] dataSet;

  /**
  * The constructor is the same as that for EventObject, except that the
  * dataSet values are indicated.
  */

  public DataSetDoubleEvent(Object source, double[][] dataSet){
    super(source);
    this.dataSet = dataSet;
  }

    //begin accessors

    public double[][] getDataSet () {
      return this.dataSet;
    }
    //end accessors

}
