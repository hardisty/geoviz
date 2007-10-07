package geovista.geoviz.spreadsheet.functions;

import java.util.HashMap;
import java.util.TreeSet;

import geovista.geoviz.spreadsheet.table.SSTable;

/*
 * Description:
 * Date: Apr 16, 2003
 * Time: 11:01:31 AM
 * @author Jin Chen
 */

public class FunctionManager {
    private SSTable table;

    // a hash table for function handlers
    private HashMap funcTable;

    public FunctionManager(SSTable tb) {
        this.table = tb;
        this.registerFunctions() ;
    }
   /**
     * Adds a function to the function table.
     *
     * @param funcName the name of the function
     * @param func the geovista.geoviz.spreadsheet.functions.Function object
     * @see geovista.geoviz.spreadsheet.functions.Function
     */
    private void register(String funcName, Function func) {
        funcTable.put(funcName, func);
    }

    /**
     * Registers the geovista.geoviz.spreadsheet.functions on the funcTable.
     * Should be called only once.
     */
    public void registerFunctions() {
	 funcTable = new HashMap();
     register("ABS", new FunctionAbs());
     register("ACOS", new FunctionAcos());
     register("ASIN", new FunctionAsin());
     register("ATAN", new FunctionAtan());
     register("AVG", new FunctionAverage());
     register("COS", new FunctionCos());
     register("E", new FunctionE());
     register("FUN_COUNT", new FunctionCount());
     register("INT", new FunctionInt());
     register("LOG", new FunctionLog());
     register("MEDIAN", new FunctionMedian(table));
     register("MYFUN", new FunctionCustomize());
     register("PI", new FunctionPI());
     register("ROUND", new FunctionRound());
     register("SIN", new FunctionSin());
     register("SQRT", new FunctionSqrt());
     register("SUM", new FunctionSum(table));
     register("TAN", new FunctionTan());

/*
	register("SUM", new FunctionSum());
	register("MEAN", new geovista.geoviz.spreadsheet.functions.FunctionAverage());
	register("AVERAGE", new geovista.geoviz.spreadsheet.functions.FunctionAverage());
	register("MEDIAN", new FunctionMedian());

	register("INT", new FunctionInt());
	register("ROUND", new FunctionRound());
	register("SIN", new FunctionSin());
	register("COS", new FunctionCos());
	register("TAN", new FunctionTan());
	register("ASIN", new FunctionAsin());
	register("ACOS", new FunctionAcos());
	register("ATAN", new FunctionAtan());
	register("SQRT", new FunctionSqrt());
	register("LOG", new FunctionLog());
	register("MIN", new FunctionMin());
	register("MAX", new FunctionMax());
	register("RANGE", new SelectionRange());
	register("STDDEV", new FunctionStddev());
	register("MEANDEV", new FunctionMeandev());
	register("COUNT", new FunctionCount());
	register("PI", new FunctionPI());
	register("E", new FunctionE());      */
    }

    /*
     * provide a way to access these function handlers
     *
     * @param fname the function name
     * @return the function object that can evaluate the specified function.
     *
     * @see Funciton
     * @see SharpTools
     */
    public Function getFuncHandler(String fname) {
            Function f=(Function)funcTable.get(fname);

            return  f;
    }

    public Object[] getFunctionNames(){
       TreeSet ts=new TreeSet(funcTable.keySet());
       return ts.toArray() ;

    }
}
