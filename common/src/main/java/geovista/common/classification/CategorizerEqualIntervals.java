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
package geovista.common.classification;

import java.util.logging.Logger;



public class CategorizerEqualIntervals extends BasicCategorizer {

	protected final static Logger logger = Logger.getLogger(CategorizerEqualIntervals.class.getName());
    public CategorizerEqualIntervals() {
        this.classifer =new ClassifierEqualIntervals();

    }

    protected void setCategorygetBoundary(CategoryList ctgList, double[] rawData, int[] classedData) {
            double[] boundaries =((ClassifierEqualIntervals)classifer).getBoundaries(rawData,ctgList.getNumberOfCategory() );
            logger.finest("boundaries:"+boundaries);
            for (int i=0;i<ctgList.getNumberOfCategory() ;i++){

                Category ctg=ctgList.getCategoryByID(i);
                if(ctg != null){
                    ctg.setMin((float)boundaries[i]);
                    ctg.setMax((float)boundaries[i+1]);
                }
            }
    }

}