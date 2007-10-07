/*------------------------------------------------------------------------------
* GeoVISTA Center, Penn State Geography Deptartment*
* Copyright (c), 2002, GeoVISTA Center
* All Rights Researved.
* Original Authors: Bonan Li
* $Author: bonan_li $
*
* $Date: 2004/03/04 00:15:39 $
*
* $Id: LayersManager.java,v 1.2 2004/03/04 00:15:39 bonan_li Exp $
*
* To Do:
*
 ------------------------------------------------------------------------------*/


package edu.psu.geovista.geoviz.map;


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import geovista.common.data.DataSetForApps;
import geovista.common.event.layerChangeEvent;
import geovista.common.event.layerChangeListener;


public class LayersManager extends JPanel implements ActionListener{
	protected final static Logger logger = Logger.getLogger(LayersManager.class.getName());
  private Vector shpLayers = new Vector();
  private Vector fileNames = new Vector();
  private Vector files = new Vector();

  private JRadioButton[] radios = null;
  private JCheckBox[] checks = null;
  private JButton[] buttons = null;
  private ButtonGroup radioGroup = new ButtonGroup();

  private int activateIdx = 0;
  private int removedIdx = 10000;
  private int[] checkStatus = null;
  private transient Vector actionListeners;
  private transient Vector layerChangeListeners;

  public LayersManager() {
    this.setPreferredSize(new Dimension(600, 200));
    this.setLayout(new GridLayout(0,4));
  }

  public void init() {
    if(this.shpLayers.isEmpty()||this.fileNames.isEmpty()||this.files.isEmpty())
      return;

    this.removeAll();
    int len = this.shpLayers.size();
    this.radios = new JRadioButton[len];
    this.checks = new JCheckBox[len];
    this.buttons = new JButton[len];
    this.checkStatus = new int[len];
    for(int i=0; i<len; i++){
      this.radios[i] = new JRadioButton();
      this.radios[i].addActionListener(this);
      this.radioGroup.add(this.radios[i]);
      this.add(this.radios[i]);
      this.checks[i] = new JCheckBox();
      this.checks[i].addActionListener(this);
//      this.checks[i].doClick();
      this.add(this.checks[i]);
      this.add(new JLabel(this.getShortFileName((String)this.fileNames.elementAt(i))));
      this.buttons[i] = new JButton("Close");
      this.buttons[i].addActionListener(this);
      this.add(this.buttons[i]);
    }
    this.radios[this.activateIdx].doClick();
    this.validate();
    this.repaint();
  }

  public void setDataSet(DataSetForApps dataSet) {
    this.shpLayers.addElement(dataSet);
    init();
  }

  public void setFileName(String name){
    this.fileNames.addElement(name);
    init();
  }

  public void setFile(File file){
    this.files.addElement(file);
    init();
  }

  public DataSetForApps getDataSetForApp() {
    return (DataSetForApps)(this.shpLayers.elementAt(this.getActivatedIdx()));
  }

  public int getActivatedIdx() {
    return this.activateIdx;
  }

  public int getRemovedIdx() {
    return this.removedIdx;
  }

  public String getShortFileName(String longName){
    int idx = longName.lastIndexOf("\\");
    return longName.substring(idx);
  }

  public File getActivateFile(){
    logger.finest("get file...");
    return (File)this.files.elementAt(this.getActivatedIdx());
  }

  public void actionPerformed(ActionEvent ae) {
    for(int i=0; i<this.shpLayers.size(); i++) {
      if(ae.getSource()==this.radios[i]){
        this.activateIdx = i;
        this.checkStatus[i] = 1;
//        this.checks[i].doClick();
      }
      else if(ae.getSource() == this.checks[i]){
      if(this.checks[i].isSelected())
          this.checkStatus[i] = 1;
        else
          this.checkStatus[i] = 0;
      }
      else if(ae.getSource() == this.buttons[i])
        this.removeShpLayer(i);
    }
    this.fireLayerChanged(new layerChangeEvent(this,this.activateIdx, this.removedIdx, this.getActivateFile(), this.getDataSetForApp()));
  }

  public void removeShpLayer(int i){
    this.removedIdx = i;
    this.shpLayers.removeElementAt(i);
    this.fileNames.removeElementAt(i);
    this.init();
  }
  public synchronized void removeActionListener(ActionListener l) {
    if (actionListeners != null && actionListeners.contains(l)) {
      Vector v = (Vector) actionListeners.clone();
      v.removeElement(l);
      actionListeners = v;
    }
  }
  public synchronized void addActionListener(ActionListener l) {
    Vector v = actionListeners == null ? new Vector(2) : (Vector) actionListeners.clone();
    if (!v.contains(l)) {
      v.addElement(l);
      actionListeners = v;
    }
  }
  public synchronized void removelayerChangeListener(layerChangeListener l) {
    if (layerChangeListeners != null && layerChangeListeners.contains(l)) {
      Vector v = (Vector) layerChangeListeners.clone();
      v.removeElement(l);
      layerChangeListeners = v;
    }
  }
  public synchronized void addlayerChangeListener(layerChangeListener l) {
    Vector v = layerChangeListeners == null ? new Vector(2) : (Vector) layerChangeListeners.clone();
    if (!v.contains(l)) {
      v.addElement(l);
      layerChangeListeners = v;
    }
  }
  protected void fireLayerChanged(layerChangeEvent e) {
    if (layerChangeListeners != null) {
      Vector listeners = layerChangeListeners;
      int count = listeners.size();
      for (int i = 0; i < count; i++) {
        ((layerChangeListener) listeners.elementAt(i)).layerChanged(e);
      }
    }
  }
}