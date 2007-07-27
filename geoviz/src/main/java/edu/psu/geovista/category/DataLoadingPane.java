/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 *
 * @author: jin Chen
 * @date: Aug 19, 2003$
 * @version: 1.0
 */
package edu.psu.geovista.category;

//import edu.psu.geovista.app.pcp.toolkits.ListControlPane;
//import edu.psu.geovista.app.pcp.toolkits.table.ConfigureTableModel;
//import edu.psu.geovista.app.plot.axis.YAxis;

import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import edu.psu.geovista.data.geog.DataSetForApps;
import edu.psu.geovista.ui.event.DataSetEvent;
import edu.psu.geovista.ui.event.DataSetListener;

public class DataLoadingPane extends ListControlPane implements DataSetListener{
   //DataAssembler dataAssembler;
	
	protected final static Logger logger = Logger.getLogger(DataLoadingPane.class.getName());
	private Object[] dataset;
	private DataSetForApps dataApps;
	private String[] items;
	private Object[] outputData;
        private int lengthOfaddAtt;

    public DataLoadingPane(){
    }

    //public DataLoadingPane(DataAssembler dataAssembler) {
        //this.dataAssembler=dataAssembler;
    //    initList();
    //    intiTable();
    //}

	public void setDataSet(DataSetForApps data){
		this.dataApps = data;
		this.dataset = data.getDataSetNumericAndSpatial();
		this.initList();
		this.initTable();
	}

	public Object[] getDataObject(){
		return this.dataset;
	}

    protected void initList() {

        items = this.dataApps.getAttributeNamesNumeric();
        this.setListData(items);
        this.lengthOfaddAtt = this.dataset.length - this.dataApps.getAttributeNamesOriginal().length - 1;
    }

    //1.create tableModel //2. init values
    protected void initTable() {
        //1. Model
        int initRowCount=0;
        Vector colName=new Vector();
        colName.add("Name");colName.add("NumRec");
        Class[] types = new Class [] {
                 java.lang.String.class, java.lang.Integer.class
        };
        boolean[] editable={false,false,false};
        ConfigureTableModel model=new ConfigureTableModel(colName,initRowCount);
        model.setTypes(types);
        model.setEditables(editable);
        myTable.setModel(model);
    }
    protected void initButtons() {
        String[] names=new String[]{"Select","Deselect","Pass Selection"};
        this.addButtons(names);

    }

    protected void actOnButtonClick(String btnName) {
        if(btnName.equals("Select") ){
            this.moveRight();
        }
        else if(btnName.equals("Deselect") ){
            this.moveLeft() ;
        }
        else if(btnName.equals("Pass Selection")){
            Object[] os= myTable.getSortValueAtColumn("Name");
            String[] colNames=new String[os.length];
            this.outputData = new Object[os.length + 1 + this.lengthOfaddAtt];
//            for (int i=0;i<colNames.length ;i++){
//                colNames[i]=(String)os[i];
//				for (int j = 0; j < this.dataApps.getNumberNumericAttributes(); j ++){
//					if (colNames[i] == this.items[j]){
//						this.outputData[i+1] = this.dataApps.getDataSetNumericAndSpatial()[j+1];
//						continue;
//					}
//				}
//            }
            for (int i=0;i<colNames.length ;i++){
                colNames[i]=(String)os[i];
            }
			for (int j = 0; j < this.dataApps.getNumberNumericAttributes(); j ++){
				for (int i=0;i<colNames.length ;i++){
					if (colNames[i] == this.items[j]){
						this.outputData[i+1] = this.dataApps.getDataSetNumericAndSpatial()[j+1];
						continue;
					}
				}
            }
            this.outputData[0] = colNames;
            for (int i = 0; i < this.lengthOfaddAtt; i ++){
              this.outputData[os.length + 1 + i] = this.dataset[this.dataApps.getAttributeNamesOriginal().length + 1 + i];
            }
            DataSetForApps outputDataSet = new DataSetForApps(outputData);
            this.fireDataSetChanged(outputDataSet);
        }

    }

    protected void addRecord(String columnName) {
        int rowCount=1705;
        Object[] arow={columnName, new Integer(rowCount) };
        this.myTable.addRow(arow);
    }

   // public void setDataAssembler(DataAssembler dataAssembler) {
   //     this.dataAssembler = dataAssembler;
   // }

	public void dataSetChanged(DataSetEvent e) {
		this.setDataSet(e.getDataSetForApps());
    }

	/**
	 * implements DataSetListener
	 */
	public void addDataSetListener(DataSetListener l) {
	  listenerList.add(DataSetListener.class, l);
	}

	/**
	 * removes an DataSetListener from the button
	 */
	public void removeDataSetListener(DataSetListener l) {
	  listenerList.remove(DataSetListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for
	 * notification on this event type. The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 * @see EventListenerList
	 */
	protected void fireDataSetChanged(DataSetForApps dataSet) {
		logger.finest("ShpToShp.fireDataSetChanged, Hi!!");
	 // Guaranteed to return a non-null array
	 Object[] listeners = listenerList.getListenerList();
	 DataSetEvent e = null;
	 // Process the listeners last to first, notifying
	 // those that are interested in this event
	 for (int i = listeners.length - 2; i >= 0; i -= 2) {
	   if (listeners[i] == DataSetListener.class) {
		 // Lazily create the event:
		 if (e == null) {
		   e = new DataSetEvent(dataSet, this);

		 }
		 ((DataSetListener)listeners[i + 1]).dataSetChanged(e);
		}
	  }
	}


}
