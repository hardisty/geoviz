/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class FindArrayOrder
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: ArraySort2D.java,v 1.2 2003/04/25 18:21:38 hardisty Exp $
 $Date: 2003/04/25 18:21:38 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *

 */

package geovista.common.data;

import java.util.Arrays;

public class ArraySort2D {
	// XXX this is freezing the jvm (infinate loop?) on nulls
	// ... needs to be fixed + have some unit tests...
	//fix implemented, but not tested...

	private class SortableDoubleArray implements
			Comparable<SortableDoubleArray> {

		double[] data;
		int index;

		SortableDoubleArray(double[] data, int index) {
			this.data = data;
			this.index = index;
		}

		public int compareTo(SortableDoubleArray o) {
			if (data[index] < o.data[index]) {
				return -1;
			}
			if (data[index] > o.data[index]) {
				return 1;
			}
			// not greater, not lessor, must be equal.
			return 0;
		}

	}

	public void sortDouble(double[][] dataIn, int whichColumn) {
		SortableDoubleArray[] doubleArrays = new SortableDoubleArray[dataIn.length];
		for (int i = 0; i < dataIn.length; i++) {
			doubleArrays[i] = new SortableDoubleArray(dataIn[i], whichColumn);
		}
		Arrays.sort(doubleArrays);
		for (int i = 0; i < dataIn.length; i++) {
			dataIn[i] = doubleArrays[i].data;
		}

	}

	// what is this method supposed to do?
	// best guess: sort by that column...
	// strategy -- stuff everything into arraylists?
	// at any rate: must use arrays.sort
	// this can take an array of objects that implement Comparable
	public static void sortDouble_old(double[][] dataIn, int whichColumn) {

		double[] dataSorted = new double[dataIn.length];

		for (int i = 0; i < dataSorted.length; i++) {
			dataSorted[i] = dataIn[i][whichColumn];
		} // next i
		Arrays.sort(dataSorted);

		// make a run length list
		// double[] uniqueVals = new double[dataSorted.length];
		int[] numOccur = new int[dataSorted.length];
		int counter = 0;
		// uniqueVals[0] = dataSorted[0];
		numOccur[0] = 1;
		int totalUnique = 1;
		for (int i = 1; i < dataSorted.length; i++) {
			if (dataSorted[i] != dataSorted[i - 1]) { // hit a new one.
				counter++;
				dataSorted[counter] = dataSorted[i];
				numOccur[counter] = 1;
				totalUnique++;
			} else { // it's a repeat
				numOccur[counter]++;
			}// end if
		} // next i
		double[] uniqueValsSorted = new double[totalUnique];
		System.arraycopy(dataSorted, 0, uniqueValsSorted, 0, totalUnique);

		// now walk through original data to find the order
		// we need to keep track of how many "hits" we've had

		double[] tempTuple = new double[dataIn[0].length]; // holding copies
		// while(madeSwitch) {
		int[] hitCount = new int[dataIn.length];
		for (int i = 0; i < dataIn.length; i++) {
			// now find each data value
			double val = dataIn[i][whichColumn];
			int uniquePos = Arrays.binarySearch(uniqueValsSorted, val);
			int pos = 0;
			for (int j = 0; j < uniquePos; j++) {
				pos = pos + numOccur[j];
			}

			pos = pos + hitCount[uniquePos]; // if we are not the first of
			// that val

			// pos is where this data tuple is "supposed" to be. Is it there
			// already?
			// or is the "right" value there already?

			double targetVal = dataIn[pos][whichColumn];
			if (pos == i) {
				// direct hit!
				hitCount[uniquePos]++;// keep track of how many of that val we
				// passed in the correct position as i
			} else if (val == targetVal) {
				// put this in the right place
				while (val == targetVal) {
					pos++;
					targetVal = dataIn[pos][whichColumn];
				}
				tempTuple = dataIn[pos];
				dataIn[pos] = dataIn[i];
				dataIn[i] = tempTuple;

				i--;// try again???
				// now put this in the "pos"
				// if not... switch 'em
			} else {
				tempTuple = dataIn[pos];
				dataIn[pos] = dataIn[i];
				dataIn[i] = tempTuple;

				i--;// try again???

			}// end if
		}// next i

		// }//while switch
	}// end method

