/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.matrix;

import geovista.matrix.map.MapMatrixElement;
import geovista.matrix.scatterplot.ScatterPlotMatrixElement;

public class MapAndScatterplotMatrix extends BiPlotMatrix
// implements DataSetListener, ColumnAppendedListener,
// IndicationListener, SubspaceListener,
// ColorArrayListener
{

	public MapAndScatterplotMatrix() {
		super();
		this.setElementClass1(ScatterPlotMatrixElement.class);
		this.setElementClass2(MapMatrixElement.class);

	}

} // end class
