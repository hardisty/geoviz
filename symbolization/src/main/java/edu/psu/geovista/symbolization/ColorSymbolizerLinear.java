/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ColorSymbolizerLinear
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: ColorSymbolizerLinear.java,v 1.2 2003/04/25 18:08:50 hardisty Exp $
 $Date: 2003/04/25 18:08:50 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *

 */


package edu.psu.geovista.symbolization;

import java.awt.Color;

//import javax.swing.colorchooser.*;

public class ColorSymbolizerLinear implements ColorSymbolizer {


    private Color lowColor; //associated with low values
    private Color highColor; //associated with high values
    //private ColorRamp ramper;
    private boolean[] anchors;
    private Color[] rampingColors;

    public ColorSymbolizerLinear() {
        //defaults
        lowColor = ColorRampPicker.DEFAULT_LOW_COLOR;
        highColor = ColorRampPicker.DEFAULT_HIGH_COLOR_PURPLE;
        this.initColors(3);

    }


    private void initColors(int numColors) {
          rampingColors = new Color[numColors];
          rampingColors[0] = this.lowColor; //anchor first and last colors
          rampingColors[numColors - 1] = this.highColor;
          if (rampingColors.length > 2) {
            for (int i = 1; i < numColors - 2; i++) {
              rampingColors[i] = Color.black;
            }
          }
    }
    public Color getLowColor() {
      return this.lowColor;
    }
    public void setLowColor(Color aColor) {
      this.lowColor = aColor;
      this.rampingColors[0] = aColor;
    }
    public Color getHighColor() {
      return this.highColor;
    }
    public void setHighColor(Color aColor) {
      this.highColor = aColor;
      if (rampingColors.length > 1) {
        rampingColors[rampingColors.length -1] = aColor;
      }
    }

    public boolean[] getAnchors() {
      return this.anchors;
    }
    public void setAnchors(boolean[] anchors) {
      this.anchors = anchors;
    }

    public int getNumClasses() {
      return this.rampingColors.length;
    }

    public Color[] getRampingColors() {
      return this.rampingColors;

    }
    public void setRampingColors(Color[] rampingColors) {
      this.rampingColors = rampingColors;
      this.lowColor = rampingColors[0];
      this.highColor = rampingColors[rampingColors.length - 1];

    }

    public Color[] symbolize(int numColors){
        if (anchors == null || anchors.length != numColors) {
          anchors = new boolean[numColors];
          anchors[0] = true; //anchor first and last colors
          anchors[numColors - 1] = true;
        }
        if (rampingColors == null || rampingColors.length != numColors) {
          this.initColors(numColors);
        }

        ColorRamp ramp = new ColorRamp();

        ramp.rampColors(rampingColors,anchors);

      return rampingColors;
    }

}
