/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class DataSetEvent
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: DataSetEvent.java,v 1.6 2005/03/24 20:34:24 hardisty Exp $
 $Date: 2005/03/24 20:34:24 $
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

import edu.psu.geovista.common.data.DataSetForApps;


/**
 * An DataSetEvent signals that a new data set is available.
 *
 */
public class DataSetEvent extends EventObject {

  private Object[] dataSet;
  private transient DataSetForApps dataSetForApps;

  /**
  * The constructor is the same as that for EventObject, except that the
  * dataSet values are indicated.
  */
@Deprecated
  public DataSetEvent(Object source, Object[] dataSet){
    super(source);
    this.dataSet = dataSet;
  }
  /**
  * Note that the DataSetForApps is first. This allows us
  * to pass in a null DataSetForApps if desired, without being confused with
  * the previous cntr.
  */
  public DataSetEvent(DataSetForApps dataSetForApps, Object source){
    super(source);
    this.dataSetForApps = dataSetForApps;
    this.dataSet = dataSetForApps.getDataObjectOriginal();
  }



    public Object[] getDataSet () {
      return this.dataSet;
    }

    public DataSetForApps getDataSetForApps(){
      //let's be lazy
      if (this.dataSetForApps == null){
        this.dataSetForApps = new DataSetForApps(this.dataSet);
      }
      return this.dataSetForApps;
    }
}