	public static void sortObject2(Object[][] dataIn, int whichColumn) {

		Object[] dataSorted = new Object[dataIn.length];

		int numNulls = 0;
		// count nulls and copy other items
		int nonNullCounter = 0;
		for (int i = 0; i < dataSorted.length; i++) {
			if (dataIn[i][whichColumn] == null) {
				numNulls++;
			} else {
				dataSorted[nonNullCounter] = dataIn[i][whichColumn];
				nonNullCounter++;
			}
		} // next i

		// because we can't Arrays.binarySearch or .sort null values, we are
		// going to remove
		// and save them now, and add them back at the end.
		Object[][] nullData = null;
		if (numNulls > 0) {
			dataSorted = new Object[dataIn.length - numNulls];
			nullData = new Object[numNulls][dataIn[0].length];
			int nullCounter = 0;
			nonNullCounter = 0;
			for (int i = 0; i < dataIn.length; i++) {
				if (dataIn[i][whichColumn] == null) {
					nullData[nullCounter] = dataIn[i];
					nullCounter++;
				} else {
					dataSorted[nonNullCounter] = dataIn[i][whichColumn];
					nonNullCounter++;
				}// end if
			}// next i
		}// end if

		// Arrays.sort(dataSorted,0,(nonNullCounter));//sort the non-null items
		Arrays.sort(dataSorted);
		// make a run length list
		// Object[] uniqueVals = new Object[dataSorted.length];
		int[] numOccur = new int[dataSorted.length];
		int counter = 0;
		// uniqueVals[0] = dataSorted[0];
		numOccur[0] = 1;
		int totalUnique = 1;

		for (int i = 1; i < dataSorted.length - numNulls; i++) {
			if (dataSorted[i] != dataSorted[i - 1]) { // hit a new one.
				counter++;
				dataSorted[counter] = dataSorted[i];
				numOccur[counter] = 1;
				totalUnique++;
			} else { // it's a repeat
				numOccur[counter]++;
			}// end if
		} // next i
		if (numNulls > 0) {
			numOccur[totalUnique] = numNulls;
		}
		Object[] uniqueValsSorted = new Object[totalUnique];
		System.arraycopy(dataSorted, 0, uniqueValsSorted, 0, totalUnique);

		// now walk through original data to find the order
		// we need to keep track of how many "hits" we've had

		Object[] tempTuple = new Object[dataIn[0].length]; // holding copies
		// while(madeSwitch) {
		int[] hitCount = new int[dataIn.length];
		for (int i = 0; i < dataIn.length; i++) {
			// now find each data value
			Object val = dataIn[i][whichColumn];
			int uniquePos = 0;
			if (val != null) {
				uniquePos = Arrays.binarySearch(uniqueValsSorted, val);
			} else {
				uniquePos = uniqueValsSorted.length;
			}
			int pos = 0;
			for (int j = 0; j < uniquePos; j++) {
				pos = pos + numOccur[j];
			}
			// }

			pos = pos + hitCount[uniquePos]; // if we are not the first of
			// that val

			// pos is where this data tuple is "supposed" to be. Is it there
			// already?
			// or is the "right" value there already?

			Object targetVal = dataIn[pos][whichColumn];
			if (pos == i) {
				// direct hit!
				hitCount[uniquePos]++;// keep track of how many of that val we
				// passed in the correct position as i
			} else if (val == targetVal) {
				// put this in the right place
				while (val == targetVal) {
					pos++;
					targetVal = dataIn[pos][whichColumn];
				}
				tempTuple = dataIn[pos];
				dataIn[pos] = dataIn[i];
				dataIn[i] = tempTuple;

				i--;// try again???
				// now put this in the "pos"

				// if not... switch 'em
			} else {
				tempTuple = dataIn[pos];
				dataIn[pos] = dataIn[i];
				dataIn[i] = tempTuple;

				i--;// try again???

			}// end if
		}// next i

		// add the nulls back in
		if (numNulls > 0) {
			int nonNullPlace = dataIn.length - numNulls - 1;
			for (int i = 0; i < numNulls; i++) {
				dataIn[nonNullPlace] = nullData[i];
				nonNullPlace++;
			}
		}

	}// end method
}// end class
