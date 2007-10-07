/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class GeoMap
 Copyright (c), 2002, GeoVISTA Center
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: GeoMap.java,v 1.20 2005/08/19 19:17:32 hardisty Exp $
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import geovista.common.classification.ClassifierPicker;
import geovista.common.data.DataSetForApps;
import geovista.common.event.AuxiliaryDataSetEvent;
import geovista.common.event.AuxiliaryDataSetListener;
import geovista.common.event.ColorArrayEvent;
import geovista.common.event.ColorArrayListener;
import geovista.common.event.ConditioningEvent;
import geovista.common.event.ConditioningListener;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.IndicationEvent;
import geovista.common.event.IndicationListener;
import geovista.common.event.SelectionEvent;
import geovista.common.event.SelectionListener;
import geovista.common.event.SpatialExtentEvent;
import geovista.common.event.SpatialExtentListener;
import geovista.common.event.VariableSelectionEvent;
import geovista.common.event.VariableSelectionListener;
import geovista.common.ui.Fisheyes;
import geovista.common.ui.slider.MultiSlider;
import geovista.common.ui.slider.RangeSlider;
import geovista.coordination.CoordinationManager;
import geovista.geoviz.sample.GeoData48States;
import geovista.geoviz.shapefile.ShapeFileDataReader;
import geovista.geoviz.shapefile.ShapeFileProjection;
import geovista.geoviz.shapefile.ShapeFileToShape;
import geovista.geoviz.ui.cursor.GeoCursors;
import geovista.geoviz.visclass.VisualClassifier;
import geovista.symbolization.BivariateColorSchemeVisualizer;
import geovista.symbolization.BivariateColorSymbolClassification;
import geovista.symbolization.BivariateColorSymbolClassificationSimple;
import geovista.symbolization.ColorSymbolClassification;
import geovista.symbolization.event.ColorClassifierEvent;
import geovista.symbolization.event.ColorClassifierListener;


/**
 * This class handles the user state, like selection, pan, zoom, plus
 * symbolization options.
 * 
 * MapCanvas does most of the work.
 */
