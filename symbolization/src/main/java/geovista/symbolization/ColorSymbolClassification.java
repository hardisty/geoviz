/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the interface ColorSymbolClassification
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: ColorSymbolClassification.java,v 1.2 2003/04/25 18:08:50 hardisty Exp $
 $Date: 2003/04/25 18:08:50 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */



package geovista.symbolization;


import java.awt.Color;

import geovista.common.classification.Classifier;

public interface ColorSymbolClassification {

    public Color[] symbolize(double[] data);

    //public int getNumClasses();
    public ColorSymbolizer getColorer();
    public Classifier getClasser();

}