/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 *  Description: It displays the geovista.geoviz.spreadsheet.formula string of a geovista.geoviz.spreadsheet.formula cell when cell is being edited.
 *              It inherits  all methods of the DefaultCellEditor.
 *
 * Date: Mar 13, 2003
 * Time: 11:54:25 AM
 * @author Jin Chen
 */

package edu.psu.geovista.geoviz.spreadsheet.formula;


import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
public class CellFomulaEditor extends DefaultCellEditor {
/** the JTextField object this editor uses
     */
    private JTextField cellField;
    /**
     *
     *
     * @param textField a JtextField object
     */
    public CellFomulaEditor(final JTextField textField,FormulaEditor _fe) {
        super(textField);
        this.cellField = textField;

/* To account for geovista.geoviz.spreadsheet.formula feature only need
         * to override the setValue method in EditorDelegate inner
         * class.
         */
        delegate = new EditorDelegate() {
            public void setValue(Object value) {


                if (value instanceof Cell) {
                    Cell temp = (Cell)value;

/* when editing geovista.geoviz.spreadsheet.formula cell
                     * a string representation is displayed
                     */
                    String v;
                    if (temp.isFormula()) {
                        v="=" + temp.getFormula().toString();
                        textField.setText(v);

                    }
                    else {

                        //otherwise it is just the normal string conversion
                        if (temp.getValue()!=null){
                            v=temp.getValue().toString();
                            textField.setText(v);
                        }
                        else{ //Must, otherwise if temp.getValue()==null, the last entered value will be put in the cell
                            v="";
                            textField.setText(v);
                        }
                    }
                    // fe.getFormulaField().setText(v);
                }
                else {

                    //empty cells display nothing
                    textField.setText((value == null) ? "" : value.toString());
                }   //if

            } //setValue()

            public Object getCellEditorValue() {
                    return textField.getText();
            }
        }; //inner class
        //MUST, thus it will listen when editing on textfield finished
        textField.addActionListener(delegate);
    }

    /** get the component used by this editor
     * @return the JTextField used by this editor
     */
    public JTextField getCellField() { return cellField; }
}
