/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class VisualClassifier
 Copyright (c), 2004, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: xpdai $
 $Id: VisualClassifier.java,v 1.5 2005/07/28 21:33:51 xpdai Exp $
 $Date: 2005/07/28 21:33:51 $
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

package edu.psu.geovista.visclass;

// import edu.psu.geovista.common.ui.panel.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import edu.psu.geovista.common.classification.Classifier;
import edu.psu.geovista.common.classification.ClassifierPicker;
import edu.psu.geovista.common.color.Palette;
import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.common.event.ColorArrayEvent;
import edu.psu.geovista.common.event.ColorArrayListener;
import edu.psu.geovista.common.event.DataSetEvent;
import edu.psu.geovista.common.event.DataSetListener;
import edu.psu.geovista.common.event.DataSetModifiedEvent;
import edu.psu.geovista.common.event.DataSetModifiedListener;
import edu.psu.geovista.common.event.PaletteEvent;
import edu.psu.geovista.common.event.PaletteListener;
import edu.psu.geovista.symbolization.ColorRampPicker;
import edu.psu.geovista.symbolization.ColorSymbolClassification;
import edu.psu.geovista.symbolization.ColorSymbolClassificationSimple;
import edu.psu.geovista.symbolization.ColorSymbolizer;
import edu.psu.geovista.symbolization.ColorSymbolizerLinear;
import edu.psu.geovista.symbolization.event.ColorClassifierEvent;
import edu.psu.geovista.symbolization.event.ColorClassifierListener;

// import javax.swing.colorchooser.*;

