/**
 * 
 */
package geovista.readers.seerstat;

import java.awt.Shape;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import geovista.common.data.DataSetForApps;
import geovista.readers.example.GeoDataUSCounties;

/**
 * @author Frank Hardisty
 * 
 */
public class SeerStatReader {

	private String dicFileLocation;
	private String txtFileLocation;
	protected final static Logger logger = Logger
			.getLogger(SeerStatReader.class.getName());

	// private HashMap<String, HashMap<Integer, String>> recodes;

	private ArrayList<Variable> variables;

	private ArrayList<String> newVariableNames;
	private HashMap<Integer, String> idToFips;
	private HashMap<String, Integer> fipsToID;
	private HashMap<String, ArrayList<Double>> txtData;

	private DataSetForApps dataSet;

	private final int fipsPlace = 1;

	public DataSetForApps readFiles() {
		idToFips = new HashMap<Integer, String>();
		fipsToID = new HashMap<String, Integer>();
		variables = new ArrayList<Variable>();

		txtData = new HashMap<String, ArrayList<Double>>();
		shreadDic();
		newVariableNames = createDataVars();
		makeFipsHash();
		readTxt();

		dataSet = makeDataSet();
		return dataSet;
	}

	private void makeFipsHash() {
		Set<Integer> ids = idToFips.keySet();
		for (Integer id : ids) {
			String fips = idToFips.get(id);
			fipsToID.put(fips, id);
		}

	}

	// private void testFips() {
	// Set<Integer> it = idToFips.keySet();
	// HashSet<String> fipsSet = new HashSet<String>();
	// for (Integer id : it) {
	// String fips = idToFips.get(id);
	// if (fipsSet.contains(fips)) {
	// logger.severe("Hit duplicate fips, very bad!!! :" + fips);
	// Exception ex = new Exception();
	// ex.printStackTrace();
	// } else {
	// fipsSet.add(fips);
	// }
	//
	// }
	// }

	private ArrayList<String> createDataVars() {
		ArrayList<String> varNames = new ArrayList<String>();
		ArrayList<Variable> pageVars = new ArrayList<Variable>();
		ArrayList<Variable> columnVars = new ArrayList<Variable>();
		ArrayList<Variable> dataVars = new ArrayList<Variable>();

		for (Variable var : variables) {
			if (var.name.equals("State-county")) {
				continue;
			}
			if (var.dim.equals(VarDimension.PAGE)) {
				pageVars.add(var);
			}
			if (var.dim.equals(VarDimension.COLUMN)) {
				columnVars.add(var);
			}
			if (var.dim.equals(VarDimension.NONE)) {
				dataVars.add(var);

			}

		}

		for (Variable pageVar : pageVars) {
			Iterator<String> pageVarRecodes = pageVar.recode.values()
					.iterator();
			while (pageVarRecodes.hasNext()) {
				String pageVarRecode = pageVarRecodes.next();
				for (Variable colVar : columnVars) {
					Iterator<String> columnVarRecodes = colVar.recode.values()
							.iterator();
					while (columnVarRecodes.hasNext()) {
						String colVarRecode = columnVarRecodes.next();
						for (Variable dataVar : dataVars) {
							String dataVarName = dataVar.name;
							String newVarName = dataVarName + pageVarRecode
									+ colVarRecode;
							varNames.add(newVarName);

						}

					}

				}
			}

		}

		return varNames;

	}

	private DataSetForApps makeDataSet() {
		Object[] varNames = newVariableNames.toArray();
		String[] varStrings = new String[varNames.length];
		for (int i = 0; i < varNames.length - 1; i++) {
			varStrings[i] = (String) varNames[i];
		}

		GeoDataUSCounties counties = new GeoDataUSCounties();
		DataSetForApps countyData = counties.getDataForApps();
		logger.finest(countyData.getDataSourceName());
		Shape[] countyBoundaries = countyData.getShapeData();
		String[] allFips = countyData.getStringArrayDataByName("FIPS");
		HashMap<String, Shape> shapeHash = new HashMap<String, Shape>();
		for (int i = 0; i < allFips.length; i++) {
			shapeHash.put(allFips[i], countyBoundaries[i]);
		}
		Set<String> tempFips = txtData.keySet();
		ArrayList<Shape> tempShapes = new ArrayList<Shape>();
		ArrayList<ArrayList<Double>> tempNums = new ArrayList<ArrayList<Double>>();
		for (String fip : tempFips) {
			if (shapeHash.containsKey(fip)) {

				tempShapes.add(shapeHash.get(fip));

				ArrayList<Double> nums = txtData.get(fip);
				tempNums.add(nums);
				// logger.info("nums " + nums);

			}
		}
		ArrayList<double[]> tempArrays = new ArrayList<double[]>();

		for (@SuppressWarnings("unused")
		Object element : varNames) {
			tempArrays.add(new double[tempShapes.size()]);
		}
		Shape[] newShapes = new Shape[tempShapes.size()];

		for (int i = 0; i < tempShapes.size(); i++) {
			ArrayList<Double> nums = tempNums.get(i);
			for (int j = 0; j < nums.size(); j++) {
				tempArrays.get(j)[i] = nums.get(j).doubleValue();
			}
			newShapes[i] = tempShapes.get(i);
		}

		Object[] data = new Object[varStrings.length + 2];
		data[0] = varStrings;
		for (int i = 0; i < varStrings.length; i++) {
			data[i + 1] = tempArrays.get(i);

		}
		data[data.length - 1] = newShapes;
		DataSetForApps seerDataSet = new DataSetForApps(data);

		logger.finest("size :" + seerDataSet.getNumberNumericAttributes());
		return seerDataSet;
	}

