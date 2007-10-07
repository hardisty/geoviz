/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ColorRampPicker
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: ColorRampPicker.java,v 1.12 2005/02/12 21:37:47 hardisty Exp $
 $Date: 2005/02/12 21:37:47 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package geovista.symbolization;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

public class ColorRampPicker
    extends JPanel
    implements ComponentListener {

  public RampSwatch[] panSet;
  protected boolean[] anchored;
  protected Color[] colors;
  protected ColorRamp ramp;
  private int nSwatches;

  public static final String COMMAND_SWATCH_COLOR_CHANGED = "color_changed";
  public static final String COMMAND_SWATCH_TEXTURE_CHANGED = "texture_changed";

  public static final int DEFAULT_NUM_SWATCHES = 3;

  public static final Color DEFAULT_LOW_COLOR = new Color(200, 200, 200); //light grey
  public static final Color DEFAULT_HIGH_COLOR_PURPLE = new Color(150, 0, 150);
  public static final Color DEFAULT_HIGH_COLOR_GREEN = new Color(0, 150, 0);

  public static final int X_AXIS = 0;
  public static final int Y_AXIS = 1;
  private transient int currOrientation = 0;

  public ColorRampPicker() {
    init();
  }

  private void init() {
    //this.setBorder(BorderFactory.createLineBorder(Color.black));
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    ramp = new ColorRamp();

    nSwatches = ColorRampPicker.DEFAULT_NUM_SWATCHES; //default
    this.colors = new Color[nSwatches];
    this.colors[0] = ColorRampPicker.DEFAULT_LOW_COLOR;
    this.colors[nSwatches - 1] = ColorRampPicker.DEFAULT_HIGH_COLOR_PURPLE;
    anchored = new boolean[nSwatches];
    this.makeRamp(nSwatches);
    this.rampSwatches();
    this.ramp.rampColors(colors, anchored);

    this.setPreferredSize(new Dimension(365, 20)); //these match 0.5 of the ClassifierPicker
    //this.setMinimumSize(new Dimension(200,20));
    //this.setMaximumSize(new Dimension(1000,60));
    this.addComponentListener(this);
  }

  public void makeRamp(int nSwatches) {
    //first find out if we already have some colors etc.
    //if so, track the num.
    int len = 0;
    if (colors != null) {
      len = colors.length;
      this.removeAll();
    }

    panSet = new RampSwatch[nSwatches];
    for (int i = 0; i < panSet.length; i++) {
      if (i == 0) { //first one
        panSet[i] = new RampSwatch(this, true, true);
        panSet[i].setSwatchColor(this.getLowColor());
      }
      else if (i == nSwatches - 1) { //last one
        panSet[i] = new RampSwatch(this, true, true);
        panSet[i].setSwatchColor(this.getHighColor());
      }
      else {
        if (i < len - 1) {
          boolean anch = anchored[i];
          Color c = colors[i];
          panSet[i] = new RampSwatch(this, anch, false);
          panSet[i].setSwatchColor(c);
        }
        else {
          panSet[i] = new RampSwatch(this, false, false);
          panSet[i].setSwatchColor(Color.white);
        }
      }
      //panSet[i].setPreferredSize(new Dimension(40,40));
      this.add(panSet[i]);
      //XXX this next box spacer business needs to be broken out properly
      //XXX using min, max, pref sizes??? maybe borders are enough...
      /*
                 int propWidth = this.getWidth()/100;
                 logger.finest(propWidth);
                 if (propWidth < 1) {
        propWidth = 1;
                 } else if (propWidth > 5) {
        propWidth = 5;
                 }
                 Dimension smallBox = new Dimension(propWidth,propWidth);
                 this.add(new Box.Filler(smallBox,smallBox,smallBox));
       */
    }
    anchored = new boolean[nSwatches];
    colors = new Color[nSwatches];

    //swatSlider.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
  }

  public void rampSwatches() {
   if (panSet == null){
     return;
   }
    if (panSet.length <= 0) {
      return;
    }
    for (int j = 0; j < panSet.length; j++) {
      colors[j] = panSet[j].getSwatchColor();
      if (panSet[j].getAnchored()) {
        anchored[j] = true;
      }
      else {
        anchored[j] = false;
      }
    }
    this.ramp.rampColors(this.colors, this.anchored);
    for (int j = 0; j < panSet.length; j++) {
      panSet[j].setSwatchColor(colors[j]);
    }

  }

  public void swatchChanged() {
    this.rampSwatches();
    this.fireActionPerformed(ColorRampPicker.COMMAND_SWATCH_COLOR_CHANGED);
  }

  private void changeOrientation(int orientation) {
    if (orientation == this.currOrientation) {
      return;
    }
    else if (orientation == ColorRampPicker.X_AXIS) {
      Component[] comps = new Component[this.getComponentCount()];
      int counter = 0;
      for (int i = this.getComponentCount() - 1; i > -1; i--) {
        comps[counter] = this.getComponent(i);
        counter++;
      }
      //this.removeAll();
      this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
      for (int i = 0; i < comps.length; i++) {
        this.add(comps[i]);
      }

      this.currOrientation = ColorRampPicker.X_AXIS;
      this.revalidate();
    }
    else if (orientation == ColorRampPicker.Y_AXIS) {
      Component[] comps = new Component[this.getComponentCount()];
      for (int i = 0; i < this.getComponentCount(); i++) {
        comps[i] = this.getComponent(i);
      }
      //this.removeAll();
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      for (int i = this.getComponentCount() - 1; i > -1; i--) {
        this.add(comps[i]);
      }
      this.currOrientation = ColorRampPicker.Y_AXIS;
      this.revalidate();
    }

  }

  //start component event handling
  //note: this class only listens to itself
  public void componentHidden(ComponentEvent e) {}

  public void componentMoved(ComponentEvent e) {}

  public void componentShown(ComponentEvent e) {}

  public void componentResized(ComponentEvent e) {
    float ratio = ( (float)this.getWidth() / (float)this.getHeight());

    if (ratio >= 1 && this.currOrientation == ColorRampPicker.Y_AXIS) {
      this.changeOrientation(ColorRampPicker.X_AXIS);
    }
    if (ratio < 1 && this.currOrientation == ColorRampPicker.X_AXIS) {
      this.changeOrientation(ColorRampPicker.Y_AXIS);
    }
  }

  //end component event handling
  //start accessors

  public void setPanSet(RampSwatch[] panSet) {
    this.panSet = panSet;
  }

  public RampSwatch[] getPanSet() {
    return this.panSet;
  }

  public void setAnchored(boolean[] anchored) {
    this.anchored = anchored;
  }

  public boolean[] getAnchored() {
    return this.anchored;
  }

  public void setColors(Color[] colors) {
    this.nSwatches = colors.length;
    this.colors = colors;
    this.anchored = new boolean[colors.length];
    for (int i = 0; i < colors.length; i++) {
      this.anchored[i] = true;
    }
    this.setLowColor(colors[0]);
    this.setHighColor(colors[colors.length - 1]);
    this.colors = colors;
    this.rampSwatches();
    this.repaint();
  }

  public Color[] getColors() {
    return this.colors;
  }

  //added by Jamison Conley Dec. 11 2003
  public SimplePaletteImpl getPalette(){
      SimplePaletteImpl palette = new SimplePaletteImpl();
      palette.setLowColor(this.getLowColor());
      palette.setHighColor(this.getHighColor());
      palette.addAnchors(colors, anchored);
      palette.setName("ramp palette");
      return palette;
  }

  public void setRamp(ColorRamp ramp) {
    this.ramp = ramp;
  }

  public ColorRamp getRamp() {
    return this.ramp;
  }

  public void setLowColor(Color lowColor) {
    this.colors[0] = lowColor;
    this.panSet[0].setSwatchColor(lowColor);
    this.makeRamp(this.nSwatches);
    this.rampSwatches();
    this.repaint();
  }

  public Color getLowColor() {
    return colors[0];
  }

  public void setHighColor(Color highColor) {
    this.colors[colors.length - 1] = highColor;
    this.makeRamp(this.nSwatches);
    this.rampSwatches();
    this.repaint();
    this.fireActionPerformed(ColorRampPicker.COMMAND_SWATCH_COLOR_CHANGED);
  }

  public Color getHighColor() {
    return this.colors[colors.length - 1];
  }

  public void setNSwatches(int nSwatches) {
    this.nSwatches = nSwatches;
    this.makeRamp(nSwatches);
    this.rampSwatches();
    this.validate();
    this.repaint();
  }

  public int getNSwatches() {
    return this.nSwatches;
  }

  /**
   * implements ActionListener
   */
  public void addActionListener(ActionListener l) {
    listenerList.add(ActionListener.class, l);
  }

  /**
   * removes an ActionListener from the component
   */
  public void removeActionListener(ActionListener l) {
    listenerList.remove(ActionListener.class, l);
  }

  /**
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  private void fireActionPerformed(String command) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    ActionEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ActionListener.class) {
        // Lazily create the event:
        if (e == null) {
          e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
        }
        ( (ActionListener) listeners[i + 1]).actionPerformed(e);
      }
    }
  }


  /**
   * Following methods are added by Diansheng, July 16th, 2003.
   * ***/
  public Color[] getInterpolatedColorsOfAnyNumber(Color lowColor,
                                                  Color highColor,
                                                  int numberOfColors) {
    for (int i = 1; i < this.colors.length - 1; i++)
      this.anchored[i] = false;

    this.setHighColor(highColor);
    this.setLowColor(lowColor);
    this.setNSwatches(numberOfColors);

    return this.getColors();
  }

  public void setAnchoredColor(Color acolor, int pos) {
    if (pos < 0 || pos >= this.nSwatches)
      return;

    this.colors[pos] = acolor;
    this.anchored[pos] = true;
    this.panSet[pos].setAnchored(true);
    this.panSet[pos].setSwatchColor(acolor);

    this.swatchChanged();

    this.repaint();
  }

  public Color[] getRampColorsOfAnyNumber(int numberOfColors) {
    for (int i = 1; i < this.colors.length - 1; i++)
      this.anchored[i] = false;
    this.setHighColor(Color.green);
    this.setLowColor(Color.blue);
    this.setNSwatches(numberOfColors);
    this.setAnchoredColor(Color.red, numberOfColors / 2);

    return this.getColors();
  }

}
