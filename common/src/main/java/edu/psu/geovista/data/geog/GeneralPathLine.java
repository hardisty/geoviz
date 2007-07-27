/*------------------------------------------------------------------------------
 * Java source file for the class GeneralPathLine
 *
 * Original Author: Frank Hardisty hardisty@sc.edu
 * $Author: hardisty $
 * $Date: 2005/04/11 20:56:20 $
 * $Id: GeneralPathLine.java,v 1.2 2005/04/11 20:56:20 hardisty Exp $
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


package edu.psu.geovista.data.geog;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * This class wraps a java.awt.geom.GeneralPath.
 * It is intended that the only difference between this class and a GeneralPath
 * is semantic.
 *
 * This class is being introduced because there are many classes which pass
 * around a DataSetForApps in its Object[] form. Thus, the spatial type
 * information needs to be encoded in the type of the class.
 *
 *
 * @version $Revision: 1.2 $
 * @author Frank Hardisty
 */
public class GeneralPathLine
    implements Shape {
  GeneralPath gp = null;

  public static final int WIND_EVEN_ODD = GeneralPath.WIND_EVEN_ODD;
  public static final int WIND_NON_ZERO = GeneralPath.WIND_NON_ZERO;

  public GeneralPathLine() {
    gp = new GeneralPath();
  }

  public GeneralPathLine(int rule) {
    gp = new GeneralPath(rule);
  }

  public GeneralPathLine(int rule, int initialCapacity) {
    gp = new GeneralPath(rule, initialCapacity);
  }

  public GeneralPathLine(Shape s) {
    gp = new GeneralPath(s);
  }

  public synchronized void moveTo(float x, float y) {
    gp.moveTo(x, y);
  }

  public synchronized void lineTo(float x, float y) {
    gp.lineTo(x, y);
  }

  public synchronized void quadTo(float x1, float y1, float x2, float y2) {
    gp.quadTo(x1, y1, x2, y2);
  }

  public synchronized void curveTo(float x1, float y1,
                                   float x2, float y2,
                                   float x3, float y3) {
    gp.curveTo(x1, y1, x2, y2, x3, y3);
  }

  public synchronized void closePath() {
    gp.closePath();
  }

  public void append(Shape s, boolean connect) {
    gp.append(s, connect);
  }

  public void append(PathIterator pi, boolean connect) {
    gp.append(pi, connect);
  }

  public synchronized int getWindingRule() {
    return gp.getWindingRule();
  }

  public void setWindingRule(int rule) {
    gp.setWindingRule(rule);
  }

  public synchronized Point2D getCurrentPoint() {
    return gp.getCurrentPoint();
  }

  public synchronized void reset() {
    gp.reset();
  }

  public void transform(AffineTransform at) {
    gp.transform(at);
  }

  public synchronized Shape createTransformedShape(AffineTransform at) {
    return gp.createTransformedShape(at);
  }

  public java.awt.Rectangle getBounds() {
    return gp.getBounds();
  }

  public synchronized Rectangle2D getBounds2D() {
    return gp.getBounds2D();
  }

  public boolean contains(double x, double y) {
    return gp.contains(x, y);
  }

  public boolean contains(Point2D p) {
    return gp.contains(p);
  }

  public boolean contains(double x, double y, double w, double h) {
    return gp.contains(x, y, w, h);
  }

  public boolean contains(Rectangle2D r) {
    return gp.contains(r);
  }

  public boolean intersects(double x, double y, double w, double h) {
    return gp.intersects(x, y, w, h);
  }

  public boolean intersects(Rectangle2D r) {

    return gp.intersects(r);
  }

  public PathIterator getPathIterator(AffineTransform at) {
    return gp.getPathIterator(at);
  }

  public PathIterator getPathIterator(AffineTransform at, double flatness) {
    return gp.getPathIterator(at, flatness);
  }

  public Object clone() {
    return new GeneralPathLine(this.gp);
  }

}
