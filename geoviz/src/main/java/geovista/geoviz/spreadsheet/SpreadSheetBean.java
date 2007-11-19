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
import geovista.geoviz.descriptive.DescriptiveStats;


public class SpreadSheetBean extends JPanel
                             implements 
                             SelectionListener, DataSetListener, TableModelListener{

    private  DescriptiveStats stats;
    private transient DataSetForApps dataSet;

    public SpreadSheetBean() {



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
          //this.setSelectedIndex(e.getSelection());
          this.stats.selectionChanged(e);
          
    }
    public void stateChanged(ChangeEvent e) {

        
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

          //this.setData(tablesData,attributeNames);

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
