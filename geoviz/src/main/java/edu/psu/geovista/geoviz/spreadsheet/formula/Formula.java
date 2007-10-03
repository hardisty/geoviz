/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 * Description:
 *   - For formula processing
 *   - The code reference to code written by Hua Zhong from Columbia University
 * Apr 2, 2003
 * Time: 10:42:12 PM
 * @author Hua Zhong
 * @author Jin Chen
 */

package edu.psu.geovista.geoviz.spreadsheet.formula;

import java.awt.Point;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import edu.psu.geovista.geoviz.spreadsheet.exception.NoReferenceException;
import edu.psu.geovista.geoviz.spreadsheet.exception.ParserException;
import edu.psu.geovista.geoviz.spreadsheet.functions.Function;
import edu.psu.geovista.geoviz.spreadsheet.functions.FunctionManager;
import edu.psu.geovista.geoviz.spreadsheet.table.SSTable;
import edu.psu.geovista.geoviz.spreadsheet.table.SSTableModel;
import edu.psu.geovista.geoviz.spreadsheet.util.Debug;



public class Formula {
    //jin:
    private Number value;
    // the raw edu.psu.geovista.geoviz.spreadsheet.formula string
    private String formulaString;  // jin: the expression
    //private String expression;


    // tokens in order of postfix - used in calculation
    private Cell owner;//direct owner
    private HashSet owners; // all owners, including direct and indirect owner
    private LinkedList nodes; // The nodes make up of the fomula.
    private LinkedList postfix;

    // error message
    private ParserException error;

    // whether this edu.psu.geovista.geoviz.spreadsheet.formula needs recalculation
    private boolean needsRecalc;

    /**
     * edu.psu.geovista.geoviz.spreadsheet.formula.Formula contructor.
     *
     * This is used to construct a edu.psu.geovista.geoviz.spreadsheet.formula.Formula object without any
     * parsing process.
     *
     * @param input the edu.psu.geovista.geoviz.spreadsheet.formula string
     * @param row the current row where the edu.psu.geovista.geoviz.spreadsheet.formula is stored
     * @param col the current column where the forluma is stored
     * @param e a edu.psu.geovista.geoviz.spreadsheet.exception.ParserException
     */
    public Formula(String input, int row, int col, ParserException e) {
        formulaString = input.toUpperCase();
        error = e;
    }

    /**
     * edu.psu.geovista.geoviz.spreadsheet.formula.Formula contructor.
     *
     * Parse the input string and translate into postfix form.
     *
     * @param input the edu.psu.geovista.geoviz.spreadsheet.formula string
     * @param row the current row where the edu.psu.geovista.geoviz.spreadsheet.formula is stored
     * @param col the current column where the forluma is stored
     * @edu.psu.geovista.geoviz.spreadsheet.exception edu.psu.geovista.geoviz.spreadsheet.exception.ParserException
     * @see #toPostfix
     */
    public Formula(Cell owner, String input, int row, int col) throws ParserException {

            formulaString = input.toUpperCase();
            try {
                this.owner =owner;
                this.getOwners().add(owner);
                // tokenize and convert the edu.psu.geovista.geoviz.spreadsheet.formula to postfix form (a list of edu.psu.geovista.geoviz.spreadsheet.formula.Node)
                nodes= tokenize(formulaString);
                //Debug.println("edu.psu.geovista.geoviz.spreadsheet.formula.Formula() nodes: "+nodes);
                //dependency = createDependency(nodes);//this depend on cells in dependency to evaluate
                //Debug.println("Dependency: "+dependency);
                postfix = toPostfix(convertParams(nodes));
                //this.showTokens(postfix);
                Debug.println("Postfix: "+postfix);
            }catch (ParserException e) {
                Debug.println("edu.psu.geovista.geoviz.spreadsheet.formula.Formula constructor: "+e);
                throwError(e);
            }
    }
    /*
    public Formula(HashSet owners){
        this.owners =owners;

    }      */


