/* -------------------------------------------------------------------
 Java source file for the class GeoMapCartogram
 Copyright (c), 2005 Frank Hardisty
 $Author: hardistf $
 $Id: CartogramGeoMap.java,v 1.9 2005/12/05 20:35:36 hardistf Exp $
 $Date: 2005/12/05 20:35:36 $
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

package edu.psu.geovista.cartogram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.event.EventListenerList;

import edu.psu.geovista.app.map.GeoMapUni;
import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.common.event.DataSetEvent;
import edu.psu.geovista.common.event.DataSetListener;
import edu.psu.geovista.data.sample.GeoDataGeneralizedStates;



/*
 * This class like a GeoMap but it makes cartograms
 */

public class GeoMapCartogram
    extends GeoMapUni {

  DataSetForApps dataSet;
  String inputFileName;
  CartogramPreferences preferencesFrame;
  boolean DEBUG = false;
  TransformsMain trans;
  CartogramPicker cgramPicker;
  DataSetForApps cartogramData;
	final static Logger logger = Logger.getLogger(GeoMapCartogram.class.getName());
  public GeoMapCartogram() {
    super();
    this.getVisClassOne().setBorder(BorderFactory.createLineBorder(Color.black));
    cgramPicker = new CartogramPicker();
    cgramPicker.setBorder(BorderFactory.createLineBorder(Color.black));
    cgramPicker.getVariableCombo().addActionListener(this);
    cgramPicker.getPreferencesButton().addActionListener(this);
    cgramPicker.getSendShapes().addActionListener(this);
    this.trans = new TransformsMain(false);
    JPanel vcPanel = this.getVcPanel();
    vcPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    vcPanel.setPreferredSize(new Dimension(500, 80));
    vcPanel.add(cgramPicker);
    if (logger.isLoggable(Level.FINEST)) {
      try {
        new Console();
      }
      catch (IOException e) {
        e.printStackTrace();
      }

    }
    try {
      //initGui();
      @SuppressWarnings("unused")
	Preferences gvPrefs = Preferences.userNodeForPackage(this.getClass());
      this.preferencesFrame = new CartogramPreferences(
          "Cartogram Preferences", this, this.trans);

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /*
   * All action handling goes here.
   */

  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);
    if (e.getSource() == this.getVisClassOne()) {
      //ignore it, handled by superclass
    }
    else if (e.getSource() == this.cgramPicker.getVariableCombo()) {
      if (this.dataSet != null) {

    		if (logger.isLoggable(Level.INFO)){
    			logger.info("creating cartogram....");
    		}

        createCartogram();
      }
    }
    else if(e.getSource() == this.cgramPicker.getSendShapes()){
      if (this.cartogramData != null){
        this.fireDataSetChanged(this.cartogramData);
      }
    }
    else if (e.getSource().equals(this.cgramPicker.getPreferencesButton())) {

    	if (logger.isLoggable(Level.FINEST)){
    		logger.finest("setting preferences....");
    	}
      this.preferencesFrame.setVisible(true);
    }

    else {

    	if (logger.isLoggable(Level.FINEST)){
    		logger.warning(          "GeoMapCartogram.actionPerformed, unexpected source encountered: " +
    		          e.getSource().getClass());
    	}
    }
  }

  /*
   * This method actually creates the temporary files and creates
    a TransformsMain to do the work.
   */
  public void createCartogram() {

    int currentVar = this.cgramPicker.getSelectedIndex();
    if (currentVar < 0){
      return;
    }
    Preferences gvPrefs = Preferences.userNodeForPackage(this.getClass());

    int mapVar = super.getCurrentVariable();
    this.trans = new TransformsMain(false);
    this.preferencesFrame.setTransformParams(this.trans);


    JProgressBar pBar = this.cgramPicker.getProgressBar();
    DataSetForApps newData = MapGenFile.createCartogram(pBar, this.dataSet, gvPrefs,
        currentVar, this.trans);

    this.setCartogramDataSet(newData);
    super.getVisClassOne().setCurrVariableIndex(mapVar);
  }
  
  /**
   * @param data
   * 
   * This method is deprecated becuase it wants to create its very own pet
   * DataSetForApps. This is no longer allowed, to allow for a mutable, 
   * common data set. Use of this method may lead to unexpected
   * program behavoir. 
   * Please use setDataSet instead.
   */
  @Deprecated
  public void setData(Object[] data) {
	 this.setDataSet(new DataSetForApps(data));
    
  }
  
  public void setDataSet(DataSetForApps data) {
    super.setDataSet(data);
    this.dataSet = data;
    this.cartogramData = null;//reset in case we already have this
    this.cgramPicker.setDataSet(data);
  }

  public void setCartogramDataSet(DataSetForApps data) {
    super.setDataSet(data);
    this.cartogramData = data;
  }
  /**
   * adds a DataSetListener
   */
  public void addDataSetListener(DataSetListener l) {
    listenerList.add(DataSetListener.class, l);
  }

  /**
   * removes a DataSetListener
   */
  public void removeDataSetListener(DataSetListener l) {
    listenerList.remove(DataSetListener.class, l);
  }

  /**
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  protected void fireDataSetChanged(DataSetForApps data) {

		if (logger.isLoggable(Level.FINEST)){
			logger.finest("ShpToShp.fireDataSetChanged, Hi!!");
		}

   // Guaranteed to return a non-null array
   Object[] listeners = listenerList.getListenerList();
   DataSetEvent e = null;
   // Process the listeners last to first, notifying
   // those that are interested in this event
   for (int i = listeners.length - 2; i >= 0; i -= 2) {
     if (listeners[i] == DataSetListener.class) {
       // Lazily create the event:
       if (e == null) {
         e = new DataSetEvent(data, this);

       }
       ((DataSetListener)listeners[i + 1]).dataSetChanged(e);
      }
    }
  }
  public static void main(String[] args) {

    GeoDataGeneralizedStates stateData = new GeoDataGeneralizedStates();

    GeoMapCartogram gui = new GeoMapCartogram();
    JFrame frame = new JFrame("Cartogram GeoMap");
    frame.getContentPane().add(gui);

    gui.setDataSet(stateData.getDataForApps());
    frame.pack();
    frame.setVisible(true);
    gui.zoomFullExtent();
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

  }

}
