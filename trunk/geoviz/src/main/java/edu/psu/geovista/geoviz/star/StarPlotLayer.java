/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class StarPlotLayer
 Copyright (c), 2003-5, Frank Hardisty
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: StarPlotLayer.java,v 1.2 2006/02/17 17:21:23 hardisty Exp $
 $Date: 2006/02/17 17:21:23 $
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
package edu.psu.geovista.geoviz.star;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;

import edu.psu.geovista.common.event.DataSetEvent;
import edu.psu.geovista.common.event.DataSetListener;
import edu.psu.geovista.common.event.IndicationEvent;
import edu.psu.geovista.common.event.IndicationListener;
import edu.psu.geovista.common.event.SubspaceEvent;
import edu.psu.geovista.data.DescriptiveStatistics;
import edu.psu.geovista.data.geog.DataSetForApps;
import edu.psu.geovista.symbolization.glyph.Glyph;

/**
 *
 * Forms StarPlotRenderer[] out of data given to it.
 * Paints StarPlotRenderer[] onto a Graphics2D object passed in.
 *
 *
 * @author Frank Hardisty
 * @version $Revision: 1.2 $
 */
public class StarPlotLayer
    implements DataSetListener, IndicationListener {

  private transient StarPlotRenderer[] plots;
  private transient DataSetForApps dataSet;
  private transient int indication = -1;
  private transient float penWidth;
  private transient int[] varList;
  private transient int[] obsList; //in case we want to subset for some reason, i.e. selection or conditioning
  private transient Color[] starColors;
  private transient HashMap obsHashMap;
  //private static boolean DEBUG = false;

  public static final int MAX_N_VARS = 6;

  public StarPlotLayer() {

    obsHashMap = new HashMap();

  }

  public void dataSetChanged(DataSetEvent e) {
	this.indication = -1;
    this.dataSet = e.getDataSetForApps();
    this.initStarPlots(dataSet.getNumObservations());

    int nVars = dataSet.getNumberNumericAttributes();
    int maxVars = StarPlotLayer.MAX_N_VARS;

    if (maxVars < nVars) {
      nVars = maxVars;
    }

    varList = new int[nVars];
    for (int i = 0; i < nVars; i++) {
      varList[i] = i;
    }

    makeStarPlots();
  }

  public void subspaceChanged(SubspaceEvent e) {
    this.varList = e.getSubspace();
    findSpikeLengths(varList);

  }


  private void initStarPlots(int numPlots) {
    this.obsList = new int[dataSet.getNumObservations()];
this.plots = new StarPlotRenderer[obsList.length];


    plots = new StarPlotRenderer[obsList.length];
    for (int i = 0; i < plots.length; i++) {
      obsList[i] = i;
      plots[i] = new StarPlotRenderer();

    }
    this.obsHashMap.clear();
    for (int i = 0; i < plots.length; i++) {
      this.obsHashMap.put(new Integer(i), this.plots[i]);

    }

  }


  private void makeStarPlots() {

    this.applyStarFillColors(this.starColors);
    findSpikeLengths(varList);
    this.indication = -1; //clear indication

  }

  private void findSpikeLengths(int[] vars){
    int nVars = vars.length;
    double[] minVals = new double[nVars];
    double[] maxVals = new double[nVars];
    double[] ranges = new double[nVars];

    int[][] spikeLengths = new int[obsList.length][nVars];
    for (int i = 0; i < nVars; i++) {
      double[] varData = dataSet.getNumericDataAsDouble(vars[i]);
      minVals[i] = DescriptiveStatistics.min(varData);
      maxVals[i] = DescriptiveStatistics.max(varData);
      ranges[i] = DescriptiveStatistics.range(varData);
      double range = 0d;
      double prop = 0d;
      for (int row = 0; row < obsList.length; row++) {
        int index = obsList[row];
        double val = varData[index];
        range = ranges[i];
        //make range zero-based
        range = range - minVals[i];
        val = val - minVals[i];
        prop = val / ranges[i];
        spikeLengths[row][i] = (int) (prop * 100d);
      }
    }
    int[] spikes = new int[nVars];
    for (int row = 0; row < obsList.length; row++) {

      for (int col = 0; col < spikes.length; col++) {

        spikes[col] = spikeLengths[row][col];
      }
      this.plots[row].setLengths(spikes);
    }

  }

  public void renderStars(Graphics2D g2) {

    if (plots == null || plots[0] == null) {
      return;
    }

    g2.setStroke(new BasicStroke(penWidth));

    for (int i = 0; i < this.plots.length; i++) {
      plots[i].paintStar(g2);
    }

    StarPlotRenderer sp = (StarPlotRenderer)this.obsHashMap.get(new Integer(
        indication));
    if (sp != null) {
      g2.setColor(StarPlotRenderer.defaultIndicationColor);
      sp.paintStar(g2);
    }

  }

  public void renderStar(Graphics2D g2, int plotNumber) {
    //xxx need to handle negative width
    if(plotNumber < 0 || plotNumber >= plots.length){
      return;
    }
    g2.setStroke(new BasicStroke(penWidth));
    plots[plotNumber].paintStar(g2);
  }

  public String[] getVarNames() {
	if (this.varList == null){
		return null;
	}

    String[] varNames = new String[this.varList.length];
    for (int i = 0; i < this.varList.length; i++) {
      varNames[i] = this.dataSet.getNumericArrayName(this.varList[i]);
    }

    return varNames;
  }

  public double[] getValues(int obs) {
	if (this.varList == null){
		return null;
	}
    double[] vals = new double[this.varList.length];
    
    for (int i = 0; i < varList.length; i++) {
      vals[i] = this.dataSet.getNumericValueAsDouble(varList[i], obs);
    }
    return vals;
  }

  public int[] getSpikeLengths(int obs) {
	if (plots == null || plots[obs] == null){
		return null;
	}
    return this.plots[obs].getSpikeLengths();
  }

  public void indicationChanged(IndicationEvent e) {

    this.setIndication(e.getIndication());

  }

  public Glyph[] findGlyphs() {
    StarPlotRenderer[] newPlots = new StarPlotRenderer[plots.length];
    for (int i = 0; i < this.plots.length; i++) {
      newPlots[i] = this.plots[i].copy();
    }
    return newPlots;
  }

  public int getIndication() {
    return indication;
  }

  public void setIndication(int newIndication) {
    //the incoming indication is an index into the overall data set
    if (this.indication == newIndication) {
      return; //no need to do anything
    }

    StarPlotRenderer spOld = (StarPlotRenderer)this.obsHashMap.get(new Integer(
        indication));
    if (this.indication >= 0 && spOld != null) { //clear old indication, if we have one
      spOld.setFillColor(this.starColors[this.indication]);
    }
    StarPlotRenderer spNew = (StarPlotRenderer)this.obsHashMap.get(new Integer(
        newIndication));
    

    if (spNew != null) { //paint new indication

      spNew.setFillColor(StarPlotRenderer.defaultIndicationColor);

      this.indication = newIndication;
    }

  }

  public String getObservationName(int index) {
    //int orderIndex = this.plotsOrder[index];
    if (this.dataSet == null) {
      return "";
    }
    if (this.dataSet.getObservationNames() == null) {
      return String.valueOf(index);
    }
    return this.dataSet.getObservationNames()[index];
  }

  public void setStarFillColors(Color[] starColors) {
    this.applyStarFillColors(starColors);
  }

  void applyStarFillColors(Color[] starColors) {
    this.starColors = starColors;
    if (plots == null ||
        starColors != null && plots.length != starColors.length) {
      return;
    }
    if (this.starColors == null) {
      this.starColors = new Color[plots.length];
      for (int i = 0; i < this.plots.length; i++) {
        this.starColors[i] = StarPlotRenderer.defaultFillColor;
        this.plots[i].setFillColor(this.starColors[i]);
      }
    }
    else {
      for (int i = 0; i < this.plots.length; i++) {
        this.plots[i].setFillColor(this.starColors[i]);
      }
    }

  }

  public Color getStarFillColor(int ind) {
    return this.starColors[ind];

  }

  void setPlotLocations(Rectangle[] plotLocs) {
    for (int i = 0; i < plots.length; i++){
      plots[i].setTargetArea(plotLocs[i]);
    }
  }

  /* Sets the visible set of starplots. the array of ints is an array of
    which starplots are to be visible. Null arrays or arrays of length zero
   are ignored.

   */
  public void setObsList(int[] obsList) { //for selections etc.
    if (obsList == null || obsList.length < 1) {
      return;
    }
    this.obsList = obsList;
    //no no no, we should set the target area of the relevant plots
    //this.plotsOrder = obsList;
    //this.plotLocations = new Rectangle[plotsOrder.length];
    //this.makeStarPlots();
    //and update our obsHashMap
    this.obsHashMap.clear();
    for (int i = 0; i < obsList.length; i++) {
      this.obsHashMap.put(new Integer(obsList[i]), this.plots[obsList[i]]);

    }
    this.indication = -1; //clear indication

  }

  public int[] getObsList() {
    return obsList;
  }


  public DataSetForApps getDataSet() {
    return dataSet;
  }
} //end class
