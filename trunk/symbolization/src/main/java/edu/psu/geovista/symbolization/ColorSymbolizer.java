/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the interface ColorSymbolizer
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: ColorSymbolizer.java,v 1.2 2003/04/25 18:08:50 hardisty Exp $
 $Date: 2003/04/25 18:08:50 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */



package edu.psu.geovista.symbolization;


import java.awt.Color;

public interface ColorSymbolizer {

    public static final Color DEFAULT_NULL_COLOR = Color.darkGray;

    public Color[] symbolize(int numClasses);
    //public Color[] modifyColorSymbols(Color[] currSymbols, boolean[] anchors);
    public int getNumClasses();

    //public Classifier getClassifier();

}