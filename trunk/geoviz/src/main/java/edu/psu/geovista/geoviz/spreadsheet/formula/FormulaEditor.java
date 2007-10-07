/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 * Description:
 *              The bar for editing a cell's geovista.geoviz.spreadsheet.formula
 * Date: Mar 25, 2003
 * Time: 10:39:53 AM
 * @author Jin Chen
 */

package edu.psu.geovista.geoviz.spreadsheet.formula;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.psu.geovista.geoviz.spreadsheet.table.SSTable;



public class FormulaEditor extends JPanel{
  JTextField cellF;   //cell field show address of the cell
  JTextField formulaF;//geovista.geoviz.spreadsheet.formula address for editing
  SSTable tb;
  public FormulaEditor(SSTable _tb) {
      this.tb = _tb;
      cellF=new JTextField("                  ");
      cellF.setHorizontalAlignment(JTextField.CENTER );
      cellF.setForeground(Color.BLUE);
      cellF.setEditable(false);

      formulaF=new JTextField();
      formulaF.addKeyListener(new KeyListener(){
            public void keyTyped(KeyEvent e){
                char c=e.getKeyChar();
                if (c=='\n'){
                    tb.editingStopped(null);
                    int column=tb.getSelectedColumn() ;
                    int row=tb.getSelectedRow()+1 ;
                    tb.requestFocus() ;
                    //move to next cell
                    tb.setRowSelectionInterval(row,row);
                    tb.setColumnSelectionInterval(column,column);

                    //cellRender.requestFocus() ;
                    /*CellFomulaEditor editor=(CellFomulaEditor)tb.getCellEditor(row,column) ;
                    Component cp=editor.getComponent() ;
                    cp.requestFocus() ;   */
                    //tb.requestFocus();
                }

            }
            public void keyPressed(KeyEvent e){}
            public void keyReleased(KeyEvent e){
                //synchronize the value in geovista.geoviz.spreadsheet.formula editor and cell editor
                String input=formulaF.getText();
                int column=tb.getSelectedColumn() ;
                int row=tb.getSelectedRow() ;
                CellFomulaEditor editor=(CellFomulaEditor)tb.getCellEditor(row,column) ;
                JTextField cellF=editor.getCellField() ;
                cellF.setText(input);
            }
      });
      formulaF.addFocusListener(new FocusListener(){
          //If input on FormulaField on bar, the corresponding cell is in edit state
          public void focusGained(FocusEvent e) {
              int column=tb.getSelectedColumn() ;
              int row=tb.getSelectedRow() ;
              tb.editCellAt(row,column);//
          }
          public void focusLost(FocusEvent e){}
      });
      this.setLayout(new BorderLayout());
      this.add(cellF,BorderLayout.WEST );
      this.add(formulaF,BorderLayout.CENTER );
  }

    public JTextField getCellField() {
        return cellF;
    }

    public JTextField getFormulaField() {
        return formulaF;
    }
}