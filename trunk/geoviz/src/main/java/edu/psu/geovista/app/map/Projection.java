/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class Projection
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: Projection.java,v 1.4 2004/05/05 17:23:10 hardisty Exp $
 $Date: 2004/05/05 17:23:10 $
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



package  edu.psu.geovista.app.map;
import java.awt.Shape;
import java.awt.geom.Point2D;

/**
 * Defines what projections must do.
 */
public interface Projection {

    /*
     * this is in radians unless the projection specifies otherwise.
     */
    public Point2D.Double project (double lat, double longVal, Point2D.Double pt);
    public Shape project(Shape input);


}