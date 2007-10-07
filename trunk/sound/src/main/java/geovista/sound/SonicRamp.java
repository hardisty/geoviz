/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class SonicRamp
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: SonicRamp.java,v 1.1 2003/07/18 14:01:21 hardisty Exp $
 $Date: 2003/07/18 14:01:21 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *

 */


package geovista.sound;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

//import javax.swing.colorchooser.*;
    /**
     * This class is a utility class which allows other classes to find ramped
     * and interpolated color values.
     */
public class SonicRamp {


    private transient Color lowColor; //associated with low values
    private transient Color highColor; //associated with high values

    public SonicRamp() {
        lowColor = Color.white;
        highColor = Color.black;
    }

    public void rampColors(Color[] colorList, boolean[] anchored)
    {
        if (colorList == null || colorList.length < 1) {
          return; //nothing do do
        }

        anchored[0] = true;
        anchored[colorList.length -1] = true;
        //here we walk through and ramp based on the anchored colors
        int currLowSwatch = 0;
        int currHighSwatch = 0;
        for (int j = 0; j < colorList.length - 1; j++){
          //find a locked color
          if (anchored[j]){
            this.lowColor = (colorList[j]);
            currLowSwatch = j;
            //look for the next anchored color
            for (int k = j +1; k < colorList.length; k++){
              if (anchored[k]) {
                this.highColor = (colorList[k]);
                currHighSwatch = k;
                //if there are any colors in between
                int numSwatches = currHighSwatch - currLowSwatch - 1;
                if (numSwatches  > 0) {
                  int currSwat = 1;
                  for (int l = currLowSwatch+1; l < currHighSwatch; l++){
                    double prop;
                    prop = (double)currSwat/((double)numSwatches+1);
                    currSwat++;
                    Color back = new Color(this.getRampedValueRGB(prop));
                    colorList[l] = back;
                  }//next l
                } //if swatches > 0
                //currLowSwatch = k;
              break;//found that anchor, don't find more.
              }//end if k locked

            }//next k
          }//end if j locked
        }//next j

    this.lowColor = (colorList[0]);//????

    }

    public void rampColors(Color[] colorList){
        boolean[] anchors = new boolean[colorList.length];
        anchors[0] = true;
        anchors[colorList.length -1] = true;
        this.rampColors(colorList,anchors);
    }

    public int getRampedValueRGB(double prop) {
      int redRange = this.highColor.getRed() - this.lowColor.getRed();
      int newRed = (int)Math.round(prop * (double)redRange);
      newRed = newRed + this.lowColor.getRed();

      int GreenRange = this.highColor.getGreen() - this.lowColor.getGreen();
      int newGreen = (int)Math.round(prop * (double)GreenRange);
      newGreen = newGreen + this.lowColor.getGreen();

      int BlueRange = this.highColor.getBlue() - this.lowColor.getBlue();
      int newBlue = (int)Math.round(prop * (double)BlueRange);
      newBlue = newBlue + this.lowColor.getBlue();


      int intARGB = (255 << 24) | (newRed << 16) | (newGreen << 8) | (newBlue << 0);
      return intARGB;
    }

    public int getRampedValueHSB(double aProp) {
      float prop = (float)aProp;
      float[] lowVals;
      float[] highVals;

      lowVals = Color.RGBtoHSB(this.lowColor.getRed(),this.lowColor.getGreen(),this.lowColor.getBlue(),null);
      highVals = Color.RGBtoHSB(this.highColor.getRed(),this.highColor.getGreen(),this.highColor.getBlue(),null);

      float hueRange = highVals[0] - lowVals[0];
      float newHue = prop * hueRange;
      newHue = newHue + lowVals[0];

      float saturationRange = highVals[1] - lowVals[1];
      float newSaturation = prop * saturationRange;
      newSaturation = newSaturation + lowVals[1];

      float brightnessRange = highVals[2] - lowVals[2];
      float newBrightness = prop * brightnessRange;
      newBrightness = newBrightness + lowVals[2];

      int intARGB = Color.HSBtoRGB(newHue, newSaturation, newBrightness);

      return intARGB;
    }

    public int getRampedValueHB(double aProp) {
      float prop = (float)aProp;
      float[] lowVals;
      float[] highVals;
      lowVals = Color.RGBtoHSB(this.lowColor.getRed(),this.lowColor.getGreen(),this.lowColor.getBlue(),null);
      highVals = Color.RGBtoHSB(this.highColor.getRed(),this.highColor.getGreen(),this.highColor.getBlue(),null);

      float hueRange = highVals[0] - lowVals[0];
      float newHue = prop * hueRange;
      newHue = newHue + lowVals[0];

      float brightnessRange = highVals[2] - lowVals[2];
      float newBrightness = prop * brightnessRange;
      newBrightness = newBrightness + lowVals[2];

      int intARGB = Color.HSBtoRGB(newHue, 1.0f, newBrightness);

      return intARGB;
    }
    /**
     * Main method for testing.
     */
    public static void main (String[] args) {
        JFrame app = new JFrame();
        app.getContentPane().setLayout(new BorderLayout());

        //make color ramp
        JPanel rampPan = new JPanel();
        rampPan.setLayout(new FlowLayout());
        JPanel[] panSet = new JPanel[10];
        SonicRamp ramp = new SonicRamp();

        for (int i = 0; i < panSet.length; i++){
          panSet[i] = new JPanel();
          panSet[i].setPreferredSize(new Dimension(100,100));
          double prop;
          prop = (double)i/(double)panSet.length;
          Color back = new Color(ramp.getRampedValueRGB(prop));
          panSet[i].setBackground(back);
          rampPan.add(panSet[i]);
        }
        app.getContentPane().add(rampPan,BorderLayout.SOUTH);


        //app.getContentPane().add(setColorsPan);

        app.pack();
        app.setVisible(true);

    }

}
