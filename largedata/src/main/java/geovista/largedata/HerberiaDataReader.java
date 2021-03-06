/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.largedata;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.vividsolutions.jts.algorithm.locate.IndexedPointInAreaLocator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.prep.PreparedGeometry;
import com.vividsolutions.jts.geom.prep.PreparedGeometryFactory;
import com.vividsolutions.jts.index.quadtree.Quadtree;
import com.vividsolutions.jts.index.strtree.STRtree;

import geovista.common.data.DataSetForApps;
import geovista.common.data.DescriptiveStatistics;
import geovista.common.data.GeoDataSource;
import geovista.common.data.SpatialWeights;
import geovista.readers.shapefile.ShapeFileDataReader;

public class HerberiaDataReader implements GeoDataSource {

	final static Logger logger = Logger.getLogger(HerberiaDataReader.class
			.getName());

	ArrayList<Date> date = new ArrayList();
	ArrayList<Integer> day = new ArrayList();
	ArrayList<String> name = new ArrayList();
	ArrayList<Integer> nCases = new ArrayList();
	ArrayList<Integer> nRespCases = new ArrayList();
	HashMap<String, Integer> idName = new HashMap<String, Integer>();
	HashMap<Integer, String> nameID = new HashMap();
	HashSet<String> observers = new HashSet();
	HashSet<String> species = new HashSet();
	ArrayList<Coordinate> locations = new ArrayList();
	Geometry[] theGeoms;
	PreparedGeometry[] preparedGeoms;
	Rectangle2D[] bounds;
	IndexedPointInAreaLocator[] theIndexes;
	Quadtree qTree;
	STRtree strTree;
	HashMap<Geometry, Integer> geomMap;

