/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class MapCanvas
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: MapCanvas.java,v 1.29 2005/08/19 19:17:32 hardisty Exp $
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

package edu.psu.geovista.geoviz.map;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import com.jhlabs.image.BoxBlurFilter;

import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.common.event.DataSetEvent;
import edu.psu.geovista.common.event.DataSetListener;
import edu.psu.geovista.common.event.IndicationEvent;
import edu.psu.geovista.common.event.IndicationListener;
import edu.psu.geovista.common.event.SelectionEvent;
import edu.psu.geovista.common.event.SelectionListener;
import edu.psu.geovista.common.event.SpatialExtentEvent;
import edu.psu.geovista.common.event.SpatialExtentListener;
import edu.psu.geovista.common.ui.ExcentricLabelClient;
import edu.psu.geovista.common.ui.ExcentricLabels;
import edu.psu.geovista.common.ui.Fisheyes;
import edu.psu.geovista.data.transform.ShapeAffineTransform;
import edu.psu.geovista.data.transform.ShapeTransformer;
import edu.psu.geovista.geoviz.condition.ConditionManager;
import edu.psu.geovista.symbolization.AffineTransformModifier;
import edu.psu.geovista.symbolization.BivariateColorSymbolClassification;
import edu.psu.geovista.symbolization.BivariateColorSymbolClassificationSimple;
import edu.psu.geovista.symbolization.glyph.Glyph;
import edu.psu.geovista.symbolization.glyph.GlyphEvent;
import edu.psu.geovista.ui.cursor.GeoCursors;

/**
 * This class handles the rendering of layer-independent objects like tooltips
 * and the image used for buffering, and manages the layers, which render
 * themselves to the image.
 * 
 * This class also transforms spatial data into user space.
 * 
 * This class is intended to be used inside other components like the GeoMap or
 * the PlotMatrix.
 */
