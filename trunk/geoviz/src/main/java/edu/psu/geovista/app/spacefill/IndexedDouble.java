/* -------------------------------------------------------------------
 Java source file for the class FillOrder
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: IndexedDouble.java,v 1.1 2006/02/17 17:35:59 hardisty Exp $
 $Date: 2006/02/17 17:35:59 $
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

package edu.psu.geovista.app.spacefill;

public class IndexedDouble implements Comparable {
  private int index;
  private double value;
  IndexedDouble(int ind, double val) {
    index = ind;
    value = val;
  }

  public int compareTo(Object o) {
    IndexedDouble indexD = (IndexedDouble) o;
    int val = 0;
    if (this.value < indexD.value) {
      val = -1;
    }
    else if (this.value > indexD.value) {
      val = 1;
    }
    return val;
  }

  public int getIndex() {
    return index;
  }

  public double getValue() {
    return value;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public void setValue(double value) {
    this.value = value;
  }

}
