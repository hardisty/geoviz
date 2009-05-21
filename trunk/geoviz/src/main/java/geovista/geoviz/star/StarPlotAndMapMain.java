/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */
package geovista.geoviz.star;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import geovista.common.event.SubspaceEvent;
import geovista.coordination.CoordinationManager;
import geovista.geoviz.map.GeoMap;
import geovista.readers.shapefile.ShapeFileDataReader;
import geovista.readers.shapefile.ShapeFileProjection;
import geovista.readers.shapefile.ShapeFileToShape;

/**
 * Class to experiment with sending glyphs from the StarPlot to the map.
 * 
 * 
 * @author Frank Hardisty
 * 
 */
public class StarPlotAndMapMain {

	public static void main(String[] args) {
		JFrame app = new JFrame();
		app.getContentPane().setLayout(new FlowLayout());
		StarPlot content = new StarPlot();
		content.setPreferredSize(new Dimension(400, 400));
		StarPlotRendererMain content2 = new StarPlotRendererMain();
		content2.setPreferredSize(new Dimension(400, 400));
		content.setBorder(new LineBorder(Color.black));
		app.getContentPane().add(content);
		app.getContentPane().add(content2);

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
		// GeoData48States stateData = new GeoData48States();
		ShapeFileToShape shpToShape = new ShapeFileToShape();
		ShapeFileProjection shpProj = new ShapeFileProjection();
		GeoMap map = new GeoMap();
		// LinkGraph lg = new LinkGraph();

		app.getContentPane().add(map);
		coord.addBean(map);

		// app.getContentPane().add(lg);
		// coord.addBean(lg);
		coord.addBean(content);
		coord.addBean(shpProj);

		if (useResource) {
			shpProj.setInputDataSet(shpRead.getDataSet());// XXX will need
			// help
		} else {
			Object[] dataSet = shpRead.getDataSet();
			shpProj.setInputDataSet(dataSet);
			Object[] outData = shpProj.getOutputDataSet();
			shpToShape.setInputDataSet(outData);
			// stateData.addActionListener(shpProj);
			// shpProj.setInputDataSet(shpRead.getDataSet());

		}
		// manually fire off a subspace event
		int[] sub = { 10, 2, 5, 7, 9, 1, 3, 4 };
		SubspaceEvent e = new SubspaceEvent(map, sub);
		content.subspaceChanged(e);

	}

}
