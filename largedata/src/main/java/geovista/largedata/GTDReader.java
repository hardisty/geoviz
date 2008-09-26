/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.largedata;

import java.awt.Shape;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import geovista.common.data.DataSetForApps;
import geovista.common.data.GeoDataSource;
import geovista.readers.shapefile.ShapeFileDataReader;

public class GTDReader implements GeoDataSource {
	int ingroupStart = 6;
	int gnameStart = 11;
	int typeStart = 64;// type of attack: assassination, bombing, etc.
	// category....
	int iyearStart = 77;
	int imonthStart = 81;
	int idayStart = 83;
	int successStart = 85;
	int regionStart = 86;
	int entityStart = 88;// entity that was attacked: police, bank, etc.
	// category....
	int countryStart = 90;
	int stateStart = 144;
	int nperpsStart = 148;
	int nkillStart = 212;
	int nwoundStart = 221;
	int damamtStart = 233;

	int ingroupEnd = 10;
	int gnameEnd = 60;
	int typeEnd = 65;
	int iyearEnd = 80;
	int imonthEnd = 82;
	int idayEnd = 84;
	int successEnd = 85;
	int regionEnd = 87;
	int entityEnd = 89;
	int countryEnd = 92;
	int stateEnd = 146;
	int nperpsEnd = 153;
	int nkillEnd = 215;
	int nwoundEnd = 225;
	int damamtEnd = 247;

	ArrayList<Integer> ingroup = new ArrayList<Integer>();
	HashMap<Integer, String> groupNames = new HashMap<Integer, String>();
	ArrayList<Integer> type = new ArrayList<Integer>();
	ArrayList<Integer> iyear = new ArrayList<Integer>();
	ArrayList<Integer> imonth = new ArrayList<Integer>();
	ArrayList<Integer> iday = new ArrayList<Integer>();
	ArrayList<Integer> success = new ArrayList<Integer>();
	ArrayList<Integer> region = new ArrayList<Integer>();
	ArrayList<Integer> entity = new ArrayList<Integer>();
	ArrayList<Integer> country = new ArrayList<Integer>();
	ArrayList<Integer> state = new ArrayList<Integer>();
	ArrayList<Integer> nperps = new ArrayList<Integer>();
	ArrayList<Integer> nkill = new ArrayList<Integer>();
	ArrayList<Integer> nwound = new ArrayList<Integer>();
	ArrayList<Integer> damamt = new ArrayList<Integer>();

	HashMap<Integer, String> typeNames = new HashMap<Integer, String>();
	HashMap<Integer, String> regionNames = new HashMap<Integer, String>();
	HashMap<Integer, String> entityNames = new HashMap<Integer, String>();
	HashMap<Integer, String> countryNames = new HashMap<Integer, String>();
	HashMap<Integer, String> stateNames = new HashMap<Integer, String>();
	HashMap<String, String> isoGtdCountry = new HashMap<String, String>();
	HashMap<String, String> gdtIsoCountry = new HashMap<String, String>();
	HashMap<String, Integer> isoID = new HashMap<String, Integer>();
	HashMap<Integer, String> IDIso = new HashMap<Integer, String>();

	private static String resourceGTD = "resources/gtd.txt.gz";
	private static String resourceGTD_country = "resources/gtd_country.txt";
	private static String resourceISO_GTD = "resources/iso_gtd.txt";
	private static String resourceISO_ID = "resources/iso_id.txt";
	private static String resource_shapefile = "resources/countries.shp";

	final static Logger logger = Logger.getLogger(GTDReader.class.getName());

