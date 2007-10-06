/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class CartogramMatrix
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: CartogramMatrix.java,v 1.1 2005/03/19 00:48:39 hardisty Exp $
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

import java.awt.Dimension;

import javax.swing.JFrame;

import edu.psu.geovista.geoviz.sample.GeoDataGeneralizedStates;
import geovista.matrix.BiPlotMatrix;


/**
 *
 * @author Frank Hardisty
 * @version $Revision: 1.1 $
 */
public class CartogramMatrix
    extends BiPlotMatrix
    //implements DataSetListener, DataSetModifiedListener,
    //IndicationListener, SubspaceListener,
    //ColorArrayListener
{

  public CartogramMatrix() {
    super();
    this.setElementClass1(CartogramMatrixElement.class);
    this.setElementClass2(CartogramMatrixElement.class);

  }
  public static void main(String[] args) {

    GeoDataGeneralizedStates stateData = new GeoDataGeneralizedStates();

    CartogramMatrix gui = new CartogramMatrix();
    gui.setPreferredSize(new Dimension(500,500));
    JFrame app = new JFrame("Cartogram Matrix");
    app.getContentPane().add(gui);
    gui.setDataSet(stateData.getDataForApps());
    app.pack();
    app.setVisible(true);

    app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }


} //end class
