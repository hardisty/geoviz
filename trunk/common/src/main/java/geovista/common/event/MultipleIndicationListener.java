/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Nate Currit */
package geovista.common.event;

import java.util.EventListener;

/**
 * event handler to be implemented and fired on mouse over events in a GUI
 * environment
 * 
 */
public interface MultipleIndicationListener extends EventListener {
	/** */
	public void multipleIndicationChanged(MultipleIndicationEvent evt);
}
