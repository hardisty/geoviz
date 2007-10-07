package geovista.geoviz.spreadsheet.functions;

import geovista.geoviz.spreadsheet.exception.NoReferenceException;
import geovista.geoviz.spreadsheet.exception.ParserException;
import geovista.geoviz.spreadsheet.formula.Node;

/*
 * Description:
 * Date: Apr 1, 2003
 * Time: 9:49:23 PM
 * @author Jin Chen
 */

public class FunctionTan extends FunctionSP {

    protected Number doFun(Node node)throws ParserException,NoReferenceException {
        return new Float(Math.tan(getSingleParameter(node)));
    }

    public String getUsage() {
        return "TAN(value)";
    }

    public String getDescription() {
        return "Returns the tangent of an angle.";
    }
}