// public class VisualClassifier extends JPanel implements ActionListener {
public class VisualClassifier extends JPanel implements ActionListener,
		ComponentListener, PaletteListener, DataSetListener,
		DataSetModifiedListener, TableModelListener {
	// Jin Chen: for extending purpose, change private to protected for
	// following fields:
	// symbolizationPanel,colors,dataColors,colorerLinear,colorClasser,classPick
	protected edu.psu.geovista.symbolization.ColorRampPicker symbolizationPanel;

	// XXX in future, we want to support much beyond colors.
	protected transient Color[] colors;
	protected transient Color[] dataColors;
	public transient boolean[] anchored;

	private transient double[] data;
	private transient int[] classificationIndex;

	 private int nClasses;
	 private boolean update;
	 private boolean interpolate;
	 private boolean setupFinished;
	 private boolean variableChooserActive = true;

	private transient JCheckBox updateBox;
	private transient JCheckBox interpolateBox;
	public static final String COMMAND_COLORS_CHANGED = "colors";
	public static final String COMMAND_BEAN_REGISTERED = "hi!";
	public static final String COMMAND_CLASSES_CHANGED = "classNumber";

	private int currOrientation = VisualClassifier.X_AXIS;
	public static final int X_AXIS = 0;
	public static final int Y_AXIS = 1;
	public transient boolean orientationInParentIsX = false;

	protected edu.psu.geovista.symbolization.ColorSymbolizerLinear colorerLinear;
	protected edu.psu.geovista.symbolization.ColorSymbolClassificationSimple colorClasser;

	protected ClassifierPicker classPick;

	private transient TexturePaint texPaint;
	private transient int oldIndication = 0;
	final static Logger logger = Logger.getLogger(VisualClassifier.class.getName());

	public VisualClassifier() {
		super();
		this.addComponentListener(this);
		this.colorClasser = new ColorSymbolClassificationSimple();
		this.colorerLinear = new ColorSymbolizerLinear();
		this.setupFinished = false;
		this.nClasses = ColorRampPicker.DEFAULT_NUM_SWATCHES;
		this.update = true;
		this.interpolate = true;

		this.classPick = new ClassifierPicker();
		classPick.addActionListener(this);

		this.makeSymbolizationPanel();
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(classPick);
		if (this.variableChooserActive == true) {
			this
					.setVariableChooserMode(ClassifierPicker.VARIABLE_CHOOSER_MODE_ACTIVE);
		} else {
			this
					.setVariableChooserMode(ClassifierPicker.VARIABLE_CHOOSER_MODE_HIDDEN);
		}

		// this.add(classificationPanel);
		this.add(symbolizationPanel);
		this.setupFinished = true;
		this.makeColors();
		this.setPreferredSize(new Dimension(300, 40));
		/*
		 * int prefHeight = (int)this.classPick.getPreferredSize().getHeight();
		 * prefHeight = prefHeight +
		 * (int)this.symbolizationPanel.getPreferredSize().getHeight(); int
		 * prefWidth = (int)this.classPick.getPreferredSize().getWidth(); if
		 * (prefWidth >
		 * (int)this.symbolizationPanel.getPreferredSize().getWidth()) {
		 * prefWidth =
		 * (int)this.symbolizationPanel.getPreferredSize().getWidth(); //take
		 * the smaller one } this.setPreferredSize(new Dimension(prefWidth,
		 * prefHeight));
		 */
		this.revalidate();
		this.makeTexPaint();
	}

	private void makeTexPaint() {

		int texSize = 4;
		Rectangle2D.Float rect = new Rectangle2D.Float(0, 0, texSize, texSize);
		BufferedImage buff = new BufferedImage(texSize, texSize,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = buff.createGraphics();
		Color trans = new Color(0, 0, 0, 0); // clear black
		g2.setColor(trans);
		g2.fillRect(0, 0, texSize, texSize);
		g2.setColor(Color.blue);
		g2.drawLine(0, 0, 32, 32);

		texPaint = new TexturePaint(buff, rect);

	}

	private void makeSymbolizationPanel() {
		symbolizationPanel = new ColorRampPicker();
		this.add(symbolizationPanel);
		symbolizationPanel.addActionListener(this);
	}

	private void makeColors() {
		if (logger.isLoggable(Level.FINEST)){
			logger.finest("VisClass, making colors...");
		}
		Color[] pickerColors = this.symbolizationPanel.getColors();
		boolean[] pickerAnchors = this.symbolizationPanel.getAnchored();
		// now we interpolate...linearly
		int nPicks = pickerColors.length;
		double picksPerColor = (double) nPicks / (double) this.nClasses;
		if (this.colors == null) {
			this.colors = new Color[nClasses];
		} else if (this.colors.length != nClasses) {
			this.colors = new Color[nClasses];
		}

		for (int i = 0; i < nClasses; i++) {
			double whichColor = (double) i * picksPerColor;
			int index = (int) Math.floor(whichColor);
			logger.finest("i = " + i + "index = " + index);
			this.colors[i] = pickerColors[index];

		}
		if (this.interpolate) {
			// for each lock in the picker, find the class that is closest.
			double colorsPerPick = (double) (this.nClasses - 1)
					/ (double) (nPicks - 1);
			// int[] newAnchors = new int[pickerAnchors.length];
			Vector newAnchors = new Vector();
			for (int i = 0; i < pickerAnchors.length; i++) {
				double whichClass = (double) i * colorsPerPick;
				int aClass = (int) Math.round(whichClass);
				if (pickerAnchors[i]) {
					Integer Ind = new Integer(aClass);
					newAnchors.add(Ind);
				}
			}

			boolean[] colorAnchors = new boolean[nClasses];
			if (newAnchors.size() > 2) {
				for (Enumeration e = newAnchors.elements(); e.hasMoreElements();) {
					Integer ind = (Integer) e.nextElement();
					colorAnchors[ind.intValue()] = true;
				}

				colorAnchors[0] = true;
				colorAnchors[colorAnchors.length - 1] = true;
			} else if (newAnchors.size() == 2) {
				colorAnchors[0] = true;
				colorAnchors[colorAnchors.length - 1] = true;
			} else if (newAnchors.size() == 1) {
				colorAnchors[0] = true;
			}
			// now find those durn colors!
			this.symbolizationPanel.getRamp().rampColors(this.colors,
					colorAnchors);
			this.colorerLinear.setRampingColors(this.colors);
			this.colorerLinear.setAnchors(colorAnchors);
			this.colorClasser.setColorer(this.colorerLinear);
		} // end if interpolate
	}

	public Color[] findDataColors() {
		if (logger.isLoggable(Level.FINEST)){
			logger.finest("VisClass, finding data colors...");
		}
		if (this.classPick.getDataSet() == null) {
			return null;
		}
		int currVar = this.classPick.getCurrVariableIndex();
		Classifier classer = this.classPick.getClasser();
		double[] data = this.classPick.getDataSet().getNumericDataAsDouble(
				currVar);
		int[] classedData = classer.classify(data, classPick.getNClasses());
		if (this.dataColors == null
				|| this.dataColors.length != classedData.length) {
			this.dataColors = new Color[classedData.length];
		}
		this.colors = this.symbolizationPanel.getColors();
		for (int i = 0; i < classedData.length; i++) {
			int aClass = classedData[i];
			if (aClass < 0) {
				aClass = 0; // XXX egregious hack
			}
			Color c = this.colors[aClass];
			this.dataColors[i] = c;
		}
		return this.dataColors;
	}

	/** Listens to the check boxen. */
	class CheckBoxListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource().equals(VisualClassifier.this.updateBox)) {
				if (e.getStateChange() == ItemEvent.SELECTED
						&& VisualClassifier.this.setupFinished) {
					VisualClassifier.this.update = true;
					VisualClassifier.this.makeColors();
					VisualClassifier.this
							.fireActionPerformed(VisualClassifier.COMMAND_COLORS_CHANGED);
					VisualClassifier.this.fireColorClassifierPerformed();
					VisualClassifier.this.fireColorArrayChanged();

				} else if (e.getStateChange() == ItemEvent.DESELECTED) {

					VisualClassifier.this.update = false;
				}
			} else if (e.getSource().equals(
					VisualClassifier.this.interpolateBox)) {
				if (e.getStateChange() == ItemEvent.SELECTED
						&& VisualClassifier.this.setupFinished) {
					VisualClassifier.this.interpolate = true;
					VisualClassifier.this.makeColors();
					VisualClassifier.this
							.fireActionPerformed(VisualClassifier.COMMAND_COLORS_CHANGED);
					VisualClassifier.this.fireColorClassifierPerformed();
					VisualClassifier.this.fireColorArrayChanged();

				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					VisualClassifier.this.interpolate = false;
					VisualClassifier.this.makeColors();
					VisualClassifier.this
							.fireActionPerformed(VisualClassifier.COMMAND_COLORS_CHANGED);
					VisualClassifier.this.fireColorClassifierPerformed();
					VisualClassifier.this.fireColorArrayChanged();
				}
			}
		}
	}

	class SliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			// if (!source.getValueIsAdjusting()) {

			int fps = (int) source.getValue();
			if (fps == 0) {
				// if (!frozen) stopAnimation();
			} else {
				VisualClassifier.this.symbolizationPanel.setNSwatches(fps);
				VisualClassifier.this.nClasses = fps;
				VisualClassifier.this.makeColors();
				VisualClassifier.this
						.fireActionPerformed(VisualClassifier.COMMAND_COLORS_CHANGED);
				VisualClassifier.this.fireColorClassifierPerformed();
				VisualClassifier.this.fireColorArrayChanged();
				VisualClassifier.this
						.fireActionPerformed(VisualClassifier.COMMAND_CLASSES_CHANGED);
			}
			// }
		}
	}

	public ColorRampPicker getSymbolizationPanel() {
		return symbolizationPanel;
	}

	public void setSymbolizationPanel(ColorRampPicker symbolizationPanel) {
		this.symbolizationPanel = symbolizationPanel;
		this.makeColors();
		logger.finest("VC, settingSymbolizationPanel");
	}

	public ColorSymbolClassification getColorSymbolClassification() {
		return this.colorClasser;
	}

	public void setColorSymbolClassification(
			ColorSymbolClassification colorClasser) {
		this.colorClasser = (ColorSymbolClassificationSimple) colorClasser;
	}

	public ColorSymbolizer getColorSymbolizer() {
		logger.finest("VC, gettingColorSymbolizer");
		return this.colorerLinear;
	}

	public void setColorSymbolizer(ColorSymbolizer colorerLinear) {
		this.colorerLinear = (ColorSymbolizerLinear) colorerLinear;
	}

	public Color[] getColorForObservations() {
		return this.findDataColors();
	}

	public Color[] getColors() {
		logger.finest("VC, gettingColors");
		if (this.data == null) {
			System.out
					.println("Warning: VisualClassifier.getColors() called when VC had no data");
			return null;
		}
		this.colors = this.symbolizationPanel.getColors();
		return this.colors;
	}

	public void setColors(Color[] colors) {
		this.colors = colors;
	}

	public int getClassNumber() {
		return this.nClasses;
	}

	public void paletteChanged(PaletteEvent e) {
		Palette pal = e.getPalette();
		int maxColors = pal.getRecommendedMaxLength();
		logger.finest("max colors = " + maxColors);

		// we can't go over the max, or it blows null exceptions
		// so, we interpolate....

		int numClasses = this.classPick.getNClasses();
		Color[] cols = null;
		if (numClasses <= maxColors) {
			cols = pal.getColors(numClasses);
		} else {
			// we have more colors wanted than the pallet can give us
			// todo: make this work
			// this.assignColors
			cols = pal.getColors(numClasses);
		}

		this.setColors(cols);
		this.symbolizationPanel.setColors(cols);
		this.revalidate();
		this.repaint();
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command == ColorRampPicker.COMMAND_SWATCH_COLOR_CHANGED) {
			this.makeColors();
			this.fireActionPerformed(VisualClassifier.COMMAND_COLORS_CHANGED);
			this.fireColorClassifierPerformed();
			VisualClassifier.this.fireColorArrayChanged();
		} else if (command == ClassifierPicker.COMMAND_CLASSES_CHANGED) {
			if (logger.isLoggable(Level.FINEST)){
				logger.finest("VisClass, n classes changed...");
			}
			int nClasses = this.classPick.getNClasses();
			this.symbolizationPanel.setNSwatches(nClasses);
			this.nClasses = nClasses;
			this.fireActionPerformed(VisualClassifier.COMMAND_CLASSES_CHANGED);
			this.makeColors();
			this.fireActionPerformed(VisualClassifier.COMMAND_COLORS_CHANGED);
			this.fireColorClassifierPerformed();
			VisualClassifier.this.fireColorArrayChanged();
		} else if (command == ClassifierPicker.COMMAND_SELECTED_VARIABLE_CHANGED) {
			if (logger.isLoggable(Level.FINEST)){
				logger.finest("VisClass, selected variable changed...");
			}

			this.fireActionPerformed(ClassifierPicker.COMMAND_SELECTED_VARIABLE_CHANGED);
			this.fireColorArrayChanged();
		} else if (command == ClassifierPicker.COMMAND_SELECTED_CLASSIFIER_CHANGED) {
			// this.colorClasser.setColorer(this.colorerLinear);
			this.colorClasser.setClasser(this.classPick.getClasser());
			fireColorClassifierPerformed();
			VisualClassifier.this.fireColorArrayChanged();
		}

		// need to pass this along, if we are a FoldupPanel
		// super.actionPerformed(e);
	}

	// start component event handling
	// note: this class only listens to itself
	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		double pickPrefWidth = this.classPick.getPreferredSize().getWidth();

		int prefWidth = (int) (pickPrefWidth * 1.5);
		if (this.getWidth() >= prefWidth) {
			this.changeOrientation(VisualClassifier.X_AXIS);
		} else {
			this.changeOrientation(VisualClassifier.Y_AXIS);
		}

	}

	// end component event handling

	public void changeOrientation(int orientation) {
		if (orientation == this.currOrientation) {
			return;
		} else if (orientation == VisualClassifier.X_AXIS) {
			Component[] comps = new Component[this.getComponentCount()];
			for (int i = 0; i < this.getComponentCount(); i++) {
				comps[i] = this.getComponent(i);
			}
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			for (int i = 0; i < this.getComponentCount(); i++) {
				this.add(comps[i]);
			}
			this.currOrientation = VisualClassifier.X_AXIS;
			this.setPreferredSize(new Dimension(300, 20));
			this.revalidate();
		} else if (orientation == VisualClassifier.Y_AXIS) {
			Component[] comps = new Component[this.getComponentCount()];
			for (int i = 0; i < this.getComponentCount(); i++) {
				comps[i] = this.getComponent(i);
			}
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			for (int i = 0; i < this.getComponentCount(); i++) {
				this.add(comps[i]);
			}
			this.currOrientation = VisualClassifier.Y_AXIS;
			this.setPreferredSize(new Dimension(300, 40));
			this.revalidate();
		}

	}

	/**
	 * implements ColorArrayListener
	 */
	public void addColorArrayListener(ColorArrayListener l) {
		listenerList.add(ColorArrayListener.class, l);
		// this.fireColorArrayChanged(); //so that if any class registers

		// it gets an event
	}

	/**
	 * removes an ColorArrayListener from the component
	 */
	public void removeColorArrayListener(ColorArrayListener l) {
		listenerList.remove(ColorArrayListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	protected void fireColorArrayChanged() { // change from private to
												// protected by Jin Chen to
												// enable fire the event
												// programmingly
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		ColorArrayEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ColorArrayListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new ColorArrayEvent(this, this.findDataColors());
				}

				((ColorArrayListener) listeners[i + 1]).colorArrayChanged(e);
			}
		} // next i
	}

	// }
	/**
	 * implements ColorClassifierListener
	 */
	public void addColorClassifierListener(ColorClassifierListener l) {
		listenerList.add(ColorClassifierListener.class, l);
		this.fireColorClassifierPerformed(); // so that if any class
												// registers
		// it gets an event
	}

	/**
	 * removes an ColorClassifierListener from the component
	 */
	public void removeColorClassifierListener(ColorClassifierListener l) {
		listenerList.remove(ColorClassifierListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	private void fireColorClassifierPerformed() {
		if (update) {
			// Guaranteed to return a non-null array
			Object[] listeners = listenerList.getListenerList();
			ColorClassifierEvent e = null;
			// Process the listeners last to first, notifying
			// those that are interested in this event
			for (int i = listeners.length - 2; i >= 0; i -= 2) {
				if (listeners[i] == ColorClassifierListener.class) {
					// Lazily create the event:
					if (e == null) {
						e = new ColorClassifierEvent(this, this
								.getColorSymbolClassification());
					}
					if (this.orientationInParentIsX == true) {
						e
								.setOrientation(ColorClassifierEvent.SOURCE_ORIENTATION_X);
					} else {
						e
								.setOrientation(ColorClassifierEvent.SOURCE_ORIENTATION_Y);
					}
					((ColorClassifierListener) listeners[i + 1])
							.colorClassifierChanged(e);
				}
			} // next i
		} // end if
	}

	/**
	 * implements ActionListener
	 */
	public void addActionListener(ActionListener l) {
		listenerList.add(ActionListener.class, l);
		this.fireActionPerformed(VisualClassifier.COMMAND_BEAN_REGISTERED);
	}

	/**
	 * removes an ActionListener from the component
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
	private void fireActionPerformed(String command) {
		if (update) {
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
			} // next i
		} // end if
	}

	public void setIndicatedClass(int indicClass) {
		// clear old indication
		// if it still exists
		if (this.oldIndication < this.symbolizationPanel.getPanSet().length) {
			this.symbolizationPanel.getPanSet()[this.oldIndication]
					.setTexPaint(null);
		}

		if (indicClass < 0) { // used for null or out of range indication
			this.repaint(); // clear old indication
			return;
		}
		if (indicClass >= this.symbolizationPanel.getPanSet().length) {
			this.repaint(); // clear old indication
			return;
		}

		this.symbolizationPanel.getPanSet()[indicClass]
				.setTexPaint(this.texPaint);

		this.oldIndication = indicClass;
		this.repaint();
	}

	public boolean[] getAnchored() {
		return anchored;
	}

	public void setAnchored(boolean[] anchored) {
		this.anchored = anchored;
	}

	public int[] getClassificationIndex() {
		return classificationIndex;
	}

	public void setClassificationIndex(int[] classificationIndex) {
		this.classificationIndex = classificationIndex;
	}

	public ClassifierPicker getClassPick() {
		return classPick;
	}

	public void setClassPick(ClassifierPicker classPick) {
		this.classPick = classPick;
	}

	public ColorSymbolClassificationSimple getColorClasser() {
		return colorClasser;
	}

	public void setColorClasser(ColorSymbolClassificationSimple colorClasser) {
		this.colorClasser = colorClasser;
	}

	public ColorSymbolizerLinear getColorerLinear() {
		return colorerLinear;
	}

	public void setColorerLinear(ColorSymbolizerLinear colorerLinear) {
		this.colorerLinear = colorerLinear;
	}

	public int getCurrOrientation() {
		return currOrientation;
	}

	public void setCurrOrientation(int currOrientation) {
		this.currOrientation = currOrientation;
	}

	public int getVariableChooserMode() {
		return this.classPick.getVariableChooserMode();
	}

	public void setVariableChooserMode(int variableChooserMode) {
		if (this.classPick.getVariableChooserMode() != variableChooserMode) {
			this.classPick.setVariableChooserMode(variableChooserMode);

		}
	}

	public void setCurrVariableIndex(int index) {
		this.classPick.setCurrVariableIndex(index);
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

	public void setDataSet(DataSetForApps data) {
		this.classPick.setDataSet(data);
	}

	public void setAdditionalData(DataSetForApps data) {
		this.classPick.setAdditionalData(data);
	}

	public int getCurrVariableIndex() {
		return this.classPick.getCurrVariableIndex();
	}

	public void setHighColor(Color c) {
		this.symbolizationPanel.setHighColor(c);
	}

	public void setOrientationInParentIsX(boolean orientationInParentIsX) {
		this.orientationInParentIsX = orientationInParentIsX;
	}

	public boolean isVariableChooserActive() {
		return variableChooserActive;
	}

	public void setVariableChooserActive(boolean variableChooserActive) {
		this.variableChooserActive = variableChooserActive;
	}

	public void dataSetChanged(DataSetEvent e) {
		this.setData(e.getDataSet());
	}

	public void dataSetModified(DataSetModifiedEvent e) {
		if (e.getEventType() == DataSetModifiedEvent.TYPE_EXTENDED) {
			// Object[] newData = e.getNewData();
			// DataSetForApps newDataSet = new DataSetForApps(newData);
			this.classPick.dataSetModified(e);
		}
	}

	public void tableChanged(TableModelEvent e) {
	
	
		this.classPick.tableChanged(e);
		this.classPick.setCurrVariableIndex(e.getColumn());

	}

	/**
	 * @return the update
	 */
	public boolean getUpdate() {
		return update;
	}

	/**
	 * @param update the update to set
	 */
	public void setUpdate(boolean update) {
		this.update = update;
	}
}
