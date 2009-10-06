/* 
 Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty
 */

package geovista.largedata;

import java.util.logging.Logger;

import geovista.common.data.DataSetForApps;
import geovista.readers.example.GeoDataClassResource;
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
public class GeoDataNZ extends GeoDataClassResource {

	final static Logger logger = Logger.getLogger(GeoDataNZ.class.getName());

	@Override
	protected DataSetForApps makeDataSetForApps() {
		return ShapeFileDataReader.makeDataSetForApps(this.getClass(),
				"ak_CAU01_ethnic");

	}

}