	private void readTxt() {
		try {
			FileInputStream fis;

			fis = new FileInputStream(txtFileLocation);

			BufferedReader input = new BufferedReader(
					new InputStreamReader(fis));
			String line = "";

			Pattern tabPattern = Pattern.compile("\t");
			int nVars = variables.size();
			logger.finest("nVars = " + nVars);

			ArrayList<Integer> dataPlaces = new ArrayList<Integer>();
			for (int i = 0; i < variables.size(); i++) {
				Variable var = variables.get(i);
				if (var.dim == VarDimension.NONE) {
					dataPlaces.add(i);
				}
			}

			int[] intDataPlaces = new int[dataPlaces.size()];
			for (int i = 0; i < dataPlaces.size(); i++) {
				intDataPlaces[i] = dataPlaces.get(i);
			}

			// Set<Integer> ids = idHash.keySet();
			while ((line = input.readLine()) != null) {

				line = line.replaceAll("[,&]", "");

				if (logger.isLoggable(Level.FINEST)) {
					logger.finest("*******");
					logger.finest(line);
				}
				String[] lineContents = tabPattern.split(line);

				String id = lineContents[fipsPlace];
				Integer idInt = Integer.valueOf(id);
				String fips = idToFips.get(idInt);

				ArrayList<Double> nums = txtData.get(fips);
				if (nums == null) {
					nums = new ArrayList<Double>();

				}

				for (int dataPlace : intDataPlaces) {
					String next = lineContents[dataPlace];// +1 because we

					if (next.equals("~")) {
						nums.add(Double.NaN);
					} else {
						Double d = createDouble(next);
						nums.add(d);
					}
				}
				txtData.put(fips, nums);

			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}

	}

	private Double createDouble(String next) {
		Double d = null;
		try {
			d = Double.valueOf(next);
		} catch (NumberFormatException e) {
			logger.finest(" hit bad number, replacing with NAN");
			e.printStackTrace();
			d = Double.NaN;
		}
		return d;
	}

	private void shreadDic() {
		try {
			FileInputStream fis;

			fis = new FileInputStream(dicFileLocation);

			BufferedReader input = new BufferedReader(
					new InputStreamReader(fis));
			String line = "";
			String exportHeader = "[Export Options]";
			String varHeader = "[Variables]";
			String recodeHeader = "[Format=";

			while ((line = input.readLine()) != null) {
				if (line.contains(exportHeader)) {
					logger.finest("hit export options");
					extractOptions(input);
				}
				if (line.contains(varHeader)) {
					logger.finest("hit variable info");
					extractSeerVariableNames(input);
					for (Variable var : variables) {
						if (var.dim == null) {
							var.dim = VarDimension.NONE;
						}
					}
				}
				if (line.contains(recodeHeader)) {
					logger.info("hit recode");
					logger.info("line = " + line);
					String variableName = extractRecodeVarName(line);
					logger.info("varName = " + variableName);
					HashMap<Integer, String> recode = new HashMap<Integer, String>();

					if (variableName.equals("State-county")) {
						extractRecode(input, recode, true);
					} else {
						extractRecode(input, recode, false);
					}

					Variable var = findVar(variableName);
					var.recode = recode;

				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private Variable findVar(String variableName) {
		Variable var = null;

		for (Variable loopVar : variables) {
			if (loopVar.name.equals(variableName)) {
				return loopVar;
			}

		}
		return var;
	}

	int numAdded = 0;

	private void extractRecode(BufferedReader input,
			HashMap<Integer, String> recode, boolean isFips) {
		while (true) {
			String line = null;
			try {
				line = input.readLine();
				line = line.trim();
			} catch (IOException e) {

				e.printStackTrace();
			}

			logger.finest("recode line:" + line);
			int equalsPlace = line.indexOf("=");
			if (equalsPlace > 0) {
				String intString = line.substring(0, equalsPlace);
				Integer id = Integer.valueOf(intString);
				String theRest = line.substring(equalsPlace + 1);
				theRest = theRest.replaceAll("\"", "");
				theRest = theRest.trim();
				// logger.finest("the rest:" + theRest);
				recode.put(id, theRest);

				if (isFips) {
					String fips = extractFips(line);
					idToFips.put(id, fips);
					numAdded++;
					logger.finest(numAdded + " adding id:" + id + " fips:"
							+ fips);

				}
			}

			if (line.equals("")) {
				logger.finest("no more names");
				return;
			}
		}

	}

	private String extractFips(String line) {
		String fips = "";
		int openPlace = line.lastIndexOf("(");
		int closePlace = line.lastIndexOf(")");
		if (openPlace >= 0 && closePlace >= 0) {
			fips = line.substring(openPlace + 1, closePlace);
		}
		logger.finest("fips = " + fips);

		return fips;

	}

	private String extractVarName(String line) {
		logger.finest("parsing: " + line);
		int nChars = "[Format=".length();
		line = line.substring(nChars);
		logger.finest("substring = " + line);
		line = line.substring(1, line.length());

		logger.finest("Variable name = " + line);
		return line;
	}

	private String extractRecodeVarName(String line) {
		logger.finest("parsing: " + line);
		int nChars = "[Format=".length();
		line = line.substring(nChars);
		logger.finest("substring = " + line);
		line = line.substring(0, line.length() - 1);

		logger.finest("Variable name = " + line);
		return line;
	}

	private void extractSeerVariableNames(BufferedReader input) {

		ArrayList<String> varInfoContents = new ArrayList<String>();
		while (true) {
			String line = null;

			try {
				line = input.readLine();
				line = line.trim();
				if (line.length() > 0) {
					varInfoContents.add(line);
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
			logger.finest("variableLine:" + line);
			if (line.equals("")) {
				logger.finest("no more variable name lines");
				processLineInfoContents(varInfoContents);
				return;
			}
		}
	}

	enum VarDimension {
		PAGE, COLUMN, ROW, NONE
	}

	private class Variable {
		String name;
		VarDimension dim;
		HashMap<Integer, String> recode;
	}

	private void processLineInfoContents(ArrayList<String> varInfoContents) {

		for (String line : varInfoContents) {
			int varNum = extractVarNum(line);
			Variable var = null;
			if (variables.size() < varNum || variables.get(varNum - 1) == null) {
				var = new Variable();
				variables.add(var);
			} else {
				var = variables.get(varNum - 1);
			}
			if (line.contains("Name")) {
				String name = extractVarName(line);
				var.name = name;
			} else if (line.contains("Dimension")) {
				var.dim = extractDimension(line);
			}

		}

	}

	private VarDimension extractDimension(String line) {
		int equalsPlace = line.indexOf("=");

		line = line.substring(equalsPlace + 1);

		if (line.equals("Page")) {
			return VarDimension.PAGE;
		} else if (line.equals("Column")) {
			return VarDimension.COLUMN;
		} else if (line.equals("Row")) {
			return VarDimension.ROW;
		} else {
			logger.severe("can't correctly parse dimension = " + line);
		}
		return null;
	}

	private int extractVarNum(String line) {
		String newLine = new String(line);
		newLine = newLine.replace("Var", "");
		newLine = newLine.replace("Name", "");
		newLine = newLine.replace("Dimension", "");
		newLine = newLine.replace("Base", "");
		int equalsPlace = newLine.indexOf("=");

		newLine = newLine.substring(0, equalsPlace);
		int intValue = Integer.valueOf(newLine);
		return intValue;
	}

	private void extractOptions(BufferedReader input) {

		while (true) {
			String line = null;
			try {
				line = input.readLine();
				line = line.trim();
			} catch (IOException e) {

				e.printStackTrace();
			}
			logger.finest("opt:" + line);
			if (line.equals("")) {
				logger.finest("you are out of options");
				return;
			}
		}

	}

	public String getDicFileLocation() {
		return dicFileLocation;
	}

	public void setDicFileLocation(String dicFileLocation) {
		this.dicFileLocation = dicFileLocation;
		String txtFileLocation = this.dicFileLocation.substring(0,
				this.dicFileLocation.length() - 4)
				+ ".txt";
		setTxtFileLocation(txtFileLocation);
	}

	public String getTxtFileLocation() {
		return txtFileLocation;
	}

	public void setTxtFileLocation(String txtFileLocation) {
		this.txtFileLocation = txtFileLocation;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName = "C:\\data\\grants\\keystone\\seer_anthony\\ACR_test3.dic";

		SeerStatReader reader = new SeerStatReader();

		reader.setDicFileLocation(fileName);

		DataSetForApps dataSet = reader.readFiles();

		// GeoVizToolkit gvt = new GeoVizToolkit();

		// gvt.setDataSet(dataSet);

		logger.info("All done!!!");
		logger.finest(dataSet.getNumberNumericAttributes() + " attributes");

	}
}
