/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class GeoMapUni
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: GeoMapUni.java,v 1.12 2005/08/12 17:25:21 hardisty Exp $
 $Date: 2005/08/12 17:25:21 $
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

package edu.psu.geovista.matrix.map;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.common.data.DescriptiveStatistics;
import edu.psu.geovista.common.event.DataSetEvent;
import edu.psu.geovista.common.event.DataSetListener;
import edu.psu.geovista.common.event.IndicationEvent;
import edu.psu.geovista.common.event.IndicationListener;
import edu.psu.geovista.common.event.PaletteEvent;
import edu.psu.geovista.common.event.PaletteListener;
import edu.psu.geovista.common.event.SelectionEvent;
import edu.psu.geovista.common.event.SelectionListener;
import edu.psu.geovista.common.event.SpatialExtentEvent;
import edu.psu.geovista.common.event.SpatialExtentListener;
import edu.psu.geovista.coordination.CoordinationManager;
import edu.psu.geovista.geoviz.map.GeoMapUni;
import edu.psu.geovista.geoviz.map.SpatialWeights;
import edu.psu.geovista.geoviz.sample.GeoData48States;
import edu.psu.geovista.geoviz.shapefile.ShapeFileDataReader;
import edu.psu.geovista.geoviz.shapefile.ShapeFileProjection;
import edu.psu.geovista.geoviz.shapefile.ShapeFileToShape;
import edu.psu.geovista.matrix.scatterplot.SingleScatterPlot;
import edu.psu.geovista.symbolization.event.ColorClassifierEvent;
import edu.psu.geovista.symbolization.event.ColorClassifierListener;

/**
 * A Moran matrix has a whole series of plots tied together:
 * 
 * 1. map of original data map of moran's I Bivariate map of the two 2.
 * histogram of original data Histogram of same scatterplot of the two
 */
