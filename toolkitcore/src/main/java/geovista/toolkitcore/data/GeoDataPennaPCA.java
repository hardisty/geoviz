/* -------------------------------------------------------------------
 Java source file for the class GeoDataPennaPCA
 Original Author: Frank Hardisty
 $Author: hardistf $
 $Id: ComparableShapes.java,v 1.1 2005/12/05 20:17:05 hardistf Exp $
 $Date: 2005/12/05 20:17:05 $
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

package geovista.toolkitcore.data;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import org.geotools.data.shapefile.Lock;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.shp.ShapefileReader;

import geovista.common.data.DataSetForApps;
import geovista.common.data.GeoDataSource;
import geovista.geoviz.shapefile.ShapeFileDataReader;
import geovista.readers.csv.GeogCSVReader;


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
public class GeoDataPennaPCA implements GeoDataSource {

	public static final String COMMAND_DATA_SET_MADE = "dataMade";

	private transient DataSetForApps dataForApps;
	private transient EventListenerList listenerList;
	private transient ShapeFileDataReader shpReader;
	final static Logger logger = Logger.getLogger(GeoDataPennaPCA.class.getName());

	public GeoDataPennaPCA() {
		super();
		listenerList = new EventListenerList();
		// this.dataForApps = this.makeDataSetForApps();//let's be lazy
		this.fireActionPerformed(COMMAND_DATA_SET_MADE);
	}

	private DataSetForApps makeDataSetForApps() {
		  return ShapeFileDataReader.makeDataSetForAppsCsv(this.getClass(), "PennaPCA");

	}

	public void setDataForApps(DataSetForApps dataForApps) {
		this.dataForApps = dataForApps;
	}

	public DataSetForApps getDataForApps() {
		if (this.dataForApps == null) {
			this.dataForApps = this.makeDataSetForApps();
		}
		return this.dataForApps;
	}

	public Object[] getDataSet() {
		if (this.dataForApps == null) {
			this.dataForApps = this.makeDataSetForApps();
		}
		return this.dataForApps.getDataObjectOriginal();

	}

	/**
	 * implements ActionListener
	 */
	public void addActionListener(ActionListener l) {
		listenerList.add(ActionListener.class, l);
		this.fireActionPerformed(COMMAND_DATA_SET_MADE);
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("GeoDataPennaPCA.addActionListener, Hi!!");
		}
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
