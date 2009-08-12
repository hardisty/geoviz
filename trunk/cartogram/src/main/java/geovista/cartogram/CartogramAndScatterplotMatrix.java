/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */
package geovista.cartogram;

import geovista.matrix.BiPlotMatrix;
import geovista.matrix.scatterplot.ScatterPlotMatrixElement;

public class CartogramAndScatterplotMatrix extends BiPlotMatrix

{

	public CartogramAndScatterplotMatrix() {
		super();
		this.setElementClass1(ScatterPlotMatrixElement.class);
		this.setElementClass2(CartogramMatrixElement.class);

	}

}
