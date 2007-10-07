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

import edu.psu.geovista.common.event.DataSetEvent;
import edu.psu.geovista.common.event.DataSetListener;
import edu.psu.geovista.common.event.SelectionEvent;
import edu.psu.geovista.common.event.SelectionListener;
import edu.psu.geovista.symbolization.ColorInterpolator;
import geovista.common.data.DataSetForApps;

/**
 * <p>Title: Studio applications</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GeoVSITA Center</p>
 * @author Xiping Dai
 * @version 1.0
 */

public class CategoryRecords extends JPanel implements ChangeListener,  SelectionListener, DataSetListener{
	protected final static Logger logger = Logger.getLogger(CategoryRecords.class.getName());
  private static Color DEFAULT_COLOR = new Color(0, 0, 255);
  private static String DEFAULT_EXAMPLE_TYPE = "ideal";

  private Color preferedColor = DEFAULT_COLOR;
  private BorderLayout borderLayout1 = new BorderLayout();
  private JPanel buttonPanel = new JPanel();
  private JButton jButton1 = new JButton();
  private JButton jButton2 = new JButton();
  private JButton jButton3 = new JButton();
  private JScrollPane jScrollPane1;
  private JTable infoTable;
  private InfoModel tableModel;

  private int[] selection; //one selection record.
  private Integer selectionLen;
  private Vector selections = new Vector(); //All selection records.
  private Vector selectedRecords = new Vector();
  private Color[] multipleSelectionColors = new Color[4];
  private int numObvs;
  private Vector oneRecord = new Vector(4);//One record for selection, but with display of selection (# of selected obvs) instead of real selection.
  private Vector categoryRecords = new Vector(); //records for selection or classification events.
private EventListenerList listenerList = new EventListenerList();

