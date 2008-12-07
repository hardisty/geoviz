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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import geovista.symbolization.AffineTransformModifier;
import geovista.symbolization.glyph.Glyph;

/**
 * Paint a multi-dimensional "star display". We draw an n-"rayed" figure, with n =
 * the number of values set. The values are expected to range from 0 to 100.
 * Each ray is a line that extends from the origin outword, proportionately in
 * length to the value it represents. The end points of each ray are connected,
 * and the figure filled.
 * 
 * 
 * @author Frank Hardisty
 * 
 */
public class StarPlotRenderer implements Glyph {

	private static ArrayList<Color> spikeColors = createSpikeColors();

	int[] lengths;
	float[] xPoints;
	float[] yPoints;
	float[] renderedXPoints;
	float[] renderedYPoints;

	boolean figureReady;

	boolean isPaintSpikes = false;

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

	private final boolean fill = true;

	final static Logger logger = Logger.getLogger(StarPlotRenderer.class
			.getName());

	public StarPlotRenderer() {
		fillColor = StarPlotRenderer.defaultFillColor;
		outlineColor = StarPlotRenderer.defaultOutlineColor;
		spikesColor = StarPlotRenderer.defaultSpikesColor;
		figureReady = false;

	}

	private static ArrayList<Color> createSpikeColors() {
		ArrayList<Color> colors = new ArrayList<Color>();

		colors.add(Color.blue);
		colors.add(Color.cyan);
		colors.add(Color.green);
		colors.add(Color.magenta);
		colors.add(Color.orange);
		colors.add(Color.pink);
		colors.add(Color.red);
		colors.add(Color.yellow);

		return colors;
	}

	public StarPlotRenderer copy() {
		StarPlotRenderer newCopy = new StarPlotRenderer();
		newCopy.setLengths(lengths);
		newCopy.setFillColor(getFillColor());

		return newCopy;
	}

	public void setLengths(int[] spikeLengths) {
		if (spikeLengths == null) {
			return;
		}
		lengths = new int[spikeLengths.length];
		for (int i = 0; i < spikeLengths.length; i++) {
			lengths[i] = spikeLengths[i];
		}
		if (xPoints == null || xPoints.length != lengths.length) {
			xPoints = new float[lengths.length];
			yPoints = new float[lengths.length];

		}

		findPoints(lengths);
		fillPaths();
	}

