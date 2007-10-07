/* -------------------------------------------------------------------
 Java source file for the class DataSetBroadcaster
 Copyright (c), 2004, Frank Hardisty
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: DataSetBroadcaster.java,v 1.2 2005/02/19 02:50:52 hardisty Exp $
 $Date: 2005/02/19 02:50:52 $
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



package geovista.common.data;


import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import geovista.common.event.AuxiliaryDataSetEvent;
import geovista.common.event.AuxiliaryDataSetListener;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;



/**
 * This class is able to accept data for rebroadcast.
*  It builds a DataSetForApps and sends it out.
*  This class is designed to work with the R-Java link.
 */
public class DataSetBroadcaster {

  //private transient DataSetForApps dataSet;
  private transient EventListenerList listenerList;


  Object[] rawData;
  String[] varNames;
  final static Logger logger = Logger.getLogger(DataSetBroadcaster.class.getName());
  public DataSetBroadcaster() {
    super();
    listenerList = new EventListenerList();
  }

  public String initData(int nColumns) {
    //Object rawData = "";
    rawData = new Object[nColumns + 1];
    logger.finest("setting size of data = " + nColumns);
    return "" + nColumns;
  }
  public String setVariableNames(String[] varNames) {
    if(this.rawData == null){
      this.rawData = new Object[varNames.length];
    }
    this.varNames = varNames;
    String message = "DataSetBroadcaster, setVariableNames, number of names = " +
        varNames.length;
    logger.finest(message);
    rawData[0] = varNames;
    return "TRUE";
  }
  public String addData(Object data, int index) {

    try {
      this.rawData[index] = data;
    }
    catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
      return "index out of bounds thrown";
    }
    catch (NullPointerException nulle) {
      nulle.printStackTrace();
      return "null pointer exception thrown";
    }
    String message = "DataSetBroadcaster, addData, index = " + index;
    logger.finest(message);
    return "TRUE";
  }
  public void setAndFireDataSet(Object[] dataSet){
    this.rawData = dataSet;
    this.fireDataSetChanged();
  }
  public void fireAuxiliaryDataSet(DataSetForApps auxDataSet){
    this.fireAuxiliaryDataSetChanged(auxDataSet);
  }
  public String fireDataSetChanged() {
    if (logger.isLoggable(Level.FINEST)){
      logger.finest("entering fireDataSetChanged()");		
			
	
    }
    if (this.rawData == null) {
      String message =
          "DataSetBroadcaster, fireDataSetChanged called while data is null";
      logger.finest(message);
      return message;
    }
    DataSetForApps dataSet = new DataSetForApps(rawData);
    this.fireDataSetChanged(dataSet);
    if (logger.isLoggable(Level.FINEST)){
      String message = "DataSetBroadcaster, fireDataSetChanged called, ok";
      logger.finest(message);
    }
    return "TRUE";
  }

  /**
   * echoStringArray is useful when calling from SJava or the like.
   * It is expected that an array of strings is the object passed in.
   *
   * @param stringObj Object
   * @return String
   */
  public String echoStringArray(Object stringObj) {
    String[] strings = null;
    try {
      strings = (String[]) stringObj;
    }
    catch (ClassCastException cce) {
      return "ClassCastException thrown " + cce.getMessage();
    }
    String returnString = "";
    for (int i = 0; i < strings.length; i++) {
      returnString = returnString + "," + strings[i];
    }
    return returnString;
  }


    public void setListenerList(EventListenerList listenerList) {
      this.listenerList = listenerList;
    }
    public EventListenerList getListenerList() {
      return this.listenerList;
    }

//  public void dataSetChanged(DataSetEvent e){
//     this.dataSet = new DataSetForApps(e.getDataSet());
//  }

  /**
   * adds a DataSetListener
   */
  public void addDataSetListener(DataSetListener l) {
    listenerList.add(DataSetListener.class, l);
  }

  /**
   * removes a DataSetListener
   */
  public void removeDataSetListener(DataSetListener l) {
    listenerList.remove(DataSetListener.class, l);
  }

  /**
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  protected void fireDataSetChanged(DataSetForApps data) {
      logger.finest("ShpToShp.fireDataSetChanged, Hi!!");
   // Guaranteed to return a non-null array
   Object[] listeners = listenerList.getListenerList();
   DataSetEvent e = null;
   // Process the listeners last to first, notifying
   // those that are interested in this event
   for (int i = listeners.length - 2; i >= 0; i -= 2) {
     if (listeners[i] == DataSetListener.class) {
       // Lazily create the event:
       if (e == null) {
         e = new DataSetEvent(data, this);

       }
       ((DataSetListener)listeners[i + 1]).dataSetChanged(e);
      }
    }
  }
  /**
   * adds a AuxiliaryDataSetListener
   */
  public void addAuxiliaryDataSetListener(AuxiliaryDataSetListener l) {
    listenerList.add(AuxiliaryDataSetListener.class, l);
  }

  /**
   * removes a AuxiliaryDataSetListener
   */
  public void removeAuxiliaryDataSetListener(AuxiliaryDataSetListener l) {
    listenerList.remove(AuxiliaryDataSetListener.class, l);
  }

  /**
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  protected void fireAuxiliaryDataSetChanged(DataSetForApps dataSet) {
      logger.finest("ShpToShp.fireAuxiliaryDataSetChanged, Hi!!");
   // Guaranteed to return a non-null array
   Object[] listeners = listenerList.getListenerList();
   AuxiliaryDataSetEvent e = null;
   // Process the listeners last to first, notifying
   // those that are interested in this event
   for (int i = listeners.length - 2; i >= 0; i -= 2) {
     if (listeners[i] == AuxiliaryDataSetListener.class) {
       // Lazily create the event:
       if (e == null) {
         e = new AuxiliaryDataSetEvent(dataSet, this);

       }
       ((AuxiliaryDataSetListener)listeners[i + 1]).dataSetAdded(e);
      }
    }
  }
}
