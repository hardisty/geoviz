package edu.psu.geovista.matrix.scatterplot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * put your documentation comment here
 */
public class BivariateLegendWithScatterPlot extends ScatterPlotWithBackground {

	public static final String COMMAND_BOUNDARIES_MOVED = "cmdMov";
	public static final String COMMAND_BOUNDARIES_NUMBERCHANGED = "cmdChg";

	transient double xScale;
	transient double yScale;
	//transient protected Color[][] classColors;//commented out hardisty 1 may 07
	//transient protected ScatterPlotWithBackground detailSP;//commented out hardisty 1 may 07

	/**
	 * put your documentation comment here
	 */
	public BivariateLegendWithScatterPlot() {
		super();

	}

	/**
	 * put your documentation comment here
	 * 
	 * @param String
	 *            attributeX
	 * @param String
	 *            attributeY
	 * @param double[]
	 *            dataX
	 * @param double[]
	 *            dataY
	 * @param boolean
	 *            axisOn
	 */
	public BivariateLegendWithScatterPlot(Object[] dataObject,
			int[] dataIndices, boolean axisOn, Color c) {
		super();
		this.dataObject = dataObject;
		this.attributeArrays = (String[]) dataObject[0];
		int len = attributeArrays.length;
		if (dataObject[len + 1] == null) {
			this.observNames = null;
		} else {
			this.observNames = (String[]) dataObject[len + 1];
		}
		this.dataIndices = (int[]) dataIndices;
		// convert Object array to double arrays.
		axisDataSetup();
		// initialize();
		this.axisOn = axisOn;
		this.background = c;
		if (c == Color.black)
			this.foreground = Color.white;
		else
			this.foreground = Color.black;
		initialize();
	}

	public void setBoundaries(double[] boundariesX, double[] boundariesY) {
		this.xBoundaries = boundariesX;
		this.yBoundaries = boundariesY;
	}

	public void setClassColors(Color[][] classColors) {
		this.classColors = classColors;
	}

	public double[] getBoundariesX() {
		return this.xBoundaries;
	}

	public double[] getBoundariesY() {
		return this.yBoundaries;
	}

	public String getShortDiscription() {
		return "XYP";
	}

	/**
	 * Set up data and axis for drawing the scatter plot.
	 */
	protected void initialize() {
		this.setBackground(background);
		this.dataArrayX = new DataArray(dataX);
		this.dataArrayY = new DataArray(dataY);
		this.conditionArray = new int[dataX.length];
		this.setBorder(BorderFactory.createLineBorder(Color.gray));

		xAxisExtents[0] = dataArrayX.getExtent()[0];
		xAxisExtents[1] = dataArrayX.getExtent()[1];
		yAxisExtents[0] = dataArrayY.getExtent()[0];
		yAxisExtents[1] = dataArrayY.getExtent()[1];

		size = this.getSize();
		this.setupDataforDisplay();
	}

	/**
	 * Draw the scatter plot.
	 * 
	 * @param g
	 */
	public void paintComponent(Graphics g) {

		if (this.dataX == null)
			return;
		g.setColor(background);
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.setColor(foreground);
		this.paintBorder(g);

		if (axisOn) {
			// drawAxis(g);
			this.drawClassBoundaryLabels(g);
		}
		drawPlot(g);
		Graphics2D g2 = (Graphics2D) g;
		if (exLabels != null && this.axisOn == true) {
			this.setToolTipText("");
			exLabels.paint(g2, getBounds());
		}
	}

	/**
	 * Draw pot (points) on the screen.
	 * 
	 * @param g
	 */
	protected void drawPlot(Graphics g) {
		int plotWidth, plotHeight;
		plotWidth = (int) this.getWidth();
		plotHeight = (int) this.getHeight();
		int size;
		size = (plotWidth < plotHeight) ? plotWidth : plotHeight;
		this.pointSize = (size < 360) ? size / 60 : 6;
		this.pointSize = (this.pointSize < 3) ? 3 : this.pointSize;
		logger.finest("attribute equal? " +attributeX.equals(attributeY));

		int len = dataArrayX.length();
		// draw color background
		this.drawClassBackground(g);
		// draw the points
		g.setColor(this.foreground);
		for (int i = 0; i < len; i++) {
			if ((exsint[i] <= this.plotEndX) && (exsint[i] >= plotOriginX)
					&& (whyint[i] <= plotOriginY) && (whyint[i] >= plotEndY)
					&& (conditionArray[i] > -1)) {

				g.drawOval(exsint[i] - 1, whyint[i] - 1, pointSize, pointSize);
				// g.fillOval(exsint[i] - 2, whyint[i] - 2, pointSize,
				// pointSize);
			}
		}

	}

