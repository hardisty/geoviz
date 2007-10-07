/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class StarPlotMain
 Copyright (c), 2003, Frank Hardisty
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: StarPlotMain.java,v 1.3 2006/02/17 17:21:23 hardisty Exp $
 $Date: 2006/02/17 17:21:23 $
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

import edu.psu.geovista.geoviz.sample.GeoData48States;
import edu.psu.geovista.geoviz.shapefile.ShapeFileDataReader;
import edu.psu.geovista.geoviz.shapefile.ShapeFileProjection;
import edu.psu.geovista.geoviz.shapefile.ShapeFileToShape;
import geovista.coordination.CoordinationManager;

/**
 * Main class for experimenting with starplots and maps that show starplots
 * 
 * 
 * @author Frank Hardisty
 * @version $Revision: 1.3 $
 */
public class StarPlotMain {

	public static void main(String[] args) {
		JFrame app = new JFrame();
		app.getContentPane().setLayout(new FlowLayout());
		StarPlot sp = new StarPlot();
		sp.setPreferredSize(new Dimension(400, 400));
		sp.setBorder(new LineBorder(Color.black));

		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		String fileName = "C:\\arcgis\\arcexe81\\Bin\\TemplateData\\USA\\counties.shp";
		fileName = "C:\\dc_tracts\\dc_tracts.shp";
		fileName = "C:\\geovista_old\\cartogram\\3states.shp";
		fileName = "C:\\geovista_old\\cartogram\\sc.shp";
		ShapeFileDataReader shpRead = new ShapeFileDataReader();
		// shpRead.setFileName(fileName);
		boolean useResource = false;

		CoordinationManager coord = new CoordinationManager();
		GeoData48States stateData = new GeoData48States();
		ShapeFileToShape shpToShape = new ShapeFileToShape();
		ShapeFileProjection shpProj = new ShapeFileProjection();
		StarPlotMap map = new StarPlotMap();
		map.setPreferredSize(new Dimension(400, 400));

		// app.getContentPane().add(sp);
		app.getContentPane().add(map);
		coord.addBean(map);

		// app.getspPane().add(lg);
		// coord.addBean(lg);
		// coord.addBean(sp);
		coord.addBean(shpProj);

		if (useResource) {
			shpProj.setInputDataSet(shpRead.convertShpToShape(stateData
					.getDataSet()));
		} else {
			shpRead.setFileName(fileName);
			Object[] dataSet = shpRead.getDataSet();

			shpProj.setInputDataSet(dataSet);
			Object[] outData = shpProj.getOutputDataSet();
			shpToShape.setInputDataSet(outData);
			// stateData.addActionListener(shpProj);
			// shpProj.setInputDataSet(shpRead.getDataSet());

		}
		app.pack();
		app.setVisible(true);
		// IndicationEvent indE = new IndicationEvent(sp, 0);
		// sp.indicationChanged(indE);
		map.zoomFullExtent();
	}

}
