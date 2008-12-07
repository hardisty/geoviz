/* 
 Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty
 */

package geovista.readers.example;

import java.util.logging.Logger;

import geovista.common.data.DataSetForApps;
import geovista.readers.shapefile.ShapeFileDataReader;

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
public class GeoData2008Election extends GeoDataClassResource {

	final static Logger logger = Logger.getLogger(GeoData2008Election.class
			.getName());

	@Override
	protected DataSetForApps makeDataSetForApps() {
		return ShapeFileDataReader.makeDataSetForApps(this.getClass(),
				"2008Election");

	}

}
