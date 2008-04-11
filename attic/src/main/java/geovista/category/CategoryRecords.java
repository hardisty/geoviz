package geovista.category;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import geovista.common.data.DataSetForApps;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.SelectionEvent;
import geovista.common.event.SelectionListener;
import geovista.symbolization.ColorInterpolator;

/**
 * <p>
 * Title: Studio applications
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: GeoVSITA Center
 * </p>
 * 
 * @author Xiping Dai
 * 
 */

public class CategoryRecords extends JPanel implements ChangeListener,
		SelectionListener, DataSetListener {
	protected final static Logger logger = Logger
			.getLogger(CategoryRecords.class.getName());
	private static Color DEFAULT_COLOR = new Color(0, 0, 255);
	private static String DEFAULT_EXAMPLE_TYPE = "ideal";

	private final Color preferedColor = DEFAULT_COLOR;
	private final BorderLayout borderLayout1 = new BorderLayout();
	private final JPanel buttonPanel = new JPanel();
	private final JButton jButton1 = new JButton();
	private final JButton jButton2 = new JButton();
	private final JButton jButton3 = new JButton();
	private JScrollPane jScrollPane1;
	private JTable infoTable;
	private InfoModel tableModel;

	private int[] selection; // one selection record.
	private Integer selectionLen;
	private final Vector selections = new Vector(); // All selection records.
	private Vector selectedRecords = new Vector();
	private Color[] multipleSelectionColors = new Color[4];
	private int numObvs;
	private final Vector oneRecord = new Vector(4);// One record for selection,
													// but with display of
													// selection (# of selected
													// obvs) instead of real
													// selection.
	private Vector categoryRecords = new Vector(); // records for selection or
													// classification events.
	private final EventListenerList listenerList = new EventListenerList();

	public CategoryRecords() {
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		setLayout(borderLayout1);
		jButton1.setText("SAVE");
		jButton2.setText("DELETE");
		jButton3.setText("DISPLAY");

		jButton1.addActionListener(new java.awt.event.ActionListener() {
			/**
			 * put your documentation comment here
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				saveButton_actionPerformed(e);
			}
		});
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			/**
			 * put your documentation comment here
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				delButton_actionPerformed(e);
			}
		});
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			/**
			 * put your documentation comment here
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				displayButton_actionPerformed(e);
			}
		});

		buttonPanel.add(jButton1, null);
		buttonPanel.add(jButton2, null);
		buttonPanel.add(jButton3, null);

		// InfoTableModel tableModel = new InfoTableModel();
		tableModel = new InfoModel();
		infoTable = new JTable(tableModel);

		infoTable.setRowSelectionAllowed(true);
		infoTable
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		// Set up renderer and editor for the Favorite Color column.
		setUpColorRenderer(infoTable);
		setUpColorEditor(infoTable);

		// Set up real input validation for integer data.
		// setUpIntegerEditor(infoTable);

		// Set up column sizes.
		// initColumnSizes(infoTable, tableModel);

		setUpSelectionColumn(infoTable.getColumnModel().getColumn(2));
		// Fiddle with the exampleType column's cell editors/renderers.
		setUpExampleColumn(infoTable.getColumnModel().getColumn(3));

		jScrollPane1 = new JScrollPane(infoTable);

		this.add(jScrollPane1, BorderLayout.CENTER);

		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void setSelection(int[] selection) {
		oneRecord.clear();
		this.selection = selection;
		selectionLen = new Integer(this.selection.length);
		oneRecord.add(System.getProperty("user.name"));
		oneRecord.add(preferedColor);
		oneRecord.add(selectionLen);
		oneRecord.add(CategoryRecords.DEFAULT_EXAMPLE_TYPE);
		selections.addElement(this.selection.clone());
		tableModel.updateRecord(oneRecord);
	}

	public int[] getSelection() {
		logger.finest("getSelection... in categoryRecords");
		return selection;
	}

	public void setSelectedRecords(Vector selectedRecords) {
		this.selectedRecords = selectedRecords;
	}

	public Vector getSelectedRocords() {
		return selectedRecords;
	}

	public void setDataSet(DataSetForApps data) {

		numObvs = data.getNumObservations();
		multipleSelectionColors = new Color[numObvs];
	}

	public void setMultipleSelectionColors(Color[] selectionColors) {
		multipleSelectionColors = selectionColors;
	}

	public Color[] getMultipleSelectionColors() {
		logger.finest("getDisplaySelection... in categoryRecords");
		return multipleSelectionColors;
	}

	public void setCategoryRecords(Vector cateRecords) {
		categoryRecords = cateRecords;
		Vector dataRecords = new Vector();
		// Transfer the selections to the length of each selection for data in
		// table.
		for (int i = 0; i < categoryRecords.size(); i++) {
			dataRecords.add(i, ((Vector) (categoryRecords.elementAt(i)))
					.clone());
			selections.add(((Vector) (categoryRecords.elementAt(i)))
					.elementAt(2));
			((Vector) (dataRecords.elementAt(i))).setElementAt(new Integer(
					(((int[]) selections.elementAt(i)).length)), 2);
		}
		dataRecords.trimToSize();
		tableModel.data = dataRecords;
	}

	public Vector getCategoryRecords() {
		return categoryRecords;
	}

	private void saveButton_actionPerformed(ActionEvent e) {
		// Transfer the data in table to categoryRecords for save.
		// The selection records should be saved in categoryRecords.
		Vector dataRecords = new Vector();
		dataRecords = tableModel.data;
		// this.categoryRecords = (Vector)dataRecords.clone();
		for (int i = 0; i < dataRecords.size(); i++) {
			categoryRecords.add(i, ((Vector) (dataRecords.elementAt(i)))
					.clone());
			((Vector) (categoryRecords.elementAt(i))).setElementAt(selections
					.get(i), 2);
			logger
					.finest("selection i: "
							+ ((int[]) ((Vector) (categoryRecords.elementAt(i)))
									.get(2)).length);
		}
		categoryRecords.trimToSize();
		fireChangeEvent();
	}

	private void delButton_actionPerformed(ActionEvent e) {
		int[] selectedRows = infoTable.getSelectedRows();
		tableModel.deleteRows(selectedRows);
		for (int element : selectedRows) {
			selections.removeElementAt(element);
		}
	}

	private void displayButton_actionPerformed(ActionEvent e) {
		for (int i = 0; i < multipleSelectionColors.length; i++) {
			multipleSelectionColors[i] = null;
		}
		int[] selectedRows = infoTable.getSelectedRows();
		logger.finest("selected rows: " + selectedRows.toString());
		for (int element : selectedRows) {
			selectedRecords.add(tableModel.data.get(element));
			// set up colors for different selections.
			Color selectionColor = (Color) ((Vector) ((tableModel.data
					.get(element)))).get(1);
			int[] currentSelection = (int[]) (selections.get(element));
			for (int j = 0; j < currentSelection.length; j++) {
				if (multipleSelectionColors[currentSelection[j]] == null) {
					multipleSelectionColors[currentSelection[j]] = selectionColor;
					logger.finest("Color null: " + currentSelection[j]
							+ selectionColor.toString());
				} else {
					Color formerColor = multipleSelectionColors[currentSelection[j]];
					Color newColor = ColorInterpolator.mixColorsRGB(
							formerColor, selectionColor);
					multipleSelectionColors[currentSelection[j]] = newColor;
					logger.finest("Color mix: " + currentSelection[j]
							+ newColor.toString());
				}
			}
		}
		fireSelectionChanged(getMultipleSelectionColors());
		fireChangeEvent();
	}

	class ColorRenderer extends JLabel implements TableCellRenderer {
		Border unselectedBorder = null;
		Border selectedBorder = null;
		boolean isBordered = true;

		public ColorRenderer(boolean isBordered) {
			super();
			this.isBordered = isBordered;
			setOpaque(true); // MUST do this for background to show up.
		}

		public Component getTableCellRendererComponent(JTable table,
				Object color, boolean isSelected, boolean hasFocus, int row,
				int column) {
			setBackground((Color) color);
			if (isBordered) {
				if (isSelected) {
					if (selectedBorder == null) {
						selectedBorder = BorderFactory.createMatteBorder(2, 5,
								2, 5, table.getSelectionBackground());
					}
					setBorder(selectedBorder);
				} else {
					if (unselectedBorder == null) {
						unselectedBorder = BorderFactory.createMatteBorder(2,
								5, 2, 5, table.getBackground());
					}
					setBorder(unselectedBorder);
				}
			}
			return this;
		}
	}

	private void setUpColorRenderer(JTable table) {
		table.setDefaultRenderer(Color.class, new ColorRenderer(true));
	}

	// Set up the editor for the Color cells.
	private void setUpColorEditor(JTable table) {

		// First, set up the button that brings up the dialog.
		final JButton button = new JButton("") {
			@Override
			public void setText(String s) {
				// Button never shows text -- only color.
			}
		};
		button.setBackground(Color.white);
		button.setBorderPainted(false);
		button.setMargin(new Insets(0, 0, 0, 0));

		// Now create an editor to encapsulate the button, and
		// set it up as the editor for all Color cells.
		final ColorEditor colorEditor = new ColorEditor(button);
		table.setDefaultEditor(Color.class, colorEditor);

		// Set up the dialog that the button brings up.
		final JColorChooser colorChooser = new JColorChooser();
		ActionListener okListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorEditor.currentColor = colorChooser.getColor();
			}
		};
		final JDialog dialog = JColorChooser.createDialog(button,
				"Pick a Color", true, colorChooser, okListener, null); // XXXDoublecheck
																		// this
																		// is OK

		// Here's the code that brings up the dialog.
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button.setBackground(colorEditor.currentColor);
				colorChooser.setColor(colorEditor.currentColor);
				// Without the following line, the dialog comes up
				// in the middle of the screen.
				// dialog.setLocationRelativeTo(button);
				dialog.setVisible(true);
			}
		});
	}

	/*
	 * The editor button that brings up the dialog. We extend DefaultCellEditor
	 * for convenience, even though it mean we have to create a dummy check box.
	 * Another approach would be to copy the implementation of TableCellEditor
	 * methods from the source code for DefaultCellEditor.
	 */
	class ColorEditor extends DefaultCellEditor {
		Color currentColor = null;

		public ColorEditor(JButton b) {
			super(new JCheckBox()); // Unfortunately, the constructor
			// expects a check box, combo box,
			// or text field.
			editorComponent = b;
			setClickCountToStart(1); // This is usually 1 or 2.

			// Must do this so that editing stops when appropriate.
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
		}

		@Override
		protected void fireEditingStopped() {
			super.fireEditingStopped();
		}

		@Override
		public Object getCellEditorValue() {
			return currentColor;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			((JButton) editorComponent).setText(value.toString());
			currentColor = (Color) value;
			return editorComponent;
		}
	}

	private void setUpSelectionColumn(TableColumn selectionColumn) {
		final JButton selButton = new JButton("selection");

		selButton.setBackground(Color.white);
		selButton.setBorderPainted(false);
		selButton.setMargin(new Insets(0, 0, 0, 0));
		// Now create an editor to encapsulate the button, and
		// set it up as the editor for all selection cells.
		final SelectionEditor selectionEditor = new SelectionEditor(selButton);
		selectionColumn.setCellEditor(selectionEditor);

		// Set up tool tips for the selection cells.
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setToolTipText("Click for selection display");
		selectionColumn.setCellRenderer(renderer);

		selButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selection = (int[]) selections.get(infoTable.getSelectedRow());
				fireChangeEvent();
				fireSelectionChanged(getSelection());
			}
		});
	}

	/*
	 * The editor button that brings up the selection event. We extend
	 * DefaultCellEditor for convenience, even though it mean we have to create
	 * a dummy check box. Another approach would be to copy the implementation
	 * of TableCellEditor methods from the source code for DefaultCellEditor.
	 */
	class SelectionEditor extends DefaultCellEditor {
		int[] selection = null;

		public SelectionEditor(JButton b) {
			super(new JCheckBox()); // Unfortunately, the constructor
			// expects a check box, combo box,
			// or text field.
			editorComponent = b;
			setClickCountToStart(1); // This is usually 1 or 2.

			// Must do this so that editing stops when appropriate.
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
		}

		@Override
		protected void fireEditingStopped() {
			super.fireEditingStopped();
		}

		@Override
		public Object getCellEditorValue() {
			return selection;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			((JButton) editorComponent).setText(value.toString());
			// selection = (int[])value;
			selectionLen = (Integer) value;
			return editorComponent;
		}
	}

	private void setUpExampleColumn(TableColumn exampleColumn) {
		// Set up the editor for the sport cells.
		JComboBox comboBox = new JComboBox();
		comboBox.addItem("Central");
		comboBox.addItem("Boundary");
		comboBox.addItem("Ideal mean");
		comboBox.addItem("Not clear");
		exampleColumn.setCellEditor(new DefaultCellEditor(comboBox));

		// Set up tool tips for the selection cells.
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setToolTipText("Click for combo box");
		exampleColumn.setCellRenderer(renderer);

		// Set up tool tip for the selection column header.
		TableCellRenderer headerRenderer = exampleColumn.getHeaderRenderer();
		if (headerRenderer instanceof DefaultTableCellRenderer) {
			((DefaultTableCellRenderer) headerRenderer)
					.setToolTipText("Click the example to see a list of choices");
		}
	}

	public void stateChanged(ChangeEvent e) {

	}

	/**
	 * put your documentation comment here
	 * 
	 * @param l
	 */
	public void addChangeListener(ChangeListener l) {
		listenerList.add(ChangeListener.class, l);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param l
	 */
	public void removeChangeListener(ChangeListener l) {
		listenerList.remove(ChangeListener.class, l);
	}

	/**
	 * put your documentation comment here
	 */
	private void fireChangeEvent() {
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				((ChangeListener) listeners[i + 1])
						.stateChanged(new ChangeEvent(this));
			}
		} // end for
	}

	// Work with coordinator.
	public void dataSetChanged(DataSetEvent e) {
		setDataSet(e.getDataSetForApps());
	}

	public void selectionChanged(SelectionEvent e) {
		setSelection(e.getSelection());
	}

	public SelectionEvent getSelectionEvent() {
		// XXX not sure if this is the right kind of selection, or the BitSet
		// kind -frank 08
		return new SelectionEvent(this, selection);
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

	private void fireSelectionChanged(Color[] newSelection) {

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