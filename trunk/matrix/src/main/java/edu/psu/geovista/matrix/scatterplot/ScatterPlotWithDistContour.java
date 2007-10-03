package edu.psu.geovista.matrix.scatterplot;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import javax.swing.BorderFactory;

import edu.psu.geovista.common.data.StatisticsVectors;

public class ScatterPlotWithDistContour extends ScatterPlot {

	private double eigenAngle;
	private double meanX, meanY;
	private double stdEigenVector1, stdEigenVector2;
	private int meanXInt, meanYInt;
	private int ellipseMajorXInt, ellipseMajorYInt;
	private int ellipseMinorXInt, ellipseMinorYInt;
	private int ellipseMajorX2Int, ellipseMajorY2Int;
	private int ellipseMinorX2Int, ellipseMinorY2Int;
	private EigenValuesVectors eigen;
	private EigenValuesVectors eigenDisplay;
	private double[][] covariance;
	private double[] stdInt;

	public ScatterPlotWithDistContour() {
		super();
	}

	/**
	 * Set up data and axis for drawing the scatter plot.
	 */
	protected void initialize() {
		setRegressionClassName("edu.psu.geovista.geoviz.scatterplot.LinearRegression");
		this.dataArrayX = new DataArray(dataX);
		this.dataArrayY = new DataArray(dataY);
		this.conditionArray = new int[dataX.length];
		this.setBorder(BorderFactory.createLineBorder(Color.gray));
		if (axisOn) {
			xAxisExtents = (double[]) this.dataArrayX.getMaxMinCoorValue()
					.clone();
			yAxisExtents = (double[]) this.dataArrayY.getMaxMinCoorValue()
					.clone();
		} else {
			xAxisExtents[0] = dataArrayX.getExtent()[0];
			xAxisExtents[1] = dataArrayX.getExtent()[1];
			yAxisExtents[0] = dataArrayY.getExtent()[0];
			yAxisExtents[1] = dataArrayY.getExtent()[1];
		}

		size = this.getSize();

		// added for colors
		this.makeColors();
		// ywkim added for regression line
		this.setVisiblePlotLine(dataX, dataY, true);
		this.setUpDistributionContour();
		this.setupDataforDisplay();
		// this.makeBoundaries();

		this.setBackground(background);

	}

	private void setUpDistributionContour() {
		this.eigen = new EigenValuesVectors();
		this.eigenDisplay = new EigenValuesVectors();
		eigen.setData(dataX, dataY);
		meanX = StatisticsVectors.mean(dataX);
		meanY = StatisticsVectors.mean(dataY);
		// calculate standard deviations on eigen vectors
		double[] stds = this.eigen.getStd();
		stdEigenVector1 = stds[1];
		stdEigenVector2 = stds[0];

		this.covariance = this.eigen.getCovariance();

	}

	protected void setupDataforDisplay() {
		super.setupDataforDisplay();
		if (this.getSize().width == 0) {
			return;
		}
		// //get positions on screen
		double xScale;
		double yScale;
		double[][] covarianceDisplay = new double[2][2];
		if (this.attributeX.equals("pcincome")) {
			xScale = 0;
		}
		xScale = getScale(plotOriginX, plotEndX, xAxisExtents[0],
				xAxisExtents[1]);
		yScale = getScale(plotOriginY, plotEndY, yAxisExtents[0],
				yAxisExtents[1]);
		// covariance for the data displayed on screen
		covarianceDisplay[0][0] = this.covariance[0][0] * xScale * xScale;
		covarianceDisplay[0][1] = this.covariance[0][1] * xScale * yScale;
		covarianceDisplay[1][0] = this.covariance[1][0] * yScale * xScale;
		covarianceDisplay[1][1] = this.covariance[1][1] * yScale * yScale;

		this.eigenDisplay.setCovariance(covarianceDisplay);
		this.stdInt = this.eigenDisplay.getStd();
		this.eigenAngle = Math.PI
				- Math.atan(this.eigenDisplay.getEigenVectors()[1][1]
						/ this.eigenDisplay.getEigenVectors()[0][1]);
		// contour mean
		this.meanXInt = this.getValueScreen(this.meanX, xScale, plotOriginX,
				xAxisExtents[0]);
		this.meanYInt = this.getValueScreen(this.meanY, yScale, plotOriginY,
				yAxisExtents[0]);
		// the end points for two vectors at one std distance
		this.ellipseMajorXInt = (int) (this.meanXInt - this.stdInt[1]
				* this.eigenDisplay.getEigenVectors()[0][1]);
		this.ellipseMajorYInt = (int) (this.meanYInt - this.stdInt[1]
				* this.eigenDisplay.getEigenVectors()[1][1]);
		this.ellipseMinorXInt = (int) (this.meanXInt - this.stdInt[0]
				* this.eigenDisplay.getEigenVectors()[0][0]);
		this.ellipseMinorYInt = (int) (this.meanYInt - this.stdInt[0]
				* this.eigenDisplay.getEigenVectors()[1][0]);
		this.ellipseMajorX2Int = (int) (this.meanXInt + this.stdInt[1]
				* this.eigenDisplay.getEigenVectors()[0][1]);
		this.ellipseMajorY2Int = (int) (this.meanYInt + this.stdInt[1]
				* this.eigenDisplay.getEigenVectors()[1][1]);
		this.ellipseMinorX2Int = (int) (this.meanXInt + this.stdInt[0]
				* this.eigenDisplay.getEigenVectors()[0][0]);
		this.ellipseMinorY2Int = (int) (this.meanYInt + this.stdInt[0]
				* this.eigenDisplay.getEigenVectors()[1][0]);

	}

