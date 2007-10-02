/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class MapMatrixElement
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: MapMatrixElement.java,v 1.14 2005/08/19 19:17:32 hardisty Exp $
 $Date: 2005/08/19 19:17:32 $
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

package edu.psu.geovista.app.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import edu.psu.geovista.app.matrix.MatrixElement;
import edu.psu.geovista.app.scatterplot.Histogram;
import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.coordination.CoordinationManager;
import edu.psu.geovista.symbolization.BivariateColorSymbolClassification;
import edu.psu.geovista.ui.cursor.GeoCursors;

public class MapMatrixElement extends MapCanvas implements MatrixElement {

	// the following are required for returning to matrix
	protected int[] elementPosition;
	private double[] xAxisExtents;
	private double[] yAxisExtents;
	transient private Object[] data;
	transient private DataSetForApps dataSet;
	String attributeX;
	double[] dataX;
	private JToolBar mapTools;
	transient private GeoCursors cursors;

	private Color selectionColor;
	private Histogram histogram = new Histogram();

	public MapMatrixElement() {
		super();
		this.autofit = true;
		this.setBorder(BorderFactory.createLineBorder(Color.gray));

		// super.exLabels = null;
		// super.fisheyes = new edu.psu.geovista.ui.Fisheyes();
		super.setMode(MapCanvas.MODE_SELECT);
	}

	public void setSelOriginalColorMode(boolean selOriginalColorMode) {
		// dummy because this is always true in the map. other matrix
		// elements???
	}

	/**
	 * pop up a detail map
	 * 
	 * @param e
	 */
	public void mouseClicked(MouseEvent e) {
		if ((e.getSource() == this) && (e.getClickCount() > 1)) { // This is a
																	// double-click
																	// or
																	// triple...

			// if (dataIndices[0] != dataIndices[1]) { //why this??? I guess we
			// don't want to pop up one from the
			// diagonal if we are a scatterplot
			GeoMap detailMap = new GeoMap();
			detailMap.setBackground(this.getBackground());
			detailMap.setDataSet(this.dataSet);
			detailMap.setBivarColorClasser(this.bivarColorClasser);
			detailMap
					.setSelectedObservations(this.getSelectedObservationsInt()); // need
																					// to
																					// do
																					// this
																					// here
																					// because

			// otherwise the selection won't "take"
			detailMap.setXVariable(super.currColorColumnX);
			detailMap.setXChooserMode(GeoMap.VARIABLE_CHOOSER_MODE_FIXED);
			detailMap.setYVariable(super.currColorColumnY);
			detailMap.setYChooserMode(GeoMap.VARIABLE_CHOOSER_MODE_FIXED);

			JFrame dummyFrame = new JFrame();
			JDialog detailMapFrame = new JDialog(dummyFrame, "Detail Map", true);

			detailMapFrame.setLocation(300, 300);
			detailMapFrame.setSize(300, 300);
			detailMapFrame.getContentPane().setLayout(new BorderLayout());
			detailMapFrame.getContentPane().add(detailMap, BorderLayout.CENTER);

			CoordinationManager cm = new CoordinationManager();
			cm.addBean(this);
			cm.addBean(detailMap);
			// XXX we should only do the following when we are a matrix element
			detailMapFrame.setVisible(true);

			// }//end dataIndeces
		} // end if doubleclick
	} // end method

	public void setDataObject(DataSetForApps data) {
		this.dataSet = data;
		super.setDataSet(data);
		this.setHistogramData();

		// super.tickleColors();
	}
	
	
	

	public void setElementPosition(int[] dataIndices) {
		this.elementPosition = (int[]) dataIndices.clone();
		// zero based or one based...
		// well...
		// i know...
		// let's keep changing our minds!
		// then we'll never get it straight!
		super.setCurrColorColumnX(this.elementPosition[0] - 1);
		super.setCurrColorColumnY(this.elementPosition[1] - 1);
		this.setHistogramData();
	}

	public int[] getElementPosition() {
		return this.elementPosition;
	}

	// For axes of scatter plot.
	// a noop for this class
	public void setAxisOn(boolean axisOn) {
	}

	// public void setConditionArray (int[] conditionArray){ }
	// Set min and max for axes. xAxisExtents[0] = min, [1] = max.
	public void setXAxisExtents(double[] xAxisExtents) {
		// histogram.setXAxisExtents(xAxisExtents);
	}

	public void setYAxisExtents(double[] yAxisExtents) {

	}

	public double[] getXAxisExtents() {
		return this.xAxisExtents;
	}

	public double[] getYAxisExtents() {
		return this.yAxisExtents;
	}

	public String getShortDiscription() {
		return "MAP";
	}

	public void setSelectionColor(Color c) {
		this.selectionColor = c;
		super.setColorSelection(c);
	}

	public Color getSelectionColor() {
		return this.selectionColor;
	}

	public void setMultipleSelectionColors(Color[] c) {
	}

	public BivariateColorSymbolClassification getBivarColorClasser() {
		return super.bivarColorClasser;
	}

	public void setColorArrayForObs(Color[] colorArrays) {
	}