  public CategoryRecords()
  {
    try
    {
      jbInit();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }
  void jbInit() throws Exception
  {
    this.setLayout(borderLayout1);
    jButton1.setText("SAVE");
    jButton2.setText("DELETE");
    jButton3.setText("DISPLAY");

		jButton1.addActionListener(new java.awt.event.ActionListener() {
                /**
                 * put your documentation comment here
                 * @param e
                 */
		public void actionPerformed (ActionEvent e) {
				saveButton_actionPerformed(e);
			}
		});
		jButton2.addActionListener(new java.awt.event.ActionListener() {
                /**
                 * put your documentation comment here
                 * @param e
                 */
		public void actionPerformed (ActionEvent e) {
				delButton_actionPerformed(e);
			}
		});
		jButton3.addActionListener(new java.awt.event.ActionListener() {
                /**
                 * put your documentation comment here
                 * @param e
                 */
		public void actionPerformed (ActionEvent e) {
				displayButton_actionPerformed(e);
			}
		});

    buttonPanel.add(jButton1, null);
    buttonPanel.add(jButton2, null);
	buttonPanel.add(jButton3, null);

    //InfoTableModel tableModel = new InfoTableModel();
	tableModel = new InfoModel();
    infoTable = new JTable(tableModel);

    infoTable.setRowSelectionAllowed(true);
	infoTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    //Set up renderer and editor for the Favorite Color column.
    setUpColorRenderer(infoTable);
    setUpColorEditor(infoTable);

    //Set up real input validation for integer data.
    //setUpIntegerEditor(infoTable);

    //Set up column sizes.
    //initColumnSizes(infoTable, tableModel);

    this.setUpSelectionColumn(infoTable.getColumnModel().getColumn(2));
	//Fiddle with the exampleType column's cell editors/renderers.
    setUpExampleColumn(infoTable.getColumnModel().getColumn(3));

    jScrollPane1 = new JScrollPane(infoTable);

    this.add(jScrollPane1, BorderLayout.CENTER);

    this.add(buttonPanel, BorderLayout.SOUTH);
  }

	public void setSelection(int[] selection){
	    this.oneRecord.clear();
		this.selection = selection;
		this.selectionLen = new Integer(this.selection.length);
		this.oneRecord.add(System.getProperty("user.name"));
		this.oneRecord.add(this.preferedColor);
		this.oneRecord.add(selectionLen);
		this.oneRecord.add(CategoryRecords.DEFAULT_EXAMPLE_TYPE);
		this.selections.addElement(this.selection.clone());
		this.tableModel.updateRecord(oneRecord);
	}

	public int[] getSelection(){
		logger.finest("getSelection... in categoryRecords");
	  return this.selection;
	}

	public void setSelectedRecords(Vector selectedRecords){
		this.selectedRecords = selectedRecords;
	}

	public Vector getSelectedRocords(){
	    return this.selectedRecords;
	}

	public void setDataSet(DataSetForApps data){

		this.numObvs = data.getNumObservations();
		this.multipleSelectionColors = new Color[this.numObvs];
	}

	public void setMultipleSelectionColors(Color[] selectionColors){
		this.multipleSelectionColors = selectionColors;
	}

	public Color[] getMultipleSelectionColors(){
		logger.finest("getDisplaySelection... in categoryRecords");
	    return this.multipleSelectionColors;
	}

	public void setCategoryRecords(Vector cateRecords){
		this.categoryRecords = cateRecords;
		Vector dataRecords = new Vector();
		//Transfer the selections to the length of each selection for data in table.
		for (int i = 0; i < categoryRecords.size(); i ++){
			dataRecords.add(i, ((Vector)(categoryRecords.elementAt(i))).clone());
			this.selections.add(((Vector)(categoryRecords.elementAt(i))).elementAt(2));
			((Vector)(dataRecords.elementAt(i))).setElementAt(new Integer((((int[])this.selections.elementAt(i)).length)), 2);
		}
		dataRecords.trimToSize();
		this.tableModel.data = dataRecords;
	}

	public Vector getCategoryRecords(){
		return categoryRecords;
	}

	private void saveButton_actionPerformed(ActionEvent e){
		//Transfer the data in table to categoryRecords for save.
		//The selection records should be saved in categoryRecords.
		Vector dataRecords = new Vector();
		dataRecords = this.tableModel.data;
		//this.categoryRecords = (Vector)dataRecords.clone();
		for (int i = 0; i < dataRecords.size(); i ++){
			this.categoryRecords.add(i, ((Vector)(dataRecords.elementAt(i))).clone());
			((Vector)(this.categoryRecords.elementAt(i))).setElementAt(this.selections.get(i), 2);
			logger.finest("selection i: " + ((int[])((Vector)(this.categoryRecords.elementAt(i))).get(2)).length);
		}
		this.categoryRecords.trimToSize();
		fireChangeEvent();
	}

	private void delButton_actionPerformed(ActionEvent e){
		int[] selectedRows = this.infoTable.getSelectedRows();
	    this.tableModel.deleteRows(selectedRows);
		for (int i = 0; i < selectedRows.length; i++){
			selections.removeElementAt(selectedRows[i]);
		}
	}

	private void displayButton_actionPerformed(ActionEvent e){
		for (int i = 0; i < this.multipleSelectionColors.length; i ++){
			this.multipleSelectionColors[i] = null;
		}
	    int[] selectedRows = this.infoTable.getSelectedRows();
		logger.finest("selected rows: " + selectedRows.toString());
		for (int i = 0; i < selectedRows.length; i++){
			this.selectedRecords.add(this.tableModel.data.get(selectedRows[i]));
			//set up colors for different selections.
			Color selectionColor = (Color)((Vector)((this.tableModel.data.get(selectedRows[i])))).get(1);
			int[] currentSelection = (int[])(selections.get(selectedRows[i]));
			for (int j = 0; j < currentSelection.length; j++){
				if (this.multipleSelectionColors[currentSelection[j]] == null){
					this.multipleSelectionColors[currentSelection[j]] = selectionColor;
					logger.finest("Color null: " + currentSelection[j] + selectionColor.toString());
				} else {
					Color formerColor = this.multipleSelectionColors[currentSelection[j]];
					Color newColor = ColorInterpolator.mixColorsRGB(formerColor,selectionColor);
				    this.multipleSelectionColors[currentSelection[j]] = newColor;
					logger.finest("Color mix: " + currentSelection[j] + newColor.toString());
				}
			}
		}
		fireSelectionChanged(getMultipleSelectionColors());
		fireChangeEvent();
	}

    class ColorRenderer extends JLabel
                        implements TableCellRenderer {
        Border unselectedBorder = null;
        Border selectedBorder = null;
        boolean isBordered = true;

        public ColorRenderer(boolean isBordered) {
            super();
            this.isBordered = isBordered;
            setOpaque(true); //MUST do this for background to show up.
        }

        public Component getTableCellRendererComponent(
                                JTable table, Object color,
                                boolean isSelected, boolean hasFocus,
                                int row, int column) {
            setBackground((Color)color);
            if (isBordered) {
                if (isSelected) {
                    if (selectedBorder == null) {
                        selectedBorder = BorderFactory.createMatteBorder(2,5,2,5,
                                                  table.getSelectionBackground());
                    }
                    setBorder(selectedBorder);
                } else {
                    if (unselectedBorder == null) {
                        unselectedBorder = BorderFactory.createMatteBorder(2,5,2,5,
                                                  table.getBackground());
                    }
                    setBorder(unselectedBorder);
                }
            }
            return this;
        }
    }

    private void setUpColorRenderer(JTable table) {
        table.setDefaultRenderer(Color.class,
                                 new ColorRenderer(true));
    }

    //Set up the editor for the Color cells.
    private void setUpColorEditor(JTable table) {

        //First, set up the button that brings up the dialog.
        final JButton button = new JButton("") {
            public void setText(String s) {
                //Button never shows text -- only color.
            }
        };
        button.setBackground(Color.white);
        button.setBorderPainted(false);
        button.setMargin(new Insets(0,0,0,0));

        //Now create an editor to encapsulate the button, and
        //set it up as the editor for all Color cells.
        final ColorEditor colorEditor = new ColorEditor(button);
        table.setDefaultEditor(Color.class, colorEditor);

        //Set up the dialog that the button brings up.
        final JColorChooser colorChooser = new JColorChooser();
        ActionListener okListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                colorEditor.currentColor = colorChooser.getColor();
            }
        };
        final JDialog dialog = JColorChooser.createDialog(button,
                                        "Pick a Color",
                                        true,
                                        colorChooser,
                                        okListener,
                                        null); //XXXDoublecheck this is OK

