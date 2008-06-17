/* 
 Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty
 */

package geovista.toolkitcore.data;

import geovista.common.data.DataSetForApps;
import geovista.geoviz.sample.GeoDataClassResource;
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
public class GeoDataCartogram extends GeoDataClassResource {

	@Override
	protected DataSetForApps makeDataSetForApps() {
		return ShapeFileDataReader.makeDataSetForAppsCsv(this.getClass(),
				"cartogram");

	}

}
