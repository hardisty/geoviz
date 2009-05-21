/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */
package geovista.geoviz.star;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;

import geovista.common.data.ArraySort2D;
import geovista.common.data.DataSetForApps;
import geovista.common.data.DescriptiveStatistics;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.IndicationEvent;
import geovista.common.event.IndicationListener;
import geovista.common.event.SubspaceEvent;
import geovista.symbolization.glyph.Glyph;

/**
 * 
 * Forms StarPlotRenderer[] out of data given to it. Paints StarPlotRenderer[]
 * onto a Graphics2D object passed in.
 * 
 * 
 * @author Frank Hardisty
 * 
 */
public class StarPlotLayer implements DataSetListener, IndicationListener {

	private transient StarPlotRenderer[] plots;
	private transient DataSetForApps dataSet;
	private transient int indication = -1;
	private transient float penWidth;
	private transient int[] varList;
	private transient int[] obsList; // in case we want to subset for some
	// reason, i.e. selection or
	// conditioning
	private transient Color[] starColors;
	private transient final HashMap<Integer, StarPlotRenderer> obsHashMap;

	// private static boolean DEBUG = false;

	enum ScaleMethod {
		Linear, Rank_Order, Log, Normalized
	};

	private ScaleMethod method;
	public static final int MAX_N_VARS = 6;

	public StarPlotLayer() {

		obsHashMap = new HashMap<Integer, StarPlotRenderer>();
		method = ScaleMethod.Linear;
	}

	public void dataSetChanged(DataSetEvent e) {
		indication = -1;
		dataSet = e.getDataSetForApps();
		initStarPlots(dataSet.getNumObservations());

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
		varList = e.getSubspace();
		findSpikeLengths(varList);

	}

	private void initStarPlots(int numPlots) {
		obsList = new int[dataSet.getNumObservations()];

		plots = new StarPlotRenderer[obsList.length];
		for (int i = 0; i < plots.length; i++) {
			obsList[i] = i;
			plots[i] = new StarPlotRenderer();

		}
		obsHashMap.clear();
		for (int i = 0; i < plots.length; i++) {
			obsHashMap.put(i, plots[i]);

		}

	}

	void makeStarPlots() {

		applyStarFillColors(starColors);
		findSpikeLengths(varList);
		indication = -1; // clear indication

	}

	private void findSpikeLengths(int[] vars) {
		int[][] spikeLengths = null;
		int nVars = vars.length;
		if (method.equals(ScaleMethod.Linear)) {
			spikeLengths = findLinearScaledSpikes(vars, nVars);
		} else if (method.equals(ScaleMethod.Rank_Order)) {
			spikeLengths = findRankOrderSpikes(vars, nVars);

		} else if (method.equals(ScaleMethod.Normalized)) {
			spikeLengths = findNormalizedSpikes(vars, nVars);
		} else if (method.equals(ScaleMethod.Log)) {
			spikeLengths = findLogarithmSpikes(vars, nVars);
		}

		int[] spikes = new int[nVars];
		for (int row = 0; row < obsList.length; row++) {

			for (int col = 0; col < spikes.length; col++) {

				spikes[col] = spikeLengths[row][col];
			}
			plots[row].setLengths(spikes);
		}

	}

	@Deprecated
	private int[][] findLogarithmSpikes(int[] vars, int nVars) {
		// XXX there's something wrong with this....
		double[] minVals = new double[nVars];
		double[] maxVals = new double[nVars];
		double[] ranges = new double[nVars];

		int[][] spikeLengths = new int[obsList.length][nVars];
		for (int i = 0; i < nVars; i++) {
			double[] varData = dataSet.getNumericDataAsDouble(vars[i]);
			double[] logData = new double[varData.length];
			for (int j = 0; j < varData.length; j++) {
				logData[j] = Math.abs(Math.log(varData[j]));
			}
			minVals[i] = DescriptiveStatistics.min(logData);
			maxVals[i] = DescriptiveStatistics.max(logData);
			ranges[i] = DescriptiveStatistics.range(logData);
			double range = 0d;
			double prop = 0d;
			for (int row = 0; row < obsList.length; row++) {
				int index = obsList[row];
				double val = logData[index];
				range = ranges[i];
				// make range zero-based
				range = range - minVals[i];
				val = val - minVals[i];
				prop = val / ranges[i];
				spikeLengths[row][i] = (int) (prop * 100d);
			}
		}
		return spikeLengths;
	}