        //Here's the code that brings up the dialog.
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				button.setBackground(colorEditor.currentColor);
                colorChooser.setColor(colorEditor.currentColor);
                //Without the following line, the dialog comes up
                //in the middle of the screen.
                //dialog.setLocationRelativeTo(button);
                dialog.setVisible(true);
            }
        });
    }

    /*
     * The editor button that brings up the dialog.
     * We extend DefaultCellEditor for convenience,
     * even though it mean we have to create a dummy
     * check box.  Another approach would be to copy
     * the implementation of TableCellEditor methods
     * from the source code for DefaultCellEditor.
     */
    class ColorEditor extends DefaultCellEditor {
        Color currentColor = null;

        public ColorEditor(JButton b) {
                super(new JCheckBox()); //Unfortunately, the constructor
                                        //expects a check box, combo box,
                                        //or text field.
            editorComponent = b;
            setClickCountToStart(1); //This is usually 1 or 2.

            //Must do this so that editing stops when appropriate.
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }

        public Object getCellEditorValue() {
            return currentColor;
        }

        public Component getTableCellEditorComponent(JTable table,
                                                     Object value,
                                                     boolean isSelected,
                                                     int row,
                                                     int column) {
            ((JButton)editorComponent).setText(value.toString());
            currentColor = (Color)value;
            return editorComponent;
        }
    }

    private void setUpSelectionColumn(TableColumn selectionColumn){
		final JButton selButton = new JButton("selection");

		selButton.setBackground(Color.white);
        selButton.setBorderPainted(false);
        selButton.setMargin(new Insets(0,0,0,0));
        //Now create an editor to encapsulate the button, and
        //set it up as the editor for all selection cells.
        final SelectionEditor selectionEditor = new SelectionEditor(selButton);
        selectionColumn.setCellEditor(selectionEditor);

        //Set up tool tips for the selection cells.
        DefaultTableCellRenderer renderer =
                new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for selection display");
        selectionColumn.setCellRenderer(renderer);

        selButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				selection = (int[])selections.get(infoTable.getSelectedRow());
				fireChangeEvent();
				fireSelectionChanged(getSelection());
            }
        });
	}

    /*
     * The editor button that brings up the selection event.
     * We extend DefaultCellEditor for convenience,
     * even though it mean we have to create a dummy
     * check box.  Another approach would be to copy
     * the implementation of TableCellEditor methods
     * from the source code for DefaultCellEditor.
     */
    class SelectionEditor extends DefaultCellEditor {
        int[] selection = null;

        public SelectionEditor(JButton b) {
			super(new JCheckBox()); //Unfortunately, the constructor
                                        //expects a check box, combo box,
                                        //or text field.
            editorComponent = b;
            setClickCountToStart(1); //This is usually 1 or 2.

            //Must do this so that editing stops when appropriate.
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }

        public Object getCellEditorValue() {
            return selection;
        }

        public Component getTableCellEditorComponent(JTable table,
                                                     Object value,
                                                     boolean isSelected,
                                                     int row,
                                                     int column) {
            ((JButton)editorComponent).setText(value.toString());
            //selection = (int[])value;
		    selectionLen = (Integer)value;
			return editorComponent;
        }
    }

    private void setUpExampleColumn(TableColumn exampleColumn) {
        //Set up the editor for the sport cells.
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("Central");
        comboBox.addItem("Boundary");
        comboBox.addItem("Ideal mean");
        comboBox.addItem("Not clear");
        exampleColumn.setCellEditor(new DefaultCellEditor(comboBox));

        //Set up tool tips for the selection cells.
        DefaultTableCellRenderer renderer =
                new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        exampleColumn.setCellRenderer(renderer);

        //Set up tool tip for the selection column header.
        TableCellRenderer headerRenderer = exampleColumn.getHeaderRenderer();
        if (headerRenderer instanceof DefaultTableCellRenderer) {
            ((DefaultTableCellRenderer)headerRenderer).setToolTipText(
                     "Click the example to see a list of choices");
        }
    }

	public void stateChanged (ChangeEvent e) {

	}
    /**
     * put your documentation comment here
     * @param l
     */
	public void addChangeListener (ChangeListener l) {
		this.listenerList.add(ChangeListener.class, l);
	}

    /**
     * put your documentation comment here
     * @param l
     */
	public void removeChangeListener (ChangeListener l) {
		this.listenerList.remove(ChangeListener.class, l);
	}

    /**
     * put your documentation comment here
     */
	private void fireChangeEvent () {
		Object[] listeners = this.listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				((ChangeListener)listeners[i + 1]).stateChanged(new ChangeEvent(this));
			}
		}             // end for
	}

        //Work with coordinator.
		public void dataSetChanged(DataSetEvent e){
          this.setDataSet(e.getDataSetForApps());
        }

        public void selectionChanged(SelectionEvent e){
          this.setSelection(e.getSelection());
        }

    /**
     * adds an SelectionListener
     */
	public void addSelectionListener (SelectionListener l) {
		listenerList.add(SelectionListener.class, l);
	}
    /**
     * removes an SelectionListener from the component
     */
	public void removeSelectionListener (SelectionListener l) {
		listenerList.remove(SelectionListener.class, l);

	}
    /**
     * Notify all listeners that have registered interest for
     * notification on this event type. The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
	private void fireSelectionChanged (int[] newSelection) {

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
                    ((SelectionListener)listeners[i + 1]).selectionChanged(e);
                }
              }//next i

	}

	private void fireSelectionChanged (Color[] newSelection) {

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
                    ((SelectionListener)listeners[i + 1]).selectionChanged(e);
                }
              }//next i

	}
}