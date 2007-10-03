package edu.psu.geovista.geoviz.spreadsheet.functions;

import edu.psu.geovista.geoviz.spreadsheet.exception.NoReferenceException;
import edu.psu.geovista.geoviz.spreadsheet.exception.ParserException;
import edu.psu.geovista.geoviz.spreadsheet.formula.Node;

/*
 * Description:
 * Date: Apr 1, 2003
 * Time: 9:12:56 PM
 * @author Jin Chen
 */

public class FunctionAcos extends FunctionSP {

    protected Number doFun(Node node)throws ParserException,NoReferenceException {
        return new Float(Math.acos(getSingleParameter(node)));
    }

    public String getUsage() {
        return "ACOS(value)";
    }

    public String getDescription() {
        return "Returns the arccosine value of a number.";
    }
}