	private void setHistogramData() {
		if (this.data == null || this.elementPosition == null) {
			return;
		}
		String[] atts = (String[]) this.data[0];

		this.attributeX = atts[elementPosition[0] - 1];
		int index = this.elementPosition[0];
		// XXX getNumericDataAsDouble has changed...
		dataX = dataSet.getNumericDataAsDouble(index - 1);
		double[] extent = histogram.getXAxisExtents();

		extent = new double[2];
		extent[0] = edu.psu.geovista.common.data.DescriptiveStatistics.min(dataX);
		extent[1] = edu.psu.geovista.common.data.DescriptiveStatistics.max(dataX);
		this.xAxisExtents = extent;

	}

	/**
	 * This method only paints the current contents of the drawingBuff.
	 * 
	 * @param g
	 */
	public void paintComponent(Graphics g) {
		if (this.elementPosition == null) {
			return;
		}
		if (this.elementPosition[0] != this.elementPosition[1]) {
			super.paintComponent(g);
		}

		if (this.elementPosition == null) {
			return;
		}

		if (this.elementPosition[0] == this.elementPosition[1]) {
			super.paintComponent(g);
			// this.drawHistogram(g);

		}
	}

	public JToolBar getTools() {
		if (mapTools == null) {
			mapTools = new JToolBar();
			JButton button = null;
			Class cl = this.getClass();
			URL urlGif = null;
			Dimension buttDim = new Dimension(20, 20);

			// first button
			try {
				urlGif = cl.getResource("resources/select16.gif");
				button = new JButton(new ImageIcon(urlGif));
				button.setPreferredSize(buttDim);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			button.setToolTipText("Enter selection mode");
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					MapMatrixElement.this.setCursor(cursors
							.getCursor(GeoCursors.CURSOR_ARROW_SELECT));
					MapMatrixElement.this.setMode(MapCanvas.MODE_SELECT);
				}
			});
			mapTools.add(button);

			mapTools.addSeparator();

			// second button
			try {
				urlGif = cl.getResource("resources/ZoomIn16.gif");
				button = new JButton(new ImageIcon(urlGif));
				button.setPreferredSize(buttDim);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			button.setToolTipText("Enter zoom in mode");
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					MapMatrixElement.this.setCursor(cursors
							.getCursor(GeoCursors.CURSOR_ARROW_ZOOM_IN));
					MapMatrixElement.this.setMode(MapCanvas.MODE_ZOOM_IN);

					// GeoMap.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			});
			mapTools.add(button);

			// third button
			try {
				urlGif = cl.getResource("resources/ZoomOut16.gif");
				button = new JButton(new ImageIcon(urlGif));
				button.setPreferredSize(buttDim);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			button.setToolTipText("Enter zoom out mode");
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					MapMatrixElement.this.setMode(MapCanvas.MODE_ZOOM_OUT);
					MapMatrixElement.this.setCursor(cursors
							.getCursor(GeoCursors.CURSOR_ARROW_ZOOM_OUT));
				}
			});
			mapTools.add(button);

			// fourth button
			try {
				urlGif = cl.getResource("resources/Home16.gif");
				button = new JButton(new ImageIcon(urlGif));
				button.setPreferredSize(buttDim);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			button.setToolTipText("Zoom to full extent");
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					MapMatrixElement.this.zoomFullExtent();
				}
			});
			mapTools.add(button);

			// fifth button
			try {
				urlGif = cl.getResource("resources/pan16.gif");
				button = new JButton(new ImageIcon(urlGif));
				button.setPreferredSize(buttDim);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			button.setToolTipText("Enter pan mode");
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					MapMatrixElement.this.setCursor(cursors
							.getCursor(GeoCursors.CURSOR_ARROW_PAN));
					MapMatrixElement.this.setMode(MapCanvas.MODE_PAN);
				}
			});
			mapTools.add(button);
			// sixth button
			try {
				urlGif = cl.getResource("resources/excentric16.gif");
				button = new JButton(new ImageIcon(urlGif));
				button.setPreferredSize(buttDim);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			button.setToolTipText("Excentric Labels");
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					MapMatrixElement.this.setCursor(cursors
							.getCursor(GeoCursors.CURSOR_ARROW_PAN));
					MapMatrixElement.this.setMode(MapCanvas.MODE_EXCENTRIC);
				}
			});
			mapTools.add(button);

			// seventh button
			try {
				urlGif = cl.getResource("resources/fisheye16.gif");
				button = new JButton(new ImageIcon(urlGif));
				button.setPreferredSize(buttDim);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			button.setToolTipText("Fisheye Lens");
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					MapMatrixElement.this.setCursor(cursors
							.getCursor(GeoCursors.CURSOR_ARROW_PAN));
					MapMatrixElement.this.setMode(MapCanvas.MODE_FISHEYE);
				}
			});
			mapTools.add(button);
			// eighth button
			try {
				urlGif = cl.getResource("resources/magnifying16.gif");
				button = new JButton(new ImageIcon(urlGif));
				button.setPreferredSize(buttDim);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			button.setToolTipText("Magnifiying Lens");
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					MapMatrixElement.this.setCursor(cursors
							.getCursor(GeoCursors.CURSOR_ARROW_PAN));
					MapMatrixElement.this.setMode(MapCanvas.MODE_PAN);
				}
			});

			mapTools.add(button);
		}
		return mapTools;
	}

	public void setBivarColorClasser(
			BivariateColorSymbolClassification bivarColorClasser,
			boolean reverseColor) {
		// TODO Auto-generated method stub

	}
}
