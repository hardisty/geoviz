/* -------------------------------------------------------------------
 Java source file for the class MapMatrix
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: MapMatrix.java,v 1.1 2006/02/16 16:57:49 hardisty Exp $
 $Date: 2006/02/16 16:57:49 $
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
package edu.psu.geovista.matrix;

import edu.psu.geovista.matrix.map.MapMatrixElement;

/**
 *
 * @author Frank Hardisty
 * @version $Revision: 1.1 $
 */
public class MapMatrix
    extends BiPlotMatrix

{

  public MapMatrix() {
    super();
    this.setElementClass1(MapMatrixElement.class);
    this.setElementClass2(MapMatrixElement.class);

  }

} //end class
