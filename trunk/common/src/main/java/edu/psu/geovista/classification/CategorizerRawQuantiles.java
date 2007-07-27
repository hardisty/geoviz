package edu.psu.geovista.classification;

/**
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Java source file for the interface Classifier
 * Copyright (c), 2002, GeoVISTA Center
 * All Rights Reserved.
 * 
 * User: jin  chen
 * Date: Jul 19, 2003
 * Time: 3:56:02 PM
 */
public class CategorizerRawQuantiles extends BasicCategorizer {


    public CategorizerRawQuantiles() {
        this.classifer =new ClassifierRawQuantiles();

    }
     protected  void setCategorygetBoundary(CategoryList ctgList, double[] rawData, int[] classedData) {
        for (int i=0;i<rawData.length ;i++){

            double value=rawData[i];
            int categoryID=classedData[i];
            Category ctg=ctgList.getCategoryByID(categoryID);
            if(ctg != null)
                ctg.tryToSetMaxMin((float)value);
        }
    }




}
