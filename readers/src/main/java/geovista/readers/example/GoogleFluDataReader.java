/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.readers.example;

import java.awt.Shape;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;

import geovista.common.data.DataSetForApps;
import geovista.common.data.DescriptiveStatistics;
import geovista.common.data.GeoDataSource;
import geovista.common.data.SpatialWeights;
import geovista.readers.join.JoinPoint;
import geovista.readers.join.Joiner;
import geovista.readers.shapefile.ShapeFileDataReader;

public class GoogleFluDataReader implements GeoDataSource {

	final static Logger logger = Logger.getLogger(GoogleFluDataReader.class
			.getName());

	ArrayList<Date> dates = new ArrayList<Date>();
	ArrayList<Date> monthDates = new ArrayList<Date>();
	ArrayList<Integer> day = new ArrayList();
	private final ArrayList<String> names = new ArrayList();
	HashMap<String, ArrayList<Double>> iLIData = new HashMap<String, ArrayList<Double>>();
	HashMap<String, ArrayList<Double>> monthlyILIData = new HashMap<String, ArrayList<Double>>();
	ArrayList<Integer> nCases = new ArrayList();
	ArrayList<Integer> nRespCases = new ArrayList();
	HashMap<String, Integer> idName = new HashMap<String, Integer>();
	HashMap<Integer, String> nameID = new HashMap();

	public GoogleFluDataReader() {
		readContents();
		makeMonthyData();
	}

