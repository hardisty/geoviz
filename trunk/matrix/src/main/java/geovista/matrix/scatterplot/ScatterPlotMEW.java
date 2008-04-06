/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.matrix.scatterplot;

import geovista.geoviz.scatterplot.ScatterPlot;
import geovista.matrix.MatrixElementWrapper;

/**
 * 
 * @author jamesm
 */
public class ScatterPlotMEW implements MatrixElementWrapper {

	/** Creates a new instance of ScatterPlotMEW */
	public ScatterPlotMEW() {

	}

	public Class getMatrixElementClass() {
		return ScatterPlot.class;
	}

}
