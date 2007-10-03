package edu.psu.geovista.geoviz.spreadsheet.functions;

import java.util.LinkedList;

import edu.psu.geovista.geoviz.spreadsheet.exception.NoReferenceException;
import edu.psu.geovista.geoviz.spreadsheet.exception.ParserException;
import edu.psu.geovista.geoviz.spreadsheet.formula.Node;

/*
 * Description:
 * <code>COUNT</code><br>
 *   usage:  <code>=COUNT(parameter list)</code><br>
 *   returns Given a function the number of parameters specified<br>
 *   example: <code>=COUNT(A1:A7)</code> returns <code>7.0</code>
 * Date: Mar 25, 2003
 * Time: 10:42:38 AM
 * @author Jin Chen
 */

public class FunctionCount extends Function {

    public Number evaluate( Node node) throws ParserException,NoReferenceException {
        int count=0;
        // requires parameters
        checkParamsExist(node);
        LinkedList params = node.getParams();//only 1 element
        Node exp=(Node)params.getFirst() ;// expresion Node(type=Node.EXP). e.g. a1:a2
        if (exp.getType() ==Node.EXP ){
            Node paraNode=((Node)exp.getParams().getFirst());// colon Node(type=Node.COLON)  e.g.: a1:a2
            if (paraNode.getType() ==Node.COLON ){
                 SelectionRange range=processColonNode(paraNode);
                 if (range.getType() ==SelectionRange.SINGLE_COLUMN ){
                     count=range.getRowCount() ;
                 }
                 else if ( range.getType() ==SelectionRange.SINGLE_ROW ){
                    count=range.getColumnCount() ;
                 }

            }
        }
        return new Integer(count);
    }//evaluate

    public String getUsage() {
	return "COUNT(value1,value2,...)";
    }

    public String getDescription() {
	return "Counts the number of cells that contain numbers and numbers within the list of arguments.";
    }
}
