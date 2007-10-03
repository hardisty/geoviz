/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class StarPlotCanvas
 Copyright (c), 2003-5, Frank Hardisty
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: StarPlotCanvas.java,v 1.7 2006/02/17 17:21:23 hardisty Exp $
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
package edu.psu.geovista.geoviz.star;

// import java.util.HashMap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.common.event.DataSetEvent;
import edu.psu.geovista.common.event.DataSetListener;
import edu.psu.geovista.common.event.IndicationEvent;
import edu.psu.geovista.common.event.IndicationListener;
import edu.psu.geovista.common.event.SubspaceEvent;
import edu.psu.geovista.geoviz.spacefill.FillOrder;
import edu.psu.geovista.symbolization.glyph.Glyph;
import edu.psu.geovista.symbolization.glyph.GlyphEvent;
import edu.psu.geovista.symbolization.glyph.GlyphListener;

/**
 * This class keeps track of where the Starplots should go. Companion class
 * StarPlotLayer sets their shapes and renders them.
 * 
 * Responds to and broadcasts DataSetChanged, IndicationChanged etc. events.
 * 
 * @author Frank Hardisty
 * @version $Revision: 1.7 $
 */
public class StarPlotCanvas extends JPanel implements ComponentListener,
		DataSetListener, IndicationListener, MouseListener, MouseMotionListener {
	private transient Image drawingBuff;
	private transient Rectangle[] plotLocations;
	private transient DataSetForApps dataSet;
	private  int indication = -1;
	private transient int plotWidth;
	private transient int plotHeight;
	private transient int nRows;
	private transient int nColumns;
	private transient float penWidth;
	// private transient int[] obsList; //in case we want to subset for some
	// reason, i.e. selection or conditioning
	private transient int[] plotsOrder;// plotsOrder is used only when finding
	// out where
	// each plot is located.
	private transient Color[] starColors;
	final static Logger logger = Logger.getLogger(StarPlotCanvas.class.getName());

	private StarPlotLayer plotLayer;

	public StarPlotCanvas() {
		if (StarPlotCanvas.logger.isLoggable(Level.FINEST)) {
			logger.finest("StarPlotCanvas, in constructor");
		}
		this.setBackground(Color.white);
		this.addComponentListener(this);
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.plotLayer = new StarPlotLayer();

	}

	public void dataSetChanged(DataSetEvent e) {
		plotLayer.dataSetChanged(e);
		this.dataSet = e.getDataSetForApps();
		this.plotLocations = new Rectangle[this.dataSet.getNumObservations()];
		for (int i = 0; i < plotLocations.length; i++) {
			this.plotLocations[i] = new Rectangle();
		}
	    if ( (this.drawingBuff == null) && (this.getWidth() > 0) &&
	            (this.getHeight() > 0)) {
	          this.drawingBuff = this.createImage(this.getWidth(), this.getHeight());
	        }
		this.findPlotOrder(dataSet.getNumericDataAsDouble(0));
		this.findPlotLocations();
		this.paintDrawingBuff();
		this.repaint();
	}

	private void findPlotOrder(double[] data) {
		// this.plotsOrder = new int[dataSet.getNumObservations()];
		plotsOrder = FillOrder.findRankOrder(data);

	}

	public void subspaceChanged(SubspaceEvent e) {
		if (StarPlotCanvas.logger.isLoggable(Level.FINEST)) {
			logger.finest("subspaceChanged in StarPlotCanvas");
		}
		plotLayer.subspaceChanged(e);
		int sortVar = e.getSubspace()[0];
		double[] data = this.dataSet.getNumericDataAsDouble(sortVar);
		this.findPlotOrder(data);
		this.findPlotLocations();
		this.fireGlyphChanged();
		this.paintDrawingBuff();
		repaint();

	}

	public void setOrder(int[] order) {
		this.plotsOrder = order;
		this.findPlotLocations();
		this.paintDrawingBuff();
		this.repaint();
	}

	private static Dimension findNRowsColumns(int nPlots) {
		Dimension dim = null;
		// we would like our plots to be as square as possible.
		// but never mind that for now
		float nRowsDouble = (float) Math.sqrt(nPlots);
		int nRows = Math.round(nRowsDouble);
		int nColumns = nRows;
		if (StarPlotCanvas.logger.isLoggable(Level.FINEST)) {
			logger.finest("StarPlotCanvas nColumns = " + nColumns);
		}
		if (nRows * nColumns < nPlots) {
			nRows++;
		}
		if (nRows * nColumns < nPlots) {
			logger.finest("StarPlotCanvas, that's unpossible!");
			nColumns++;
		}
		dim = new Dimension(nRows, nColumns);
		return dim;
	}

	private void findPlotLocations() {
		Rectangle[] plotLocs = this.plotLocations;
		if (plotLocs == null || this.getWidth() <= 0 || plotsOrder == null) {
			return;
		}
		if (plotLocs.length != this.plotLayer.getObsList().length) {
			return;
		}
		Dimension dim = StarPlotCanvas.findNRowsColumns(plotLocs.length);
		this.nRows = dim.width;
		this.nColumns = dim.height;

		plotWidth = this.getWidth() / nColumns;
		plotHeight = this.getHeight() / nRows;
		int plotMin = plotWidth;
		if (plotHeight < plotWidth) {
			plotMin = plotHeight;
		}
		logger.finest(" plotmin = " + plotMin);
		penWidth = (float) plotMin / 100f;
		if (penWidth < 0f) {
			penWidth = 0f;
		}
		logger.finest(" penwidth = " + penWidth);
		int plotsAssigned = 0;
		int x = 0;
		int y = 0;
		int index = 0;
		for (int row = 0; row < nRows; row++) {
			for (int col = 0; col < nColumns; col++) {

				x = (col * plotWidth);
				y = (row * plotHeight);
				index = this.plotsOrder[plotsAssigned];
				plotLocs[index] = new Rectangle();
				plotLocs[index].setBounds(x, y, plotWidth, plotHeight);

				plotsAssigned++;
				if (plotsAssigned >= plotLocs.length) {
					break;
				}
			}
		}
		this.plotLayer.setPlotLocations(plotLocs);
	}

	/**
	 * Attention all stars! Paint yourselves onto the buffer. This can be an
	 * expensive operation, so this method should be called by a RenderThread.
	 */
	private void paintDrawingBuff() {
		if (this.drawingBuff == null || this.dataSet == null) {
			return;
		}
		Graphics g = this.drawingBuff.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		Color veryLightGray = new Color(220, 220, 220);
		g.setColor(veryLightGray);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		this.paintCircles(g2);
		g2.setStroke(new BasicStroke(penWidth));
		plotLayer.renderStars(g2);
		paintGrid(g2);
	}

	public void paintComponent(Graphics g) {
		//super.paintComponent(g);
		Color veryLightGray = new Color(220, 220, 220);
		g.setColor(veryLightGray);

		if (plotLocations == null || plotLocations[0] == null) {
			return;
		}
		Graphics2D g2 = (Graphics2D) g;
		// Draw buff
		if (this.drawingBuff != null) {
			g2.drawImage(this.drawingBuff, null, this);

		}// Just draw background
		else {
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}

		g2.setColor(StarPlotRenderer.defaultIndicationColor);
		plotLayer.renderStar(g2, this.indication);

	}

	// paint grid and background circles
	private void paintGrid(Graphics2D g2) {

		int startX;
		int endX;
		int startY;
		int endY;
		startX = 0;
		endX = this.getWidth();
		g2.setColor(StarPlotRenderer.defaultOutlineColor);
		for (int row = 0; row < nRows; row++) {
			startY = row * this.plotHeight;
			endY = startY;
			g2.drawLine(startX, startY, endX, endY);
		}
		startY = 0;
		endY = this.getHeight();
		for (int col = 0; col < nColumns; col++) {
			startX = col * this.plotWidth;
			endX = startX;
			g2.drawLine(startX, startY, endX, endY);
		}

	}

	private void paintCircles(Graphics2D g2) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Rectangle[] plotLocs = this.plotLocations;

		g2.setColor(Color.white);
		int x = 0;
		int y = 0;
		int width = this.plotWidth;
		int height = this.plotHeight;
		for (int plot = 0; plot < plotLocs.length; plot++) {
			if (plotLocs[plot] == null) {
				return;
			}
			x = plotLocs[plot].x;
			y = plotLocs[plot].y;
			width = plotLocs[plot].width;
			height = plotLocs[plot].height;
			if (this.plotWidth > this.plotHeight) {
				x = x + ((width - height) / 2);
				width = height;

			} else if (plotHeight > plotWidth) {
				y = y + ((height - width) / 2);
				height = width;
			}

			g2.fillOval(x, y, width, height);

		}
	}

	// start component event handling
	// note: this class only listens to itself
	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {

	}

	public void componentResized(ComponentEvent e) {

		this.findPlotLocations();
		if (this.getWidth() > 0 && (this.getHeight() > 0)) {
			this.drawingBuff = this.createImage(this.getWidth(), this
					.getHeight());
		}
		this.paintDrawingBuff();
		this.repaint();

	}

	public void componentShown(ComponentEvent e) {

	}

	// end component handling

	/***************************************************************************
	 * Interface methods for mouse events *
	 **************************************************************************/
	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
		this.fireIndicationChanged(-1); // clear indication
		this.setIndication(-1);
	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {
		if (this.plotWidth == 0) {
			return;
		}

		int row = (int) ((float) e.getY() / (float) this.plotHeight);

		int column = e.getX() / this.plotWidth;

		int plot = 0;
		plot = (row * nColumns) + column;

		if (plot >= this.plotLocations.length) {
			return;
		}
		plot = this.plotsOrder[plot];
		if (plot < this.plotLocations.length && plot != this.indication) {
			this.setIndication(plot);
			this.fireIndicationChanged(plot);

		}

	}

	// end mouse events
	public String[] getVarNames() {
		return this.plotLayer.getVarNames();
	}

	public double[] getValues(int obs) {
		double[] vals = this.plotLayer.getValues(obs);
		if (vals == null) {
			return null;
		}
		return vals;
	}

	public int[] getSpikeLengths(int obs) {
		return this.plotLayer.getSpikeLengths(obs);
	}

	public void indicationChanged(IndicationEvent e) {

		this.setIndication(e.getIndication());

	}

	public Glyph[] findGlyphs() {
		return plotLayer.findGlyphs();
	}

	/**
	 * adds an GlyphListener to the component
	 */
	public void addGlyphListener(GlyphListener l) {
		if (StarPlotCanvas.logger.isLoggable(Level.FINEST)) {
			logger.finest(this.getClass().getName()
					+ " adding glyph listener");
		}
		listenerList.add(GlyphListener.class, l);
	}

	/**
	 * removes an GlyphListener from the component
	 */
	public void removeGlyphListener(GlyphListener l) {
		listenerList.remove(GlyphListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	public void fireGlyphChanged() {
		if (StarPlotCanvas.logger.isLoggable(Level.FINEST)) {
			logger.finest("firing glyph changed");
		}
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		GlyphEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == GlyphListener.class) {
				// Lazily create the event:
				if (e == null) {
					Glyph[] glys = this.findGlyphs();
					e = new GlyphEvent(this, glys);
				}

				((GlyphListener) listeners[i + 1]).glyphChanged(e);
			}
		}
	}

	/**
	 * adds an IndicationListener to the component
	 */
	public void addIndicationListener(IndicationListener l) {
		listenerList.add(IndicationListener.class, l);
	}

	/**
	 * removes an IndicationListener from the component
	 */
	public void removeIndicationListener(IndicationListener l) {
		listenerList.remove(IndicationListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	public void fireIndicationChanged(int indication) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		IndicationEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == IndicationListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new IndicationEvent(this, indication);
				}

				((IndicationListener) listeners[i + 1]).indicationChanged(e);
			}
		}
	}

	public int getIndication() {
		return indication;
	}

	public void setIndication(int newIndication) {
		this.plotLayer.setIndication(newIndication);
		this.indication = newIndication;
		this.repaint();
	}

	public String getObservationName(int index) {
		if (this.dataSet == null) {
			return "";
		}
		if (this.dataSet.getObservationNames() == null) {
			return String.valueOf(index);
		}
		return this.dataSet.getObservationNames()[index];
	}

	public void setStarFillColors(Color[] starColors) {
		this.applyStarFillColors(starColors);
		this.paintDrawingBuff();
		this.repaint();
	}

	private void applyStarFillColors(Color[] starColors) {
		this.starColors = starColors;
		this.plotLayer.applyStarFillColors(starColors);

	}

	public Color getStarFillColor(int ind) {
		if (this.starColors == null) {
			return null;
		}
		return this.starColors[ind];

	}

	/*
	 * Sets the visible set of starplots. the array of ints is an array of which
	 * starplots are to be visible. Null arrays or arrays of length zero are
	 * ignored.
	 * 
	 */
	public void setObsList(int[] obsList) { // for selections etc.
		this.plotLayer.setObsList(obsList);
		this.paintDrawingBuff();
		this.repaint();

	}

	public DataSetForApps getDataSet() {
		return dataSet;
	}
} // end class
