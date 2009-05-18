/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */
package geovista.cartogram;

import geovista.geoviz.scatterplot.ScatterPlot;
import geovista.matrix.BiPlotMatrix;

public class CartogramAndScatterplotMatrix extends BiPlotMatrix

{

	public CartogramAndScatterplotMatrix() {
		super();
		this.setElementClass1(ScatterPlot.class);
		this.setElementClass2(CartogramMatrixElement.class);

	}

}
