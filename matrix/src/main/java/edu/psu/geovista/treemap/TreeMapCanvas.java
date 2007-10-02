/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class TreeMapCanvas
 Copyright (c), 2003, Frank Hardisty
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: TreeMapCanvas.java,v 1.4 2005/03/28 14:58:31 hardisty Exp $
 $Date: 2005/03/28 14:58:31 $
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
package edu.psu.geovista.treemap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import treemap.TMView;

import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.common.event.ClassificationEvent;
import edu.psu.geovista.common.event.ColorArrayEvent;
import edu.psu.geovista.common.event.ColorArrayListener;
import edu.psu.geovista.common.event.DataSetEvent;
import edu.psu.geovista.common.event.DataSetListener;
import edu.psu.geovista.common.event.DataSetModifiedEvent;
import edu.psu.geovista.common.event.DataSetModifiedListener;
import edu.psu.geovista.common.event.IndicationEvent;
import edu.psu.geovista.common.event.IndicationListener;
import edu.psu.geovista.common.event.SubspaceEvent;
import edu.psu.geovista.common.event.SubspaceListener;
import edu.psu.geovista.data.DescriptiveStatistics;
import edu.sc.geoviz.treemap.TMDataDraw;
import edu.sc.geoviz.treemap.TMDataNode;
import edu.sc.geoviz.treemap.TMDataSize;

/**
 * Paints an array of TreeMapCanvas. Responds to and broadcasts DataSetChanged,
 * IndicationChanged etc. events.
 *
 * @author Frank Hardisty
 * @version $Revision: 1.4 $
 */