	protected void drawClassBackground(Graphics g) {
		// draw background in colors.
		if (this.classColors != null) {
			logger.finest("classer is not null");
			for (int i = 0; i < this.classColors.length; i++) {
				for (int j = 0; j < this.classColors[0].length; j++) {
					g.setColor(this.classColors[i][j]);
					g
							.fillRect(this.xBoundariesInt[i],
									this.yBoundariesInt[j + 1],
									this.xBoundariesInt[i + 1]
											- this.xBoundariesInt[i],
									this.yBoundariesInt[j]
											- this.yBoundariesInt[j + 1]);
				}
			}
		}
		// draw boundaries
		g.setColor(Color.white);
		for (int i = 0; i < this.xBoundariesInt.length; i++) {
			g.drawLine(this.xBoundariesInt[i], this.plotOriginY,
					this.xBoundariesInt[i], this.plotEndY);
		}
		for (int j = 0; j < this.yBoundariesInt.length; j++) {
			g.drawLine(this.plotOriginX, this.yBoundariesInt[j], this.plotEndX,
					this.yBoundariesInt[j]);
		}
	}

	protected void drawClassBoundaryLabels(Graphics g) {
		int plotWidth, plotHeight;
		plotWidth = (int) this.getWidth();
		plotHeight = (int) this.getHeight();
		g.setColor(foreground);
		// g.drawLine(plotOriginX, plotEndY, plotOriginX, plotOriginY);
		// g.drawLine(plotOriginX, plotOriginY, plotEndX, plotOriginY);
		// draw tick bars for scales on Y coordinate
		int fontSize;
		fontSize = (plotWidth < plotHeight) ? plotWidth : plotHeight;
		fontSize = ((int) (fontSize / 32) < 9) ? 9 : fontSize / 32;
		Font font = new Font("", Font.PLAIN, fontSize);
		g.setFont(font);

		g.setColor(this.foreground);
		String label;
		for (int i = 0; i < this.xBoundariesInt.length; i++) {
			// g.drawLine(this.xBoundariesInt[i], 0, this.xBoundariesInt[i],
			// this.getHeight());
			label = Double.toString(this.xBoundaries[i]);
			if (label.length() >= 8) {
				label = label.substring(0, 7);
			}
			Graphics2D g2d = (Graphics2D) g;
			g2d
					.rotate(Math.PI / 4, this.xBoundariesInt[i] - 2,
							plotOriginY + 8);
			g.drawString(label, this.xBoundariesInt[i] - 2, plotOriginY + 8);
			g2d.rotate(-Math.PI / 4, this.xBoundariesInt[i] - 2,
					plotOriginY + 8);
		}
		for (int j = 0; j < this.yBoundariesInt.length; j++) {
			// g.drawLine(0, this.yBoundariesInt[j], this.getWidth(),
			// this.yBoundariesInt[j]);
			label = Double.toString(this.yBoundaries[j]);
			if (label.length() >= 8) {
				label = label.substring(0, 7);
			}

			g.drawString(label, plotOriginX
					- (int) (plotWidth * AXISSPACEPORTION * 3 / 4),
					this.yBoundariesInt[j] + 2);
		}
	}

	protected double getValueFromScreenValue(int data, double scale, int min,
			double dataMin) {
		double value;
		value = ((double) (data - min)) / scale + dataMin;
		return value;
	}

	protected void setupDataforDisplay() {
		logger.finest("In setup data for display ..." +xAxisExtents[0]);
		this.setVisibleAxis(axisOn);
		if (dataArrayX == null)
			return;
		int len = dataArrayX.length();
		if (len != dataArrayY.length())
			return;
		// exsint = new int[len];
		// whyint = new int[len];
		// get positions on screen

		xScale = getScale(plotOriginX, plotEndX, xAxisExtents[0],
				xAxisExtents[1]);
		exsint = getValueScreen(dataX, xScale, plotOriginX, xAxisExtents[0]);
		yScale = getScale(plotOriginY, plotEndY, yAxisExtents[0],
				yAxisExtents[1]);
		whyint = getValueScreen(dataY, yScale, plotOriginY, yAxisExtents[0]);
		// get class boundaries' positions on screen
		if (this.xBoundaries != null && this.yBoundaries != null) {
			logger.finest("x and y boundaries are not null.");
			this.xBoundariesInt = new int[this.xBoundaries.length];
			this.yBoundariesInt = new int[this.yBoundaries.length];
			this.xBoundariesInt = getValueScreen(this.xBoundaries, xScale,
					plotOriginX, xAxisExtents[0]);
			this.yBoundariesInt = getValueScreen(this.yBoundaries, yScale,
					plotOriginY, yAxisExtents[0]);
		}
	}

