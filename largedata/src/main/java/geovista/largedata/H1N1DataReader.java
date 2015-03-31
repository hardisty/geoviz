/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.largedata;

import geovista.common.data.DataSetForApps;
import geovista.common.data.DescriptiveStatistics;
import geovista.common.data.GeoDataSource;
import geovista.common.data.SpatialWeights;
import geovista.readers.csv.GeogCSVReader;
import geovista.readers.example.GoogleFluDataReader;
import geovista.readers.shapefile.ShapeFileDataReader;
import geovista.readers.shapefile.ShapeFileProjection;

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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Logger;

import cern.colt.Arrays;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

@SuppressWarnings("unused")
public class H1N1DataReader implements GeoDataSource {

    final static Logger logger = Logger.getLogger(H1N1DataReader.class
	    .getName());

    HashMap<Integer, Report> reports = new HashMap<Integer, Report>();

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

    HashMap<Integer, Coordinate> coordinateMap;
    HashMap<Integer, String> descMap;

    HashMap<Geometry, Integer> geomMap;
    private static String resource_shapefile = "resources/countries.shp";
    private static String resource_rhiza = "resources/h1n1_inc.csv";

    DataSetForApps dataSet;

    public H1N1DataReader() {
	coordinateMap = new HashMap<Integer, Coordinate>();
	descMap = new HashMap<Integer, String>();
    }

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

