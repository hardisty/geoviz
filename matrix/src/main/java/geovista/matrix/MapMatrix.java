/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.matrix;

import geovista.matrix.map.MapMatrixElement;

public class MapMatrix extends BiPlotMatrix

{

	public MapMatrix() {
		super();
		this.setElementClass1(MapMatrixElement.class);
		this.setElementClass2(MapMatrixElement.class);

	}

}