    /********************************************************************
     *                  Return value                                    *
     ********************************************************************/
     public  Number getValue(){
        return this.value ;
     }
     /*public String getExpression(){
         return this.expression ;
     }        */
     /**
      *  generate expression based on given node
      *  Not yet
      */
     private void evalueExpression(LinkedList nodes){
        if (this.isBad() ){//for invalid edu.psu.geovista.geoviz.spreadsheet.formula not passing
            return;
        }
        StringBuffer sb=new StringBuffer();
        Iterator it=nodes.iterator() ;
        while(it.hasNext() ){
            Node node=(Node)it.next() ;
            if (node.isType(Node.REL_ADDR) )
            {   Cell cell=node.getReference() ;
                Point address=cell.getViewAddress() ;  //address in Table
                if (address!=null){
/*int x=(int)address.getX() ;
                    int y=(int)address.getY();
                    //String row=edu.psu.geovista.geoviz.spreadsheet.formula.Node.translateRow(x );
                    String row=Integer.toString(x);
                    String col=Cell.translateVVColumn(y);//translate to String. e.g. 1=>A
                    addrs=col+row;
                    System.out.print("node:("+addrs+")");   */
                    sb.append(cell.getViewAddressText() );
                }
                else{
                     sb.append("#REF!");
                }
            }
            else if(node.isType(Node.ABS_ADDR )){

                Point address=node.getAddress() ;//absolute address
                int x=(int)address.getX() ; //view's row
                int y=(int)address.getY(); //view's col
                String row=translateRow(x);
                String col=SSTable.translateVVColumn(y);///.translateColumn(y);
                String addrs="$"+col+"$"+row;
                System.out.print("node:("+addrs+")");
                sb.append(addrs);
            }
            else if(node.isType(Node.COLON )){ // e.g.(A1:A10)
                    Point sp=null,ep=null;//start point, end point of the range
                    Node start=node.getNextRange() ;//start(upLeft) of the range
                    Node end=start.getNextRange() ;//end (downRight) of the range
                    StringBuffer address=new StringBuffer();
                    if (start.getType() ==Node.REL_ADDR ){
                        Cell cell=start.getReference() ;
                        sp=cell.getViewAddress();
                        String spAddr=Cell.getRelCellAddress(sp);
                        address.append(spAddr);

                    }
                    else if(start.getType() ==Node.ABS_ADDR ){
                        sp=start.getAddress();
                        String spAddr=Cell.getAbsCellAddress(sp);
                        address.append(spAddr);

                    }
                    else{
                        assert false: "Unable to handle the expression";
                        //throw edu.psu.geovista.geoviz.spreadsheet.exception: unable to handle the expression
                    }

                    if (end.getType() ==Node.REL_ADDR ){
                        Cell cell=end.getReference() ;
                        ep=cell.getViewAddress();
                        address.append(":");
                        String spAddr=Cell.getRelCellAddress(ep);
                        address.append(spAddr);
                    }
                    else if(end.getType() ==Node.ABS_ADDR ){
                        ep=end.getAddress() ;
                        address.append(":");
                        String spAddr=Cell.getAbsCellAddress(ep);
                        address.append(spAddr);
                    }
                    else{

                        assert false: "Unable to handle the expression";
                        //throw edu.psu.geovista.geoviz.spreadsheet.exception: unable to handle the expression
                    }
                    sb.append(address.toString() );

            }
            else{
                String s=node.getData() ;
                sb.append(s);
                System.out.print(s+" ");
            }
        } //while
        this.formulaString =sb.toString() ;
     }



    /**
     * Check for bad edu.psu.geovista.geoviz.spreadsheet.formula.
     *
     * @return boolean true if postfix
     */
    public boolean isBad() {
	// postfix was set to null when there was any error
        // in processing the edu.psu.geovista.geoviz.spreadsheet.formula string
        return postfix == null;
    }

    public void setBad() {
        postfix=null;
    }

    /**
     * Check whether needs a recalc
     *
     * @return boolean true if needs recalculation
     */
    public boolean needsRecalc() {
        return needsRecalc;
    }

   /**
     * Mark it as needsRecalc
     *
     * @parem boolean true if needs recalculation
     */
    public void setNeedsRecalc(boolean needs) {
	needsRecalc = needs;
    }