	private int[][] findNormalizedSpikes(int[] vars, int nVars) {
		double[] minVals = new double[nVars];
		double[] maxVals = new double[nVars];
		double[] ranges = new double[nVars];

		int[][] spikeLengths = new int[obsList.length][nVars];
		for (int i = 0; i < nVars; i++) {
			double[] varData = dataSet.getNumericDataAsDouble(vars[i]);
			double[] zData = DescriptiveStatistics.calculateZScores(varData);
			minVals[i] = DescriptiveStatistics.min(zData);
			maxVals[i] = DescriptiveStatistics.max(zData);
			ranges[i] = DescriptiveStatistics.range(zData);
			double range = 0d;
			double prop = 0d;
			for (int row = 0; row < obsList.length; row++) {
				int index = obsList[row];
				double val = zData[index];
				range = ranges[i];
				// make range zero-based
				range = range - minVals[i];
				val = val - minVals[i];
				prop = val / ranges[i];
				spikeLengths[row][i] = (int) (prop * 100d);
			}
		}
		return spikeLengths;
	}

	private int[][] findRankOrderSpikes(int[] vars, int nVars) {
		double[] minVals = new double[nVars];
		double[] maxVals = new double[nVars];
		double[] ranges = new double[nVars];

		int[][] spikeLengths = new int[obsList.length][nVars];
		ArraySort2D sorter = new ArraySort2D();
		for (int i = 0; i < nVars; i++) {
			double[] varData = dataSet.getNumericDataAsDouble(vars[i]);
			int[] sortedIndexes = sorter.getSortedIndex(varData);
			minVals[i] = DescriptiveStatistics.min(sortedIndexes);
			maxVals[i] = DescriptiveStatistics.max(sortedIndexes);
			ranges[i] = DescriptiveStatistics.range(sortedIndexes);
			double range = 0d;
			double prop = 0d;
			for (int row = 0; row < obsList.length; row++) {
				int index = obsList[row];
				double val = sortedIndexes[index];
				range = ranges[i];
				// make range zero-based
				range = range - minVals[i];
				val = val - minVals[i];
				prop = val / ranges[i];
				spikeLengths[row][i] = (int) (prop * 100d);
			}
		}

		return spikeLengths;
	}

