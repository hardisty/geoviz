package geovista.geoviz.spreadsheet.functions;

import geovista.geoviz.spreadsheet.exception.NoReferenceException;
import geovista.geoviz.spreadsheet.exception.ParserException;
import geovista.geoviz.spreadsheet.formula.Node;

/*
 * Description:
 * Date: Apr 1, 2003
 * Time: 9:18:04 PM
 * @author Jin Chen
 */

public class FunctionAtan extends FunctionSP {

    protected Number doFun(Node node)throws ParserException,NoReferenceException {
        return new Float(Math.atan(getSingleParameter(node)));
    }

    public String getUsage() {
        return "ATAN(value)";
    }

    public String getDescription() {
        return "Returns the arctangent value of a number.";
    }
}
