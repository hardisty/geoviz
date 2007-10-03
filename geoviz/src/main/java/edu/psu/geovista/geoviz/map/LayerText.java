/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class LayerText
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: LayerText.java,v 1.4 2005/07/16 17:24:12 hardisty Exp $
 $Date: 2005/07/16 17:24:12 $
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 -------------------------------------------------------------------   */



package  edu.psu.geovista.geoviz.map;

//imports for Main method
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import javax.swing.JFrame;

import edu.psu.geovista.symbolization.glyph.Glyph;


/**
 * put your documentation comment here
 */
public class LayerText extends LayerShape {
    transient Vector strings;
    transient Vector points;
    transient Vector fonts;
    transient Vector colors;
    transient Color defaultColor;
    transient Font defaultFont;
    //GeoReference gvGeoReference;
    private transient boolean scalable;


    public int findIndication(int x, int y) {return Integer.MIN_VALUE;}
    /**
     * put your documentation comment here
     * @param scalable
     */
    public void setScaleable (boolean scalable) {
        this.scalable = scalable;
    }

    public void setGlyphs(Glyph[] glyphs){
      //XXX noop
    }

    /**
     * put your documentation comment here
     * @return
     */
    public boolean getScaleable () {
        return  this.scalable;
    }

    /**
     * put your documentation comment here
     * @param s
     * @param p
     * @param f
     * @param c
     */
    public void addString (String s, Point p, Font f, Color c) {
        strings.add(s);
        points.add(p);
        fonts.add(f);
        colors.add(c);
    }

    /**
     * put your documentation comment here
     * @param s
     * @param p
     * @param fontSize
     * @param c
     */
    public void addString (String s, Point p, float fontSize, Color c) {
        strings.add(s);
        points.add(p);
        fonts.add(defaultFont.deriveFont(fontSize));
        colors.add(c);
    }

    /**
     * put your documentation comment here
     * @param s
     * @param p
     */
    public void addString (String s, Point p) {
        strings.add(s);
        points.add(p);
        fonts.add(defaultFont);
        colors.add(defaultColor);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public Vector getStrings () {
        return  strings;
    }

    /**
     * put your documentation comment here
     * @return
     */
    public Vector getPoints () {
        return  points;
    }

    /**
     * put your documentation comment here
     * @return
     */
    public Vector getFonts () {
        return  fonts;
    }




    /*
     * selectionX1 is expected to be less than selectionX2, same with Y1 and y2
     */
    public void findSelection (int selectionX1, int selectionX2, int selectionY1,
            int selectionY2) {

    }
    /*
     * selectionX1 is expected to be less than selectionX2, same with Y1 and y2
     */
    public void findSelectionShift (int selectionX1, int selectionX2, int selectionY1,
            int selectionY2) {

    }


    /**
     * put your documentation comment here
     * @param g2
     */
    public void render (Graphics2D g2) {

        int numStrings = strings.size();
        for (int stringNum = 0; stringNum < numStrings; stringNum++) {
            Point p = (Point)getPoints().get(stringNum);
            String drawS = (String)getStrings().get(stringNum);
            Font f = (Font)getFonts().get(stringNum);
            g2.setFont(f);
            //Color c = (Color)getColors().get(stringNum);
            //g2.setColor(c);
            g2.drawString(drawS, p.x, p.y);
        }
    }

    /**
     * dummy method to fit superclass
     * @param polygonColors
     */
    public void setColorIndex (int[] polygonColors) {
      //this.colorIndex = polygonColors;
    }
    /**
     * dummy method to fit superclass
     * @param polygonColors
     */
    public int[] getColorIndex () {
      //return this.colorIndex;
          return null;
    }
    /**
     * Main method for testing.
     */
    public static void main (String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setSize(200, 200);
        jFrame.setVisible(true);
        Graphics2D g2 = (Graphics2D)jFrame.getGraphics();
        String s = "Hello World!";
        Point point = new Point(50, 100);
        LayerText gvTextLayer = new LayerText();
        gvTextLayer.addString(s, point);
        gvTextLayer.render(g2);
    }
}



