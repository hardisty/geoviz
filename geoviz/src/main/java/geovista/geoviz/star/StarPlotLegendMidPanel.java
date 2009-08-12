/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */
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
	final static Logger logger = Logger.getLogger(StarPlotLegend.class
			.getName());

	public StarPlotLegendMidPanel() {

		fillColor = Color.black;
		outlineColor = Color.white;
		setBackground(Color.white);
		addComponentListener(this);

		addMouseMotionListener(this);
		addMouseListener(this);
		Dimension size = new Dimension(105, 125);
		setMinimumSize(size);
		setPreferredSize(size);
		// this.setMaximumSize(size);
		setOpaque(false);
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
		sp.setLengths(spikeLengths);
		findPlotLocation(plotLocation);

	}

	private void findPlotLocation(Rectangle plotLoc) {
		if (plotLoc == null) {
			return;
		}

		plotWidth = getWidth();
		plotHeight = getHeight();
		int plotMin = plotWidth;
		if (plotHeight < plotWidth) {
			plotMin = plotHeight;
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest(" plotmin = " + plotMin);
		}
		penWidth = plotMin / 100f;
		int x = 0;
		int y = 0;

		plotLoc = new Rectangle();
		x = StarPlotLegendMidPanel.BUFFER;
		y = StarPlotLegendMidPanel.BUFFER;
		plotLoc.setBounds(x, y, plotWidth - BUFFER * 2, plotHeight - BUFFER * 2
				- TEXT_HEIGHT);
		sp.setTargetArea(plotLoc);

	}

	@Override
	public void paintComponent(Graphics g) {
		if (sp == null || obsName == null || penWidth < 0) {
			return;
		}
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setStroke(new BasicStroke(penWidth));

		sp.paintStar(g2);
		g2.drawString(obsName, BUFFER * 2, getHeight() - BUFFER);
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
		findPlotLocation(plotLocation);
		int lesserSize = getWidth();
		if (getHeight() < lesserSize) {
			lesserSize = getHeight();
		}
		Dimension square = new Dimension(lesserSize, lesserSize);
		setMaximumSize(square);
		setPreferredSize(square);
		this.repaint();

	}

	public void componentShown(ComponentEvent e) {
		logger.finest("componentShown event from "
				+ e.getComponent().getClass().getName());
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
		if (sp == null || plotWidth <= 0) {
			return;
		}

	}

	// end mouse events

	public int[] getSpikeLengths() {
		return spikeLengths;
	}

	public void setSpikeLengths(int[] spikeLengths) {
		this.spikeLengths = spikeLengths;
		makeStarPlot();
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
		findPlotLocation(new Rectangle());
		return sp.getRenderedXPoints();
	}

	public float[] getYPoints() {
		return sp.getRenderedYPoints();
	}

	public void setObsName(String obsName) {
		this.obsName = obsName;
	}

}
