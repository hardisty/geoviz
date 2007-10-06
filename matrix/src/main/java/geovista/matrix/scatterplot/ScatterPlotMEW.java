/*
 * ScatterPlotMEW.java
 *
 * Created on June 2, 2004, 11:51 AM
 */

package geovista.matrix.scatterplot;

import geovista.matrix.MatrixElementWrapper;

/**
 *
 * @author  jamesm
 */
public class ScatterPlotMEW implements MatrixElementWrapper{
    
    /** Creates a new instance of ScatterPlotMEW */
    public ScatterPlotMEW() {
        
    }
    
    public Class getMatrixElementClass() {
        return ScatterPlot.class;
    }
    
}
