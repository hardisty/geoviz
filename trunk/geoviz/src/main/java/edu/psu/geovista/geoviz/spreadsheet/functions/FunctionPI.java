package edu.psu.geovista.geoviz.spreadsheet.functions;

import edu.psu.geovista.geoviz.spreadsheet.exception.NoReferenceException;
import edu.psu.geovista.geoviz.spreadsheet.exception.ParserException;
import edu.psu.geovista.geoviz.spreadsheet.formula.Node;

/*
 * Description:
 * Date: Apr 1, 2003
 * Time: 9:44:38 PM
 * @author Jin Chen
 */

public class FunctionPI extends FunctionSP {

    protected Number doFun(Node node)throws ParserException,NoReferenceException {
        // no parameters allowed
        if (node.getParams().size() != 0)
	    throw new ParserException("#PARAM?");
        return new Float(Math.PI);
    }

    public String getUsage() {
        return "PI()";
    }

    public String getDescription() {
        return "Returns the value of PI.";
    }
}