public class GeoMap extends JPanel
		implements
		// MouseListener, MouseMotionListener,
		ActionListener, SelectionListener, IndicationListener, DataSetListener,
		AuxiliaryDataSetListener, ColorClassifierListener, ColorArrayListener,
		SpatialExtentListener, ComponentListener, ConditioningListener,
		TableModelListener,
		// the following line commented out until Glyph support improves
		// GlyphListener,
		VariableSelectionListener {

	public static final int VARIABLE_CHOOSER_MODE_ACTIVE = 0;
	public static final int VARIABLE_CHOOSER_MODE_FIXED = 1;
	public static final int VARIABLE_CHOOSER_MODE_HIDDEN = 2;

	transient protected MapCanvas mapCan;

	transient protected VisualClassifier visClassOne;
	transient protected VisualClassifier visClassTwo;

	protected JPanel legendPanel = new JPanel();

	transient protected JToolBar mapTools;

	transient protected JPanel topContent;

	transient protected Cursor[] customCursors;

	transient protected GeoCursors cursors;

	transient protected BivariateColorSchemeVisualizer biViz;

	transient protected Dimension currSize;

	transient protected Fisheyes fisheyes;

	protected final static Logger logger = Logger.getLogger(GeoMap.class.getName());
	
	transient DataSetForApps dataSet;

	// Added by Diansheng. The reason for this is that for some applications we
	// need
	// to assign colors using other componenents instead of the visual
	// classifier.
	private boolean visualClassifierNeeded = true;

	public GeoMap(boolean needVisualClassifier) {
		super();
		this.visualClassifierNeeded = needVisualClassifier;
		init();
	}

	public GeoMap() {
		super();
		init();
	}

	private void init() {
		if (this.visualClassifierNeeded) {
			JPanel vcPanel = new JPanel();
			vcPanel.setLayout(new BoxLayout(vcPanel, BoxLayout.Y_AXIS));
			visClassOne = new VisualClassifier();
			visClassTwo = new VisualClassifier();
			visClassOne.setAlignmentX(Component.LEFT_ALIGNMENT);
			visClassTwo.setAlignmentX(Component.LEFT_ALIGNMENT);
			visClassOne
					.setVariableChooserMode(ClassifierPicker.VARIABLE_CHOOSER_MODE_ACTIVE);
			visClassTwo
					.setVariableChooserMode(ClassifierPicker.VARIABLE_CHOOSER_MODE_ACTIVE);
			visClassOne.addActionListener(this);
			visClassOne.setOrientationInParentIsX(true);
			visClassTwo.addActionListener(this);
			visClassTwo.setOrientationInParentIsX(false);

			vcPanel.add(visClassTwo);
			vcPanel.add(visClassOne);

			legendPanel = new JPanel();
			legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.X_AXIS));
			
			legendPanel.add(vcPanel);
			legendPanel.add(Box.createRigidArea(new Dimension(4, 2)));
			biViz = new BivariateColorSchemeVisualizer();
			

			legendPanel.add(biViz);

			topContent = new JPanel();
			topContent.setLayout(new BoxLayout(topContent, BoxLayout.Y_AXIS));
			makeToolbar();
			legendPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			mapTools.setAlignmentX(Component.LEFT_ALIGNMENT);

			topContent.add(legendPanel);
		}
		topContent.add(mapTools);

		// note: uncomment the line below for animation panel stuff
		// vcPanel.add(this.makeAnimationPanel());
		cursors = new GeoCursors();
		this.setCursor(cursors.getCursor(GeoCursors.CURSOR_ARROW_SELECT));

		this.setLayout(new BorderLayout());
		this.add(topContent, BorderLayout.NORTH);
		mapCan = new MapCanvas();
		this.add(mapCan, BorderLayout.CENTER);
		this.mapCan.addIndicationListener(this);
		visClassOne.addColorClassifierListener(this);
		visClassTwo.addColorClassifierListener(this);
		this.addIndicationListener(biViz);

		visClassTwo.setHighColor(new Color(0, 150, 0)); // green
		this.currSize = new Dimension(this.getSize());
		this.fisheyes = new Fisheyes();
		this.fisheyes.setLensType(Fisheyes.LENS_HEMISPHERE);

		// this.colorClassifierChanged(new
		// ColorClassifierEvent(visClassTwo,visClassTwo.getColorSymbolClassification()));
	}

	public JPanel makeAnimationPanel() {
		JPanel animationPanel = new JPanel();
		Dimension prefSize = new Dimension(100, 50);
		animationPanel.setSize(prefSize);
		animationPanel.setPreferredSize(prefSize);

		// animationPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
		JLabel timeLabel = new JLabel("Time Step:");
		animationPanel.add(timeLabel);

		JSlider timeSlider = new JSlider(0, 10, 3);
		timeSlider.setPaintLabels(true);
		timeSlider.setPaintTicks(true);
		timeSlider.setPaintTrack(true);
		timeSlider.setMajorTickSpacing(1);
		timeSlider.createStandardLabels(2);

		animationPanel.add(timeSlider);

		return animationPanel;
	}

	public void makeToolbar() {
		mapTools = new JToolBar();

		// Dimension prefSize = new Dimension(100,10);
		// mapTools.setMinimumSize(prefSize);
		// mapTools.setPreferredSize(prefSize);
		JButton button = null;
		Class cl = GeoMap.class;
		URL urlGif = null;
		Dimension buttDim = new Dimension(20, 20);

		// first button
		try {
			urlGif = cl.getResource("resources/select16.gif");
			button = new JButton(new ImageIcon(urlGif));
			button.setPreferredSize(buttDim);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		button.setToolTipText("Enter selection mode");
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				GeoMap.this.setCursor(cursors
						.getCursor(GeoCursors.CURSOR_ARROW_SELECT));
				GeoMap.this.mapCan.setMode(MapCanvas.MODE_SELECT);
			}
		});
		mapTools.add(button);

		mapTools.addSeparator();

		// second button
		try {
			urlGif = cl.getResource("resources/ZoomIn16.gif");
			button = new JButton(new ImageIcon(urlGif));
			button.setPreferredSize(buttDim);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		button.setToolTipText("Enter zoom in mode");
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				GeoMap.this.setCursor(cursors
						.getCursor(GeoCursors.CURSOR_ARROW_ZOOM_IN));
				GeoMap.this.mapCan.setMode(MapCanvas.MODE_ZOOM_IN);

				// GeoMap.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
		mapTools.add(button);

		// third button
		try {
			urlGif = cl.getResource("resources/ZoomOut16.gif");
			button = new JButton(new ImageIcon(urlGif));
			button.setPreferredSize(buttDim);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		button.setToolTipText("Enter zoom out mode");
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				GeoMap.this.mapCan.setMode(MapCanvas.MODE_ZOOM_OUT);
				GeoMap.this.setCursor(cursors
						.getCursor(GeoCursors.CURSOR_ARROW_ZOOM_OUT));
			}
		});
		mapTools.add(button);

		// fourth button
		try {
			urlGif = cl.getResource("resources/Home16.gif");
			button = new JButton(new ImageIcon(urlGif));
			button.setPreferredSize(buttDim);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		button.setToolTipText("Zoom to full extent");
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				GeoMap.this.mapCan.zoomFullExtent();
			}
		});
		mapTools.add(button);

		// fifth button
		try {
			urlGif = cl.getResource("resources/pan16.gif");
			button = new JButton(new ImageIcon(urlGif));
			button.setPreferredSize(buttDim);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		button.setToolTipText("Enter pan mode");
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				GeoMap.this.setCursor(cursors
						.getCursor(GeoCursors.CURSOR_ARROW_PAN));
				GeoMap.this.mapCan.setMode(MapCanvas.MODE_PAN);
			}
		});
		mapTools.add(button);
		// sixth button
		try {
			urlGif = cl.getResource("resources/excentric16.gif");
			button = new JButton(new ImageIcon(urlGif));
			button.setPreferredSize(buttDim);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		button.setToolTipText("Excentric Labels");
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				GeoMap.this.setCursor(cursors
						.getCursor(GeoCursors.CURSOR_ARROW_PAN));
				GeoMap.this.mapCan.setMode(MapCanvas.MODE_EXCENTRIC);
			}
		});
		mapTools.add(button);

		// seventh button
		try {
			urlGif = cl.getResource("resources/fisheye16.gif");
			button = new JButton(new ImageIcon(urlGif));
			button.setPreferredSize(buttDim);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		button.setToolTipText("Fisheye Lens");
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				GeoMap.this.setCursor(cursors
						.getCursor(GeoCursors.CURSOR_ARROW_PAN));
				GeoMap.this.mapCan.setMode(MapCanvas.MODE_FISHEYE);
			}
		});
		mapTools.add(button);
		// eighth button
		try {
			urlGif = cl.getResource("resources/magnifying16.gif");
			button = new JButton(new ImageIcon(urlGif));
			button.setPreferredSize(buttDim);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		button.setToolTipText("Magnifiying Lens");
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				GeoMap.this.setCursor(cursors
						.getCursor(GeoCursors.CURSOR_ARROW_PAN));
				GeoMap.this.mapCan.setMode(MapCanvas.MODE_MAGNIFYING);
			}
		});

		mapTools.add(button);

	}

	// jin: called by visualClassifier when change classification
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		String command = e.getActionCommand();
		String varChangedCommand = ClassifierPicker.COMMAND_SELECTED_VARIABLE_CHANGED;

		if ((src == this.visClassOne) && command.equals(varChangedCommand)) {
			int index = visClassOne.getCurrVariableIndex();
			// index++; dataSetForApps goodness
			if (index >= 0) { // fix bug by jin,there could be a chance when
				// index=-1
				this.mapCan.setCurrColorColumnX(index);
			} else {
				this.mapCan.setCurrColorColumnX(0);
			}
		} else if ((src == this.visClassTwo)
				&& command.equals(varChangedCommand)) {
			int index = visClassTwo.getCurrVariableIndex();
			// index++; dataSetForApps goodness
			if (index >= 0) { // fix bug by jin, there could be a chance when
				// index=-1, it =>
				// DataSetForApps.getNumericDataAsDouble()
				// throws exceptions
				this.mapCan.setCurrColorColumnY(index);
			} else {
				this.mapCan.setCurrColorColumnY(0);
			}
		}

	}

	public boolean isVisualClassifierNeeded() {
		return this.visualClassifierNeeded;
	}

	public void setVisualClassifierNeeded(boolean b) {
		this.visualClassifierNeeded = b;
		topContent.removeAll();
		if (this.visualClassifierNeeded) {
			topContent.add(legendPanel);
		}
		topContent.add(mapTools);
	}

	public void selectionChanged(SelectionEvent e) {
		mapCan.selectionChanged(e);
	}

	public void conditioningChanged(ConditioningEvent e) {
		mapCan.setConditionArray(e.getConditioning());
	}

	public void indicationChanged(IndicationEvent e) {
		Object source = e.getSource();

		if ((source == this.mapCan) && (e.getIndication() >= 0)) {
			// jin: for debug purpose
			int xClass = e.getXClass();
			int yClass = e.getYClass();
			this.visClassOne.setIndicatedClass(xClass);
			this.visClassTwo.setIndicatedClass(yClass);

			// this.fireIndicationChanged(e.getIndication());
		} else if ((source == this.mapCan) && (e.getIndication() < 0)) {
			this.visClassOne.setIndicatedClass(-1);
			this.visClassTwo.setIndicatedClass(-1);
			this.biViz.indicationChanged(new IndicationEvent(this, -1));

			// this.fireIndicationChanged(e.getIndication());
		} else {
			mapCan.indicationChanged(e);
		}
	}

	public void spatialExtentChanged(SpatialExtentEvent e) {
		mapCan.spatialExtentChanged(e);
	}

	public void dataSetChanged(DataSetEvent e) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("GeoMap, got a data set, id = "
					+ e.getDataSetForApps().hashCode());
		}
		this.setDataSet(e.getDataSetForApps());

		
	}

	public void dataSetAdded(AuxiliaryDataSetEvent e) {
		if (e.getDataSetForApps().getShapeData() == null){
			return;
		}
		this.setAuxiliarySpatialData(e.getDataSetForApps());
	}

	public void setAuxiliarySpatialData(DataSetForApps dataSet) {
		this.mapCan.setAuxiliarySpatialData(dataSet);

	}

	/**
	 * 
	 */
	public void setTextures(TexturePaint[] textures) {
		this.mapCan.setTextures(textures);
	}

	public void zoomFullExtent() {
		this.mapCan.zoomFullExtent();// sometimes we need to do this...
		// the map should figure out for itself when it need to redisplay, but
		// it doesnt
		// hence this method
	}

	public void variableSelectionChanged(VariableSelectionEvent e) {
		visClassOne.setCurrVariableIndex(e.getVariableIndex() + 1);
		mapCan.setCurrColorColumnX(e.getVariableIndex() + 1);
	}

	public void colorArrayChanged(ColorArrayEvent e) {
		if (this.mapCan == null || e.getColors() == null) {
			return;
		}
		this.mapCan.setObservationColors(e.getColors());
	}

	public void colorClassifierChanged(ColorClassifierEvent e) {
		if (e.getSource() == this.visClassOne) {
			e.setOrientation(ColorClassifierEvent.SOURCE_ORIENTATION_X);
		}

		if (e.getSource() == this.visClassTwo) {
			e.setOrientation(ColorClassifierEvent.SOURCE_ORIENTATION_Y);
		}

		this.biViz.colorClassifierChanged(e);

		if (this.mapCan == null) {
			return;
		}

		ColorSymbolClassification colorSymbolizerX = this.visClassOne
				.getColorClasser();
		ColorSymbolClassification colorSymbolizerY = this.visClassTwo
				.getColorClasser();

		BivariateColorSymbolClassificationSimple biColorSymbolizer = new BivariateColorSymbolClassificationSimple();
		// turn these around to match what happens in the
		// scatterplot
		biColorSymbolizer.setClasserX(colorSymbolizerX.getClasser());
		biColorSymbolizer.setColorerX(colorSymbolizerX.getColorer());

		biColorSymbolizer.setClasserY(colorSymbolizerY.getClasser());
		biColorSymbolizer.setColorerY(colorSymbolizerY.getColorer());
		this.mapCan.setBivarColorClasser(biColorSymbolizer);
	}

	public void setXVariable(int var) {
		this.visClassOne.setCurrVariableIndex(var);
	}

	public void setYVariable(int var) {
		this.visClassTwo.setCurrVariableIndex(var);
	}

	public void setXChooserMode(int chooserMode) {
		this.visClassOne.setVariableChooserMode(chooserMode);
	}

	public void setYChooserMode(int chooserMode) {
		this.visClassTwo.setVariableChooserMode(chooserMode);
	}

	public void setBivarColorClasser(
			BivariateColorSymbolClassification bivarColorClasser) {
		this.mapCan.setBivarColorClasser(bivarColorClasser);
	}

	public void setSelectedObservations(int[] selObs) {
		this.mapCan.setSelectedObservationsInt(selObs);
	}

	public void setClusteringColor(Color[] clusteringColor) {
	}

	public int[] getSelectedObservations() {
		return this.mapCan.getSelectedObservationsInt();
	}

	public Color[] getColors() {
		return this.mapCan.getColors();
	}

	public BivariateColorSymbolClassification getBivariateColorSymbolClassification() {
		return this.biViz.getBivariateColorClassification();
	}

	public void setDataSet(DataSetForApps dataSet) {
		dataSet.addTableModelListener(this);
		this.dataSet = dataSet;
		this.mapCan.setDataSet(dataSet);
		this.visClassOne.setDataSet(dataSet);
		this.visClassTwo.setDataSet(dataSet);

		// set default data to get color from
		int numNumeric = dataSet.getNumberNumericAttributes();

		int currColorColumnX = this.mapCan.getCurrColorColumnX();
		int currColorColumnY = this.mapCan.getCurrColorColumnY();

		if (currColorColumnX < 0) {
			if (numNumeric > 0) {
				int index = visClassOne.getCurrVariableIndex();
				// this.setXVariable(1);
				// this.mapCan.setCurrColorColumnX(1); // jin: set the initial
				// variable index of visualclassifier
				this.setXVariable(index);
				this.mapCan.setCurrColorColumnX(index);
			}
		} else if (currColorColumnX < numNumeric) {
			this.setXVariable(currColorColumnX);
			this.mapCan.setCurrColorColumnX(currColorColumnX);
		}

		if (currColorColumnY < 0) {
			if (numNumeric > 1) {

				int index = visClassTwo.getCurrVariableIndex();
				// this.setYVariable(2);
				// this.mapCan.setCurrColorColumnY(2); // jin: set the initial
				// variable index of visualclassifier
				this.setYVariable(index);
				this.mapCan.setCurrColorColumnY(index);
			} else if (numNumeric == 1) {
				this.setYVariable(1);
				this.mapCan.setCurrColorColumnY(1);
			}
		} else if (currColorColumnY < numNumeric) {
			setYVariable(currColorColumnY);
			this.mapCan.setCurrColorColumnY(currColorColumnY);
		}

		// Jin: fix the bug the initially the variable names in comboxes not
		// mataching the variable names in the indication

		if (numNumeric > 2) { // frank: fix the bug of the map crashing if
			// there are not enough variables
			visClassOne.setCurrVariableIndex(1); // Jin
			visClassTwo.setCurrVariableIndex(2);
			// frank here again... lets try something else....
			ActionEvent e = new ActionEvent(this, 0,
					ClassifierPicker.COMMAND_SELECTED_VARIABLE_CHANGED);
			visClassOne.actionPerformed(e);
			visClassTwo.actionPerformed(e);
		}
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
	public void setData(Object[] data) {
		this.setDataSet(new DataSetForApps(data));

	}

	public void setBackground(Color bg) {
		if ((mapCan != null) && (bg != null)) {
			this.mapCan.setBackground(bg);
		}
	}

	// start component listener stuff
	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		// todo: zoom in or out a bit

	}

	public void componentShown(ComponentEvent e) {
	}

	// end component listener stuff

	/**
	 * adds an IndicationListener
	 */
	public void addIndicationListener(IndicationListener l) {
		this.mapCan.addIndicationListener(l);
	}

	/**
	 * removes an IndicationListener from the component
	 */
	public void removeIndicationListener(IndicationListener l) {
		this.mapCan.removeIndicationListener(l);
	}

	/**
	 * adds an SelectionListener
	 */
	public void addSelectionListener(SelectionListener l) {
		this.mapCan.addSelectionListener(l);
	}

	/**
	 * removes an SelectionListener from the component
	 */
	public void removeSelectionListener(SelectionListener l) {
		this.mapCan.removeSelectionListener(l);
	}

	/**
	 * adds an SpatialExtentListener
	 */
	public void addSpatialExtentListener(SpatialExtentListener l) {
		this.mapCan.addSpatialExtentListener(l);
	}

	/**
	 * removes an SpatialExtentListener from the component
	 */
	public void removeSpatialExtentListener(SpatialExtentListener l) {
		listenerList.remove(SpatialExtentListener.class, l);
		this.mapCan.removeSpatialExtentListener(l);
	}

