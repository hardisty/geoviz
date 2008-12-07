/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the interface BivariateColorSymbolClassification
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: BivariateColorSymbolClassification.java,v 1.2 2003/04/25 18:08:50 hardisty Exp $
 $Date: 2003/04/25 18:08:50 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */

package geovista.symbolization;

import java.awt.Color;

import geovista.common.classification.Classifier;

public interface BivariateColorSymbolClassification {

	public Color[] symbolize(double[] dataX, double[] dataY);

	public ColorSymbolizer getXColorSymbolizer();

	public ColorSymbolizer getYColorSymbolizer();

	public Classifier getClasserX();

	public Classifier getClasserY();

	/**
	 * Returns an array of colors, Color[X][Y], where X = the Xth class, and Y =
	 * the Yth class;
	 * 
	 * @return the colors
	 */
	public Color[][] getClassColors();

}