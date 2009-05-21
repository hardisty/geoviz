/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.geoviz.star;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * 
 * 
 * @author Frank Hardisty
 * 
 */
public class StarPlotExampleViewer extends JPanel implements ComponentListener {

	StarPlot[] plots;

	double[] values;

	int[] lengths;

	Color fillColor;

	Color outlineColor;

	double[] minVals;

	double[] maxVals;
	protected final static Logger logger = Logger
			.getLogger(StarPlotExampleViewer.class.getName());

	public StarPlotExampleViewer() {

		fillColor = Color.black;
		outlineColor = Color.white;
		addComponentListener(this);

	}

	// private void findLengths(double[] newVals) {
	// double prop = Double.NaN;
	// double range = Double.NaN;
	// double val = Double.NaN;
	// for (int i = 0; i < newVals.length; i++) {
	// values[i] = newVals[i];
	// range = this.maxVals[i] - this.minVals[i];
	// //make range zero-based
	// range = range - this.minVals[i];
	// //same with our val
	// val = newVals[i];
	// val = val - this.minVals[i];
	// prop = val / range;
	// lengths[i] = (int) (prop * 100d);
	//
	// }
	// }

	public StarPlot sp;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Rectangle targetArea = new Rectangle();
		targetArea.setBounds(getX(), getY(), getWidth(), getHeight());

		// sp.paintStar(g2, targetArea);

	}

	public void setSp(StarPlot sp) {
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
		StarPlotExampleViewer content = new StarPlotExampleViewer();
		content.setPreferredSize(new Dimension(400, 400));
		StarPlotExampleViewer content2 = new StarPlotExampleViewer();
		content2.setPreferredSize(new Dimension(400, 400));
		content.setBorder(new LineBorder(Color.black));
		app.getContentPane().add(content);

		StarPlot sp = new StarPlot();
		content.setSp(sp);
		content2.setSp(sp);
		app.pack();
		app.setVisible(true);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}