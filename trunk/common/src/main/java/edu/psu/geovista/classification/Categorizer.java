package edu.psu.geovista.classification;

/**
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Java source file for the interface Classifier
 * Copyright (c), 2002, GeoVISTA Center
 * All Rights Reserved.
 *
 * To add new method in the Classifier without change existing classes implement Classifier
 * Categorizer play similar function as Classifer but implemented in
 * categorize() instead of classify().  categorize() will return a CategoryList
 *
 * User: jchen
 * Date: Jul 19, 2003
 * Time: 3:43:34 PM
 */
public interface Categorizer extends DescribedClassifier{
    public CategoryList categorize(double[] rawData, int numClasses);
    public  CategoryList categorize(double[] rawData,int[] classedData, int numClasses);

}
