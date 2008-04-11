/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

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
public class DataSetModifiedBroadcaster implements DataSetListener {
	protected final static Logger logger = Logger
			.getLogger(DataSetModifiedBroadcaster.class.getName());
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
		return listenerList;
	}

	public void dataSetChanged(DataSetEvent e) {
		// XXX this would prevent listeners from having a reference to the
		// original
		// data structure
		dataSet = new DataSetForApps(e.getDataSet());
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
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
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
					e = new DataSetModifiedEvent(this, dataSet
							.getDataObjectOriginal(), newData);

				}
				((DataSetModifiedListener) listeners[i + 1]).dataSetModified(e);
			}
		}
	}

	public void setNewData(double[] newData) {
		this.newData = newData;
		fireDataSetModifiedChanged();
	}

	public void setDataSet(DataSetForApps dataSet) {
		this.dataSet = dataSet;
	}
}
