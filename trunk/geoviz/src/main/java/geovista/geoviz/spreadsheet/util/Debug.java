package geovista.geoviz.spreadsheet.util;

/*
 * This is a class used for print out debug information and can be
 * easily turned on/off.
 */
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import geovista.geoviz.spreadsheet.formula.Node;

public class Debug {

    private static  boolean debug = false;

    public static void setDebug(boolean flag) {
	debug = flag;
    }

    public static boolean isDebug() {
	return debug;
    }

    public static void println(Object s) {
	if (debug)
	    System.out.println(s.toString());
    }
    public static void showVector(Vector v, String msg){
        if (!isDebug()) return;
        System.out.println(" ------- Do "+msg+" --->");
        System.out.println("Vector: "+v);
        Iterator iter=v.iterator() ;
        while(iter.hasNext() ){
            Object o=iter.next() ;
            if (o!=null)
                System.out.println(o.toString());
            else
                System.out.println("null");

        }
        System.out.println(" ------- Do "+msg+"<-- \n");
    }
    public static void showLinkedList(LinkedList ll, String msg){
        if (!isDebug()) return;
        System.out.println(" ------- Do "+msg+" --->");
        Iterator iter=ll.iterator() ;
        while(iter.hasNext() ){
            Object o=iter.next() ;
            System.out.println(o.toString());

        }
        System.out.println(" ------- Do "+msg+"<-- \n");
    }


    public static void showNode(Node node, String msg){
        if (!isDebug()) return;
        System.out.println(" ------- Do "+msg+" --->");
        System.out.println("type:"+node.getType() );
        System.out.println("data:"+node.getData() );
        System.out.println(" "+ node.getNumber() );
        if (node.isType(Node.FUNCTION )){
            showLinkedList(node.getParams() , "show node parameter");
        }

        System.out.println(" ------- Do "+msg+"<-- \n");
    }
}
