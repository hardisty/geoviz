/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the interface BivariateColorSymbolizer
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: BivariateColorSymbolizer.java,v 1.2 2003/04/25 18:08:50 hardisty Exp $
 $Date: 2003/04/25 18:08:50 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */



package edu.psu.geovista.symbolization;


import java.awt.Color;

public interface BivariateColorSymbolizer {

    public Color[][] symbolize(int numClassesX, int numClassesY);

    public int getNumClassesX();
    public int getNumClassesY();


}