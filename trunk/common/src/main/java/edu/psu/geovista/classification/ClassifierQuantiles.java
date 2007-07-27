package edu.psu.geovista.classification;

/**
 * Title: ClassifierQuantiles
 * Description: Quantile classification method
 * Copyright:    Copyright (c) 2001
 * Company: GeoVISTA
 * @author Xiping Dai
 * @version 1.0
 */

import java.util.logging.Logger;

import edu.psu.geovista.data.ArraySort2D;

public class ClassifierQuantiles implements DescribedClassifier, BoundaryClassifier {
    private static final String shortName = "Q Tiles";
    private static final String fullName = "Quantiles";
    private int[] classification;
    private double[][] dataWithIndex;
    
    protected final static Logger logger = Logger.getLogger(ClassifierQuantiles.class.getName());
    public String getShortName(){
      return ClassifierQuantiles.shortName;
    }

    public String getFullName(){
      return ClassifierQuantiles.fullName;
    }
    public ClassifierQuantiles() {
    }

    public double[] getBoundaries(double[] data, int numClasses){
      double[] boundaries = new double[numClasses +1];
      int nObs;
      if (dataWithIndex == null || dataWithIndex.length != data.length || dataWithIndex[0].length != 2) {
          dataWithIndex = new double[data.length][2];
      }

      //add index
      //and find number of non-nulls
      nObs = 0;
      for (int i = 0; i < data.length; i++) {
        dataWithIndex[i][0] = data[i];
        dataWithIndex[i][1] = i;
        if (!Double.isNaN(data[i])){
          nObs++;
        }
      }

      double nPerClass = (double)nObs/(double)numClasses;

      //now sort
      ArraySort2D sorter = new ArraySort2D();
      sorter.sortDouble(dataWithIndex,0);

      for (int i = 0; i < numClasses; i++){
        boundaries[i] = dataWithIndex[(int)(i*nPerClass)][0];
      }
      boundaries[numClasses] = dataWithIndex[nObs-1][0];
      return boundaries;
    }


