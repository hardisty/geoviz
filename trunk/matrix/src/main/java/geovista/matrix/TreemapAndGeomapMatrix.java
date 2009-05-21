/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.matrix;

import geovista.matrix.map.MapMatrixElement;
import geovista.matrix.treemap.TreeMapMatrixElement;

public class TreemapAndGeomapMatrix extends BiPlotMatrix

{

	public TreemapAndGeomapMatrix() {
		super();
		this.setElementClass1(MapMatrixElement.class);
		this.setElementClass2(TreeMapMatrixElement.class);

	}

}