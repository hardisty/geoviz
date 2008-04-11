/* Licensed under LGPL v. 2.1 or any later version;
see GNU LGPL for details.
Original Author: Xiping Dai */

package geovista.common.event;

import java.util.EventObject;

/**
 * An ClassBoundariesEvent signals that the data has been classified into n
 * classes.
 * 
 * The integers represents the class of each observation in the overall data
 * set.
 * 
 */
public class ClassBoundariesEvent extends EventObject {

	private transient double[] classBoundaries;
	private transient double[] classBoundariesY;

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * classBoundaries values are indicated.
	 */

	public ClassBoundariesEvent(Object source, double[] classBoundaries) {
		super(source);
		this.classBoundaries = classBoundaries;
	}

	// begin accessors
	public void setClassBoundaries(double[] classBoundaries) {
		this.classBoundaries = classBoundaries;
	}

	public double[] getClassBoundaries() {
		return classBoundaries;
	}

	// end accessors

	public ClassBoundariesEvent(Object source, double[] classBoundariesX,
			double[] classBoundariesY) {
		super(source);
		classBoundaries = classBoundariesX;
		this.classBoundariesY = classBoundariesY;
	}

	public void setClassBoundariesY(double[] classBoundariesY) {
		this.classBoundariesY = classBoundariesY;
	}

	public double[] getClassBoundariesY() {
		return classBoundariesY;
	}

}
