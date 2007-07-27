/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the interface ShapeSymbolizer
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: ShapeSymbolizer.java,v 1.2 2003/04/25 18:08:50 hardisty Exp $
 $Date: 2003/04/25 18:08:50 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */



package edu.psu.geovista.symbolization;


import java.awt.Color;
import java.awt.Shape;

public interface ShapeSymbolizer {

    public static final Color DEFAULT_NULL_COLOR = Color.darkGray;

    public Shape[] symbolize(int numClasses);

    public int getNumClasses();

}