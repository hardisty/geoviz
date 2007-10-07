/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class StarPlotRenderer
 Copyright (c), 2003, Frank Hardisty
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: StarPlotRenderer.java,v 1.4 2006/02/17 17:21:23 hardisty Exp $
 $Date: 2006/02/17 17:21:23 $
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
package geovista.geoviz.star;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.psu.geovista.symbolization.AffineTransformModifier;
import edu.psu.geovista.symbolization.glyph.Glyph;

/**
 * Paint a multi-dimensional "star display". We draw an n-"rayed" figure, with n =
 * the number of values set. The values are expected to range from 0 to 100.
 * Each ray is a line that extends from the origin outword, proportionately in
 * length to the value it represents. The end points of each ray are connected,
 * and the figure filled.
 * 
 * 
 * @author Frank Hardisty
 * @version $Revision: 1.4 $
 */
public class StarPlotRenderer implements Glyph {

	int[] lengths;
	float[] xPoints;
	float[] yPoints;
	float[] renderedXPoints;
	float[] renderedYPoints;
	boolean figureReady;

	GeneralPath originalFigure;
	GeneralPath originalSpikes;
	Shape paintFigure;
	Shape paintSpikes;

	Rectangle targetArea;
	AffineTransform zoomForm;

	Color fillColor;
	Color outlineColor;
	Color spikesColor;

	public static final Color defaultFillColor = Color.white;
	public static final Color defaultIndicationColor = Color.green;
	public static final Color defaultSpikesColor = new Color(20, 20, 20, 255);

	private static Stroke spikeStroke = new BasicStroke(1.2f);
	private static Stroke spikeStrokeNoFill = new BasicStroke(2f);
	private static Stroke outlineStroke = new BasicStroke(0.8f);

	public static final Color defaultOutlineColor = Color.black;

	private boolean fill = true;

	final static Logger logger = Logger.getLogger(StarPlotRenderer.class.getName());

	public StarPlotRenderer() {
		fillColor = StarPlotRenderer.defaultFillColor;
		outlineColor = StarPlotRenderer.defaultOutlineColor;
		spikesColor = StarPlotRenderer.defaultSpikesColor;
		figureReady = false;

	}

	public StarPlotRenderer copy() {
		StarPlotRenderer newCopy = new StarPlotRenderer();
		newCopy.setLengths(this.lengths);
		newCopy.setFillColor(this.getFillColor());

		return newCopy;
	}

	public void setLengths(int[] spikeLengths) {
		if (spikeLengths == null){
			return;
		}
		this.lengths = new int[spikeLengths.length];
		for (int i = 0; i < spikeLengths.length; i++) {
			this.lengths[i] = spikeLengths[i];
		}
		if (xPoints == null || xPoints.length != lengths.length) {
			xPoints = new float[lengths.length];
			yPoints = new float[lengths.length];

		}

		this.findPoints(lengths);
		this.fillPaths();
	}

	private void fillPaths() {
		originalFigure = new GeneralPath();
		originalSpikes = new GeneralPath();
		if (this.xPoints.length < 1) {
			return;
		}
		originalFigure.moveTo(this.xPoints[0], this.yPoints[0]);
		originalSpikes.moveTo(0, 0); // home
		originalSpikes.lineTo(this.xPoints[0], this.yPoints[0]);
		for (int i = 1; i < xPoints.length; i++) {
			originalFigure.lineTo(this.xPoints[i], this.yPoints[i]);
			originalSpikes.moveTo(0, 0);
			originalSpikes.lineTo(this.xPoints[i], this.yPoints[i]);
		}
		originalFigure.lineTo(this.xPoints[0], this.yPoints[0]);
		this.figureReady = true;
	}