    public void readStatesShapefile() {
	logger.info("getting states info");
	GoogleFluDataReader states = null;
	try {
	    states = new GoogleFluDataReader();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	Shape[] theShapes = states.getDataForApps().getShapeData();
	theGeoms = ShapeFileDataReader.shapesToGeoms(theShapes);
	geomMap = new HashMap<Geometry, Integer>();
	for (int j = 0; j < theGeoms.length; j++) {
	    Geometry geom = theGeoms[j];
	    geomMap.put(geom, j);

	}
	ShapeFileProjection proj = new ShapeFileProjection();
	proj.setInputDataSetForApps(states.getDataForApps());
	dataSet = proj.getOutputDataSetForApps();

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

    private static final Calendar now = Calendar.getInstance();

    public static Calendar parseTimestamp(String timestamp) {
	/*
	 * * we specify Locale.US since months are in english
	 */
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
	Date d = null;
	try {
	    d = sdf.parse(timestamp);
	} catch (ParseException e) {
	    return now;
	}
	Calendar cal = Calendar.getInstance();
	cal.setTime(d);
	return cal;
    }

    /*
     * 
     * Data notes
     * 
     * 0 confirmed -- n cases || 1 country || 2 county || 3 date -- M/DD/YYYY ||
     * 4 description -- long text || 5 fatal || 6 fatality || 7 latitude || 8
     * longitude || 9 negative || 10 place || 11 postal_code || 12 source_url ||
     * 13 state -- text, two chars in case of US || 14 status || 15 suspected
     */

    private class Report {
	int id;
	int nCases;
	Calendar date;
	String description;
	Point location;
    }

    private final Report emptyReport = new Report();

    private Report parseReport(String[] report, int id, GeometryFactory fact) {
	Report rep = new Report();

	rep.id = id;
	Integer intCases = 0;
	try {
	    intCases = Integer.valueOf(report[0]);
	} catch (Exception ex) {
	    logger.info("no cases for " + id);
	    return emptyReport;
	}
	rep.nCases = intCases;
	Calendar when = parseTimestamp(report[3]);
	if (when == now) {
	    logger.info("no date for " + id);
	    return emptyReport;
	}
	rep.date = when;
	String desc = report[4];
	double lat = Double.valueOf(report[7]);
	double longit = Double.valueOf(report[8]);

	Coordinate coord = new Coordinate(longit, lat);
	coordinateMap.put(id, coord);
	descMap.put(id, desc);

	rep.location = fact.createPoint(coord);
	return rep;
    }

    public void readRhizaContents() {
	long startTime = System.currentTimeMillis();

	try {
	    InputStream fis = this.getClass().getResourceAsStream(
		    resource_rhiza);
	    BufferedReader input = new BufferedReader(
		    new InputStreamReader(fis));
	    // input.readLine();
	    // ExcelCSVParser parser = new ExcelCSVParser(fis);

	    GeogCSVReader reader = new GeogCSVReader();
	    String[][] vals = reader.readFileToStrings(fis);

	    String[] firstLine = vals[1];
	    // logger.info(Arrays.toString(firstLine));
	    int id = 0;
	    GeometryFactory fact = new GeometryFactory();
	    for (id = 1; id < vals.length; id++) {
		String[] report = vals[id];
		Report rep = parseReport(report, id, fact);
		if (rep != emptyReport) {
		    reports.put(id, rep);
		}
	    }

	    long endTime = System.currentTimeMillis();
	    logger.info((endTime - startTime) / 1000 + " seconds");
	    logger.info(id + " reports ");
	    logger.info(reports.size() + " valid reports ");

	} catch (Exception ex) {
	    ex.printStackTrace();
	}

    }

    private void countHits(int[] hits, Point coord) {

	for (int i = 0; i < theGeoms.length; i++) {
	    Geometry theGeom = theGeoms[i];

	    if (theGeom.contains(coord)) {
		hits[i]++;
		break;
	    }

	}

    }

    public int[] countAllHits() {
	return countCertainHits(reports.values());

    }

    int[] countCertainHits(Collection<Report> reps) {
	int[] hitCount = new int[theGeoms.length];

	for (Report element : reps) {

	    countHits(hitCount, element.location);
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

    public DataSetForApps makeDataSetForApps(String text) {
	Object[] coords = locations.toArray();
	int[] hits = countAllHits();
	logger.info(Arrays.toString(hits));

	HashMap<Integer, String> descs = descMap;
	ArrayList<Integer> childHits = new ArrayList<Integer>();
	for (Integer i : descs.keySet()) {
	    String desc = descs.get(i);
	    if (desc.contains(text)) {
		childHits.add(i);
	    }
	}

	int[] childHitCounts = countCertainHits(getReports(childHits));

	logger.info(Arrays.toString(childHitCounts));

	double[] proportions = new double[childHitCounts.length];
	for (int i = 0; i < proportions.length; i++) {
	    proportions[i] = (float) childHitCounts[i] / (float) hits[i];
	}

	logger.info(Arrays.toString(proportions));

	dataSet = new DataSetForApps(dataSet, text, proportions);
	return dataSet;
    }

    private DataSetForApps makeDataSetForApps() {

	readStatesShapefile();
	readRhizaContents();

	Object[] coords = locations.toArray();
	int[] hits = countAllHits();
	logger.info(Arrays.toString(hits));

	HashMap<Integer, String> descs = descMap;
	ArrayList<Integer> childHits = new ArrayList<Integer>();
	for (Integer i : descs.keySet()) {
	    String desc = descs.get(i);
	    if (desc.contains("children") || desc.contains("child")
		    || desc.contains("student")) {
		childHits.add(i);
	    }
	}

	int[] childHitCounts = countCertainHits(getReports(childHits));

	logger.info(Arrays.toString(childHitCounts));

	double[] proportions = new double[childHitCounts.length];
	for (int i = 0; i < proportions.length; i++) {
	    proportions[i] = (float) childHitCounts[i] / (float) hits[i];
	}

	logger.info(Arrays.toString(proportions));

	dataSet = new DataSetForApps(dataSet, "Child", proportions);
	return dataSet;
    }

    private DataSetForApps makeDataSetForAppsOld() {

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

    public Collection<Report> getReports(ArrayList<Integer> ids) {
	Collection<Report> returnedReports = new ArrayList<Report>();
	for (Integer i : ids) {
	    Report rep = reports.get(i);
	    returnedReports.add(rep);
	}

	return returnedReports;

    }

    public static void main(String[] args) {

	H1N1DataReader reader = new H1N1DataReader();
	// H1N1DataReader.printMemory();
	// reader.readIsoCodes();
	// reader.findCountryCodes();
	// reader.readIDCodes();
	// reader.readWorldShapefile();
	reader.readStatesShapefile();
	reader.readRhizaContents();
	//
	long startTime = System.nanoTime();
	// reader.countAllHits();
	long endTime = System.nanoTime();
	// H1N1DataReader.printMemory();
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
	int[] hits = reader.countAllHits();
	logger.info(Arrays.toString(hits));

	HashMap<Integer, String> descs = reader.descMap;
	ArrayList<Integer> childHits = new ArrayList<Integer>();
	for (Integer i : descs.keySet()) {
	    String desc = descs.get(i);
	    if (desc.contains("children") || desc.contains("child")) {
		childHits.add(i);
	    }
	}

	int[] childHitCounts = reader.countCertainHits(reader
		.getReports(childHits));

	logger.info(Arrays.toString(childHitCounts));

	float[] proportions = new float[childHitCounts.length];
	for (int i = 0; i < proportions.length; i++) {
	    proportions[i] = (float) childHitCounts[i] / (float) hits[i];
	}

	logger.info(Arrays.toString(proportions));

	endTime = System.nanoTime();

	// logger.info("hits = " + Arrays.toString(hits));
	// logger
	// .info("finding  hits took " + (endTime - startTime)
	// / 1000000000f);
	logger.info("All done!");

    }
}
