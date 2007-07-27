/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class RampSwatch
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: RampSwatch.java,v 1.3 2005/02/12 20:30:47 hardisty Exp $
 $Date: 2005/02/12 20:30:47 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *

 */


package edu.psu.geovista.symbolization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.TexturePaint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class RampSwatch extends JPanel implements MouseListener{

    protected boolean anchored;
    protected Color swatchColor;
    protected transient ColorRampPicker parent;
    protected transient boolean isEnd;
    protected transient ImageIcon iconBlack;
    protected transient ImageIcon iconWhite;
    protected transient TexturePaint texPaint;
    protected final static Logger logger = Logger.getLogger(RampSwatch.class.getName());
    public RampSwatch(ColorRampPicker parent, boolean anchored, boolean end) {
        this.makeImage();
        this.parent = parent;
        this.swatchColor = Color.white;
        this.setBackground(swatchColor);
        this.addMouseListener(this);
        this.setAnchored(anchored);
        this.isEnd = end;
    }


    public void makeImage(){
        Class cl = this.getClass();
        URL urlGif = cl.getResource("resources/anchorBlack.gif");
        ImageIcon icon = new ImageIcon(urlGif,
                               "Anchors the color in a ramp");
        this.iconBlack = icon;

        URL urlGif2 = cl.getResource("resources/anchorWhite.gif");
        ImageIcon icon2 = new ImageIcon(urlGif2,
                               "Anchors the color in a ramp");
        this.iconWhite = icon2;
    }

    public void setTexPaint(TexturePaint texPaint){
      this.texPaint = texPaint;
    }

    public void setSwatchColor(Color newColor){
      this.swatchColor = newColor;
      this.setBackground(newColor);
    }
    public Color getSwatchColor(){
      return this.swatchColor;
    }
    public void setAnchored(boolean anchor){
      this.anchored = anchor;
      if (anchor || this.isEnd){
        this.setBorder(BorderFactory.createLoweredBevelBorder());
      } else {
        this.setBorder(BorderFactory.createRaisedBevelBorder());
      }
    }
    public boolean getAnchored(){
      return this.anchored;
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    if (e.getClickCount() > 1) { //double or more clicks
        //this.swatch.setBorder(BorderFactory.createLineBorder(Color.black));
        logger.finest("Mouse clicked (# of clicks: "  + e.getClickCount() + ")");
      GvColorChooser chooser = new GvColorChooser();
      Color newColor = chooser.showGvDialog(
                                                RampSwatch.this,
                                                "Pick a Color",
                                                this.getBackground());
        if (newColor != null) {
          this.requestFocus();
          this.setSwatchColor(newColor);
          this.setToolTipText("<html> " +
                                          "Red = " + newColor.getRed() + "<br>" +
                                          "Green = " +  newColor.getGreen() + "<br>" +
                                          "Blue = " + newColor.getBlue());

          this.setAnchored(true);
          this.parent.swatchChanged();


        }//end if newColor
      } else if (e.getClickCount() == 1) {//toggle anchor state on single click
        if (this.isEnd){
          return;// if we are an end, we should always remain anchored!
        }
        if (this.anchored) {
          this.setAnchored(false);
          this.parent.swatchChanged(); //if we are now "unanchored", need to update
        } else {
          this.setAnchored(true); // This won't affect other swatches
        }
      }//end if doubleclick
    }

    public void paintComponent(Graphics g) {
      g.setColor(this.getBackground());

      //adding support for textures
      if (this.texPaint != null) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setPaint(texPaint);
        g2.fillRect(0,0,this.getWidth(),this.getHeight());
      } else {
        g.fillRect(0,0,this.getWidth(),this.getHeight());
      }


      if (this.getAnchored()) {
        int midX = this.getWidth() / 2;
        int midY = this.getHeight() / 2;

        Color c = this.getBackground();
        int colorValue = c.getRed() + c.getBlue() + c.getGreen();
        Image ico = null;
        if (colorValue > 200) { //pulled this out of my hat
          ico = this.iconBlack.getImage();
        } else {
          ico = this.iconWhite.getImage();
        }
        midX = midX - (ico.getWidth(this)/2);
        midY = midY - (ico.getHeight(this)/2);
        g.drawImage(ico,midX,midY,this);
      }
    }
    /**
     * Main method for testing.
     */
    public static void main (String[] args) {
        JFrame app = new JFrame();
        //app.getContentPane().setLayout(new BorderLayout());
        app.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        ColorRampPicker pick = new ColorRampPicker();
        RampSwatch swat = new RampSwatch(pick, true, false);
        app.getContentPane().add(swat);

        //app.getContentPane().add(swatchesPanel,BorderLayout.SOUTH);


        //app.getContentPane().add(setColorsPan);

        app.pack();
        app.setVisible(true);

    }

}
