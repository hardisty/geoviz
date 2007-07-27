/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ShapeTransformer
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: ShapeTransformer.java,v 1.3 2005/04/11 17:37:47 hardisty Exp $
 $Date: 2005/04/11 17:37:47 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.data.transform;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import edu.psu.geovista.data.geog.DataSetForApps;
import edu.psu.geovista.ui.event.DataSetEvent;
import edu.psu.geovista.ui.event.DataSetListener;

/**
 * Transforms variables and adds them to the current working data set. 
 */
public class VariableTransformer extends JPanel implements DataSetListener, TableModelListener, ActionListener{
	DataSetForApps dataSet;
	JButton sendButton;
	JComboBox variableCombo;
	
	public VariableTransformer(){
		
	}
	public void dataSetChanged(DataSetEvent e) {
		this.dataSet = e.getDataSetForApps();
		this.dataSet.addTableModelListener(this);

		this.variableCombo.removeActionListener(this);
		this.variableCombo.removeAllItems();
		for (int i = 0; i < this.dataSet.getNumberNumericAttributes(); i++){
			this.variableCombo.addItem(this.dataSet.getNumericArrayName(i));
		}
		this.variableCombo.addActionListener(this);
	}
	

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.variableCombo)){
			this.dataSet.getNumericDataAsDouble(this.variableCombo.getSelectedIndex());

		}
		
	}
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		
	}


}
