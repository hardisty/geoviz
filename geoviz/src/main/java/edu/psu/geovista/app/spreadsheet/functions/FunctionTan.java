package edu.psu.geovista.app.spreadsheet.functions;

import edu.psu.geovista.app.spreadsheet.exception.NoReferenceException;
import edu.psu.geovista.app.spreadsheet.exception.ParserException;
import edu.psu.geovista.app.spreadsheet.formula.Node;

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
