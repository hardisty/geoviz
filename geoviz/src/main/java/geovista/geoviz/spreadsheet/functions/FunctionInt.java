package geovista.geoviz.spreadsheet.functions;

import geovista.geoviz.spreadsheet.exception.NoReferenceException;
import geovista.geoviz.spreadsheet.exception.ParserException;
import geovista.geoviz.spreadsheet.formula.Node;

/*
 * Description:
 * Date: Apr 1, 2003
 * Time: 9:40:19 PM
 * @author Jin Chen
 */

public class FunctionInt extends FunctionSP {

    protected Number doFun(Node node)throws ParserException,NoReferenceException {
        return new Float((int)getSingleParameter(node));
    }

    public String getUsage() {
       return "INT(value)";
    }

    public String getDescription() {
        	return "Returns the integer part of a number.";
    }
}