    /**
     * Tokenize the edu.psu.geovista.geoviz.spreadsheet.formula string into a list of Nodes.
     *
     * @param input the input string to tokenize
     * @edu.psu.geovista.geoviz.spreadsheet.exception edu.psu.geovista.geoviz.spreadsheet.exception.ParserException
     *
     * @see edu.psu.geovista.geoviz.spreadsheet.formula.Node
     */
    private LinkedList tokenize(String input) throws ParserException {
            LinkedList tokens = new LinkedList();
            //Stack stack = new Stack();        Jin: no use
            final Node zero = new Node();
            zero.setType(Node.NUMBER);
            zero.setNumber(0);
            //	input.toUpperCase();

            int cur = 0;
            int lastType = Node.DEFAULT;
            Node lastToken = null;
            //	boolean hasRange = false; // has a pending address range
            int nParen = 0; // balance of parens

            while (cur < input.length()) {
                Node node = new Node();

             try {
                char c = input.charAt(cur++);
                node.setData(String.valueOf(c));
                //1.
                if (Character.isLetter(c)) {
                    // 1. edu.psu.geovista.geoviz.spreadsheet.functions.Function or Relative Address
                    node.setType(Node.FUNCTION);
                    node.setParams(new LinkedList());

                    // get all preceding letters
                    while (cur < input.length() &&
                       Character.isLetter(input.charAt(cur)))
                       node.appendData(input.charAt(cur++));

                    if (Character.isDigit(input.charAt(cur))) {
                            //2. !!! Relative address
                            Cell owner=this.getOwner() ;
                            SSTable table=owner.getDataModel().getTable() ;
                            node.setType(Node.REL_ADDR);
                            String scol=node.getData(); //Column name in view. e.g.: "B"
                            //int refcol=Cell.translateColumn(scol) ;//Model col
                            int refcol=table.translateColumn(scol) ;//Model col
                            //node.setCol(refcol);
                            node.setData("");
                            while (cur < input.length() &&
                                   Character.isDigit(input.charAt(cur)))
                                node.appendData(input.charAt(cur++));
                            // relative row
                            int refrow=translateRow(node.getData())  ;//absolute row number
                            //node.setRow(refrow- row); //relative postion regard to current row(row, col)
                            //node.setRow(refrow);
                            node.setData(null);
                            //Jin->
                            Cell cell=(Cell)this.getCell(refrow,refcol);
                            System.out.println("set Refrence ("+refrow+","+refcol+") ="+cell.getValue() );
                            node.setReference(cell);
                            //Jin<-
                    }
                }else if (Character.isDigit(c) || c == '.') { //Number or .
/*||
                     (lastType == edu.psu.geovista.geoviz.spreadsheet.formula.Node.DEFAULT ||
                      lastType == edu.psu.geovista.geoviz.spreadsheet.formula.Node.LPAREN || lastType == edu.psu.geovista.geoviz.spreadsheet.formula.Node.COMMA) &&
                      (c == '+' || c == '-')) */
                    // Numbers
                    while (cur < input.length() &&
                       (Character.isDigit(input.charAt(cur)) ||
                        input.charAt(cur) == '.'))
                    // OK, we don't check for input like "3.56.4"
                    // this will be checked below by parseNumber
                    node.appendData(input.charAt(cur++));

                    try {
                    try {
                        node.setNumber(Integer.parseInt(node.getData()));
                    }
                    catch (NumberFormatException e) {
                        node.setNumber(Float.parseFloat(node.getData()));
                    }
                    node.setType(Node.NUMBER);
                    }catch (NumberFormatException e) {
                    // invalid number format
                    throwError("#NUM?");
                    }
                }else if (c == '(') {
                    nParen++;
                    node.setType(Node.LPAREN);
                }else if (c == ')') {
                    nParen--;
                    node.setType(Node.RPAREN);
                }else if (c == ',') {
                    node.setType(Node.COMMA);
                }else if (c == ':') {

                    node.setPending(true);
                    node.setType(Node.COLON );

                    Node prev = null;

                    try {
                        prev = (Node)tokens.removeLast();
                    }
                    catch (Exception e) {
                        throwError("#ADDR?");
                    };

                    if (prev.isType(Node.REL_ADDR) ||
                        prev.isType(Node.ABS_ADDR)) {
                        node.setNextRange(prev);
                    }
                    else
                        // invalid address format
                        throwError("#ADDR?");

                }else if (c == '+' || c == '-' || c == '*' || c == '/' ||
                     c == '^' || c == '%') {
                    node.setType(Node.OPERATOR);
                }else if (c == '$') {
                    // !!! Absolute Address starts with $
                    node.setType(Node.ABS_ADDR);
                    node.setData("");
                    // a letter must follow the $
                    if (! Character.isLetter(input.charAt(cur))) {
                    // invalid address format
                    throwError("#ADDR?");
                    }
                    // look for column
                    while (Character.isLetter(input.charAt(cur)))
                    node.appendData(input.charAt(cur++));

                    // absolute address has to be the form of
                    // ${letters}${numbers}
                    if (input.charAt(cur++) != '$' ||
                    ! Character.isDigit(input.charAt(cur))) {
                    // invalid address format
                    throwError("#ADDR?");
                    }
                    String scol=node.getData();//view's column in String. e.g.: "A"
                    int refcol=SSTable.translateVVColumn(scol);//view column
                    //node.setCol(refcol);
                    node.setData("");
                    while (cur < input.length() &&
                       Character.isDigit(input.charAt(cur)))
                    node.appendData(input.charAt(cur++));
                    String data=node.getData(); //Table's row index in String
                    int refrow=translateRow(data) ; //Table's row
                    //node.setRow(refrow);
                    node.setData(null);
                    node.setAddress(refrow,refcol);
                } //absolute address
                else if (c == ' ')
                    continue;
                else
                    // invalid char
                    throwError("#NAME?");

                //this.showTokens(tokens);

                 //2.
                // after a ADDR or NUMBER token the following char
                // should not be a letter or digit
                if (cur < input.length() && (node.isType(Node.REL_ADDR) ||
                                 node.isType(Node.ABS_ADDR) ||
                                 node.isType(Node.NUMBER)) &&
                    Character.isLetterOrDigit(input.charAt(cur))) {
                    throwError
                    // invalid char
                    ("#NAME?");
                }

                // process the second address of a cell range
                if (lastToken != null &&
                    lastToken.isType(Node.COLON) &&
                    lastToken.isPending()) { //Range
                                if (node.isType(Node.REL_ADDR) || node.isType(Node.ABS_ADDR)) {

                                    Node range = (Node) tokens.removeLast();

                                    try {
                                        ((Node) range.getNextRange()).setNextRange(node);
                                        range.setPending(false);
                                    } catch (NullPointerException e) {
                                        // invalid address format
                                        throwError("#ADDR?");
                                    }

                                    node = range;
                                    //			util.Debug.println("edu.psu.geovista.geoviz.spreadsheet.formula.Node: "+node);
                                } else
                                    throwError("#ADDR?");
                }

                 //		edu.psu.geovista.geoviz.spreadsheet.util.Debug.println("Add: "+node);

                if (node.isType(Node.OPERATOR) &&
                    (node.getData().equals("+") ||
                     node.getData().equals("-")) &&
                    (lastToken == null || lastToken.isType(Node.LPAREN) ||
                     lastToken.isType(Node.COMMA))) {
                                    tokens.add(zero);
                }
                System.out.println("node:"+node.getData());
                tokens.add(node);
                lastType = node.getType();
                Debug.println("lastType:"+lastType);
                lastToken = node;
                //this.showTokens(tokens);

      }catch (IndexOutOfBoundsException e) {
                // error
                throwError("#NAME?");
      }catch (ParserException e) {
                throwError(e);
      }catch (Exception e) {
                    e.printStackTrace() ;
                 ///edu.psu.geovista.geoviz.spreadsheet.util.Debug.println(e.toString());
      } //try


      }//while loop at beginning

      if (nParen != 0) // imbalanced parenthesis
             throwError("#PAREN?");

      /*Debug.showLinkedList(tokens,"tokenize() show token");
      Node node=(Node)tokens.get(2);
      Debug.showNode(node,"node 2");
      Node r1=node.getNextRange() ;
      Debug.showNode(r1,"Range 1");
      Debug.showNode(r1.getNextRange(),"Range2");
      /*LinkedList param=node.getParams() ;
      Iterator iter=param.iterator() ;
      while(iter.hasNext() ){
          System.out.println(" "+iter.next() );
      }     */
      //this.showTokens(tokens);  */
      return tokens;
    }