	private void fillPaths() {
		originalFigure = new GeneralPath();
		originalSpikes = new GeneralPath();
		if (xPoints.length < 1) {
			return;
		}
		originalFigure.moveTo(xPoints[0], yPoints[0]);
		originalSpikes.moveTo(0, 0); // home
		originalSpikes.lineTo(xPoints[0], yPoints[0]);
		for (int i = 1; i < xPoints.length; i++) {
			originalFigure.lineTo(xPoints[i], yPoints[i]);
			originalSpikes.moveTo(0, 0);
			originalSpikes.lineTo(xPoints[i], yPoints[i]);
		}
		originalFigure.lineTo(xPoints[0], yPoints[0]);
		figureReady = true;
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
		zoomForm = AffineTransformModifier.makeGeogAffineTransform(paintArea,
				targetArea, true, true);
		paintSpikes = originalSpikes.createTransformedShape(zoomForm);
		paintFigure = originalFigure.createTransformedShape(zoomForm);
		// renderedXPoints = null; // reset
		// renderedYPoints = null;
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("in StarPlotRenderer.projectFigure");
		}
		// if (renderedXPoints == null) {
		// getRenderedXPoints();
		// }
		transformPoints();
		// zoomForm.transform(renderedXPoints, 0, renderedXPoints, 0,
		// xPoints.length - 1);
		// zoomForm.transform(renderedYPoints, 0, renderedYPoints, 0,
		// xPoints.length - 1);
		figureReady = true;
	}

	/**
	 * 
	 */

	public void paintStar(Graphics2D target) {
		if (!figureReady || paintFigure == null) {
			return;
		}
		Stroke st = target.getStroke();

		target.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		if (fill) {
			target.setColor(fillColor);
			target.fill(paintFigure);
			if (isPaintSpikes) {
				paintSpikeColors(target);
			}
			target.setColor(spikesColor);
			target.setStroke(StarPlotRenderer.spikeStroke);
			target.draw(paintSpikes);
			target.setColor(outlineColor);
			target.setStroke(StarPlotRenderer.outlineStroke);
			target.draw(paintFigure);
		} else {
			target.setStroke(StarPlotRenderer.spikeStrokeNoFill);
			target.setColor(spikesColor);
			target.draw(paintSpikes);
		}

		target.setStroke(st);

	}

	public void paintStarOutline(Graphics2D target, Color outlineColor,
			float size) {
		if (!figureReady || paintFigure == null) {
			return;
		}
		Stroke st = target.getStroke();
		float[] dash = { .5f };
		BasicStroke outlineStroke = new BasicStroke(size, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_ROUND, 1f, dash, 0);
		outlineStroke = new BasicStroke(size);

		target.setStroke(outlineStroke);

		target.setColor(outlineColor);
		target.draw(paintFigure);

		target.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

	}

	private void paintSpikeColors(Graphics2D g2) {
		Stroke newStroke = new BasicStroke(3f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL);
		g2.setStroke(newStroke);
		renderedXPoints = getRenderedXPoints();
		renderedYPoints = getRenderedYPoints();
		int midX = (targetArea.width / 2) + targetArea.x;
		int midY = (targetArea.height / 2) + targetArea.y;
		for (int i = 0; i < renderedXPoints.length; i++) {
			int colorNum = i % spikeColors.size();
			g2.setColor(spikeColors.get(colorNum));
			g2.drawLine(midX, midY, (int) renderedXPoints[i],
					(int) renderedYPoints[i]);
		}

	}

	public void draw(Graphics2D g2) { // we use this when we are acting as a
		// glyph

		paintStar(g2);

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
		 * logger.finest("~~~~~~~~~~~~~~~~~~ "); radians = Math.PI/4; sineAns =
		 * Math.sin(radians); cosAns = Math.cos(radians); logger.finest("Radians = " +
		 * radians); logger.finest("Sine = " + sineAns); logger.finest("Cosine = " +
		 * cosAns); logger.finest("~~~~~~~~~~~~~~~~~~ "); radians = Math.PI/2;
		 * sineAns = Math.sin(radians); cosAns = Math.cos(radians);
		 * logger.finest("Radians = " + radians); logger.finest("Sine = " +
		 * sineAns); logger.finest("Cosine = " + cosAns);
		 * logger.finest("~~~~~~~~~~~~~~~~~~ "); radians = Math.PI; sineAns =
		 * Math.sin(radians); cosAns = Math.cos(radians); logger.finest("Radians = " +
		 * radians); logger.finest("Sine = " + sineAns); logger.finest("Cosine = " +
		 * cosAns); logger.finest("~~~~~~~~~~~~~~~~~~ "); radians = Math.PI +
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
		projectFigure(this.targetArea);
	}

	public int[] getSpikeLengths() {
		return lengths;
	}

	private void transformPoints() {
		if (zoomForm == null) {
			zoomForm = new AffineTransform();
			logger.finest("null transform in sp.transformPoints");
		}
		int nPts = xPoints.length;
		float[] srcPts = new float[nPts * 2];
		float[] dstPts = new float[nPts * 2];
		for (int i = 0; i < nPts; i++) {
			srcPts[i * 2] = xPoints[i];
			srcPts[i * 2 + 1] = yPoints[i];
		}
		zoomForm.transform(srcPts, 0, dstPts, 0, nPts);
		if (renderedXPoints == null || renderedXPoints.length != nPts) {
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("making new points");
			}
			renderedXPoints = new float[nPts];
			renderedYPoints = new float[nPts];
		}
		for (int i = 0; i < nPts; i++) {
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("calculating points");
			}
			renderedXPoints[i] = dstPts[i * 2];
			renderedYPoints[i] = dstPts[i * 2 + 1]; // +1 for the y
			// points in x,y
			// order
		}

	}

	public float[] getRenderedXPoints() {

		if (renderedXPoints == null) {
			transformPoints();
		}
		return renderedXPoints;
	}

	public float[] getRenderedYPoints() {

		if (renderedYPoints == null) {
			transformPoints();
		}

		return renderedYPoints;
	}

	public void setLocation(Point location) { // when we are a Glyph

		int width = 50; // xxx totally abitrary
		int height = 50;
		int x = location.x - (width / 2);
		int y = location.y - (height / 2);
		Rectangle targetArea = new Rectangle(x, y, width, height);
		setTargetArea(targetArea);
	}

	public Shape getOutline() {
		return paintFigure;
	}

}
