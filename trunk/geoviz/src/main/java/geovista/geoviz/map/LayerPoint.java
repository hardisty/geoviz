/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class LayerPoint
 Copyright (c), 2002, GeoVISTA Center
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: LayerPoint.java,v 1.11 2005/08/19 19:17:32 hardisty Exp $
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

package geovista.geoviz.map;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

import edu.psu.geovista.symbolization.glyph.Glyph;

/**
 * put your documentation comment here
 */
public class LayerPoint extends LayerShape {

	private Point2D[] originalPoints;

	public void setGlyphs(Glyph[] glyphs) {
	}

	/*
	 * SelectionX1 is expected to be less than selectionX2, same with Y1 and y2.
	 * Selected observations should be rendered with the color "colorSelection".
	 */
	public void findSelection(int selectionX1, int selectionX2,
			int selectionY1, int selectionY2) {
		Rectangle selBox = new Rectangle(selectionX1, selectionY1, selectionX2
				- selectionX1, selectionY2 - selectionY1);

		Vector selObs = new Vector();
		for (int i = 0; i < this.spatialData.length; i++) {
			Rectangle shpBox = this.spatialData[i].getBounds();
			if (selBox.intersects(shpBox)) {
				if (this.spatialData[i].contains(selBox)
						|| this.spatialData[i].intersects(selBox)) {
					selObs.add(new Integer(i));
				} // end if really intersects
			} // end if rough intersects
		} // next
		this.selectedObservations = new int[selObs.size()];
		int j = 0;
		for (Enumeration e = selObs.elements(); e.hasMoreElements();) {
			Integer anInt = (Integer) e.nextElement();
			this.selectedObservations[j] = anInt.intValue();
			j++;
		}
	}

	/*
	 * selectionX1 is expected to be less than selectionX2, same with Y1 and y2
	 */
	public void findSelectionShift(int selectionX1, int selectionX2,
			int selectionY1, int selectionY2) {
		Rectangle selBox = new Rectangle(selectionX1, selectionY1, selectionX2
				- selectionX1, selectionY2 - selectionY1);

		Vector selObs = new Vector();
		Arrays.sort(this.selectedObservations); // have to do this for the
												// searching
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
		this.selectedObservations = new int[selectedObserCp.length
				+ selObs.size()];
		int j = 0;
		for (j = 0; j < selectedObserCp.length; j++) {
			this.selectedObservations[j] = selectedObserCp[j];
		}
		for (Enumeration e = selObs.elements(); e.hasMoreElements();) {
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

				} // end if really intersects
			} // end if rough intersects
		} // next
		// couldn't find anything, so
		return Integer.MIN_VALUE;
	}

	/**
	 * sets the points that the layer will use to draw from these are assumed to
	 * be in user space
	 * 
	 * @param originalPoints
	 */
	public void setOriginalPoints(Point2D[] originalPoints) {
		this.originalPoints = originalPoints;
	}

	/**
	 * retrieves the points
	 * 
	 * @param originalPoints
	 */
	public Point2D[] getOriginalPoints() {
		return this.originalPoints;
	}

	public Point2D[] shapesToPoints(Shape[] shapes) {
		Point2D[] points = new Point2D.Double[shapes.length];
		for (int i = 0; i < shapes.length; i++) {
			Point2D pnt = new Point2D.Double();
			Shape shp = shapes[i];
			PathIterator path = shp.getPathIterator(null);
			double[] seg = new double[6];
			int segType = path.currentSegment(seg);
			if (segType != PathIterator.SEG_MOVETO) {
				throw new IllegalArgumentException(
						"LayerPointExtension.shapesToPoints expects only PathIterator.SEG_MOVETO segments");
			}
			pnt.setLocation(seg[0], seg[1]);
			points[i] = pnt;
		}

		return points;
	}

	public Shape[] pointsToShapes(Point2D[] points) {
		Shape[] shapes = new Shape[points.length];
		for (int i = 0; i < shapes.length; i++) {
			GeneralPath shp = new GeneralPath();
			Point2D pnt = points[i];
			shp.moveTo((float) pnt.getX(), (float) pnt.getY());
			shapes[i] = shp;
		}

		return shapes;
	}

	public Shape[] findShapesForPoints(Point2D[] points) {
		Shape[] shapes = new Shape[points.length];
		float shapeSize = 8f;
		float half = shapeSize / 2f;
		for (int i = 0; i < shapes.length; i++) {
			Point2D pt = points[i];

			Ellipse2D eli = new Ellipse2D.Float((float) pt.getX() - half,
					(float) pt.getY() - half, shapeSize, shapeSize);
			GeneralPath path = new GeneralPath(eli);
			shapes[i] = path;
		}

		return shapes;
	}
}