	public void readCAShapefile() {
		FileInputStream shpStream = null;
		try {
			shpStream = new FileInputStream(
					"C:\\data\\geovista_data\\herberia\\ca.shp");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		theGeoms = ShapeFileDataReader.getGeoms(shpStream);
		// theGeoms = ShapeFileDataReader.makeSimplerGeoms(theGeoms, 1);
		Shape[] shapes = ShapeFileDataReader.geomsToShapes(theGeoms);
		preparedGeoms = new PreparedGeometry[theGeoms.length];
		bounds = new Rectangle2D[theGeoms.length];
		for (int i = 0; i < theGeoms.length; i++) {
			preparedGeoms[i] = PreparedGeometryFactory.prepare(theGeoms[i]);
			bounds[i] = shapes[i].getBounds2D();
		}
		int nPolys = 0;
		// GeometryFactory fact = new GeometryFactory();
		strTree = new STRtree();
		// for (Geometry geom : theGeoms) {
		// DelaunayTriangulationBuilder dtb = new
		// DelaunayTriangulationBuilder();
		// dtb.setSites(geom);
		// Geometry triangles = dtb.getTriangles(fact);
		// int nGeoms = triangles.getNumGeometries();
		// for (int i = 0; i < nGeoms; i++) {
		// Geometry triangle = triangles.getGeometryN(i);
		//
		// strTree.insert(triangle.getEnvelope().getEnvelopeInternal(),
		// triangle);
		// }
		// // MultiPolygon mp = (MultiPolygon) geom;
		// // nPolys = mp.getNumGeometries();
		// }

		theIndexes = new IndexedPointInAreaLocator[nPolys];

		qTree = new Quadtree();

		geomMap = new HashMap<Geometry, Integer>();
		for (int j = 0; j < theGeoms.length; j++) {
			Geometry geom = theGeoms[j];
			geomMap.put(geom, j);

			// theIndexes[i] = new IndexedPointInAreaLocator(mp);
			// i++;
			logger.info("n polys = " + geom.getNumGeometries());
			MultiPolygon mp = (MultiPolygon) geom;
			for (int i = 0; i < mp.getNumGeometries(); i++) {
				qTree.insert(geom.getGeometryN(i).getEnvelopeInternal(), geom);
				strTree
						.insert(geom.getGeometryN(i).getEnvelopeInternal(),
								geom);

			}

		}

	}

	private static double isDouble(String str) {
		try {
			return Double.parseDouble(str);

		} catch (NumberFormatException nfe) {
			return Double.NaN;
		}
	}

	public void readHerberiaContents() {

		try {
			String fileName = "C:\\data\\geovista_data\\herberia\\herb_old.txt";
			FileInputStream fis = new FileInputStream(fileName);
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			BufferedReader input = new BufferedReader(
					new InputStreamReader(fis));
			String line = "";
			// DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			// Date currDate = new Date();
			Pattern tabPattern = Pattern.compile("\t");

			long lineCount = 0;
			long itemCount = 0;
			long geoItemCount = 0;
			long startTime = System.currentTimeMillis();
			HashMap<Integer, Long> lengthHash = new HashMap();
			HashSet<String> ganderSet = new HashSet();
			int[] hits = new int[theGeoms.length];
			int i = 0;

			while ((line = input.readLine()) != null) {
				if (i < 10) {
					System.out.print(line);
				}
				i++;
				String[] lineContents = tabPattern.split(line);
				// Scanner sc = new Scanner(line).useDelimiter("\t");

				itemCount = itemCount + lineContents.length;

				if (lineContents.length >= 14) {
					// logger.info(lineContents[12] + lineContents[13]);
					String yString = lineContents[12];
					String xString = lineContents[13];
					double yCoord = isDouble(yString);
					double xCoord = isDouble(xString);
					if (Double.isNaN(xCoord) || Double.isNaN(yCoord)) {
						continue;
					}
					geoItemCount++;

					Coordinate coord = new Coordinate(xCoord, yCoord);

					locations.add(coord);

					// countHits(hits, coord);
				}
				Integer lengthInt = Integer.valueOf(lineContents.length);
				if (lengthHash.containsKey(lengthInt)) {
					Long value = lengthHash.get(lengthInt);
					value++;
					lengthHash.put(lengthInt, value);
				} else {
					logger.info("adding " + lengthInt);
					logger.info(line);
					lengthHash.put(lengthInt, 1l);
				}
				if (lineContents.length > 3) {
					String speciesName = lineContents[1];
					// logger.info(speciesName);
					species.add(speciesName);
					String observerName = lineContents[2];
					// logger.info(observerName);
					if (observerName.contains("gander")
							|| observerName.contains("Gander")) {
						ganderSet.add(observerName);
					}
					observers.add(observerName);
				}
				// while (sc.hasNext()) {
				// sc.next();
				// itemCount++;
				// }
				// subString = sc.next();
				lineCount++;

				// logger.info(subString);
			}
			long endTime = System.currentTimeMillis();
			logger.info((endTime - startTime) / 1000 + " seconds");
			logger.info(lineCount + " lines ");
			logger.info(itemCount + " items ");
			logger.info(geoItemCount + " geoitems ");
			logger.info("" + lengthHash.keySet());
			logger.info("" + lengthHash.values());
			logger.info("n observers = " + observers.size());
			logger.info("n species = " + species.size());
			logger.info("" + ganderSet);

			Iterator ganderIt = ganderSet.iterator();
			while (ganderIt.hasNext()) {
				logger.info("" + ganderIt.next());
			}
			logger.info(Arrays.toString(hits));

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void countHits(int[] hits, Coordinate coord) {

		// List possibles = strTree.query(new Envelope(coord));
		//
		// int nPossibles = possibles.size();
		// for (int i = 0; i < nPossibles; i++) {
		// Geometry geom = (Geometry) possibles.get(i);
		// if (geom.contains(coordGeom)) {
		// hits[0]++;
		// }
		// break;
		// }

		for (int i = 0; i < theGeoms.length; i++) {
			Geometry theGeom = theGeoms[i];
			Envelope evn = theGeom.getEnvelopeInternal();
			if (evn.contains(coord)) {
				Geometry coordGeom = GeometryFactory
						.createPointFromInternalCoord(coord, theGeom);
				if (theGeom.contains(coordGeom)) {
					hits[i]++;
					break;
				}
			}

		}

		// List geoms = strTree.query(new Envelope(coord));
		// Iterator it = geoms.iterator();
		// // logger.info(geoms.size() + "");
		// while (it.hasNext()) {
		// Geometry geom = (Geometry) it.next();
		// int index = geomMap.get(geom);
		// Rectangle2D bounds = this.bounds[index];
		// if (coord.x > bounds.getMinX() && coord.x < bounds.getMaxX()) {
		// if (coord.y > bounds.getMinY() && coord.y < bounds.getMaxY()) {
		//
		// Geometry coordGeom = GeometryFactory
		// .createPointFromInternalCoord(coord, geom);
		// if (geom.contains(coordGeom)) {
		// hits[geomMap.get(geom)]++;
		// }
		// }
		// }
		// }

		// for (int i = 0; i < theGeoms.length; i++) {
		//
		// PreparedGeometry prepGeom = preparedGeoms[i];
		//		
		// Rectangle2D bounds = this.bounds[i];
		// if (coord.x > bounds.getMinX() && coord.x < bounds.getMaxX()) {
		// if (coord.y > bounds.getMinY() && coord.y < bounds.getMaxY()) {
		// Geometry geom = theGeoms[i];
		// 
		// if (prepGeom.containsProperly(coordGeom)) {
		// hits[i]++;
		// break;
		// }
		// }
		// }
		// // int answer = theIndexes[i].locate(coord);
		// //
		// // if (answer == Location.INTERIOR) {
		// // hits[i]++;
		// // break;
		// // }
		//
		// }
	}

	public void countAllHits() {
		int[] hitCount = new int[theGeoms.length];
		for (Coordinate coord : locations) {
			countHits(hitCount, coord);
		}

	}

	public int[] countAllHits(Object[] coords) {
		int[] hitCount = new int[theGeoms.length];
		for (Object element : coords) {
			Coordinate coord = (Coordinate) element;
			countHits(hitCount, coord);
		}
		return hitCount;

	}

	private void readIDCodes() {
		try {
			FileInputStream isoGtdStream = new FileInputStream(
					"C:\\data\\grants\\nevac\\purdue\\id_name.csv");
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			BufferedReader input = new BufferedReader(new InputStreamReader(
					isoGtdStream));
			try {
				String line = null; // not declared within while loop
				/*
				 * readLine is a bit quirky : it returns the content of a line
				 * MINUS the newline. it returns null only for the END of the
				 * stream. it returns an empty String if two newlines appear in
				 * a row.
				 */
				String name = "";
				String ID = "";
				while ((line = input.readLine()) != null) {
					Scanner scan = new Scanner(line).useDelimiter(",");
					ID = scan.next();

					name = scan.next();

					if (idName.containsKey(name)) {
						logger.severe("hit duplicate name, bad bad");
					}

					idName.put(name, Integer.valueOf(ID));
					nameID.put(Integer.valueOf(ID), name);
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	private DataSetForApps makeDataSetForApps() {

		int nCounties = idName.size();
		int periodicity = 30;
		int minDay = 1;
		int maxDay = 730;
		if (nCases == null) {
			logger.severe("data not loaded");
			return null;
		}
		int numBins = (maxDay - minDay) / periodicity;
		numBins++; // count from one
		int varTypes = 1;
		// cases, respetory cases
		Object[] numericalArrays = new Object[(numBins * varTypes) + 1];
		for (int i = 0; i < numBins * varTypes; i++) {
			double[] data = new double[nCounties];
			numericalArrays[i] = data;
		}
		ArrayList<String> yearHeadings = makeYearHeadings(periodicity, minDay,
				maxDay);

		for (int i = 0; i < date.size(); i++) {
			int day = this.day.get(i);
			// logger.info(year);
			// int day = minDay;
			int bin = day / periodicity;
			double[] killedYear = (double[]) numericalArrays[bin];
			// double[] woundedYear = (double[]) numericalArrays[bin + numBins];

			String name = this.name.get(i);
			int rowID = idName.get(name);

			if (rowID >= 0) {
				Integer nCase = nCases.get(i);
				if (nCase != null && nCase.equals(Integer.MIN_VALUE) == false) {
					// logger.info(nKillInt.toString());
					killedYear[rowID] = killedYear[rowID] + nCase;
				}
				Integer nCaseResp = nRespCases.get(i);
				if (nCaseResp != null
						&& nCaseResp.equals(Integer.MIN_VALUE) == false) {
					// woundedYear[rowID] = woundedYear[rowID] + nCaseResp;
				}

			}

		}

		String[] varNames = new String[numericalArrays.length];

		String[] obsNames = makeObsNames(nCounties);
		numericalArrays[numericalArrays.length - 1] = obsNames;
		for (int i = 0; i < numBins; i++) {
			varNames[i] = yearHeadings.get(i);
			// varNames[i + numBins] = "nResp_" + yearHeadings.get(i);

		}
		varNames[varNames.length - 1] = "name";
		FileInputStream shpStream = null;
		try {
			shpStream = new FileInputStream(
					"C:\\data\\grants\\nevac\\purdue\\IN.shp");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Shape[] geoms = ShapeFileDataReader.getShapes(shpStream);
		FileInputStream shpStream2 = null;
		try {
			shpStream2 = new FileInputStream(
					"C:\\data\\grants\\nevac\\purdue\\IN.shp");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SpatialWeights weights = ShapeFileDataReader.getWeights(shpStream2);
		DataSetForApps dataSet = new DataSetForApps(varNames, numericalArrays,
				geoms, weights);
		// transformStdDev(dataSet);

		return dataSet;
	}

	@SuppressWarnings("unused")
	private void transformStdDev(DataSetForApps data) {
		double[] rowData = new double[data.getNumberNumericAttributes()];

		for (int obs = 0; obs < data.getNumObservations(); obs++) {
			for (int var = 0; var < data.getNumberNumericAttributes(); var++) {
				rowData[var] = data.getNumericValueAsDouble(var, obs);
			}
			double[] zScores = DescriptiveStatistics.calculateZScores(rowData);
			for (int var = 0; var < data.getNumberNumericAttributes(); var++) {
				double[] numericVar = data.getNumericDataAsDouble(var);
				numericVar[obs] = zScores[var];
			}
		}

	}

	private ArrayList<String> makeYearHeadings(int periodicity, int minVal,
			int maxVal) {
		int numBins = (maxVal - minVal) / periodicity;
		ArrayList<String> headings = new ArrayList<String>();
		Date firstDay = date.get(0);
		DateFormat varNameformat = new SimpleDateFormat("yy-MM");
		DateFormat baseFormat = new SimpleDateFormat("yyy-MM-ddd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(firstDay);
		int originalDay = cal.get(Calendar.DAY_OF_MONTH);
		int originalMonth = cal.get(Calendar.MONTH);
		int originalYear = cal.get(Calendar.YEAR);
		logger.info(varNameformat.format(firstDay));
		for (int i = 0; i <= numBins; i++) {// note the <= in the loop
			int binMin = (periodicity * i) + minVal;
			int binMax = binMin + periodicity;
			if (binMax > maxVal) {
				binMax = maxVal;
			}
			int day = originalDay + binMax;
			String dateString = originalYear + "-" + originalMonth + "-" + day;
			Date newDate = null;
			try {
				newDate = baseFormat.parse(dateString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cal.setTime(newDate);
			String label = varNameformat.format(cal.getTime());
			logger.info(label);
			headings.add(label);

		}

		return headings;

	}

	private String[] makeObsNames(int nCountries) {
		String[] obsNames = new String[nCountries];
		for (int i = 0; i < nCountries; i++) {

			String name = nameID.get(i);
			obsNames[i] = name;
		}
		return obsNames;
	}

	public DataSetForApps getDataForApps() {
		readIDCodes();
		readHerberiaContents();

		return makeDataSetForApps();

	}

	public static void printMemory() {
		Runtime runtime = Runtime.getRuntime();

		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();

		logger.info("free memory: " + freeMemory / 1024);
		logger.info("allocated memory: " + allocatedMemory / 1024);
		logger.info("max memory: " + maxMemory / 1024);
		logger.info("total free memory: "
				+ (freeMemory + (maxMemory - allocatedMemory)) / 1024);
	}

	public static void main(String[] args) {

		HerberiaDataReader reader = new HerberiaDataReader();
		HerberiaDataReader.printMemory();
		// reader.readIsoCodes();
		// reader.findCountryCodes();
		// reader.readIDCodes();
		reader.readCAShapefile();
		reader.readHerberiaContents();
		// 
		long startTime = System.nanoTime();
		// reader.countAllHits();
		long endTime = System.nanoTime();
		HerberiaDataReader.printMemory();
		// logger.info("finding hits took " + (endTime - startTime) /
		// 1000000000f);
		// DataSetForApps dataSet = reader.makeDataSetForApps();
		startTime = System.nanoTime();

		endTime = System.nanoTime();
		Object[] coords = reader.locations.toArray();
		// logger.info("finding quad hits took " + (endTime - startTime)
		// / 1000000000f);

		logger.info("starting hit count");

		startTime = System.nanoTime();
		int[] hits = reader.countAllHits(coords);

		endTime = System.nanoTime();
		logger.info("hits = " + Arrays.toString(hits));
		logger
				.info("finding  hits took " + (endTime - startTime)
						/ 1000000000f);
		logger.info("All done!");

	}

}