    /**
     * Convert function parameters.  From a linear sequence of nodes,
     * output a tree-like structure, with all the functions having a
     * linked list of parameters, and each parameter having a linked
     * list of nodes (that is, each parameter can be a edu.psu.geovista.geoviz.spreadsheet.formula).
     *
     * The basic rules are:
     * <ol>
     * <li>Pass values to the output (a linked list used as a stack) except
     * the following.</li>
     * <li>If a function name is encountered, it's set to "pending" (meaning
     * it's expecting an enclosing parenthesis) and passed to the output, and
     * its following '(' is discarded.</li>
     * <li>If a left parenthesis is encountered, it's set to "pending"
     * and passed to the output.</li>
     * <li>If a comma is encountered, pop up all the previous nodes to a list
     * until an unpending function node is found.  Then set the list having
     * all the popped nodes as the function's last parameter.  The function
     * node is pushed back.</li>
     * <li>For a ')', pop all the previous nodes to a list until an unpending
     * left parenthesis or an unpending function is found.  For the former,
     * the left parenthesis is set to "unpending", and push back all the
     * popped nodes (including the right parenthesis).  For the latter,
     * it's the same as the comma case, except that the function node is
     * set to "unpending".</li>
     * </ol>
     *
     */
    private LinkedList convertParams(final LinkedList tokens)
	throws ParserException {

	if (tokens == null) {
	    throw error;
	}

	LinkedList stack = new LinkedList();

	Iterator it = tokens.iterator();

	try {
	    while (it.hasNext()) {
		Node node = (Node)it.next();

		if (node.isType(Node.FUNCTION)) {
		    node.setPending(true);
		    stack.add(node);
		    node = (Node)it.next();
		    // should be LParen
		    if (!node.isType(Node.LPAREN)) // ( expected
			throwError("#NO(?");
		}
		else if (node.isType(Node.LPAREN)) {
		    node.setPending(true);
		    stack.add(node);
		}
		else if (node.isType(Node.COMMA)) {
		    Node exp = new Node();
		    LinkedList list = new LinkedList();
		    Node param = (Node)stack.removeLast();//pop();
		    // pop out until the unpending FUNCTION
		    while (!param.isType(Node.FUNCTION) ||
			   !param.isPending()) {
			list.addFirst(param);
			param = (Node)stack.removeLast();//pop();
		    }

		    exp.setType(Node.EXP);
		    exp.setExp(list);

		    param.addParam(exp);

		    // still pending
		    //		    stack.push(param);
		    stack.add(param);
		}
		else if (node.isType(Node.RPAREN)) {
		    // we don't know whether this is for a function.
		    Node exp = new Node();
		    LinkedList list = new LinkedList();
		    Node param = (Node)stack.removeLast(); //stack.pop();

		    // process the last parameter
		    while (!param.isPending() ||
			   !param.isType(Node.FUNCTION) &&
			   !param.isType(Node.LPAREN)) {
			list.addFirst(param);
			param = (Node)stack.removeLast();//pop();
		    }

		    // set to unpending
		    if (param.isType(Node.LPAREN)) {
                // this is a normal left paren
                param.setPending(false);
                // push back
                stack.add(param);
                stack.addAll(list);
                stack.add(node);
		    }
		    else {
                // this is a function left paren
                //			edu.psu.geovista.geoviz.spreadsheet.util.Debug.println("exp is "+list);
                // set the expression of that parameter
                exp.setType(Node.EXP);
                exp.setExp(list);
                // add a parameter for the function
                param.addParam(exp);
                param.setPending(false);
                stack.add(param);
		    }
		}
		else
		    stack.add(node); //push(node);

	    }

	}
	catch (ParserException e) {
	    throw e;
	}
	catch (Exception e) {
	    Debug.println(e);
	    // general param error
	    throwError("#PARAM?");
	}

	return stack;
    }

