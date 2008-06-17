/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.largedata;

import java.awt.Shape;
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
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.vividsolutions.jts.algorithm.locate.IndexedPointInAreaLocator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Location;
import com.vividsolutions.jts.index.quadtree.Quadtree;

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
	IndexedPointInAreaLocator[] theIndexes;
	Quadtree qTree;

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
		theIndexes = new IndexedPointInAreaLocator[theGeoms.length];
		qTree = new Quadtree();
		for (int i = 0; i < theGeoms.length; i++) {
			Geometry geom = theGeoms[i];
			theIndexes[i] = new IndexedPointInAreaLocator(geom);
			qTree.insert(geom.getEnvelopeInternal(), geom);
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
			String fileName = "C:\\data\\geovista_data\\herberia\\herb.txt";
			FileInputStream fis = new FileInputStream(fileName);
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			BufferedReader input = new BufferedReader(
					new InputStreamReader(fis));
			String line = "";
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date currDate = new Date();
			Pattern tabPattern = Pattern.compile("\t");
			long lineCount = 0;
			long itemCount = 0;
			long geoItemCount = 0;
			long startTime = System.currentTimeMillis();
			HashMap<Integer, Long> lengthHash = new HashMap();
			HashSet<String> ganderSet = new HashSet();
			int[] hits = new int[theGeoms.length];
			while ((line = input.readLine()) != null) {

				String[] lineContents = tabPattern.split(line);
				// Scanner sc = new Scanner(line).useDelimiter("\t");

				itemCount = itemCount + lineContents.length;

				if (lineContents.length >= 14) {
					// System.out.println(lineContents[12] + lineContents[13]);
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

					countHits(hits, coord);
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
					// System.out.println(speciesName);
					species.add(speciesName);
					String observerName = lineContents[2];
					// System.out.println(observerName);
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

				// System.out.println(subString);
			}
			long endTime = System.currentTimeMillis();
			System.out.println((endTime - startTime) / 1000 + " seconds");
			System.out.println(lineCount + " lines ");
			System.out.println(itemCount + " items ");
			System.out.println(geoItemCount + " geoitems ");
			System.out.println(lengthHash.keySet());
			System.out.println(lengthHash.values());
			System.out.println("n observers = " + observers.size());
			System.out.println("n species = " + species.size());
			System.out.println(ganderSet);

			Iterator ganderIt = ganderSet.iterator();
			while (ganderIt.hasNext()) {
				System.out.println(ganderIt.next());
			}
			System.out.println(Arrays.toString(hits));

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void countHits(int[] hits, Coordinate coord) {

		for (int i = 0; i < theGeoms.length; i++) {
			// Geometry geom = theGeoms[i];
			int answer = theIndexes[i].locate(coord);
			if (answer == Location.INTERIOR) {
				hits[i]++;
				break;
			}

		}
	}

	private void countQuadHits() {
		Envelope env = new Envelope();

		for (Coordinate coord : locations) {
			env.init(coord);
			env.init(env);
			List geoms = qTree.query(env);
			for (int i = 0; i < geoms.size(); i++) {
				int answer = theIndexes[i].locate(coord);// XXX this would
				// not work, just
				// timing here...
			}
			// countHits(hitCount, coord);
		}
	}

	public void countAllHits() {
		int[] hitCount = new int[theGeoms.length];
		for (Coordinate coord : locations) {
			countHits(hitCount, coord);
		}

	}

	public void countAllHits(Object[] coords) {
		int[] hitCount = new int[theGeoms.length];
		for (Object element : coords) {
			Coordinate coord = (Coordinate) element;
			countHits(hitCount, coord);
		}

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
			// System.out.println(year);
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
		System.out.println(varNameformat.format(firstDay));
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
			System.out.println(label);
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

		System.out.println("free memory: " + freeMemory / 1024);
		System.out.println("allocated memory: " + allocatedMemory / 1024);
		System.out.println("max memory: " + maxMemory / 1024);
		System.out.println("total free memory: "
				+ (freeMemory + (maxMemory - allocatedMemory)) / 1024);
	}

	public static void main(String[] args) {

		HerberiaDataReader reader = new HerberiaDataReader();
		reader.printMemory();
		// reader.readIsoCodes();
		// reader.findCountryCodes();
		// reader.readIDCodes();
		reader.readCAShapefile();
		reader.readHerberiaContents();
		// 
		long startTime = System.nanoTime();
		reader.countAllHits();
		long endTime = System.nanoTime();
		reader.printMemory();
		logger.info("finding hits took " + (endTime - startTime) / 1000000000f);
		// DataSetForApps dataSet = reader.makeDataSetForApps();
		startTime = System.nanoTime();
		reader.countQuadHits();
		endTime = System.nanoTime();
		Object[] coords = reader.locations.toArray();
		logger.info("finding quad hits took " + (endTime - startTime)
				/ 1000000000f);

		startTime = System.nanoTime();
		reader.countAllHits(coords);
		endTime = System.nanoTime();

		logger.info("finding quad hits took " + (endTime - startTime)
				/ 1000000000f);
		System.out.println("All done!");

	}

}