	private void parseString(String input) {
		String subString = "";

		subString = input.substring(ingroupStart - 1, ingroupEnd);
		subString = subString.trim();
		// System.out.println("ingroup:" + subString);
		if (subString.equals("")) {
			ingroup.add(Integer.MIN_VALUE);
		} else {
			Integer intVal = Integer.valueOf(subString);
			ingroup.add(intVal);

			if (groupNames.containsKey(intVal) == false) {
				subString = input.substring(gnameStart - 1, gnameEnd);
				subString = subString.trim();
				// logger.info("groupName:" + subString);
				groupNames.put(intVal, subString);
			}
		}

		subString = input.substring(typeStart - 1, typeEnd);
		subString = subString.trim();
		// System.out.println("type:" + subString);
		if (subString.equals("")) {
			type.add(Integer.MIN_VALUE);
		} else {
			type.add(Integer.valueOf(subString));
		}

		subString = input.substring(iyearStart - 1, iyearEnd);
		subString = subString.trim();
		// System.out.println("iyear:" + subString);
		if (subString.equals("")) {
			iyear.add(Integer.MIN_VALUE);
		} else {
			iyear.add(Integer.valueOf(subString));
		}

		subString = input.substring(imonthStart - 1, imonthEnd);
		subString = subString.trim();
		// System.out.println("imonth:" + subString);
		if (subString.equals("") || isInteger(subString) == false) {
			imonth.add(Integer.MIN_VALUE);
		} else {
			imonth.add(Integer.valueOf(subString));
		}

		subString = input.substring(idayStart - 1, idayEnd);
		subString = subString.trim();
		// System.out.println("iday:" + subString);
		if (subString.equals("")) {
			iday.add(Integer.MIN_VALUE);
		} else {
			iday.add(Integer.valueOf(subString));
		}

		subString = input.substring(successStart - 1, successEnd);
		subString = subString.trim();
		// System.out.println("success:" + subString);
		if (subString.equals("")) {
			success.add(Integer.MIN_VALUE);
		} else {
			success.add(Integer.valueOf(subString));
		}

		subString = input.substring(regionStart - 1, regionEnd);
		subString = subString.trim();
		// System.out.println("region:" + subString);
		if (subString.equals("")) {
			region.add(Integer.MIN_VALUE);
		} else {
			region.add(Integer.valueOf(subString));
		}

		subString = input.substring(entityStart - 1, entityEnd);
		subString = subString.trim();
		// System.out.println("entity:" + subString);
		if (subString.equals("")) {
			entity.add(Integer.MIN_VALUE);
		} else {
			entity.add(Integer.valueOf(subString));
		}
		subString = input.substring(countryStart - 1, countryEnd);
		subString = subString.trim();
		// System.out.println("country:" + subString);
		if (subString.equals("")) {
			country.add(Integer.MIN_VALUE);
		} else {
			country.add(Integer.valueOf(subString));
		}

		subString = input.substring(stateStart - 1, stateEnd);
		subString = subString.trim();
		// System.out.println("state:" + subString);
		if (subString.equals("")) {
			state.add(Integer.MIN_VALUE);
		} else {
			state.add(Integer.valueOf(subString));
		}

		subString = input.substring(nperpsStart - 1, nperpsEnd);
		subString = subString.trim();
		// System.out.println("nperps:" + subString);
		if (subString.equals("")) {
			nperps.add(Integer.MIN_VALUE);
		} else {
			nperps.add(Integer.valueOf(subString));
		}

		subString = input.substring(nkillStart - 1, nkillEnd);
		subString = subString.trim();
		// System.out.println("nkill:" + subString);
		if (subString.equals("")) {
			nkill.add(Integer.MIN_VALUE);
		} else {
			nkill.add(Integer.valueOf(subString));
		}

		subString = input.substring(nwoundStart - 1, nwoundEnd);
		subString = subString.trim();
		// System.out.println("nwound:" + subString);
		if (subString.equals("")) {
			nwound.add(Integer.MIN_VALUE);
		} else {
			nwound.add(Integer.valueOf(subString));
		}

		subString = input.substring(damamtStart - 1, damamtEnd);
		subString = subString.trim();
		// StringBuffer buff = new StringBuffer(subString);
		subString = subString.replaceAll(",", "");
		subString = subString.replaceAll(" ", "");

		if (isInteger(subString) == false) {
			damamt.add(Integer.MIN_VALUE);

		} else if (subString.equals("")) {
			damamt.add(Integer.MIN_VALUE);
		} else {
			// System.out.println("damamt:" + subString);
			damamt.add(Integer.valueOf(subString));
		}
	}

	private static boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	private void simplifyNulls(ArrayList<Integer> intList) {

		for (int i = 0; i < intList.size(); i++) {
			Integer intVal = intList.get(i);
			if (intVal < 0 && intVal != Integer.MIN_VALUE) {

				// logger.severe("simplifying " + intVal);
				intVal = Integer.valueOf(Integer.MIN_VALUE);
				intList.set(i, intVal);

			}
		}
	}