public class MoranMatrix extends JPanel implements SelectionListener,
		IndicationListener, DataSetListener, ColorClassifierListener,
		SpatialExtentListener, PaletteListener, TableModelListener {

	GeoMapUni map;
	SingleScatterPlot sp;
	DataSetForApps dataSetOriginal;
	DataSetForApps dataSetZ;
	DataSetForApps dataSetMoran;
	SpatialWeights spatialWeights;

	public MoranMatrix() {
		super();
		BoxLayout box = new BoxLayout(this, BoxLayout.X_AXIS);
		map = new GeoMapUni();
		sp = new SingleScatterPlot();
		this.setLayout(box);
		Dimension prefSize = new Dimension(300, 300);
		this.map.setPreferredSize(prefSize);
		this.sp.setPreferredSize(prefSize);
		LineBorder border = (LineBorder) BorderFactory
				.createLineBorder(Color.black);
		map.setBorder(border);
		sp.setBorder(border);

		this.add(map);
		this.add(sp);

	}

	public void selectionChanged(SelectionEvent e) {
		this.map.selectionChanged(e);
		this.sp.selectionChanged(e);

	}

	public void indicationChanged(IndicationEvent e) {
		this.map.indicationChanged(e);
		this.sp.indicationChanged(e);

	}

	public void dataSetChanged(DataSetEvent e) {
		this.dataSetOriginal = e.getDataSetForApps();
		this.spatialWeights = new SpatialWeights(this.dataSetOriginal
				.getShapeData());
		// first get the z scores....
		Object[] dataObjects = dataSetOriginal.getDataObjectOriginal();
		Object[] zDataObjects = new Object[dataObjects.length];
		String[] names = (String[]) dataObjects[0];
		String[] newNames = new String[names.length];
		System.arraycopy(names, 0, newNames, 0, names.length);
		for (int i = 1; i < dataObjects.length; i++) {
			Object thing = dataObjects[i];
			if (thing instanceof double[]) {
				double[] doublething = (double[]) thing;
				newNames[i - 1] = names[i - 1] + "_Z";
				zDataObjects[i] = this.calculateZScores(doublething);
			} else {
				zDataObjects[i] = dataObjects[i];
			}
		}
		zDataObjects[0] = newNames;
		this.dataSetZ = new DataSetForApps(zDataObjects);

		// now do the moran's

		Object[] moranDataObjects = new Object[dataObjects.length];

		String[] moranNames = new String[names.length];
		System.arraycopy(names, 0, moranNames, 0, names.length);
		for (int i = 1; i < dataObjects.length; i++) {
			Object thing = dataObjects[i];
			if (thing instanceof double[]) {
				double[] doublething = (double[]) thing;
				moranNames[i - 1] = names[i - 1] + "_M";
				moranDataObjects[i] = this.calculateMoranScores(doublething, i);
			} else {
				moranDataObjects[i] = dataObjects[i];
			}
		}
		moranDataObjects[0] = moranNames;
		this.dataSetMoran = new DataSetForApps(moranDataObjects);

		DataSetEvent e2 = new DataSetEvent(dataSetMoran, this);
		this.map.dataSetChanged(e2);
		this.sp.dataSetChanged(e2);
	}

	public void colorClassifierChanged(ColorClassifierEvent e) {
		// TODO Auto-generated method stub

	}

	public void spatialExtentChanged(SpatialExtentEvent e) {
		// TODO Auto-generated method stub

	}

	public void paletteChanged(PaletteEvent e) {
		// TODO Auto-generated method stub

	}

	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return
	 */
	double[] calculateZScores(double[] data) {
		double[] xData;

		xData = new double[data.length];
		System.arraycopy(data, 0, xData, 0, data.length);

		double meanX = DescriptiveStatistics.fineMean(xData);
		double stdDevX = DescriptiveStatistics.fineStdDev(xData, false);
		double[] fullXData = new double[xData.length];
		for (int i = 0; i < xData.length; i++) {
			fullXData[i] = (xData[i] - meanX) / stdDevX;
		}
		return fullXData;
	}

	/**
	 * @return
	 */
	double[] calculateMoranScores(double[] zData, int whichVar) {
		double[] moranData;
		moranData = new double[zData.length];
		System.arraycopy(zData, 0, moranData, 0, zData.length);
		double[] moranScores = new double[moranData.length];
		for (int i = 0; i < moranData.length; i++) {
			int[] iBors = this.spatialWeights.getNeighbors(i);
			double sumScore = 0;
			for (int j = 0; j < iBors.length; j++) {
				sumScore = sumScore
						+ this.dataSetZ.getValueAsDouble(whichVar, iBors[j]);
			}
			moranScores[i] = this.dataSetZ.getValueAsDouble(whichVar, i)
					* sumScore;
		}
		return moranScores;
	}

	public static void main(String[] args) {

		boolean useProj = false;
		boolean useResource = true;
		JFrame app = new JFrame("MoranMap Main Class: Why?");
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		app.getContentPane().setLayout(
				new BoxLayout(app.getContentPane(), BoxLayout.X_AXIS));

		app.pack();
		app.setVisible(true);

		MoranMap map2 = new MoranMap();
		app.getContentPane().add(map2);
		app.pack();
		app.setVisible(true);

		String fileName = "C:\\arcgis\\arcexe81\\Bin\\TemplateData\\USA\\counties.shp";
		fileName = "C:\\temp\\shapefiles\\intrstat.shp";
		fileName = "C:\\data\\geovista_data\\shapefiles\\larger_cities.shp";
		fileName = "C:\\data\\geovista_data\\shapefiles\\jin\\CompanyProdLL2000Def.shp";
		fileName = "C:\\data\\geovista_data\\Historical-Demographic\\census\\census80_90_00.shp";

		ShapeFileDataReader shpRead = new ShapeFileDataReader();
		shpRead.setFileName(fileName);
		CoordinationManager coord = new CoordinationManager();
		ShapeFileToShape shpToShape = new ShapeFileToShape();
		ShapeFileProjection shpProj = new ShapeFileProjection();
		GeoData48States stateData = new GeoData48States();
		// coord.addBean(map2);
		coord.addBean(shpToShape);

		if (useResource) {

			shpProj.setInputDataSetForApps(stateData.getDataForApps());
		} else {
			if (useProj) {
				stateData.addActionListener(shpProj);
				shpProj.setInputDataSet(shpRead.getDataSet());
			}
		}
		Object[] data = null;
		if (useProj) {
			data = shpProj.getOutputDataSet();
		} else {
			data = shpRead.getDataSet();
		}

		shpToShape.setInputDataSet(data);
		DataSetForApps dataSet = shpToShape.getOutputDataSetForApps();

		long startTime = System.currentTimeMillis();
		double total = 0;
		long count = 0;

		int nNumeric = dataSet.getNumberNumericAttributes();
		for (int i = 0; i < nNumeric; i++) {
			double[] zVals = DescriptiveStatistics.calculateZScores(dataSet
					.getNumericDataAsDouble(i));
			total = total + zVals[0];
			count++;
		}

		long endTime = System.currentTimeMillis();
		System.out.println("that took = " + (endTime - startTime));
		System.out.println(count);

	}

}
