/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 * Description:
 * Function classes used only by geovista.geoviz.spreadsheet.formula.Formula to evaluate functions.
 * Any function needs to have a function handler that implements
 * the "evaluate" interface of the base class functions.Function.
 * A function can accept zero, one, or more parameters.  Each parameter
 * is a number, a relative/absolute address or an address range (e.g., A1:B3).

 * The code has some reference to code written by Hua Zhong from Columbia University
 *
 * Apr 2, 2003
 * Time: 10:42:12 PM
 * @author Jin Chen
 */

package edu.psu.geovista.geoviz.spreadsheet.functions;



import java.awt.Point;
import java.util.LinkedList;

import edu.psu.geovista.geoviz.spreadsheet.exception.NoReferenceException;
import edu.psu.geovista.geoviz.spreadsheet.exception.ParserException;
import edu.psu.geovista.geoviz.spreadsheet.formula.Cell;
import edu.psu.geovista.geoviz.spreadsheet.formula.Formula;
import edu.psu.geovista.geoviz.spreadsheet.formula.Node;
import edu.psu.geovista.geoviz.spreadsheet.util.Debug;


public abstract class Function {

    static private ParserException exception = new ParserException("#PARAM?");
    //protected HashSet  owners;//who use the function now
    protected Formula owner;

    // whether the specified parameter node is an address range
    protected boolean isRange(Node param) {
	LinkedList exp = param.getExp();
	return exp.size() == 1 &&
	    ((Node)exp.getFirst()).isType(Node.COLON);
        //((geovista.geoviz.spreadsheet.formula.Node)param.getExp().getFirst()).isType(geovista.geoviz.spreadsheet.formula.Node.COLON);
    }

    // return the first node of a specified parameter
    protected Node getFirst(Node param) {
        return (Node)param.getExp().getFirst();
    }

    // whether this function has any parameter
    protected static void checkParamsExist(Node func) throws ParserException {

        if (func.getParams().size()==0)
	    throw exception;
    }

    /**
     * This gets the first float number of a parameter list, for functions
     * only accepting a single parameter such as <code>ABS</code>, <code>COS
     * </code>, etc.
     *
     * @param table the SharpTabelModel
     * @param node the geovista.geoviz.spreadsheet.formula unit
     * @param col the int column coordinate
     * @param row the int row coordinate
     * @return the float number
     */
    protected float getSingleParameter( Node node)
        throws ParserException,NoReferenceException {
        //	geovista.geoviz.spreadsheet.formula.Node param = node.getNextParam();
        LinkedList params = node.getParams();
        Debug.showLinkedList(params,"getSingleParameter( ) show params of node "+node );
        if (params.size() != 1)
	    throw new ParserException("#PARAM?");

        LinkedList exp = ((Node)params.getFirst()).getExp();  //??? why first, what if there is multiple parameters
        Debug.showLinkedList(exp,"getSingleParameter( ) show exp "+exp );
        //Formula f=new Formula(this.getOwners() );
        return this.getOwner().evaluate(exp) .floatValue();

    }


    /**
         * This should be implemented in each function.
         *
         * @param table the SharpTabelModel
         * @param node the function node starting with the funciton name
         * with a chain of parameters
         * @param col the int column coordinate
         * @param row the int row coordinate
         * @geovista.geoviz.spreadsheet.exception ParserException
         */
    public abstract Number evaluate(Node node) throws ParserException,NoReferenceException;



    /**
     * Return the usage of the function
     */
    public abstract String getUsage();

    /**
     * Return the description of the function
     */
    public abstract String getDescription();

    /**
     * Whether this function requires parameters.
     * By default yes.
     * @see FunctionPI
     * @see FunctionE
     */
    public boolean requireParams() { return true; }

    /**
     *  Jin:
     *  Only for those function whose arguments is set of range.
     *  e.g.: SUM, AVG,MEAN...
     *  @param  node the function node.
     *  @return  address(x,y) in view
     */
    public static SelectionRange getRangeArea(Node node) throws ParserException{
            SelectionRange range=null;

            checkParamsExist(node);
            LinkedList params = node.getParams();//only 1 element
            Node exp=(Node)params.getFirst() ;// expresion Node(type=Node.EXP). e.g. a1:a2
            Node colonNode=((Node)exp.getParams().getFirst());// colon Node(type=Node.COLON)  e.g.: a1:a2
            range=processColonNode(colonNode);

            return range;
    }
    /**
     *  convert a colon node(which contain 2 ranges) into a SelectionRange
     */
    public static SelectionRange processColonNode(Node colonNode)throws ParserException {
            SelectionRange range=null;
            Point[] ps=new Point[2];//a 2 points(sp,ep)
            Point sp=null,ep=null;//start point, end point of the range
            //!!! following part is same as Formula's evalueExpression
            Node start=colonNode.getNextRange() ;//start(upLeft) of the range
            Node end=start.getNextRange() ;//end (downRight) of the range

            if (start.getType() ==Node.REL_ADDR ){
                Cell cell=start.getReference() ;
                sp=cell.getViewAddress();

                Debug.println("Cell address :"+cell.getViewAddress() );
            }
            else if(start.getType() ==Node.ABS_ADDR ){
                sp=start.getAddress();
                Debug.println("AB address:"+start.getAddress() );
            }
            else{
                assert false: "Unable to handle the expression";
                //throw geovista.geoviz.spreadsheet.exception: unable to handle the expression
            }
            if (end.getType() ==Node.REL_ADDR ){
                Cell cell=end.getReference() ;
                ep=cell.getViewAddress();
                Debug.println("Cell address :"+cell.getViewAddress() );
            }
            else if(end.getType() ==Node.ABS_ADDR ){
                ep=end.getAddress() ;
                Debug.println("AB address:"+end.getAddress() );
            }
            else{

                assert false: "Unable to handle the expression";
                //throw geovista.geoviz.spreadsheet.exception: unable to handle the expression
            }
            if (sp.getX()==ep.getX()){//a single column range
                if (sp.getY() <=ep.getY()){
                    ps[0]=sp;
                    ps[1]=ep;
                }
                else{
                    ps[0]=ep;
                    ps[1]=sp;
                }
                range=new SelectionRange(ps,SelectionRange.SINGLE_ROW );
            }
            else if (sp.getY()==ep.getY()){//a single row range
               if (sp.getX() <=ep.getX()){
                    ps[0]=sp;
                    ps[1]=ep;
                }
                else{
                    ps[0]=ep;
                    ps[1]=sp;
                }
                range=new SelectionRange(ps,SelectionRange.SINGLE_COLUMN  );

            }
            else{ //assume only handle a single column range or a single row range with sp above ep
                throw new ParserException("Unable to calculate on the given range.");
            }
            return range;
    }
     /*
    public HashSet getOwners() {
        return owners;
    }

    public void setOwners(HashSet owners) {
        this.owners = owners;
    }          */

    protected Function getSupportFuntion(String fname, Formula owner){
        //Function sf=Formula.getFuncHandler(fname);
        Function sf=owner.getFunctionManager().getFuncHandler(fname);
        sf.setOwner(owner);
        return sf;
    }

    public void setOwner(Formula owner) {
        this.owner = owner;
    }

    public Formula getOwner() {
        return owner;
    }
} //geovista.geoviz.spreadsheet.functions.Function Class








