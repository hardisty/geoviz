/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class DescriptiveStatistics
 Original Author: Frank Hardisty
 $Author: hardistf $
 $Id: ComparableShapes.java,v 1.1 2005/12/05 20:17:05 hardistf Exp $
 $Date: 2005/12/05 20:17:05 $
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 -------------------------------------------------------------------   */

package geovista.common.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
		logger.finest("calculating moran scores");
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
