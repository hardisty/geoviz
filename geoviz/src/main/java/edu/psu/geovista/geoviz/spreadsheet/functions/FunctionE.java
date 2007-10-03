package edu.psu.geovista.geoviz.spreadsheet.functions;

import edu.psu.geovista.geoviz.spreadsheet.exception.NoReferenceException;
import edu.psu.geovista.geoviz.spreadsheet.exception.ParserException;
import edu.psu.geovista.geoviz.spreadsheet.formula.Node;

/*
 * Description:
 * Date: Apr 1, 2003
 * Time: 9:33:17 PM
 * @author Jin Chen
 */

public class FunctionE extends FunctionSP {

    protected Number doFun(Node node)throws ParserException,NoReferenceException {
        // no parameters allowed
        if (node.getParams().size() != 0)
            throw new ParserException("#PARAM?");
        return new Float(Math.E);

    }

    public String getUsage() {
        return "E()";
    }

    public String getDescription() {
        return "Returns value of e.";
    }
}