/*
 * ScatterPlotMEW.java
 *
 * Created on June 2, 2004, 11:51 AM
 */

package edu.psu.geovista.matrix.scatterplot;

import edu.psu.geovista.matrix.MatrixElementWrapper;

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
