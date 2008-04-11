/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.common.event;

import java.util.EventListener;

/**
 * This interface enables listening to senders of ClassBoundariesEvents.
 * 
 * This interface also enables "fireClassBoundariesChanged" methods in classes
 * that generate and broadcast ClassBoundariesEvents.
 * 
 */
public interface ClassBoundariesListener extends EventListener {

	public void classBoundariesChanged(ClassBoundariesEvent e);

}
