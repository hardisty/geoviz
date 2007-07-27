package edu.psu.geovista.classification;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author Xiping Dai
 * @version 1.0
 */

public interface MultiVariateClassifier {

    public static final int NULL_CLASS = -1;

    public int[] multiVariateClassify(Object[] data, int numClasses);

}