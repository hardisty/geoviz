/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class BivariateColorSymbolizerLinear
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: BivariateColorSymbolizerLinear.java,v 1.2 2003/04/25 18:08:50 hardisty Exp $
 $Date: 2003/04/25 18:08:50 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *

 */


package geovista.symbolization;

import java.awt.Color;

//import javax.swing.colorchooser.*;

public class BivariateColorSymbolizerLinear implements BivariateColorSymbolizer {


  private ColorSymbolizer xSymbolizer;
  private ColorSymbolizer ySymbolizer;

    public BivariateColorSymbolizerLinear() {
        //defaults
        xSymbolizer = new ColorSymbolizerLinear();
        ySymbolizer = new ColorSymbolizerLinear();
    }

    public Color[][] symbolize(int numClassesX, int numClassesY) {
      Color[] xColors = xSymbolizer.symbolize(numClassesX);
      Color[] yColors = ySymbolizer.symbolize(numClassesY);
      Color[][] returnColors = new Color[numClassesY][numClassesX]; //row, column
      for (int i = 0; i < numClassesY; i++) {
        for (int j = 0; j < numClassesX; j++) {
          returnColors[i][j] = ColorInterpolator.mixColorsRGB(xColors[i],yColors[j]);
        }
      }
      return returnColors;
    }

    public int getNumClassesX(){
      return xSymbolizer.getNumClasses();
    }

    public int getNumClassesY(){
      return ySymbolizer.getNumClasses();
    }

    public void setXSymbolizer (ColorSymbolizer xSymbolizer) {
      this.xSymbolizer = xSymbolizer;
    }

    public ColorSymbolizer getXSymbolizer () {
      return this.xSymbolizer;
    }

    public void setYSymbolizer (ColorSymbolizer ySymbolizer) {
      this.ySymbolizer = ySymbolizer;
    }

    public ColorSymbolizer getYSymbolizer () {
      return this.ySymbolizer;
    }

}
