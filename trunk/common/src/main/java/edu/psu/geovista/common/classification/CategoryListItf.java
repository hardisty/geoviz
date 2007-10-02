package edu.psu.geovista.common.classification;

/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Reserved.
 *
 *
 * @author: jin Chen 
 * @date: Jan 3, 2005$
 * @version: 1.0
 */

public interface CategoryListItf {
    public void add(CategoryItf ctg);
    public CategoryItf getCategory(int index);
    public String getVariableName();
    public void setVariableName(String variableName);
    public String getClassifyMethodName();
    public void setClassifyMethodName(String classifyMethodName);
    public int getClassifyMethodType() ;
    public void setClassifyMethodType(int classifyMethodType) ;
    public int getNumOfDimension();
    public void setNumOfDimension(int numOfDimension);
    public int getNumOfCategories();
    //public List getCtgList()
}