    /**
     * This converts tokens to postfix format using stack.
     * <p>
     * The basic rules are:
     * <ol>
     * <li>Pass values to the output (a linked list)</li>
     * <li>Push '(' to the stack</li>
     * <li>For an operator, pop all the previous operators that have a lower
     *     priority to the output and push this one to the stack</li>
     * <li>For ')', pop all the previous operators until a (</li>
     * <li>If we reach the end, pop up everything</li>
     * </ol>
     *
     * @param tokens a linked list to convert
     * @edu.psu.geovista.geoviz.spreadsheet.exception edu.psu.geovista.geoviz.spreadsheet.exception.ParserException
     *
     * @see edu.psu.geovista.geoviz.spreadsheet.formula.Node
     * @see #tokenize
     * @see #convertParam
     */
    private LinkedList toPostfix(LinkedList tokens) throws ParserException {
	if (tokens == null) {
	    throw error;
	}

	// stack is used for the conversion
	Stack stack = new Stack();
	LinkedList postfix = new LinkedList();
	Iterator it = tokens.iterator();
	while (it.hasNext()) {
	    Node node = (Node)it.next();
	    switch (node.getType()) {

	    case Node.NUMBER:
	    case Node.REL_ADDR:
	    case Node.ABS_ADDR:
	    case Node.COLON:
		// just add normal values to the list
		postfix.add(node);
		break;

	    case Node.LPAREN:
		// push to stack; pop out when a RPAREN is encountered
		stack.push(node);
		break;
	    case Node.OPERATOR:
		// get the precedence priority of the operator
		int priority = getPriority(node);

		// pop up operators with the same or higher priority from
		// the stack
		while (! stack.empty() &&
		       ! ((Node)stack.peek()).isType(Node.LPAREN) &&
		       getPriority((Node)stack.peek()) >= priority) {
		    postfix.add((Node)stack.pop());
		}
		stack.push(node);
		break;
	    case Node.RPAREN:
		try {
		    Node op = (Node)stack.pop();
		    // pop out until the last LPAREN
		    while (! op.isType(Node.LPAREN)) {
			postfix.add(op);
			op = (Node)stack.pop();
		    }
		}
		catch (EmptyStackException e) {
		    // should not happen - imbalance in parenthesis
		    throwError("#PAREN?");
		}
		break;
	    case Node.FUNCTION:

		// get the param list
		LinkedList params = node.getParams();

		Iterator paramIter = params.iterator();

		while (paramIter.hasNext()) {
		    Node exp = (Node)paramIter.next();
		    exp.setExp(toPostfix(exp.getExp()));
		}

		postfix.add(node);

		break;

	    default:
		// unknown error - should not happen
		throwError("#ERROR?");
	    }
	}

	// pop up the rest nodes
	while (!stack.empty())
	    postfix.add((Node)stack.pop());

	return postfix;
    }


