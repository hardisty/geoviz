/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.matrix;

import javax.swing.JFrame;

import geovista.common.data.DataSetForApps;
import geovista.common.event.DataSetEvent;
import geovista.geoviz.sample.GeoData48States;

/**
 * Special geovista.matrix.treemap.tm that knows how to handle quantitative
 * data. Responds to and broadcasts DataSetChanged, IndicationChanged etc.
 * events.
 * 
 * @author Frank Hardisty
 * @version $Revision: 1.1 $
 */
public class TreemapAndGeomapMatrixMain {

	public static void main(String[] args) {
		JFrame app = new JFrame("TreemapAndGeomapMatrixMain");
		TreemapAndGeomapMatrix tm = new TreemapAndGeomapMatrix();
		app.getContentPane().add(tm);
		app.pack();
		app.setVisible(true);

		GeoData48States stateData = new GeoData48States();
		DataSetForApps dataSet = stateData.getDataForApps();
		DataSetEvent e = new DataSetEvent(dataSet, app);
		tm.dataSetChanged(e);

		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

} // end class
