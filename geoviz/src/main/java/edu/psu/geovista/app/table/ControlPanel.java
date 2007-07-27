
package edu.psu.geovista.app.table;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
class ControlPanel extends JPanel implements ActionListener,ItemListener
		{
		    //Control c;
		    TableBrowser myTB;
			int token=0;
			int tempNumber=1;
			JTable table=null;
			JInternalFrame frame=null;
			// the following variable is for lock
			TableColumn firstColumn=null;
			JTable headerTable=null;
			TableColumnModel tcm=null;
			JScrollPane scrollPane=null;
			Object[] selectionModes = new Object[] {
				"SINGLE",
				"SINGLE_INTERVAL",
				"MULTIPLE_INTERVAL",
			};
			int[] selectionConstants = {
				ListSelectionModel.SINGLE_SELECTION,
				ListSelectionModel.SINGLE_INTERVAL_SELECTION,
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION,
			};
	// define the sub panel for the control
			JPanel selectionPanel=new JPanel(),
				selectablesPanel=new JPanel(),
				operationPanel=new JPanel(),
				sortPanel=new JPanel(),
				viewPanel=new JPanel();

			JCheckBox[] selectables=new JCheckBox[]
			{
				new JCheckBox("Row",true),
				new JCheckBox("Col",true),
				new JCheckBox("Cell"),
			};

			JComboBox modeCombo = new JComboBox(selectionModes);
			JButton deleteButton=new JButton("Del");
			JButton[] sortOperations=new JButton[]
			{
			    new JButton("MAX"),
			    new JButton("MIN"),
			    new JButton("AVE"),
			    new JButton("%"),
			};



			JButton[] zooms=new JButton[]
			{
				new JButton("+"),
				new JButton("-")

			};
			//JButton custom=new JButton("newTB");
			JButton sendButton=new JButton("Send");

			JCheckBox firstColumnLocked=new JCheckBox("Lock");

		public JCheckBox[] getTableMode()
		{
		   return  selectables;
		}

		public void iniTable()
		{


					table=myTB.getCurrentTable();
					//frame=myTB.getCurrentFrame();
					tcm = table.getColumnModel();
					headerTable=new JTable(table.getModel());
					headerTable.getTableHeader().setReorderingAllowed(false);
					headerTable.setRowHeight(table.getRowHeight());
					MyTools.setActualPreferredColumnWidths(table);
					MyTools.setActualPreferredColumnWidths(headerTable);
					firstColumn=tcm.getColumn(0);
					headerTable.setPreferredScrollableViewportSize(
						new Dimension(firstColumn.getPreferredWidth() +
							table.getColumnModel().getColumnMargin(),0));
					scrollPane = (JScrollPane)
						SwingUtilities.getAncestorOfClass(
							JScrollPane.class, table);

					setToken(1);

		}

		public int getToken()
		{
			return token;
		}
		public void setToken(int tk)
		{
			token=tk;
		}

		public ControlPanel( final TableBrowser tb) {

		    initialControlPanel();


			this.myTB=tb;
			for(int i=0;i<selectables.length;i++)
			{
				add(selectables[i]);
				selectables[i].addItemListener(this);

			}
			add(firstColumnLocked);

			 add(deleteButton);
			for(int i=0;i<zooms.length;i++)
			{
				 add(zooms[i]);
				zooms[i].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e)
					{
						String name=e.getActionCommand();
						if(name.equals("+"))
						{
							if(selectables[2].isSelected())
							{
								myTB.resizeRowOfCurrentTable(2,1);
								myTB.resizeColumnOfCurrentTable(2,1);

							}
							else if(selectables[0].isSelected())
							{
								myTB.resizeRowOfCurrentTable(2,1);
							}
							else if(selectables[1].isSelected())
							{
								myTB.resizeColumnOfCurrentTable(2,1);

							}
						}
						else if (name.equals("-"))
						{
							if(selectables[2].isSelected())
							{
								myTB.resizeRowOfCurrentTable(1,2);
								myTB.resizeColumnOfCurrentTable(1,2);
							}
							else if(selectables[0].isSelected())
							{
								myTB.resizeRowOfCurrentTable(1,2);
							}
							else if(selectables[1].isSelected())
							{
								myTB.resizeColumnOfCurrentTable(1,2);

							}

						}
					}
				});


			}

			for(int i=0;i<sortOperations.length;i++)
			{
			     add(sortOperations[i]);
				sortOperations[i].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e)
					{
						String name=e.getActionCommand();

							if(selectables[1].isSelected())
							{
							    columnCaculation(name);
							}
							else
							{
					    JOptionPane.showMessageDialog(myTB,"please choose column action.");
							}

					}
				});

			}


			 //add(custom);
			 add(sendButton);
			/*custom.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					showTheDialog();
				}
			});*/

			sendButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					 send();
                                         ControlPanel.this.myTB.stateChanged(new ChangeEvent(sendButton));
				}

			});






			firstColumnLocked.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int tk=getToken();
					if(tk==0)
					{
						iniTable();
					}


