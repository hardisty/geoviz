package edu.psu.geovista.app.spreadsheet.functions;

import edu.psu.geovista.app.spreadsheet.exception.NoReferenceException;
import edu.psu.geovista.app.spreadsheet.exception.ParserException;
import edu.psu.geovista.app.spreadsheet.formula.Node;
import edu.psu.geovista.app.spreadsheet.util.Debug;

/*
 * Description: Take a single parameter
 * Date: Mar 30, 2003
 * Time: 4:00:10 PM
 * @author Jin Chen
 */

public abstract class FunctionSP extends Function{
    public Number evaluate( Node node) throws ParserException,NoReferenceException {
        Debug.showNode(node," FunctionSin() show node "+node);
        //return new Float(Math.sin(getSingleParameter( node)));
        return this.doFun(node);
    }
    protected abstract Number doFun(Node node) throws ParserException,NoReferenceException ;

    public abstract String getUsage();
    public abstract String getDescription();
}
