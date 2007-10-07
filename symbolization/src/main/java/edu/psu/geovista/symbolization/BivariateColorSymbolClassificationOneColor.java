/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the interface BivariateColorSymbolClassificationOneColor
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: BivariateColorSymbolClassificationOneColor.java,v 1.2 2003/04/25 18:08:50 hardisty Exp $
 $Date: 2003/04/25 18:08:50 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */



package edu.psu.geovista.symbolization;

import java.awt.Color;

import geovista.common.classification.Classifier;
import geovista.common.classification.ClassifierEqualIntervals;

public class BivariateColorSymbolClassificationOneColor implements BivariateColorSymbolClassification {

    private ColorSymbolizer colorerX;
    private Classifier classerX;
    private ColorSymbolizer colorerY;
    private Classifier classerY;
    private transient int numClassesX;
    private transient int numClassesY;

    private Color oneColor;

    public static final int DEFAULT_NUM_CLASSES = 1;

    public BivariateColorSymbolClassificationOneColor() {
      //defaults

      this.setOneColor(Color.white);
      classerX = new ClassifierEqualIntervals();
      classerY = new ClassifierEqualIntervals();
      numClassesX = BivariateColorSymbolClassificationOneColor.DEFAULT_NUM_CLASSES;
      numClassesY = BivariateColorSymbolClassificationOneColor.DEFAULT_NUM_CLASSES;

    }

    public ColorSymbolizer getXColorSymbolizer(){
   return colorerX;
  }

  public ColorSymbolizer getYColorSymbolizer(){
   return colorerY;
  }
  public Color[][] getClassColors(){
    Color[][] currColors = new Color[numClassesX][numClassesY];
    Color[] xColors = colorerX.symbolize(numClassesX);
    Color[] yColors = colorerY.symbolize(numClassesY);

    for (int x = 0; x < currColors.length; x++){
      for (int y = 0; y < currColors.length; y++){
        Color colorX = xColors[x];
        Color colorY = yColors[y];

        currColors[x][y] = ColorInterpolator.mixColorsRGB(colorX,colorY);
      }
    }

    return currColors;

  }


    public Color[] symbolize(double[] dataX, double[] dataY){

      if (dataX == null || dataY == null) {
        return null;
      }

      if (dataX.length != dataY.length) {
        throw new IllegalArgumentException("BivariateColorSymbolClassificationOneColor.symbolize" +
                                            " recieved input arrays of different length.");
      }

      if (dataX == dataY) { //if they are the same object
        return this.symbolizeUnivariate(dataX);
      }

      Color[] colorsX = colorerX.symbolize(this.numClassesX);
      int[] classesX = classerX.classify(dataX,this.numClassesX);
      int myClassX = 0;
      Color colorX = null;

      Color[] colorsY = colorerY.symbolize(this.numClassesY);
      int[] classesY = classerY.classify(dataY,this.numClassesY);
      int myClassY = 0;
      Color colorY = null;

      Color[] returnColors = new Color[dataX.length];

      for (int i = 0; i < classesX.length; i++) {
        myClassX = classesX[i];
        myClassY = classesY[i];
        if (myClassX == Classifier.NULL_CLASS || myClassY == Classifier.NULL_CLASS) {
          returnColors[i] = ColorSymbolizer.DEFAULT_NULL_COLOR;
        } else {
          colorX = colorsX[classesX[i]];
          colorY = colorsY[classesY[i]];
          returnColors[i] = ColorInterpolator.mixColorsRGB(colorX,colorY);
        }
      }
      return returnColors;
    }

    private Color[] symbolizeUnivariate(double[] dataX){
        Color[] colorsX = colorerX.symbolize(this.numClassesX);
        int[] classesX = classerX.classify(dataX,this.numClassesX);
        int myClassX = 0;
        Color colorX = null;

      Color[] colorsY = colorerY.symbolize(this.numClassesX);
      Color colorY = null;

        Color[] returnColors = new Color[dataX.length];

      for (int i = 0; i < classesX.length; i++) {
        myClassX = classesX[i];
        if (myClassX == Classifier.NULL_CLASS) {
          returnColors[i] = ColorSymbolizer.DEFAULT_NULL_COLOR;
        } else {
          colorX = colorsX[classesX[i]];
          colorY = colorsY[classesX[i]];
          returnColors[i] = ColorInterpolator.mixColorsRGB(colorX,colorY);
        }
      }

      return returnColors;
    }

    public void setColorerY(ColorSymbolizer colorerY) {
      this.colorerY = colorerY;
      this.numClassesY = colorerY.getNumClasses();
    }
    public ColorSymbolizer getColorerY() {
      return this.colorerY;
    }

    public void setClasserY(Classifier classerY) {
      this.classerY = classerY;
    }
    public Classifier getClasserY() {
      return this.classerY;
    }

    public void setColorerX(ColorSymbolizer colorerX) {
      this.colorerX = colorerX;
      this.numClassesX = colorerX.getNumClasses();
    }
    public ColorSymbolizer getColorerX() {
      return this.colorerX;
    }

    public void setClasserX(Classifier classerX) {
      this.classerX = classerX;
    }
    public Classifier getClasserX() {
      return this.classerX;
    }
  public Color getOneColor() {
    return oneColor;
  }
  public void setOneColor(Color oneColor) {
      this.oneColor = oneColor;
      ColorSymbolizerLinear colX = new ColorSymbolizerLinear();
      colX.setLowColor(oneColor);
      colX.setHighColor(oneColor);
      colorerX = colX;

      ColorSymbolizerLinear colY = new ColorSymbolizerLinear();
      colY.setLowColor(oneColor);
      colY.setHighColor(oneColor);
      colorerY = colY;
  }

}