/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class MapScatterplotTreemapMatrix
 Copyright (c), 2003, Frank Hardisty
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: MapScatterplotTreemapMatrix.java,v 1.1 2005/04/19 14:07:03 hardisty Exp $
 $Date: 2005/04/19 14:07:03 $
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
package edu.psu.geovista.app.matrix;

import edu.psu.geovista.app.map.MapMatrixElement;
import edu.psu.geovista.app.scatterplot.ScatterPlot;
import edu.psu.geovista.treemap.TreeMapMatrixElement;

/**
 *
 * @author Frank Hardisty
 * @version $Revision: 1.1 $
 */
public class MapScatterplotTreemapMatrix
    extends FixedRowMatrix
    //implements DataSetListener, DataSetModifiedListener,
    //IndicationListener, SubspaceListener,
    //ColorArrayListener

{

  public MapScatterplotTreemapMatrix() {
    super();
    this.setElementClass0(new ScatterPlot());
    this.setElementClass1(new MapMatrixElement());
    this.setElementClass2(new TreeMapMatrixElement());

  }



} //end class
