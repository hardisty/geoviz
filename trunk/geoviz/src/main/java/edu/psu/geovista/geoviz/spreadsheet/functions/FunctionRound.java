package edu.psu.geovista.geoviz.spreadsheet.functions;

import edu.psu.geovista.geoviz.spreadsheet.exception.NoReferenceException;
import edu.psu.geovista.geoviz.spreadsheet.exception.ParserException;
import edu.psu.geovista.geoviz.spreadsheet.formula.Node;

/*
 * Description:
 * Date: Apr 1, 2003
 * Time: 9:46:16 PM
 * @author Jin Chen
 */

public class FunctionRound extends FunctionSP {

    protected Number doFun(Node node)throws ParserException,NoReferenceException {
        return new Float(Math.round(getSingleParameter(node)));
    }

    public String getUsage() {
        return "ROUND(value)";
    }

    public String getDescription() {
        return "Returns the nearest integer of a number.";
    }
}