//					headerTable.getTableHeader().setReorderingAllowed(false);

					if(firstColumnLocked.isSelected()) {
						tcm.removeColumn(firstColumn);
						scrollPane.setRowHeaderView(headerTable);
						scrollPane.setCorner(
							JScrollPane.UPPER_LEFT_CORNER,
							headerTable.getTableHeader());
					}
//					else if(!firstColumnLocked.isSelected()){
					else {
						tcm.addColumn(firstColumn);
						int numCols = tcm.getColumnCount();
						tcm.moveColumn(numCols-1, 0);
						scrollPane.setRowHeaderView(null);
						setToken(0);

					}
				}
			});




			initializeControls();


		}
		private void initializeControls() {
			int mode =
				myTB.getCurrentTable().getSelectionModel().getSelectionMode();

			if(mode == ListSelectionModel.SINGLE_SELECTION) {
				modeCombo.setSelectedIndex(0);
			}
			else if(mode ==
				ListSelectionModel.SINGLE_INTERVAL_SELECTION) {
				modeCombo.setSelectedIndex(1);
			}
			else if(mode ==
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION) {
				modeCombo.setSelectedIndex(2);
			}

			selectables[0].setSelected(true);
			selectables[1].setSelected(true);
			selectables[2].setSelected(myTB.getCurrentTable().getCellSelectionEnabled());
			deleteButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
				    int temp=0;
					if(selectables[0].isSelected())
					{
						int[] cn=myTB.getCurrentTable().getSelectedRows();
						for( int i=0;i<cn.length;i++)
						{
						    temp=0;
						    if(cn[i]!=0)
							temp=cn[i]-i;

							((DefaultTableModel)(myTB.getCurrentTable().getModel())).removeRow(temp);
						}


					}
					else if(selectables[1].isSelected())
					{
						int[] cn=myTB.getCurrentTable().getSelectedColumns();
						String name=null;
						for( int i=0;i<cn.length;i++)

					    {

							name=myTB.getCurrentTable().getColumnName(cn[i]-i);
							myTB.getCurrentTable().getColumnModel().removeColumn(myTB.getCurrentTable().getColumn(name));

						}

					}

				}
			});

		}
		public void itemStateChanged(ItemEvent e){

						JCheckBox checkBox=(JCheckBox)e.getSource();
						boolean b=checkBox.isSelected();
						if(checkBox==selectables[0])
						{
							myTB.getCurrentTable().setRowSelectionAllowed(b);

						}
						else if(checkBox==selectables[1])
						{
							myTB.getCurrentTable().setColumnSelectionAllowed(b);

						}
						else if(checkBox==selectables[2])
						{
							myTB.getCurrentTable().setCellSelectionEnabled(b);
						}

		}

		public void actionPerformed(ActionEvent e) {
					int index = modeCombo.getSelectedIndex();
					myTB.getCurrentTable().setSelectionMode(
									selectionConstants[index]);
		}


	private void send()
	{
myTB.fireChangeEvent();
	}
	private void columnCaculation(String index)
	{
	    String resultString="";
	    JTable tb=myTB.getCurrentTable();
	    DefaultTableModel tm=(DefaultTableModel)tb.getModel();
	    int rowCount=tb.getModel().getRowCount();
	    int columnNum=myTB.getCurrentTable().getSelectedColumn();
	    String tempString="";
	    double tempNum=0.0;
	    double resultNum=0.0;
	    int  theNum=0;
	    int totalRow=rowCount;
	    String oldString=null,newString=null;
if(index=="MAX")
	    {
		resultNum=0.0;

		for(int i=0;i<rowCount;i++)
		{
		    tempNum=0.0;
		    tempString=(String)tm.getValueAt(i,columnNum);
		    try
		    {
			tempNum=Double.parseDouble(tempString);
		    }catch(Exception e){ };

		    if(tempNum>resultNum)
		    {
			resultNum=tempNum;
			theNum=i;
		    }
		}
		resultString=resultNum +"\n At row "+theNum;
		//identify the max value
		oldString=(String)tm.getValueAt(theNum,columnNum);
		newString="--> "+oldString;
		tm.setValueAt(newString,theNum,columnNum);


	 //move the viewport to the current value
	       scrolTable(theNum);

	    }
	    else if(index=="MIN")
	    {
		resultNum=100000000.0;
		for(int i=0;i<rowCount;i++)
		{
		    tempString=(String)tm.getValueAt(i,columnNum);
		    tempNum=100000000.0;
		    try
		    {
			tempNum=Double.parseDouble(tempString);
		    }catch(Exception e){ };

		    if(tempNum<resultNum)
		    {
			resultNum=tempNum;
			theNum=i;
		    }
		}
		resultString=resultNum+"\n At row "+theNum;
		oldString=(String)tm.getValueAt(theNum,columnNum);
		newString="--> "+oldString;
		tm.setValueAt(newString,theNum,columnNum);

		scrolTable(theNum);

	    }
	    else if(index=="AVE")
	    {
		resultNum=0.0;

		for(int i=0;i<rowCount;i++)
		{
		    tempNum=0.0;
		    tempString=(String)tm.getValueAt(i,columnNum);
		    try
		    {
			tempNum=Double.parseDouble(tempString);
		    }catch(Exception e){totalRow--;};

		    resultNum=resultNum+tempNum;
		}
		resultNum=resultNum/totalRow;
		resultString="The mean is "+resultNum;
		theNum=1;
		oldString=(String)tm.getValueAt(theNum,columnNum);


	    }
	    else if(index=="%")
	    {
		resultString=percentageChange();
		theNum=1;
		oldString=(String)tm.getValueAt(theNum,columnNum);

	    }
	    JOptionPane.showMessageDialog(myTB,resultString);
	tm.setValueAt(oldString,theNum,columnNum);
	tb.repaint();

	}

	private String percentageChange()
	{
	    JTable tb=myTB.getCurrentTable();
	    DefaultTableModel tm=(DefaultTableModel)tb.getModel();
	    int rowCount=tb.getModel().getRowCount();
	    int columnNum=myTB.getCurrentTable().getSelectedColumn();
	    String result="";
	    String tempString1="";
	    double tempNum1=0.0;
	    String tempString2="";
	    double tempNum2=0.0;
	    double tempTotal=0.0;
	    double finalNum=0.0;
	    String finalString="";

	    if(columnNum==1||columnNum==2)
	    {
		for(int i=0;i<rowCount;i++)
		{
		    tempString1=(String)tm.getValueAt(i,1);
		    tempString2=(String)tm.getValueAt(i,2);
		    finalString="";

		    try
		    {
			tempNum1=Double.parseDouble(tempString1);
			tempNum2=Double.parseDouble(tempString2);
			tempTotal=tempNum1+tempNum2;
			if(columnNum==1)
			{
			    finalNum=tempNum1*100.0/tempTotal;
			}
			else
			{
			    finalNum=tempNum2*100.0/tempTotal;
			}
			finalString=finalNum+" ";
		    }catch(Exception e){ finalString="(NA)";};
		    tm.setValueAt(finalString,i,columnNum);
		}
		result="Succeed!";
	    }
	    else
	    {
		result="Sorry, we have not implement this column now";
	    }

	    return result;


	}


