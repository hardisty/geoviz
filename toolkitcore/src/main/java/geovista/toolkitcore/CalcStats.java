package geovista.toolkitcore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

import geovista.common.data.DescriptiveStatistics;

public class CalcStats {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName = "C:\\temp\\gis.txt";
		CalcStats calc = new CalcStats(fileName);

		try {
			calc.processLineByLine();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log("done!");
	}

	/**
	 * @param aFileName
	 *            full name of an existing, readable file.
	 * @return
	 */
	public CalcStats(String aFileName) {
		fFile = new File(aFileName);
		outFile = new File("C:\\temp\\results.csv");

		try {
			outStream = new FileOutputStream(outFile);
			outWriter = new OutputStreamWriter(outStream);
		} catch (FileNotFoundException e) {
			outStream = null;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static int OE_PLACE = 9;

	/** Template method that calls {@link #processLine(String)}. */
	public final void processLineByLine() throws FileNotFoundException {
		ArrayList<Double> meanVals = new ArrayList<Double>();
		ArrayList<Double> varianceVals = new ArrayList<Double>();
		ArrayList<Double> skewnessVals = new ArrayList<Double>();
		ArrayList<Double> kurtosisVals = new ArrayList<Double>();
		ArrayList<Double> oeVals = new ArrayList<Double>();

		Double currentCluster = new Double(0d);

		Scanner scanner = new Scanner(fFile);
		try {
			double[] dVals = null;
			// first use a Scanner to get each line
			while (scanner.hasNextLine()) {
				ArrayList<Double> values = processLine(scanner.nextLine());
				Double thisCluster = values.get(1);
				if (thisCluster.equals(currentCluster)) {
					// add it
					oeVals.add(values.get(OE_PLACE));
				} else {// new Cluster
					if (oeVals.size() > 0) {
						dVals = new double[oeVals.size()];
						for (int i = 0; i < dVals.length; i++) {
							dVals[i] = oeVals.get(i);
						}
						calcStats(meanVals, varianceVals, skewnessVals,
								kurtosisVals, dVals);
					}
					currentCluster = thisCluster;
					oeVals.clear();
					oeVals.add(values.get(OE_PLACE));
				}

			}
			dVals = new double[oeVals.size()];
			for (int i = 0; i < dVals.length; i++) {
				dVals[i] = oeVals.get(i);
			}
			calcStats(meanVals, varianceVals, skewnessVals, kurtosisVals, dVals);

			String line = "";
			line = "Cluster id,mean,variance,skewness,kurtosis\n";
			try {
				outWriter.write(line);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			for (int i = 0; i < meanVals.size(); i++) {
				line = (i + 1) + "," + meanVals.get(i) + ","
						+ varianceVals.get(i) + "," + skewnessVals.get(i) + ","
						+ kurtosisVals.get(i) + "\n";
				try {
					outWriter.write(line);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			log(meanVals);
			log(varianceVals);

		} finally {
			// ensure the underlying stream is always closed
			scanner.close();
			try {
				outWriter.flush();
				outWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void calcStats(ArrayList<Double> meanVals,
			ArrayList<Double> varianceVals, ArrayList<Double> skewnessVals,
			ArrayList<Double> kurtosisVals, double[] dVals) {
		meanVals.add(DescriptiveStatistics.mean(dVals));
		varianceVals.add(DescriptiveStatistics.variance(dVals, false));
		skewnessVals.add(DescriptiveStatistics.skewness(dVals, false));
		kurtosisVals.add(DescriptiveStatistics.kurtosis(dVals, false));
	}

	/**
	 * Overridable method for processing lines in different ways.
	 * 
	 * <P>
	 * This simple default implementation expects simple name-value pairs,
	 * separated by an '=' sign. Examples of valid input :
	 * <tt>height = 167cm</tt>
	 * <tt>mass =  65kg</tt>
	 * <tt>disposition =  "grumpy"</tt>
	 * <tt>this is the name = this is the value</tt>
	 */
	protected ArrayList<Double> processLine(String aLine) {
		// use a second Scanner to parse the content of each line
		Scanner scanner = new Scanner(aLine);
		scanner.useDelimiter(" ");
		ArrayList<Double> values = new ArrayList<Double>();
		while (scanner.hasNext()) {
			String nextThing = scanner.next();
			nextThing = nextThing.trim();
			Double d = CalcStats.isNumeric(nextThing);
			if (d != null) {
				values.add(d);
			}
			// String name = scanner.next();
			// String value = scanner.next();
			// log("Name is : " + quote(name.trim()) + ", and Value is : "+
			// quote(value.trim()));

		}
		log(values);
		// (no need for finally here, since String is source)
		scanner.close();
		return values;
	}

	private static Double isNumeric(String str) {
		try {

			return Double.parseDouble(str);

		} catch (NumberFormatException nfe) {
			// swollow it...
		}
		return null;
	}

	// PRIVATE //
	private final File fFile;
	private final File outFile;
	private FileOutputStream outStream;
	private OutputStreamWriter outWriter;

	private static void log(Object aObject) {
		System.out.println(String.valueOf(aObject));
	}

}
