/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */
package geovista.geoviz.star;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import geovista.common.event.SelectionEvent;
import geovista.common.event.SubspaceEvent;
import geovista.coordination.CoordinationManager;
import geovista.geoviz.map.GeoMap;
import geovista.readers.shapefile.ShapeFileDataReader;
import geovista.readers.shapefile.ShapeFileProjection;
import geovista.readers.shapefile.ShapeFileToShape;

/**
 * Paint a multi-dimensional "star display". We draw an n-"rayed" figure, with n
 * = the number of values set. The values are expected to range from 0 to 100.
 * Each ray is a line that extends from the origin outword, proportionately in
 * length to the value it represents. The end points of each ray are connected,
 * and the figure filled.
 * 
 * 
 * @author Frank Hardisty
 * 
 */
public class SelectionStarPlotMain {

	public static void main(String[] args) {
		JFrame app = new JFrame();
		app.getContentPane().setLayout(new FlowLayout());
		SelectionStarPlot content = new SelectionStarPlot();
		content.setPreferredSize(new Dimension(400, 400));
		StarPlotRendererMain content2 = new StarPlotRendererMain();
		content2.setPreferredSize(new Dimension(400, 400));
		content.setBorder(new LineBorder(Color.black));
		app.getContentPane().add(content);

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

		app.getContentPane().add(map);
		coord.addBean(map);

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

		}
		// manually fire off a subspace event
		int[] sub = { 10, 2, 5, 7, 9, 1, 3, 4 };
		SubspaceEvent e = new SubspaceEvent(map, sub);
		content.subspaceChanged(e);
		// manually fire off a selection event
		int[] selection = { 1, 2, 3, 4, 6, 9 };
		SelectionEvent eSel = new SelectionEvent(map, selection);
		content.selectionChanged(eSel);

	}

}