	/**
	 * Draw the scatter plot.
	 * 
	 * @param g
	 */
	public void paintComponent(Graphics g) {

		if (this.dataIndices == null)
			return;
		g.setColor(background);
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.setColor(foreground);
		this.paintBorder(g);

		if (axisOn) {
			drawAxis(g);
		}

		drawPlot(g);

		this.drawContour(g);
		this.drawEigenVectorsCross(g);

		Graphics2D g2 = (Graphics2D) g;
		if (exLabels != null && this.axisOn == true) {
			this.setToolTipText("");
			exLabels.paint(g2, getBounds());
		}

		// added by ywkim for regression line
		if (plotLine) {
			if (this.dataIndices[0] != this.dataIndices[1]
					&& this.regressionClass != null) {
				if (this.pointSelected == true && this.plotLineForSelections) {
					g.setColor(Color.gray);
				} else {

					this
							.drawCorrelationValue(g, this.correlation,
									this.rSquare);
					g.setColor(Color.red);
				}
				drawPlotLine(g, this.yStartPosition, this.yEndPosition);
			}
		}

		if (this.plotLineForSelections) {
			if (this.dataIndices[0] != this.dataIndices[1]
					&& this.regressionClass != null) {
				if (this.pointSelected == true) {
					this.drawCorrelationValue(g, this.correlationForSelections,
							this.rSquareForSelections);
					g.setColor(Color.red);
					drawPlotLine(g, this.yStartPositionSelections,
							this.yEndPositionSelections);
				}
			}

		}

		// add by Jin Chen for indication
		if (indiationId >= 0 && this.dataIndices[0] != this.dataIndices[1]) {
			this.drawIndication(g2, indiationId);
		}
	}

	private void drawContour(Graphics g) {
		if (this.dataIndices[0] == this.dataIndices[1] || this.stdInt == null)
			return;
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(Color.blue);

		g2d.rotate(-this.eigenAngle, meanXInt, meanYInt);
		g2d.drawOval(meanXInt - (int) stdInt[1], meanYInt - (int) stdInt[0],
				2 * (int) stdInt[1], 2 * (int) stdInt[0]);
		g2d.rotate(this.eigenAngle, meanXInt, meanYInt);
		// g.fillOval(this.meanXInt, this.meanYInt, 5, 5);

	}

	private void drawEigenVectorsCross(Graphics g) {
		if (this.dataIndices[0] == this.dataIndices[1])
			return;
		g.setColor(Color.black);
		g.drawLine(this.ellipseMajorXInt, this.ellipseMajorYInt,
				this.ellipseMajorX2Int, this.ellipseMajorY2Int);
		g.setColor(Color.gray);
		g.drawLine(this.ellipseMinorXInt, this.ellipseMinorYInt,
				this.ellipseMinorX2Int, this.ellipseMinorY2Int);
	}

	public Shape toShape() {
		Ellipse2D.Double e = new Ellipse2D.Double(this.meanXInt
				- this.stdEigenVector1, this.meanYInt - this.stdEigenVector2,
				this.stdEigenVector1 * 2, this.stdEigenVector2 * 2);
		AffineTransform at = AffineTransform.getRotateInstance(this.eigenAngle,
				this.meanXInt, this.meanYInt);
		return at.createTransformedShape(e);
	}

}
