package geovista.geoviz.spreadsheet.functions;

import geovista.geoviz.spreadsheet.exception.NoReferenceException;
import geovista.geoviz.spreadsheet.exception.ParserException;
import geovista.geoviz.spreadsheet.formula.Node;

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