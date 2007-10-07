/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class SonicClassifier
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: SonicClassifier.java,v 1.9 2005/05/31 17:42:53 hardisty Exp $
 $Date: 2005/05/31 17:42:53 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *

 */


package edu.psu.geovista.sound;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import edu.psu.geovista.common.color.Pallet;
import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.common.event.ColorArrayEvent;
import edu.psu.geovista.common.event.ColorArrayListener;
import edu.psu.geovista.common.event.DataSetEvent;
import edu.psu.geovista.common.event.DataSetListener;
import edu.psu.geovista.common.event.IndicationEvent;
import edu.psu.geovista.common.event.IndicationListener;
import edu.psu.geovista.common.event.PalletEvent;
import edu.psu.geovista.common.event.PalletListener;
import geovista.common.classification.Classifier;
import geovista.common.classification.ClassifierPicker;


public class SonicClassifier extends JPanel implements ActionListener, ComponentListener,
                                                        PalletListener, IndicationListener,
                                                        DataSetListener{
	protected final static Logger logger = Logger.getLogger(SonicClassifier.class.getName());

    private SonicRampPicker sonificationPanel;
    //XXX in future, we want to support much beyond colors.
    private transient Color[] colors;
    private transient Color[] dataColors;
    public transient boolean[] anchored;

    private transient int[] classificationIndex;


     private int nClasses;
     private boolean update;
     private boolean interpolate;
     private boolean setupFinished;

    private transient JCheckBox updateBox;
    private transient JCheckBox interpolateBox;
    public static final String COMMAND_COLORS_CHANGED = "colors";
    public static final String COMMAND_BEAN_REGISTERED = "hi!";

    private int currOrientation = SonicClassifier.X_AXIS;
    public static final int X_AXIS = 0;
    public static final int Y_AXIS = 1;
    public static final int DEFAULT_N_CLASSES = 8;
    public transient boolean orientationInParentIsX = false;



    private ClassifierPicker classPick;

    private transient TexturePaint texPaint;
    private transient int oldIndication = 0;
    private transient int[] currClassification;


    public SonicClassifier() {
        super();
        this.addComponentListener(this);

        this.setupFinished = false;
        this.nClasses = SonicRampPicker.DEFAULT_NUM_SWATCHES;

        this.update = true;
        this.interpolate = true;

        this.classPick = new ClassifierPicker();

        classPick.addActionListener(this);

        this.makeSymbolizationPanel();
        this.classPick.setNClasses(this.nClasses);

        this.add(classPick);
        //this.add(classificationPanel);
        this.add(sonificationPanel);
        this.setupFinished = true;
        this.makeColors();
        setVariableChooserMode(ClassifierPicker.VARIABLE_CHOOSER_MODE_ACTIVE);
        this.setPreferredSize(new Dimension(300,40));
        /*
        int prefHeight = (int)this.classPick.getPreferredSize().getHeight();
        prefHeight = prefHeight + (int)this.sonificationPanel.getPreferredSize().getHeight();
        int prefWidth = (int)this.classPick.getPreferredSize().getWidth();
        if (prefWidth > (int)this.sonificationPanel.getPreferredSize().getWidth()) {
          prefWidth = (int)this.sonificationPanel.getPreferredSize().getWidth(); //take the smaller one
        }
        this.setPreferredSize(new Dimension(prefWidth, prefHeight));
        */
        this.revalidate();
        this.makeTexPaint();
    }

    private void makeTexPaint(){

      int texSize = 4;
      Rectangle2D.Float rect = new Rectangle2D.Float(0,0,texSize,texSize);
      BufferedImage buff = new BufferedImage(texSize,texSize,BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2 = buff.createGraphics();
      Color trans = new Color(0,0,0,0); //clear black
      g2.setColor(trans);
      g2.fillRect(0,0,texSize,texSize);
      g2.setColor(Color.blue);
      g2.drawLine(0,0,32,32);

      texPaint = new TexturePaint(buff,rect);

    }
    private void makeSymbolizationPanel() {
      sonificationPanel = new SonicRampPicker();
      this.add(sonificationPanel);
      sonificationPanel.addActionListener(this);
    }

    private void makeColors(){
      Color[] pickerColors = this.sonificationPanel.getColors();
      boolean[] pickerAnchors = this.sonificationPanel.getAnchored();
      //now we interpolate...linearly
      int nPicks = pickerColors.length;
      double picksPerColor = (double)nPicks/(double)this.nClasses;
      if (this.colors == null){
        this.colors = new Color[nClasses];
      } else if (this.colors.length != nClasses) {
        this.colors = new Color[nClasses];
      }

      for (int i = 0; i < nClasses; i++) {
          double whichColor = (double)i * picksPerColor;
          int index = (int)Math.floor(whichColor);
          logger.finest("i = " + i + "index  = " + index);
          this.colors[i] = pickerColors[index];

      }
      if (this.interpolate) {
        //for each lock in the picker, find the class that is closest.
        double colorsPerPick = (double)(this.nClasses-1)/(double)(nPicks-1);
        //int[] newAnchors = new int[pickerAnchors.length];
        Vector newAnchors = new Vector();
        for (int i = 0; i < pickerAnchors.length; i++) {
            double whichClass = (double)i * colorsPerPick;
            int aClass = (int)Math.round(whichClass);
            if (pickerAnchors[i]){
              Integer Ind = new Integer(aClass);
              newAnchors.add(Ind);
            }
        }

        boolean[] colorAnchors = new boolean[nClasses];
        if (newAnchors.size() > 2) {
          for (Enumeration e = newAnchors.elements() ; e.hasMoreElements() ;) {
           Integer ind = (Integer)e.nextElement();
           colorAnchors[ind.intValue()] = true;
          }

          colorAnchors[0] = true;
          colorAnchors[colorAnchors.length -1] = true;
        } else if (newAnchors.size() == 2) {
          colorAnchors[0] = true;
          colorAnchors[colorAnchors.length -1] = true;
        } else if (newAnchors.size() == 1) {
          colorAnchors[0] = true;
        }
        //now find those durn colors!
        this.sonificationPanel.getRamp().rampColors(this.colors,colorAnchors);

      }//end if interpolate
    }
    public Color[] findDataColors(){//side effect: finds and records classification
      if (this.classPick.getDataSet() == null){
        return null;
      }
      int currVar = this.classPick.getCurrVariableIndex();
      Classifier classer = this.classPick.getClasser();
      double[] data = this.classPick.getDataSet().getNumericDataAsDouble(currVar);
      int[] classedData = classer.classify(data,classPick.getNClasses());
      this.currClassification = classedData;
      if (this.dataColors == null || this.dataColors.length != classedData.length){
        this.dataColors = new Color[classedData.length];
      }
      this.colors = this.sonificationPanel.getColors();
      for (int i = 0; i < classedData.length; i++){
        int aClass = classedData[i];
        if (aClass < 0) aClass = 0;//XXX egregious hack
        Color c = this.colors[aClass];
        this.dataColors[i] = c;
      }
      return this.dataColors;
    }


    /** Listens to the check boxen. */
    class CheckBoxListener implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
          if (e.getSource().equals(SonicClassifier.this.updateBox)){
            if (e.getStateChange() == ItemEvent.SELECTED && SonicClassifier.this.setupFinished){
                SonicClassifier.this.update = true;
                // hack alertXXX combine these next two lines
                 SonicClassifier.this.findDataColors();
                SonicClassifier.this.makeColors();

                SonicClassifier.this.fireActionPerformed(SonicClassifier.COMMAND_COLORS_CHANGED);
                
                SonicClassifier.this.fireColorArrayChanged();

            } else if (e.getStateChange() == ItemEvent.DESELECTED){

                SonicClassifier.this.update = false;
            }
          } else if (e.getSource().equals(SonicClassifier.this.interpolateBox)){
            if (e.getStateChange() == ItemEvent.SELECTED && SonicClassifier.this.setupFinished){
                SonicClassifier.this.interpolate = true;
                // hack alertXXX combine these next two lines
                 SonicClassifier.this.findDataColors();
                SonicClassifier.this.makeColors();
                SonicClassifier.this.fireActionPerformed(SonicClassifier.COMMAND_COLORS_CHANGED);
               
                SonicClassifier.this.fireColorArrayChanged();

            } else if (e.getStateChange() == ItemEvent.DESELECTED){
                SonicClassifier.this.interpolate = false;
                // hack alertXXX combine these next two lines
                 SonicClassifier.this.findDataColors();
                SonicClassifier.this.makeColors();
                SonicClassifier.this.fireActionPerformed(SonicClassifier.COMMAND_COLORS_CHANGED);
               
                SonicClassifier.this.fireColorArrayChanged();
            }
          }
        }
    }
    class SliderListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider)e.getSource();
            //if (!source.getValueIsAdjusting()) {

                int fps = (int)source.getValue();
                if (fps == 0) {
                    //if (!frozen) stopAnimation();
                } else {
                  SonicClassifier.this.sonificationPanel.setNSwatches(fps);
                  SonicClassifier.this.nClasses = fps;
                  // hack alertXXX combine these next two lines
                 SonicClassifier.this.findDataColors();
                  SonicClassifier.this.makeColors();
                  SonicClassifier.this.fireActionPerformed(SonicClassifier.COMMAND_COLORS_CHANGED);
                  
                  SonicClassifier.this.fireColorArrayChanged();
                }
            //}
        }
    }
  public SonicRampPicker getSymbolizationPanel() {
    return sonificationPanel;
  }
  public void setSymbolizationPanel(SonicRampPicker sonificationPanel) {
    this.sonificationPanel = sonificationPanel;
    this.makeColors();
    logger.finest("Sonic Classifier, settingSymbolizationPanel");
  }

  public void playObservationKey(int obs){
    if (obs < 0){
      this.getSymbolizationPanel().playKey(obs); //could be null
      return;
    }
      if (this.currClassification == null){
        this.findDataColors();
      } else if (this.currClassification == null){
        return;
      } else if (obs >= this.currClassification.length){
        return;
      }
      int aClass = this.currClassification[obs];
      logger.finest("aClass = " + aClass);
      if (aClass < 0){
          return;//null class, maybe play special sound?
      }
      this.getSymbolizationPanel().playKey(aClass);

    }
    public void indicationChanged (IndicationEvent e){
      int ind = e.getIndication();
      logger.finest("indication = " + ind);
      if (ind >= 0){
        this.playObservationKey(ind);
      }

    }
    public void dataSetChanged(DataSetEvent e){
      this.setData(e.getDataSet());
    }


    public Color[] getColorForObservations(){
      return this.findDataColors();
    }
    public Color[] getColors() {
      logger.finest("VC, gettingColors");
      this.colors = this.sonificationPanel.getColors();
     return this.colors;
    }

    public void setColors(Color[] colors) {
      this.colors = colors;
    }

    public void palletChanged(PalletEvent e){
      Pallet pal = e.getPallet();
      int maxColors = pal.getRecommendedMaxLength();
      logger.finest("max colors = " + maxColors);

      //we can't go over the max, or it blows null exceptions
      //so, we interpolate....

      int numClasses = this.classPick.getNClasses();
      Color[] cols = null;
      if (numClasses <= maxColors){
          cols = pal.getColors(numClasses);
      } else {
          //we have more colors wanted than the pallet can give us
          //todo: make this work
          //this.assignColors
          cols = pal.getColors(numClasses);
      }


      this.setColors(cols);
      this.sonificationPanel.setColors(cols);
      this.revalidate();
      this.repaint();
    }

    public void actionPerformed(ActionEvent e) {
      String command = e.getActionCommand();
      if (command == SonicRampPicker.COMMAND_SWATCH_COLOR_CHANGED){
        // hack alertXXX combine these next two lines
                 this.findDataColors();
        this.makeColors();
        this.fireActionPerformed(SonicClassifier.COMMAND_COLORS_CHANGED);
       
        SonicClassifier.this.fireColorArrayChanged();
      } else if (command == ClassifierPicker.COMMAND_CLASSES_CHANGED) {
        int nClasses = this.classPick.getNClasses();
        this.sonificationPanel.setNSwatches(nClasses);
        this.nClasses = nClasses;
        // hack alertXXX combine these next two lines
        this.findDataColors();
        this.makeColors();
        this.fireActionPerformed(SonicClassifier.COMMAND_COLORS_CHANGED);
     
        SonicClassifier.this.fireColorArrayChanged();
      } else if (command == ClassifierPicker.COMMAND_SELECTED_VARIABLE_CHANGED) {

        this.fireActionPerformed(ClassifierPicker.COMMAND_SELECTED_VARIABLE_CHANGED);
        // hack alertXXX combine these next two lines
                 this.findDataColors();
        this.fireColorArrayChanged();
      } else if (command == ClassifierPicker.COMMAND_SELECTED_CLASSIFIER_CHANGED) {
        //this.colorClasser.setColorer(this.colorerLinear);
 
      
        // hack alertXXX combine these next two lines
                 SonicClassifier.this.findDataColors();
        SonicClassifier.this.fireColorArrayChanged();
      }

      //need to pass this along, if we are a FoldupPanel
      //super.actionPerformed(e);
    }
     //start component event handling
     //note: this class only listens to itself
    public void componentHidden(ComponentEvent e) {}

    public void componentMoved(ComponentEvent e) {}

    public void componentShown(ComponentEvent e) {}

    public void componentResized(ComponentEvent e) {
      double pickPrefWidth = this.classPick.getPreferredSize().getWidth();

      int prefWidth = (int)(pickPrefWidth * 1.5);
      if (this.getWidth() >= prefWidth) {
        this.changeOrientation(SonicClassifier.X_AXIS);
      } else {
        this.changeOrientation(SonicClassifier.Y_AXIS);
      }

    }
    //end component event handling

    public void changeOrientation(int orientation){
      if (orientation == this.currOrientation) {
        return;
      } else if (orientation == SonicClassifier.X_AXIS) {
        Component[] comps = new Component[this.getComponentCount()];
        for (int i = 0; i < this.getComponentCount(); i++) {
          comps[i] = this.getComponent(i);
        }
        this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        for (int i = 0; i < this.getComponentCount(); i++) {
          this.add(comps[i]);
        }
        this.currOrientation = SonicClassifier.X_AXIS;
        this.setPreferredSize(new Dimension(300,20));
        this.revalidate();
      } else if (orientation == SonicClassifier.Y_AXIS) {
        Component[] comps = new Component[this.getComponentCount()];
        for (int i = 0; i < this.getComponentCount(); i++) {
          comps[i] = this.getComponent(i);
        }
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        for (int i = 0; i < this.getComponentCount(); i++) {
          this.add(comps[i]);
        }
        this.currOrientation = SonicClassifier.Y_AXIS;
        this.setPreferredSize(new Dimension(300,40));
        this.revalidate();
      }

    }