	private int[][] findLinearScaledSpikes(int[] vars, int nVars) {
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
				// make range zero-based
				range = range - minVals[i];
				val = val - minVals[i];
				prop = val / ranges[i];
				spikeLengths[row][i] = (int) (prop * 100d);
			}
		}
		return spikeLengths;
	}

	public void renderStars(Graphics2D g2) {

		if (plots == null || plots[0] == null) {
			return;
		}

		g2.setStroke(new BasicStroke(penWidth));

		for (StarPlotRenderer element : plots) {
			element.paintStar(g2);
		}

		StarPlotRenderer sp = obsHashMap.get(indication);
		if (sp != null) {
			g2.setColor(StarPlotRenderer.defaultIndicationColor);
			sp.paintStar(g2);
		}

	}

	public void renderStar(Graphics2D g2, int plotNumber) {
		// xxx need to handle negative width
		if (plotNumber < 0 || plotNumber >= plots.length) {
			return;
		}
		g2.setStroke(new BasicStroke(penWidth));
		plots[plotNumber].paintStar(g2);
	}

	public String[] getVarNames() {
		if (varList == null) {
			return null;
		}

		String[] varNames = new String[varList.length];
		for (int i = 0; i < varList.length; i++) {
			varNames[i] = dataSet.getNumericArrayName(varList[i]);
		}

		return varNames;
	}

	public double[] getValues(int obs) {
		if (varList == null) {
			return null;
		}
		double[] vals = new double[varList.length];

		for (int i = 0; i < varList.length; i++) {
			vals[i] = dataSet.getNumericValueAsDouble(varList[i], obs);
		}
		return vals;
	}

	public int[] getSpikeLengths(int obs) {
		if (plots == null || plots[obs] == null) {
			return null;
		}
		return plots[obs].getSpikeLengths();
	}

	public void indicationChanged(IndicationEvent e) {

		setIndication(e.getIndication());

	}

	public Glyph[] findGlyphs() {
		StarPlotRenderer[] newPlots = new StarPlotRenderer[plots.length];
		for (int i = 0; i < plots.length; i++) {
			newPlots[i] = plots[i].copy();
		}
		return newPlots;
	}

	public int getIndication() {
		return indication;
	}

	public void setIndication(int newIndication) {
		// the incoming indication is an index into the overall data set
		if (indication == newIndication) {
			return; // no need to do anything
		}

		StarPlotRenderer spOld = obsHashMap.get(indication);
		if (indication >= 0 && spOld != null) { // clear old indication, if we
			// have one
			spOld.setFillColor(starColors[indication]);
		}
		StarPlotRenderer spNew = obsHashMap.get(newIndication);

		if (spNew != null) { // paint new indication

			spNew.setFillColor(StarPlotRenderer.defaultIndicationColor);

			indication = newIndication;
		}

	}

	public String getObservationName(int index) {
		// int orderIndex = this.plotsOrder[index];
		if (dataSet == null) {
			return "";
		}
		if (dataSet.getObservationNames() == null) {
			return String.valueOf(index);
		}
		return dataSet.getObservationNames()[index];
	}

	public void setStarFillColors(Color[] starColors) {
		applyStarFillColors(starColors);
	}

	void applyStarFillColors(Color[] starColors) {
		this.starColors = starColors;
		if (plots == null || starColors != null
				&& plots.length != starColors.length) {
			return;
		}
		if (this.starColors == null) {
			this.starColors = new Color[plots.length];
			for (int i = 0; i < plots.length; i++) {
				this.starColors[i] = StarPlotRenderer.defaultFillColor;
				plots[i].setFillColor(this.starColors[i]);
			}
		} else {
			for (int i = 0; i < plots.length; i++) {
				plots[i].setFillColor(this.starColors[i]);
			}
		}

	}

	public Color getStarFillColor(int ind) {
		return starColors[ind];

	}

	void setPlotLocations(Rectangle[] plotLocs) {
		for (int i = 0; i < plots.length; i++) {
			plots[i].setTargetArea(plotLocs[i]);
		}
	}

	/*
	 * Sets the visible set of starplots. the array of ints is an array of which
	 * starplots are to be visible. Null arrays or arrays of length zero are
	 * ignored.
	 */
	public void setObsList(int[] obsList) { // for selections etc.
		if (obsList == null || obsList.length < 1) {
			return;
		}
		this.obsList = obsList;
		// no no no, we should set the target area of the relevant plots
		// this.plotsOrder = obsList;
		// this.plotLocations = new Rectangle[plotsOrder.length];
		// this.makeStarPlots();
		// and update our obsHashMap
		obsHashMap.clear();
		for (int element : obsList) {
			obsHashMap.put(new Integer(element), plots[element]);

		}
		indication = -1; // clear indication

	}

	public int[] getObsList() {
		return obsList;
	}

	public DataSetForApps getDataSet() {
		return dataSet;
	}

	public StarPlotRenderer[] getPlots() {
		return plots;
	}

	public ScaleMethod getMethod() {
		return method;
	}

	public void setMethod(ScaleMethod method) {
		this.method = method;
		findSpikeLengths(varList);

	}
}
