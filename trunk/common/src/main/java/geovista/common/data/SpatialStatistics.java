/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class SpatialStatistics {

	private static final Logger logger = Logger
			.getLogger(SpatialStatistics.class.getName());

	/**
	 */
	// 
	public static double[] calculateMoranScores(double[] zData,
			SpatialWeights sw) {

		double[] moranScores = new double[zData.length];
		for (int i = 0; i < zData.length; i++) {
			List<Integer> bors = sw.getNeighbor(i);
			double sumScore = 0;
			for (int j = 0; j < bors.size(); j++) {
				sumScore = sumScore + zData[bors.get(j)];
			}
			moranScores[i] = zData[i] * sumScore;
		}
		return moranScores;
	}

	public static double[] calculateRandomMoranScores(double[] zData,
			SpatialWeights sw) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("calculating moran scores");
		}
		double[] moranData;
		moranData = new double[zData.length];
		System.arraycopy(zData, 0, moranData, 0, zData.length);

		shuffleCollection(moranData);

		double[] moranScores = new double[moranData.length];
		for (int i = 0; i < moranData.length; i++) {
			List<Integer> bors = sw.getNeighbor(i);
			double sumScore = 0;
			for (int j = 0; j < bors.size(); j++) {
				sumScore = sumScore + moranData[bors.get(j)];
			}
			moranScores[i] = moranData[i] * sumScore;
		}
		return moranScores;
	}

	private static void shuffleCollection(double[] moranData) {
		ArrayList<Double> randomList = new ArrayList();
		for (double d : moranData) {
			randomList.add(d);
		}
		Collections.shuffle(randomList);
		for (int i = 0; i < randomList.size(); i++) {
			moranData[i] = randomList.get(i);
		}
	}

	public static List<double[]> findMonteValues(double[] data, int iterations,
			SpatialWeights sw) {

		ArrayList<double[]> monteVals = new ArrayList<double[]>();
		for (double element : data) {
			double[] obsMonte = new double[iterations];
			monteVals.add(obsMonte);
		}

		for (int iteration = 0; iteration < iterations; iteration++) {
			double[] iterationVals = calculateRandomMoranScores(data, sw);
			for (int obs = 0; obs < data.length; obs++) {
				double[] monteArray = monteVals.get(obs);
				monteArray[iteration] = iterationVals[obs];
			}
		}

		return monteVals;
	}

	public static double[] findPValues(double[] zData, double[] moranValues,
			int numTries, SpatialWeights sw) {

		// if this approach uses too much memory, we could do it in iterations,
		// using the sqrt of
		// the number of iterations desired, and then averaging
		List<double[]> monteVals = findMonteValues(zData, numTries, sw);
		double[] pVals = new double[zData.length];

		double[] moranScores = SpatialStatistics
				.calculateMoranScores(zData, sw);

		for (int obs = 0; obs < zData.length; obs++) {
			pVals[obs] = DescriptiveStatistics.percentAbove(monteVals.get(obs),
					moranScores[obs]);
		}

		return pVals;
	}

} // end class
