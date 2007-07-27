package edu.psu.geovista.app.spreadsheet.functions;

import edu.psu.geovista.app.spreadsheet.exception.NoReferenceException;
import edu.psu.geovista.app.spreadsheet.exception.ParserException;
import edu.psu.geovista.app.spreadsheet.formula.Node;

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
