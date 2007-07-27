package edu.psu.geovista.app.spreadsheet.functions;

import edu.psu.geovista.app.spreadsheet.exception.NoReferenceException;
import edu.psu.geovista.app.spreadsheet.exception.ParserException;
import edu.psu.geovista.app.spreadsheet.formula.Node;

/*
 * Description:
 * Date: Apr 1, 2003
 * Time: 9:19:50 PM
 * @author Jin Chen
 */

public class FunctionCos extends FunctionSP {

    protected Number doFun(Node node)throws ParserException,NoReferenceException {
        return new Float(Math.cos(getSingleParameter(node)));
    }

    public String getUsage() {
        return "COS(value)";
    }

    public String getDescription() {
        return "Returns the cosine value of a number.";
    }
}