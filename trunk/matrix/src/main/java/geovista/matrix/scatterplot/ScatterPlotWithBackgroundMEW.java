/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.matrix.scatterplot;

import geovista.geoviz.scatterplot.ScatterPlotWithBackground;
import geovista.matrix.MatrixElementWrapper;

/**
 * 
 * @author jamesm
 */
public class ScatterPlotWithBackgroundMEW implements MatrixElementWrapper {

	/** Creates a new instance of ScatterPlotMEW */
	public ScatterPlotWithBackgroundMEW() {

	}

	public Class getMatrixElementClass() {
		return ScatterPlotWithBackground.class;
	}

}
