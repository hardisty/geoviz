/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class VariableSequenceAnimator
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Xiping Dai
 $Author: xpdai $
 $Id: VariableSequenceAnimator.java,v 1.1 2004/07/28 15:25:33 xpdai Exp $
 $Date: 2004/07/28 15:25:33 $
 Reference:        Document no:
 ___                ___
 -------------------------------------------------------------------  *
 */
package edu.psu.geovista.animation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.common.event.DataSetEvent;
import edu.psu.geovista.common.event.DataSetListener;
import edu.psu.geovista.common.event.VariableSelectionEvent;
import edu.psu.geovista.common.event.VariableSelectionListener;


/**
 * VariableSequenceAnimator is used to send out variable index that
 * corrispond to current variable displayed.
 *
 */
public class VariableSequenceAnimator extends JPanel implements ActionListener,
                                                          ChangeListener,
                                                          DataSetListener
                                                       {
  private Timer ticker;

  private transient JButton startStopButton;
  private transient boolean going = false;
  private  int speed; //in milliseconds
  private transient DataSetForApps data;

  private transient ClassedObs[] obs;
  private transient JSlider timeSlider;

  private transient JComboBox varCombo;
  private transient boolean varComboIsAdjusting;

  private transient JRadioButton xVarButton;
  private transient JRadioButton yVarButton;
  private transient int currentVarIndex;
  /**
  * null ctr
  */
  public VariableSequenceAnimator() {
    speed = 1000;
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


    this.xVarButton = new JRadioButton("Iterate over X variable");
    this.yVarButton = new JRadioButton("Iterate over Y variable");
    this.xVarButton.setSelected(true);
    this.xVarButton.addActionListener(this);
    this.yVarButton.addActionListener(this);

    ButtonGroup buttGroup = new ButtonGroup();
    buttGroup.add(xVarButton);
    buttGroup.add(yVarButton);

    JPanel buttonHolder = new JPanel();
    BoxLayout boxLayout = new BoxLayout(buttonHolder,BoxLayout.Y_AXIS);
    buttonHolder.setLayout(boxLayout);

    buttonHolder.add(xVarButton);
    buttonHolder.add(yVarButton);

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

  private void iterateVariables(){
        if (this.currentVarIndex < this.data.getNumberNumericAttributes()) { //go up one
          //this.fireConditioningChanged(this.currConditioning);
          this.fireVariableSelectionChanged(this.currentVarIndex);
          this.ticker.setDelay(this.speed);
          this.currentVarIndex++;

        }else{
          this.ticker.setDelay(this.speed * 10);
          this.currentVarIndex = 0;
        }
  }

  public void actionPerformed(ActionEvent e) {
    if (this.data == null){
      //without data, we don't do anything
      return;
    }
    if (e.getSource() == this.ticker) {
      this.iterateVariables();
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
    } else if (e.getSource() == this.xVarButton){
    } else if (e.getSource() == this.yVarButton){
    } else if (e.getSource() == this.varCombo){
        if (this.obs != null && this.varComboIsAdjusting == false){ //might be null if we are getting started
          this.currentVarIndex = this.varCombo.getSelectedIndex();
        }
    }
  }

  public void dataSetChanged(DataSetEvent e) {
    this.data = e.getDataSetForApps();
    this.varCombo.removeAllItems();
    String[] numericVarNames = data.getAttributeNamesNumeric();
    for (int i = 0; i < numericVarNames.length; i++){
      this.varCombo.addItem(numericVarNames[i]);
    }
    this.currentVarIndex = 0;
  }


  /**
  * adds an VariableSelectionListener
  */
  public void addVariableSelectionListener(VariableSelectionListener l) {
    listenerList.add(VariableSelectionListener.class, l);
  }

  /**
   * removes an VariableSelectionListener from the component
   */
  public void removeVariableSelectionListener(VariableSelectionListener l) {
    listenerList.remove(VariableSelectionListener.class, l);
  }

  /**
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  private void fireVariableSelectionChanged(int newVariableSelection) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    VariableSelectionEvent e = null;

    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == VariableSelectionListener.class) {
        // Lazily create the event:
        if (e == null) {
          e = new VariableSelectionEvent(this, newVariableSelection);
        }

        ((VariableSelectionListener) listeners[i + 1]).variableSelectionChanged(e);
      }
    }
    //next i
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
