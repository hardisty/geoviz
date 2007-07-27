/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class LayerPolygon
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: LayerPolygon.java,v 1.11 2005/08/19 19:17:32 hardisty Exp $
 $Date: 2005/08/19 19:17:32 $
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



package edu.psu.geovista.app.map;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

/**
 * put your documentation comment here
 */
public class LayerPolygon
    extends LayerShape {


  /*
   * SelectionX1 is expected to be less than selectionX2, same with Y1 and y2.
   * Selected observations should be rendered with the color "colorSelection".
   */
  public void findSelection(int selectionX1, int selectionX2, int selectionY1,
                            int selectionY2) {
    Rectangle selBox = new Rectangle(selectionX1, selectionY1,
                                     selectionX2 - selectionX1,
                                     selectionY2 - selectionY1);

    Vector selObs = new Vector();
    for (int i = 0; i < this.spatialData.length; i++) {
      Rectangle shpBox = this.spatialData[i].getBounds();
      if (selBox.intersects(shpBox)) {
        if (this.spatialData[i].contains(selBox) ||
            this.spatialData[i].intersects(selBox)) {
          selObs.add(new Integer(i));
        } //end if really intersects
      } //end if rough intersects
    } //next
    this.selectedObservations = new int[selObs.size()];
    int j = 0;
    for (Enumeration e = selObs.elements(); e.hasMoreElements(); ) {
      Integer anInt = (Integer) e.nextElement();
      this.selectedObservations[j] = anInt.intValue();
      j++;
    }
  }

  /*
   * selectionX1 is expected to be less than selectionX2, same with Y1 and y2
   */
  public void findSelectionShift(int selectionX1, int selectionX2,
                                 int selectionY1,
                                 int selectionY2) {
    Rectangle selBox = new Rectangle(selectionX1, selectionY1,
                                     selectionX2 - selectionX1,
                                     selectionY2 - selectionY1);

    Vector selObs = new Vector();
    Arrays.sort(this.selectedObservations); //have to do this for the searching
    for (int i = 0; i < this.spatialData.length; i++) {
      Rectangle shpBox = this.spatialData[i].getBounds();
      if (selBox.intersects(shpBox)) {
        if (Arrays.binarySearch(this.selectedObservations, i) < 0) {
          selObs.add(new Integer(i));
        }
      }
    }
    int[] selectedObserCp = new int[this.selectedObservations.length];
    selectedObserCp = (int[]) (this.selectedObservations.clone());
    this.selectedObservations = new int[selectedObserCp.length + selObs.size()];
    int j = 0;
    for (j = 0; j < selectedObserCp.length; j++) {
      this.selectedObservations[j] = selectedObserCp[j];
    }
    for (Enumeration e = selObs.elements(); e.hasMoreElements(); ) {
      Integer anInt = (Integer) e.nextElement();
      this.selectedObservations[j] = anInt.intValue();
      j++;
    }

  }

  public int findIndication(int x, int y) {
    for (int i = 0; i < this.spatialData.length; i++) {
      Rectangle shpBox = this.spatialData[i].getBounds();
      if (shpBox.contains(x, y)) {
        if (this.spatialData[i].contains(x, y)) {
          return i;

        } //end if really intersects
      } //end if rough intersects
    } //next
    //couldn't find anything, so
    return Integer.MIN_VALUE;
  }

}
