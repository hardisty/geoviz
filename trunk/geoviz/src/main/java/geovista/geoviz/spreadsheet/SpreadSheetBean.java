package geovista.geoviz.spreadsheet;

/*
 * Description:
 * Date: Feb 9, 2003
 * Time: 12:09:50 PM
 * @author Jin Chen
 */



import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import geovista.common.data.DataSetForApps;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.SelectionEvent;
import geovista.common.event.SelectionListener;
import geovista.common.utils.DescriptiveStats;
import geovista.geoviz.spreadsheet.formula.Cell;
import geovista.geoviz.spreadsheet.formula.CellFomulaEditor;
import geovista.geoviz.spreadsheet.formula.FormulaEditor;
import geovista.geoviz.spreadsheet.table.SSTable;
import geovista.geoviz.spreadsheet.table.SSTableModel;
import geovista.geoviz.spreadsheet.tools.ToolManager;
import geovista.geoviz.spreadsheet.util.ColumnSelector;
import geovista.geoviz.spreadsheet.util.Debug;

public class SpreadSheetBean extends JPanel
                             implements java.io.Serializable,ChangeListener,
                             SelectionListener, DataSetListener, TableModelListener{
    public static final boolean DEBUG=false;
    static final long serialVersionUID = 2262286688943155764L;
    private  SSTable table; //
    private  transient boolean scffo=false; //selection change fired from outsite(beans);
    private  DescriptiveStats stats;
    private transient DataSetForApps dataSet;

    public SpreadSheetBean() {

        this.setLayout(new BorderLayout());

        table=new SSTable( new SSTableModel());
        table.setBean(this);

        table.reSetRowHeader() ;

        // support select column, row
        table.getTableHeader().setReorderingAllowed(false);
        ColumnSelector selector=new ColumnSelector(table);
        table.getTableHeader().addMouseListener(selector);
        table.addMouseMotionListener(selector) ;

        table.setColumnSelectionAllowed(true);
        table.setRowSelectionAllowed(true);
        table.setCellSelectionEnabled(true);
        ListSelectionModel lsm=table.getSelectionModel();
        lsm.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
        //support select column

        table.setAutoCreateColumnsFromModel(false); //If true, any change to Model => recreation of ColumnModel
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF );
        FormulaEditor fe=new FormulaEditor(table);
//        table.setFormulaEditor(fe);
        table.setDefaultEditor(Cell.class, new CellFomulaEditor(new JTextField(),fe));

        // set initial column set value->
         if (Debug.isDebug() ){
             int len=10;
             Float[] values=new Float[len];
             for(int i=0;i<len;i++){
                Float v=new Float(i+1);
                values[i]=v;
                for (int j=1;j<5;j++){
                    table.setValueAt(v,i,j)  ;
                }
             }
         }
        //set value<-
        table.reSetColumnHeader() ;
        ToolManager tb=new ToolManager(table);


        JPanel controlP=new JPanel();
        controlP.setLayout(new BorderLayout());
        controlP.add(tb.getToolBar(),BorderLayout.CENTER ) ;
        controlP.add(fe,BorderLayout.SOUTH ) ;
        //this.add(controlP, BorderLayout.NORTH );
        stats = new DescriptiveStats();
        this.addSelectionListener(stats);
        this.add(stats, BorderLayout.SOUTH);
        this.add(new JScrollPane(table), BorderLayout.CENTER);


    }
    /*
    private DefaultTableModel getTableModel(){
       SSTableModel sstbm=(SSTableModel) SSTableModel.getInstance() ;
        return sstbm;
    }      */
    /********************************************************************
     *                Bean property
     ********************************************************************/
    public void dataSetChanged(DataSetEvent e){
    	  e.getDataSetForApps().addTableModelListener(this);
    	  this.dataSet = e.getDataSetForApps();
          this.setDataSet(e.getDataSetForApps());
          this.stats.dataSetChanged(e);
    }
    public void selectionChanged(SelectionEvent e){
          this.setSelectedIndex(e.getSelection());
          this.stats.selectionChanged(e);
          
    }
    public void stateChanged(ChangeEvent e) {
           if(this.scffo ){
                //Not fire the event if the selection is made by outside(other beans)
                this.scffo =false;


           }
           else{
               //fire selection event only when the selection is made internally by bean itself
               int[] sel = table.getSelectedRows();
               this.fireSelectionChanged(sel);
           }

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
    /********************************************************************
     *                GeoVista Function/interface
     ********************************************************************/
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
            //Object datum = data[column+1];
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

    public void setData(Vector data,Vector columnIdentifiers)
	{
         // Insert rowIndex column
        Iterator Iter =data.iterator();
        while(Iter.hasNext()){
            Object o= Iter.next();
            if (o!=null &&o instanceof Vector  ){
                Vector v=(Vector)o;
                Debug.println("v count->:"+v.size());
                v.insertElementAt(null,0);
                Debug.println("v count<-:"+v.size());

            }
        }
        // Insert rowIndex column<-
        Debug.println("columnIdentifiers count<-:"+columnIdentifiers.size());
        columnIdentifiers.insertElementAt("Row Index",0); //Not insert null, super.getColumnName(0) will return 0. It will affect SSTableModel.getColumnName()
        Debug.println("columnIdentifiers count<-:"+columnIdentifiers.size());
        SSTableModel tbm=(SSTableModel)table.getModel();
        tbm.setDataVector(data,columnIdentifiers);
        //tbm.setDataVector(data,new Vector());
	}
    public void setData(Object[][] dataVector,Object[] columnIdentifiers)
	{

        //SSTableModel tbm=(SSTableModel)table.getModel();
        setData(convertToVector(dataVector), convertToVector(columnIdentifiers));
        //tbm.setDataVector(dataVector,columnIdentifiers);
        //getTableInstance().tableChanged(new TableModelEvent(tbm, TableModelEvent.HEADER_ROW));

	}
     /**
     * Returns a vector that contains the same objects as the array.
     * @param anArray  the array to be converted
     * @return  the new vector; if <code>anArray</code> is <code>null</code>,
     *				returns <code>null</code>
     */
    protected static Vector convertToVector(Object[] anArray) {
        if (anArray == null) {
            return null;
	}
        Vector v = new Vector(anArray.length);
        for (int i=0; i < anArray.length; i++) {
            v.addElement(anArray[i]);
        }
        return v;
    }

    /**
     * Returns a vector of vectors that contains the same objects as the array.
     * @param anArray  the double array to be converted
     * @return the new vector of vectors; if <code>anArray</code> is
     *				<code>null</code>, returns <code>null</code>
     */
    protected static Vector convertToVector(Object[][] anArray) {
        if (anArray == null) {
            return null;
	}
        Vector v = new Vector(anArray.length);
        for (int i=0; i < anArray.length; i++) {
            v.addElement(convertToVector(anArray[i]));
        }
        return v;
    }
    public void setSelectedIndex(int[] selectedRows)
         {   long start,stop; //put is at start of method
                     if(DEBUG){//put is at start of method
                         start=Calendar.getInstance().getTime().getTime() ;
                     }
             //table.setSelectionMode(2);
                 if (selectedRows == null){
                     return;
                 }
             int len = selectedRows.length;
         if(len>0){
              this.setScffo(true);//selection event is fired by other beans from outside
             table.setSelectedRow(selectedRows[0]);  //To clear previously selection
             for (int i = 1 ; i<len ; i++){
                  this.setScffo(true);//selection event is fired by other beans from outside
                  table.addSelectedRow(selectedRows[i]) ;
             }

             //table.repaint() ;

         }
         else{
             //dp{
             if (DEBUG){
                 System.out.println("setSelectedIndex() get empty array");
             }//dp}
         }

         if(DEBUG){  //put is at end of method
                         stop=Calendar.getInstance().getTime().getTime() ;
                         System.out.println(" setSelectedIndex() take "+(stop-start)*0.001f+" to finish" );
         }
     }

     private  void setScffo(boolean scffo) {
         this.scffo = scffo;
     }

    /********************************************************************
     *                Test
     ********************************************************************/
     public static void main(String args[]) {
        JFrame mf=new JFrame ();
        mf.getRootPane().getContentPane().add(	new SpreadSheetBean() ) ;
		GJApp.launch(mf, "A Simple Model",100,100,800,600);
    }
	public void tableChanged(TableModelEvent e) {
		this.setDataSet(this.dataSet);
		
	}




}

/********************* For testing purpse ***************************/
class GJApp extends WindowAdapter {
	static private JPanel statusArea = new JPanel();
	static private JLabel status = new JLabel(" ");
	static private ResourceBundle resources;

	public static void launch(final JFrame f, String title,
							  final int x, final int y,
							  final int w, int h) {
		launch(f,title,x,y,w,h,null);
	}
	public static void launch(final JFrame f, String title,
							  final int x, final int y,
							  final int w, int h,
							  String propertiesFilename) {
		f.setTitle(title);
		f.setBounds(x,y,w,h);
		f.setVisible(true);

		statusArea.setBorder(BorderFactory.createEtchedBorder());
		statusArea.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		statusArea.add(status);
		status.setHorizontalAlignment(JLabel.LEFT);

		f.setDefaultCloseOperation(
							WindowConstants.DISPOSE_ON_CLOSE);

		if(propertiesFilename != null) {
			resources = ResourceBundle.getBundle(
						propertiesFilename, Locale.getDefault());
		}

		f.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	static public JPanel getStatusArea() {
		return statusArea;
	}
	static public void showStatus(String s) {
		status.setText(s);
	}
	static Object getResource(String key) {
		if(resources != null) {
			return resources.getString(key);
		}
		return null;
	}

}
