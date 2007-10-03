/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class GeoDataTestNulls
 Copyright (c), 2002, GeoVISTA Center
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
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: GeoDataTestNulls.java,v 1.2 2005/09/15 15:04:03 hardisty Exp $
 $Date: 2005/09/15 15:04:03 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.readers.geog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

import javax.swing.event.EventListenerList;

import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.readers.csv.GeogCSVReader;

/**
 * Reads shapefiles from included resources
 *
 * Object[0] = names of variables
 * 0bject[1] = data (double[], int[], or String[])
 * 0bject[1] = data (double[], int[], or String[])
 * ...
 * Object[n-1] = the shapefile data
 *
 * also see DBaseFile, ShapeFile
 *
 */
public class GeoDataTestNulls {

  public static final String COMMAND_DATA_SET_MADE = "dataMade";

  private transient DataSetForApps dataForApps;
  private transient EventListenerList listenerList;

  public GeoDataTestNulls() {
    super();
    listenerList = new EventListenerList();
    this.dataForApps = this.makeDataSetForApps();
    this.fireActionPerformed(COMMAND_DATA_SET_MADE);
  }

  private DataSetForApps makeDataSetForApps(){
      Object[] shpData = null;
      try {

        Class cl = this.getClass();

        InputStream isCSV = cl.getResourceAsStream("resources/test_nulls.csv");
        GeogCSVReader csv = new GeogCSVReader();
        Object[] csvData = csv.readFile(isCSV);

        shpData = new Object[csvData.length + 1];
        for (int i = 0; i < csvData.length; i++) {
          shpData[i] = csvData[i];
        }

        InputStream isSHP = cl.getResourceAsStream("resources/test_nulls.shp");
        shpData[csvData.length] = new ShapeFile(isSHP);

      } catch (Exception ex) {
        ex.printStackTrace();
      }
      //this.fireActionPerformed(COMMAND_DATA_SET_MADE);
        this.dataForApps = new DataSetForApps(shpData);
      return dataForApps;

  }

  //private Object[] makeDataSet(String fileName){
  //    this.makeDataSetForApps(fileName);
  //    return dataForApps.getDataObjectOriginal();
  //}

  public void setDataForApps (DataSetForApps dataForApps) {
      this.dataForApps = dataForApps;
    }
    public DataSetForApps getDataForApps() {
      return this.dataForApps;
    }

    public Object[] getDataSet() {
      return this.dataForApps.getDataObjectOriginal();

    }


  /**
   * implements ActionListener
   */
  public void addActionListener(ActionListener l) {
    listenerList.add(ActionListener.class, l);
    this.fireActionPerformed(COMMAND_DATA_SET_MADE);

  }

  /**
   * removes an ActionListener from the button
   */
  public void removeActionListener(ActionListener l) {
    listenerList.remove(ActionListener.class, l);
  }

  /**
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  protected void fireActionPerformed(String command) {
   // Guaranteed to return a non-null array
   Object[] listeners = listenerList.getListenerList();
   ActionEvent e = null;
   // Process the listeners last to first, notifying
   // those that are interested in this event
   for (int i = listeners.length - 2; i >= 0; i -= 2) {
     if (listeners[i] == ActionListener.class) {
       // Lazily create the event:
       if (e == null) {
         e = new ActionEvent(this,
                    ActionEvent.ACTION_PERFORMED,
                    command);
       }
       ((ActionListener)listeners[i + 1]).actionPerformed(e);
      }
    }
  }
  public EventListenerList getListenerList() {
    return listenerList;
  }
  public void setListenerList(EventListenerList listenerList) {
    this.listenerList = listenerList;
  }

}
