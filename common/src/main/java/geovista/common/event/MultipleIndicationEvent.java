/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Nate Currit */

package geovista.common.event;

import java.util.EventObject;

/**
 * event that indicates multiple indications to handle multiple indications
 * frequently occur with hierarchical data
 * 
 */
public class MultipleIndicationEvent extends EventObject {
	/** Holds value of property indication. */
	private transient final int[] indications;

	/**
	 * Creates a new instance of MultiSelectionEvent
	 * 
	 * @param source
	 *            object where event originates
	 * @param indications
	 *            array of indicated (brushed over) array elements
	 */
	public MultipleIndicationEvent(Object source, int[] indications) {
		super(source);
		this.indications = indications;
	}

	/**
	 * Getter for first indication element. this getter is to be used by
	 * components that only want to handle single events, as per the old
	 * IndicationEvent
	 * 
	 * @return Value of first element of property indication.
	 */
	public int getIndication() {
		return indications[0];
	}

	/**
	 * Getter for property indications.
	 * 
	 * @return Value of property indication.
	 */
	public int[] getIndications() {
		return indications;
	}
}