    /**
     *  get cell from TableModel, if null, create a new cell
     *  and store it in the TableModel
     */
    private Cell getCell(int row, int col){
              //SSTableModel tbm=SSTableModel.getInstance();
              SSTableModel tbm=this.getOwner().getDataModel() ;
              return(Cell) tbm.getValueAt(row, col) ;
    }

    /**
     * From the edu.psu.geovista.geoviz.spreadsheet.formula.Node list; Creates the dependency set.
     *
     * @return a HashSet of edu.psu.geovista.geoviz.spreadsheet.formula.CellPoint that the current cell references
     */

    public HashSet getOwners() {
        if (this.owners ==null){
            this.owners =new HashSet();
        }
        return owners;
    }

    public Cell getOwner() {
        return owner;
    }

    public boolean addOwner(Cell cell) {
        if (this.owners ==null){
            this.owners =new HashSet();
        }
        return this.owners.add(cell);
    }




    /**
     * This gets the priority of an operator.
     *
     * @param op the operator character
     * @return  1='+' '-', 2='*' '/', 3='^'
     */
    private static int getPriority(char op) {
        switch (op) {

        case '+':
        case '-':
            return 1;
        case '*':
        case '/':
        case '%':
            return 2;
        case '^':
            return 3;
        default:
            return 0;
        }
    }

    /**
     * This returns the highest-priority node.
     */
    private static int getPriority(Node node) {
        return getPriority(node.getData().charAt(0));
    }

    /**
     * This returns the string value of the edu.psu.geovista.geoviz.spreadsheet.formula.
     *
     * @return the string value
     */
    public String toString() {
        this.evalueExpression(this.nodes );
        return formulaString;
    }

    /**
     * This takes an operator and two operands and returns the result.
     *
     * @param op the operator
     * @param op1 operand 1
     * @param op2 operand 2
     * @return the float value of operand 1 operator operand 2
     */
    private static Number calc(char op, Number op1, Number op2) {
	float n1 = op1.floatValue();
	float n2 = op2.floatValue();
	float result;
	switch (op) {
	    case '+': result = n1+n2; break;
	    case '-': result = n1-n2; break;
	    case '*': result = n1*n2; break;
	    case '/': result = n1/n2; break;
	    case '^': result = (float)Math.pow(n1, n2); break;
	    case '%': result = (float)((int)n1%(int)n2); break;
	    default: result = 0; break;
	}

	return new Float(result);
    }

    /**
     * This evaluates the function.
     *
     * @param table the TableModel object
     * @param node the head node of the function
     * @return the value as a Float object
     * @edu.psu.geovista.geoviz.spreadsheet.exception edu.psu.geovista.geoviz.spreadsheet.exception.ParserException
     */
    private Number evalFunction(Node node)
        throws ParserException,NoReferenceException {

            String funcName = node.getData();

            // get function handler from the funcTable

            //Function func = getFuncHandler(funcName);
            Function func = this.getFunctionManager().getFuncHandler(funcName);

        //func.setOwners(this.getOwners() );

            if (func == null) {
                // not registered function
                throw new ParserException("#FUNC?");

            }else{
              func.setOwner(this);
              return func.evaluate(node);
            }

    }
    public FunctionManager getFunctionManager() {
         SSTable table=owner.getDataModel().getTable() ;
         return table.getFunManager();
    }

    public Number evaluate() throws ParserException,NoReferenceException {
        return evaluate(this.postfix );
    }

