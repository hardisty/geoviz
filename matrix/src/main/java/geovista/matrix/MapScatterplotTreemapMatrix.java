/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.matrix;

import geovista.matrix.map.MapMatrixElement;
import geovista.matrix.scatterplot.ScatterPlotMatrixElement;
import geovista.matrix.treemap.TreeMapMatrixElement;

public class MapScatterplotTreemapMatrix extends FixedRowMatrix
// implements DataSetListener, DataSetModifiedListener,
// IndicationListener, SubspaceListener,
// ColorArrayListener

{

	public MapScatterplotTreemapMatrix() {
		super();
		setElementClass0(new ScatterPlotMatrixElement());
		setElementClass1(new MapMatrixElement());
		setElementClass2(new TreeMapMatrixElement());

	}

} // end class
