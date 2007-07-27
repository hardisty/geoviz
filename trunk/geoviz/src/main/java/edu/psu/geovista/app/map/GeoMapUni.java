/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class GeoMapUni
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: GeoMapUni.java,v 1.12 2005/08/12 17:25:21 hardisty Exp $
 $Date: 2005/08/12 17:25:21 $
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

package edu.psu.geovista.app.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import edu.psu.geovista.classification.ClassifierPicker;
import edu.psu.geovista.common.event.PaletteEvent;
import edu.psu.geovista.common.event.PaletteListener;
import edu.psu.geovista.data.geog.DataSetForApps;
import edu.psu.geovista.symbolization.BivariateColorSymbolClassification;
import edu.psu.geovista.symbolization.BivariateColorSymbolClassificationSimple;
import edu.psu.geovista.symbolization.ColorSymbolClassification;
import edu.psu.geovista.symbolization.event.ColorClassifierEvent;
import edu.psu.geovista.symbolization.event.ColorClassifierListener;
import edu.psu.geovista.ui.cursor.GeoCursors;
import edu.psu.geovista.ui.event.DataSetEvent;
import edu.psu.geovista.ui.event.DataSetListener;
import edu.psu.geovista.ui.event.IndicationEvent;
import edu.psu.geovista.ui.event.IndicationListener;
import edu.psu.geovista.ui.event.SelectionEvent;
import edu.psu.geovista.ui.event.SelectionListener;
import edu.psu.geovista.ui.event.SpatialExtentEvent;
import edu.psu.geovista.ui.event.SpatialExtentListener;
import edu.psu.geovista.visclass.VisualClassifier;

/**
 * This class handles the user state, like selection, pan, zoom, plus
 * symbolization options.
 *
 * MapCanvas does most of the work.
 */
public class GeoMapUni
    extends JPanel implements
//MouseListener, MouseMotionListener,
    ActionListener, SelectionListener, IndicationListener, DataSetListener,
    ColorClassifierListener,
    SpatialExtentListener,
    PaletteListener, TableModelListener {
  public static final int VARIABLE_CHOOSER_MODE_ACTIVE = 0;
  public static final int VARIABLE_CHOOSER_MODE_FIXED = 1;
  public static final int VARIABLE_CHOOSER_MODE_HIDDEN = 2;
  transient private MapCanvas mapCan;
  transient private VisualClassifier visClassOne;
  //transient private VisualClassifier visClassTwo;
  transient private JToolBar mapTools;
  transient private JPanel topContent;
  transient private GeoCursors cursors;
  private JPanel vcPanel = new JPanel();
  transient private DataSetForApps dataSet;
  public GeoMapUni() {
    super();

    vcPanel = new JPanel();
    vcPanel.setPreferredSize(new Dimension(600, 40));
    vcPanel.setLayout(new BoxLayout(vcPanel, BoxLayout.Y_AXIS));
    visClassOne = new VisualClassifier();
//        visClassTwo = new VisualClassifier();
    visClassOne.setAlignmentX(Component.LEFT_ALIGNMENT);
//        visClassTwo.setAlignmentX(Component.LEFT_ALIGNMENT);
    visClassOne.setVariableChooserMode(
        ClassifierPicker.VARIABLE_CHOOSER_MODE_ACTIVE);
//        visClassTwo.setVariableChooserMode(
//                ClassifierPicker.VARIABLE_CHOOSER_MODE_ACTIVE);
    visClassOne.addActionListener(this);
//        visClassTwo.addActionListener(this);
    vcPanel.add(visClassOne);
//        vcPanel.add(visClassTwo);

    JPanel legendPanel = new JPanel();
    legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.X_AXIS));
    legendPanel.add(vcPanel);
    legendPanel.add(Box.createRigidArea(new Dimension(4, 2)));

    topContent = new JPanel();
    topContent.setLayout(new BoxLayout(topContent, BoxLayout.Y_AXIS));
    makeToolbar();
    legendPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    mapTools.setAlignmentX(Component.LEFT_ALIGNMENT);
    topContent.add(legendPanel);
    topContent.add(mapTools);

    //note: uncomment the line below for animation panel stuff
    //vcPanel.add(this.makeAnimationPanel());
    cursors = new GeoCursors();
    this.setCursor(cursors.getCursor(GeoCursors.CURSOR_ARROW_SELECT));

    this.setLayout(new BorderLayout());
    this.add(topContent, BorderLayout.NORTH);
    mapCan = new MapCanvas();
    this.add(mapCan, BorderLayout.CENTER);
    this.mapCan.addIndicationListener(this);
    visClassOne.addColorClassifierListener(this);
