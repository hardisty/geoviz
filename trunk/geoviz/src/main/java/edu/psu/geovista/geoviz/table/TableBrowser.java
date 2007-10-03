package edu.psu.geovista.geoviz.table;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.common.event.DataSetEvent;
import edu.psu.geovista.common.event.DataSetListener;
import edu.psu.geovista.common.event.SelectionEvent;
import edu.psu.geovista.common.event.SelectionListener;

public class TableBrowser extends JPanel
	    implements  ChangeListener,Serializable,
                        SelectionListener, DataSetListener
{

    private Container contentPane= this;
    // currentTable is the current working table
    private JTable currentTable;
    private JInternalFrame currentFrame=new JInternalFrame();
    private int model=0;
    // control pane is defined providing the manipulations on the current table
    private ControlPanel myControl=null;
    private MxmTableModel tableModel;
    private JScrollPane struc ;
    private Object[] columnName;
protected  EventListenerList listenerList = new EventListenerList();

// initialization of the table browser
public TableBrowser()
{

	    //tableModel = new MxmTableModel(10,5);
	    //settableProperties();
	    // initailized the GUI
	    //buildGUI();
	}

        public void selectionChanged(SelectionEvent e){
          this.setSelectedIndex(e.getSelection());
        }

        public void dataSetChanged(DataSetEvent e){
          this.setDataSet(e.getDataSetForApps());
        }

	public void setColumnNames(Vector column)
	{
	    Object[] col = new Object[column.size()];
	    for (int i = 0; i< column.size(); i++)
	    {
		col[i] = ((Object)column.elementAt(i));
	    }
	    columnName = col;
	   //tableModel.setColumnIdentifiers(column);
	}
	public void setColumnNames(Object column)
	{
	    Object[] column1 = (Object[]) column;
	    System.out.println(column1.length + " is the number of columns");
	    columnName =  new Object[column1.length];
	     columnName = column1;
	    //tableModel.setColumnIdentifiers(columnName);
	}
	public void setDataReverse(Vector data)
	{
	    int rows = ((Vector)data.elementAt(0)).size();
	    int col = data.size();
	    Vector tmpVect = new Vector();
	    Object[][] dataRev = new Object[rows][col];
	    for (int j = 0; j<col ; j++)
	    {
		    tmpVect = (Vector)data.elementAt(j);
		    for (int i = 0 ; i < rows ; i++)
		    {
			dataRev[i][j] = tmpVect.elementAt(i);
		    }
	    }
	    tableModel = new MxmTableModel(dataRev,columnName);
	    settableProperties();
	    buildGUI();
	}
	public void setData(Vector data,Vector column)
	{

	     tableModel = new MxmTableModel(data,column);
	    tableModel.setDataVector(data,column);
	    settableProperties();
	    buildGUI();
	}
	public void setData(Vector data,Vector column,int mod)
	{
	    model= mod;
	     tableModel = new MxmTableModel(data,column);
	    tableModel.setDataVector(data,column);
	    settableProperties();
	    buildGUI();
	}
	public void setData(Object[][] data,Object[] column)
	{
	    columnName = column;
	    tableModel = new MxmTableModel(data,columnName);
	    tableModel.setDataVector(data,column);
	    settableProperties();
	    buildGUI();
	}
        /*for use with coordinator
        */
        public void setDataSet(DataSetForApps dataSet) {





          String[] attributeNames = dataSet.getAttributeNamesOriginal();
          int numObs = dataSet.getNumObservations();
          Object[][] tablesData = new Object[numObs][attributeNames.length];

          double[] doubleData = null;
          int[] intData = null;
          String[] stringData = null;
          boolean[] boolData = null;

          for (int column = 0; column < attributeNames.length; column++) {
        	  Object datum = dataSet.getNamedArrays()[column];//XXX is this the right accessor?

            if (datum instanceof double[]) {
              doubleData = (double[])datum;
              for (int row = 0; row < numObs; row++) {
                double dat = doubleData[row];
                Double d = new Double(dat);
                tablesData[row][column] = d;
              }
            }

            if (datum instanceof int[]) {
              intData = (int[])datum;
              for (int row = 0; row < numObs; row++) {
                int dat = intData[row];
                Integer d = new Integer(dat);
                tablesData[row][column] = d;
              }
            }

            if (datum instanceof String[]) {

              stringData = (String[])datum;
              for (int row = 0; row < numObs; row++) {
                String dat = stringData[row];
                tablesData[row][column] = dat;
              }
            }

            if (datum instanceof boolean[]) {
              boolData = (boolean[])datum;
              for (int row = 0; row < numObs; row++) {
                boolean dat = boolData[row];
                Boolean d = new Boolean(dat);
                tablesData[row][column] = d;
              }
            }


          }

          this.setData(tablesData,attributeNames);

        }


	public void setData(double[][] data)
	{
	  int len = data.length;
	  int wid = data[0].length;
	  System.out.println("________________");
	  System.out.println("length" + len);
	  System.out.println("width" + wid);
	  int x = 0;
	  Object[][] realdata = new Object[len][wid];

	  for (int i =0 ; i< len ; i++)
	  {
	    for (int j=0; j< wid ; j++)
	    {
		Double dob = null;
		try
		{
		    dob = new Double(data[i][j]);
		}
		catch(Exception e)
		{
		    x = x+ 1;

		}


		if (dob != null)
		realdata[i][j] = (Object)(dob.toString());
		else
		realdata[i][j] = (Object)"";
	    }

	  }
	    System.out.println("count =" + x);
	    //put a check for the data & column names
	    tableModel = new MxmTableModel(realdata,columnName);
	    settableProperties();
	    buildGUI();
	}
	public void addRow(Object[] data)
	{
	    tableModel.addRow(data);
	}
	public void addRow(Vector data)
	{
	    tableModel.addRow(data);
	}
	public TableBrowser(Vector column, int numOfRows)
	{
	    tableModel = new MxmTableModel(column,numOfRows);
	    settableProperties();
	    // initailized the GUI
	    buildGUI();
	}
/*	public TableBrowser(int column, int numOfRows)
	{
	    tableModel = new MxmTableModel(column,numOfRows);
	    settableProperties();
	    // initailized the GUI
	    buildGUI();
	}*/
	public TableBrowser(Vector column)
	{
	    tableModel = new MxmTableModel(column,5);
	    settableProperties();
	    // initailized the GUI
	    buildGUI();
	}
	public TableBrowser(Vector data, Vector column)
	{
	    tableModel = new MxmTableModel(data,column);
	   settableProperties();
	    // initailized the GUI
	    buildGUI();
	}
	public TableBrowser(Object[] columnNames, int numRows)
	{
	    tableModel = new MxmTableModel(columnNames,numRows);
	   settableProperties();
	    // initailized the GUI
	    buildGUI();
	}
	public TableBrowser(Object[] columnNames)
	{
	    tableModel = new MxmTableModel(columnNames,5);
	   settableProperties();
	    // initailized the GUI
	    buildGUI();
	}
	public TableBrowser(Object[][] data, Object[] columnNames)
	{
	    tableModel = new MxmTableModel(data,columnNames);
	   settableProperties();
	    // initailized the GUI
	    buildGUI();
	}

	public int[] sendSelectedIndex()
	{
	    System.out.println(currentTable.getSelectedRows()[1]);
	    return currentTable.getSelectedRows();
	}
	public void setSelectedIndex(int[] selectedRows)
	{
	    //currentTable.setSelectionBackground(new Color(100,100,100));
	    //currentTable.setSelectionForeground(new Color(100,100,100));
	    currentTable.setSelectionMode(2);
		if (selectedRows == null){
		    return;
		}
	    int len = selectedRows.length;
	    for (int i = 0 ; i<len ; i++){
		System.out.println(currentTable.isRowSelected(i));
		currentTable.addRowSelectionInterval(selectedRows[i],selectedRows[i] );

	    }
	    for (int i = 0 ; i<len ; i++){

	    contentPane.repaint();
	    }
	}

    private void settableProperties()
    {
	try
		{
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (Exception e)
		{
			System.out.println("Windows Look/feel failed");
			System.out.println(e.getMessage());
		}

	currentTable = new JTable(tableModel);
        //currentTable.setPreferredSize(new Dimension(800,600));
	if (model==1)
	{
	    MxmTableCellRenderer renderer = new MxmTableCellRenderer( JLabel.LEFT );
	    int columnCount = currentTable.getColumnCount();
	    for( int i = 0 ; i < columnCount ; i++ )
	    {
		String id = currentTable.getColumnName( i ) ;
		javax.swing.table.TableColumn col = currentTable.getColumn( id ) ;
		col.setCellRenderer( renderer ) ;
	    }
	}
	struc = new JScrollPane(currentTable);
	struc.setAutoscrolls(false);
	if (columnName != null)
	    if (columnName.length >10)
		currentTable.setAutoResizeMode(0);
	//super.setTitle("Table Browser");
	super.setVisible(true);
	//contentPane.setSize(800,400);
    }
	public ControlPanel getMyControl()
	{
	    return myControl;
	}

	public JInternalFrame getCurrentFrame()
	{
	    return currentFrame;
	}
	public void setCurrentFrame(JInternalFrame fr)
	{
	    this.currentFrame=fr;
	}
	public JTable getCurrentTable()
	{
	    return currentTable;
	}
	public void setCurrentTable(JTable tb)
	{
	    this.currentTable=tb;
	}

	private void buildGUI()
	{
			    contentPane.removeAll();
		//initialize the menu pane
		buildMenue();
		//initialize the control pane
		buildControlPanel();
		// initialize the desktop pane
		desktopGUI();
		//this.pack();
		contentPane.repaint();

	}

	private void buildControlPanel()
	{
	    //pass a reference of this class to the control pane
	    //which is defined as the control pane class

	    myControl=new ControlPanel(this);
	    contentPane.add(myControl,BorderLayout.NORTH);

	}
	//end of the buildControlPanel


	private void desktopGUI()
	{
System.out.println("repainted");

	    contentPane.add(struc,BorderLayout.CENTER);
	    System.out.println(contentPane.getComponentCount());
	    //###contentPane.setSize(800,600);
	}
	private void buildMenue()
	{

		Action[] actions = {
				new NewAction(),
				new OpenAction(),
				new PrintAction(),
				new CutAction(),
				new CopyAction(),
				new PasteAction(),
				new ExitAction()
		};

		JMenuBar menubar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");

		for(int i=0; i < actions.length; ++i) {
			fileMenu.add(actions[i]);

			if(i == 2 || i == actions.length-2){
				fileMenu.addSeparator();
			}
		}
		menubar.add(fileMenu);
		Action[] actions1 = {
				new PropertiesAction()
		};
		JMenu option = new JMenu("Option");
		for(int i=0; i < actions1.length; ++i) {
			option.add(actions1[i]);
		}
		menubar.add(option);
		menubar.add(new JMenu("Help"));
	    menubar.add(Box.createHorizontalGlue());


		ImageIcon iconIDL=null;
/*
		try{
			iconIDL=new ImageIcon(new URL(codebaseURL,"idl.gif"));
		}catch(Exception ed)
		{
		    System.out.println("can not find the image");
		}*/
		JLabel IDLLabel = new JLabel(iconIDL);
		menubar.add(IDLLabel);

		//this.setJMenuBar(menubar);

	}

      void showStatus(String s)
	{
		//do nothing now
	}

	class NewAction extends AbstractAction {
		public NewAction() {
			super("New ...");
		}
		public void actionPerformed(ActionEvent event) {
			showStatus("new");
		}
	}

	class OpenAction extends AbstractAction {
		public OpenAction() {
			super("Open ...");
			}
		public void actionPerformed(ActionEvent event) {
			showStatus("open");
		}
	}
	class CutAction extends AbstractAction {
		public CutAction() {
			super("Cut");
		}
		public void actionPerformed(ActionEvent event) {
			showStatus("cut");
		}
	}
	class CopyAction extends AbstractAction {
		public CopyAction() {
			super("Copy");
		}
		public void actionPerformed(ActionEvent event) {
			showStatus("copy");
		}
	}
	class PasteAction extends AbstractAction {
		public PasteAction() {
			super("Paste");
		}
		public void actionPerformed(ActionEvent event) {
			showStatus("paste");
		}
	}
	class ExitAction extends AbstractAction {
		public ExitAction() {
			super("Exit");
		}
		public void actionPerformed(ActionEvent event) {
			   //dispose();
		}
	}
	class PrintAction extends AbstractAction {
		public PrintAction() {
		 super("Print");
		}
		public void actionPerformed(ActionEvent event) {
			//PrintUtilities.printComponent(new JLabel("Hello, world"));
		}
	}
	class PropertiesAction extends AbstractAction {
		public PropertiesAction() {
		 super("Property");
		}
		public void actionPerformed(ActionEvent event) {
		    System.out.println("Hai");

		    JFrame fr =  new JFrame();
		    JTabbedPane pane = new JTabbedPane();


		    pane.add(new JPanel());
		    pane.setTitleAt(0,"Hai");
		    pane.add(new JPanel());
		    pane.setTitleAt(1,"Hai");
		    pane.add(new JPanel());
		    pane.setTitleAt(2,"Hai");
		    fr.getContentPane().add(pane);
		    fr.setTitle("Property window");
		    fr.setSize(200,350);
		    fr.setResizable(false);
		    fr.setVisible(true);

		}
	}
	//end of the initialization for the menu pane
	public void resizeRowOfCurrentTable(int amplify,int shrink)
	{
	    int oldY=getScrolRow();
		currentTable.setRowHeight(currentTable.getRowHeight()*amplify/shrink);
		ScrollTable(0,oldY,amplify,shrink);
	}

	public void resizeColumnOfCurrentTable(int amplify,int shrink)
	{
	     int oldX=getScrolColumn();
		TableColumnModel tcm = currentTable.getColumnModel();
		int w=0;
		for(int i=0; i < tcm.getColumnCount(); i++) {
			TableColumn column = tcm.getColumn(i);
			w=column.getWidth();
			column.setPreferredWidth((int)(w*amplify/shrink));
			column.setWidth((int)(w*amplify/shrink));
		}
		ScrollTable(oldX,0,amplify,shrink);



	}

	public void setTableMode()
	{
	    JCheckBox[] checkBox=this.getMyControl().getTableMode();
	    currentTable.setRowSelectionAllowed(checkBox[0].isSelected());
	    currentTable.setColumnSelectionAllowed(checkBox[1].isSelected());
	    currentTable.setCellSelectionEnabled(checkBox[2].isSelected());
	}
	public void setTableMode(JTable tb)
	{
	    JCheckBox[] checkBox=this.getMyControl().getTableMode();

		tb.setRowSelectionAllowed(checkBox[0].isSelected());

		tb.setColumnSelectionAllowed(checkBox[1].isSelected());

		tb.setCellSelectionEnabled(checkBox[2].isSelected());


	}


	private int getScrolRow()
{
		int moveNum=currentTable.getSelectedRow();
		return currentTable.getRowHeight()*moveNum;



}

	private int getScrolColumn()
{

	    int moveNum=currentTable.getSelectedColumn();
		int oldX=0;
		for(int i=0;i<moveNum;i++)
		{
		    oldX+=currentTable.getColumnModel().getColumn(i).getWidth();
		}
		return oldX;
 }
private void ScrollTable(int row,int col,int amplify,int shrink)
{
    JScrollPane myScrollPane = (JScrollPane)SwingUtilities.getAncestorOfClass(
							    JScrollPane.class, currentTable);

		JViewport myViewport=myScrollPane.getViewport();
		Point point= myViewport.getViewPosition();

		point.x=row*amplify/shrink;
		point.y=col*amplify/shrink;
		if(amplify<0||shrink<0)
		{
		    point.x=0;
		    point.y=0;
		}
		myViewport.setViewPosition(point);
	myScrollPane.setViewport(null);
	myScrollPane.setViewport(myViewport);
	myScrollPane.setPreferredSize(new Dimension(800,600));
        myScrollPane.repaint();



}



public void stateChanged(ChangeEvent e) {
		JButton button = (JButton) e.getSource();
                if (button.getText() == "Send"){
                  int[] sel = currentTable.getSelectedRows();
                  this.fireSelectionChanged(sel);
                }
	}
public void addChangeListener(ChangeListener l) {
			this.listenerList.add(ChangeListener.class, l);
		}

		public void removeChangeListener(ChangeListener l) {
			this.listenerList.remove(ChangeListener.class, l);
		}

		public void fireChangeEvent () {
		    Object[] listeners = this.listenerList.getListenerList();
	    // Process the listeners last to first, notifying
	    // those that are interested in this event

			for (int i = listeners.length - 2; i >= 0; i -= 2) {
				if (listeners[i] == ChangeListener.class) {
					((ChangeListener)listeners[i + 1]).stateChanged(new ChangeEvent(this));
				}
			}            // end for

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
}//little edit
