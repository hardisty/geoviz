/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ProjectionIdentity
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: ProjectionIdentity.java,v 1.4 2004/05/05 17:23:10 hardisty Exp $
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



package  edu.psu.geovista.geoviz.map;
import java.awt.Shape;
import java.awt.geom.Point2D;

/**
 * A default projection that does nothing.
 */
public class ProjectionIdentity implements Projection {

    /*
     * This returns the data passed in as a point without otherwise changing it.
     * The second argument can be null, or not. If it is not, that object will
     * returned by the method
     */
    public Point2D.Double project (double lat, double longVal, Point2D.Double pt){
        if (pt ==  null) {
          pt  = new Point2D.Double(longVal, lat);
        } else {
          pt.setLocation(longVal, lat);
        }
        return pt;
    }
    public Shape project (Shape shpIn){
      return shpIn;
    }
}