/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Reserved.
 *
 * Contain some categories.  There are 2 cases
    1. conotain cateogries for only one dimension (variable). That is, the classification is made independently only on the variable
    2. contain categories for multiple dimensions (variables). That is, the classification is made based on these variables.
 * @author: yunping liu
 * @date: Dec 7, 2004$
 * @version: 2.0
 */
package edu.psu.geovista.common.classification;

//import java.awt.*;
import java.util.List;
import java.util.Vector;

public class BasicCategoryList implements CategoryListItf {
    protected Vector categories;
    protected String classifyMethodName;
    protected int classifyMethodType;
    protected int numOfDimension;
    protected  String variableName; //the name of the variable based on which the classification is made
    /** Creates a new instance of JDMCategoryList */
    public BasicCategoryList(Vector categories, String classifyMethodName, int classifyMethodType, 
                               int numOfDimension) {
        if (categories != null) this.categories = categories;
        else this.categories = new Vector();
        this.classifyMethodName = classifyMethodName;
        this.classifyMethodType = classifyMethodType;
        this.numOfDimension = numOfDimension;
    }
    
    public BasicCategoryList(String classifyMethodName, int classifyMethodType, int numOfDimension) {
        this(null, classifyMethodName, classifyMethodType, numOfDimension);
    }
    
    public void add(CategoryItf category) {
        this.categories.add(category);
    }
    
    public void clearAll() {
        if (this.getNumOfCategories() > 0) this.categories.clear();
    }
    
    public CategoryItf getCategory(int index) {
        return (CategoryItf)this.categories.get(index);
    }
    
    public String getClassifyMethodName() {
        return this.classifyMethodName;
    }
    
    public int getClassifyMethodType() {
        return this.classifyMethodType;
    }
    
    public int getNumOfCategories() {
        return this.categories.size();
    }
    
    public int getNumOfDimension() {
        return this.numOfDimension;
    }
    
    public void setClassifyMethodName(String classifyMethodName) {
        this.classifyMethodName = classifyMethodName;
    }
    
    public void setClassifyMethodType(int classifyMethodType) {
        this.classifyMethodType = classifyMethodType;
    }
    
    public void setNumOfDimension(int numOfDimension) {
        this.numOfDimension = numOfDimension;
    }

    public List getCategoryList() {
        return categories;
    }

    public void setCategoryList(List categoryList) {
        this.categories = (Vector)categoryList;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }
}
