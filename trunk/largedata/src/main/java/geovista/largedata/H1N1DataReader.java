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
import java.io.InputStream;
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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import geovista.common.data.DataSetForApps;
import geovista.common.data.DescriptiveStatistics;
import geovista.common.data.GeoDataSource;
import geovista.common.data.SpatialWeights;
import geovista.readers.csv.ExcelCSVParser;
import geovista.readers.shapefile.ShapeFileDataReader;

@SuppressWarnings("unused")
public class H1N1DataReader implements GeoDataSource {

	final static Logger logger = Logger.getLogger(H1N1DataReader.class
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

	Rectangle2D[] bounds;

	HashMap<Geometry, Integer> geomMap;
	private static String resource_shapefile = "resources/countries.shp";
	private static String resource_rhiza = "resources/h1n1_inc.csv";

	public void readWorldShapefile() {
		logger.info("reading world shapefile");
		InputStream shpStream = this.getClass().getResourceAsStream(
				resource_shapefile);

		theGeoms = ShapeFileDataReader.getGeoms(shpStream, 1);
		// theGeoms = ShapeFileDataReader.makeSimplerGeoms(theGeoms, 1);
		Shape[] shapes = ShapeFileDataReader.geomsToShapes(theGeoms);

		bounds = new Rectangle2D[theGeoms.length];
		for (int i = 0; i < theGeoms.length; i++) {

			bounds[i] = shapes[i].getBounds2D();
		}

		int nPolys = 0;

		geomMap = new HashMap<Geometry, Integer>();
		for (int j = 0; j < theGeoms.length; j++) {
			Geometry geom = theGeoms[j];
			geomMap.put(geom, j);

		}
		// Geometry[] geoms = new Geometry[theGeoms.length];
		// for (int i=0;i)

		// ShapeFileDataWriter.writeShapefile()

	}

	private static double isDouble(String str) {
		try {
			return Double.parseDouble(str);

		} catch (NumberFormatException nfe) {
			return Double.NaN;
		}
	}

	private String extractRest(BufferedReader input) {
		char quote = (char) 34;
		StringBuilder builder = new StringBuilder();
		try {
			int val = input.read();
			if (val == -1) {
				return "";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	public void readRhizaContents() {

		try {
			InputStream fis = this.getClass().getResourceAsStream(
					resource_rhiza);
			BufferedReader input = new BufferedReader(
					new InputStreamReader(fis));
			// input.readLine();
			ExcelCSVParser parser = new ExcelCSVParser(fis);
			String[][] vals = parser.getAllValues();

			String[] firstLine = vals[1];
			logger.info(Arrays.toString(firstLine));

			int val = -1;
			if (val == -1) {
				return;
			}

			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!

			String line = "";
			// DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			// Date currDate = new Date();
			Pattern commaPattern = Pattern.compile(",");

			long lineCount = 0;
			long itemCount = 0;
			long geoItemCount = 0;
			long startTime = System.currentTimeMillis();
			HashMap<Integer, Long> lengthHash = new HashMap();
			HashSet<String> ganderSet = new HashSet();
			int[] hits = new int[theGeoms.length];
			int i = 0;
			input.readLine();// skip header

			while ((line = input.readLine()) != null) {
				if (i < 10) {
					System.out.print(line);
				}
				i++;
				String[] lineContents = commaPattern.split(line);
				// Scanner sc = new Scanner(line).useDelimiter("\t");

				itemCount = itemCount + lineContents.length;

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
		readRhizaContents();

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

		H1N1DataReader reader = new H1N1DataReader();
		H1N1DataReader.printMemory();
		// reader.readIsoCodes();
		// reader.findCountryCodes();
		// reader.readIDCodes();
		reader.readWorldShapefile();
		reader.readRhizaContents();
		// 
		long startTime = System.nanoTime();
		// reader.countAllHits();
		long endTime = System.nanoTime();
		H1N1DataReader.printMemory();
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
