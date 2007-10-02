/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class DataSetListener
 Copyright (c), 2002, GeoVISTA Center
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: DataSetListener.java,v 1.3 2005/02/19 02:43:55 hardisty Exp $
 $Date: 2005/02/19 02:43:55 $
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

import java.util.EventListener;


/**
 * This interface enables listening to senders of DataSetEvents.
 *
 * This interface also enables "fireDataSetChanged" methods in classes
 * that generate and broadcast DataSetEvents.
 *
 */
public interface DataSetListener extends EventListener {


  public void dataSetChanged(DataSetEvent e);


}
