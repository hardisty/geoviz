/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class StarPlotLegendSidePanel
 Copyright (c), 2003, Frank Hardisty
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: StarPlotLegendSidePanel.java,v 1.1 2005/02/13 03:26:27 hardisty Exp $
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 * Paint a legend for a StarPlot, along with the current plot.
 * 
 * 
 * @author Frank Hardisty
 * @version $Revision: 1.1 $
 */
public class StarPlotLegendSidePanel extends JPanel implements
		ComponentListener, MouseListener, MouseMotionListener {

	public static final float ALIGNMENT_LEFT = 0;
	public static final float ALIGNMENT_RIGHT = 1;
	private float alignment;
	JLabel[] labels;
	JPanel labelHolder;
	SpringLayout sl;
	double[] values;
	String[] variableNames;
	final static Logger logger = Logger.getLogger(StarPlotLegendSidePanel.class.getName());

	public StarPlotLegendSidePanel() {

		this.setBackground(Color.lightGray);
		this.addComponentListener(this);

		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.alignment = StarPlotLegendSidePanel.ALIGNMENT_LEFT;
		this.setFont(this.getFont().deriveFont(Font.PLAIN, 8f));
		labelHolder = new JPanel();

		// this.setLayout(new BorderLayout());
		// note to self, replace with spring layout
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		// sl = new SpringLayout();
		// this.setLayout(sl);

		this.add(labelHolder);
		BoxLayout lay = new BoxLayout(labelHolder, BoxLayout.Y_AXIS);
		labelHolder.setLayout(lay);

		Dimension size = new Dimension(125, 100);
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		if (logger.isLoggable(Level.FINEST)) {
			labelHolder.setBorder(new LineBorder(Color.pink)); // for debugging
																// layout
			this.setBorder(new LineBorder(Color.blue)); // for debugging layout
		}
		this.setOpaque(false);

	}

	private void makeLabels() {
		int nVars = variableNames.length;
		this.labels = new JLabel[nVars];
		labelHolder.removeAll();
		if (nVars < 1) {
			return;// bail else div by zero
		}
		// these next fontsize values assume the height is fixed at 125
		if (nVars == 10) {
			this.setFont(this.getFont().deriveFont(Font.PLAIN, 8f));
		} else if (nVars == 9) {
			this.setFont(this.getFont().deriveFont(Font.PLAIN, 9f));
		} else if (nVars == 8) {
			this.setFont(this.getFont().deriveFont(Font.PLAIN, 9f));
		} else if (nVars == 7) {
			this.setFont(this.getFont().deriveFont(Font.PLAIN, 11f));
		}
		// else if (nVars == 6) {
		// this.setFont(this.getFont().deriveFont(Font.PLAIN, 12f));
		// }
		// else if (nVars == 5) {
		// this.setFont(this.getFont().deriveFont(Font.PLAIN, 13f));
		// }else if (nVars == 4){
		// this.setFont(this.getFont().deriveFont(Font.PLAIN, 14f));
		// }

		else {
			this.setFont(this.getFont().deriveFont(Font.PLAIN, 12f));
		}

		labelHolder.setAlignmentX(this.alignment);
		int maxHeight = this.getHeight() / nVars;

		int maxWidth = this.getWidth();
		Dimension maxLabelSize = new Dimension(maxWidth, maxHeight);
		int align = SwingConstants.LEFT;
		if (this.alignment == StarPlotLegendSidePanel.ALIGNMENT_RIGHT) {
			align = SwingConstants.RIGHT;
		}

		for (int i = 0; i < nVars; i++) {
			this.labels[i] = new JLabel(this.variableNames[i] + " = "
					+ this.values[i]);
			this.labels[i].setMaximumSize(maxLabelSize);
			this.labels[i].setFont(this.getFont());
			if (logger.isLoggable(Level.FINEST)) {
				this.labels[i].setBorder(new LineBorder(Color.white)); // for
																		// debugging
																		// layout
			}
			labels[i].setHorizontalAlignment(align);
			labelHolder.add(labels[i]);
		}
		labelHolder.revalidate();

	}

	// start component event handling
	// note: this class only listens to itself
	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
		this.repaint();
	}

	public void componentResized(ComponentEvent e) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("height = " + this.getHeight());
			logger.finest("wid " + this.getWidth());
		}
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
		this.makeLabels();

	}

	public void alignLabelsLeft(boolean left) {


	}

	public float getAlignment() {
		return alignment;
	}

	public void setAlignment(float alignment) {
		this.alignment = alignment;
	}

	public Point2D.Float getLabelLocation(int label) {
		Point2D.Float pt = new Point2D.Float();

		boolean left = false;
		if (this.alignment == 0f) {
			left = true;
		}
		if (this.labels == null || this.labels.length <= label
				|| this.labels[label] == null) {
			return null;
		}
		double intY = this.labels[label].getY();
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("label Y = " + intY);
		}
		intY = intY + this.labels[label].getHeight() / 2;
		intY = intY + this.labelHolder.getY();
		float y = (float) intY;
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest(" label " + label + ", return y = " + y);
		}
		float x = 0;
		if (left) {

			x = (float) this.labels[label].getX();
		} else {

			x = (float) labels[label].getX() + labels[label].getWidth();
		}
		pt.setLocation(x, y);
		return pt;
	}

	// end mouse events

} // end class