	public int[] classify (double[] data, int numClasses){

		if (data == null){
            throw new IllegalArgumentException("Can't pass null into classify method");
        }
        if (numClasses < 1){
            throw new IllegalArgumentException("Need at least one class to classify");
        }

      if (classification == null || classification.length != data.length) {
          classification = new int[data.length];
      }
      if (dataWithIndex == null || dataWithIndex.length != data.length || dataWithIndex[0].length != 2) {
          dataWithIndex = new double[data.length][2];
      }

      //add index
      //and find number of non-nulls
      int nObs = 0;
      for (int i = 0; i < data.length; i++) {
        dataWithIndex[i][0] = data[i];
        dataWithIndex[i][1] = i;
        if (!Double.isNaN(data[i])){
          nObs++;
        }
      }

	  //If classes number more than available obeservations numbers for classfy
	  //Change the the number of classes to the available number
	  if (numClasses > nObs){
		numClasses = nObs;
	  }

      //now sort
      ArraySort2D sorter = new ArraySort2D();
      sorter.sortDouble(dataWithIndex,0);
	  int index;

		if (numClasses == 1) {
		    for (int j = 0; j < nObs; j ++){
				index = (int)dataWithIndex[j][1];
	    		this.classification[index] = 0;
			}
		}else {
			

			
			double numInEachClass = (double)nObs/(double)numClasses;
            int endElm = 0;
			int lastClassEndElm=0;
			double end;
            // Calculate the begin and end values for this classification range.	For quantiles, we might
            // have to do some tricky round-off.
			for (int i = 0; i < numClasses; i++) {
			    //lastClassEndElm = endElm;
			    if (i < (numClasses - 1)){
				    endElm = (int)(numInEachClass*(i + 1))-1;
				    //Check to see if the class end (boundary) has passed the ideal one.
					if ((lastClassEndElm >= endElm && (lastClassEndElm != 0))){
						continue;
					}

                    // Determine how many elements in the array are equal to the last element in this (i) class.
				    end = dataWithIndex[endElm][0];
					int sameBefore = 0;
					int sameAfter = 0;
					while(((endElm - sameBefore -1) >= lastClassEndElm)&&(dataWithIndex[endElm - sameBefore -1][0] == end)){
						sameBefore ++;
					}
					// Determine how many elements in the i+1 class are equal to the last element in i class .
					while(((endElm + sameAfter +1) <= nObs)&&(dataWithIndex[endElm + sameAfter +1][0] == end)){
						sameAfter ++;
					}
                    // If the same numbers of same data on both sides of classification line,
                    // assign all of the same data to the next class.
					if ((sameBefore <= sameAfter)) {
						endElm = endElm - sameBefore;
						for (int j = lastClassEndElm+1; j <= endElm; j ++){
							index = (int)dataWithIndex[j][1];
						    this.classification[index] = i;
						}
						lastClassEndElm = endElm;

						if ((int)(sameAfter / numInEachClass) > 0){
							endElm = endElm + sameBefore + sameAfter;
						    for (int j = lastClassEndElm+1; j <= endElm; j ++){
							    index = (int)dataWithIndex[j][1];
								this.classification[index] = i+1;
						    }
							lastClassEndElm = endElm;
						}
					}else {
						endElm = endElm + sameAfter;
						for (int j = lastClassEndElm+1; j <= endElm; j ++){
							index = (int)dataWithIndex[j][1];
						    this.classification[index] = i;
						}
						lastClassEndElm = endElm;
					}
				} else {
				    // Everything goes into last bucket
					//System.out.print("last Elm: " + (nObs - 1) + " ");
					index = 0;
					for (int j = lastClassEndElm + 1; j < nObs; j ++){
						index = (int)dataWithIndex[j][1];
					    this.classification[index] = i;
					}
					logger.finest("last class drawn.");
				}
			}

			/*for (int i = 0; i < numClasses; i++) {
				int sameBefore = 0;
				int sameAfter = 0;
				lastClassEndElm = endElm;
				if (i < (numClasses - 1)) {
					elmClassified = (int)(numInEachClass*(i + 1));
					logger.finest("elmClassified: " + elmClassified);
					if (numInEachClass*(i + 1) < (double)(elmClassified + 0.5)) {
						endElm = elmClassified - 1;
					}
					else {
						endElm = elmClassified;
					}
					logger.finest("endElm: " + endElm);
					end = dataWithIndex[endElm][0];
                    // Determine how many elements in the array are equal to the sortedArray[endElm] (last element in
                    // this class. Assign the elements with the same value to the side with more same elements.
					if ((endElm - 1) >= 0){
					for (int same = 1; (((endElm - same) >= 0) && (dataWithIndex[endElm - same][0] == end)); same++) {
						sameBefore++;
						int indexValue = endElm - sameBefore;
						logger.finest("GvCQ.classify(), sameBefore = " + sameBefore + ", indexValue = "
						//		+ indexValue);
						if (indexValue == lastClassEndElm)
							break;
					}
					}
					for (int same = 1; ((dataWithIndex[endElm + same][0] == end) &&
							((endElm + same) < nObs)); same++) {
						sameAfter++;
						if ((endElm + sameAfter) == nObs)
							break;
					}
                    // If the same numbers of same data on both sides of classification line,
                    // assign all of the same data to the next class.
					if (((endElm - sameBefore) == 0) || ((endElm - sameBefore) == lastClassEndElm
							+ 1)) {
						endElm = endElm + sameAfter;
					}
					else {
						if (((sameBefore) < sameAfter) && (sameBefore < (int)(numInEachClass +
								0.5) - 1)) {
							endElm = endElm - sameBefore;
						}
						else {
							endElm = endElm + sameAfter;
						}
					}
					if (endElm == (nObs - 1)) {                  //this class include last element
						if (i == 0) {             //only have one value in this data attribute
							for (int j = 0; j < data.length; j ++){
								this.classification[j] = 0;
							}
							System.err.println("Need more than one observation value.");
						}
						else {
							while (i < numClasses - 1) {
								index = 0;
								for (int j = lastClassEndElm; j < endElm; j ++){
									index = (int)dataWithIndex[j][1];
								    this.classification[index] = i;
							    }
								lastClassEndElm = endElm + 1;
							}
							index = 0;
							for (int j = lastClassEndElm; j < nObs; j ++){
								index = (int)dataWithIndex[j][1];
							    this.classification[index] = i;
							}
						}
					}
					else {
						index = 0;
						for (int j = lastClassEndElm+1; j <= endElm; j ++){//some problem here?
							index = (int)dataWithIndex[j][1];
						    this.classification[index] = i;
						}
                        //If the class range go over to the supposed last element of the next class, set the next class
                        //as a class with only one element which is the endElm element in the array.
						while ((i + 1 < numClasses) && (endElm >= ((int)(numInEachClass*(i +
								2.0) + 0.5) - 1))) {
                            logger.finest("end:" + end);
							index = (int)dataWithIndex[endElm][1];
							this.classification[index] = i;
						}
					}
				}
				else {
				    // Everything goes into last bucket
					//System.out.print("last Elm: " + (nObs - 1) + " ");
					index = 0;
					for (int j = lastClassEndElm + 1; j < nObs; j ++){
						index = (int)dataWithIndex[j][1];
					    this.classification[index] = i;
					}
					logger.finest("last class drawn.");
				}
			}*/
		}
		index = 0;
		for (int j = nObs; j < data.length; j++){
			index = (int)dataWithIndex[j][1];
			this.classification[index] = Classifier.NULL_CLASS;
		}
		return classification;
	}
}
