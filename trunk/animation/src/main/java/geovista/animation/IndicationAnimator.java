/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class IndicationAnimator
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: IndicationAnimator.java,v 1.5 2005/03/28 14:57:01 hardisty Exp $
 $Date: 2005/03/28 14:57:01 $
 Reference:        Document no:
 ___                ___
 -------------------------------------------------------------------  *
 */
package geovista.animation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import geovista.common.classification.ClassifierPicker;
import geovista.common.data.DataSetForApps;
import geovista.common.event.ClassificationEvent;
import geovista.common.event.ClassificationListener;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.DimensionEvent;
import geovista.common.event.DimensionListener;
import geovista.common.event.IndicationEvent;
import geovista.common.event.IndicationListener;
import geovista.common.event.SubspaceEvent;
import geovista.common.event.SubspaceListener;

/**
 * IndicationAnimator is used to send out indication signals that
 * corrispond to current classifications.
 *
 */
public class IndicationAnimator
    extends JPanel
    implements ActionListener,
    ChangeListener,
    DataSetListener,
    SubspaceListener,
    ClassificationListener {
  private  transient Timer ticker;
  private transient int currObs;
  private transient JButton startStopButton;
  private transient boolean going = false;
  private  transient int fps; 
  private transient int delay;//in milliseconds
  static final int FPS_MIN = 0;
  static final int FPS_MAX = 30;
  static final int FPS_INIT = 15;    //initial frames per second
  private transient DataSetForApps data;
  private transient int maxIndication = 0;
  private  transient ClassifierPicker classPick;
  private transient int[] classes;
  private transient double[] values;
  private transient ClassedObs[] obs;
  private transient int[] subspace;
  private transient int subspaceIndex;
  private transient JSlider timeSlider;
  private transient JCheckBox subspaceBox;
  private transient boolean usingSubspace;
  private transient String[] varNames;
  final transient static Logger logger = Logger.getLogger(IndicationAnimator.class.getName());
  /**
   * null ctr
   */
  public IndicationAnimator() {
    usingSubspace = true;
    fps = FPS_INIT;

    ticker = new Timer(fps, this);

    this.add(this.makeTopPanel());

    classPick = new ClassifierPicker();
    classPick.setVariableChooserMode(ClassifierPicker.VARIABLE_CHOOSER_MODE_ACTIVE);
    this.add(classPick);
    classPick.addClassificationListener(this);
    classPick.addActionListener(this);
    classPick.setBorder(new LineBorder(Color.white));
    this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

  }

  private JPanel makeTopPanel() {
    JPanel topPanel = new JPanel();
    startStopButton = new JButton("Start");
    topPanel.add(startStopButton);
    startStopButton.addActionListener(this);
    this.subspaceBox = new JCheckBox("Subspace?", true);
    this.subspaceBox.addActionListener(this);
    topPanel.add(subspaceBox);
    timeSlider = new JSlider(FPS_MIN, FPS_MAX, FPS_INIT);
    timeSlider.setMajorTickSpacing(20);
    timeSlider.setPaintLabels(true);
    topPanel.add(timeSlider);
    timeSlider.addChangeListener(this);
    return topPanel;
  }

  public void stateChanged(ChangeEvent e) {
	  JSlider source = (JSlider)e.getSource();
    if (e.getSource() == this.timeSlider &&
        !source.getValueIsAdjusting()) {
        int fps = (int)source.getValue();
        if (fps == 0) {
            //if (!frozen) stopAnimation();
        } else{

        	delay = 1000 / fps;
        	ticker.setDelay(delay);
        }
    	if(logger.isLoggable(Level.FINEST)){
    		logger.finest("delay = " + delay);
    	}
    }
  }

  private void iterateObs() { //main loop
    int whichObs = this.obs[currObs].index;
    this.fireIndicationChanged(whichObs);

	if (logger.isLoggable(Level.FINEST)&& currObs == 0){
		logger.finest("zero obs index = " + obs[currObs].index + ", value = " + obs[currObs].value);
	}

    if (currObs < this.maxIndication) { //go up one
    	this.ticker.setDelay(delay);
    	currObs++;
    }
    else {
      this.ticker.setDelay(this.delay * 10);
      if (this.usingSubspace) {
    		if (logger.isLoggable(Level.FINEST)){
    			logger.finest("new var!!!");
    		}

        this.iterateSubspace();
      }
      currObs = 0; //reset
    }
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == this.ticker) {
      this.iterateObs();
    }
    else if (e.getSource() == this.startStopButton) {
      if (going) {
        this.going = false; //turn off
        this.ticker.stop();
        this.startStopButton.setText("Start");
      }
      else {
        this.going = true; //turn on
        this.ticker.start();
        this.startStopButton.setText("Stop");
      }
    }
    else if (e.getSource() == this.subspaceBox) {
      this.usingSubspace = this.subspaceBox.isSelected();
    }
    else if (e.getSource() == this.classPick &&
             e.getActionCommand().equals(ClassifierPicker.
                                         COMMAND_SELECTED_VARIABLE_CHANGED)) {
      //xxx hack for demo
      this.subspaceIndex = this.classPick.getCurrVariableIndex();

    }

  }

  private void reclassObs() {
    if (this.data == null) {
      return;
    }
    Arrays.sort(obs);
  }

  private void iterateSubspace() {

    if (this.subspaceIndex >= subspace.length - 1) {
      this.subspaceIndex = 0;
    }
    else {
      this.subspaceIndex++;
    }
    int currVar = subspace[this.subspaceIndex];
	if (logger.isLoggable(Level.FINEST)){
		logger.finest("iterating subspace");
		logger.finest("subspaceIndex = " + subspaceIndex);
		logger.finest("currVar = " + currVar);
	}
    this.classPick.setCurrVariableIndex(currVar);
    String varName = this.varNames[currVar];
    DimensionEvent dimEvent = new DimensionEvent(this, currVar, varName);
    this.fireDimensionChanged(dimEvent);
  }

  public void subspaceChanged(SubspaceEvent e) {
    this.subspace = e.getSubspace();
    iterateSubspace();
  }

  public void dataSetChanged(DataSetEvent e) {
    this.data = e.getDataSetForApps();
    this.maxIndication = this.data.getNumObservations() - 1;
    this.classPick.removeActionListener(this);
    this.classPick.setDataSet(this.data);
    this.classPick.addActionListener(this);

    this.obs = new ClassedObs[this.data.getNumObservations()];
    for (int i = 0; i < this.obs.length; i++) {
      this.obs[i] = new ClassedObs();
      this.obs[i].index = i;
    }
    this.classPick.fireClassificationChanged();
    this.subspace = new int[data.getNumberNumericAttributes()];
    this.varNames = data.getAttributeNamesNumeric();
    for (int i = 0; i < subspace.length; i++) {
      subspace[i] = i; //oh, the agony
    }
  }

  public void classificationChanged(ClassificationEvent e) {
    this.classes = e.getClassification();
    if (e.getSource() == this.classPick) {
      values = this.data.getNumericDataAsDouble(this.classPick.
                                                getCurrVariableIndex());
      for (int i = 0; i < this.obs.length; i++) {
        int index = this.obs[i].index;
        this.obs[i].classed = this.classes[index];
        double aVal = this.values[index];
        this.obs[i].value = aVal;
      }
    }
    else {
      for (int i = 0; i < this.obs.length; i++) {
        int index = this.obs[i].index;
        this.obs[i].classed = this.classes[index];
        this.obs[i].value = this.classes[index];
      }
    }

    this.reclassObs();
  }

  /**
   * adds an DimensionListener
   */
  public void addDimensionListener(DimensionListener l) {
    listenerList.add(DimensionListener.class, l);
  }

  /**
   * removes an DimensionListener from the component
   */
  public void removeDimensionListener(DimensionListener l) {
    listenerList.remove(DimensionListener.class, l);
  }

  /**
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  private void fireDimensionChanged(DimensionEvent e) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();

    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == DimensionListener.class) {

        ( (DimensionListener) listeners[i + 1]).dimensionChanged(e);
      }
    }

    //next i
  }

  /**
   * adds an IndicationListener
   */
  public void addIndicationListener(IndicationListener l) {
    listenerList.add(IndicationListener.class, l);
  }

  /**
   * removes an IndicationListener from the component
   */
  public void removeIndicationListener(IndicationListener l) {
    listenerList.remove(IndicationListener.class, l);
  }

  /**
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  private void fireIndicationChanged(int newIndication) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    IndicationEvent e = null;

    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == IndicationListener.class) {
        // Lazily create the event:
        if (e == null) {
          e = new IndicationEvent(this, newIndication);
        }

        ( (IndicationListener) listeners[i + 1]).indicationChanged(e);
      }
    }

    //next i
  }

  public static void main(String[] args) {
    IndicationAnimator inAnim = new IndicationAnimator();


    JFrame app = new JFrame();
    app.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    app.getContentPane().setLayout(new BorderLayout());

    app.getContentPane().add(inAnim);
    app.pack();
    app.setVisible(true);
  }

  private class ClassedObs
      implements Comparable {
    int index;
    int classed;
    double value;

    //we compare by value
    public int compareTo(Object o) {
      ClassedObs e = (ClassedObs) o;
      int val = 0;
      if (Double.isNaN(e.value)) {
        if (Double.isNaN(this.value)) {
          return 0;
        }
        else {
          return 1;
        }
      } //end if the other value is NaN

      if (Double.isNaN(this.value)) {
        val = -1; //everything is bigger than NaN
      }
      else if (this.value < e.value) {
        val = -1;
      }
      else if (this.value > e.value) {
        val = 1;
      }

      return val;
    }
  }
}
