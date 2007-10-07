/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class StarPlotLegend
 Copyright (c), 2003, Frank Hardisty
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: StarPlotLegend.java,v 1.2 2005/09/15 14:55:53 hardisty Exp $
 $Date: 2005/09/15 14:55:53 $
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
package geovista.geoviz.star;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * Paint a legend for a StarPlot, along with the current plot.
 * 
 * 
 * @author Frank Hardisty
 * @version $Revision: 1.2 $
 */
public class StarPlotLegend extends JPanel implements ComponentListener,
		MouseListener, MouseMotionListener {

	StarPlotLegendMidPanel midPanel;
	StarPlotLegendSidePanel rightPanel;
	StarPlotLegendSidePanel leftPanel;
	Rectangle plotLocation;
	double[] values;
	String[] variableNames;
	String observationName;
	int[] spikeLengths;
	Color fillColor;
	Color outlineColor;
	GeneralPath connectorLines;
	Stroke connectorStroke;
	int plotWidth;
	int plotHeight;
	float penWidth;
	boolean odd;
	int rightNum;
	int leftNum;
	final static Logger logger = Logger.getLogger(StarPlotLegend.class.getName());

	public StarPlotLegend() {

		fillColor = Color.black;
		outlineColor = Color.white;
		this.setBackground(Color.lightGray);
		this.addComponentListener(this);

		this.addMouseMotionListener(this);
		this.addMouseListener(this);

		this.midPanel = new StarPlotLegendMidPanel();

		this.rightPanel = new StarPlotLegendSidePanel();
		this.rightPanel.setAlignment(StarPlotLegendSidePanel.ALIGNMENT_LEFT);
		this.leftPanel = new StarPlotLegendSidePanel();
		this.leftPanel.setAlignment(StarPlotLegendSidePanel.ALIGNMENT_RIGHT);
		if (logger.isLoggable(Level.FINEST)) {
			this.midPanel.setBorder(new LineBorder(Color.green)); // for
			// debugging
			// layout
		}
		// trying border layout...
		this.setLayout(new BorderLayout());
		this.add(midPanel, BorderLayout.CENTER);
		this.add(leftPanel, BorderLayout.WEST);
		this.add(rightPanel, BorderLayout.EAST);
		// trying flow layout...
		// this.setLayout(new FlowLayout());
		//
		// this.add(leftPanel);
		// this.add(midPanel);
		// this.add(rightPanel);

		this.makeConnectorStroke();

	}

	private void makeConnectorStroke() {
		float width = 0.5f;
		int cap = BasicStroke.CAP_ROUND;
		int join = BasicStroke.JOIN_BEVEL;

		float miterLimit = 0f;
		float[] dashPattern = { 2f, 3f };
		float dashPhase = 5f;
		this.connectorStroke = new BasicStroke(width, cap, join, miterLimit,
				dashPattern, dashPhase);

	}

	private void makeStarPlot() {

		this.midPanel.setSpikeLengths(this.spikeLengths);

	}

	private void addDataSidePanels() {
		odd = false;
		if (this.spikeLengths.length % 2 == 1) {
			odd = true;
		}
		rightNum = this.spikeLengths.length / 2;
		leftNum = rightNum;
		if (odd) {
			rightNum++;
		}
		String[] rightStrings = new String[rightNum];
		String[] leftStrings = new String[leftNum];
		double[] rightVals = new double[rightNum];
		double[] leftVals = new double[leftNum];

		for (int i = 0; i < spikeLengths.length; i++) {
			if (i < rightNum) {
				rightStrings[i] = this.variableNames[i];
				rightVals[i] = this.values[i];
			} else {
				// for the left ones, need to count down not up, -1 for zero
				// based
				leftStrings[spikeLengths.length - i - 1] = this.variableNames[i];
				leftVals[spikeLengths.length - i - 1] = this.values[i];
			}
		}
		this.rightPanel.setValues(rightVals);
		this.leftPanel.setValues(leftVals);
		this.rightPanel.setVariableNames(rightStrings);
		this.leftPanel.setVariableNames(leftStrings);
		this.findConnectorLines();
	}

	public void findConnectorLines() {
		connectorLines = new GeneralPath();
		int len = this.spikeLengths.length;
		float[] xPoints = this.midPanel.getXPoints();
		float[] yPoints = this.midPanel.getYPoints();
		int midX = this.midPanel.getX();
		int rightX = this.rightPanel.getX();
		float x1, x2, y1, y2;

		for (int i = 0; i < len; i++) {

			if (i < rightNum) {
				Point2D.Float pt = this.rightPanel.getLabelLocation(i);
				if (pt == null) {
					return;
				}
				x1 = (float) pt.getX() + rightX;
				y1 = (float) pt.getY();
				connectorLines.moveTo(x1, y1);
				x2 = xPoints[i] + midX;
				y2 = yPoints[i];
				connectorLines.lineTo(x2, y2);

			} else {

				logger.finest("i = " + i);
				logger.finest("len - i - 1 = " + (len - i - 1));
				// for the left ones, need to count down not up, -1 for zero
				// based
				Point2D.Float pt = this.leftPanel.getLabelLocation(len - i - 1);
				if (pt == null) {
					return;
				}

				x1 = (float) pt.getX();
				y1 = (float) pt.getY();
				connectorLines.moveTo(x1, y1);

				x2 = xPoints[i] + midX;
				y2 = yPoints[i];
				connectorLines.lineTo(x2, y2);

			}

		}

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (midPanel == null || this.connectorLines == null) {
			return;
		}
		Graphics2D g2 = (Graphics2D) g.create();
		g.setColor(Color.white);
		Rectangle rect = this.findStarCircle();

		g.fillOval(rect.x, rect.y, rect.width, rect.height);
		g2.setColor(Color.black);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(this.connectorStroke);
		if (this.connectorLines != null) {
			findConnectorLines();
			g2.draw(this.connectorLines);
		}

		g2.dispose();
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("in StarPlotLegend, paint");
		}

	}

	private Rectangle findStarCircle() {
		Rectangle rect = new Rectangle();
		int x, y, width, height;
		int buff = StarPlotLegendMidPanel.BUFFER;
		x = midPanel.getX() + buff;
		y = midPanel.getY() + buff;
		width = midPanel.getWidth() - buff * 2;
		height = midPanel.getHeight() - buff * 2
				- StarPlotLegendMidPanel.TEXT_HEIGHT;
		if (width > height) {
			x = x + ((width - height) / 2);
			width = height;

		} else if (height > width) {
			y = y + ((height - width) / 2);
			height = width;
		}
		rect.setBounds(x, y, width, height);
		return rect;
	}

	// start component event handling
	// note: this class only listens to itself
	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {

		this.repaint();
	}

	public void componentResized(ComponentEvent e) {
		if (this.spikeLengths == null || this.rightPanel == null
				|| this.values == null || this.leftPanel== null) {
			return;
		}
		this.findConnectorLines();
		// trying to make the side panels take up remaining space, not the
		// middle panel
		// double midWidth = this.midPanel.getPreferredSize().getWidth();
		// int halfRest = (int)((midWidth - (double)this.getWidth())/2d);
		// Dimension remainder = new Dimension(halfRest,this.getHeight());
		//
		// this.leftPanel.setPreferredSize(remainder);
		// this.rightPanel.setPreferredSize(remainder);
		// this.revalidate();
		this.repaint();

	}

	public void componentShown(ComponentEvent e) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("componentShown event from "
					+ e.getComponent().getClass().getName());
		}
		this.repaint();
	}

	// end component handling

	// start mouse events
	/***************************************************************************
	 * Interface methods for mouse events *
	 **************************************************************************/
	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {

	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {
		if (midPanel == null || this.plotWidth <= 0) {
			return;
		}

	}

	public int[] getSpikeLengths() {
		return spikeLengths;
	}

	public void setSpikeLengths(int[] spikeLengths) {
		this.spikeLengths = spikeLengths;
		this.makeStarPlot();
		this.addDataSidePanels();
		this.repaint();
	}

	public double[] getValues() {
		return values;
	}

	public void setValues(double[] values) {
		this.values = values;
	}

	public String[] getVariableNames() {
		return variableNames;
	}

	public void setVariableNames(String[] variableNames) {
		this.variableNames = variableNames;
	}

	public void setObsName(String obsName) {
		this.midPanel.setObsName(obsName);
	}

	public void setStarFillColor(Color starColor) {
		if (starColor != null && this.midPanel.sp != null) {
			this.midPanel.sp.setFillColor(starColor);
		}
	}
} // end class
