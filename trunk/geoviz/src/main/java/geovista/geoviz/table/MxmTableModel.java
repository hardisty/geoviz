package geovista.geoviz.table;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;


public class MxmTableModel extends DefaultTableModel {
	private int indexes[];
	private String myDomain=null;
	private int errorIndex=0;

    public MxmTableModel(Vector data, Vector columnNames)
    {
       super(data,columnNames);
       allocate();
    }


    public MxmTableModel()
    {
	super();
    }


    public MxmTableModel(Vector columnNames, int numRows)
    {
	super(columnNames,numRows);
	allocate();
    }

    public MxmTableModel(Object[] columnNames, int numRows)
    {
	super(columnNames,numRows);
	allocate();
    }

    public MxmTableModel(Object[][] data, Object[] columnNames)
    {
	super(data,columnNames);
	allocate();
    }
	public void insertRow( int row, Vector rowData )
	{
		super.insertRow( row, rowData );
	}



	/*public Object getValueAt(int row, int column) {
		return super.getValueAt(indexes[row], column);
	}*/
	public void setValueAt(Object aValue, int row, int column) {
		super.setValueAt(aValue, indexes[row], column);
	}
	public void tableChanged(TableModelEvent e) {
		allocate();
	}
	public void sort(int column,String myIndex,String domain) {
		int rowCount = super.getRowCount();
		myDomain=domain;

		if(myIndex.equals("ASC"))
		{

		for(int i=0; i < rowCount; i++) {
			for(int j = i+1; j < rowCount; j++) {

				if(compare(indexes[i], indexes[j], column) < 0 && (errorIndex==0))
				{
					swap(i,j);
				}
				 if(errorIndex==1) break;
			}
			 if(errorIndex==1) break;
		}//end of for loop
		    if(errorIndex==1)
		    {
			JOptionPane.showMessageDialog(null,"You should choose sort by string");
			errorIndex=0;
		    }

		}
		else if(myIndex.equals("DES"))
		{

		    for(int i=0; i < rowCount; i++) {
			    for(int j = i+1; j < rowCount; j++) {

				    if(compare(indexes[i], indexes[j], column) > 0) {
					if(errorIndex==1) break;
					swap(i,j);
				    }
				     if(errorIndex==1) break;
			    }
			     if(errorIndex==1) break;
		    }//end of for loop
		    if(errorIndex==1)
		    {
			JOptionPane.showMessageDialog(null,"You should choose sort by string");
			errorIndex=0;

		    }


		}


	}
    public void swap(int i, int j) {
		int tmp = indexes[i];
		indexes[i] = indexes[j];
		indexes[j] = tmp;
	}
	public int compare(int i, int j, int column) {
		Object io = super.getValueAt(i,column);
		Object jo = super.getValueAt(j,column);
		float c=(float)0.0;
		if(myDomain.equals("STR"))
		{

		c = jo.toString().compareTo(io.toString());

		}
		else if(myDomain.equals("NUM"))
		{
		    float firstFloat=(float)0.0;
		    float secondFloat=(float)0.0;
		    try
		    {
		    firstFloat=Float.parseFloat(io.toString());
		    secondFloat=Float.parseFloat(jo.toString());
		    }
		    catch(Exception e3)
		    {

		      errorIndex=1;
		    }
		    c=secondFloat-firstFloat;


		}
		return (c < 0) ? -1 : ((c > 0) ? 1 : 0);
	}
	private void allocate() {
		indexes = new int[getRowCount()];

		for(int i=0; i < indexes.length; ++i) {
			indexes[i] = i;
		}
	}
	public Class getColumnClass(int c)
	{
		return getValueAt(0, c).getClass();
	}

 }