   /**
    * It evaluates the postfix expression by a stack.
    *
    * @param table the TableModel object
    * @param postfix the edu.psu.geovista.geoviz.spreadsheet.formula in postfix form
    * @param row the row of the cell to be evaluated
    * @param col the column of the cell to be evaluated
    * @return the result as a Float object
    * @edu.psu.geovista.geoviz.spreadsheet.exception edu.psu.geovista.geoviz.spreadsheet.exception.ParserException
    */
    public  Number evaluate(LinkedList postfix) throws ParserException,NoReferenceException {
            if (this.isBad() ){
                throw new ParserException("#LOOP?");
            }

            try {
                Stack stack = new Stack();

                 Iterator it = postfix.iterator();
                int numOfNode=0;
                Object o;
                while (it.hasNext()) {
                numOfNode++;
                Node node = (Node)it.next();
                //Number result;
                Number result;
                Cell cell=null;
                switch (node.getType()) {
                case Node.OPERATOR:
                    // pop the 2 operands from stack top and save the result
                    // back to stack
                    Number n2 = (Number)stack.pop();
                    Number n1 = (Number)stack.pop();
                    result = calc(node.getData().charAt(0), n1, n2);
                    break;
                case Node.FUNCTION:
                    // evaluate the function
                    result = evalFunction(node);
                    Debug.println("result :"+result);
                    break;
                case Node.NUMBER:
                    // directly return the number
                    result = new Float(node.getNumber());
                    break;
                case Node.ABS_ADDR:
                    // get the numeric value of that cell
                    //result = //getNumericValueAt(table, node.getRow(),
                        //	       node.getCol());
                    //table.getNumericValueAt(node.getRow(), node.getCol());
                        Point address=node.getAddress() ;
                        //JTable tb=SpreadSheetBean.getTableInstance() ;
                        JTable tb=owner.getDataModel().getTable() ;
                        int x=(int)address.getX() ;
                        int y=(int)address.getY() ;
                        cell=(Cell)tb.getValueAt(x,y);
                        this.checkReference(cell);

                        cell.evaluate();

                        o=cell.getValue() ;
                        if (o==null){ //If refrenced cell is null, show 0
                            result=new Float(0.0f);
                        }
                        else if (o instanceof String){
                            //reference is marked as deleted
                            if( o.equals("#REF!")){
                                throw new NoReferenceException("#REF!");
                            }
                            else{//the refrenced cell is a String
                                if (numOfNode==1&&!it.hasNext() ){

                                    //If the edu.psu.geovista.geoviz.spreadsheet.formula contain ONLY the cell, just throw the String to the referencing cell
                                    // which is setValue() as the string
                                    throw new ParserException((String)o);
                                }
                                else{
                                    ////If the edu.psu.geovista.geoviz.spreadsheet.formula contain more than the cell, throw  "#VALUE!"
                                    throw new ParserException("#VALUE!");
                                }
                            }
                        }
                        else{
                            result= (Number)o;
                        }
                        /*
                        o=cell.getValue() ;
                        if (o==null){//If refrenced cell is null, show 0
                            result=new Float(0.0f);
                        }
                        result=(Number)cell.getValue() ;

                        System.out.println(" Not implemented yet!");   */

                    break;
                case Node.REL_ADDR:
                    // get the numeric value of that cell
                    /*result = //getNumericValueAt(table, node.getRow()+row,
                        //	       node.getCol()+col);
                    table.getNumericValueAt(node.getRow()+row,
                                node.getCol()+col);    */
                        cell=node.getReference() ;
                        this.checkReference(cell);
                        cell.evaluate();

                        o=cell.getValue() ;
                        if (o==null){ //If refrenced cell is null, show 0
                            result=new Float(0.0f);
                        }
                        else if (o instanceof String){
                            //reference is marked as deleted
                            if( o.equals("#REF!")){
                                throw new NoReferenceException("#REF!");
                            }
                            else{//the refrenced cell is a String
                                if (numOfNode==1&&!it.hasNext() ){

                                    //If the edu.psu.geovista.geoviz.spreadsheet.formula contain ONLY the cell, just throw the String to the referencing cell
                                    // which is setValue() as the string
                                    throw new ParserException((String)o);
                                }
                                else{
                                    ////If the edu.psu.geovista.geoviz.spreadsheet.formula contain more than the cell, throw  "#VALUE!"
                                    throw new ParserException("#VALUE!");
                                }
                            }
                        }
                        else{
                            result= (Number)o;
                        }
                        //result=null;
                        System.out.println(" Not implemented yet!");
                    break;
                default:
                    // evaluation error
                    throw new ParserException("#EVAL?");
                }

                // push to the stack
                stack.push(result);
                }

                Number result = (Number)stack.pop();

                return result;
            }catch (EmptyStackException e) {
                // imbalance between operands and operators
                throw new ParserException("#OP?");
                // ("Wrong format of edu.psu.geovista.geoviz.spreadsheet.formula: too many operators");
            }catch (ParserException e) {
                throw e;
            } catch (NoReferenceException e) {
                throw e;

            }catch (Exception e) {
                e.printStackTrace() ;
                //edu.psu.geovista.geoviz.spreadsheet.util.Debug.println(e);
            }

            return new Integer(0);
    }
    /**
     *
     */
    private void checkReference(Cell cell) throws ParserException{
                        if(this.owners.contains(cell)){
                            this.setBad() ;//stop any further evaluation on the edu.psu.geovista.geoviz.spreadsheet.formula
                            //current cell reference to one of his owner
                            //JFrame mf=(JFrame)SwingUtilities.getAncestorOfClass(JFrame.class,SpreadSheetBean.getTableInstance() );
                            JFrame mf=(JFrame)SwingUtilities.getAncestorOfClass(JFrame.class,owner.getDataModel().getTable() );

                            JOptionPane.showConfirmDialog(mf,
                                    "Loop reference error","Error",JOptionPane.OK_OPTION);
                            throw new ParserException("#LOOP?");
                        }
                        else{
                            HashSet owners=this.getOwners() ;
                            Iterator iter = owners.iterator();
                            while(iter.hasNext()){
                              Cell c = (Cell)iter.next();
                              if (cell.isFormula() ){
                                Debug.println("cell:"+cell.getViewAddress() );
                                cell.addOwner(c);
                              }
                              //false NOT mean loop: e.g.: A reference to B,C and B,C feference to D
                               // Thus A is owner of D for twice
                            }

                        }
    }