	private void simplifyAllNulls() {
		simplifyNulls(type);
		simplifyNulls(iyear);
		simplifyNulls(imonth);
		simplifyNulls(iday);
		simplifyNulls(success);
		simplifyNulls(region);
		simplifyNulls(entity);
		simplifyNulls(country);
		simplifyNulls(state);
		simplifyNulls(nperps);
		simplifyNulls(nkill);
		simplifyNulls(nwound);
		simplifyNulls(damamt);
	}

	public void readContents() {

		try {
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			InputStream gtdStream = this.getClass().getResourceAsStream(
					GTDReader.resourceGTD);
			GZIPInputStream zipStream = new GZIPInputStream(gtdStream);
			BufferedReader input = new BufferedReader(new InputStreamReader(
					zipStream));
			try {
				String line = null; // not declared within while loop
				/*
				 * readLine is a bit quirky : it returns the content of a line
				 * MINUS the newline. it returns null only for the END of the
				 * stream. it returns an empty String if two newlines appear in
				 * a row.
				 */
				while ((line = input.readLine()) != null) {
					parseString(line);
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	private void readIsoCodes() {
		try {
			InputStream isoGtdStream = this.getClass().getResourceAsStream(
					GTDReader.resourceISO_GTD);
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
				String isoCode = "";
				String gtdName = "";
				while ((line = input.readLine()) != null) {
					isoCode = line.substring(0, 3);
					gtdName = line.substring(4, line.length());
					if (isoGtdCountry.containsKey(gtdName)) {
						logger.severe("hit duplicate gtd country name, "
								+ gtdName + ", bad bad");
					}
					isoGtdCountry.put(gtdName, isoCode);
					if (gdtIsoCountry.containsKey(isoCode)) {

					}
					gdtIsoCountry.put(isoCode, gtdName);
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	private void readIDCodes() {
		try {
			InputStream isoGtdStream = this.getClass().getResourceAsStream(
					GTDReader.resourceISO_ID);
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
				String isoCode = "";
				String ID = "";
				while ((line = input.readLine()) != null) {
					Scanner scan = new Scanner(line);
					ID = scan.next();

					if (scan.hasNext()) {
						isoCode = scan.next();
					} else {
						isoCode = "";
					}
					// ID = line.substring(4, line.length());
					if (isoID.containsKey(isoCode)) {
						logger.severe("hit duplicate iso, bad bad");
					}
					if (isoCode.equals("") == false) {
						if (logger.isLoggable(Level.FINEST)) {
							logger
									.info("id = " + ID + ", isoCode = "
											+ isoCode);
						}
						isoID.put(isoCode, Integer.valueOf(ID));
					}
					IDIso.put(Integer.valueOf(ID), isoCode);
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	private void findCountryCodes() {
		InputStream isoGtdStream = this.getClass().getResourceAsStream(
				GTDReader.resourceGTD_country);
		// use buffering, reading one line at a time
		// FileReader always assumes default encoding is OK!
		BufferedReader input = new BufferedReader(new InputStreamReader(
				isoGtdStream));
		Scanner sc = new Scanner(input);
		String name = "";
		String nextThing = "";
		Integer id = null;
		while (sc.hasNext()) {

			nextThing = sc.next();
			// System.out.println(nextThing);
			// logger.info(nextThing);
			if (isInteger(nextThing)) {
				if (id != null) {
					name = removeQuotes(name);
					name = name.trim();
					// System.out.println("id = " + id + ",name = " + name);
					if (isoGtdCountry.containsKey(name) == false) {
						// logger.severe("Can't find:" + name);
					}
					countryNames.put(id, name);
				}
				id = new Integer(nextThing);
				name = "";
			} else {
				name = name + " " + nextThing;
			}

			// if

		}

	}

	private void printMissingCountries() {
		Iterator<Integer> it = country.iterator();
		int counter = 0;
		while (it.hasNext()) {
			Integer code = it.next();
			String name = countryNames.get(code);
			if (isoGtdCountry.containsKey(name) == false && name != null) {
				logger.severe("Can't find:" + name);
				counter++;
			}
		}
		// logger.severe(counter + " missing");
	}

	private static String removeQuotes(String input) {
		String returnString = input.replaceAll("\"", "");
		returnString = returnString.replaceAll("\'", "");
		return returnString;
	}

	private DataSetForApps makeDataSetForApps() {

		int nCountries = 250;
		int periodicity = 5;
		int minYear = 1970;
		int maxYear = 1997;
		if (country == null) {
			logger.severe("data not loaded");
			return null;
		}
		int numBins = (maxYear - minYear) / periodicity;
		numBins++; // count from one
		int varTypes = 3;
		// killed, wounded, counts
		// (to add) attacktype, attackedentity
		Object[] numericalArrays = new Object[(numBins * varTypes) + 1];
		for (int i = 0; i < numBins * varTypes; i++) {
			double[] data = new double[nCountries];
			numericalArrays[i] = data;
		}
		ArrayList<String> yearHeadings = makeYearHeadings(periodicity, minYear,
				maxYear);

		for (int i = 0; i < country.size(); i++) {
			int year = iyear.get(i);
			// System.out.println(year);
			year = year - minYear;
			int bin = year / periodicity;
			double[] killedYear = (double[]) numericalArrays[bin];
			double[] woundedYear = (double[]) numericalArrays[bin + numBins];
			double[] countYear = (double[]) numericalArrays[bin + numBins
					+ numBins];
			Integer countryCode = country.get(i);
			int rowID = getID(countryCode);
			if (rowID >= 0) {
				Integer nKillInt = nkill.get(i);
				if (nKillInt != null
						&& nKillInt.equals(Integer.MIN_VALUE) == false) {
					// logger.info(nKillInt.toString());
					killedYear[rowID] = killedYear[rowID] + nKillInt;
				}
				Integer nWoundInt = nwound.get(i);
				if (nWoundInt != null
						&& nWoundInt.equals(Integer.MIN_VALUE) == false) {
					woundedYear[rowID] = woundedYear[rowID] + nWoundInt;
				}
				countYear[rowID]++;

			}

		}

		String[] varNames = new String[numericalArrays.length];

		String[] obsNames = makeObsNames(nCountries);
		numericalArrays[numericalArrays.length - 1] = obsNames;
		for (int i = 0; i < numBins; i++) {
			varNames[i] = "nKill_" + yearHeadings.get(i);
			varNames[i + numBins] = "nWound_" + yearHeadings.get(i);
			varNames[i + numBins + numBins] = "nAttack_" + yearHeadings.get(i);
		}
		varNames[varNames.length - 1] = "name";
		InputStream shpStream = this.getClass().getResourceAsStream(
				GTDReader.resource_shapefile);
		Shape[] geoms = ShapeFileDataReader.getShapes(shpStream);

		DataSetForApps dataSet = new DataSetForApps(varNames, numericalArrays,
				geoms);

		return dataSet;
	}

	private String[] makeObsNames(int nCountries) {
		String[] obsNames = new String[nCountries];
		for (int i = 0; i < nCountries; i++) {
			String name = getCountryName(i);
			obsNames[i] = name;
		}
		return obsNames;
	}

	private int getID(Integer countryCode) {
		if (countryCode < 0) {
			return countryCode;
		}
		String countryName = countryNames.get(countryCode);
		String ISOCode = isoGtdCountry.get(countryName);
		Integer id = isoID.get(ISOCode);
		if (id == null) {
			return Integer.MIN_VALUE;
		}
		return id;
	}

	private String getCountryName(int id) {

		String ISOCode = IDIso.get(id);
		String name = gdtIsoCountry.get(ISOCode);
		return name;
	}

	private ArrayList<String> makeYearHeadings(int periodicity, int minYear,
			int maxYear) {
		int numBins = (maxYear - minYear) / periodicity;
		ArrayList<String> headings = new ArrayList<String>();
		for (int i = 0; i <= numBins; i++) {// note the <= in the loop
			int binMin = (periodicity * i) + minYear;
			int binMax = binMin + periodicity;
			if (binMax > maxYear) {
				binMax = maxYear;
			}
			String label = String.valueOf(binMin).substring(2) + "-"
					+ String.valueOf(binMax).substring(2);
			System.out.println(label);
			headings.add(label);

		}

		return headings;

	}

	public DataSetForApps getDataForApps() {
		readIsoCodes();
		findCountryCodes();
		readContents();
		readIDCodes();
		// printMissingCountries();
		simplifyAllNulls();
		return makeDataSetForApps();

	}

	public static void main(String[] args) {

		GTDReader reader = new GTDReader();
		reader.readIsoCodes();
		reader.findCountryCodes();
		reader.readContents();
		reader.readIDCodes();
		reader.printMissingCountries();
		reader.simplifyAllNulls();
		DataSetForApps dataSet = reader.makeDataSetForApps();

		System.out.println("All done! we have this many obs:"
				+ dataSet.getNumObservations());

	}

}
