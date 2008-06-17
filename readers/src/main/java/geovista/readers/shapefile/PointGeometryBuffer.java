/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.readers.shapefile;

import java.awt.Shape;
import java.io.FileInputStream;
import java.util.logging.Logger;

import javax.swing.JFrame;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import geovista.common.data.DataSetForApps;
import geovista.geoviz.map.GeoMap;
import geovista.readers.csv.GeogCSVReader;

/**
 * Creates a ring buffer based on input points and radii
 * 
 */
public class PointGeometryBuffer {
	protected final static Logger logger = Logger
			.getLogger(PointGeometryBuffer.class.getName());
	public static final String COMMAND_DATA_SET_MADE = "dataMade";

	public PointGeometryBuffer() {
		super();

	}

	private Geometry[] makeBuffers(Geometry[] inputGeoms, double[] distance) {
		Geometry[] resultGeoms = new Geometry[inputGeoms.length];
		for (int i = 0; i < inputGeoms.length; i++) {
			resultGeoms[i] = inputGeoms[i].buffer(distance[i], 50);
		}

		return resultGeoms;
	}

	private DataSetForApps makeBufferDataSet(double[] xVals, double[] yVals,
			double[] distance) {
		GeometryFactory fact = new GeometryFactory();

		Geometry[] geoms = new Geometry[xVals.length];
		for (int i = 0; i < xVals.length; i++) {
			Coordinate coord = new Coordinate(xVals[i], yVals[i]);

			geoms[i] = fact.createPoint(coord);

		}
		Geometry[] bufferGeoms = makeBuffers(geoms, distance);
		Shape[] shapes = ShapeFileDataReader.geomsToShapes(bufferGeoms);
		String[] names = { "X", "Y", "Buffer" };
		Object[] data = { names, xVals, yVals, distance, shapes };
		return new DataSetForApps(data);
	}

	public DataSetForApps makeBuffers(String pointFileName) {
		DataSetForApps shpData = null;

		try {

			GeogCSVReader csv = new GeogCSVReader();
			FileInputStream inStream = new FileInputStream(pointFileName);
			Object[] dbData = csv.readFile(inStream);
			double[] xVals = (double[]) dbData[1];
			double[] yVals = (double[]) dbData[2];
			double[] bufferVals = (double[]) dbData[3];

			shpData = makeBufferDataSet(xVals, yVals, bufferVals);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return shpData;
	}

	public static void main(String[] args) {
		ShapeFileProjection shpProj = new ShapeFileProjection();
		String fileName = "C:\\temp\\buffers.csv";
		PointGeometryBuffer buffer = new PointGeometryBuffer();
		DataSetForApps bufferData = buffer.makeBuffers(fileName);
		JFrame app = new JFrame("testing buffers");
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GeoMap map = new GeoMap();
		DataSetForApps paShps = ShapeFileDataReader
				.makeDataSetForApps("C:\\temp\\pa_cnty");

		shpProj.setInputDataSet(paShps.getDataObjectOriginal());

		app.add(map);
		app.pack();
		app.setVisible(true);

		map.setDataSet(shpProj.getOutputDataSetForApps());
		shpProj.setInputAuxiliaryData(bufferData);
		map.setAuxiliarySpatialData(shpProj
				.getOutputAuxiliarySpatialDataForApps());
	}

}
