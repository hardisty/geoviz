/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class SelectionAnimator
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: SelectionAnimator.java,v 1.3 2005/02/19 02:44:41 hardisty Exp $
 $Date: 2005/02/19 02:44:41 $
 Reference:        Document no:
 ___                ___
 -------------------------------------------------------------------  *
 */
package edu.psu.geovista.animation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Vector;
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

import edu.psu.geovista.common.classification.ClassifierPicker;
import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.common.event.ClassificationEvent;
import edu.psu.geovista.common.event.ClassificationListener;
import edu.psu.geovista.common.event.DataSetEvent;
import edu.psu.geovista.common.event.DataSetListener;
import edu.psu.geovista.common.event.SelectionEvent;
import edu.psu.geovista.common.event.SelectionListener;
import edu.psu.geovista.common.event.SubspaceEvent;
import edu.psu.geovista.common.event.SubspaceListener;

/**
 * SelectionAnimator is used to send out indication signals that
 * corrispond to current classifications.
 *
 */
public class SelectionAnimator
    extends JPanel
    implements ActionListener,
    ChangeListener,
    DataSetListener,
    SubspaceListener,
    ClassificationListener {
  private  Timer ticker;
  private transient int currClassIndex;
  private  Vector selections;
  private transient JButton startStopButton;
  private transient JButton clearSelectionButton;
  private transient boolean going = false;
  private int speed; //in milliseconds
  private transient DataSetForApps data;
  private transient int maxClass = 0;
  private  ClassifierPicker classPick;
  private transient int[] classes;
  private transient ClassedObs[] obs;
  private transient int[] subspace;
  private transient int subspaceIndex;
  private transient JSlider timeSlider;
  private transient JCheckBox subspaceBox;
  private boolean usingSubspace;
  private transient int[] tempArray;
  final static Logger logger = Logger.getLogger(SelectionAnimator.class.getName());
  /**
   * null ctr
   */
  public SelectionAnimator() {
    this.usingSubspace = true;
    speed = 250;
    this.selections = new Vector();
    ticker = new Timer(speed, this);

    this.add(this.makeTopPanel());

    classPick = new ClassifierPicker();
    classPick.setVariableChooserMode(ClassifierPicker.VARIABLE_CHOOSER_MODE_ACTIVE);
    this.add(classPick);
    classPick.addClassificationListener(this);
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
    timeSlider = new JSlider(1, 10, 5);
    topPanel.add(timeSlider);
    timeSlider.addChangeListener(this);

    clearSelectionButton = new JButton("Clear Selection");
    topPanel.add(clearSelectionButton);
    clearSelectionButton.addActionListener(this);

    return topPanel;
  }

  public void stateChanged(ChangeEvent e) {
    if (e.getSource() == this.timeSlider &&
        !this.timeSlider.getValueIsAdjusting()) {
      this.speed = this.timeSlider.getValue() * 50;
      this.ticker.setDelay(speed);
    }
  }

  private void iterateSelections() { //main loop

		if (logger.isLoggable(Level.FINEST)){
			logger.finest("speed = " + speed);
			logger.finest("index = " + obs[currClassIndex].index);			
		}


    if (currClassIndex <= this.maxClass && this.selections.size()>this.currClassIndex ) { //go up one
      int[] whichClass = (int[])this.selections.get(this.currClassIndex);
      this.fireSelectionChanged(whichClass);

      this.ticker.setDelay(this.speed);
      currClassIndex++;
    }
    else { //go back to zero
      this.ticker.setDelay(this.speed * 10);

      currClassIndex = 0; //reset
      if (this.usingSubspace) {
        this.iterateSubspace();
      }
    }
  }

  public void actionPerformed(ActionEvent e) {
    if (this.data == null) {
      //without data, we don't do anything
      return;
    }
    if (e.getSource() == this.ticker) {
      this.iterateSelections();
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
    else if (e.getSource() == this.clearSelectionButton) {
      this.fireSelectionChanged(new int[0]);
    }
  }

  private void reclassObs() {
    if (this.data == null) {
      return;
    }
    Arrays.sort(obs);
    this.selections.removeAllElements();

    maxClass = obs[obs.length - 1].classed;
    int classCounter = 0;
    int prevClass = -1;
    for (int i = 0; i < obs.length; i++) {
      int currClass = obs[i].classed;
      if (currClass != prevClass) {
        //new class
        classCounter = 0;
        prevClass = currClass;
        tempArray[classCounter] = obs[i].index;
      }
      else {
        classCounter++;
        tempArray[classCounter] = obs[i].index;

      }
      //peek ahead for either last obs or change in class
      if ( (i == obs.length - 1) || (obs[i + 1].classed != currClass)) {
        int[] thisClass = new int[classCounter + 1];
        //copy temp array into thisClass;
        for (int j = 0; j <= classCounter; j++) {
          thisClass[j] = tempArray[j];
        }
        //put thisClass into vector
        this.selections.add(thisClass);

      }
    }
  }

  private void iterateSubspace() {
	 

    if (this.subspaceIndex + 1 >= subspace.length) {
      this.subspaceIndex = 0;
    }
    else {
      this.subspaceIndex++;
    }

    int currVar = subspace[this.subspaceIndex]; //
	if (logger.isLoggable(Level.FINEST)){
		logger.finest("subspace.length = " + subspace.length);
		logger.finest("subspaceIndex = " + subspaceIndex);
		logger.finest("currVar = " + currVar);

	}

    this.classPick.setCurrVariableIndex(currVar);
  }

  public void subspaceChanged(SubspaceEvent e) {
    this.subspace = e.getSubspace();
    iterateSubspace();
  }

  public void dataSetChanged(DataSetEvent e) {

    this.data = e.getDataSetForApps();

    this.classPick.setDataSet(e.getDataSetForApps());
    tempArray = new int[data.getNumObservations()];

    this.obs = new ClassedObs[this.data.getNumObservations()];
    for (int i = 0; i < this.obs.length; i++) {
      this.obs[i] = new ClassedObs();
      this.obs[i].index = i;
    }
    this.classPick.fireClassificationChanged();
    this.subspace = new int[data.getNumberNumericAttributes()];
    for (int i = 0; i < subspace.length; i++) {
      subspace[i] = i;// + 1; //oh, the agony
    }
  }

  public void classificationChanged(ClassificationEvent e) {
    this.classes = e.getClassification();
    for (int i = 0; i < this.obs.length; i++) {
      int index = this.obs[i].index;
      this.obs[i].classed = this.classes[index];
    }

    this.reclassObs();
  }

  /**
   * adds an SelectionListener
   */
  public void addSelectionListener(SelectionListener l) {
    listenerList.add(SelectionListener.class, l);
  }

  /**
   * removes an SelectionListener from the component
   */
  public void removeSelectionListener(SelectionListener l) {
    listenerList.remove(SelectionListener.class, l);
  }

  /**
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  private void fireSelectionChanged(int[] newSelection) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    SelectionEvent e = null;

    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == SelectionListener.class) {
        // Lazily create the event:
        if (e == null) {
          e = new SelectionEvent(this, newSelection);
        }

        ( (SelectionListener) listeners[i + 1]).selectionChanged(e);
      }
    }

    //next i
  }

  public static void main(String[] args) {
    SelectionAnimator inAnim = new SelectionAnimator();


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

    //we compare by classed
    public int compareTo(Object o) {
      ClassedObs e = (ClassedObs) o;
      int val = 0;
      if (Double.isNaN(e.classed)) {
        if (Double.isNaN(this.classed)) {
          return 0;
        }
        else {
          return 1;
        }
      } //end if the other classed is NaN

      if (Double.isNaN(this.classed)) {
        val = -1; //everything is bigger than NaN
      }
      else if (this.classed < e.classed) {
        val = -1;
      }
      else if (this.classed > e.classed) {
        val = 1;
      }

      return val;
    }
  }
}
