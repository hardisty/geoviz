/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.toolkitcore.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Logger;

import geovista.common.data.DataSetForApps;

public class GTDReader {
	int ingroupStart = 6;
	int gnameStart = 11;
	int typeStart = 64;
	int iyearStart = 77;
	int imonthStart = 81;
	int idayStart = 83;
	int successStart = 85;
	int regionStart = 86;
	int entityStart = 88;
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
	final static Logger logger = Logger.getLogger(GTDReader.class.getName());

	private void parseString(String input) {
		String subString = "";

		subString = input.substring(ingroupStart, ingroupEnd);
		subString = subString.trim();
		// System.out.println("ingroup:" + subString);
		if (subString.equals("")) {
			ingroup.add(Integer.MIN_VALUE);
		} else {
			Integer intVal = Integer.valueOf(subString);
			ingroup.add(intVal);

			if (groupNames.containsKey(intVal) == false) {
				subString = input.substring(gnameStart, gnameEnd);
				subString = subString.trim();
				// logger.info("groupName:" + subString);
				groupNames.put(intVal, subString);
			}
		}

		subString = input.substring(typeStart, typeEnd);
		subString = subString.trim();
		// System.out.println("type:" + subString);
		if (subString.equals("")) {
			type.add(Integer.MIN_VALUE);
		} else {
			type.add(Integer.valueOf(subString));
		}

		subString = input.substring(iyearStart, iyearEnd);
		subString = subString.trim();
		// System.out.println("iyear:" + subString);
		if (subString.equals("")) {
			iyear.add(Integer.MIN_VALUE);
		} else {
			iyear.add(Integer.valueOf(subString));
		}

		subString = input.substring(imonthStart, imonthEnd);
		subString = subString.trim();
		// System.out.println("imonth:" + subString);
		if (subString.equals("")) {
			imonth.add(Integer.MIN_VALUE);
		} else {
			imonth.add(Integer.valueOf(subString));
		}

		subString = input.substring(idayStart, idayEnd);
		subString = subString.trim();
		// System.out.println("iday:" + subString);
		if (subString.equals("")) {
			iday.add(Integer.MIN_VALUE);
		} else {
			iday.add(Integer.valueOf(subString));
		}

		subString = input.substring(successStart, successEnd);
		subString = subString.trim();
		// System.out.println("success:" + subString);
		if (subString.equals("")) {
			success.add(Integer.MIN_VALUE);
		} else {
			success.add(Integer.valueOf(subString));
		}

		subString = input.substring(regionStart, regionEnd);
		subString = subString.trim();
		// System.out.println("region:" + subString);
		if (subString.equals("")) {
			region.add(Integer.MIN_VALUE);
		} else {
			region.add(Integer.valueOf(subString));
		}

		subString = input.substring(entityStart, entityEnd);
		subString = subString.trim();
		// System.out.println("entity:" + subString);
		if (subString.equals("")) {
			entity.add(Integer.MIN_VALUE);
		} else {
			entity.add(Integer.valueOf(subString));
		}
		subString = input.substring(countryStart, countryEnd);
		subString = subString.trim();
		// System.out.println("country:" + subString);
		if (subString.equals("")) {
			country.add(Integer.MIN_VALUE);
		} else {
			country.add(Integer.valueOf(subString));
		}

		subString = input.substring(stateStart, stateEnd);
		subString = subString.trim();
		// System.out.println("state:" + subString);
		if (subString.equals("")) {
			state.add(Integer.MIN_VALUE);
		} else {
			state.add(Integer.valueOf(subString));
		}

		subString = input.substring(nperpsStart, nperpsEnd);
		subString = subString.trim();
		// System.out.println("nperps:" + subString);
		if (subString.equals("")) {
			nperps.add(Integer.MIN_VALUE);
		} else {
			nperps.add(Integer.valueOf(subString));
		}

		subString = input.substring(nkillStart, nkillEnd);
		subString = subString.trim();
		// System.out.println("nkill:" + subString);
		if (subString.equals("")) {
			nkill.add(Integer.MIN_VALUE);
		} else {
			nkill.add(Integer.valueOf(subString));
		}

		subString = input.substring(nwoundStart, nwoundEnd);
		subString = subString.trim();
		// System.out.println("nwound:" + subString);
		if (subString.equals("")) {
			nwound.add(Integer.MIN_VALUE);
		} else {
			nwound.add(Integer.valueOf(subString));
		}

		subString = input.substring(damamtStart, damamtEnd);
		subString = subString.trim();
		// StringBuffer buff = new StringBuffer(subString);
		subString = subString.replaceAll(",", "");
		subString = subString.replaceAll(" ", "");

		if (isNumeric(subString) == false) {
			damamt.add(Integer.MIN_VALUE);

		} else if (subString.equals("")) {
			damamt.add(Integer.MIN_VALUE);
		} else {
			// System.out.println("damamt:" + subString);
			damamt.add(Integer.valueOf(subString));
		}
	}

	private static boolean isNumeric(String str) {
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

	private void readContents(File aFile) {

		try {
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			BufferedReader input = new BufferedReader(new FileReader(aFile));
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

	private void readIsoCodes(File isoFile) {
		try {
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			BufferedReader input = new BufferedReader(new FileReader(isoFile));
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
						logger
								.severe("hit duplicate gtd country name, bad bad");
					}
					isoGtdCountry.put(gtdName, isoCode);
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	private void findCountryCodes(File countryFile) {
		Scanner sc = null;
		try {
			sc = new Scanner(countryFile);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
			return;
		}
		String name = "";
		String nextThing = "";
		Integer id = null;
		while (sc.hasNext()) {

			nextThing = sc.next();
			// System.out.println(nextThing);
			// logger.info(nextThing);
			if (isNumeric(nextThing)) {
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
		// let's start be doing five year intervals

		return null;
	}

	public static void main(String[] args) {
		String fileName = "C:\\data\\geovista_data\\gtd\\gtd.txt";
		File gtdContents = new File(fileName);
		GTDReader reader = new GTDReader();

		fileName = "C:\\data\\geovista_data\\gtd\\iso_gtd.txt";
		File isoCodes = new File(fileName);
		reader.readIsoCodes(isoCodes);

		fileName = "C:\\data\\geovista_data\\gtd\\gtd_country.txt";
		File countryCodes = new File(fileName);
		reader.findCountryCodes(countryCodes);
		reader.readContents(gtdContents);
		reader.printMissingCountries();
		reader.simplifyAllNulls();
		System.out.println("All done!");
	}

}
