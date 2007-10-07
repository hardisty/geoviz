/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class DataSetModifiedBroadcaster
 Copyright (c), 2003, Frank Hardisty
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: DataSetModifiedBroadcaster.java,v 1.1 2004/05/05 16:44:54 hardisty Exp $
 $Date: 2004/05/05 16:44:54 $
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


import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.DataSetModifiedEvent;
import geovista.common.event.DataSetModifiedListener;



/**
 * This class is able to accept modified data for rebroadcast.
 */
public class DataSetModifiedBroadcaster implements DataSetListener{
	protected final static Logger logger = Logger.getLogger(DataSetModifiedBroadcaster.class.getName());
  private transient DataSetForApps dataSet;
  private transient EventListenerList listenerList;

  double[] newData;

  public DataSetModifiedBroadcaster() {
    super();
    listenerList = new EventListenerList();
  }



    public void setListenerList(EventListenerList listenerList) {
      this.listenerList = listenerList;
    }
    public EventListenerList getListenerList() {
      return this.listenerList;
    }

  public void dataSetChanged(DataSetEvent e){
	  //XXX this would prevent listeners from having a reference to the original 
	  //data structure
     this.dataSet = new DataSetForApps(e.getDataSet());
  }

  /**
   * implements DataSetModifiedListener
   */
  public void addDataSetModifiedListener(DataSetModifiedListener l) {
    listenerList.add(DataSetModifiedListener.class, l);
  }

  /**
   * removes an DataSetModifiedListener from the button
   */
  public void removeDataSetModifiedListener(DataSetModifiedListener l) {
    listenerList.remove(DataSetModifiedListener.class, l);
  }

  /**
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  protected void fireDataSetModifiedChanged() {
      logger.finest("ShpToShp.fireDataSetModifiedChanged, Hi!!");
   // Guaranteed to return a non-null array
   Object[] listeners = listenerList.getListenerList();
   DataSetModifiedEvent e = null;
   // Process the listeners last to first, notifying
   // those that are interested in this event
   for (int i = listeners.length - 2; i >= 0; i -= 2) {
     if (listeners[i] == DataSetModifiedListener.class) {
       // Lazily create the event:
       if (e == null) {
         e = new DataSetModifiedEvent(this,this.dataSet.getDataObjectOriginal(), this.newData);

       }
       ((DataSetModifiedListener)listeners[i + 1]).dataSetModified(e);
      }
    }
  }
  public void setNewData(double[] newData) {
    this.newData = newData;
    this.fireDataSetModifiedChanged();
  }
  public void setDataSet(DataSetForApps dataSet) {
    this.dataSet = dataSet;
  }
}
