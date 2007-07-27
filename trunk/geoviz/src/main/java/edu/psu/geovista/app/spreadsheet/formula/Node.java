/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 * Description:
 * Node is basic unit for Formula processing.
 * <p>
 * It can be one of the following:
 * <ol>
 * <li>Relative Address (LETTERS+numbers: A1)</li>
 * <li>Absolute Address ($LETTERS$numbers: $A$1)</li>
 * <li>functions.Function (LETTERS)</li>
 * <li>Left Parenthese</li>
 * <li>Right Parenthese</li>
 * <li>Number (a float number)</li>
 * <li>Operator (+ - * / % ^)</li>
 * <li>Comma (separating parameters)</li>
 * <li>Colon (used in range addresses: A1:C6)</li>
 * </ol>
 *
 * The code has some reference to code written by Hua Zhong from Columbia University
 * Apr 2, 2003
 * Time: 10:42:12 PM
 * @author Jin Chen
 */


package edu.psu.geovista.app.spreadsheet.formula;

import java.awt.Point;
import java.util.LinkedList;


public class Node {
    /** Based on tool ***/
    //public static int baseRow = 0;
    //public static int baseCol = 1;  //replace SharpTools.baseCol



    public static final int DEFAULT = 0; // reserved
    public static final int REL_ADDR = 1; // LETTERS+numbers: A1
    public static final int ABS_ADDR = 2; // $LETTERS$numbers: $A$1
    public static final int FUNCTION = 3; // LETTERS: SUM
    public static final int LPAREN = 4; // (
    public static final int RPAREN = 5; // )
    public static final int NUMBER = 6;  // all numbers and has .: 0.5
    public static final int OPERATOR = 7; // + - * / ^
    public static final int COMMA = 8; // ,
    public static final int COLON = 9; // :
    public static final int EXP = 10; // an expression.  the exp field counts.
    // for each function param, its type is EXP

//    private static final String[] desc = {
//    	"Default", "Rel_Addr", "Abs_Addr", "edu.psu.geovista.app.spreadsheet.functions.Function",
//    	"LBracket", "RBracket", "Number", "Operator",
//	"Comma", "Colon", "Param" };

    private int type; // which type the node is (see above 10 types)
    private String data; // the raw data. Jin: the expression
    private float number; // the numeric value. Jin: the value
    private int row;
    private int col;

    private LinkedList exp; // a LinkedList for a function's params

    /*
     * Used for edu.psu.geovista.app.spreadsheet.functions.Function address range parameter (ADDR1:ADDR2)
     * An address range is ultimately represented as follows:
     *
     * node type: COLON
     * node.nextRange points to the start address (a node of REL_ADDR or
     * ABS_ADDR), the start address' nextRange points to the end address.
     */
    private Node nextRange;

    private boolean pending; // used for processing functions, see edu.psu.geovista.app.spreadsheet.formula.Formula


    //Jin
    private Cell reference;//Only used by Relative or Absolute address node, store the cell the address refer to
    private Point address;//Store absolute address

    /**
     * This is an empty node constructor
     */
    Node() {
    }

    /**
     * edu.psu.geovista.app.spreadsheet.formula.Node constructor
     *
     * @param node
     */
    Node(Node node) {
	type = node.type;
	if (data != null)
	    data = new String(node.data);
	number = node.number;
	row = node.row;
	col = node.col;
    }
    /**
     *  For relative address
     */
    public Cell getReference() {
        return reference;//edu.psu.geovista.app.spreadsheet.formula.Cell object
    }
    public void setReference(Cell reference) {
        this.reference = reference;
        setType(Node.REL_ADDR);
        this.address =null;  //absolute address
    }

    /**
     *  For absolute address
     */
    public Point getAddress(){
        return this.address ;
    }

    public void setAddress(Point address) {
        this.address = address;
        setType(Node.ABS_ADDR );
        this.reference =null;
    }
    public void setAddress(int x, int y) {
        this.setAddress(new Point(x,y));
    }

    /** get/set edu.psu.geovista.app.spreadsheet.functions */
    public int getType() { return type; }
    public void setType(int type) {
        this.type = type;
    }
    public boolean isType(int type) { return this.type == type; }

    public float getNumber() { return number; }
    public String getData() { return data; }

    public LinkedList getParams() { return exp; }
    public Node getNextRange() { return nextRange; }
    //Return the parameters
    public LinkedList getExp() { return exp; }

    //public int getRow() { return row; }
    //public int getCol() { return col; }


    public void setNumber(float number) { this.number = number; }

    public void setData(String data) { this.data = data; }
    public void appendData(char data) { this.data += data; }
    public void appendData(String data) { this.data += data; }

    public void setParams(LinkedList list) { exp = list; }
    public void addParam(Node node) {
	if (node.getExp().size()>0)
	    exp.add(node);
    }

    public void setNextRange(Node node) { nextRange = node; }
    public void setExp(LinkedList exp) { this.exp = exp; }

    public void setPending(boolean pending) { this.pending = pending; }
    public boolean isPending() { return pending; }




}