	/**
	 * Begin the drawing of selection region (box).
	 * 
	 * @param e
	 */
	public void mousePressed(MouseEvent e) {
		if (dataX == null || dataY == null)
			return;

		if (e.isPopupTrigger())
			maybeShowPopup(e);
		// selRecords.clear();
		mouseX1 = e.getX();
		mouseY1 = e.getY();
	}

	/**
	 * Work with mouseDragged to draw a selection region (box) for selection.
	 * 
	 * @param e
	 */
	public void mouseReleased(MouseEvent e) {
		if (dataX == null)
			return;

		mouseX2 = e.getX();
		mouseY2 = e.getY();
		int v, v1;
		int lenX = this.xBoundariesInt.length;
		int lenY = this.yBoundariesInt.length;
		// move x boundaries
		int[] pixelValues = (int[]) this.xBoundariesInt.clone();
		double[] values = (double[]) this.xBoundaries.clone();
		for (int i = 0; i < lenX; i++) {
			if (lenX > this.xBoundariesInt.length) {
				return;
			}
			v = this.xBoundariesInt[i];
			if (Math.abs(mouseX1 - v) <= 5) {
				// merge boundaries
				if ((mouseX2 - mouseX1) > 0 && (i + 1) < lenX) {
					v1 = this.xBoundariesInt[i + 1];
					if (Math.abs(mouseX2 - v1) <= 5) {
						this.xBoundariesInt = new int[lenX - 1];
						this.xBoundaries = new double[lenX - 1];
						for (int r = 0; r < i; r++) {
							this.xBoundariesInt[r] = pixelValues[r];
							this.xBoundaries[r] = values[r];
						}
						for (int r = i; r < lenX - 1; r++) {
							this.xBoundariesInt[r] = pixelValues[r + 1];
							this.xBoundaries[r] = values[r + 1];
						}

						this
								.fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
					} else if (i != 0 && mouseX2 > this.xBoundariesInt[i - 1]
							&& mouseX2 < this.xBoundariesInt[i + 1]) {// avoid
																		// over
																		// data
																		// range
						this.xBoundariesInt[i] = mouseX2;
						this.xBoundaries[i] = this.getValueFromScreenValue(
								mouseX2, xScale, this.plotOriginX,
								this.xBoundaries[0]);
						this
								.fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_MOVED);
					}
				} else if ((mouseX2 - mouseX1) < 0 && (i - 1) >= 0) {
					v1 = this.xBoundariesInt[i - 1];
					if (Math.abs(mouseX2 - v1) <= 3) {
						this.xBoundariesInt = new int[lenX - 1];
						this.xBoundaries = new double[lenX - 1];
						for (int r = 0; r < i; r++) {
							this.xBoundariesInt[r] = pixelValues[r];
							this.xBoundaries[r] = values[r];
						}
						for (int r = i; r < lenX - 1; r++) {
							this.xBoundariesInt[r] = pixelValues[r + 1];
							this.xBoundaries[r] = values[r + 1];
						}

						this
								.fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
					} else if (i != lenX - 1
							&& mouseX2 > this.xBoundariesInt[i - 1]
							&& mouseX2 < this.xBoundariesInt[i + 1]) {
						this.xBoundariesInt[i] = mouseX2;
						this.xBoundaries[i] = this.getValueFromScreenValue(
								mouseX2, xScale, this.plotOriginX,
								this.xBoundaries[0]);
						this
								.fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_MOVED);
					}
				}
				// add new boundaries
				if (i == 0) {
					v1 = this.xBoundariesInt[1];
					if ((mouseX2 - mouseX1) > 0 && (mouseX2 < v1)) {
						this.xBoundariesInt = new int[lenX + 1];
						this.xBoundaries = new double[lenX + 1];

						this.xBoundariesInt[0] = pixelValues[0];
						this.xBoundaries[0] = values[0];

						this.xBoundariesInt[1] = mouseX2;
						this.xBoundaries[1] = this.getValueFromScreenValue(
								mouseX2, xScale, this.plotOriginX,
								this.xBoundaries[0]);

						for (int r = 2; r < lenX + 1; r++) {
							this.xBoundariesInt[r] = pixelValues[r - 1];
							this.xBoundaries[r] = values[r - 1];
						}

						this
								.fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
					}
					break;
				}
				if (i == lenX - 1) {
					v1 = this.xBoundariesInt[i - 1];
					if ((mouseX2 - mouseX1) < 0 && (mouseX2 > v1)) {
						this.xBoundariesInt = new int[lenX + 1];
						this.xBoundaries = new double[lenX + 1];
						for (int r = 0; r < lenX - 1; r++) {
							this.xBoundariesInt[r] = pixelValues[r];
							this.xBoundaries[r] = values[r];
						}

						this.xBoundariesInt[i] = mouseX2;
						this.xBoundaries[i] = this.getValueFromScreenValue(
								mouseX2, xScale, this.plotOriginX,
								this.xBoundaries[0]);

						this.xBoundariesInt[lenX] = pixelValues[lenX - 1];
						this.xBoundaries[lenX] = values[lenX - 1];

						this
								.fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
					}
					break;
				}
				// fire classnumber changed event

			}
		}

		// move y boundaries
		pixelValues = (int[]) this.yBoundariesInt.clone();
		values = (double[]) this.yBoundaries.clone();
		for (int i = 0; i < lenY; i++) {
			if (lenY > this.yBoundariesInt.length) {
				return;
			}

			v = this.yBoundariesInt[i];
			if (Math.abs(mouseY1 - v) <= 5) {
				// merge boundaries
				if ((mouseY2 - mouseY1) > 0 && (i + 1) < lenY) {
					v1 = this.yBoundariesInt[i + 1];
					if (Math.abs(mouseY2 - v1) <= 5) {
						this.yBoundariesInt = new int[lenY - 1];
						this.yBoundaries = new double[lenY - 1];
						for (int r = 0; r < i; r++) {
							this.yBoundariesInt[r] = pixelValues[r];
							this.yBoundaries[r] = values[r];
						}
						for (int r = i; r < lenY - 1; r++) {
							this.yBoundariesInt[r] = pixelValues[r + 1];
							this.yBoundaries[r] = values[r + 1];
						}

						this
								.fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
					} else if (i != 0 && mouseY2 < this.yBoundariesInt[i - 1]
							&& mouseY2 > this.yBoundariesInt[i + 1]) {
						this.yBoundariesInt[i] = mouseY2;
						this.yBoundaries[i] = this.getValueFromScreenValue(
								mouseY2, yScale, this.plotOriginY,
								this.yBoundaries[0]);
						this
								.fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_MOVED);
					}
				} else if ((mouseY2 - mouseY1) < 0 && (i - 1) >= 0) {
					v1 = this.yBoundariesInt[i - 1];
					if (Math.abs(mouseY2 - v1) <= 3) {
						this.yBoundariesInt = new int[lenY - 1];
						this.yBoundaries = new double[lenY - 1];
						for (int r = 0; r < i; r++) {
							this.yBoundariesInt[r] = pixelValues[r];
							this.yBoundaries[r] = values[r];
						}
						for (int r = i; r < lenY - 1; r++) {
							this.yBoundariesInt[r] = pixelValues[r + 1];
							this.yBoundaries[r] = values[r + 1];
						}

						this
								.fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
					} else if (i != lenY - 1
							&& mouseY2 < this.yBoundariesInt[i - 1]
							&& mouseY2 > this.yBoundariesInt[i + 1]) {
						this.yBoundariesInt[i] = mouseY2;
						this.yBoundaries[i] = this.getValueFromScreenValue(
								mouseY2, yScale, this.plotOriginY,
								this.yBoundaries[0]);
						this
								.fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_MOVED);
					}
				}
				// add new boundaries
				if (i == 0) {
					v1 = this.yBoundariesInt[1];
					if ((mouseY2 - mouseY1) < 0 && (mouseY2 > v1)) {
						this.yBoundariesInt = new int[lenY + 1];
						this.yBoundaries = new double[lenY + 1];

						this.yBoundariesInt[0] = pixelValues[0];
						this.yBoundaries[0] = values[0];

						this.yBoundariesInt[1] = mouseY2;
						this.yBoundaries[1] = this.getValueFromScreenValue(
								mouseY2, yScale, this.plotOriginY,
								this.yBoundaries[0]);

						for (int r = 2; r < lenY + 1; r++) {
							this.yBoundariesInt[r] = pixelValues[r - 1];
							this.yBoundaries[r] = values[r - 1];
						}

						this
								.fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
					}
					break;
				}
				if (i == lenY - 1) {
					v1 = this.yBoundariesInt[i - 1];
					if ((mouseY2 - mouseY1) > 0 && (mouseY2 < v1)) {
						this.yBoundariesInt = new int[lenY + 1];
						this.yBoundaries = new double[lenY + 1];
						for (int r = 0; r < lenY - 1; r++) {
							this.yBoundariesInt[r] = pixelValues[r];
							this.yBoundaries[r] = values[r];
						}

						this.yBoundariesInt[i] = mouseY2;
						this.yBoundaries[i] = this.getValueFromScreenValue(
								mouseY2, yScale, this.plotOriginY,
								this.yBoundaries[0]);

						this.yBoundariesInt[lenY] = pixelValues[lenY - 1];
						this.yBoundaries[lenY] = values[lenY - 1];

						this
								.fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
					}
					break;
				}
				// fire classnumber changed event

			}
		}

		this.repaint();

	}

	/**
	 * Mouse click for selecting or brushing points (observations).
	 * 
	 * @param e
	 */
	public void mouseClicked(MouseEvent e) {
		logger.finest("mouse clicked: ");
		int count = e.getClickCount();
		int[] mousePos = new int[2];
		mousePos[0] = e.getX();
		mousePos[1] = e.getY();
		// single click, select performed.
		if (dataX == null)
			return;
		if (count == 1) {
			if (Arrays.equals(dataX, dataY)) {
				for (int i = 0; i < dataX.length; i++) {
					if ((exsint[i] - 5 < mousePos[0])
							&& (mousePos[0] < exsint[i] + 5)
							&& (whyint[i] - 5 < mousePos[1])
							&& (mousePos[1] < whyint[i] + 5)
							&& (conditionArray[i] > -1)) {
						// selRecords.add(bigI);
						this.selections[i] = 1;
					}
				}
				// while (e.isShiftDown());
				fireActionPerformed(COMMAND_POINT_SELECTED);
			}
		}

		if (count == 2) // This is a double-click or triple...
		{
			int v1, v2;
			int lenY = this.yBoundariesInt.length;
			int lenX = this.xBoundariesInt.length;
			int[] pixelValues = (int[]) this.yBoundariesInt.clone();
			double[] values = (double[]) this.yBoundaries.clone();
			if ((mousePos[0] - this.xBoundariesInt[0]) < 50) {
				for (int i = 0; i < this.yBoundariesInt.length - 1; i++) {
					v1 = this.yBoundariesInt[i];
					v2 = this.yBoundariesInt[i + 1];
					if ((mousePos[1] - v1) < -5 && (v2 - mousePos[1]) < -5) {
						this.yBoundariesInt = new int[lenY + 1];
						this.yBoundaries = new double[lenY + 1];
						for (int j = 0; j <= i; j++) {
							this.yBoundariesInt[j] = pixelValues[j];
							this.yBoundaries[j] = values[j];
						}

						this.yBoundariesInt[i + 1] = mousePos[1];
						this.yBoundaries[i + 1] = this.getValueFromScreenValue(
								mousePos[1], yScale, this.plotOriginY,
								this.yBoundaries[0]);

						for (int j = i + 2; j < lenY + 1; j++) {
							this.yBoundariesInt[j] = pixelValues[j - 1];
							this.yBoundaries[j] = values[j - 1];
						}
						this
								.fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
						break;
					}
				}
			}
			if ((this.yBoundariesInt[0] - mousePos[1]) < 50) {
				pixelValues = (int[]) this.xBoundariesInt.clone();
				values = (double[]) this.xBoundaries.clone();

				for (int i = 0; i < this.xBoundariesInt.length - 1; i++) {
					v1 = this.xBoundariesInt[i];
					v2 = this.xBoundariesInt[i + 1];
					if ((mousePos[0] - v1) > 5 && (v2 - mousePos[0]) > 5) {
						this.xBoundariesInt = new int[lenX + 1];
						this.xBoundaries = new double[lenX + 1];
						for (int j = 0; j <= i; j++) {
							this.xBoundariesInt[j] = pixelValues[j];
							this.xBoundaries[j] = values[j];
						}

						this.xBoundariesInt[i + 1] = mousePos[0];
						this.xBoundaries[i + 1] = this.getValueFromScreenValue(
								mousePos[0], xScale, this.plotOriginX,
								this.xBoundaries[0]);

						for (int j = i + 2; j < lenX + 1; j++) {
							this.xBoundariesInt[j] = pixelValues[j - 1];
							this.xBoundaries[j] = values[j - 1];
						}
						this
								.fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
						break;
					}

				}
			}

		}
	}

	/**
	 * New data ranges setup dialog.
	 * 
	 * @param x
	 * @param y
	 */
	protected void showDialog(int x, int y) {
		JFrame dummyFrame = new JFrame();
		JDialog dialog = new JDialog(dummyFrame, "Data Range Configuer", true);
		JButton actionButton;
		JButton resetButton;
		dialog.setLocation(x, y);
		dialog.setSize(300, 150);
		dialog.getContentPane().setLayout(new GridLayout(5, 2));
		xAxisMinField.setText(Double.toString(xAxisExtents[0]));
		xAxisMaxField.setText(Double.toString(xAxisExtents[1]));
		yAxisMinField.setText(Double.toString(yAxisExtents[0]));
		yAxisMaxField.setText(Double.toString(yAxisExtents[1]));
		// create buttons for action
		actionButton = new JButton("OK");
		actionButton.addActionListener(new java.awt.event.ActionListener() {

			/**
			 * Button to set up new data ranges shown up in scatter plot.
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				try {
					actionButton_actionPerformed(e);
				} catch (Exception exception) {
				}
			}
		});
		resetButton = new JButton("Reset");
		resetButton.addActionListener(new java.awt.event.ActionListener() {

			/**
			 * put your documentation comment here
			 * 
			 * @param e
			 */
			public void actionPerformed(ActionEvent e) {
				resetButton_actionPerformed(e);
			}
		});
		dialog.getContentPane().add(new JLabel((this.attributeX + " Min")));
		dialog.getContentPane().add(xAxisMinField);
		dialog.getContentPane().add(new JLabel((this.attributeX + " Max")));
		dialog.getContentPane().add(xAxisMaxField);
		dialog.getContentPane().add(new JLabel((this.attributeY + " Min")));
		dialog.getContentPane().add(yAxisMinField);
		dialog.getContentPane().add(new JLabel((this.attributeY + " Max")));
		dialog.getContentPane().add(yAxisMaxField);
		dialog.getContentPane().add(actionButton);
		dialog.getContentPane().add(resetButton);
		dialog.setVisible(true);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param e
	 */
	protected void maybeShowPopup(MouseEvent e) {
		{
			getPopup().show(e.getComponent(), e.getX(), e.getY());
		}
	}

	/**
	 * Set up new data ranges to show.
	 * 
	 * @param e
	 */
	protected void actionButton_actionPerformed(ActionEvent e) {
		// get the input data from text field
		xAxisExtents[0] = Double.parseDouble(xAxisMinField.getText());
		xAxisExtents[1] = Double.parseDouble(xAxisMaxField.getText());
		yAxisExtents[0] = Double.parseDouble(yAxisMinField.getText());
		yAxisExtents[1] = Double.parseDouble(yAxisMaxField.getText());
		this.dataArrayX.setExtent(xAxisExtents);
		this.dataArrayY.setExtent(yAxisExtents);
		this.setupDataforDisplay();
		fireActionPerformed(COMMAND_DATARANGE_SET);
		logger.finest("ok, fire event.");
		repaint();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param e
	 */
	protected void resetButton_actionPerformed(ActionEvent e) {
		this.dataArrayX.setDataExtent();
		this.dataArrayY.setDataExtent();
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
		xAxisMinField.setText(Double.toString(xAxisExtents[0]));
		xAxisMaxField.setText(Double.toString(xAxisExtents[1]));
		yAxisMinField.setText(Double.toString(yAxisExtents[0]));
		yAxisMaxField.setText(Double.toString(yAxisExtents[1]));
		this.setupDataforDisplay();
		fireActionPerformed(COMMAND_DATARANGE_SET);
		repaint();
	}

}