//    /**
//* implements ColorArrayListener
//*/
//public void addColorArrayListener(ColorArrayListener l) {
//    listenerList.add(ColorArrayListener.class, l);
//    //this.fireColorArrayChanged(); //so that if any class registers
//
//    //it gets an event
//}
//
///**
// * removes an ColorArrayListener from the component
// */
//public void removeColorArrayListener(ColorArrayListener l) {
//    listenerList.remove(ColorArrayListener.class, l);
//}

/**
 * Notify all listeners that have registered interest for
 * notification on this event type. The event instance
 * is lazily created using the parameters passed into
 * the fire method.
 * @see EventListenerList
 */
private void fireColorArrayChanged() {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    ColorArrayEvent e = null;

    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
        if (listeners[i] == ColorArrayListener.class) {
            // Lazily create the event:
            if (e == null) {
                e = new ColorArrayEvent(this, this.findDataColors());
            }

            ((ColorArrayListener) listeners[i + 1]).colorArrayChanged(e);
        }
    } //next i
}

    /**
     * implements ActionListener
     */
        public void addActionListener (ActionListener l) {
                listenerList.add(ActionListener.class, l);
                this.fireActionPerformed(SonicClassifier.COMMAND_BEAN_REGISTERED);
        }

    /**
     * removes an ActionListener from the component
     */
        public void removeActionListener (ActionListener l) {
                listenerList.remove(ActionListener.class, l);
        }
    /**
     * Notify all listeners that have registered interest for
     * notification on this event type. The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
        private void fireActionPerformed (String command) {
          if (update) {
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
                                        ((ActionListener)listeners[i + 1]).actionPerformed(e);
                                }
                        }//next i
          }//end if
        }

  public void setIndicatedClass(int indicClass) {
          //clear old indication
          //if it still exists
      if (this.oldIndication < this.sonificationPanel.getPanSet().length) {
        this.sonificationPanel.getPanSet()[this.oldIndication].setTexPaint(null);
      }

      if (indicClass < 0) {   //used for null or out of range indication
        this.repaint(); //clear old indication
        return;
      }
      if (indicClass >= this.sonificationPanel.getPanSet().length) {
        this.repaint(); //clear old indication
        return;
      }

      this.sonificationPanel.getPanSet()[indicClass].setTexPaint(this.texPaint);
      this.oldIndication = indicClass;
      this.repaint();
  }
  public boolean[] getAnchored() {
    return anchored;
  }
  public void setAnchored(boolean[] anchored) {
    this.anchored = anchored;
  }
  public int[] getClassificationIndex() {
    return classificationIndex;
  }
  public void setClassificationIndex(int[] classificationIndex) {
    this.classificationIndex = classificationIndex;
  }
  public ClassifierPicker getClassPick() {
    return classPick;
  }
  public void setClassPick(ClassifierPicker classPick) {
    this.classPick = classPick;
  }

  public int getCurrOrientation() {
    return currOrientation;
  }
  public void setCurrOrientation(int currOrientation) {
    this.currOrientation = currOrientation;
  }
  public int getVariableChooserMode() {
    return this.classPick.getVariableChooserMode();
  }
  public void setVariableChooserMode(int variableChooserMode) {
    if (this.classPick.getVariableChooserMode() != variableChooserMode) {
      this.classPick.setVariableChooserMode(variableChooserMode);

    }
  }

  public void setCurrVariableIndex(int index){
    this.classPick.setCurrVariableIndex(index);
  }
  /**
   * @param dataIn
   * 
   * This method is deprecated becuase it wants to create its very own pet
   * DataSetForApps. This is no longer allowed. Please use setDataSet
   * instead.
   */
  @Deprecated
  public void setData(Object[] data) {
    //this.classPick.setData(data);
  }
  public void setDataSet(DataSetForApps data){
	  this.classPick.setDataSet(data);
	    // hack alertXXX combine these next two lines
      this.findDataColors();
this.fireColorArrayChanged();
  }

  public int getCurrVariableIndex() {
    return this.classPick.getCurrVariableIndex();
  }

  public void setHighColor(Color c){
    this.sonificationPanel.setHighColor(c);
  }
  public void setOrientationInParentIsX(boolean orientationInParentIsX) {
    this.orientationInParentIsX = orientationInParentIsX;
  }

}
