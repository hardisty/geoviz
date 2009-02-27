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

package geovista.matrix.map;

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
import javax.swing.JToolBar;

import geovista.common.data.DataSetForApps;
import geovista.geoviz.map.GeoCursors;
import geovista.geoviz.map.MapCanvas;
import geovista.geoviz.scatterplot.Histogram;
import geovista.matrix.MatrixElement;
import geovista.symbolization.BivariateColorSymbolClassification;

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
	private final Histogram histogram = new Histogram();

	public MapMatrixElement() {
		super();
		autofit = true;
		setBorder(BorderFactory.createLineBorder(Color.gray));

		// super.exLabels = null;
		// super.fisheyes = new geovista.common.ui.Fisheyes();
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
	@Override
	public void mouseClicked(MouseEvent e) {
		if ((e.getSource() == this) && (e.getClickCount() > 1)) { // This is a

			// }//end dataIndeces
		} // end if doubleclick
	} // end method

	public void setDataObject(DataSetForApps data) {
		dataSet = data;
		super.setDataSet(data);
		setHistogramData();

		// super.tickleColors();
	}

	public void setDataIndices(int[] dataIndices) {
		elementPosition = dataIndices.clone();
		// zero based or one based...
		// well...
		// i know...
		// let's keep changing our minds!
		// then we'll never get it straight!
		super.setCurrColorColumnX(elementPosition[0] - 1);
		super.setCurrColorColumnY(elementPosition[1] - 1);
		setHistogramData();
	}

	public int[] getElementPosition() {
		return elementPosition;
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
		return xAxisExtents;
	}

	public double[] getYAxisExtents() {
		return yAxisExtents;
	}

	public String getShortDiscription() {
		return "MAP";
	}

	@Override
	public void setSelectionColor(Color c) {
		selectionColor = c;
		super.setColorSelection(c);
	}

	@Override
	public Color getSelectionColor() {
		return selectionColor;
	}

	public void setMultipleSelectionColors(Color[] c) {
	}

	public BivariateColorSymbolClassification getBivarColorClasser() {
		return super.bivarColorClasser;
	}

	public void setColorArrayForObs(Color[] colorArrays) {
	}

	private void setHistogramData() {
		if (data == null || elementPosition == null) {
			return;
		}
		String[] atts = (String[]) data[0];

		attributeX = atts[elementPosition[0] - 1];
		int index = elementPosition[0];
		// XXX getNumericDataAsDouble has changed...
		dataX = dataSet.getNumericDataAsDouble(index - 1);
		double[] extent = histogram.getXAxisExtents();

		extent = new double[2];
		extent[0] = geovista.common.data.DescriptiveStatistics.min(dataX);
		extent[1] = geovista.common.data.DescriptiveStatistics.max(dataX);
		xAxisExtents = extent;

	}

	/**
	 * This method only paints the current contents of the drawingBuff.
	 * 
	 * @param g
	 */
	@Override
	public void paintComponent(Graphics g) {
		if (elementPosition == null) {
			return;
		}
		if (elementPosition[0] != elementPosition[1]) {
			super.paintComponent(g);
		}

		if (elementPosition == null) {
			return;
		}

		if (elementPosition[0] == elementPosition[1]) {
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

	@Override
	public void setIndication(int indication) {
		super.setIndication(indication);

	}
}
