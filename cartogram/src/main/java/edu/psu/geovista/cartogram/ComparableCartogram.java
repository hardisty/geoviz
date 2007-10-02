/* -------------------------------------------------------------------
 Java source file for the class ComparableShapes
 Original Author: Frank Hardisty
 $Author: hardistf $
 $Id: ComparableShapes.java,v 1.1 2005/12/05 20:17:05 hardistf Exp $
 $Date: 2005/12/05 20:17:05 $
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

import java.awt.Shape;

import edu.psu.geovista.common.data.DataSetForApps;

/*
 * Class used to compare arrays of shapes for equality.
 */

public final class ComparableCartogram {
  private Shape[] inputShapes;
  private Shape[] cartogramShapes;
  private int maxNSquareLog;
  private double blurWidth;
  private double blurWidthFactor;
  private int var;


  public ComparableCartogram(DataSetForApps inputData, int maxNSquareLog, double blurWidth, double blurWidthFactor, int var) {
    this.inputShapes = inputData.getShapeData();
    this.maxNSquareLog = maxNSquareLog;
    this.blurWidth = blurWidth;
    this.blurWidthFactor = blurWidthFactor;
    this.var = var;
  }

  public boolean isEqualTo(Object obj) {
    if (! (obj instanceof ComparableCartogram)) {
      return false;
    }
    ComparableCartogram testShapes = (ComparableCartogram) obj;
    if(this.cartogramShapes == null){
      return false;
    }
    if (testShapes.var != this.var){
      return false;
    }
    if (testShapes.inputShapes.length != this.inputShapes.length) {
      return false;
    }
    if (testShapes.maxNSquareLog != this.maxNSquareLog) {
      return false;
    }
    if (testShapes.blurWidth != this.blurWidth) {
      return false;
    }
    if (testShapes.blurWidthFactor != this.blurWidthFactor) {
      return false;
    }
    if (testShapes.inputShapes.length != this.inputShapes.length) {
      return false;
    }

    Shape[] first = testShapes.inputShapes;
    Shape[] second = this.inputShapes;
    if (first == null || second == null){
      return false;
    }
    if (first.length != second.length){
      return false;
    }


    for (int i = 0; i < first.length; i++){
      if (!(first[i].getBounds2D().equals(second[i].getBounds2D()))){
        return false;
      }
     }


    return true;
  }

  public Shape[] getCartogramShapes() {
    return cartogramShapes;
  }

  public void setCartogramShapes(Shape[] cartogramShapes) {
    this.cartogramShapes = cartogramShapes;

  }


}
