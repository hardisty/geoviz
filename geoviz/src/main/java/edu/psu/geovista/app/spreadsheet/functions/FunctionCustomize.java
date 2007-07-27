package edu.psu.geovista.app.spreadsheet.functions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import edu.psu.geovista.app.spreadsheet.exception.NoReferenceException;
import edu.psu.geovista.app.spreadsheet.exception.ParserException;
import edu.psu.geovista.app.spreadsheet.formula.Node;
import edu.psu.geovista.app.spreadsheet.util.Debug;

/*
 * Description:
 * Date: Mar 28, 2003
 * Time: 9:33:26 AM
 * @author Jin Chen
 */

public class FunctionCustomize extends Function {
    public Number evaluate( Node node) throws ParserException,NoReferenceException {
            ArrayList params=new ArrayList(); //the parameters to pass to customized function
            LinkedList exps = node.getParams();//
            Debug.showLinkedList(exps,"Show params");
            //Node exp=(Node)exps.getFirst() ;// expresion Node(type=Node.EXP). e.g. a1:a2
            Iterator iter = exps.iterator();
            while(iter.hasNext()){
                Node exp= (Node)iter.next();
                Debug.showNode(exp, "FunctionCustomize show exp");
                Number p=this.getOwner().evaluate(exp.getExp() ) ;
                params.add(p);

                /*float p=Formula.evaluateFun(exp.getExp() ).floatValue();
                Debug.println("param:"+p);  */
                /**
                LinkedList param=exp.getParams() ;
                Debug.println("");
                Iterator  it=param.iterator();
                while(it.hasNext()){
                    Node p= (Node)it.next();  //type1 Node
                    Cell cell=p.getReference() ;//
                    if(cell.isFormula()){
                        cell.evaluate() ;
                    }
                    if (! (cell.getValue() instanceof Number)){
                        //If not a Number, the error msg is in cell.getValue()
                        throw new ParserException(cell.getValue() );
                    }
                    Debug.println("show param:"+cell.getValue() );
                    params.add(cell.getValue());
                }   */
            }

            Object o=this.evaluate(params.toArray()) ;

            if (!(o instanceof Number)){
                throw new ParserException("#NUM?");
            }

            return (Number)o;
    }

    private Object evaluate(Object[] param) {
         float sum=0;
         for (int i=0;i<param.length ;i++){
            sum=sum+((Number)param[i]).floatValue() ;
         }
        return new Float(sum);
    }

    public String getUsage() {
        return "MYFUN(value1,value2,...)";
    }

    public String getDescription() {
        return "Returns the average (arithmetric mean) of its arguments.";
    }
}