	public void readContents() {

		try {
			Scanner sc = null;
			String fileName = "C:\\data\\geovista_data\\syndromic\\google_trends\\historic1.csv";
			FileInputStream fis = new FileInputStream(fileName);
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			BufferedReader input = new BufferedReader(
					new InputStreamReader(fis));
			String namesLine = input.readLine();
			extractNames(namesLine);
			initiLIData();
			String subString = "";
			String line = "";
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			while ((line = input.readLine()) != null) {

				sc = new Scanner(line).useDelimiter(",");

				subString = sc.next();

				Date d = format.parse(subString);

				dates.add(d);
				int counter = 0;
				while (sc.hasNext()) {
					Double num = Double.valueOf(sc.next());
					String place = names.get(counter);
					counter++;
					ArrayList<Double> placeData = iLIData.get(place);
					placeData.add(num);

				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void initiLIData() {
		for (String name : names) {
			ArrayList<Double> nums = new ArrayList<Double>();
			iLIData.put(name, nums);

			ArrayList<Double> monthNums = new ArrayList<Double>();
			monthlyILIData.put(name, monthNums);
		}

	}

	private void makeMonthyData() {
		int month = dates.get(0).getMonth();
		ArrayList<Integer> datePlaces = new ArrayList<Integer>();
		// here we summarize by month
		int monthCounter = 0;
		for (int whichDate = 0; whichDate < dates.size(); whichDate++) {
			int currMonth = dates.get(whichDate).getMonth();
			if (currMonth != month) {
				for (int i = 0; i < datePlaces.size(); i++) {

					Set<String> keySet = iLIData.keySet();
					for (String placeName : keySet) {
						ArrayList<Double> originalData = iLIData.get(placeName);
						ArrayList<Double> sumData = monthlyILIData
								.get(placeName);

						if (i == 0) {
							sumData.add(originalData.get(i));

						} else {

							Double sum = sumData.get(monthCounter);
							sum = sum + originalData.get(i);
							sumData.set(monthCounter, sum);
						}

					}
				}
				Set<String> keySet = iLIData.keySet();
				for (String placeName : keySet) {
					if (monthCounter == 5) {
						System.out.println("watchit");
					}
					ArrayList<Double> sumData = monthlyILIData.get(placeName);
					Double avg = sumData.get(monthCounter);
					avg = avg / datePlaces.size();
					sumData.set(monthCounter, avg);
				}
				monthDates.add(dates.get(whichDate));
				monthCounter++;
				datePlaces.clear();
				month = currMonth;

			} else {
				datePlaces.add(whichDate);
			}

		}

	}

	private void extractNames(String namesLine) {
		Scanner scan = new Scanner(namesLine);
		scan.useDelimiter(",");
		scan.next();// skip "date"
		while (scan.hasNext()) {
			String aName = scan.next();
			System.out.println("names = " + aName);
			names.add(aName);
		}

	}

	private DataSetForApps makeDataSetForApps() {

		int nCounties = idName.size();
		int periodicity = 60;
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

		for (int i = 0; i < dates.size(); i++) {
			int day = this.day.get(i);
			// System.out.println(year);
			// int day = minDay;
			int bin = day / periodicity;
			double[] killedYear = (double[]) numericalArrays[bin];
			// double[] woundedYear = (double[]) numericalArrays[bin + numBins];

			String name = names.get(i);
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

		// String[] obsNames = makeObsNames(nCounties);
		// numericalArrays[numericalArrays.length - 1] = obsNames;
		for (int i = 0; i < numBins; i++) {
			varNames[i] = yearHeadings.get(i);
			// varNames[i + numBins] = "nResp_" + yearHeadings.get(i);

		}
		varNames[varNames.length - 1] = "names";
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
		transformStdDev(dataSet);

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
		Date firstDay = dates.get(0);
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

	public DataSetForApps getDataForApps() {
		GeoDataGeneralizedStates dataMaker = new GeoDataGeneralizedStates();
		DataSetForApps dataSet = dataMaker.getDataForApps();
		String[] stateNames = dataSet.getObservationNames();
		ArrayList<String> stateNameList = new ArrayList<String>();
		for (String element : stateNames) {
			stateNameList.add(element);
		}
		List<JoinPoint> join = Joiner.leftOuterJoin(stateNameList, names);

		for (JoinPoint pt : join) {
			if (pt.getRight() == JoinPoint.NO_COORDINATE) {
				System.out.println("ark ark, missed " + pt.getValue());
			}
		}
		Object[] doubleData = new Object[iLIData.get("United States").size()];
		for (int i = 0; i < doubleData.length; i++) {
			doubleData[i] = new double[join.size()];
		}
		String[] varNames = new String[doubleData.length + 1];
		for (int i = 0; i < doubleData.length; i++) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dates.get(i));
			varNames[i + 1] = cal.get(Calendar.DAY_OF_MONTH) + "-"
					+ (cal.get(Calendar.MONTH) + 1) + "-"
					+ cal.get(Calendar.YEAR);
			System.out.println("var = " + varNames[i]);
		}
		varNames[0] = "ST_NAME";

		for (int row = 0; row < join.size(); row++) {
			String name = stateNames[row];
			ArrayList<Double> numList = iLIData.get(name);
			for (int column = 0; column < doubleData.length; column++) {
				double[] nums = (double[]) doubleData[column];
				double aNum = numList.get(column);

				nums[row] = aNum;
			}
		}

		Object[] objectData = new Object[doubleData.length + 3];
		objectData[0] = varNames;
		objectData[1] = dataSet.getObservationNames();
		for (int i = 0; i < doubleData.length; i++) {
			objectData[i + 2] = doubleData[i];
		}
		SpatialWeights sw = dataSet.getSpatialWeights();

		objectData[objectData.length - 2] = dataSet.getShapeData();
		objectData[objectData.length - 1] = sw;

		DataSetForApps newDataSet = new DataSetForApps(objectData);

		newDataSet.setSpatialWeights(sw);

		return newDataSet;
	}

	public static void main(String[] args) {

		GoogleFluDataReader reader = new GoogleFluDataReader();
		// reader.readIsoCodes();
		// reader.findCountryCodes();

		DataSetForApps dataSet = reader.getDataForApps();

		System.out.println("All done! we have this many obs:"
				+ dataSet.getNumObservations());

	}
}
