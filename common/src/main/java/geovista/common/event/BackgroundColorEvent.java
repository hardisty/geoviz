/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class BackgroundColorEvent
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: jmacgill $
 $Id: BackgroundColorEvent.java,v 1.1 2004/12/03 19:27:04 jmacgill Exp $
 $Date: 2004/12/03 19:27:04 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package geovista.common.event;

import java.awt.Color;
import java.util.EventObject;


/**
 * An BackgroundColorEvent signals that a single observation has been singled out.
 * This is often because the user has "moused over" that observation.
 *
 * The integer represents the index of that observation in the overall data set.
 *
 */
public class BackgroundColorEvent extends EventObject {

  private transient Color background;

  /**
  * The constructor is the same as that for EventObject, except that the
  * background value is indicated.
  */

  public BackgroundColorEvent(Object source, Color background){
    super(source);
    this.background = background;
  }

    //begin accessors
    public void setBackgroundColor (Color background) {
      this.background = background;
    }
    public Color getBackgroundColor () {
      return this.background;
    }
    //end accessors

}