public class MapCanvas extends JPanel implements ComponentListener,
		ActionListener, MouseListener, MouseMotionListener, SelectionListener,
		IndicationListener, DataSetListener, SpatialExtentListener,
		ExcentricLabelClient {
	public final static int MODE_SELECT = 0; // default mode
	public static final int MODE_ZOOM_IN = 1;
	public static final int MODE_ZOOM_OUT = 2;
	public static final int MODE_PAN = 3;
	public static final int MODE_EXCENTRIC = 4;
	public static final int MODE_FISHEYE = 5;
	public static final int MODE_MAGNIFYING = 6;
	private int mouseX1;
	transient private int mouseX2;
	transient private int mouseY1;
	transient private int mouseY2;
	private transient DataSetForApps dataSet;

	private Vector shapeLayers;
	private int indication = Integer.MIN_VALUE;
	private transient int activeLayer;
	private transient ShapeTransformer transformer;

	private transient int[] selectedObservations;
	private transient Color[] objectColors;
	private transient String[] variableNames;
	protected int currColorColumnX = -1; // jin: the index of
													// variable for 1st visual
													// classification
	protected int currColorColumnY = -1; // jin: the index of
													// variable for 2nd visual
													// classification

	// Colors
	protected transient double[] dataColorX;
	protected transient double[] dataColorY;

	protected transient BivariateColorSymbolClassificationSimple bivarColorClasser = new BivariateColorSymbolClassificationSimple();

	private transient Rectangle2D savedSrc = null;
	private transient Image drawingBuff;
	private AffineTransform imagePanningXForm = new AffineTransform();
	protected transient ExcentricLabels exLabels;
	private String tipText = "";
	private transient Font tipFont;
	private boolean drawTip = true;
	private transient boolean drawBox = false;
	private transient int mode;
	private GeoCursors cursors = new GeoCursors();
	private float[] dash = new float[] { 5f, 7f, 5f };
	private transient BasicStroke dashStroke = new BasicStroke((float) 2.0,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, (float) 10.0, dash,
			0);
	private transient BasicStroke solidStroke = new BasicStroke(2f);
	protected transient boolean autofit = false;
	protected Fisheyes fisheyes;
	final static Logger logger = Logger.getLogger(MapCanvas.class.getName());

	public MapCanvas() {
		super();
		this.setPreferredSize(new Dimension(300, 300));
		shapeLayers = new Vector();
		this.addComponentListener(this);
		this.transformer = new ShapeAffineTransform();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		mouseX1 = -2;
		this.drawTip = true;
		this.mode = MapCanvas.MODE_SELECT;

		int greyAmt = 255;
		Color bgColor = new Color(greyAmt, greyAmt, greyAmt);
		this.setBackground(bgColor);

		// this.exLabels = new ExcentricLabels();
		// this.fisheyes = new Fisheyes();
		// fisheyes.setLensType(Fisheyes.LENS_GAUSSIAN);
	}

	/***************************************************************************
	 * Set symbols, which have been configured according to data values.
	 */
	public void setGlyphs(Glyph[] sbs) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("MapCanvas, got glyphs at setGlyphs");
		}
		LayerShape ls = (LayerShape) this.shapeLayers
				.elementAt(this.activeLayer);
		ls.setGlyphs(sbs);

		this.zoomFullExtent();// XXX should not have to do this line
		this.paintDrawingBuff();
		this.repaint();
	}

	public void setTextures(TexturePaint[] textures) {
		LayerShape ls = (LayerShape) this.shapeLayers
				.elementAt(this.activeLayer);
		ls.setTextures(textures);
		this.paintDrawingBuff();
		this.repaint();

	}

	/***************************************************************************
	 * Set colors for observations. Normally this method is called by other
	 * components that classify or cluster data observations and assign colors.
	 */
	public void setObservationColors(Color[] obsColors) {
		if (this.shapeLayers.size() < 1 || this.dataSet == null) {
			return;
		}

		try {
			if (this.dataSet.getNumObservations() != obsColors.length) {
				throw new Exception(
						"###ERROR: obsColors.length != number of observations!!!"
								+ this.dataSet.getNumObservations() + " != "
								+ obsColors.length);
			}
		} catch (Exception ee) {
			ee.printStackTrace();
			return;
		}

		this.objectColors = obsColors;

		LayerShape ls = (LayerShape) this.shapeLayers.get(this.activeLayer);
		ls.setObjectColors(this.objectColors);

		paintDrawingBuff();
		this.repaint();
	}

	private void sendSpatialDataToLayer(LayerShape l, Shape[] spatialData) {
		l.setSpatialData(spatialData);
		l.setParentSize(this.getHeight(), this.getWidth());
	}

	/**
	 * Given a region (defined by a rect), transform all current layers into
	 * that region, based on maximising the visibilty of the active layer. It is
	 * assumed that each layer has it's appropriate original spatial data set.
	 */
	public void findAndSetUserSpaceSpatialData(Rectangle2D dest,
			boolean useSavedSrc) {
		if (shapeLayers == null) {
			return;
		}
		if (shapeLayers.size() == 0) {
			return;
		}
		LayerShape currLayer = (LayerShape) this.shapeLayers
				.get(this.activeLayer);
		Shape[] originalData = currLayer.getOriginalSpatialData();

		// two cases for finding src rectangle: second or later set of shapes,
		// or first.
		Rectangle2D src = null;

		if (useSavedSrc && (savedSrc != null)) {
			src = this.savedSrc;
		} else {
			src = findFullExtentRect(originalData);
			this.savedSrc = src;
		}

		AffineTransform xForm = AffineTransformModifier
				.makeGeogAffineTransform(src, dest, true, true);
		for (Enumeration e = this.shapeLayers.elements(); e.hasMoreElements();) {
			LayerShape shapeLayer = (LayerShape) e.nextElement();
			Shape[] originalShapes = shapeLayer.getOriginalSpatialData();
			Shape[] returnShapes = originalShapes;//just for now, to make findbugs happy
			this.transformer.setXForm(xForm);
			if (shapeLayer instanceof LayerPolygon) {
				returnShapes = this.transformer.makeTransformedShapes(
						originalShapes, xForm);
			} else if (shapeLayer instanceof LayerLine) {
				returnShapes = this.transformer.makeTransformedShapes(
						originalShapes, xForm);
			} else if (shapeLayer instanceof LayerPoint) {
				LayerPoint lp = (LayerPoint) shapeLayer;
				Point2D[] userSpacePoints = this.transformer
						.makeTransformedPoints(dataSet.getPoint2DData(), xForm);
				Shape[] circleShapes = lp.findShapesForPoints(userSpacePoints);
				returnShapes = circleShapes;

			} else {
				System.out
						.println("Unsupported shape type encountered, shape type = "
								+ shapeLayer.getClass().getName());
			}
			this.sendSpatialDataToLayer(shapeLayer, returnShapes);

		}
	}

	private Rectangle2D findFullExtentRect(Shape[] someShapes) {
		Shape[] someData = someShapes;
		double xMax;
		double xMin;
		double yMax;
		double yMin;
		xMax = Double.MAX_VALUE * -1;
		xMin = Double.MAX_VALUE;
		yMax = Double.MAX_VALUE * -1;
		yMin = Double.MAX_VALUE;

		for (int i = 0; i < someData.length; i++) {
			Rectangle2D bounding = someData[i].getBounds2D();

			if (bounding.getMaxX() > xMax) {
				xMax = bounding.getMaxX();
			}

			if (bounding.getMinX() < xMin) {
				xMin = bounding.getMinX();
			}

			if (bounding.getMaxY() > yMax) {
				yMax = bounding.getMaxY();
			}

			if (bounding.getMinY() < yMin) {
				yMin = bounding.getMinY();
			}
		}

		Rectangle2D src = new Rectangle2D.Double(xMin, yMin, xMax - xMin, yMax
				- yMin);
		this.savedSrc = src;

		// this.spatialDataFullExtent = src;
		return src;
	}

	// public void findFullExtentSpatialData(Shape[] originalShapes,
	// boolean useSavedSrc) {
	// Rectangle2D dest = new Rectangle2D.Float(0, 0, this.getWidth(),
	// this.getHeight());
	// this.findAndSetUserSpaceSpatialData(dest,
	// useSavedSrc);
	//
	//
	// }

	private void zoomNewExtent(Rectangle2D dest) {

		this.findAndSetUserSpaceSpatialData(dest, false);

		paintDrawingBuff();
		this.repaint();
	}

	/*
	 * This resets all shapes, thus it might be expensive.
	 * 
	 */
	public void zoomFullExtent() {
		Rectangle2D dest = new Rectangle2D.Float(0, 0, this.getWidth(), this
				.getHeight());
		this.findAndSetUserSpaceSpatialData(dest, false);

		paintDrawingBuff();
		this.repaint();
	}

	public void spatialExtentChanged(SpatialExtentEvent e) {
		Rectangle2D rect = e.getSpatialExtent();
		this.zoomNewExtent(rect);
	}

	public void zoomIn(int x1, int x2, int y1, int y2) {
		if (x1 > x2) {
			int temp = x1;
			x1 = x2;
			x2 = temp;
		}

		if (y1 > y2) {
			int temp = y1;
			y1 = y2;
			y2 = temp;
		}

		int diffX = x2 - x1;
		int diffY = y2 - y1;
		int boxTolerance = 10;

		if ((diffX < boxTolerance) && (diffY < boxTolerance)) { // accidental
																// box, should
																// have been a
																// click
			x2 = x1;
			y2 = y1;
		}

		if ((x1 == x2) && (y1 == y2)) {
			// single click, we want to zoom in a bit with the click as the
			// center
			float scaleFactor = 0.45f;
			int center = x1;
			x1 = center - (int) ((float) this.getWidth() * scaleFactor);
			x2 = center + (int) ((float) this.getWidth() * scaleFactor);
			center = y1;
			y1 = center - (int) ((float) this.getHeight() * scaleFactor);
			y2 = center + (int) ((float) this.getHeight() * scaleFactor);
		}

		Rectangle2D src = new Rectangle2D.Float(x1, y1, x2 - x1, y2 - y1);
		Rectangle2D dest = new Rectangle2D.Float(0, 0, this.getWidth(), this
				.getHeight());
		this.transformCurrShapesLayers(src, dest);
	}

	public void zoomOut(int x1, int x2, int y1, int y2) {
		if (x1 > x2) {
			int temp = x1;
			x1 = x2;
			x2 = temp;
		}

		if (y1 > y2) {
			int temp = y1;
			y1 = y2;
			y2 = temp;
		}

		Rectangle2D src = new Rectangle2D.Float(x1, y1, x2 - x1, y2 - y1);
		Rectangle2D dest = new Rectangle2D.Float(0, 0, this.getWidth(), this
				.getHeight());
		this.transformCurrShapesLayers(dest, src);
	}

	private void transformCurrShapesLayers(Rectangle2D src, Rectangle2D dest) {
		AffineTransform xForm = AffineTransformModifier
				.makeGeogAffineTransform(src, dest, false, false);
		xForm.concatenate(this.transformer.getXForm());
		this.transformer.setXForm(xForm);
		for (Enumeration e = this.shapeLayers.elements(); e.hasMoreElements();) {
			LayerShape ls = (LayerShape) e.nextElement();
			// start print centroids
			logger.finest("Centroids:");
			 for (int i = 0; i < this.dataSet.getNumObservations(); i++){
			Point p = ls.findCentroid(i);
			logger.finest(p.x + ","+p.y);
			 }
			// end print centroids

			Shape[] preTransformShapes = ls.getOriginalSpatialData();
			Shape[] returnShapes = preTransformShapes;//just for now, to make findbugs happy
			if (ls instanceof LayerPolygon || ls instanceof LayerLine) {

				returnShapes = this.transformer.makeTransformedShapes(
						preTransformShapes, xForm);
			} else if (ls instanceof LayerPoint) {
				LayerPoint lp = (LayerPoint) ls;
				// XXX todo
				// frank -- extra work (points -> shapes -> points -> shapes)
				// here should be avoided, maybe by setting transformed points?
				Point2D[] preTransformPoints = lp.getOriginalPoints();

				Point2D[] userSpacePoints = this.transformer
						.makeTransformedPoints(preTransformPoints, xForm);
				Shape[] circleShapes = lp.findShapesForPoints(userSpacePoints);
				returnShapes = circleShapes;

			} else {
				System.err
						.println("Unsupported shape type encountered, shape type = "
								+ ls.getClass().getName());
			}
			this.sendSpatialDataToLayer(ls, returnShapes);
		}

		paintDrawingBuff();
		this.repaint();

		LayerShape activeLayer = (LayerShape) this.shapeLayers
				.get(this.activeLayer);
		Rectangle2D rect = this
				.findFullExtentRect(activeLayer.getSpatialData());
		this.fireSpatialExtentChanged(rect);

	}

	public void pan(int x1, int x2, int y1, int y2) {
		int xDiff = x2 - x1;
		int yDiff = y2 - y1;

		Rectangle2D dest = new Rectangle2D.Float(0 + xDiff, 0 + yDiff, this
				.getWidth(), this.getHeight());
		Rectangle2D src = new Rectangle2D.Float(0, 0, this.getWidth(), this
				.getHeight());
		this.transformCurrShapesLayers(src, dest);

	}

	public void panBuff(int x1, int x2, int y1, int y2) {
		int xDiff = x2 - x1;
		int yDiff = y2 - y1;

		// AffineTransform panningXForm = new AffineTransform();
		imagePanningXForm = AffineTransform.getTranslateInstance(xDiff, yDiff);
	}

	protected void sendColorsToLayers(int numObs) {
		if (this.shapeLayers.size() < 1) {
			return;
		}

		if (this.dataColorX == null) {
			return;
		}

		if (this.dataColorY == null) {
			return;
		}

		if ((this.objectColors == null) || (this.objectColors.length != numObs)) {
			this.objectColors = new Color[numObs];
		}

		this.objectColors = this.bivarColorClasser.symbolize(this.dataColorX,
				this.dataColorY);

		LayerShape ls = (LayerShape) this.shapeLayers.get(this.activeLayer);
		ls.setObjectColors(this.objectColors);
		paintDrawingBuff();
		this.repaint();
	} // end method

	private void makeToolTip(int arrayIndex) {
		if (arrayIndex < 0 || this.currColorColumnX < 0) {
			tipText = "";
		} else if (this.currColorColumnX == this.currColorColumnY) {
			String xVal = String.valueOf(dataSet.getNumericValueAsDouble(
					this.currColorColumnX, arrayIndex));
			String s = "";
			String[] observationNames = this.dataSet.getObservationNames();

			if (observationNames != null) {
				s = s + "Name = " + observationNames[arrayIndex] + "\n";
			}

			s = s + variableNames[currColorColumnX] + " = " + xVal;

			this.tipText = s;
		} else if ((this.currColorColumnX >= 0) && (this.currColorColumnY >= 0)) { // jin
																					// fix
																					// bug:
																					// replace
																					// >
																					// with
																					// >=
																					// to
																					// fix
																					// bug
																					// that
																					// 1st
																					// variable
																					// get
																					// not
																					// indication.
			// setting multi-line tool tip
			// b.setToolTipText("<html>ToolTip : 1st Line<br>2nd Line<br> 3rd
			// Line </html>");
			String xVal = String.valueOf(dataSet.getNumericValueAsDouble(
					this.currColorColumnX, arrayIndex));
			String yVal = String.valueOf(dataSet.getNumericValueAsDouble(
					this.currColorColumnY, arrayIndex));
			String s = "";
			String[] observationNames = this.dataSet.getObservationNames();

			if (observationNames != null) {
				s = s + "Name = " + observationNames[arrayIndex] + "\n";
			}

			/*
			 * s = s + variableNames[currColorColumnX] + " = " + xVal + "\n" +
			 * variableNames[currColorColumnY] + " = " + yVal;
			 */
			s = s + variableNames[currColorColumnY] + " = " + yVal + "\n" + // jin
																			// fix
																			// minor
																			// bug:
																			// make
																			// it
																			// match
																			// to
																			// the
																			// corresponding
																			// comboboxes
																			// in
																			// viusalclassifier
					variableNames[currColorColumnX] + " = " + xVal;

			this.tipText = s;
		} // if
	}

	// start component event handling
	// note: this class only listens to itself
	public void componentHidden(ComponentEvent e) {

	}

	protected void tickleColors() {
		// egregious hack
		this.setCurrColorColumnX(this.currColorColumnX);
		this.setCurrColorColumnY(this.currColorColumnY);
	}

	public void componentShown(ComponentEvent e) {
		this.componentResized(e);
		logger.finest("showing component");

		// LayerShape aLayer = (LayerShape)
		// this.shapeLayers.get(this.activeLayer);
		// if (aLayer.colorsRecieved == false){
		// this.sendColorsToLayers(this.dataColorX.length);
		//
		// }
	}

	public void componentMoved(ComponentEvent e) {

	}

	public void componentResized(ComponentEvent e) {
		if ((this.shapeLayers.size() > 0) && (this.getWidth() > 0)
				&& (this.getHeight() > 0)) {
			this.drawingBuff = this.createImage(this.getWidth(), this
					.getHeight());

			for (int i = 0; i < this.shapeLayers.size(); i++) {
				LayerShape ls = (LayerShape) this.shapeLayers.get(i);
				ls.setParentSize(this.getHeight(), this.getWidth());
			} // next layer

			if (this.autofit) {
				this.zoomFullExtent();
			}

			if (this.fisheyes != null) {
				float width = (float) this.getWidth();
				this.fisheyes.setLensRadius(width / 5f);

				logger.finest("lens radius = "+  this.fisheyes.getLensRadius());
			}
			// hack
			// this.sendColorsToLayers(this.dataColorX.length);
			if (activeLayer < shapeLayers.size()) { // Jin: avoid
													// ArrayIndexOutOfBoundsException:
													// Array index out of range:
													// 0
				LayerShape ls = (LayerShape) this.shapeLayers
						.get(this.activeLayer);
				ls.setObjectColors(this.objectColors);
				// previous line results in sending colors to layers
				paintDrawingBuff();
			}
			// this.repaint();
		} // end if layers exist and size is > 0
		logger.finest("resizing component");
	}

	// end component event handling
	// start accessors
	public void setMode(int mode) {
		if (mode == MapCanvas.MODE_SELECT) {
			this.fisheyes = null;
			this.exLabels = null;
			this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		} else if (mode == MapCanvas.MODE_PAN) {
			this.setCursor(cursors.getCursor(GeoCursors.CURSOR_PAN));
		} else if (mode == MapCanvas.MODE_ZOOM_IN) {
			this.setCursor(cursors.getCursor(GeoCursors.CURSOR_ZOOM_IN));
		} else if (mode == MapCanvas.MODE_ZOOM_OUT) {
			this.setCursor(cursors.getCursor(GeoCursors.CURSOR_ZOOM_OUT));
		} else if (mode == MapCanvas.MODE_EXCENTRIC) {
			this.exLabels = new ExcentricLabels();
			this.initExcentricLabels();
			// this.exLabels.setVisible(true);
			this.fisheyes = null;
			this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		} else if (mode == MapCanvas.MODE_FISHEYE) {
			this.exLabels = null;
			this.fisheyes = new Fisheyes();
			fisheyes.setLensType(Fisheyes.LENS_GAUSSIAN);
			this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		} else if (mode == MapCanvas.MODE_MAGNIFYING) {
			this.exLabels = null;
			this.fisheyes = new Fisheyes();
			fisheyes.setLensType(Fisheyes.LENS_INVERSE_COSINE);
			this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}

		else {
			throw new IllegalArgumentException(
					"MapCanvas.setMode, ecountered unknown mode");
		}

		this.mode = mode;
	}

	public void setBivarColorClasser(
			BivariateColorSymbolClassification bivarColorClasser) {
		this.bivarColorClasser = (BivariateColorSymbolClassificationSimple) bivarColorClasser;

		if (this.dataSet != null) {
			this.sendColorsToLayers(this.dataSet.getNumObservations());
		}
	}

	public void setAuxiliarySpatialData(DataSetForApps auxData) {
		logger.finest("got spatial data 2");
		if (auxData == null) {
			return;
		}
		LayerShape ls = new LayerPolygon();
		int layerType = auxData.getSpatialType();

		if (layerType == DataSetForApps.SPATIAL_TYPE_POLYGON) {
			ls = new LayerPolygon();
			ls.setOriginalSpatialData(auxData.getShapeData());

		} else if (layerType == DataSetForApps.SPATIAL_TYPE_LINE) {
			ls = new LayerPolygon();
			ls.setOriginalSpatialData(auxData.getShapeData());
		} else if (layerType == DataSetForApps.SPATIAL_TYPE_POINT) {
			LayerPoint lp = new LayerPoint();
			lp.setOriginalPoints(auxData.getPoint2DData());
			Shape[] pointShapesOriginal = lp.pointsToShapes(auxData
					.getPoint2DData());
			ls.setOriginalSpatialData(pointShapesOriginal);
			
		} else {
			System.err
					.println("unexpected layer type ecountered in MapCanvas.setAuxiliarySpatialData");
		}

		// ok, default behaviours here:
		// correct behavior depends on active layer type
		// if the active layer is a polygon layer, then
		// new layer goes on top of previous layer
		// otherwise we couldn't see the aux layer
		// so in that case add it to the end
		// and new layer has no fill
		//
		// if the active layer is a point layer or line layer
		// then the aux layer ought to go behind the points
		// so we put it first
		//
		// the layer manager enables the user to fix it if we guess wrong
		ls.setIsAuxiliary(true);
		if (this.shapeLayers.get(this.activeLayer) instanceof LayerPolygon) {
			this.shapeLayers.add(ls); // added to the end of the vector,
										// paints last, on top
			ls.setFillAux(false);
		} else {
			this.shapeLayers.add(0, ls); // paints first, so ends up on the
											// bottom
			ls.setFillAux(true);
			this.activeLayer++;
		}

		this.zoomFullExtent();
		this.paintDrawingBuff();
		this.repaint();
	}

	/**
	 * @param data
	 * 
	 * This method is deprecated becuase it wants to create its very own pet
	 * DataSetForApps. This is no longer allowed, to allow for a mutable, common
	 * data set. Use of this method may lead to unexpected program behavoir.
	 * Please use setDataSet instead.
	 */
	@Deprecated
	public void setDataSet(Object[] data) {
		this.setDataSet(new DataSetForApps(data));

	}

	public void setDataSet(DataSetForApps dataSet) {
		this.dataColorX = null;
		this.dataColorY = null;
		this.currColorColumnX = -1;
		this.currColorColumnY = -1;

		this.dataSet = dataSet;
		if (this.shapeLayers.size() > 0) {
			this.shapeLayers.remove(this.activeLayer); // frank: be careful
														// here
			
			

			logger.finest("in setDataSet, shapeLayer.size = " +shapeLayers.size() + ", activeLayer = " + activeLayer);
		}

		this.variableNames = this.dataSet.getAttributeNamesNumeric();

		int layerType = dataSet.getSpatialType();
		LayerShape ls = null;

		if (layerType == DataSetForApps.SPATIAL_TYPE_POLYGON) {
			ls = new LayerPolygon();
			ls.setOriginalSpatialData(dataSet.getShapeData());
		} else if (layerType == DataSetForApps.SPATIAL_TYPE_LINE) {

			ls = new LayerLine();
			ls.setOriginalSpatialData(dataSet.getShapeData());
		}

		else if (layerType == DataSetForApps.SPATIAL_TYPE_POINT) {
			LayerPoint lp = new LayerPoint();
			lp.setOriginalPoints(dataSet.getPoint2DData());
			Shape[] pointShapesOriginal = lp.pointsToShapes(dataSet
					.getPoint2DData());
			lp.setOriginalSpatialData(pointShapesOriginal);
			ls = lp;
		}
		shapeLayers.add(ls);
		this.activeLayer = shapeLayers.size() - 1;
		this.zoomFullExtent();
		// set default data to get color from
		if ((this.drawingBuff == null) && (this.getWidth() > 0)
				&& (this.getHeight() > 0)) {
			this.drawingBuff = this.createImage(this.getWidth(), this
					.getHeight());
		}

		// resizing will autofit if this.autofit = true
		// resizing resizes the drawing buffer, and repaints it
		boolean realAutofit = this.autofit;
		this.autofit = true;
		this.componentResized(new ComponentEvent(this, 0));
		this.autofit = realAutofit;
		this.initExcentricLabels();
	}

	public void setTransformer(ShapeTransformer transformer) {
		this.transformer = transformer;
	}

	/**
	 * Design specially for SimpleGeoMap set the currColorColumnX so that Map
	 * indication will display the value of the attribute whose index in
	 * dataModel equal to the currColorColumnX What distinguish this method from
	 * setCurrColorColumnX() is that: this method noly set value while
	 * setCurrColorColumnX() will do one more task: get data value of the
	 * attribute and ask the built-in classifier to generate color for it and
	 * repaint the map to display the color.
	 * 
	 * Since SimpleGeoMap will use external classifier rather than the built-in
	 * one, it need this method which not change the colors.
	 * 
	 * @param currColorColumnX
	 */
	public void setColorColumnX(int currColorColumnX) {
		if ((this.dataSet != null)
				&& (currColorColumnX <= dataSet.getNumberNumericAttributes())) {
			this.currColorColumnX = currColorColumnX;
		}
	}

	public void setCurrColorColumnX(int currColorColumnX) {
		if ((this.dataSet != null)
				&& (currColorColumnX <= dataSet.getNumberNumericAttributes())) {
			this.currColorColumnX = currColorColumnX;
			// XXX getNumericDataAsDouble has changed...
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("this.currColorColumnX ="
						+ this.currColorColumnX);
			}
			this.dataColorX = this.dataSet
					.getNumericDataAsDouble(currColorColumnX);
			this.sendColorsToLayers(this.dataColorX.length);
		}
	}

	public int getCurrColorColumnX() {
		return this.currColorColumnX;
	}

	public Color[] getColors() {
		if (this.shapeLayers.size() == 0) {
			return new Color[0];
		}

		LayerShape ls = (LayerShape) this.shapeLayers
				.elementAt(this.activeLayer);

		return ls.getColors();
	}

	/**
	 * Design specially for SimpleGeoMap set the currColorColumnY so that Map
	 * indication will display the value of the attribute whose index in
	 * dataModel equal to the currColorColumnY What distinguish this method from
	 * setCurrColorColumnY() is that: this method noly set value while
	 * setCurrColorColumnY() will do one more task: get data value of the
	 * attribute and ask the built-in classifier to generate color for it and
	 * repaint the map to display the color.
	 * 
	 * Since SimpleGeoMap will use external classifier rather than the built-in
	 * one, it need this method which not change the colors.
	 * 
	 * @param currColorColumnY
	 */
	public void setColorColumnY(int currColorColumnY) {
		if ((this.dataSet != null)
				&& (currColorColumnY <= dataSet.getNumberNumericAttributes())) {
			this.currColorColumnY = currColorColumnY;
		}
	}

	public void setCurrColorColumnY(int currColorColumnY) {
		if ((this.dataSet != null)
				&& (currColorColumnY <= dataSet.getNumberNumericAttributes())) {
			this.currColorColumnY = currColorColumnY;
			// XXX getNumericDataAsDouble has changed...
			this.dataColorY = this.dataSet
					.getNumericDataAsDouble(currColorColumnY);
			this.sendColorsToLayers(this.dataColorY.length);
		}
	}

	public int getCurrColorColumnY() {
		return this.currColorColumnY;
	}

	// jin: paint indication on map
	public void setIndication(int indication) {
		// if(true)return;

		if (fisheyes != null) {
			// if we have fisheyes, this is too expensive!!!
			// bail
			return;
		}

		if (indication != this.indication) {
			this.indication = indication;

			if (shapeLayers.size() > 0) {
				LayerShape ls = (LayerShape) shapeLayers.get(this.activeLayer);
				ls.setIndication(indication);

				if (indication >= 0
						&& indication < dataSet.getNumObservations()) {
					this.drawTip = true;

					Point p = ls.findCentroid(indication);
					if (p == null) {
						return;
					}
					this.mouseX2 = (int) p.getX();
					this.mouseY2 = (int) p.getY();
				} else {
					this.drawTip = false;
				}

				this.makeToolTip(indication);
			} // if we have at least one layer

			// paintDrawingBuff();
			this.repaint();
		} // if indication is new
	}

	public void setSelectedObservations(Vector selectedObservations) {
		this.selectedObservations = new int[selectedObservations.size()];

		int i = 0;

		for (Enumeration e = selectedObservations.elements(); e
				.hasMoreElements();) {
			Integer bigIint = (Integer) e.nextElement();
			this.selectedObservations[i] = bigIint.intValue();
			i++;
		}

		this.setSelectedObservationsInt(this.selectedObservations);
	}

	public Vector getSelectedObservations() {
		Vector v = new Vector();

		for (int i = 0; i < selectedObservations.length; i++) {
			v.add(new Integer(selectedObservations[i]));
		}

		return v;
	}

	public void setSelections(int[] selections) {
		Vector v = new Vector();

		for (int i = 0; i < selections.length; i++) {
			if (selections[i] == 1) {
				v.add(new Integer(i));
			}
		}

		this.selectedObservations = new int[v.size()];

		int i = 0;

		for (Enumeration e = v.elements(); e.hasMoreElements();) {
			Integer bigIint = (Integer) e.nextElement();
			this.selectedObservations[i] = bigIint.intValue();
			i++;
		}

		this.setSelectedObservationsInt(this.selectedObservations);
	}

	public int[] getSelections() {
		int[] selections = new int[this.dataColorX.length];
		if (this.selectedObservations == null) {
			this.selectedObservations = new int[0];
		}
		for (int i = 0; i < this.selectedObservations.length; i++) {
			selections[this.selectedObservations[i]] = 1;
		}

		return selections;
	}

	public void selectionChanged(SelectionEvent e) {
		int[] sel = e.getSelection();
		this.setSelectedObservationsInt(sel);
	}

	public void indicationChanged(IndicationEvent e) {
		if (this == e.getSource()) {
			return;
		}

		int indication = e.getIndication();
		this.setIndication(indication);
	}

	public void glyphChanged(GlyphEvent e) {
		if (this.dataSet == null) {
			return;
		}
		LayerShape layer = (LayerShape) this.shapeLayers.get(this.activeLayer);
		layer.setGlyphs(e.getGlyphs());
		this.paintDrawingBuff();
		this.repaint();
	}

	public void dataSetChanged(DataSetEvent e) {
		this.setDataSet(e.getDataSetForApps());

		logger.finest("set auxiliary data layer");
		// this.setAuxiliarySpatialData(e.getDataSet());
	}

	public void setSelectedObservationsInt(int[] selectedObservations) {
		this.selectedObservations = selectedObservations;

		if (selectedObservations == null) {
			return;
		}

		if ((shapeLayers.size() > 0) && (this.drawingBuff != null)) {
			LayerShape ls = (LayerShape) this.shapeLayers.get(this.activeLayer);
			ls.setSelectedObservations(this.selectedObservations);
			paintDrawingBuff();
			this.repaint();
		} else if ((shapeLayers.size() > 0) && (this.drawingBuff == null)
				&& (this.selectedObservations.length > 1)) { // means we have
																// data but are
																// not visible
																// yet

			LayerShape ls = (LayerShape) this.shapeLayers.get(this.activeLayer);
			ls.setSelectedObservations(this.selectedObservations);
		}
	}

	public int[] getSelectedObservationsInt() {
		return this.selectedObservations;
	}

	public void setColorSelection(Color colorSelection) {

		if (shapeLayers.size() > 0) {
			for (Enumeration e = shapeLayers.elements(); e.hasMoreElements();) {
				LayerShape ls = (LayerShape) e.nextElement();
				ls.setColorSelection(colorSelection);
			}

			paintDrawingBuff();
			this.repaint();
		}
	}

	public void setBackground(Color c) {
		super.setBackground(c);
		if (shapeLayers == null) {
			return;
		}
		for (Enumeration e = shapeLayers.elements(); e.hasMoreElements();) {
			LayerShape ls = (LayerShape) e.nextElement();
			ls.setColorBackground(c);
		}

		paintDrawingBuff();
		this.repaint();

	}

	// set clustering color
	public void setClusteringColor(Color[] clusteringColor) {
		LayerShape ls = (LayerShape) this.shapeLayers
				.elementAt(this.activeLayer);
		ls.setObjectColors(clusteringColor);
		paintDrawingBuff();
		this.repaint();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param conditionArray
	 */
	public void setConditionArray(int[] conditionArray) {
		if (shapeLayers.size() > 0 && drawingBuff != null) {
			for (Enumeration e = shapeLayers.elements(); e.hasMoreElements();) {
				LayerShape ls = (LayerShape) e.nextElement();
				ls.setConditionArray(conditionArray);
			}

			paintDrawingBuff();
			this.repaint();
		}
	}

	// end accessors
	public void actionPerformed(ActionEvent e) {
		// * this method used for testing only...

		if (e.getSource() instanceof ConditionManager) {
			ConditionManager cm = (ConditionManager) e.getSource();
			int[] conditionArray = cm.getConditionResults();
			this.setConditionArray(conditionArray);
		}

		// */
	}

	// start mouse event handling

	/**
	 * Draws a bounding box for selection.
	 * 
	 * @param e
	 */
	public void mouseDragged(MouseEvent e) {
		this.drawBox = true;
		this.drawTip = false;

		if (mode == MapCanvas.MODE_PAN) {
			Cursor grabCur = cursors.getCursor(GeoCursors.CURSOR_GRAB);

			if (this.getCursor() != grabCur) {
				this.setCursor(grabCur);
			}
		}

		mouseX2 = e.getX();
		mouseY2 = e.getY();

		if (mode == MapCanvas.MODE_PAN) {
			panBuff(mouseX1, mouseX2, mouseY1, mouseY2);
		}

		repaint();
	}

	/**
	 * Activates a tool tip.
	 * 
	 * @param e
	 */
	public void mouseMoved(MouseEvent e) {
		if (this.shapeLayers.size() < 1) {
			return;
		}

		if (fisheyes != null) {
			fisheyes.setFocus(e.getX(), e.getY());
			repaint();
		}

		LayerShape ls = (LayerShape) this.shapeLayers.get(this.activeLayer);
		int indic = ls.findIndication(e.getX(), e.getY());

		if (indic != indication) {
			this.setIndication(indic);

			int xClass = -1;
			int yClass = -1;

			if (indic > 0) {
				xClass = this.bivarColorClasser.getClassX(indic);
				yClass = this.bivarColorClasser.getClassY(indic);
			}

			this.fireIndicationChanged(indic, xClass, yClass);
		}

		mouseX2 = e.getX();
		mouseY2 = e.getY();
	}

	/**
	 * Inits selection bounding box.
	 * 
	 * @param e
	 */
	public void mousePressed(MouseEvent e) {
		mouseX1 = e.getX();
		mouseY1 = e.getY();
		mouseX2 = e.getX();
		mouseY2 = e.getY();
	}

	/**
	 * Makes selection.
	 * 
	 * @param e
	 */
	public void mouseReleased(MouseEvent e) {
		if (this.dataSet == null) {
			return;
		}

		if (mode == MapCanvas.MODE_PAN) {
			this.setCursor(cursors.getCursor(GeoCursors.CURSOR_PAN));
		}

		imagePanningXForm.setToIdentity();

		mouseX2 = e.getX();
		mouseY2 = e.getY();

		if (mode == MapCanvas.MODE_SELECT) {
			if (mouseX1 > mouseX2) {
				int temp = mouseX1;
				mouseX1 = mouseX2;
				mouseX2 = temp;
			}

			if (mouseY1 > mouseY2) {
				int temp = mouseY1;
				mouseY1 = mouseY2;
				mouseY2 = temp;
			}

			if (e.isShiftDown()) {
				this.makeSelectionShift(mouseX1, mouseX2 + 1, mouseY1,
						mouseY2 + 1);
			} else {
				this.makeSelection(mouseX1, mouseX2 + 1, mouseY1, mouseY2 + 1);
			}
		} else if (mode == MapCanvas.MODE_ZOOM_IN) {
			this.zoomIn(mouseX1, mouseX2, mouseY1, mouseY2);
		} else if (mode == MapCanvas.MODE_ZOOM_OUT) {
			this.zoomOut(mouseX1, mouseX2, mouseY1, mouseY2);
		} else if (mode == MapCanvas.MODE_PAN) {
			this.pan(mouseX1, mouseX2, mouseY1, mouseY2);
		}

		this.drawBox = false;
		repaint();
	}

	/**
	 * makes crosshair cursor
	 * 
	 * @param e
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * resets cursor
	 * 
	 * @param e
	 */
	public void mouseExited(MouseEvent e) {
		e.consume();
		this.drawTip = false; // prevents toolip from drawing
		this.drawBox = false;
		this.repaint();
		this.setIndication(-1);
		this.fireIndicationChanged(-1, 0, 0);

		if (fisheyes != null) {
			fisheyes.setFocus(-1000f, -1000f);
		}
	}

	/**
	 * pop up a detail map
	 * 
	 * @param e
	 */
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if(this.mode == MapCanvas.MODE_ZOOM_OUT){

			int width = this.getWidth();
			int height = this.getHeight();
			//width = width - (int)(width*.8);
			//height = height - (int)(height*.8);
			
			this.zoomOut(0,0,width,height);
		} else if (this.mode == MapCanvas.MODE_SELECT){
			if (e.isShiftDown()){
			this.makeSelectionShift(x, x+3, y, y+3);
			} else {
			this.makeSelection(x, x+3, y, y+3);
			}
		} else if(this.mode == MapCanvas.MODE_ZOOM_IN){
			this.zoomIn(x, x, y, y);
		} else if (mode == MapCanvas.MODE_PAN) {
			Cursor grabCur = cursors.getCursor(GeoCursors.CURSOR_GRAB);

			if (this.getCursor() != grabCur) {
				this.setCursor(grabCur);
				this.repaint();
			}
		}
		
		
		
	} // end method

	private void makeSelection(int x1, int x2, int y1, int y2) {
		int xDiff = Math.abs(x2 - x1);
		int yDiff = Math.abs(y2 - y1);
		int minPixels = 3;

		if ((xDiff < minPixels) && (yDiff < minPixels)) {
			return;
		}

		if (shapeLayers.size() > 0) {
			LayerShape ls = (LayerShape) this.shapeLayers.get(this.activeLayer);
			ls.findSelection(x1, x2, y1, y2);
			this.selectedObservations = ls.getSelectedObservations();

			// let's try just redrawing the selection
			// for (int i = 0; i < selectedObservations.length; i++) {
			ls.setSelectedObservations(selectedObservations);

			this.fireActionPerformed(LayerShape.COMMAND_SELECTION);
			this.fireSelectionChanged(this.selectedObservations);

			// unfortunately, we need to completely redraw any aux layers on top
			// of our active layer
			this.paintTopAuxLayers();

			// XXX shouldn't have to do this
			paintDrawingBuff();
			this.repaint();
		}
	} // method

	private void makeSelectionShift(int x1, int x2, int y1, int y2) {
		if (shapeLayers.size() > 0) {
			for (Enumeration e = shapeLayers.elements(); e.hasMoreElements();) {
				LayerShape ls = (LayerShape) e.nextElement();
				ls.findSelectionShift(x1, x2, y1, y2);
				this.selectedObservations = ls.getSelectedObservations();
			}

			this.fireActionPerformed(LayerShape.COMMAND_SELECTION);
			paintDrawingBuff();
			this.repaint();
		}
	}

	// end mouse event handling

	private boolean useBlur = true;

	/**
	 * Attention all layers! Paint yourselves onto the buffer. This can be an
	 * expensive operation, so this method is normally called by a RenderThread.
	 */
	private void paintDrawingBuff() {
		if (this.drawingBuff == null) {
			return;
		}
		if (logger.isLoggable(Level.FINEST)){
			//logger.throwing(this.getClass().getName(), "binka", new Exception());
			logger.finest("painting the buffer... again!");
		}
		Graphics g = this.drawingBuff.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
		g.setColor(this.getBackground());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		if (useBlur) {
			for (Enumeration e = shapeLayers.elements(); e.hasMoreElements();) {
				LayerShape ls = (LayerShape) e.nextElement();
				Color beforeColor = new Color(ls.colorBlur.getRGB());
				Color halfGrey = new Color(248, 248, 248, 230);
				ls.colorBlur = halfGrey;
				ls.renderBackground(g2); // paint your whole self (including
				ls.colorBlur = 	beforeColor;						// selections)
			} // next element
			
			
			//g.fillRect(0, 0, this.getWidth(), this.getHeight());
			BoxBlurFilter filter = new BoxBlurFilter();
			filter.setHRadius(10);
			filter.setVRadius(3);
			filter.setIterations(3);
			//maybe we could eliminate the use of the extra buffer?
			//we could use the panel itself as one drawing surface
			//and the drawingBuff as the other. 
			
			//OK, new theory. Grabbing bufferedimages this often is causing problems
			//so we cache. 
			BufferedImage blurBuff = new BufferedImage(this.drawingBuff	.getWidth(this), this.drawingBuff.getHeight(this),BufferedImage.TYPE_INT_ARGB);
			//VolatileImage blurBuff= this.getGraphicsConfiguration().createCompatibleVolatileImage(this.drawingBuff.getWidth(this), this.drawingBuff.getHeight(this));
			blurBuff.getGraphics().drawImage(this.drawingBuff, 0, 0, this);
			filter.filter(blurBuff, blurBuff);
			g2.drawImage(blurBuff, null, 0, 0);
		}
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if (shapeLayers.size() > 0) {
			for (Enumeration e = shapeLayers.elements(); e.hasMoreElements();) {
				LayerShape ls = (LayerShape) e.nextElement();
				ls.fisheyes = this.fisheyes;
				ls.render(g2); // paint your whole self, selections only
			} // next element
		} // end if

	}

	/**
	 * Attention all auxiliary layers! paint yourselves onto the buffer.
	 * 
	 * @param g
	 */
	private void paintTopAuxLayers() {
		if (this.drawingBuff == null) {
			return;
		}

		Graphics g = this.drawingBuff.getGraphics();
		Graphics2D g2 = (Graphics2D) g;

		if (shapeLayers.size() > 0) {
			int layerNum = 0;
			for (Enumeration e = shapeLayers.elements(); e.hasMoreElements();) {
				LayerShape ls = (LayerShape) e.nextElement();
				logger.finest("layerNum = " + layerNum + ", activeLayer = " + activeLayer);
				if (ls.getIsAuxiliary() && layerNum > this.activeLayer) { // if
																			// you
																			// are
																			// aux
																			// and
																			// on
																			// top
					ls.fisheyes = this.fisheyes;
					ls.render(g2); // paint your whole self
				} // end if aux
				layerNum++;
			} // next element
		} // end if
	}

	/**
	 * paints buffer, then drawing box
	 * 
	 * @param g
	 */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		if ((this.drawingBuff == null) || (this.fisheyes != null)) {
			this.drawingBuff = this.createImage(this.getWidth(), this
					.getHeight());
			paintDrawingBuff();
		}

		// Draw buff
		if (this.drawingBuff != null) {
			g2.drawImage(this.drawingBuff, this.imagePanningXForm, this);

			// g.drawImage(this.drawingBuff,0,0,this);
		}
		// Just draw background
		else {
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}

		// Draw indication.
		if ((this.indication != Integer.MIN_VALUE) && (shapeLayers.size() > 0)
				&& (this.mode != MapCanvas.MODE_PAN) && (this.fisheyes == null)) {
			LayerShape ls = (LayerShape) this.shapeLayers
					.elementAt(this.activeLayer);

			if (indication >= 0) {
				((LayerShape) ls).renderObservation(indication, g2);
			}

		}

		drawSelectionBox(g2);

		// this.toolTip.paint(g);
		if (this.drawTip && this.exLabels == null) {
			drawTooltip(g2, this.tipText, this.mouseX2, this.mouseY2,
					Color.lightGray);
		} else if (this.drawTip
				&& (this.exLabels != null && this.exLabels.isVisible() == false)) {
			drawTooltip(g2, this.tipText, this.mouseX2, this.mouseY2,
					Color.lightGray);
		}

		if (exLabels != null && this.exLabels.isVisible()) {
			Font currFont = g2.getFont();
			Font biggerFont = new Font("Ariel",Font.PLAIN,currFont.getSize()+2);
			g2.setFont(biggerFont);
			Stroke biggerStroke = new BasicStroke(3f);
			g2.setStroke(biggerStroke);
			exLabels.paint(g2, getBounds());
		}
	}

	/**
	 * Helper function to draw a "tooltip" on the given graphics object.
	 * 
	 * @param g2
	 *            The Graphics2D Object to draw on.
	 * @param text
	 *            The (multiline) text of the tooltip.
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @param col
	 *            The background color.
	 */
	private void drawTooltip(Graphics2D g2, String text, int x, int y, Color col) {
		if (!drawTip) {
			return;
		}

		int i;
		int mheight;
		int mwidth = 0;
		int numLines;
		int lineHeight;
		Font f = g2.getFont();
		tipFont = f.deriveFont(9f);
		g2.setFont(tipFont);

		StringTokenizer tok = new StringTokenizer(text, "\n");
		numLines = tok.countTokens();

		String[] lines = new String[numLines];

		for (i = 0; i < numLines; i++) {
			lines[i] = tok.nextToken();

			int tempwidth = g2.getFontMetrics().stringWidth(lines[i]) + 6;

			if (tempwidth > mwidth) {
				mwidth = tempwidth;
			}
		}

		lineHeight = g2.getFontMetrics().getHeight();
		mheight = (numLines * lineHeight) + 2;

		x += 20;
		y += 20;

		// if we are too close to the right side....
		if ((x + mwidth) > this.getWidth()) {
			x -= (mwidth + 40);
		}

		// if we are too close to the bottom...
		if ((y + mheight) > this.getHeight()) {
			y -= (mheight + 40);
		}

		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.7f);
		g2.setComposite(ac);

		g2.setStroke(new BasicStroke(0.5f));
		g2.setColor(new Color(0.2f, 0.2f, 0.2f, 0.7f));

		// g2.drawRect(x, y, mwidth, mheight);
		g2.setColor(col);
		g2.fillRect(x + 1, y + 1, mwidth - 1, mheight - 1);

		g2.setColor(Color.black);

		ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
		g2.setComposite(ac);

		for (i = 0; i < numLines; i++) {
			g2.drawString(lines[i], x + 3, (y + ((i + 1) * lineHeight)) - 4);
		}
	}

	/***************************************************************************
	 * 
	 */
	private void drawSelectionBox(Graphics2D g2) {
		// draw selection box
		if (!drawBox) {
			return;
		}

		Stroke tempStroke = g2.getStroke();
		Stroke boxStroke = null;

		if (this.mode == MapCanvas.MODE_SELECT) {
			boxStroke = this.dashStroke;
		} else if ((this.mode == MapCanvas.MODE_ZOOM_IN)
				|| (this.mode == MapCanvas.MODE_ZOOM_OUT)) {
			boxStroke = this.solidStroke;
		} else {
			return;
		}

		g2.setStroke(boxStroke);
		g2.setPaintMode();
		g2.setColor(Color.black);
		g2.setXORMode(Color.white);

		// let's take drawing the selection rectangle by cases
		// not elegant, but the alternative is introducing more class variables
		int selectX = 0;
		int selectY = 0;
		int selectWidth = 0;
		int selectHeight = 0;

		if ((mouseX1 <= mouseX2) && (mouseY1 <= mouseY2)) {
			selectX = mouseX1;
			selectY = mouseY1;
			selectWidth = mouseX2 - mouseX1;
			selectHeight = mouseY2 - mouseY1;
		}

		if ((mouseX2 < mouseX1) && (mouseY1 <= mouseY2)) {
			selectX = mouseX2;
			selectY = mouseY1;
			selectWidth = mouseX1 - mouseX2;
			selectHeight = mouseY2 - mouseY1;
		}

		if ((mouseX1 <= mouseX2) && (mouseY2 < mouseY1)) {
			selectX = mouseX1;
			selectY = mouseY2;
			selectWidth = mouseX2 - mouseX1;
			selectHeight = mouseY1 - mouseY2;
		}

		if ((mouseX2 < mouseX1) && (mouseY2 < mouseY1)) {
			selectX = mouseX2;
			selectY = mouseY2;
			selectWidth = mouseX1 - mouseX2;
			selectHeight = mouseY1 - mouseY2;
		}

		g2.drawRect(selectX, selectY, selectWidth, selectHeight);
		g2.setStroke(tempStroke);
	}

	public int getNumberOfLayers() {
		return this.shapeLayers.size();
	}

	// start excentric labeling stuff
	private void initExcentricLabels() {
		if (exLabels != null) {
			this.exLabels = new ExcentricLabels();
			exLabels.setComponent(this);
			exLabels.setOpaque(true);

			Color halfWhite = new Color(255, 255, 255, 123);
			exLabels.setBackgroundColor(halfWhite);
			this.addMouseListener(exLabels);
		}
	}

	public String getObservationLabel(int i) {
		String[] labels = this.dataSet.getObservationNames();
		String label = labels[i];

		return label;
	}

	public Shape getShapeAt(int i) {
		LayerShape ls = (LayerShape) this.shapeLayers.get(this.activeLayer);
		Shape shp = ls.getSpatialData()[i];

		return shp;
	}

	public int[] pickAll(Rectangle2D hitBox) {
		LayerShape ls = (LayerShape) this.shapeLayers.get(this.activeLayer);
		int[] selObs = ls.findSelection(hitBox);

		return selObs;
	}

	// end excentric labeling stuff

	/**
	 * implements ActionListener
	 */
	public void addActionListener(ActionListener l) {
		listenerList.add(ActionListener.class, l);
	}

	/**
	 * removes an ActionListener from the button
	 */
	public void removeActionListener(ActionListener l) {
		listenerList.remove(ActionListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	protected void fireActionPerformed(String command) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		ActionEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
							command);
				}

				((ActionListener) listeners[i + 1]).actionPerformed(e);
			}
		}
	}

	/**
	 * adds an IndicationListener
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
	private void fireIndicationChanged(int newIndication, int xClass, int yClass) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		IndicationEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == IndicationListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new IndicationEvent(this, newIndication, xClass, yClass);
				}

				((IndicationListener) listeners[i + 1]).indicationChanged(e);
			}
		} // next i
	}

	/**
	 * adds an SelectionListener
	 */
	public void addSelectionListener(SelectionListener l) {
		logger.finest("mapCan, selection listeners = " +listenerList.getListenerCount(SelectionListener.class));
		listenerList.add(SelectionListener.class, l);
	}

	/**
	 * removes an SelectionListener from the component
	 */
	public void removeSelectionListener(SelectionListener l) {
		logger.finest("mapCan, removing a selection listener");
		listenerList.remove(SelectionListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	private void fireSelectionChanged(int[] newSelection) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		SelectionEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == SelectionListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new SelectionEvent(this, newSelection);
				}

				((SelectionListener) listeners[i + 1]).selectionChanged(e);
			}
		} // next i
	}

	/**
	 * adds an SpatialExtentListener
	 */
	public void addSpatialExtentListener(SpatialExtentListener l) {
		listenerList.add(SpatialExtentListener.class, l);
	}

	/**
	 * removes an SpatialExtentListener from the component
	 */
	public void removeSpatialExtentListener(SpatialExtentListener l) {
		listenerList.remove(SpatialExtentListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	private void fireSpatialExtentChanged(Rectangle2D newSpatialExtent) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		SpatialExtentEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == SpatialExtentListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new SpatialExtentEvent(this, newSpatialExtent);
				}

				((SpatialExtentListener) listeners[i + 1])
						.spatialExtentChanged(e);
			}
		} // next i
	}

	/*
	 * 
	 * without a gui, this method is basically a placeholder
	 */
	public void setActiveLayerIdx(int idx) {
		int numlayers = this.getNumberOfLayers();
		if (numlayers > 0 && idx < numlayers) {
			this.activeLayer = idx;
			((LayerShape) this.shapeLayers.elementAt(this.activeLayer))
					.setIsAuxiliary(false);
			for (int i = 0; i < numlayers; i++) {
				if (i != this.activeLayer) {
					((LayerShape) this.shapeLayers.elementAt(i))
							.setIsAuxiliary(true);
				}
			}
			this.validate();
			this.repaint();
		}
	}

	public void setAutofit(boolean autofit) {
		this.autofit = autofit;
	}

}
