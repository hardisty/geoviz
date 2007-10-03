/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class StarPlotCanvas
 Copyright (c), 2003, Frank Hardisty
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: StarPlotCanvasMain.java,v 1.1 2005/02/13 03:26:27 hardisty Exp $
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
package edu.psu.geovista.geoviz.star;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import edu.psu.geovista.coordination.CoordinationManager;
import edu.psu.geovista.data.shapefile.ShapeFileDataReader;
import edu.psu.geovista.data.shapefile.ShapeFileProjection;
import edu.psu.geovista.data.shapefile.ShapeFileToShape;
import edu.psu.geovista.geoviz.sample.GeoData48States;

/**
 * Paint a multi-dimensional "star display". We draw an n-"rayed" figure, with n =
 * the number of values set. The values are expected to range from 0 to 100.
 * Each ray is a line that extends from the origin outword, proportionately in
 * length to the value it represents. The end points of each ray are connected,
 * and the figure filled.
 * 
 * 
 * @author Frank Hardisty
 * @version $Revision: 1.1 $
 */
public class StarPlotCanvasMain {

	public static void main(String[] args) {
		JFrame app = new JFrame();
		app.getContentPane().setLayout(new FlowLayout());
		StarPlotCanvas content = new StarPlotCanvas();
		content.setPreferredSize(new Dimension(400, 400));
		StarPlotRendererMain content2 = new StarPlotRendererMain();
		content2.setPreferredSize(new Dimension(400, 400));
		content.setBorder(new LineBorder(Color.black));
		app.getContentPane().add(content);
		StarPlotRenderer sp = new StarPlotRenderer();
		int[] lengths = { 100, 10, 45, 22, 67, 89, 34, 87 };
		sp.setLengths(lengths);

		content2.setSp(sp);
		app.pack();
		app.setVisible(true);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		String fileName = "C:\\arcgis\\arcexe81\\Bin\\TemplateData\\USA\\counties.shp";
		fileName = "C:\\dc_tracts\\dc_tracts.shp";
		ShapeFileDataReader shpRead = new ShapeFileDataReader();
		shpRead.setFileName(fileName);
		boolean useResource = true;

		CoordinationManager coord = new CoordinationManager();
		GeoData48States stateData = new GeoData48States();
		ShapeFileToShape shpToShape = new ShapeFileToShape();
		ShapeFileProjection shpProj = new ShapeFileProjection();
		// GeoMap map = new GeoMap();
		// app.getContentPane().add(map);
		// coord.addBean(map);
		coord.addBean(content);
		coord.addBean(shpProj);

		if (useResource) {
			shpProj.setInputDataSet(shpRead.convertShpToShape(stateData
					.getDataSet()));
		} else {
			Object[] dataSet = shpRead.getDataSet();

			// ShapeFileDataReader reader = new ShapeFileDataReader();
			shpProj.setInputDataSet(dataSet);
			Object[] outData = shpProj.getOutputDataSet();
			shpToShape.setInputDataSet(outData);
			// stateData.addActionListener(shpProj);
			// shpProj.setInputDataSet(shpRead.getDataSet());

		}

	}

}
