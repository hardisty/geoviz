/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.readers.join;

/*
 * Class to hold the results of a join operation. Immutable.
 */
public class JoinPoint {
	private final int left;
	private final int right;
	private final String value;

	public static int NO_COORDINATE = Integer.MIN_VALUE;

	// let's be immutable
	public JoinPoint(int left, int right, String value) {
		this.left = left;
		this.right = right;
		this.value = value;

	}

	public int getLeft() {
		return left;
	}

	public int getRight() {
		return right;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return left + ", " + right;
	}
}