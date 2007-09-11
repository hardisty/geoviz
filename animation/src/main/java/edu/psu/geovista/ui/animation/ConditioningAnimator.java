/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ConditioningAnimator
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: ConditioningAnimator.java,v 1.2 2003/07/10 00:24:56 hardisty Exp $
 $Date: 2003/07/10 00:24:56 $
 Reference:        Document no:
 ___                ___
 -------------------------------------------------------------------  *
 */
package edu.psu.geovista.ui.animation;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import edu.psu.geovista.data.geog.DataSetForApps;
import edu.psu.geovista.ui.event.ConditioningEvent;
import edu.psu.geovista.ui.event.ConditioningListener;
import edu.psu.geovista.ui.event.DataSetEvent;
import edu.psu.geovista.ui.event.DataSetListener;
import edu.psu.geovista.ui.event.SubspaceEvent;
import edu.psu.geovista.ui.event.SubspaceListener;


/**
 * ConditioningAnimator is used to send out indication signals that
 * corrispond to current classifications.
 *
 */
public class ConditioningAnimator extends JPanel implements ActionListener,
                                                          ChangeListener,
                                                          DataSetListener,
                                                          SubspaceListener
                                                       {
  private  Timer ticker;
  private transient int currClassIndex;
  private transient int lowCondIndex;
  private transient int highCondIndex;
  private transient int[] currConditioning;

  private transient JButton startStopButton;
  private transient boolean going = false;
  private  int speed; //in milliseconds
  private transient DataSetForApps data;
  private transient ClassedObs[] obs;
  private transient int[] subspace;
  private transient int subspaceIndex;
  private transient JSlider timeSlider;

  private transient JComboBox varCombo;
  private transient boolean varComboIsAdjusting;

  private transient JRadioButton subspaceButton;
  private transient JRadioButton oneVarButton;
  private  boolean usingSubspace;
  
  final static Logger logger = Logger.getLogger(ConditioningAnimator.class.getName());
  /**
  * null ctr
  */
  public ConditioningAnimator() {
    this.usingSubspace = true;
    speed = 250;
    ticker = new Timer(speed, this);

    this.add(this.makeTopPanel());


    this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

  }
  private JPanel makeTopPanel(){
    JPanel topPanel = new JPanel();
    startStopButton = new JButton("Start");
    topPanel.add(startStopButton);
    startStopButton.addActionListener(this);

    this.varCombo = new JComboBox();
    varCombo.addActionListener(this);
    topPanel.add(varCombo);


    this.subspaceButton = new JRadioButton("Iterate over subspace");
    this.oneVarButton = new JRadioButton("Iterate over one variable");
    this.subspaceButton.setSelected(true);
    this.subspaceButton.addActionListener(this);
    this.oneVarButton.addActionListener(this);

    ButtonGroup buttGroup = new ButtonGroup();
    buttGroup.add(subspaceButton);
    buttGroup.add(oneVarButton);

    JPanel buttonHolder = new JPanel();
    BoxLayout boxLayout = new BoxLayout(buttonHolder,BoxLayout.Y_AXIS);
    buttonHolder.setLayout(boxLayout);

    buttonHolder.add(subspaceButton);
    buttonHolder.add(oneVarButton);

    topPanel.add(buttonHolder);
    timeSlider = new JSlider(1,10,5);
    topPanel.add(timeSlider);
    timeSlider.addChangeListener(this);
    return  topPanel;
  }
  public void stateChanged(ChangeEvent e){
    if (e.getSource() == this.timeSlider && !this.timeSlider.getValueIsAdjusting()){
      this.speed = this.timeSlider.getValue() * 50;
      this.ticker.setDelay(speed);
    }
  }
  private void iterateConditionings(){//main loop

    if (this.highCondIndex < this.data.getNumObservations()-1) { //go up one
     int index = this.obs[this.lowCondIndex].index;
     this.currConditioning[index] = 0;
     this.lowCondIndex++;
     this.highCondIndex++;
     index = this.obs[this.highCondIndex].index;
     this.currConditioning[index] = -1;
     this.fireConditioningChanged(this.currConditioning);

     this.ticker.setDelay(this.speed);
     currClassIndex++;
    } else {//go back to zero
      this.ticker.setDelay(this.speed * 10);

      currClassIndex = 0; //reset
      if (this.usingSubspace){

         int currVar = this.iterateSubspace();

         this.instantiateCurrentVariable(currVar);
         this.conditionOutLowerRange();
         this.varComboIsAdjusting = true;
         this.varCombo.setSelectedIndex(currVar);
         this.varComboIsAdjusting = false;
      }
    }
  }
  public void actionPerformed(ActionEvent e) {
    if (this.data == null){
      //without data, we don't do anything
      return;
    }
    if (e.getSource() == this.ticker) {
      this.iterateConditionings();
    } else if (e.getSource() == this.startStopButton) {
      if (going) {
        this.going = false; //turn off
        this.ticker.stop();
        this.startStopButton.setText("Start");
      } else {
        this.going = true; //turn on
        this.ticker.start();
        this.startStopButton.setText("Stop");
      }
    } else if (e.getSource() == this.subspaceButton){
        this.usingSubspace = true;
    } else if (e.getSource() == this.oneVarButton){
        this.usingSubspace = false;
    } else if (e.getSource() == this.varCombo){
        if (this.obs != null && this.varComboIsAdjusting == false){ //might be null if we are getting started
          int currVar = this.varCombo.getSelectedIndex();
          this.instantiateCurrentVariable(currVar);
          this.conditionOutLowerRange();
        }

    }
  }


  private int iterateSubspace(){
	if (logger.isLoggable(Level.FINEST)){
		logger.finest("iterating subspace");
	}

    if (this.subspaceIndex >= subspace.length-1){
      this.subspaceIndex = 0;
    } else{
      this.subspaceIndex++;
    }
	if (logger.isLoggable(Level.FINEST)){
		logger.finest("subspaceIndex = " + subspaceIndex);
	}

    int currVar = subspace[this.subspaceIndex]+1;//evil strikes again
    return currVar;

  }
  private void instantiateCurrentVariable(int currVar){

    double[] values = this.data.getNumericDataAsDouble(currVar);
    for (int i = 0; i < obs.length; i++){
      obs[i].index = i;
      obs[i].value = values[i];
    }
    Arrays.sort(obs);
  }
  private void conditionOutLowerRange(){

    for (int i = 0; i < this.currConditioning.length; i++){
      currConditioning[i] = 0;//default == activated
    }
    int numObs = this.data.getNumObservations();
    int fifth = numObs/5;
    for (int i = 0; i < fifth; i++){
      int index = obs[i].index;
      currConditioning[index] = -1;
    }
    this.lowCondIndex = 0;
    this.highCondIndex = fifth -1;

  }
  public void subspaceChanged (SubspaceEvent e){
    this.subspace = e.getSubspace();
    this.subspaceIndex = 0;
    int currVar = subspace[subspaceIndex];
    this.instantiateCurrentVariable(currVar);
    this.conditionOutLowerRange();
    this.varComboIsAdjusting = true;
    this.varCombo.setSelectedIndex(currVar);
    this.varComboIsAdjusting = false;

  }
  public void dataSetChanged(DataSetEvent e) {
    this.data = e.getDataSetForApps();
    this.currConditioning =new int[data.getNumObservations()];
    String[] numericVarNames = data.getAttributeNamesNumeric();
    for (int i = 0; i < numericVarNames.length; i++){
      this.varCombo.addItem(numericVarNames[i]);
    }
    //tempArray = new int[data.getNumObservations()];

    this.obs = new ClassedObs[this.data.getNumObservations()];
    for (int i = 0; i < obs.length; i++){
      obs[i] = new ClassedObs();
    }
    this.subspace = new int[data.getNumberNumericAttributes()];
    for (int i = 0; i < subspace.length; i++){
      subspace[i] = i;
    }
    this.subspaceIndex = 0;
    this.instantiateCurrentVariable(this.subspace[subspaceIndex]);
    this.conditionOutLowerRange();
  }





  /**
  * adds an ConditioningListener
  */
  public void addConditioningListener(ConditioningListener l) {
    listenerList.add(ConditioningListener.class, l);
  }

  /**
   * removes an ConditioningListener from the component
   */
  public void removeConditioningListener(ConditioningListener l) {
    listenerList.remove(ConditioningListener.class, l);
  }

  /**
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  private void fireConditioningChanged(int[] newConditioning) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    ConditioningEvent e = null;

    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ConditioningListener.class) {
        // Lazily create the event:
        if (e == null) {
          e = new ConditioningEvent(this, newConditioning);
        }

        ((ConditioningListener) listeners[i + 1]).conditioningChanged(e);
      }
    }

    //next i
  }

  public static void main(String[] args) {
    ConditioningAnimator inAnim = new ConditioningAnimator();

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

  private class ClassedObs implements Comparable {
    int index;

    double value;

    //we compare by value
    public int compareTo(Object o) {
      ClassedObs e = (ClassedObs) o;
      int val = 0;
      if (Double.isNaN(e.value)){
        if (Double.isNaN(this.value)){
          return 0;
        } else {
          return 1;
        }
      }//end if the other value is NaN

      if (Double.isNaN(this.value)){
        val = -1;//everything is bigger than NaN
      } else if (this.value < e.value) {
        val = -1;
      } else if (this.value > e.value) {
        val = 1;
      }

      return val;
    }
  }
}