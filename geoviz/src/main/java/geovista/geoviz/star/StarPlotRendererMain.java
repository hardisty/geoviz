/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.geoviz.star;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * 
 * 
 * 
 * @author Frank Hardisty
 * 
 */
public class StarPlotRendererMain extends JPanel implements ComponentListener {
	protected final static Logger logger = Logger
			.getLogger(StarPlotRendererMain.class.getName());
	StarPlotRenderer[] plots;
	double[] values;
	int[] lengths;
	Color fillColor;
	Color outlineColor;

	double[] minVals;
	double[] maxVals;

	public StarPlotRendererMain() {

		fillColor = Color.black;
		outlineColor = Color.white;
		addComponentListener(this);

	}

	public StarPlotRenderer sp;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("painting");
		}
		Rectangle targetArea = new Rectangle();
		targetArea.setBounds(getX(), getY(), getWidth(), getHeight());
		sp.setTargetArea(targetArea);
		sp.paintStar(g2);

	}

	public void setSp(StarPlotRenderer sp) {
		this.sp = sp;
	}

	// start component event handling
	// note: this class only listens to itself
	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		logger.finest("width = " + getWidth());
		logger.finest("height = " + getHeight());
		logger.finest("besos a frank");
	}

	public void componentShown(ComponentEvent e) {
		logger.finest("componentShown event from "
				+ e.getComponent().getClass().getName());
	}

	// end component handling
	public static void main(String[] args) {
		JFrame app = new JFrame();
		// app.getContentPane().setLayout(new FlowLayout());
		StarPlotRendererMain content = new StarPlotRendererMain();
		content.setPreferredSize(new Dimension(400, 400));
		// StarPlotRendererMain content2 = new StarPlotRendererMain();
		// content2.setPreferredSize(new Dimension(400, 400));
		content.setBorder(new LineBorder(Color.black));
		app.getContentPane().add(content);
		StarPlotRenderer sp = new StarPlotRenderer();
		int[] lengths = { 100, 10, 45, 22, 67, 100, 34, 87 };
		sp.setLengths(lengths);
		content.setSp(sp);
		// content2.setSp(sp);
		app.pack();
		app.setVisible(true);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
