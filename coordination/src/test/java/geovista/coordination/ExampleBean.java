/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ExampleBean
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: ExampleBean.java,v 1.2 2002/08/13 20:57:41 hardisty Exp $
 $Date: 2002/08/13 20:57:41 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *

 */

package geovista.coordination;

import geovista.common.event.IndicationEvent;
import geovista.common.event.IndicationListener;

import javax.swing.event.EventListenerList;

/**
 * This class demonstrates a simple bean that fires an event whenever one of two
 * member variables are set, adding them together.
 * 
 * This class is a bean because it has a constructor which takes no arguments (a
 * null constructor) and it has "get" and "set" accessors for all non-static and
 * non-transient member variables.
 * 
 * The methods "addActionListener", "removeActionListener", and
 * "fireActionPerformed" can be dropped in with no changes to classes that wish
 * to fire ActionEvents.
 * 
 * 
 */
public class ExampleBean implements IndicationListener {


	private int result;
	private int indication;
	private EventListenerList listenerList = new EventListenerList();

	// null constructor
	public ExampleBean() {
		System.out.println("ExampleBean, result = " + result);
	}

	public void setListenerList(EventListenerList listenerList) {
		this.listenerList = listenerList;
	}

	public EventListenerList getListenerList() {
		return listenerList;
	}

	public void indicationChanged(IndicationEvent e) {
		indication = e.getIndication();
	}

	/**
	 * adds an IndicationListener
	 */
	public void addIndicationListener(IndicationListener l) {
		listenerList.add(IndicationListener.class, l);
	}

	/**
	 * removes an IndicationListener from the component
	 */
	public void removeIndicationListener(IndicationListener l) {
		listenerList.remove(IndicationListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	public void fireIndicationChanged(int newIndication) {

		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		IndicationEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == IndicationListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new IndicationEvent(this, newIndication);
				}
				((IndicationListener) listeners[i + 1]).indicationChanged(e);
			}
		}// next i

	}

	public int getIndication() {
		return indication;
	}
	public void setIndication(int ind){
		this.indication = ind;
	}

}
