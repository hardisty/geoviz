/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class AuxiliaryDataSetEvent
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: AuxiliaryDataSetEvent.java,v 1.2 2005/02/19 03:05:22 hardisty Exp $
 $Date: 2005/02/19 03:05:22 $
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


package edu.psu.geovista.ui.event;

import edu.psu.geovista.data.geog.DataSetForApps;


/**
 * An AuxiliaryDataSetEvent signals that a new data set is available.
 *
 */
public class AuxiliaryDataSetEvent extends DataSetEvent {


  /**
  * The constructor is the same as that for EventObject, except that the
  * dataSet values are indicated.
  *
  * This class is identical to DataSetEvent, except in name. So why have it?
  * The idea is that classes such as GeoMap may want to handle some data sets
  * in a special way. Simply adding a flag to the DataSetEvent would cause
  * other clients which did not check that flag to behave incorrectly.
  *
  */
  @Deprecated
  public AuxiliaryDataSetEvent(Object source, Object[] dataSet){
    super(source, dataSet);

  }

  public AuxiliaryDataSetEvent(DataSetForApps dataSetForApps, Object source){
    super(dataSetForApps, source);

  }


}