    public static Number processCellValue(Object o)
            throws ParserException,NoReferenceException{
        Number result=null;
        if (o==null){ //If refrenced cell is null, show 0
                            result=new Float(0.0f);
        }
        else if (o instanceof String){
                         //reference is marked as deleted
                            if( o.equals("#REF!")){
                                throw new NoReferenceException("#REF!");
                            }
                            else{//the refrenced cell is a String
                                    throw new ParserException("#VALUE!");
                            }
       }
       else{
                            result= (Number)o;
        }
        return result;
    }
//commented out by frank hardisty... can this be used somewhere? if not, delete
//    private Number evaluateCell(Cell cell) throws ParserException {
//
//        if (cell != null) {
//            int type = cell.getValueType();
//            if (cell.isFormula() ) {
//                Object value = cell.getValue();
//                Formula form = cell.getFormula();
//                // if need recalc
//                if (form.needsRecalc()) {
//                    try {
//                        value = form.evaluate();
//                        cell.setValue(value);
//                    }
//                    catch (ParserException e) {
//                        cell.setValue(e);
//                        value = e;
//                    }
//                    catch(NoReferenceException e){
//                        e.printStackTrace() ;//only for debug
//                        cell.setValue("#REF");
//                    }
//                }
//
//                if (value instanceof ParserException)
//                    throw (ParserException)value;
//                else
//                    return (Number)cell.getValue();
//            }
//            else if (type == Cell.NUMBER)
//                return (Number)cell.getValue();
//            else
//                return new Float(0);
//        }
//        else
//        // a string or null
//        //	return new Float(0);
//            throw new ParserException("#REFS?");
//
//        }


    // The following are just simple edu.psu.geovista.geoviz.spreadsheet.functions

    /**
     * This translates the string form of row into row number ('12' -> 12),
     * Translate View's row to Model's row.
     *
     * @param row the string representation of the row
     * @return the int representation of the row
     */
    final private static int translateRow(String row) {
        int x=Integer.parseInt(row);
        return SSTable.transRowViewToTable(x);
    }

    /**
     * This translates the int form of row into row string (12 -> '12').
     * Translate Table's row to View's row.
     * @param row the int representation of the row
     * @return the string representation of the row
     */
    final private static String translateRow(int row) {
            int x= SSTable.transRowTableToView(row);
        //int x= SSTable.transRowTableToView(row);
            return Integer.toString(x);
    }



    /**
     * Label the bad cells and throw edu.psu.geovista.geoviz.spreadsheet.exception.ParserException.
     * error is saved so next time it won't re-evaluate again:
     * it directly throws the same edu.psu.geovista.geoviz.spreadsheet.exception.
     *
     * @param s the thing that's bad
     * @edu.psu.geovista.geoviz.spreadsheet.exception edu.psu.geovista.geoviz.spreadsheet.exception.ParserException
     */
    private void throwError(Object s) throws ParserException {
	// test code
        //	System.err.println("Marking edu.psu.geovista.geoviz.spreadsheet.formula "+formulaString+" as bad");
        postfix = null;
        if (error instanceof ParserException)
            throw (ParserException) s;
        else {
            error = new ParserException(s);
            throw error;
        }
    }




}
