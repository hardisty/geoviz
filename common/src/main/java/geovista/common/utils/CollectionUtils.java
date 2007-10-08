/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Reserved.
 *
 * @Original Author: jin Chen
 * @date: May 24, 2005$
 * @version: 1.0
 */
package geovista.common.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class CollectionUtils {

    public static final boolean DEBUG = false;

   /**
    * Assume the Hastables contain only
    * @param frm
    * @param to
    */
    public static void copy(Hashtable frm, Hashtable to)  {
    }
    /*******************************************************************************************************
     *                array
     *******************************************************************************************************/
    /**
     * return
      * @param array     contain only  unique values
     * @return
     */
    public static String[] uniques(String[] array){
        HashSet set=new HashSet();

        for (int i=0;i<array.length ;i++){
            String s = array[i];
            set.add(s);
        }
        String[] uniques = convertToStringArray(set);
        return  uniques;


    }
     public static int[] convertToIntArray(Set set){
        int[] intA;
        if (set.size() <=0){
            intA=new int[0];
        }
        else{
           intA=new int[set.size() ];
            Object[] tmp=set.toArray() ;
            for (int i=0;i<intA.length ;i++){
                intA[i]=((Integer)tmp[i]).intValue() ;
            }
        }
          return intA;
    }
     public static int[] convertToIntArray(Collection data){
        int[] intA;
        if (data.size() <=0){
            intA=new int[0];
        }
        else{
           intA=new int[data.size() ];
            Object[] tmp=data.toArray() ;
            for (int i=0;i<intA.length ;i++){
                intA[i]=((Integer)tmp[i]).intValue() ;
            }
        }
          return intA;
    }
    public static HashSet convertArrayToSet(int[] array){
        HashSet set=new HashSet();
        for (int i=0;i<array.length ;i++){
            Integer e=new Integer(array[i]);
            set.add(e);
        }
        return set;
    }
    public static void convertArrayToSet(HashSet set,int[] array){
        //HashSet set=new HashSet();
        for (int i=0;i<array.length ;i++){
            Integer e=new Integer(array[i]);
            set.add(e);
        }
        //return set;
    }
    public static List convertArrayToList(Color[] array){
         ArrayList  list=new ArrayList();
        for (int i=0;i<array.length ;i++){
            list.add(array[i]);
        }
         return list;
    }
    public static List convertArrayToList(int[] array){
        ArrayList  list=new ArrayList();
        for (int i=0;i<array.length ;i++){
            Integer e=new Integer(array[i]);
            list.add(e);
        }
        return list;
    }
    public static List convertArrayToList(String [] array){
        ArrayList  list=new ArrayList();
        for (int i=0;i<array.length ;i++){
            //String  s=array[i];
            list.add(array[i]);
        }
        return list;
    }
    public static Object[] convertToArray(Collection collection){
        if(collection ==null){
            throw new NullPointerException("Can't convert null set");
        }
        Object[] ss=new Object[collection.size() ];
        int i=0;
        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            Object s =  iterator.next();
            ss[i++]=s;
        }
        return ss;
    }
    public static String[] convertToStringArray(Set set){
        if(set==null){
            throw new NullPointerException("Can't convert null set");
        }
        String[] ss=new String[set.size() ];
        int i=0;
        for (Iterator iterator = set.iterator(); iterator.hasNext();) {
            String s = (String) iterator.next();
            ss[i++]=s;
        }
        return ss;
    }
    public static String[] convertToStringArray(List list){
        if(list==null){
            throw new NullPointerException("Can't convert null list");
        }
        String[] ss=new String[list.size() ];
        int i=0;
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            Object o = iterator.next();
            String s = (o==null)?"":o.toString() ; //if null, save as empty space
            ss[i++]=s;
        }
        return ss;
    }
    public static String[] convertToStringArray(Object[] obj){
        String[] st=new String[obj.length ];
        for (int i=0;i<obj.length ;i++){
            st[i]=obj[i].toString() ;
        }
        return st;

    }

     /*******************************************************************************************************
     *                Vector
     *******************************************************************************************************/
    public static Vector getEmptyVector(int size){
         Vector v=new Vector(size);
         //Collections.fill(v,null);not work
         for (int i=0;i<size;i++){
            v.add(null);
         }
         return v;
     }
    /**
     * convert a column based a array ( each element is a array represent a column) to a row-based vector ( each element is a vector represent a data row
     * @param data  element of the data can be primitive type array
     * @return
     */
     public static Vector toRowVector(Object[][] array){
        int colcount = array.length;
        int rowcount = array[0].length;

        Vector data=new Vector();
        for (int i=0;i<rowcount ;i++){

            Vector record=new Vector(colcount);
            for (int j=0;j<colcount ;j++){
                record.add(array[j][i]);
            }
             data.add(record);

        }
        return data;
     }

    /**
     * convert a column based a vector ( each element is a array represent a column)  to a row-based vector ( each element is a vector represent a data row
     * @param colvectors
     * @return
     */
     public static Vector toRowVector(Vector<Vector> colvectors){
        int colcount = colvectors.size();
        Vector v0 = colvectors.get(0);
        int rowcount = v0.size();
        Vector data=new Vector();
        for (int i=0;i<rowcount ;i++){

            Vector row=new Vector(colcount);
            for (int j=0;j<colcount ;j++){ //for each row, add column value
                Vector colv = colvectors.get(j);
                row.add(colv.get(i) );
            }
            data.add(row);

        }
        return data;
    }
    /**
     * @param c1
     * @param c2
     * @return     true if two collection has same size and  each element of the two collection equal ( not have to be exact same object)
     */
     public static boolean equals(Collection c1, Collection c2){
         if(c1.size() !=c2.size() )return false;
         Object[] a1 = c1.toArray();
         Object[] a2 = c2.toArray();

         for (int i=0;i<c1.size() ;i++){
             if(!a1[i].equals(a2[i]))return false;
         }
         return true;

     }
    public  static Vector convertToVector(Object[] anArray) {
        if (anArray == null) {
            return null;
        }
        Vector v = new Vector(anArray.length);
        for (int i=0; i < anArray.length; i++) {
            v.add(anArray[i]);
        }
        return v;
    }
    /**
     * Returns a vector that contains the same objects as the array.
     * @param anArray  the array to be converted
     * @return  the new vector; if <code>anArray</code> is <code>null</code>,
     *				returns <code>null</code>
     */
    public  static Vector convertFloatToVector(float[] anArray) {
        if (anArray == null||anArray.length <=0) {
            return null;
        }
        Vector v = new Vector(anArray.length);
        for (int i=0; i < anArray.length; i++) {
            v.add(new Float(anArray[i]));
        }
        return v;
    }
    public  static Vector convertDoubleToVector(double[] anArray) {
        if (anArray == null||anArray.length <=0) {
            return null;
        }
        Vector v = new Vector(anArray.length);
        for (int i=0; i < anArray.length; i++) {
            v.add(new Double(anArray[i]));
        }
        return v;
    }
    /**
     * assume v contain Integer or Double or Float or Long
     * @param v
     * @return
     */
    public static float[] convertVectorToFloat(Vector v){
        Object[] os=v.toArray();
        float[] result=new float[os.length ];
        for (int i=0;i<os.length ;i++){
            Number o=(Number) os[i];
            result[i]=o.floatValue() ;// floatValue(o);

        }
        return result;
    }
    /**
     * assume v contain Integer or Double or Float or Long
     * @param v
     * @return
     */
    public static int[] convertVectorToInt(Vector v){
        Object[] os=v.toArray();
        int[] result=new int[os.length ];
        for (int i=0;i<os.length ;i++){
            Number o=(Number) os[i];
            result[i]=o.intValue() ;// floatValue(o);

        }
        return result;
    }
    /**
     * assume v contain Integer or Double or Float or Long
     * @param v
     * @return
     */
    public static double[] convertVectorToDouble(Vector v){
        Object[] os=v.toArray();
        double[] result=new double[os.length ];
        for (int i=0;i<os.length ;i++){
            Number o=(Number) os[i];
            if(o!=null)
                result[i]=o.doubleValue() ;//floatValue(o);
            else
                result[i]=Double.NaN ;

        }
        return result;
    }

    public static float floatValue(Object o) {
       // float f=0;
       /* if(o instanceof Integer){
            f=((Integer)o).floatValue() ;
        }
        else if(o instanceof Double){
            f=((Double)o).floatValue() ;
        }
        else if(o instanceof Float){
            f=((Float)o).floatValue() ;
        }
        else{
            f=((Long)o).floatValue() ;
        }*/
        Number number=(Number) o;
        float f = number.floatValue();
        return f;
    }

    /**
     *  ignore null value
     * @param values
     * @return
     */
    public static Set getUniqueValues(Collection values){
        Set uniqueValues =new HashSet();

        for (Iterator iterator = values.iterator(); iterator.hasNext();) {
            Object avalue = (Object) iterator.next();
            if(avalue!=null){// must, unable to sort on null value
                uniqueValues.add(avalue);
            }

        }
        return uniqueValues;
    }
    /**
     *
     * @param values
     * @return    an array contain no-duplicate, non-negative values
     */
    public static int[] getNoNegativeUniqueValues(Collection<Integer> values){
        Set<Integer> set=new HashSet<Integer>();
        /*for (int i=0;i<values.size();i++){
            int aindex = values.get(i);
            if(aindex >=0 ){
                set.add(aindex);
            }
        }*/

        for (Iterator iterator = values.iterator(); iterator.hasNext();) {
            int avalue = (Integer) iterator.next();
            if(avalue >=0 ){
                set.add(avalue);
            }
        }
        return CollectionUtils.convertToIntArray(set);
    }
    public static List sort(Set data){
        List sortdata=new ArrayList(data);
        Collections.sort(sortdata);
        return sortdata;

    }
    /*******************************************************************************************************
     *                map
     *******************************************************************************************************/
    /**
     *
     * @param names   a string array
     * @return  build map: a stringname --> its index in the arrary
     */
    public static Map<String,Integer> getStringIndex(String[] names) {
         Map<String,Integer> name2Index=new HashMap<String, Integer>(names.length );
        //String[] stnames = (String[]) shpnames[1];//state_name column
        for (int i=0;i< names.length ;i++){
            name2Index.put(names[i],i);
        }
        return name2Index;
    }

    /*******************************************************************************************************
     *                print
     *******************************************************************************************************/
    public static void print(Map map){
        Set keys = map.keySet();
        System.out.println("print map "+map);
        for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
            Object key = (Object) iterator.next();
            Object value = map.get(key);
            System.out.println("    "+key+" -> "+ value);
        }
        System.out.println("");

    }

    public static void main(String[] args) {


    }

}


