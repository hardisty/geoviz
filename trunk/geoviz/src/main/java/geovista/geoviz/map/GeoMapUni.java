/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class GeoMapUni
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: GeoMapUni.java,v 1.12 2005/08/12 17:25:21 hardisty Exp $
 $Date: 2005/08/12 17:25:21 $
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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import geovista.common.classification.ClassifierPicker;
import geovista.common.data.DataSetForApps;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.IndicationEvent;
import geovista.common.event.IndicationListener;
import geovista.common.event.PaletteEvent;
import geovista.common.event.PaletteListener;
import geovista.common.event.SelectionEvent;
import geovista.common.event.SelectionListener;
import geovista.common.event.SpatialExtentEvent;
import geovista.common.event.SpatialExtentListener;
import geovista.geoviz.scatterplot.Histogram;
import geovista.geoviz.visclass.VisualClassifier;
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
public class GeoMapUni extends JPanel
		implements
		// MouseListener, MouseMotionListener,
		ActionListener, SelectionListener, IndicationListener, DataSetListener,
		ColorClassifierListener, SpatialExtentListener, PaletteListener,
		TableModelListener {
	public static final int VARIABLE_CHOOSER_MODE_ACTIVE = 0;
	public static final int VARIABLE_CHOOSER_MODE_FIXED = 1;
	public static final int VARIABLE_CHOOSER_MODE_HIDDEN = 2;
	private final MapCanvas mapCan;
	private final VisualClassifier visClassOne;
	private final Histogram histo;
	// transient private VisualClassifier visClassTwo;
	transient private JToolBar mapTools;
	private final JPanel topContent;
	private final GeoCursors cursors;
	private JPanel vcPanel = new JPanel();
	transient private DataSetForApps dataSet;

	public GeoMapUni() {
		super();

		vcPanel = new JPanel();
		vcPanel.setPreferredSize(new Dimension(600, 40));
		vcPanel.setMaximumSize(new Dimension(600, 40));
		vcPanel.setLayout(new BoxLayout(vcPanel, BoxLayout.Y_AXIS));
		visClassOne = new VisualClassifier();
		// visClassTwo = new VisualClassifier();
		visClassOne.setAlignmentX(Component.LEFT_ALIGNMENT);
		// visClassTwo.setAlignmentX(Component.LEFT_ALIGNMENT);
		visClassOne
				.setVariableChooserMode(ClassifierPicker.VARIABLE_CHOOSER_MODE_ACTIVE);
		// visClassTwo.setVariableChooserMode(
		// ClassifierPicker.VARIABLE_CHOOSER_MODE_ACTIVE);
		visClassOne.addActionListener(this);
		// visClassTwo.addActionListener(this);
		vcPanel.add(visClassOne);
		// vcPanel.add(visClassTwo);

		JPanel legendPanel = new JPanel();
		legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.X_AXIS));
		legendPanel.add(vcPanel);
		legendPanel.add(Box.createRigidArea(new Dimension(4, 2)));

		histo = new Histogram();
		histo.setAxisOn(false);
		Dimension histDim = new Dimension(70, 70);
		histo.setPreferredSize(histDim);

		legendPanel.add(histo);

		topContent = new JPanel();
		topContent.setLayout(new BoxLayout(topContent, BoxLayout.Y_AXIS));
		makeToolbar();
		legendPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mapTools.setAlignmentX(Component.LEFT_ALIGNMENT);
		topContent.add(legendPanel);
		topContent.add(mapTools);

		// note: uncomment the line below for animation panel stuff
		// vcPanel.add(this.makeAnimationPanel());
		cursors = new GeoCursors();
		setCursor(cursors.getCursor(GeoCursors.CURSOR_ARROW_SELECT));

		setLayout(new BorderLayout());
		this.add(topContent, BorderLayout.NORTH);
		mapCan = new MapCanvas();
		this.add(mapCan, BorderLayout.CENTER);
		mapCan.addIndicationListener(this);
		addIndicationListener(histo);
		addSelectionListener(histo);
		histo.addSelectionListener(this);
		visClassOne.addColorClassifierListener(this);
		// visClassTwo.addColorClassifierListener(this);

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
		Class cl = GeoMapUni.class;
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
				GeoMapUni.this.setCursor(cursors
						.getCursor(GeoCursors.CURSOR_ARROW_SELECT));
				mapCan.setMode(MapCanvas.MODE_SELECT);
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
				GeoMapUni.this.setCursor(cursors
						.getCursor(GeoCursors.CURSOR_ARROW_ZOOM_IN));
				mapCan.setMode(MapCanvas.MODE_ZOOM_IN);

				// GeoMapUni.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
				mapCan.setMode(MapCanvas.MODE_ZOOM_OUT);
				GeoMapUni.this.setCursor(cursors
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
				mapCan.zoomFullExtent();
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
				GeoMapUni.this.setCursor(cursors
						.getCursor(GeoCursors.CURSOR_ARROW_PAN));
				mapCan.setMode(MapCanvas.MODE_PAN);
			}
		});
		mapTools.add(button);
	}

	public VisualClassifier getVisClassOne() {
		return visClassOne;
	}

	public JPanel getVcPanel() {
		return vcPanel;
	}

	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		String command = e.getActionCommand();
		String varChangedCommand = ClassifierPicker.COMMAND_SELECTED_VARIABLE_CHANGED;

		if ((src == visClassOne) && command.equals(varChangedCommand)) {
			int index = visClassOne.getCurrVariableIndex();
			// index++;//no longer, data set for apps has changed
			mapCan.setCurrColorColumnX(index);
			mapCan.setCurrColorColumnY(index);
			histo.setData(dataSet.getNumericDataAsDouble(index));
			this.firePropertyChange("SelectedVariable", index, index);
		}

	}

	public void setSelectedVariable(int var) {
		// me so crazy -- frank
		visClassOne.getClassPick().getVariableCombo().setSelectedIndex(var);
	}

	public void selectionChanged(SelectionEvent e) {
		mapCan.selectionChanged(e);
	}

	public SelectionEvent getSelectionEvent() {
		return new SelectionEvent(this, mapCan.getSelections());
	}

	public void indicationChanged(IndicationEvent e) {
		Object source = e.getSource();

		if ((source == mapCan) && (e.getIndication() >= 0)) {
			visClassOne.setIndicatedClass(e.getXClass());
			// this.visClass.setIndicatedClass(e.getYClass());

			// this.fireIndicationChanged(e.getIndication());
		} else if ((source == mapCan) && (e.getIndication() < 0)) {
			visClassOne.setIndicatedClass(-1);
			// this.visClassTwo.setIndicatedClass(-1);

			// this.fireIndicationChanged(e.getIndication());
		} else {
			mapCan.indicationChanged(e);
		}
	}

	SpatialExtentEvent savedEvent;

	public SpatialExtentEvent getSpatialExtentEvent() {
		return savedEvent;
	}

	public void spatialExtentChanged(SpatialExtentEvent e) {
		mapCan.spatialExtentChanged(e);
		savedEvent = e;
	}

	public void zoomFullExtent() {
		mapCan.zoomFullExtent();
	}

	public void dataSetChanged(DataSetEvent e) {
		// mapCan.dataSetChanged(e);
		setDataSet(e.getDataSetForApps());
		dataSet = e.getDataSetForApps();
		dataSet.addTableModelListener(this);
		histo.dataSetChanged(e);
	}

	public void paletteChanged(PaletteEvent e) {
		visClassOne.paletteChanged(e);

	}

	public void colorClassifierChanged(ColorClassifierEvent e) {
		if (e.getSource() == visClassOne) {
			e.setOrientation(ColorClassifierEvent.SOURCE_ORIENTATION_X);
		}
		if (mapCan == null) {
			return;
		}
		ColorSymbolClassification colorSymbolizerX = visClassOne
				.getColorClasser();
		ColorSymbolClassification colorSymbolizerY = visClassOne
				.getColorClasser();

		BivariateColorSymbolClassificationSimple biColorSymbolizer = new BivariateColorSymbolClassificationSimple();

		biColorSymbolizer.setClasserX(colorSymbolizerX.getClasser());
		biColorSymbolizer.setColorerX(colorSymbolizerX.getColorer());

		biColorSymbolizer.setClasserY(colorSymbolizerY.getClasser());
		biColorSymbolizer.setColorerY(colorSymbolizerY.getColorer());
		mapCan.setBivarColorClasser(biColorSymbolizer);
	}

	public void setXVariable(int var) {
		visClassOne.setCurrVariableIndex(var);
	}

	public int getCurrentVariable() {
		return visClassOne.getCurrVariableIndex();
	}

	public void setYVariable(int var) {
		visClassOne.setCurrVariableIndex(var);
	}

	public void setXChooserMode(int chooserMode) {
		visClassOne.setVariableChooserMode(chooserMode);
	}

	public void setYChooserMode(int chooserMode) {
		visClassOne.setVariableChooserMode(chooserMode);
	}

	public void setBivarColorClasser(
			BivariateColorSymbolClassification bivarColorClasser) {
		mapCan.setBivarColorClasser(bivarColorClasser);
	}

	public void setSelectedObservations(int[] selObs) {
		mapCan.setSelectedObservationsInt(selObs);
	}

	public int[] getSelectedObservations() {
		return mapCan.getSelectedObservationsInt();
	}

	public Color[] getColors() {
		return mapCan.getColors();
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
		setDataSet(new DataSetForApps(data));

	}

	public void setDataSet(DataSetForApps data) {
		mapCan.setDataSet(data);
		visClassOne.setDataSet(data);

		int numNumeric = data.getNumberNumericAttributes();

		int currColorColumnX = mapCan.getCurrColorColumnX();

		if (currColorColumnX < 0) {
			if (numNumeric > 0) {
				setXVariable(0);
				setYVariable(0);
				mapCan.setCurrColorColumnX(0);
				mapCan.setCurrColorColumnY(0);
			}
		} else if (currColorColumnX < numNumeric) {
			setXVariable(currColorColumnX);
			setYVariable(currColorColumnX);
			mapCan.setCurrColorColumnX(currColorColumnX);
			mapCan.setCurrColorColumnY(currColorColumnX);
		}

	}

	public void setAuxiliarySpatialData(DataSetForApps data) {

		mapCan.setAuxiliarySpatialData(data);
	}

	@Override
	public void setBackground(Color bg) {
		if ((mapCan != null) && (bg != null)) {
			mapCan.setBackground(bg);
		}
	}

	/**
	 * adds an IndicationListener
	 */
	public void addIndicationListener(IndicationListener l) {
		mapCan.addIndicationListener(l);
	}

	/**
	 * removes an IndicationListener from the component
	 */
	public void removeIndicationListener(IndicationListener l) {
		mapCan.removeIndicationListener(l);
	}

	/**
	 * adds an SelectionListener
	 */
	public void addSelectionListener(SelectionListener l) {
		mapCan.addSelectionListener(l);
		histo.addSelectionListener(l);
	}

	/**
	 * removes an SelectionListener from the component
	 */
	public void removeSelectionListener(SelectionListener l) {
		mapCan.removeSelectionListener(l);
	}

	/**
	 * adds an SpatialExtentListener
	 */
	public void addSpatialExtentListener(SpatialExtentListener l) {
		mapCan.addSpatialExtentListener(l);
	}

	/**
	 * removes an SpatialExtentListener from the component
	 */
	public void removeSpatialExtentListener(SpatialExtentListener l) {
		listenerList.remove(SpatialExtentListener.class, l);
		mapCan.removeSpatialExtentListener(l);
	}

	public void tableChanged(TableModelEvent e) {
		visClassOne.setDataSet(dataSet);

	}

}
