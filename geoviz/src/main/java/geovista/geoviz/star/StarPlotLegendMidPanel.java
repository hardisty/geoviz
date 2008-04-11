/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class StarPlotLegendMidPanel
 Copyright (c), 2003, Frank Hardisty
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: StarPlotLegendMidPanel.java,v 1.1 2005/02/13 03:26:27 hardisty Exp $
 $Date: 2005/02/13 03:26:27 $
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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Paint a legend for a StarPlot, along with the current plot.
 * 
 * 
 * @author Frank Hardisty
 * 
 */
public class StarPlotLegendMidPanel extends JPanel implements
		ComponentListener, MouseListener, MouseMotionListener {

	StarPlotRenderer sp;
	Rectangle plotLocation;
	double[] values;
	String[] variableNames;
	String observationName;
	int[] spikeLengths;
	Color fillColor;
	Color outlineColor;
	int plotWidth;
	int plotHeight;
	float penWidth;
	public static final int BUFFER = 5;
	public static final int TEXT_HEIGHT = 20;
	JLabel obsLabel;
	String obsName;
	final static Logger logger = Logger.getLogger(StarPlotLegend.class.getName());

	public StarPlotLegendMidPanel() {

		fillColor = Color.black;
		outlineColor = Color.white;
		this.setBackground(Color.white);
		this.addComponentListener(this);

		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		Dimension size = new Dimension(105, 125);
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		// this.setMaximumSize(size);
		this.setOpaque(false);
		// JPanel invisibleMiddle = new JPanel();
		// invisibleMiddle.setOpaque(false);
		// invisibleMiddle.setPreferredSize(new Dimension(105,105));
		// this.setLayout(new BorderLayout());
		// this.add(invisibleMiddle, BorderLayout.CENTER);
		// this.obsLabel = new JLabel();
		// this.obsLabel.setPreferredSize(new Dimension(105,20));
		// this.obsLabel.setOpaque(false);
		// this.obsLabel.setBorder(new LineBorder(Color.blue));
		// this.obsLabel.setHorizontalAlignment(JLabel.CENTER);
		// this.obsLabel.setVerticalAlignment(JLabel.CENTER);
		// this.obsLabel.setText("spank me");
		// this.obsLabel.setBackground(Color.pink);
		// this.obsName = "spank me!";
	}

	private void makeStarPlot() {
		sp = new StarPlotRenderer();

		plotLocation = new Rectangle();
		this.sp.setLengths(this.spikeLengths);
		findPlotLocation(plotLocation);

	}

	private void findPlotLocation(Rectangle plotLoc) {
		if (plotLoc == null) {
			return;
		}

		plotWidth = this.getWidth();
		plotHeight = this.getHeight();
		int plotMin = plotWidth;
		if (plotHeight < plotWidth) {
			plotMin = plotHeight;
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest(" plotmin = " + plotMin);
		}
		penWidth = (float) plotMin / 100f;
		int x = 0;
		int y = 0;

		plotLoc = new Rectangle();
		x = StarPlotLegendMidPanel.BUFFER;
		y = StarPlotLegendMidPanel.BUFFER;
		plotLoc.setBounds(x, y, plotWidth - BUFFER * 2, plotHeight - BUFFER * 2
				- TEXT_HEIGHT);
		sp.setTargetArea(plotLoc);

	}

	public void paintComponent(Graphics g) {
		if (sp == null || this.obsName == null) {
			return;
		}
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setStroke(new BasicStroke(penWidth));

		sp.paintStar(g2);
		g2.drawString(this.obsName, BUFFER * 2, this.getHeight() - BUFFER);
		g2.dispose();
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("in StarPlotLegendMidPanel, paint");
		}
	}

	// start component event handling
	// note: this class only listens to itself
	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
		// this.findPlotLocations(this.plotLocations);
		this.repaint();
	}

	public void componentResized(ComponentEvent e) {
		this.findPlotLocation(this.plotLocation);
		int lesserSize = this.getWidth();
		if (this.getHeight() < lesserSize) {
			lesserSize = this.getHeight();
		}
		Dimension square = new Dimension(lesserSize, lesserSize);
		this.setMaximumSize(square);
		this.setPreferredSize(square);
		this.repaint();

	}

	public void componentShown(ComponentEvent e) {
		logger.finest("componentShown event from "+ e.getComponent().getClass().getName());
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
		if (sp == null || this.plotWidth <= 0) {
			return;
		}

	}

	// end mouse events

	public int[] getSpikeLengths() {
		return spikeLengths;
	}

	public void setSpikeLengths(int[] spikeLengths) {
		this.spikeLengths = spikeLengths;
		this.makeStarPlot();
	}

	public double[] getValues() {
		return values;
	}

	public void setValues(double[] values) {
		this.values = values;
	}

	public Rectangle getPlotLocation() {
		return plotLocation;
	}

	public void setPlotLocation(Rectangle plotLocation) {
		this.plotLocation = plotLocation;
	}

	public float[] getXPoints() {
		this.findPlotLocation(new Rectangle());
		return this.sp.getRenderedXPoints();
	}

	public float[] getYPoints() {
		return this.sp.getRenderedYPoints();
	}

	public void setObsName(String obsName) {
		this.obsName = obsName;
	}

} // end class
