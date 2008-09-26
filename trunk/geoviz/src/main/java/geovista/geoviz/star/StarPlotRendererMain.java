/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class StarPlotCanvas
 Copyright (c), 2003, Frank Hardisty
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: StarPlotRendererMain.java,v 1.2 2005/09/15 14:55:53 hardisty Exp $
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
