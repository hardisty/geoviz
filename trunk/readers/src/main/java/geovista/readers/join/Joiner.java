/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.readers.join;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Joiner {
	final static Logger logger = Logger.getLogger(Joiner.class.getName());

	public static List<JoinPoint> innerJoin(String[] leftValues,
			String[] rightValues) {
		ArrayList<JoinPoint> join = new ArrayList<JoinPoint>();

		Joiner j = new Joiner();

		IndexedString[] leftArray = createdIndexedArray(leftValues, j);

		IndexedString[] rightArray = createdIndexedArray(rightValues, j);

		for (IndexedString element : leftArray) {
			int found = Arrays.binarySearch(rightArray, element);
			if (found >= 0) {
				JoinPoint joinPoint = new JoinPoint(element.index,
						rightArray[found].index, element.data);
				join.add(joinPoint);
			}
		}

		return join;

	}

	public static List<JoinPoint> innerJoin(ArrayList<String> leftValues,
			ArrayList<String> rightValues) {
		String[] leftArray = new String[leftValues.size()];
		leftArray = leftValues.toArray(leftArray);

		String[] rightArray = new String[rightValues.size()];
		rightArray = rightValues.toArray(rightArray);
		return Joiner.innerJoin(leftArray, rightArray);

	}

	public static List<JoinPoint> leftOuterJoin(ArrayList<String> leftValues,
			ArrayList<String> rightValues) {
		String[] leftArray = new String[leftValues.size()];
		leftArray = leftValues.toArray(leftArray);

		String[] rightArray = new String[rightValues.size()];
		rightArray = rightValues.toArray(rightArray);
		return Joiner.leftOuterJoin(leftArray, rightArray);
	}

	private static IndexedString[] createdIndexedArray(String[] leftValues,
			Joiner j) {
		IndexedString[] leftArray = new IndexedString[leftValues.length];
		for (int i = 0; i < leftValues.length; i++) {
			IndexedString val = j.new IndexedString(leftValues[i], i);
			leftArray[i] = val;

		}
		Arrays.sort(leftArray);
		return leftArray;
	}

	public static List<JoinPoint> leftOuterJoin(String[] leftValues,
			String[] rightValues) {
		ArrayList<JoinPoint> join = new ArrayList<JoinPoint>();

		Joiner j = new Joiner();

		IndexedString[] leftArray = createdIndexedArray(leftValues, j);

		IndexedString[] rightArray = createdIndexedArray(rightValues, j);

		for (IndexedString element : leftArray) {
			int found = Arrays.binarySearch(rightArray, element);
			if (found >= 0) {
				JoinPoint joinPoint = new JoinPoint(element.index,
						rightArray[found].index, element.data);
				join.add(joinPoint);
			} else {
				JoinPoint joinPoint = new JoinPoint(element.index,
						JoinPoint.NO_COORDINATE, element.data);
				join.add(joinPoint);
			}
		}

		return join;

	}

	private class IndexedString implements Comparable<IndexedString> {
		String data;
		int index;

		IndexedString(String data, int index) {
			this.data = data;
			this.index = index;
		}

		public int compareTo(IndexedString otherVal) {
			return data.compareTo(otherVal.data);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return data.hashCode();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			return data.equals(obj);
		}

		@Override
		public String toString() {
			return data + ", " + index;
		}
	}

	// public static

	public static void main(String[] args) {
		String[] leftVals = { "carpie", "1", "2", "3", "3", "9" };
		String[] rightVals = { "3", "10", "spanky", "2", "3", "1" };

		List<JoinPoint> join = Joiner.innerJoin(leftVals, rightVals);
		for (JoinPoint j : join) {
			logger.info("" + j);
		}

		join = Joiner.leftOuterJoin(leftVals, rightVals);
		for (JoinPoint j : join) {
			logger.info("" + j);
		}

	}

}
