/*
 * MultiSelectionListener.java
 *
 * Created on October 20, 2003, 9:43 AM
 */
package geovista.common.event;

import java.util.EventListener;


/** event handler to be implemented and fired on mouse over events in a GUI
 * environment
 * @author Nate Currit
 */
public interface MultipleIndicationListener extends EventListener {
    /** */    
    public void multipleIndicationChanged(MultipleIndicationEvent evt);
}