	private void findPoints(int[] lengths) {
		// need some trigonometry here
		double nVals = lengths.length;
		double max = Math.PI * 2;
		double angle = 0;
		double fraction = 0;
		float x = 0;
		float y = 0;
		double iDouble = 0;

		for (int i = 0; i < lengths.length; i++) {
			iDouble = i;
			fraction = iDouble / nVals;
			angle = fraction * max;
			x = (float) Math.sin(angle) * lengths[i];
			y = (float) Math.cos(angle) * lengths[i];
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("Setting location, x = " + x + ", y = " + y);
			}
			xPoints[i] = x;
			yPoints[i] = y;

		}

	}

	private void projectFigure(Rectangle targetArea) {
		if (originalSpikes == null) {
			figureReady = false;
			return;
		}
		Rectangle paintArea = new Rectangle();
		paintArea.setBounds(-100, -100, 200, 200);
		// why double the x and y on the positive?
		// because the width and height need to be the negative value + the same
		// positive to be centered on zero
		this.zoomForm = AffineTransformModifier.makeGeogAffineTransform(
				paintArea, targetArea, true, true);
		paintSpikes = originalSpikes.createTransformedShape(zoomForm);
		paintFigure = originalFigure.createTransformedShape(zoomForm);
		this.renderedXPoints = null; // reset
		this.renderedYPoints = null;
		if (logger.isLoggable(Level.FINEST)){
			logger.finest("in StarPlotRenderer.projectFigure");
		}
		figureReady = true;
	}

	/**
	 * 
	 */

	public void paintStar(Graphics2D target) {
		if (!figureReady || this.paintFigure == null) {
			return;
		}
		Stroke st = target.getStroke();

		target.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		if (this.fill) {
			target.setColor(this.fillColor);
			target.fill(this.paintFigure);
			target.setColor(this.spikesColor);
			target.setStroke(StarPlotRenderer.spikeStroke);
			target.draw(paintSpikes);
			target.setColor(this.outlineColor);
			target.setStroke(StarPlotRenderer.outlineStroke);
			target.draw(this.paintFigure);
		} else {
			target.setStroke(StarPlotRenderer.spikeStrokeNoFill);
			target.setColor(this.spikesColor);
			target.draw(paintSpikes);
		}

		target.setStroke(st);

	}

	public void draw(Graphics2D g2) { // we use this when we are acting as a
		// glyph

		this.paintStar(g2);

	}

	public static void main(String[] args) {
		StarPlotRenderer sd = new StarPlotRenderer();
		int[] lengths = { 1, 34, 100, 0, 87, 22 };

		sd.setLengths(lengths);
		/*
		 * //some trig double sineAns = 0; double cosAns= 0; double radians=0;
		 * radians = 0; sineAns = Math.sin(radians); cosAns = Math.cos(radians);
		 * logger.finest("Radians = " + radians); logger.finest("Sine = " +
		 * sineAns); logger.finest("Cosine = " + cosAns);
		 * logger.finest("~~~~~~~~~~~~~~~~~~ "); radians = Math.PI/4;
		 * sineAns = Math.sin(radians); cosAns = Math.cos(radians);
		 * logger.finest("Radians = " + radians); logger.finest("Sine = " +
		 * sineAns); logger.finest("Cosine = " + cosAns);
		 * logger.finest("~~~~~~~~~~~~~~~~~~ "); radians = Math.PI/2;
		 * sineAns = Math.sin(radians); cosAns = Math.cos(radians);
		 * logger.finest("Radians = " + radians); logger.finest("Sine = " +
		 * sineAns); logger.finest("Cosine = " + cosAns);
		 * logger.finest("~~~~~~~~~~~~~~~~~~ "); radians = Math.PI; sineAns =
		 * Math.sin(radians); cosAns = Math.cos(radians);
		 * logger.finest("Radians = " + radians); logger.finest("Sine = " +
		 * sineAns); logger.finest("Cosine = " + cosAns);
		 * logger.finest("~~~~~~~~~~~~~~~~~~ "); radians = Math.PI +
		 * Math.PI / 2; sineAns = Math.sin(radians); cosAns = Math.cos(radians);
		 * logger.finest("Radians = " + radians); logger.finest("Sine = " +
		 * sineAns); logger.finest("Cosine = " + cosAns);
		 * logger.finest("~~~~~~~~~~~~~~~~~~ ");
		 */

	}

	/**
	 * Getter for property fillColor.
	 * 
	 * @return Value of property fillColor.
	 * 
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * Setter for property fillColor.
	 * 
	 * @param fillColor
	 *            New value of property fillColor.
	 * 
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public Rectangle getTargetArea() {
		return targetArea;
	}

	public void setTargetArea(Rectangle targetArea) {
		this.targetArea = targetArea;
		this.projectFigure(this.targetArea);
	}

	public int[] getSpikeLengths() {
		return this.lengths;
	}

	private void transformPoints() {
		if (zoomForm == null) {
			zoomForm = new AffineTransform();
			logger.finest("null transform in sp.transformPoints");
		}
		int nPts = this.xPoints.length;
		float[] srcPts = new float[nPts * 2];
		float[] dstPts = new float[nPts * 2];
		for (int i = 0; i < nPts; i++) {
			srcPts[i * 2] = this.xPoints[i];
			srcPts[i * 2 + 1] = this.yPoints[i];
		}
		zoomForm.transform(srcPts, 0, dstPts, 0, nPts);
		if (this.renderedXPoints == null) {
			this.renderedXPoints = new float[nPts];
			this.renderedYPoints = new float[nPts];
		}
		for (int i = 0; i < nPts; i++) {
			this.renderedXPoints[i] = dstPts[i * 2];
			this.renderedYPoints[i] = dstPts[i * 2 + 1]; // +1 for the y
			// points in x,y
			// order
		}

	}

	public float[] getRenderedXPoints() {

		this.transformPoints();
		return renderedXPoints;
	}

	public float[] getRenderedYPoints() {
		if (this.renderedYPoints == null) {
			this.transformPoints();
		}

		return renderedYPoints;
	}

	public void setLocation(Point location) { // when we are a Glyph

		int width = 50; // xxx totally abitrary
		int height = 50;
		int x = location.x - (width / 2);
		int y = location.y - (height / 2);
		Rectangle targetArea = new Rectangle(x, y, width, height);
		this.setTargetArea(targetArea);
	}

	public Shape getOutline() {
		return this.paintFigure;
	}

}
