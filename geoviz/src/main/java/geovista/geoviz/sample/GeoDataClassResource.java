/* 
 Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty
 */

package geovista.geoviz.sample;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import geovista.common.data.DataSetForApps;
import geovista.common.data.GeoDataSource;

/**
 * Reads shapefiles from included resources
 * 
 * Object[0] = names of variables 0bject[1] = data (double[], int[], or
 * String[]) 0bject[1] = data (double[], int[], or String[]) ... Object[n-1] =
 * the shapefile data
 * 
 * also see DBaseFile, ShapeFile
 * 
 */
public abstract class GeoDataClassResource implements GeoDataSource {

	public static final String COMMAND_DATA_SET_MADE = "dataMade";

	private transient DataSetForApps dataForApps;
	private transient EventListenerList listenerList;
	protected final static Logger logger = Logger
			.getLogger(GeoDataClassResource.class.getName());

	public GeoDataClassResource() {
		super();
		listenerList = new EventListenerList();
		fireActionPerformed(COMMAND_DATA_SET_MADE);
	}

	protected abstract DataSetForApps makeDataSetForApps();

	// private Object[] makeDataSet(String fileName){
	// this.makeDataSetForApps(fileName);
	// return dataForApps.getDataObjectOriginal();
	// }

	public void setDataForApps(DataSetForApps dataForApps) {
		this.dataForApps = dataForApps;
	}

	public DataSetForApps getDataForApps() {
		if (dataForApps == null) {
			dataForApps = makeDataSetForApps();
		}
		return dataForApps;
	}

	public Object[] getDataSet() {
		if (dataForApps == null) {
			dataForApps = makeDataSetForApps();
		}
		return dataForApps.getDataObjectOriginal();

	}

	/**
	 * implements ActionListener
	 */
	public void addActionListener(ActionListener l) {
		listenerList.add(ActionListener.class, l);
		fireActionPerformed(COMMAND_DATA_SET_MADE);
		logger.finest("GeoDataClassResource.addActionListener, Hi!!");
	}

	/**
	 * removes an ActionListener from the button
	 */
	public void removeActionListener(ActionListener l) {
		listenerList.remove(ActionListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
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
					e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
							command);
				}
				((ActionListener) listeners[i + 1]).actionPerformed(e);
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
