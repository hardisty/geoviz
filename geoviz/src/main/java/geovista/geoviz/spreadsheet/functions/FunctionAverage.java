package geovista.geoviz.spreadsheet.functions;

import geovista.geoviz.spreadsheet.exception.NoReferenceException;
import geovista.geoviz.spreadsheet.exception.ParserException;
import geovista.geoviz.spreadsheet.formula.Node;

/*
 * Description:
 * <code>AVERAGE</code><br>
 *   usage: <code>=AVERAGE(parameter list)</code><br>
 *   returns the arithmetic mean of the specified parameters<br>
 *   example: <code>=AVERAGE(1,2,3)</code> returns <code>2.0</code>
 * Date: Mar 25, 2003
 * Time: 10:43:56 AM
 * @author Jin Chen
 */

public class FunctionAverage extends Function {

    public Number evaluate( Node node) throws ParserException,NoReferenceException {
        //Function sumf=Formula.getFuncHandler("SUM");
        Function sumf=this.getSupportFuntion("SUM",this.getOwner() );
        float sum=sumf.evaluate(node).floatValue() ;
        //Function fcf=Formula.getFuncHandler("FUN_COUNT");
        Function fcf=this.getSupportFuntion("FUN_COUNT",this.getOwner());
        int   nCells=fcf.evaluate(node).intValue() ;
        return new Float(sum/nCells);
    }

    public String getUsage() {
	return "AVERAGE(value1,value2,...)";
    }

    public String getDescription() {
	return "Returns the average (arithmetric mean) of its arguments.";
    }
}

