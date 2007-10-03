/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class CartogramAndScatterplotMatrix
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: CartogramAndScatterplotMatrix.java,v 1.1 2005/03/19 00:48:39 hardisty Exp $
 $Date: 2005/03/19 00:48:39 $
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 -------------------------------------------------------------------   */
package edu.psu.geovista.cartogram;

import edu.psu.geovista.matrix.BiPlotMatrix;
import edu.psu.geovista.matrix.scatterplot.ScatterPlot;

/**
 *
 * @author Frank Hardisty
 * @version $Revision: 1.1 $
 */
public class CartogramAndScatterplotMatrix
    extends BiPlotMatrix

{

  public CartogramAndScatterplotMatrix() {
    super();
    this.setElementClass1(ScatterPlot.class);
    this.setElementClass2(CartogramMatrixElement.class);

  }



} //end class
