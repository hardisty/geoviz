/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 * Description
 *
 * User: jinchen
 * Date: Jun 2, 2003
 * Time: 11:05:30 AM
 */
package edu.psu.geovista.common.classification;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryList {

	final static Logger logger = Logger.getLogger(CategoryList.class.getName());
    private Color[] colors; //category's color, not data(record) color
    private Color[] dataColors;//an element is the color of each record. It exist for cash purpose.
    protected String categoryMethodName;
    private Hashtable ctgList=new Hashtable();//categoryList

    public CategoryList(Color[] colors) {
 
            this.colors = colors;


    }
    public CategoryList(Color[] colors,int[] classedData) {
        this(colors);
        if (classedData!=null)
        this.generateCategoy(classedData,colors.length );
    }
     public CategoryList(int[] classedData, int numOfCategory) {
          if (classedData!=null)
          this.generateCategoy(classedData,numOfCategory );

     }
     public void put(Object key, Object value) {
         ctgList.put(key,value);
     }
    /**
     *
     * @param classedData    array's index is record id, value is the category it belong to
     * @param numOfCategory
     */
    public void generateCategoy(int[] classedData, int numOfCategory) {
        for (int i=0;i<numOfCategory;i++){
            Category c=new Category();
            c.setShowLevel(i);
            c.setVisible(true);
            c.setID(new Integer(i)) ;//!!! make first category's ID =0, as getCategory(int id) use 0-based index
            put(c.getID() ,c);
        }

        for (int i=0;i<classedData.length ;i++){
            int ctgIndex=classedData[i];
            Category c=this.getCategoryByID(ctgIndex);
            if(c!=null)
            c.add(new Integer(i));//put the record id in the category
        }
        //dp{

        if (logger.isLoggable(Level.FINEST)){
            logger.finest("category size");
            ArrayList ctgs=Collections.list(ctgList.elements() );
            Iterator iter=ctgs.iterator() ;
            while(iter.hasNext()){
               Category ctg = (Category)iter.next();
               logger.finest(ctg +" has "+ctg.getMemberIdSize() );
               logger.finest("");
            }
        }//dp}

        if (this.getColors() !=null){
            this.setCategoryColor() ;
        }


    }

    public HashSet getRecordsNeedShown(){
        HashSet all=new HashSet();
        //HashSet tmp;
        Iterator iter=ctgList.values().iterator() ;
        while(iter.hasNext()){
           Category ctg = (Category)iter.next();
           if (ctg.isVisible() ){
              logger.finest("visible " +ctg +" has "+ctg.getMemberIdSize() );
              all.addAll(ctg.getMemberIds()) ;
           }

        }
        return all;

    }
    public HashSet getVisibleCategories(){
        return null;

    }

    private void setCategoryColor() {
        for (int i=0;i<this.colors.length ;i++){
            Category c=this.getCategoryByID(i);
            c.setColor(colors[i]);
        }
    }

    public  Category getCategoryByID(int ctgIndex) {

        return(Category)this.ctgList.get(new Integer(ctgIndex)) ;
    }
    public Category getCategoryByID(Integer id) {
        return(Category)this.ctgList.get(id);
    }
    public  Category getCategoryByViewID(int ctgIndex) {
        return getCategoryByID(ctgIndex-1);
    }
    public  Category getCategoryByViewID(Integer id) {
        int ctgIndex=id.intValue() -1;
        return getCategoryByID(ctgIndex);
    }

    public int getNumberOfCategory() {
        return this.ctgList.size() ;
    }

    public void setCategoryMethodName(String categoryMethodName) {
        this.categoryMethodName = categoryMethodName;
    }

    public String getCategoryMethodName() {
        return categoryMethodName;
    }

    public void setColors(Color[] colors) {
        this.colors = colors;
        this.setCategoryColor() ;
    }

    public Color[] getColors() {

        return colors;
    }

    public Color[] getDataColors() {
        return dataColors;
    }

    public void setDataColors(Color[] dataColors) {
        this.dataColors = dataColors;
    }

    public Hashtable getCtgList() {
        return ctgList;
    }
    public int size(){
        return this.ctgList.size() ;
    }

    public void setAllVisible() {

    }
    public void showCategoryList() {
        if (logger.isLoggable(Level.FINEST)){
            logger.finest("show category -->");
            ArrayList ctgs=Collections.list(ctgList.elements() );
            Iterator iter=ctgs.iterator() ;
            while(iter.hasNext()){
               Category ctg = (Category)iter.next();
               logger.finest("Category:"+ctg.getID() );
               logger.finest(ctg +" has "+ctg.getMemberIdSize() );
               logger.finest("data range:"+ctg.getMin() +"-"+ctg.getMax() );
               logger.finest("");
            }
             logger.finest("show category <--");
        }//dp}
    }


}
