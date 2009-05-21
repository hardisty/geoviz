/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.rjava;

import java.util.EventListener;

import geovista.common.event.ColumnAppendedEvent;

/**
 * This interface enables listening to senders of DataSetEvents.
 * 
 * This interface also enables "fireDataSetChanged" methods in classes that
 * generate and broadcast DataSetEvents.
 * 
 */
public interface DataSetModifiedListener extends EventListener {

	public void dataSetModified(ColumnAppendedEvent e);

}
