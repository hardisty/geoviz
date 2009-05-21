/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.geoviz.star;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import geovista.coordination.CoordinationManager;
import geovista.readers.shapefile.ShapeFileDataReader;
import geovista.readers.shapefile.ShapeFileProjection;
import geovista.readers.shapefile.ShapeFileToShape;

/**
 * Main class for experimenting with starplots and maps that show starplots
 * 
 * 
 * @author Frank Hardisty
 * 
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
		// GeoData48States stateData = new GeoData48States();
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
			shpProj.setInputDataSet(shpRead.getDataSet());// XXX will need
			// help
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

	}

}