//        visClassTwo.addColorClassifierListener(this);



    //this.colorClassifierChanged(new ColorClassifierEvent(visClassTwo,visClassTwo.getColorSymbolClassification()));
  }

  public JPanel makeAnimationPanel() {
    JPanel animationPanel = new JPanel();
    Dimension prefSize = new Dimension(100, 50);
    animationPanel.setSize(prefSize);
    animationPanel.setPreferredSize(prefSize);

    //animationPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
    JLabel timeLabel = new JLabel("Time Step:");
    animationPanel.add(timeLabel);

    JSlider timeSlider = new JSlider(0, 10, 3);
    timeSlider.setPaintLabels(true);
    timeSlider.setPaintTicks(true);
    timeSlider.setPaintTrack(true);
    timeSlider.setMajorTickSpacing(1);
    timeSlider.createStandardLabels(2);

    animationPanel.add(timeSlider);

    return animationPanel;
  }

  public void makeToolbar() {
    mapTools = new JToolBar();

    //Dimension prefSize = new Dimension(100,10);
    //mapTools.setMinimumSize(prefSize);
    //mapTools.setPreferredSize(prefSize);
    JButton button = null;
    Class cl = GeoMapUni.class;
    URL urlGif = null;
    Dimension buttDim = new Dimension(20, 20);

    //first button
    try {
      urlGif = cl.getResource("resources/select16.gif");
      button = new JButton(new ImageIcon(urlGif));
      button.setPreferredSize(buttDim);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    button.setToolTipText("Enter selection mode");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        GeoMapUni.this.setCursor(cursors.getCursor(
            GeoCursors.CURSOR_ARROW_SELECT));
        GeoMapUni.this.mapCan.setMode(MapCanvas.MODE_SELECT);
      }
    });
    mapTools.add(button);

    mapTools.addSeparator();

    //second button
    try {
      urlGif = cl.getResource("resources/ZoomIn16.gif");
      button = new JButton(new ImageIcon(urlGif));
      button.setPreferredSize(buttDim);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    button.setToolTipText("Enter zoom in mode");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        GeoMapUni.this.setCursor(cursors.getCursor(
            GeoCursors.CURSOR_ARROW_ZOOM_IN));
        GeoMapUni.this.mapCan.setMode(MapCanvas.MODE_ZOOM_IN);

        //GeoMapUni.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
      }
    });
    mapTools.add(button);

    //third button
    try {
      urlGif = cl.getResource("resources/ZoomOut16.gif");
      button = new JButton(new ImageIcon(urlGif));
      button.setPreferredSize(buttDim);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    button.setToolTipText("Enter zoom out mode");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        GeoMapUni.this.mapCan.setMode(MapCanvas.MODE_ZOOM_OUT);
        GeoMapUni.this.setCursor(cursors.getCursor(
            GeoCursors.CURSOR_ARROW_ZOOM_OUT));
      }
    });
    mapTools.add(button);

    //fourth button
    try {
      urlGif = cl.getResource("resources/Home16.gif");
      button = new JButton(new ImageIcon(urlGif));
      button.setPreferredSize(buttDim);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    button.setToolTipText("Zoom to full extent");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        GeoMapUni.this.mapCan.zoomFullExtent();
      }
    });
    mapTools.add(button);

    //fifth button
    try {
      urlGif = cl.getResource("resources/pan16.gif");
      button = new JButton(new ImageIcon(urlGif));
      button.setPreferredSize(buttDim);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    button.setToolTipText("Enter pan mode");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        GeoMapUni.this.setCursor(cursors.getCursor(
            GeoCursors.CURSOR_ARROW_PAN));
        GeoMapUni.this.mapCan.setMode(MapCanvas.MODE_PAN);
      }
    });
    mapTools.add(button);
  }

  public VisualClassifier getVisClassOne() {
    return this.visClassOne;
  }

  public JPanel getVcPanel() {
    return vcPanel;
  }

  public void actionPerformed(ActionEvent e) {
    Object src = e.getSource();
    String command = e.getActionCommand();
    String varChangedCommand = ClassifierPicker.
        COMMAND_SELECTED_VARIABLE_CHANGED;

    if ( (src == this.visClassOne) && command.equals(varChangedCommand)) {
      int index = visClassOne.getCurrVariableIndex();
      //index++;//no longer, data set for apps has changed
      this.mapCan.setCurrColorColumnX(index);
      this.mapCan.setCurrColorColumnY(index);

      this.firePropertyChange("SelectedVariable", index, index);
    }

  }

  public void selectionChanged(SelectionEvent e) {
    mapCan.selectionChanged(e);
  }

  public void indicationChanged(IndicationEvent e) {
    Object source = e.getSource();

    if ( (source == this.mapCan) && (e.getIndication() >= 0)) {
      this.visClassOne.setIndicatedClass(e.getXClass());
//            this.visClass.setIndicatedClass(e.getYClass());

      //this.fireIndicationChanged(e.getIndication());
    }
    else if ( (source == this.mapCan) && (e.getIndication() < 0)) {
      this.visClassOne.setIndicatedClass( -1);
//            this.visClassTwo.setIndicatedClass(-1);

      //this.fireIndicationChanged(e.getIndication());
    }
    else {
      mapCan.indicationChanged(e);
    }
  }

  public void spatialExtentChanged(SpatialExtentEvent e) {
    mapCan.spatialExtentChanged(e);
  }

  public void zoomFullExtent() {
    this.mapCan.zoomFullExtent();
  }

  public void dataSetChanged(DataSetEvent e) {
    //mapCan.dataSetChanged(e);
    this.setDataSet(e.getDataSetForApps());
    this.dataSet = e.getDataSetForApps();
    this.dataSet.addTableModelListener(this);
  }

  public void paletteChanged(PaletteEvent e) {
    this.visClassOne.paletteChanged(e);

  }

  public void colorClassifierChanged(ColorClassifierEvent e) {
    if (e.getSource() == this.visClassOne) {
      e.setOrientation(ColorClassifierEvent.SOURCE_ORIENTATION_X);
    }
    if (this.mapCan == null) {
      return;
    }
    ColorSymbolClassification colorSymbolizerX = this.visClassOne.getColorClasser();
    ColorSymbolClassification colorSymbolizerY = this.visClassOne.getColorClasser();

    BivariateColorSymbolClassificationSimple biColorSymbolizer =
        new BivariateColorSymbolClassificationSimple();

    biColorSymbolizer.setClasserX(colorSymbolizerX.getClasser());
    biColorSymbolizer.setColorerX(colorSymbolizerX.getColorer());

    biColorSymbolizer.setClasserY(colorSymbolizerY.getClasser());
    biColorSymbolizer.setColorerY(colorSymbolizerY.getColorer());
    this.mapCan.setBivarColorClasser(biColorSymbolizer);
  }

  public void setXVariable(int var) {
    this.visClassOne.setCurrVariableIndex(var);
  }

  public int getCurrentVariable() {
    return this.visClassOne.getCurrVariableIndex();
  }

  public void setYVariable(int var) {
    this.visClassOne.setCurrVariableIndex(var);
  }

  public void setXChooserMode(int chooserMode) {
    this.visClassOne.setVariableChooserMode(chooserMode);
  }

  public void setYChooserMode(int chooserMode) {
    this.visClassOne.setVariableChooserMode(chooserMode);
  }

  public void setBivarColorClasser(BivariateColorSymbolClassification bivarColorClasser) {
    this.mapCan.setBivarColorClasser(bivarColorClasser);
  }

  public void setSelectedObservations(int[] selObs) {
    this.mapCan.setSelectedObservationsInt(selObs);
  }

  public int[] getSelectedObservations() {
    return this.mapCan.getSelectedObservationsInt();
  }

  public Color[] getColors() {
    return this.mapCan.getColors();
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
    this.mapCan.setDataSet(data);
    this.visClassOne.setDataSet(data);



    int numNumeric = data.getNumberNumericAttributes();

    int currColorColumnX = this.mapCan.getCurrColorColumnX();

    if (currColorColumnX < 0) {
      if (numNumeric > 0) {
        this.setXVariable(0);
        this.setYVariable(0);
        this.mapCan.setCurrColorColumnX(0);
        this.mapCan.setCurrColorColumnY(0);
      }
    }
    else if (currColorColumnX < numNumeric) {
      this.setXVariable(currColorColumnX);
      this.setYVariable(currColorColumnX);
      this.mapCan.setCurrColorColumnX(currColorColumnX);
      this.mapCan.setCurrColorColumnY(currColorColumnX);
    }

  }

  public void setAuxiliarySpatialData(DataSetForApps data) {

    mapCan.setAuxiliarySpatialData(data);
  }

  public void setBackground(Color bg) {
    if ( (mapCan != null) && (bg != null)) {
      this.mapCan.setBackground(bg);
    }
  }

  /**
   * adds an IndicationListener
   */
  public void addIndicationListener(IndicationListener l) {
    this.mapCan.addIndicationListener(l);
  }

  /**
   * removes an IndicationListener from the component
   */
  public void removeIndicationListener(IndicationListener l) {
    this.mapCan.removeIndicationListener(l);
  }

  /**
   * adds an SelectionListener
   */
  public void addSelectionListener(SelectionListener l) {
    this.mapCan.addSelectionListener(l);
  }

  /**
   * removes an SelectionListener from the component
   */
  public void removeSelectionListener(SelectionListener l) {
    this.mapCan.removeSelectionListener(l);
  }

  /**
   * adds an SpatialExtentListener
   */
  public void addSpatialExtentListener(SpatialExtentListener l) {
    this.mapCan.addSpatialExtentListener(l);
  }

  /**
   * removes an SpatialExtentListener from the component
   */
  public void removeSpatialExtentListener(SpatialExtentListener l) {
    listenerList.remove(SpatialExtentListener.class, l);
    this.mapCan.removeSpatialExtentListener(l);
  }

public void tableChanged(TableModelEvent e) {
	this.visClassOne.setDataSet(this.dataSet);
	
}

}
