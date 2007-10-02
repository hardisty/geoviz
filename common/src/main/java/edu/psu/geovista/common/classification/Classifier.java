/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the interface Classifier
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: Classifier.java,v 1.2 2003/04/25 18:18:24 hardisty Exp $
 $Date: 2003/04/25 18:18:24 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *

 */


package edu.psu.geovista.common.classification;


public interface Classifier {

    public static final int NULL_CLASS = -1;


    /**
     * Returns an integer array the same length as the input data array,
      * with each integer representing the class of that datum.
      *
      */


    public int[] classify(double[] data, int numClasses);
}
