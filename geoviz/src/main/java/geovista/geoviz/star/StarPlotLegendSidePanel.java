/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */
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
 * 
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
	final static Logger logger = Logger.getLogger(StarPlotLegendSidePanel.class
			.getName());

	public StarPlotLegendSidePanel() {

		setBackground(Color.lightGray);
		addComponentListener(this);

		addMouseMotionListener(this);
		addMouseListener(this);
		alignment = StarPlotLegendSidePanel.ALIGNMENT_LEFT;
		setFont(getFont().deriveFont(Font.PLAIN, 8f));
		labelHolder = new JPanel();

		// this.setLayout(new BorderLayout());
		// note to self, replace with spring layout
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		// sl = new SpringLayout();
		// this.setLayout(sl);

		this.add(labelHolder);
		BoxLayout lay = new BoxLayout(labelHolder, BoxLayout.Y_AXIS);
		labelHolder.setLayout(lay);

		Dimension size = new Dimension(125, 100);
		setMinimumSize(size);
		setPreferredSize(size);
		if (logger.isLoggable(Level.FINEST)) {
			labelHolder.setBorder(new LineBorder(Color.pink)); // for debugging
			// layout
			setBorder(new LineBorder(Color.blue)); // for debugging layout
		}
		setOpaque(false);

	}

	private void makeLabels() {
		int nVars = variableNames.length;
		labels = new JLabel[nVars];
		labelHolder.removeAll();
		if (nVars < 1) {
			return;// bail else div by zero
		}
		// these next fontsize values assume the height is fixed at 125
		if (nVars == 10) {
			setFont(getFont().deriveFont(Font.PLAIN, 8f));
		} else if (nVars == 9) {
			setFont(getFont().deriveFont(Font.PLAIN, 9f));
		} else if (nVars == 8) {
			setFont(getFont().deriveFont(Font.PLAIN, 9f));
		} else if (nVars == 7) {
			setFont(getFont().deriveFont(Font.PLAIN, 11f));
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
			setFont(getFont().deriveFont(Font.PLAIN, 12f));
		}

		labelHolder.setAlignmentX(alignment);
		int maxHeight = getHeight() / nVars;

		int maxWidth = getWidth();
		Dimension maxLabelSize = new Dimension(maxWidth, maxHeight);
		int align = SwingConstants.LEFT;
		if (alignment == StarPlotLegendSidePanel.ALIGNMENT_RIGHT) {
			align = SwingConstants.RIGHT;
		}

		for (int i = 0; i < nVars; i++) {
			labels[i] = new JLabel(variableNames[i] + " = " + values[i]);
			labels[i].setMaximumSize(maxLabelSize);
			labels[i].setFont(getFont());
			if (logger.isLoggable(Level.FINEST)) {
				labels[i].setBorder(new LineBorder(Color.white)); // for
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
			logger.finest("height = " + getHeight());
			logger.finest("wid " + getWidth());
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
		makeLabels();

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
		if (alignment == 0f) {
			left = true;
		}
		if (labels == null || labels.length <= label || labels[label] == null) {
			return null;
		}
		double intY = labels[label].getY();
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("label Y = " + intY);
		}
		intY = intY + labels[label].getHeight() / 2;
		intY = intY + labelHolder.getY();
		float y = (float) intY;
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest(" label " + label + ", return y = " + y);
		}
		float x = 0;
		if (left) {

			x = labels[label].getX();
		} else {

			x = (float) labels[label].getX() + labels[label].getWidth();
		}
		pt.setLocation(x, y);
		return pt;
	}

	// end mouse events

}