/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class SubspaceEvent
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: SubspaceEvent.java,v 1.2 2003/05/05 17:34:40 hardisty Exp $
 $Date: 2003/05/05 17:34:40 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.ui.event;

import java.util.EventObject;


/**
 * An SubspaceEvent signals that a set of variables, or columns, is
 * selected.
 *
 */
public class SubspaceEvent extends EventObject {

  private transient int[] subspace;

  /**
  * The constructor is the same as that for EventObject, except that the
  * subspace values are indicated.
  */

  public SubspaceEvent(Object source, int[] subspace){
    super(source);
    this.subspace = subspace;
  }

    //begin accessors
    public void setSubspace (int[] subspace) {
      this.subspace = subspace;
    }
    public int[] getSubspace () {
      return this.subspace;
    }
    //end accessors

}