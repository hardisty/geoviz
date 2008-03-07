package geovista.geoviz.scatterplot;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @author
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import geovista.common.classification.ClassifierPicker;
import geovista.common.data.DataSetForApps;
import geovista.common.event.ColorArrayEvent;
import geovista.common.event.ColorArrayListener;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.IndicationEvent;
import geovista.common.event.IndicationListener;
import geovista.common.event.SelectionEvent;
import geovista.common.event.SelectionListener;
import geovista.common.event.VariableSelectionEvent;
import geovista.common.event.VariableSelectionListener;
import geovista.geoviz.visclass.VisualClassifier;
import geovista.symbolization.BivariateColorSchemeVisualizer;
import geovista.symbolization.BivariateColorSymbolClassification;
import geovista.symbolization.BivariateColorSymbolClassificationSimple;
import geovista.symbolization.ColorSymbolClassification;
import geovista.symbolization.event.ColorClassifierEvent;
import geovista.symbolization.event.ColorClassifierListener;

public class SingleScatterPlot extends JPanel implements DataSetListener,
		ActionListener, ColorClassifierListener, SelectionListener,
		IndicationListener, ColorArrayListener, VariableSelectionListener,
		TableModelListener {

	public static final int VARIABLE_CHOOSER_MODE_ACTIVE = 0;
	public static final int VARIABLE_CHOOSER_MODE_FIXED = 1;
	public static final int VARIABLE_CHOOSER_MODE_HIDDEN = 2;
	private final ScatterPlot scatterPlot;
	private final int[] displayIndices = new int[2];
	private final VisualClassifier visClassOne;
	private final VisualClassifier visClassTwo;
	private final JPanel topContent;
	private final BivariateColorSchemeVisualizer biViz;
	transient private Color backgroundColor;
	transient private int[] selections;
	int[] savedSelection;
	transient private Color[] multipleSelectionColors;
	transient private Color[] colorArrays;

	Logger logger = Logger.getLogger(SingleScatterPlot.class.getName());
	DataSetForApps dataSet;

	public SingleScatterPlot() {
		super();

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
		visClassTwo.addActionListener(this);

		vcPanel.add(visClassTwo);
		vcPanel.add(visClassOne);

		JPanel legendPanel = new JPanel();
		legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.X_AXIS));
		biViz = new BivariateColorSchemeVisualizer();
		legendPanel.add(vcPanel);
		legendPanel.add(Box.createRigidArea(new Dimension(4, 2)));
		legendPanel.add(biViz);

		topContent = new JPanel();
		topContent.setLayout(new BoxLayout(topContent, BoxLayout.Y_AXIS));
		legendPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		topContent.add(legendPanel);
		setLayout(new BorderLayout());
		this.add(topContent, BorderLayout.NORTH);
		scatterPlot = new ScatterPlot();
		scatterPlot.setRegressionClass(LinearRegression.class);
		scatterPlot.addActionListener(this);
		this.add(scatterPlot, BorderLayout.CENTER);
		visClassOne.addColorClassifierListener(this);
		visClassTwo.addColorClassifierListener(this);

		visClassTwo.setHighColor(new Color(0, 150, 0));// Jin

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

		visClassOne.setDataSet(data);
		visClassTwo.setDataSet(data);

		// set default data to get color from

		scatterPlot.setBackground(Color.white);
		// XXX is this OK?
		scatterPlot.setRegressionClass(LinearRegression.class);

		scatterPlot.setDataSet(data);

		selections = new int[data.getNumObservations()];

		displayIndices[0] = 1;
		displayIndices[1] = 2;
		setXVariable(displayIndices[0]);
		setYVariable(displayIndices[1]);
		scatterPlot.setAxisOn(true);
		scatterPlot.setElementPosition(displayIndices);
	}

	public void setXVariable(int var) {
		visClassOne.setCurrVariableIndex(var);
	}

	public void setYVariable(int var) {
		visClassTwo.setCurrVariableIndex(var);
	}

	public void setBivarColorClasser(
			BivariateColorSymbolClassification bivarColorClasser) {
		scatterPlot.setBivarColorClasser(bivarColorClasser, false);
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setRegressionClass(Object obj) {
		setRegressionClassName((obj != null) ? obj.getClass().getName() : null);
	}

	public void setRegressionClassName(String classname) {
		scatterPlot.setRegressionClassName(classname);
	}

	public void colorClassifierChanged(ColorClassifierEvent e) {
		if (e.getSource() == visClassOne) {
			e.setOrientation(ColorClassifierEvent.SOURCE_ORIENTATION_X);
		} else if (e.getSource() == visClassTwo) {
			e.setOrientation(ColorClassifierEvent.SOURCE_ORIENTATION_Y);
		}

		biViz.colorClassifierChanged(e);

		if (scatterPlot == null) {
			return;
		}

		ColorSymbolClassification colorSymbolizerX = visClassOne
				.getColorClasser();
		ColorSymbolClassification colorSymbolizerY = visClassTwo
				.getColorClasser();

		BivariateColorSymbolClassificationSimple biColorSymbolizer = new BivariateColorSymbolClassificationSimple();

		biColorSymbolizer.setClasserX(colorSymbolizerX.getClasser());
		biColorSymbolizer.setColorerX(colorSymbolizerX.getColorer());

		biColorSymbolizer.setClasserY(colorSymbolizerY.getClasser());
		biColorSymbolizer.setColorerY(colorSymbolizerY.getColorer());
		scatterPlot.setBivarColorClasser(biColorSymbolizer, false);
	}

	public void dataSetChanged(DataSetEvent e) {
		dataSet = e.getDataSetForApps();
		setDataSet(dataSet);
		dataSet.addTableModelListener(this);

	}

	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		String command = e.getActionCommand();
		String varChangedCommand = ClassifierPicker.COMMAND_SELECTED_VARIABLE_CHANGED;
		String colorChangedCommand = VisualClassifier.COMMAND_COLORS_CHANGED;

		if ((src == visClassOne) && command.equals(varChangedCommand)) {
			int index = visClassOne.getCurrVariableIndex();
			index++;
			displayIndices[0] = index;
			scatterPlot.setElementPosition(displayIndices);
		} else if ((src == visClassTwo) && command.equals(varChangedCommand)) {
			int index = visClassTwo.getCurrVariableIndex();
			index++;
			displayIndices[1] = index;
			scatterPlot.setElementPosition(displayIndices);
		} else if ((src == scatterPlot)
				&& command.compareTo(ScatterPlot.COMMAND_POINT_SELECTED) == 0) {
			selections = scatterPlot.getSelections();
			fireSelectionChanged(getSelectedObvs());
		} else if (command.equals(colorChangedCommand)) {
			fireColorArrayChanged();
		}
	}

	public Color[] getColors() {
		return colorArrays;
	}

	/**
	 * adds an IndicationListener to the button
	 */
	public void addIndicationListener(IndicationListener l) {
		scatterPlot.addIndicationListener(l);
	}

	/**
	 * removes an IndicationListener from the button
	 */
	public void removeIndicationListener(IndicationListener l) {
		scatterPlot.removeIndicationListener(l);
	}

	// /**
	// * implements ColorArrayListener
	// */
	// public void addColorArrayListener(ColorArrayListener l) {
	// listenerList.add(ColorArrayListener.class, l);
	// this.fireColorArrayChanged(); // so that if any class registers
	// }

	// /**
	// * removes an ColorArrayListener from the component
	// */
	// public void removeColorArrayListener(ColorArrayListener l) {
	// listenerList.remove(ColorArrayListener.class, l);
	// }

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	private void fireColorArrayChanged() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		ColorArrayEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ColorArrayListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new ColorArrayEvent(this, getColors());
				}
				((ColorArrayListener) listeners[i + 1]).colorArrayChanged(e);
			}
		} // next i
	}

	public void setSelectedObvs(int[] selected) {

		if (selected == null) {
			return;
		} else {
			for (int i = 0; i < selections.length; i++) {
				selections[i] = 0;
			}
			for (int i = 0; i < selected.length; i++) {
				selections[selected[i]] = 1;
			}
		}
		multipleSelectionColors = null;
		scatterPlot.setSelections(selections);
		repaint();
	}

	/**
	 * Return index array for selected observations.
	 * 
	 * @return
	 */
	public int[] getSelectedObvs() {
		// need to transform data length long int[] to short int[]
		Vector selectedObvs = new Vector();
		for (int i = 0; i < selections.length; i++) {
			if (selections[i] == 1) {
				selectedObvs.add(new Integer(i));
			}
		}
		int[] selectedShortArray = new int[selectedObvs.size()];
		for (int i = 0; i < selectedObvs.size(); i++) {
			selectedShortArray[i] = ((Integer) selectedObvs.get(i)).intValue();
		}
		return selectedShortArray;
	}

	public void setMultipleSelectionColors(Color[] multipleSelectionColors) {
		this.multipleSelectionColors = multipleSelectionColors;
		scatterPlot.setMultipleSelectionColors(this.multipleSelectionColors);
	}

	public void selectionChanged(SelectionEvent e) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("sending to selectionchanged");
		}
		if (e.getMultipleSlectionColors() != null) {
			setMultipleSelectionColors(e.getMultipleSlectionColors());
		} else {
			setSelectedObvs(e.getSelection());

			scatterPlot.repaint();
		}
		savedSelection = e.getSelection();
	}

	public SelectionEvent getSelectionEvent() {
		return new SelectionEvent(this, savedSelection);
	}

	/**
	 * adds an SelectionListener.
	 * 
	 * @see EventListenerList
	 */
	public void addSelectionListener(SelectionListener l) {
		listenerList.add(SelectionListener.class, l);
	}

	/**
	 * removes an SelectionListener from the component.
	 * 
	 * @see EventListenerList
	 */
	public void removeSelectionListener(SelectionListener l) {
		listenerList.remove(SelectionListener.class, l);

	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	protected void fireSelectionChanged(int[] newSelection) {
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
		}// next i

	}

	// /**
	// * implements ColorClassifierListener
	// */
	// public void addColorClassifierListener(ColorClassifierListener l) {
	// this.visClassOne.addColorClassifierListener(l);
	// this.visClassTwo.addColorClassifierListener(l);
	//
	// }

	// /**
	// * removes an ColorClassifierListener from the component
	// */
	// public void removeColorClassifierListener(ColorClassifierListener l) {
	// this.visClassOne.removeColorClassifierListener(l);
	// this.visClassTwo.removeColorClassifierListener(l);
	// }

	public void indicationChanged(IndicationEvent e) {
		scatterPlot.indicationChanged(e);
	}

	public void colorArrayChanged(ColorArrayEvent e) {
		Color[] colors = e.getColors();
		scatterPlot.setColorArrayForObs(colors);
	}

	public void variableSelectionChanged(VariableSelectionEvent e) {
		visClassOne.setCurrVariableIndex(e.getVariableIndex() + 1);
	}

	public void tableChanged(TableModelEvent e) {
		visClassOne.setDataSet(dataSet);
		visClassTwo.setDataSet(dataSet);

	}

}
