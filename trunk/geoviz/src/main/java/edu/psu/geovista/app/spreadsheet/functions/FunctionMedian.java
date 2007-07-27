package edu.psu.geovista.app.spreadsheet.functions;

import java.awt.Point;

import edu.psu.geovista.app.spreadsheet.exception.NoReferenceException;
import edu.psu.geovista.app.spreadsheet.exception.ParserException;
import edu.psu.geovista.app.spreadsheet.formula.Cell;
import edu.psu.geovista.app.spreadsheet.formula.Node;
import edu.psu.geovista.app.spreadsheet.table.SSTable;

/*
 * Description:
 * Date: Mar 26, 2003
 * Time: 12:12:28 PM
 * @author Jin Chen
 */

public class FunctionMedian extends Function{
    private SSTable tb;

    public FunctionMedian(SSTable tb) {
        this.tb = tb;
    }

    public Number evaluate( Node node) throws ParserException,NoReferenceException {

        Number result=null;
        SelectionRange fr=getRangeArea(node);
        Point[] ps=fr.getRange() ;
        //SSTable tb=SpreadSheetBean.getTableInstance() ;
        if (fr.getType() ==SelectionRange.SINGLE_COLUMN ){
            int vy;
             vy=(int)ps[0].getY() ;
             int sx=(int)ps[0].getX() ;  //starting row
             int ex=(int)ps[1].getX() ;  //end row
             int[] vx=this.getMedianIndex(sx,ex);
             if (vx[1]==-1){//odd number of cells
                 result=(Number)((Cell)tb.getValueAtIndex(vx[0],vy)).getValue() ;
             }
             else{//even number of cells
                 Number r0=(Number)((Cell)tb.getValueAtIndex(vx[0],vy)).getValue() ;
                 Number r1=(Number)((Cell)tb.getValueAtIndex(vx[1],vy)).getValue() ;
                 float md=(r0.floatValue() +r1.floatValue() )/2;
                 result=new Float(md);
             }
        }
        else if(fr.getType() ==SelectionRange.SINGLE_ROW ){
            int vx;
             vx=(int)ps[0].getX() ;
             int sy=(int)ps[0].getY() ;  //starting row
             int ey=(int)ps[1].getY() ;  //end row
             int[] vy=this.getMedianIndex(sy,ey);
             if (vy[1]==-1){//odd number of cells
                 result=(Number)((Cell)tb.getValueAtIndex(vx,vy[0])).getValue() ;
             }
             else{//even number of cells
                 Number r0=(Number)((Cell)tb.getValueAtIndex(vx,vy[0])).getValue() ;
                 Number r1=(Number)((Cell)tb.getValueAtIndex(vx,vy[1])).getValue() ;
                 float md=(r0.floatValue() +r1.floatValue() )/2;
                 result=new Float(md);
             }
        }
        else{

            assert false :"Can't handle";

        }
        return result;

    }
    /**
     *  si,ei : start index, end index
     *  return int[]  median index
     */
    private int[] getMedianIndex(int si,int ei) {
        int index[]=new int[2];
        int nCells=Math.abs(si-ei)+1;
        int middle=0;
        if (nCells%2==0){//even number
            middle=(si+ei)/2;
            index[0]=middle;
            index[1]=middle+1;

        }
        else{//odd
            middle=(si+ei)/2;
            index[0]=middle;
            index[1]=-1;//means no index available
        }
        return index;

    }
    public String getUsage() {
        return "MEDIAN(cell1:cell2)";
    }

    public String getDescription() {
        return "Caculate a median value for a given row/column range.";
    }
}
