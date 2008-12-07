package geovista.geoviz.scatterplot;

/**
 * Title: ScatterPlot
 * Description: construct a scatterplot
 * Copyright:    Copyright (c) 2001
 * Company: GeoVISTA Center
 * @author Xiping Dai
 * @author Jin Chen
 * 
 */
import java.awt.Color;
import java.awt.Graphics;

import geovista.symbolization.BivariateColorSymbolClassification;

/**
 * put your documentation comment here
 */
public class ScatterPlot extends ScatterPlotBasic

{
	public static final Color COLOR_NOSELECTED = new Color(204, 204, 204);
	transient protected ScatterPlot detailSP;
	transient boolean externalColor = false;

	/**
	 * put your documentation comment here
	 */
	public ScatterPlot() {
		super();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param String
	 *            attributeXName
	 * @param String
	 *            attributeYName
	 * @param double[]
	 *            dataX
	 * @param double[]
	 *            dataY
	 * @param boolean
	 *            axisOn
	 * @param boolean
	 *            plotLine
	 * @param double
	 *            slope
	 * @param double
	 *            intercept
	 */
	public ScatterPlot(Object[] dataObject, int[] dataIndices, boolean axisOn,
			Color c) {
		super();
		this.dataObject = dataObject;
		attributeArrayNames = (String[]) dataObject[0];
		int len = attributeArrayNames.length;
		if (dataObject[len + 1] == null) {
			observNames = null;
		} else {
			observNames = (String[]) dataObject[len + 1];
		}
		this.dataIndices = dataIndices;
		// convert Object array to double arrays.
		axisDataSetup();

		this.axisOn = axisOn;

		background = c;
		if (c == Color.black) {
			foreground = Color.white;
		} else {
			foreground = Color.black;
		}
		initialize();
	}

	@Override
	public void setColorArrayForObs(Color[] colorArray) {
		// this.colorArrayForObs = colorArray;
		if (colorArray != null) {
			pointColors = colorArray;
			externalColor = true;
		}
		if (pointColors == null) {
			logger.finest("In SP... pointColors null");
		}
		this.repaint();
	}

	public Color[] getColors() {
		return pointColors;
	}

	@Override
	public String getShortDiscription() {
		return "XYP";
	}

	/**
	 * Set up data and axis for drawing the scatter plot.
	 */
	@Override
	public void initialize() {
		super.initialize();
		// added for colors
		if (externalColor == false) {
			makeColors();
		}

	}

	@Override
	protected void drawSlections(Graphics g, Color[] colorNonSelected, int len) {
		logger.fine("in scatterplot, drawslections");
		long startTime = System.nanoTime();

		if (colorNonSelected != null && colorNonSelected.length != len) {
			return;
		}
		if (pointSelected == false) { // only draw original points.
			for (int i = 0; i < len; i++) {
				if ((exsint[i] <= plotEndX) && (exsint[i] >= plotOriginX)
						&& (whyint[i] <= plotOriginY)
						&& (whyint[i] >= plotEndY) && (conditionArray[i] > -1)) {
					if (colorNonSelected != null) {
						g.setColor(colorNonSelected[i]);
					}
					renderObs(g, i);
				}
			}
		} else { // draw original points and selected points.
			// according to the color mode, draw selected points and
			// non-selected points.
			if (selOriginalColorMode == false) {
				for (int i = 0; i < len; i++) {
					if ((exsint[i] <= plotEndX) && (exsint[i] >= plotOriginX)
							&& (whyint[i] <= plotOriginY)
							&& (whyint[i] >= plotEndY)
							&& (conditionArray[i] > -1)) {

						g.setColor(colorNonSelected[i]);
						renderObs(g, i);
					}
				}
				for (int i = 0; i < len; i++) {
					g.setColor(selectionColor);
					if ((exsint[i] <= plotEndX) && (exsint[i] >= plotOriginX)
							&& (whyint[i] <= plotOriginY)
							&& (whyint[i] >= plotEndY)
							&& (conditionArray[i] > -1)) {

						if (selections[i] == 1) {
							renderObs(g, i);
						}
					}
				}
			} else {
				for (int i = 0; i < len; i++) {
					if ((exsint[i] <= plotEndX) && (exsint[i] >= plotOriginX)
							&& (whyint[i] <= plotOriginY)
							&& (whyint[i] >= plotEndY)
							&& (conditionArray[i] > -1)) {
						/*
						 * if (colorNonSelected != null) { final Color c =
						 * colorNonSelected[i];
						 * 
						 * g.setColor(c); }
						 */
						g.setColor(new Color(61, 3, 87));// distinguish
						// selected and
						// non-selected
						g.drawOval(exsint[i] - 2, whyint[i] - 2, pointSize - 2,
								pointSize - 2);
					}
				}
				for (int i = 0; i < len; i++) {
					if ((exsint[i] <= plotEndX) && (exsint[i] >= plotOriginX)
							&& (whyint[i] <= plotOriginY)
							&& (whyint[i] >= plotEndY)
							&& (conditionArray[i] > -1)) {
						if (colorNonSelected != null) {
							g.setColor(colorNonSelected[i]);
						}
						if (selections[i] == 1) {

							renderObs(g, i);

						}
					}
				}
			}
		}
		long endTime = System.nanoTime();
		long diffTime = endTime - startTime;
		double seconds = (diffTime) / 1000000000d;
		logger.fine("scatterplot draw time = " + seconds);

	}

	// GeneralPath path = NGon.findNGon(150);

	private void renderObs(Graphics g, int i) {

		g.fillOval(exsint[i] - 2, whyint[i] - 2, pointSize, pointSize);
		// Graphics2D g2 = (Graphics2D) g;
		// g2.fill(path);
	}

	/**
	 * Sets colors for the current data.
	 */
	@Override
	public void setBivarColorClasser(
			BivariateColorSymbolClassification bivarColorClasser,
			boolean reverseColor) {
		this.bivarColorClasser = bivarColorClasser;
		makeColors();
		repaint();
	}

	@Override
	public BivariateColorSymbolClassification getBivarColorClasser() {
		return bivarColorClasser;
	}

}
