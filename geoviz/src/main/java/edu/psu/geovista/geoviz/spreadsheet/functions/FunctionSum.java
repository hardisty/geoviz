package edu.psu.geovista.geoviz.spreadsheet.functions;

import java.awt.Point;

import edu.psu.geovista.geoviz.spreadsheet.exception.NoReferenceException;
import edu.psu.geovista.geoviz.spreadsheet.exception.ParserException;
import edu.psu.geovista.geoviz.spreadsheet.formula.Cell;
import edu.psu.geovista.geoviz.spreadsheet.formula.Formula;
import edu.psu.geovista.geoviz.spreadsheet.formula.Node;
import edu.psu.geovista.geoviz.spreadsheet.table.SSTable;

/*
 * Description:
 *    <code>SUM</code><br>
 *    usage: <code>=SUM(parameter list)</code><br>
 *    returns the arithmetic sum of the specified parameters<br>
 *    example: <code>=SUM(-1,2,57)</code> returns <code>58.0</code>
 * Date: Mar 25, 2003
 * Time: 10:41:06 AM
 * @author Jin Chen
 */

public  class FunctionSum extends Function {
    private SSTable tb;

    public FunctionSum(SSTable tb) {
        this.tb = tb;
    }

    public Number evaluate( Node node) throws ParserException,NoReferenceException {
          //SSTable tb=SpreadSheetBean.getTableInstance() ;
          float sum=0;
          SelectionRange fr=getRangeArea(node);
          Point[] range=fr.getRange() ;
          int sx=(int)range[0].getX() ;
          int ex=(int)range[1].getX() ;
          int sy=(int)range[0].getY() ;
          int ey=(int)range[1].getY() ;
          if (fr.getType() ==SelectionRange.SINGLE_COLUMN ){
              int y=(int)range[0].getY() ;
              for (int x=sx;x<=ex;x++){
                  Cell cell=((Cell)tb.getValueAtIndex(x,y));
                  float v=Formula.processCellValue(cell.getValue()).floatValue();
                  sum=sum+v;
              }
          }
          else if(fr.getType() ==SelectionRange.SINGLE_ROW) {
             int x=(int)range[0].getX() ;
              for (int y=sy;y<=ey;y++){
                  Cell cell=((Cell)tb.getValueAtIndex(x,y));
                  float v=Formula.processCellValue(cell.getValue()).floatValue();
                  sum=sum+v;
              }
          }
         return new Float(sum);

    }

    public String getUsage() {
	return "SUM(value1,value2,...)";
    }

    public String getDescription() {
	return "Adds all the numbers in a set of values.";
    }
}//geovista.geoviz.spreadsheet.functions.FunctionSum
