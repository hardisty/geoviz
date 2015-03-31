/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */
package geovista.geoviz.star;

import geovista.common.data.DataSetForApps;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.IndicationEvent;
import geovista.common.event.IndicationListener;
import geovista.common.event.SubspaceEvent;
import geovista.geoviz.spacefill.FillOrder;
import geovista.symbolization.glyph.Glyph;
import geovista.symbolization.glyph.GlyphEvent;
import geovista.symbolization.glyph.GlyphListener;

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

/**
 * This class keeps track of where the Starplots should go. Companion class
 * StarPlotLayer sets their shapes and renders them.
 * 
 * Responds to and broadcasts DataSetChanged, IndicationChanged etc. events.
 * 
 * @author Frank Hardisty
 * 
 */
public class StarPlotCanvas extends JPanel implements ComponentListener,
	DataSetListener, IndicationListener, MouseListener, MouseMotionListener {
    private transient Image drawingBuff;
    private transient Rectangle[] plotLocations;
    private transient DataSetForApps dataSet;
    private int indication = -1;
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
    final static Logger logger = Logger.getLogger(StarPlotCanvas.class
	    .getName());

    private final StarPlotLayer plotLayer;
    private int varIndex;
    public boolean drawGrid;
    public Color backgroundColor;

    public StarPlotCanvas() {
	if (logger.isLoggable(Level.FINEST)) {
	    logger.finest("StarPlotCanvas, in constructor");
	}
	setBackground(Color.white);
	addComponentListener(this);
	addMouseMotionListener(this);
	addMouseListener(this);
	plotLayer = new StarPlotLayer();
	this.drawGrid = true;
	this.backgroundColor = new Color(220, 220, 220);// very light grey

    }

    public void dataSetChanged(DataSetEvent e) {
	plotLayer.dataSetChanged(e);
	dataSet = e.getDataSetForApps();
	plotLocations = new Rectangle[dataSet.getNumObservations()];
	for (int i = 0; i < plotLocations.length; i++) {
	    plotLocations[i] = new Rectangle();
	}
	if ((drawingBuff == null) && (getWidth() > 0) && (getHeight() > 0)) {
	    drawingBuff = this.createImage(getWidth(), getHeight());
	}
	findPlotOrder(dataSet.getNumericDataAsDouble(0));
	findPlotLocations();
	paintDrawingBuff();
	this.repaint();
    }

    private void findPlotOrder(double[] data) {
	// this.plotsOrder = new int[dataSet.getNumObservations()];
	plotsOrder = FillOrder.findRankOrder(data);

    }

    public void subspaceChanged(SubspaceEvent e) {
	if (logger.isLoggable(Level.FINEST)) {
	    logger.finest("subspaceChanged in StarPlotCanvas");
	}
	plotLayer.subspaceChanged(e);
	int sortVar = e.getSubspace()[0];
	double[] data = dataSet.getNumericDataAsDouble(sortVar);
	findPlotOrder(data);
	findPlotLocations();
	fireGlyphChanged();
	paintDrawingBuff();
	repaint();

    }

    public void setOrder(int[] order) {
	plotsOrder = order;
	findPlotLocations();
	paintDrawingBuff();
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
	    logger.info("StarPlotCanvas, that's unpossible!");
	    nColumns++;
	}
	dim = new Dimension(nRows, nColumns);
	return dim;
    }

    private void findPlotLocations() {
	Rectangle[] plotLocs = plotLocations;
	if (plotLocs == null || getWidth() <= 0 || plotsOrder == null
		|| plotsOrder.length != plotLocs.length) {
	    return;
	}
	if (plotLocs.length != plotLayer.getObsList().length) {
	    return;
	}
	Dimension dim = StarPlotCanvas.findNRowsColumns(plotLocs.length);
	nRows = dim.width;
	nColumns = dim.height;

	plotWidth = getWidth() / nColumns;
	plotHeight = getHeight() / nRows;
	int plotMin = plotWidth;
	if (plotHeight < plotWidth) {
	    plotMin = plotHeight;
	}
	if (logger.isLoggable(Level.FINEST)) {
	    logger.finest(" plotmin = " + plotMin);
	}
	penWidth = plotMin / 100f;
	if (penWidth < 0f) {
	    penWidth = 0f;
	}
	if (logger.isLoggable(Level.FINEST)) {
	    logger.finest(" penwidth = " + penWidth);
	}
	int plotsAssigned = 0;
	int x = 0;
	int y = 0;
	int index = 0;
	for (int row = 0; row < nRows; row++) {
	    for (int col = 0; col < nColumns; col++) {

		x = (col * plotWidth);
		y = (row * plotHeight);
		index = plotsOrder[plotsAssigned];
		plotLocs[index] = new Rectangle();
		plotLocs[index].setBounds(x, y, plotWidth, plotHeight);

		plotsAssigned++;
		if (plotsAssigned >= plotLocs.length) {
		    break;
		}
	    }
	}
	plotLayer.setPlotLocations(plotLocs);
    }

    /**
     * Attention all stars! Paint yourselves onto the buffer. This can be an
     * expensive operation, so this method should be called by a RenderThread.
     */
    private void paintDrawingBuff() {
	if (drawingBuff == null || dataSet == null) {
	    return;
	}
	Graphics g = drawingBuff.getGraphics();
	Graphics2D g2 = (Graphics2D) g;

	g.setColor(this.backgroundColor);
	g.fillRect(0, 0, getWidth(), getHeight());

	paintCircles(g2);
	g2.setStroke(new BasicStroke(penWidth));
	plotLayer.renderStars(g2);
	if (this.drawGrid) {
	    paintGrid(g2);
	}
    }

    @Override
    public void paintComponent(Graphics g) {

	g.setColor(this.backgroundColor);

	if (plotLocations == null || plotLocations[0] == null) {
	    return;
	}
	Graphics2D g2 = (Graphics2D) g;
	// Draw buff
	if (drawingBuff != null) {
	    g2.drawImage(drawingBuff, null, this);

	}// Just draw background
	else {
	    g.fillRect(0, 0, getWidth(), getHeight());

	}

	g2.setColor(StarPlotRenderer.defaultIndicationColor);
	if (indication >= 0) {
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		    RenderingHints.VALUE_ANTIALIAS_ON);
	    Rectangle indRect = plotLocations[indication];
	    g2.setColor(Color.black);
	    g.fillRect(indRect.x, indRect.y, indRect.width, indRect.height);
	    g2.setColor(StarPlotRenderer.defaultIndicationColor);
	    plotCircle(g2, indRect);

	}

	plotLayer.renderStar(g2, indication);

    }

    // paint grid and background circles
    private void paintGrid(Graphics2D g2) {

	int startX;
	int endX;
	int startY;
	int endY;
	startX = 0;
	endX = getWidth();
	g2.setColor(StarPlotRenderer.defaultOutlineColor);
	for (int row = 0; row < nRows; row++) {
	    startY = row * plotHeight;
	    endY = startY;
	    g2.drawLine(startX, startY, endX, endY);
	}
	startY = 0;
	endY = getHeight();
	for (int col = 0; col < nColumns; col++) {
	    startX = col * plotWidth;
	    endX = startX;
	    g2.drawLine(startX, startY, endX, endY);
	}

    }

    private void paintCircles(Graphics2D g2) {
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	Rectangle[] plotLocs = plotLocations;

	g2.setColor(Color.white);

	for (Rectangle element : plotLocs) {
	    plotCircle(g2, element);

	}
    }

    private void plotCircle(Graphics2D g2, Rectangle element) {
	int x;
	int y;
	int width;
	int height;
	if (element == null) {
	    return;
	}
	x = element.x;
	y = element.y;
	width = element.width;
	height = element.height;
	if (plotWidth > plotHeight) {
	    x = x + ((width - height) / 2);
	    width = height;

	} else if (plotHeight > plotWidth) {
	    y = y + ((height - width) / 2);
	    height = width;
	}

	g2.fillOval(x, y, width, height);
    }

    // start component event handling
    // note: this class only listens to itself
    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {

    }

    public void componentResized(ComponentEvent e) {

	if (getWidth() > 0 && (getHeight() > 0)) {
	    drawingBuff = this.createImage(getWidth(), getHeight());
	    findPlotLocations();
	}
	paintDrawingBuff();
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
	fireIndicationChanged(-1); // clear indication
	setIndication(-1);
    }

    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {
	if (plotWidth == 0) {
	    return;
	}

	int row = (int) ((float) e.getY() / (float) plotHeight);

	int column = e.getX() / plotWidth;

	int plot = 0;
	plot = (row * nColumns) + column;

	if (plot >= plotLocations.length) {
	    return;
	}
	plot = plotsOrder[plot];
	if (plot < plotLocations.length && plot != indication) {
	    setIndication(plot);
	    fireIndicationChanged(plot);

	}

    }

    // end mouse events
    public String[] getVarNames() {
	return plotLayer.getVarNames();
    }

    public double[] getValues(int obs) {
	double[] vals = plotLayer.getValues(obs);
	if (vals == null) {
	    return null;
	}
	return vals;
    }

    public int[] getSpikeLengths(int obs) {
	return plotLayer.getSpikeLengths(obs);
    }

    public void indicationChanged(IndicationEvent e) {
	int ind = e.getIndication();
	if (ind > dataSet.getNumObservations()) {
	    logger.severe("got indication greater than data set size, ind = "
		    + ind);
	    // Thread.dumpStack();
	    return;
	}
	setIndication(e.getIndication());

    }

    public Glyph[] findGlyphs() {
	return plotLayer.findGlyphs();
    }

    /**
     * adds an GlyphListener to the component
     */
    public void addGlyphListener(GlyphListener l) {
	if (StarPlotCanvas.logger.isLoggable(Level.FINEST)) {
	    logger.finest(this.getClass().getName() + " adding glyph listener");
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
		    Glyph[] glys = findGlyphs();
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
	plotLayer.setIndication(newIndication);
	indication = newIndication;
	this.repaint();
    }

    public String getObservationName(int index) {
	if (dataSet == null) {
	    return "";
	}
	if (dataSet.getObservationNames() == null) {
	    return String.valueOf(index);
	}
	return dataSet.getObservationNames()[index];
    }

    public void setStarFillColors(Color[] starColors) {
	applyStarFillColors(starColors);
	paintDrawingBuff();
	this.repaint();
    }

    private void applyStarFillColors(Color[] starColors) {
	this.starColors = starColors;
	plotLayer.applyStarFillColors(starColors);

    }

    public Color getStarFillColor(int ind) {
	if (starColors == null || starColors.length <= ind) {
	    return null;
	}
	return starColors[ind];

    }

    /*
     * Sets the visible set of starplots. the array of ints is an array of which
     * starplots are to be visible. Null arrays or arrays of length zero are
     * ignored.
     */
    public void setObsList(int[] obsList) { // for selections etc.
	plotLayer.setObsList(obsList);
	paintDrawingBuff();
	this.repaint();

    }

    public DataSetForApps getDataSet() {
	return dataSet;
    }

    public StarPlotLayer getPlotLayer() {
	return plotLayer;
    }

    public void setCurrentVar(int varIndex) {
	this.varIndex = varIndex;

    }

    public int[] findNeighbors(int obs) {
	int[] intArray = {};
	double[] currVar = (double[]) dataSet.getNamedArrays()[varIndex];
	double val = currVar[obs];
	double upperNeighbor = Double.MAX_VALUE;
	double lowerNeighbor = Double.MAX_VALUE * -1;
	double upGap = upperNeighbor - val;
	double downGap = val - lowerNeighbor;

	for (int i = 0; i < currVar.length; i++) {
	    if (i != obs) {
		double newVal = currVar[i];
		if (newVal > val) {
		    double newUpGap = newVal - val;
		    if (newUpGap > upGap) {
			upperNeighbor = newVal;
			upGap = newUpGap;
		    }
		} else if (newVal < val) {
		    double newDownGap = val - newVal;
		    if (newDownGap > downGap) {
			lowerNeighbor = newVal;
			downGap = newDownGap;
		    }
		} else if (newVal == val) {

		}
	    }
	}
	return intArray;
    }
}
