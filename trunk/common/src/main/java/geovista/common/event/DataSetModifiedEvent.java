/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventObject;

/**
 * An DataSetModifiedEvent signals that some new data is available for the
 * current data set. For example, a derived field has been calculated.
 * 
 */
public class DataSetModifiedEvent extends EventObject {

	private transient final Object[] dataSet;
	private transient final double[] newData;
	private final int eventType;
	public static final int TYPE_MODIFIED = 0;
	public static final int TYPE_REDUCED = 1;
	public static final int TYPE_EXTENDED = 2;

	/**
	 * The constructor is the same as that for EventObject, except that the
	 * dataSet and newData values are indicated. The dataSet is the unmodified
	 * data set. Clients will compare this array against their current arrays to
	 * find how this applies. This constructor internally sets the event as
	 * TYPE_EXTENDED.
	 */

	public DataSetModifiedEvent(Object source, Object[] dataSet,
			double[] newData) {
		super(source);
		eventType = DataSetModifiedEvent.TYPE_EXTENDED;
		this.dataSet = dataSet;
		this.newData = newData;
		// XXX question: should we check for the appropriate array length
		// here???

	}

	// begin accessors
	/**
	 * This method may be used to compare the broadcast dataSet reference to a
	 * previously broadcast one.
	 */

	public Object[] getDataSet() {
		return dataSet;
	}

	/**
	 * Returns only the new data to be appended, if any. If there isn't any,
	 * returns null.
	 */

	public double[] getNewData() {
		return newData;
	}

	/**
	 * Returns the type of event (TYPE_EXTENDED, TYPE_REDUCED, or TYPE_MODIFIED)
	 */

	public int getEventType() {
		return eventType;
	}

	// end accessors

}
