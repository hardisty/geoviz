/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ClassBoundariesEvent
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Xiping Dai
 $Author: xpdai $
 $Id: ClassBoundariesEvent.java,v 1.2 2004/10/04 21:22:20 xpdai Exp $
 $Date: 2004/10/04 21:22:20 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package geovista.common.event;

import java.util.EventObject;


/**
 * An ClassBoundariesEvent signals that the data has been classified into n
 * classes.
 *
 * The integers represents the class of each observation in the overall data
 * set.
 *
 */
public class ClassBoundariesEvent extends EventObject {

  private transient double[] classBoundaries;
  private transient double[] classBoundariesY;

  /**
  * The constructor is the same as that for EventObject, except that the
  * classBoundaries values are indicated.
  */

  public ClassBoundariesEvent(Object source, double[] classBoundaries){
    super(source);
    this.classBoundaries = classBoundaries;
  }

    //begin accessors
    public void setClassBoundaries (double[] classBoundaries) {
      this.classBoundaries = classBoundaries;
    }
    public double[] getClassBoundaries () {
      return this.classBoundaries;
    }
    //end accessors

    public ClassBoundariesEvent(Object source, double[] classBoundariesX, double[] classBoundariesY){
      super(source);
      this.classBoundaries = classBoundariesX;
      this.classBoundariesY = classBoundariesY;
    }

    public void setClassBoundariesY (double[] classBoundariesY) {
      this.classBoundariesY = classBoundariesY;
    }
    public double[] getClassBoundariesY () {
      return this.classBoundariesY;
    }

}