//	/**
//	 * implements ColorClassifierListener
//	 */
//	public void addColorClassifierListener(ColorClassifierListener l) {
//		this.visClassOne.addColorClassifierListener(l);
//		this.visClassTwo.addColorClassifierListener(l);
//
//	}
//
//	/**
//	 * removes an ColorClassifierListener from the component
//	 */
//	public void removeColorClassifierListener(ColorClassifierListener l) {
//		this.visClassOne.removeColorClassifierListener(l);
//		this.visClassTwo.removeColorClassifierListener(l);
//	}

	public void setActiveLayerIdx(int idx) {
		if (this.mapCan != null) {
			this.mapCan.setActiveLayerIdx(idx);
			// this.setDataSet(this.mapCan.get); //frank: commented out
		}
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	public void fireSelectionChanged(int[] newSelection) {
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

	public static void main(String[] args) {
		
		
		
		boolean useProj = true;
		boolean useResource = true;
		JFrame app = new JFrame("MapBean Main Class: Why?");
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		app.getContentPane().setLayout(
				new BoxLayout(app.getContentPane(), BoxLayout.X_AXIS));
		
		
		MultiSlider mSlider = new MultiSlider();
		
		RangeSlider rSlider = new RangeSlider();
		rSlider.setVisible(true);
		mSlider.setNumberOfThumbs(5);
		mSlider.setVisible(true);
		//mSlider.setOrientation(mSlider.VERTICAL);
		app.getContentPane().add(mSlider);
		app.pack();
		app.setVisible(true);

		GeoMap map2 = new GeoMap();
		
		
		//app.getContentPane().add(map2);
		app.pack();
		app.setVisible(true);

		String fileName = "C:\\arcgis\\arcexe81\\Bin\\TemplateData\\USA\\counties.shp";
		fileName = "C:\\temp\\shapefiles\\intrstat.shp";
		fileName = "C:\\data\\geovista_data\\shapefiles\\larger_cities.shp";
		fileName = "C:\\data\\geovista_data\\shapefiles\\jin\\CompanyProdLL2000Def.shp";
		fileName = "C:\\data\\geovista_data\\Historical-Demographic\\census\\census80_90_00.shp";

		ShapeFileDataReader shpRead = new ShapeFileDataReader();
		shpRead.setFileName(fileName);
		CoordinationManager coord = new CoordinationManager();
		ShapeFileToShape shpToShape = new ShapeFileToShape();
		ShapeFileProjection shpProj = new ShapeFileProjection();
		GeoData48States stateData = new GeoData48States();
		/*
		String fileName2 = "C:\\data\\grants\\nevac\\crimes\\cri.shp";
		ShapefileReader reader = new ShapefileReader();
		DriverProperties dp = new DriverProperties(fileName2);
		FeatureCollection featColl = null;

		try {
			featColl = reader.read(dp);
		} catch (IllegalParametersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Feature> featList = featColl.getFeatures();
		
		for (Feature feat : featList) {

			Geometry geom = (Geometry) feat.getAttribute(0);
			System.out.println(geom.getClass().getName());

			Java2DConverter converter = new Java2DConverter(new Viewport(app
					.getGlassPane()));
			try {
				Shape shp = converter.toShape(geom);
				System.out.println(shp);

			} catch (NoninvertibleTransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}

		}
		
		
		*/
		
		
		
		
		
		
		//coord.addBean(map2);
		coord.addBean(shpToShape);

		if (useResource) {

			shpProj.setInputDataSetForApps(stateData.getDataForApps());
		} else {
			if (useProj) {
				stateData.addActionListener(shpProj);
				shpProj.setInputDataSet(shpRead.getDataSet());
			}
		}
		Object[] dataSet = null;
		if (useProj) {
			dataSet = shpProj.getOutputDataSet();
		} else {
			dataSet = shpRead.getDataSet();
		}

		shpToShape.setInputDataSet(dataSet);
		// shpToShape.setInputDataSet(dataSet);
		// Rectangle2D rect = new Rectangle2D.Float(-30f,-30f,600f,600f);
		// SpatialExtentEvent ext = new SpatialExtentEvent(map,rect);

		// ShapeFileProjection shpProj2 = new ShapeFileProjection();
		// Projection proj = shpProj.getProj();
		// shpProj2.setProj(proj);
		// shpProj2.setInputAuxiliaryData(stateData.getDataSet());

		// shpToShape2.setInputDataSet(shpProj2.getOutputAuxiliarySpatialData());
		// map2.setAuxiliarySpatialData(shpToShape2.getOutputDataSet());
		ShapeFileToShape shpToShape2 = new ShapeFileToShape();


		// map2.setAuxiliarySpatialData(shpToShape2.getOutputDataSet());

		ShapeFileToShape shpToShape3 = new ShapeFileToShape();
		fileName = "C:\\data\\geovista_data\\shapefiles\\jin\\States.shp";

		ShapeFileDataReader shpRead3 = new ShapeFileDataReader();
		shpRead3.setFileName(fileName);
		// shpToShape3.setInputDataSet(shpRead3.getDataSet());
		shpToShape3.setInputDataSet(stateData.getDataSet());
		map2.setAuxiliarySpatialData(shpToShape3.getOutputDataSetForApps());

		// map2.setDataSet(shpToShape2.getOutputDataSet());

	}

	public void tableChanged(TableModelEvent e) {

		logger.finest("data changed, whooop!");
		int col = e.getColumn();
		logger.finest(col + " is the column...");
		//this.mapCan.setDataSet(dataSet);

		this.visClassOne.setDataSet(dataSet);
		this.visClassTwo.setDataSet(dataSet);

		
		
//		this.visClassOne.setUpdate(false);
//		this.visClassTwo.setUpdate(false);
//		//this.visClassOne.tableChanged(e);
//		// frank here again... lets try something else....
//		ActionEvent e2 = new ActionEvent(this, 0,
//				ClassifierPicker.COMMAND_SELECTED_VARIABLE_CHANGED);
//		visClassOne.actionPerformed(e2);
//		visClassTwo.actionPerformed(e2);
//		//this.visClassTwo.tableChanged(e);
//		this.visClassOne.setUpdate(true);
//		this.visClassTwo.setUpdate(true);
//		
		// so, what do we do?
		// add the new column to the picker, I suppose

	}
}
