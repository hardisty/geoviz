/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class SpaceFill
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: SpaceFill.java,v 1.4 2005/03/24 20:34:24 hardisty Exp $
 $Date: 2005/03/24 20:34:24 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *

 */

package geovista.geoviz.spacefill;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import edu.psu.geovista.symbolization.BivariateColorSymbolClassification;
import geovista.common.data.DataSetForApps;
import geovista.common.event.ConditioningEvent;
import geovista.common.event.ConditioningListener;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.IndicationEvent;
import geovista.common.event.IndicationListener;
import geovista.common.event.SelectionEvent;
import geovista.common.event.SelectionListener;
import geovista.common.ui.panel.FoldupPanel;
import geovista.geoviz.visclass.VisualClassifier;

public class SpaceFill extends JPanel implements ActionListener, Serializable,
		SelectionListener, IndicationListener, DataSetListener,
		ConditioningListener {

	private SpaceFillCanvas spat;
	private VisualClassifier vc;

	private String[] variableNames;


	private JPanel topPane;
	private FoldupPanel allTop;
	private JComboBox colorColumnCombo;
	private JComboBox orderColumnCombo;
	private JComboBox fillOrderCombo;
	private int fillOrder;
	private boolean useDrawingShapes;

	private transient String currColorName;
	private transient String currOrderName;

	public SpaceFill() {
		this.useDrawingShapes = true;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setLayout(new BorderLayout());
		this.allTop = new FoldupPanel();
		this.allTop.getContentPanel().setLayout(
				new BoxLayout(this.allTop.getContentPanel(), BoxLayout.Y_AXIS));
		this.topPane = this.makeTopPanel();
		this.topPane.setPreferredSize(new Dimension(200, 30));
		vc = new VisualClassifier();
		this.allTop.getContentPanel().add(vc);

		vc.addActionListener(this);

		this.allTop.setPreferredSize(new Dimension(200, 50));
		this.allTop.getContentPanel().add(topPane);
		this.add(allTop, BorderLayout.NORTH);
		this.spat = makeSpat();
		spat.addActionListener(this);
		// spat.setPreferredSize(new Dimension(2000,2000));
		spat.setMaximumSize(new Dimension(20000, 20000));
		this.add(spat, BorderLayout.CENTER);
		Color[] colors = this.vc.getColors();
		this.spat.setColors(colors);

		this.spat.setColorSymbolizer(this.vc.getColorSymbolClassification());
		this.setPreferredSize(new Dimension(400, 400));

		// orders to implement (in order): scan, bostrophedon, spiral, Morton,
		// Peano
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals(VisualClassifier.COMMAND_COLORS_CHANGED)) {
			this.spat
					.setColorSymbolizer(this.vc.getColorSymbolClassification());
			this
					.fireActionPerformed(SpaceFillCanvas.COMMAND_COLOR_CLASSFICIATION);
			// this.spat.setColors(colors);
		}
		if (command.equals( SpaceFillCanvas.COMMAND_SELECTION)
				&& e.getSource() == this.spat) {
			// pass it along
			this.fireActionPerformed(SpaceFillCanvas.COMMAND_SELECTION);
			this.fireSelectionChanged(this.spat.getSelectedObservationsInt());
		}

	}

	private JPanel makeTopPanel() {

		JPanel topPanel = new JPanel();
		colorColumnCombo = new JComboBox();
		colorColumnCombo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				if (cb.getItemCount() > 0) {
					String arrayName = (String) cb.getSelectedItem();

					if (arrayName.equals(SpaceFill.this.currColorName) == false) {
						SpaceFill.this.currColorName = arrayName;
						int index = cb.getSelectedIndex();
						SpaceFill.this.setCurrColorColumn(index + 1); // skip
																		// header
																		// + 1
					}// end if
				}// end if count > 0
			}// end inner class
		});// end add listener
		JLabel labelColor = new JLabel("Color by:");

		topPanel.add(labelColor, BorderLayout.NORTH);
		topPanel.add(colorColumnCombo, BorderLayout.NORTH);
		JLabel someSpace = new JLabel("    ");
		topPanel.add(someSpace, BorderLayout.NORTH);

		orderColumnCombo = new JComboBox();
		JLabel labelOrder = new JLabel("Order by:");
		orderColumnCombo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				if (cb.getItemCount() > 0) {
					String arrayName = (String) cb.getSelectedItem();

					if (arrayName.equals(SpaceFill.this.currOrderName) == false) {
						SpaceFill.this.currOrderName = arrayName;
						int index = cb.getSelectedIndex();
						SpaceFill.this.setCurrOrderColumn(index + 1);// skip
																		// header
					}// end if
				}// end if count > 0
			}// end inner class
		});// end add listener
		topPanel.add(labelOrder, BorderLayout.EAST);
		topPanel.add(orderColumnCombo, BorderLayout.EAST);

		fillOrderCombo = new JComboBox();
		JLabel labelFill = new JLabel("Fill Order:");
		String[] desc = FillOrder.findFillOrderDescriptions();
		for (int i = 0; i < desc.length; i++) {
			fillOrderCombo.addItem(desc[i]);
		}
		fillOrderCombo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				if (cb.getItemCount() > 0) {
					int fillOrd = cb.getSelectedIndex();
					// if (SpaceFill.this.fillOrder != fillOrd){
					SpaceFill.this.setFillOrder(fillOrd);
					// }//end if
				}// end if count > 0
			}// end inner class
		});// end add listener
		topPanel.add(labelFill, BorderLayout.EAST);
		topPanel.add(fillOrderCombo, BorderLayout.EAST);

		JCheckBox useShapesCB = new JCheckBox("Use Shapes?", true);
		useShapesCB.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					SpaceFill.this.setUseDrawingShapes(true);
				} else {
					SpaceFill.this.setUseDrawingShapes(false);
				}
			}// end itemStateChanged

		});// end add listener
		topPanel.add(useShapesCB, BorderLayout.EAST);

		topPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		return topPanel;
	}// end method

	private SpaceFillCanvas makeSpat() {
		SpaceFillCanvas spat = new SpaceFillCanvas();
		// spat.setPreferredSize(new Dimension(400,350));
		return spat;
	}

	
	  /**
	   * @param data
	   * 
	   * This method is deprecated becuase it wants to create its very own pet
	   * DataSetForApps. This is no longer allowed, to allow for a mutable, 
	   * common data set. Use of this method may lead to unexpected
	   * program behavoir. 
	   * Please use setDataSet instead.
	   */
	  @Deprecated
	  public void setData(Object[] data) {
		 this.setDataSet(new DataSetForApps(data));
	    
	  }	
	
	
	// start accessors
	public void setDataSet(DataSetForApps dataSet) {

		this.setVariableNames(dataSet.getAttributeNamesNumeric());
		this.spat.setDataSet(dataSet);
		if (dataSet.getNumberNumericAttributes() > 2) {
			this.colorColumnCombo.setSelectedIndex(1);
			this.orderColumnCombo.setSelectedIndex(0);
		}

		this.spat.setObservationNames(dataSet.getObservationNames());
	}

	public Object[] getData() {
		return this.spat.getData();
	}

	public void setCurrOrderColumn(int currOrderColumn) {
		this.orderColumnCombo.setSelectedIndex(currOrderColumn - 1);

		this.spat.setCurrOrderColumn(currOrderColumn);
	}

	public int getCurrOrderColumn() {
		return this.spat.getCurrOrderColumn();
	}

	public void setCurrColorColumn(int currColorColumn) {
		this.colorColumnCombo.setSelectedIndex(currColorColumn - 1);
		this.spat.setCurrColorColumn(currColorColumn);
	}

	public int getCurrColorColumn() {
		return this.spat.getCurrColorColumn();
	}

	public void setColorSelection(Color colorSelection) {
		this.spat.setColorSelection(colorSelection);
	}

	public Color getColorSelection() {
		return this.spat.getColorSelection();
	}

	public void setColorIndication(Color colorIndication) {
		this.spat.setColorIndication(colorIndication);
	}

	public Color getColorIndication() {
		return this.spat.getColorIndication();
	}

	public void setColorNull(Color colorNull) {
		this.spat.setColorNull(colorNull);
	}

	public Color getColorNull() {
		return this.spat.getColorNull();
	}

	public void setColorOutOfFocus(Color colorOutOfFocus) {
		this.spat.setColorOutOfFocus(colorOutOfFocus);
	}

	public Color getColorOutOfFocus() {
		return this.spat.getColorOutOfFocus();
	}

	public void setColorNotInStudyArea(Color colorNotInStudyArea) {
		this.spat.setColorNotInStudyArea(colorNotInStudyArea);
	}

	public Color getColorNotInStudyArea() {
		return this.spat.getColorNotInStudyArea();
	}

	public void setVariableNames(String[] variableNames) {

		this.variableNames = variableNames;
		this.spat.setVariableNames(variableNames);
		this.colorColumnCombo.removeAllItems();
		this.orderColumnCombo.removeAllItems();
		for (int i = 0; i < variableNames.length; i++) {
			this.colorColumnCombo.addItem(variableNames[i]);
			this.orderColumnCombo.addItem(variableNames[i]);
		}
	}

	public String[] getVariableNames() {
		return this.variableNames;
	}

	public void setSelectedObservations(Vector selectedObservations) {
		this.spat.setSelectedObservations(selectedObservations);
	}

	public Vector getSelectedObservations() {
		return this.spat.getSelectedObservations();
	}

	public void setSelectedObservationsInt(int[] selectedObservations) {
		this.spat.setSelectedObservationsInt(selectedObservations);
	}

	public void selectionChanged(SelectionEvent e) {
		int[] sel = e.getSelection();
		this.setSelectedObservationsInt(sel);
	}

	public void indicationChanged(IndicationEvent e) {
		int indic = e.getIndication();
		if (indic < 0) {
			return;
		}
		this.spat.setIndication(indic);
	}

	public void dataSetChanged(DataSetEvent e) {
		this.setData(e.getDataSet());
	}

	public void conditioningChanged(ConditioningEvent e) {
		this.spat.setConditionArray(e.getConditioning());
	}

	public int[] getSelectedObservationsInt() {
		return this.spat.getSelectedObservationsInt();
	}

	public void setTopPane(FoldupPanel topPane) {
		this.topPane = topPane;
	}

	public JPanel getTopPane() {
		return this.topPane;
	}

	public void setColorColumnCombo(JComboBox colorColumnCombo) {
		this.colorColumnCombo = colorColumnCombo;
	}

	public JComboBox getColorColumnCombo() {
		return this.colorColumnCombo;
	}

	public void setOrderColumnCombo(JComboBox orderColumnCombo) {
		this.orderColumnCombo = orderColumnCombo;
	}

	public JComboBox getOrderColumnCombo() {
		return this.orderColumnCombo;
	}

	public void setFillOrder(int fillOrder) {
		if (this.fillOrder != fillOrder) {
			if (fillOrder > FillOrder.FILL_ORDER_MAX || fillOrder < 0) {
				throw new IllegalArgumentException(
						"Fill order outside legal range defined in FillOrder");
			} else {
				this.fillOrder = fillOrder;
				this.spat.setFillOrder(fillOrder);
			}
		}//
	}// end method

	public int getFillOrder() {
		return this.fillOrder;
	}

	public void setUseDrawingShapes(boolean useDrawingShapes) {
		this.useDrawingShapes = useDrawingShapes;
		this.spat.setUseDrawingShapes(useDrawingShapes);
	}

	public boolean getUseDrawingShapes() {
		return this.useDrawingShapes;
	}

	public BivariateColorSymbolClassification getBivarColorClasser() {
		return this.spat.getBivarColorClasser();
	}

	// end accessors

	/**
	 * adds an ActionListener to the component
	 */
	public void addActionListener(ActionListener l) {
		listenerList.add(ActionListener.class, l);
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
	public void fireActionPerformed(String command) {
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
	 * adds an SelectionListener
	 */
	public void addSelectionListener(SelectionListener l) {
		listenerList.add(SelectionListener.class, l);
	}

	/**
	 * removes an SelectionListener from the component
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
		}// next i

	}

}