public class TreeMapCanvas
    extends JPanel implements DataSetListener, DataSetModifiedListener,
    IndicationListener, SubspaceListener,
    ColorArrayListener, MouseListener,
    MouseMotionListener {
	protected final static Logger logger = Logger.getLogger(TreeMapCanvas.class.getName());
  private TMDataNode root = null; // the root of the tree
  TMDataSize fSize;
  TMDataDraw fDraw;
  int indication;
  TMView tmview;
  DataSetForApps dataSet;
  TMDataNode[] leaves;
  int[] classification;
  Color[] colors;
  int groupingVarID;
  int sizingVarID;
  int colorVarID;

  public TreeMapCanvas() {
    this.setLayout(new BorderLayout());
    root = new TMDataNode(0d, "root");
    fSize = new TMDataSize();
    fDraw = new TMDataDraw();
    treemap.TreeMap treeMap = new treemap.TreeMap(root);
    tmview = treeMap.getView(fSize, fDraw);
    this.add(tmview, BorderLayout.CENTER);
    this.addMouseListener(this);
    this.addMouseMotionListener(this);
    tmview.addMouseListener(this);
    tmview.addMouseMotionListener(this);
  }

  private String findRootName() {
    if (this.dataSet == null) {
      return "no data set";
    }
    String rootName = "";
    rootName = rootName + "Colored by: " +
        this.dataSet.getNumericArrayName(this.colorVarID);
    rootName = rootName + ", Grouped by: " +
        this.dataSet.getNumericArrayName(this.groupingVarID);
    rootName = rootName + ", Sized by: " +
        this.dataSet.getNumericArrayName(this.sizingVarID);

    return rootName;
  }

  private void initTMView() {
    String rootName = findRootName();
    this.root = new TMDataNode(0, rootName);
    if (dataSet == null || this.classification == null) {
      return;
    }

    int nObs = dataSet.getNumObservations();
    leaves = new TMDataNode[nObs];

    String varName = dataSet.getNumericArrayName(this.groupingVarID);
    int nBranches = DescriptiveStatistics.max(this.classification) + 1;
    TMDataNode[] branches = new TMDataNode[nBranches];
    double[] branchMin = new double[nBranches];
    double[] branchMax = new double[nBranches];

    for (int i = 0; i < branches.length; i++) {
      TMDataNode branch = new TMDataNode(0, varName + " branch " + i);
      branches[i] = branch;
      branchMin[i] = Double.MAX_VALUE;
      branchMax[i] = Double.MAX_VALUE * -1;
    }

    for (int i = 0; i < nObs; i++) {
      double val = dataSet.getNumericValueAsDouble(this.sizingVarID, i);
      double branchVal = dataSet.getNumericValueAsDouble(this.groupingVarID,i);
      String name = dataSet.getObservationName(i);
      TMDataNode aData = new TMDataNode(val, name);
      int whichBranch = this.classification[i];
      branches[whichBranch].addChild(aData);
      if (branchVal < branchMin[whichBranch]) {
        branchMin[whichBranch] = branchVal;
      }
      if (branchVal > branchMax[whichBranch]) {
        branchMax[whichBranch] = branchVal;
      }

      leaves[i] = aData;
      if (this.colors != null) {
        leaves[i].setColor(this.colors[i]);
      }
    }
    for (int i = 0; i < branches.length; i++) {
      root.addChild(branches[i]);
      branches[i].setName(branchMin[i] + " < " + varName + " < " +  branchMax[i]);
    }

    treemap.TreeMap treeMap = new treemap.TreeMap(root); //xxx fix me? do we need a new tree every time?
    tmview = treeMap.getView(fSize, fDraw);
    tmview.addMouseListener(this);
    tmview.addMouseMotionListener(this);
    this.removeAll();
    this.add(tmview, BorderLayout.CENTER);

  }

  public void dataSetChanged(DataSetEvent e) {
    this.dataSet = e.getDataSetForApps();
    initTMView();
  }

  public void dataSetModified(DataSetModifiedEvent e) {

  }

  public void subspaceChanged(SubspaceEvent e) {

  }

  public void classificationChanged(ClassificationEvent e) {
    int[] newClasses = e.getClassification();
    this.setClassification(newClasses);
  }

  public void setClassification(int[] newClasses) {
    this.classification = newClasses;
    this.initTMView();
  }
public void setLayoutMethod(String layoutMethod) {

    tmview.setAlgorithm(layoutMethod);
  }
  public void setGroupingVarID(int groupingVarID) {
    this.groupingVarID = groupingVarID;
  }

  public void setSizingVarID(int sizingVarID) {
    if (this.sizingVarID == sizingVarID || this.dataSet == null) {
      return;
    }
    this.sizingVarID = sizingVarID;
    double[] data = this.dataSet.getNumericDataAsDouble(sizingVarID);
    if (this.leaves == null) {
      return;
    }
    for (int i = 0; i < data.length; i++) {
      leaves[i].setValue(data[i]);

    }
    //xxx fah we should do the line following, instead of initTMView
    //xxx but the method doesn't exist yet...
    //tmview.changeTMComputeSize(new TMComputeSize());
    this.initTMView();

  }

  public void setColorVarID(int colorVarID) {
    this.colorVarID = colorVarID;
    this.root.setName(this.findRootName());

  }

  public void colorArrayChanged(ColorArrayEvent e) {
    Color[] dataColors = e.getColors();
    this.colors = dataColors; //remember these for when leaves get made, if not now
    if (this.leaves == null || this.dataSet == null) {
      return;
    }
    for (int i = 0; i < dataColors.length; i++) {
      leaves[i].setColor(dataColors[i]);

    }
    if (this.root != null) {
      tmview.changeTMComputeDraw(new TMDataDraw());
    }
  }

  public void indicationChanged(IndicationEvent e) {
    int newIndication = e.getIndication();
logger.finest("this.indicaiton = " + this.indication);
logger.finest("new  = " + newIndication);

    //need to fix old one?
    if (newIndication < 0 && this.indication >= 0) { //new indication is out of range, old was was in range
      this.leaves[indication].setIsIndicated(false);

      this.indication = newIndication;
      tmview.repaint();
      tmview.changeTMComputeDraw(new TMDataDraw());
    }
    if (newIndication != this.indication && this.leaves != null && newIndication >= 0 &&
        newIndication < this.leaves.length) {
      if (this.indication >= 0) {
        this.leaves[indication].setIsIndicated(false);
      }
      this.leaves[newIndication].setIsIndicated(true);
      this.indication = newIndication;

      tmview.changeTMComputeDraw(new TMDataDraw());

    }
  }

  //start mouse events
  /***
   * Interface methods for mouse events
   *  * **/
  public void mouseClicked(MouseEvent e) {

  }

  public void mousePressed(MouseEvent e) {

  }

  public void mouseReleased(MouseEvent e) {

  }

  public void mouseEntered(MouseEvent e) {

  }

  public void mouseExited(MouseEvent e) {
    this.fireIndicationChanged( -1); //clear indication
    this.indicationChanged(new IndicationEvent(this, -1));
  }

  public void mouseDragged(MouseEvent e) {

  }

  public void mouseMoved(MouseEvent e) {

    if (this.tmview == null || this.dataSet == null) {
      return;
    }
    int whichPlot = -1;
    Object node = tmview.getNodeUnderTheMouse(e);
    if(node == null){
      return;//can't find anything, so forget about it.
    }
    for (int i = 0; i < this.leaves.length; i++) {
      if (node == leaves[i]) {
        whichPlot = i;
      }
    }

    this.fireIndicationChanged(whichPlot);

  }

//end mouse events
  /**
   * adds an IndicationListener to the component
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
  public void fireIndicationChanged(int indication) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    IndicationEvent e = null;

    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == IndicationListener.class) {
        // Lazily create the event:
        if (e == null) {
          e = new IndicationEvent(this, indication);
        }

        ( (IndicationListener) listeners[i + 1]).indicationChanged(e);
      }
    }
  }

  public int getGroupingVarID() {
    return groupingVarID;
  }

} //end class
