/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Reserved.
 *
 * To maintain the attributes' index in DataSetForApps. Many applications using DataSetForApps depend on index of attributes
 * This class help these class to get the index based on attributes' name

 * @author: jin Chen
 * @date: Jan 12, 2005$
 * @version: 1.0
 */
package edu.psu.geovista.data.geog.utils;

import java.util.Arrays;
import java.util.HashMap;

import edu.psu.geovista.data.geog.DataSetForApps;

public class AttributesNameIndexs {
    HashMap nameIndexs=new HashMap();//maintain index of variables in original dataSet. For now scatterplot only accept index to display 2 particular variables
    protected String[] attributes;

    public AttributesNameIndexs(String[] attributes) {
        //this.attributes = attributes;
        this.attributes = (String[]) attributes.clone();
        setNameIndex(this.attributes);//must called before names is sort
    }

    public AttributesNameIndexs(DataSetForApps dataSet) {
            String[] attrsn = (String[])dataSet.getAttributeNamesNumeric();// .getAttributeNamesNumeric();//!!! not match, not work.getAttributeNamesNumeric();//[0];
            attributes = (String[]) attrsn.clone();

            setNameIndex(attributes);// must called before names is sort
        }

        private void setNameIndex(String names[]){
            nameIndexs.clear();
            for (int i=0;i<names.length ;i++){
                this.nameIndexs.put(names[i],new Integer(i+1));
            }
        }
        public  int getIndex(String varname){
            Integer Index = (Integer) this.nameIndexs.get(varname);
            if(Index!=null){
                return Index.intValue() ;
            }
            else{
                return -1;
            }
        }

    public String[] getAttributes() {
        return attributes;
    }
    public String[] getSortedAttributes() {

        String[] attrs = (String[]) attributes.clone();
        Arrays.sort(attrs);
        return (String[]) attrs;
    }
}
