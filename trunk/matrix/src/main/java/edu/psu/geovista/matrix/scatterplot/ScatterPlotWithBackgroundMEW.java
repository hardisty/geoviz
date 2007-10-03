/**
 * Title: ScatterPlotWithBackgroundMEW
 * Description: construct a scatterplot
 * Copyright:    Copyright (c) 2001
 * Company: GeoVISTA Center
 * @author Xiping Dai
 * @version 1.0
 */

package edu.psu.geovista.matrix.scatterplot;

import edu.psu.geovista.matrix.MatrixElementWrapper;

/**
 *
 * @author  jamesm
 */
public class ScatterPlotWithBackgroundMEW implements MatrixElementWrapper{

    /** Creates a new instance of ScatterPlotMEW */
    public ScatterPlotWithBackgroundMEW() {

    }

    public Class getMatrixElementClass() {
        return ScatterPlotWithBackground.class;
    }

}
