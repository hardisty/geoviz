/* -------------------------------------------------------------------
 Java source file for the class DataSetModifiedEvent
 Copyright (c), 2004, Frank Hardisty
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: DataSetModifiedEvent.java,v 1.1 2004/05/05 16:33:31 hardisty Exp $
 $Date: 2004/05/05 16:33:31 $
 This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.
  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.
  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
  -------------------------------------------------------------------   */

package edu.psu.geovista.common.event;

import java.util.EventObject;

/**
 * An DataSetModifiedEvent signals that some new data is available for the
 * current data set. For example, a derived field has been calculated.
 *
 */
public class DataSetModifiedEvent
    extends EventObject {

  private transient Object[] dataSet;
  private transient double[] newData;
  private  int eventType;
  public static final int TYPE_MODIFIED = 0;
  public static final int TYPE_REDUCED = 1;
  public static final int TYPE_EXTENDED = 2;

  /**
   * The constructor is the same as that for EventObject, except that the
   * dataSet and newData values are indicated. The dataSet is the unmodified
   * data set. Clients will compare this array against their current arrays to
   * find how this applies. This constructor internally
   * sets the event as TYPE_EXTENDED.
   */

  public DataSetModifiedEvent(Object source, Object[] dataSet, double[] newData) {
    super(source);
    this.eventType = DataSetModifiedEvent.TYPE_EXTENDED;
    this.dataSet = dataSet;
    this.newData = newData;
    //XXX question: should we check for the appropriate array length here???

  }

  //begin accessors
  /**
   * This method may be used to compare the broadcast dataSet reference
   * to a previously broadcast one.
   */

  public Object[] getDataSet() {
    return this.dataSet;
  }

  /**
   * Returns only the new data to be appended, if any. If there isn't any,
   * returns null.
   */

  public double[] getNewData() {
    return newData;
  }

  /**
   * Returns the type of event (TYPE_EXTENDED, TYPE_REDUCED, or TYPE_MODIFIED)
   */

  public int getEventType() {
    return eventType;
  }

  //end accessors

}