private void initialControlPanel()
{
	sortOperations[0].setToolTipText("Maximum value for a specific column");
    sortOperations[1].setToolTipText("Minimum value for a specific column");
    sortOperations[2].setToolTipText("Average value for a specific column");
    sortOperations[3].setToolTipText("Persentage of the column to a specific column");
    zooms[0].setToolTipText("Zoom in the table");
    zooms[1].setToolTipText("Zoom out the table");
    deleteButton.setToolTipText("Delete one row or column");
    //custom.setToolTipText("Custom user\'s own table");
    sendButton.setToolTipText("Send Values to other applications");
    selectables[0].setToolTipText("Enter ROW model");
    selectables[1].setToolTipText("Enter COLUMN model");
    selectables[2].setToolTipText("Enter CELL model");
    firstColumnLocked.setToolTipText("Set the first column locked");



}

private void scrolTable(int y)
{
	    JScrollPane myScrollPane = (JScrollPane)SwingUtilities.getAncestorOfClass(
							    JScrollPane.class, myTB.getCurrentTable());
		JViewport myViewport=myScrollPane.getViewport();
		Point point= myViewport.getViewPosition();
		if(y<0) y=0;

		point.y =myTB.getCurrentTable().getRowHeight()*y;
		myViewport.setViewPosition(point);
		myScrollPane.setViewport(null);
		myScrollPane.setViewport(myViewport);
		myScrollPane.repaint();

}


}
