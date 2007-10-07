/* -------------------------------------------------------------------
 Java source file for the class CartogramChooseVariable
 Copyright (c), 2005 Frank Hardisty
 $Author: hardistf $
 $Id: CartogramChooseVariable.java,v 1.1 2005/12/05 20:17:05 hardistf Exp $
 $Date: 2005/12/05 20:17:05 $
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 -------------------------------------------------------------------   */

package geovista.cartogram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumnModel;

import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.geoviz.map.GeoMapUni;
import edu.psu.geovista.geoviz.visclass.VisualClassifier;
import geovista.common.classification.ClassifierPicker;


/*
 * This class is the second step in the CartogramWizard
 */


public class CartogramChooseVariable extends JPanel implements
        ListSelectionListener, TableColumnModelListener, ActionListener {
    JLabel stepLabel;
    JScrollPane tablePane;
    DataSetForApps dataSet;
    JList varList;
    JPanel centerPanel;
    GeoMapUni map;
    JTable table;
    VisualClassifier visClassOne;


    /**
     * CartogramChooseVariable
     */
    public CartogramChooseVariable() {
        stepLabel = new JLabel(
                "Step Two: Choose which variable the size of the areas will be based on.");
        stepLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        stepLabel.setBackground(Color.PINK);
        stepLabel.setOpaque(true);
        BorderLayout border = new BorderLayout();
        this.setLayout(border);
        this.add(stepLabel, BorderLayout.SOUTH);
        varList = new JList();

        varList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //this.add(picker, BorderLayout.WEST);
        JScrollPane scrollPane = new JScrollPane();
        JViewport scrollView = new JViewport();
        scrollView.add(varList);
        scrollPane.setViewport(scrollView);
        scrollPane.getViewport();
        scrollPane.setMinimumSize(new Dimension(80, 300));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Variables"));
        this.add(scrollPane, BorderLayout.WEST);
        this.centerPanel = new JPanel();

    }

    public void valueChanged(ListSelectionEvent e) {

        int whichVar = this.varList.getSelectedIndex();
        if (whichVar < 0) {
            return;
        }
        this.map.setXVariable(whichVar);
        this.table.setColumnSelectionInterval(whichVar, whichVar);
        Rectangle aRect = this.table.getCellRect(0, whichVar, true);
        this.table.scrollRectToVisible(aRect);

    }

    public void createTable() {
        int nColumns = this.dataSet.getNumberNumericAttributes();
        int nRows = this.dataSet.getNumObservations();
        Object[][] data = new Object[nRows][nColumns];
        Object[] names = dataSet.getAttributeNamesNumeric();

        for (int column = 0; column < nColumns; column++) {

            for (int row = 0; row < nRows; row++) {
                Double num = new Double(dataSet.getNumericValueAsDouble(column,
                        row));
                data[row][column] = num;
            }
        }
        table = new JTable(data, names);
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablePane = new JScrollPane(table);
        tablePane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        tablePane.setBorder(BorderFactory.createTitledBorder("Data Set"));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.
                                               HORIZONTAL_SCROLLBAR_AS_NEEDED);
        varList.addListSelectionListener(this);
        TableColumnModel model = table.getColumnModel();
        model.addColumnModelListener(this);

    }

    public void setDataSet(DataSetForApps dataSet) {
        if (dataSet != null && dataSet != this.dataSet) {
            this.dataSet = dataSet;
            String[] vars = dataSet.getAttributeNamesNumeric();
            this.varList.removeAll();
            this.varList.setListData(vars);
            this.createTable();
            if(dataSet.getNumberNumericAttributes() >= 1){
                this.varList.setSelectedIndex(0);
            }
            this.doCenterPaneLayout();
        }
    }

    public DataSetForApps getDataSet() {
        return this.dataSet;
    }

    public void setMap(GeoMapUni map) {
        this.map = map;
        this.doCenterPaneLayout();
        this.visClassOne = map.getVisClassOne();
        this.visClassOne.addActionListener(this);
    }

    private void doCenterPaneLayout() {
        if (this.map == null) {
            this.map = new GeoMapUni();
        }
        if (this.tablePane == null) {
            this.tablePane = new JScrollPane();
        }
        Dimension stuffSize = new Dimension(500, 200);
        this.map.setPreferredSize(stuffSize);
        this.tablePane.setPreferredSize(stuffSize);
        this.centerPanel.removeAll();
        GridLayout grid = new GridLayout(2, 1);
        this.centerPanel.setLayout(grid);
        this.centerPanel.add(this.map);
        this.centerPanel.add(this.tablePane);
        this.add(centerPanel);

    }

    public void columnAdded(TableColumnModelEvent e) {}

    public void columnMarginChanged(ChangeEvent e) {}

    public void columnMoved(TableColumnModelEvent e) {}

    public void columnRemoved(TableColumnModelEvent e) {}

    public void columnSelectionChanged(ListSelectionEvent e) {
        int whichVar = this.table.getSelectedColumn();
        this.map.setXVariable(whichVar);
        this.varList.setSelectedIndex(whichVar);

    }

    public void actionPerformed(ActionEvent e) {
        String varChangedCommand = ClassifierPicker.
                                   COMMAND_SELECTED_VARIABLE_CHANGED;
        if ((e.getSource() == this.visClassOne) &&
            e.getActionCommand().equals(varChangedCommand)) {
            int whichVar = map.getCurrentVariable();
            this.varList.setSelectedIndex(whichVar);
            this.table.setColumnSelectionInterval(whichVar, whichVar);
            Rectangle aRect = this.table.getCellRect(0, whichVar, true);
            this.table.scrollRectToVisible(aRect);
        }

    }


}
