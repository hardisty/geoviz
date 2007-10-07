/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ClassColorEvent
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Xiping Dai
 $Author: jxc93 $
 $Id: ClassNumberEvent.java,v 1.3 2005/10/14 14:56:37 jxc93 Exp $
 $Date: 2005/10/14 14:56:37 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package geovista.common.event;

import java.util.EventObject;


/**
 * An ClassNumberEvent signals that a single observation has been singled out.
 * This is often because the user has "moused over" that observation.
 * Jin: The event means the number of categories in a classfication result has changed. It also indicate dimension of classificaiton changed ( 1D or 2D, see comment for bivariateClassNumber)   
 * todo:  it would be better if implement the concept as with 2 subclasses: 1D ClassNumber event, 2D ClassNumberEvent.
 *
 */
public class ClassNumberEvent extends EventObject {

  private transient int classNumber;              //jin:
  private transient int[] bivariateClassNumber;   //jin: if the array is not empty, it must be a multivariate classificaiton. In this case, classNumber must be 0

  /**
  * The constructor is the same as that for EventObject, except that the
  * indication value is indicated.
  */

  public ClassNumberEvent(Object source, int classNumber){
    super(source);
    this.classNumber = classNumber;
  }

    //begin accessors
    public int getClassNumber () {
      return this.classNumber;
    }

    //end accessors

    public ClassNumberEvent(Object source, int[] classNumber){
      super(source);
      this.bivariateClassNumber = classNumber;
    }

    public int[] getBivariateClassNumber(){
      return this.bivariateClassNumber;
    }

}
