/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 *
 * @author: jin Chen 
 * @date: Jul 23, 2003$
 * @version: 1.0
 */
package edu.psu.geovista.common.classification;

public class CategorizerQuantiles extends BasicCategorizer {


    public CategorizerQuantiles() {
        this.classifer =new ClassifierQuantiles();

    }
     /**
     * set max and min value for each category in ctgList.
     * Note: can also exploit local variable "end" in ClassifierQuantiles.classfy()
     *      end = max value of each class.
     * @param ctgList
     * @param rawData
     * @param classedData
     */
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
