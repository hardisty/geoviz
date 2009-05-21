/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.geoviz.map;

import java.awt.Shape;
import java.awt.geom.Point2D;

import geovista.projection.OldProjection;

/**
 * A default projection that does nothing.
 */
public class ProjectionIdentity implements OldProjection {

	/*
	 * This returns the data passed in as a point without otherwise changing it.
	 * The second argument can be null, or not. If it is not, that object will
	 * returned by the method
	 */
	public Point2D.Double project(double lat, double longVal, Point2D.Double pt) {
		if (pt == null) {
			pt = new Point2D.Double(longVal, lat);
		} else {
			pt.setLocation(longVal, lat);
		}
		return pt;
	}

	public Shape project(Shape shpIn) {
		return shpIn;
	}
}