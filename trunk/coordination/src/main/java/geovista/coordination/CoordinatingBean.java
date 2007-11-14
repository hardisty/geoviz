/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class CoordinatingBean
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: CoordinatingBean.java,v 1.2 2002/08/13 20:57:41 hardisty Exp $
 $Date: 2002/08/13 20:57:41 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *

 */


package geovista.coordination;
import geovista.common.event.IndicationEvent;
import geovista.common.event.IndicationListener;

import java.awt.Color;

import javax.swing.event.EventListenerList;

/**
 * This class demonstrates a simple bean that fires an event whenever one
 * of two member variables are set, adding them together.
 *
 * This class is a bean because it has a constructor which takes no arguments
 * (a null constructor) and it has "get" and "set" accessors for all non-static
 * and non-transient member variables.
 *
 * The methods "addActionListener", "removeActionListener", and "fireActionPerformed"
 * can be dropped in with no changes to classes that wish to fire ActionEvents.
 *
 *
 */
public class CoordinatingBean implements IndicationListener{

    private int argOne;
    private int argTwo;
    private int result;
    private int indication;
    private int[] selection;
    private EventListenerList listenerList = new EventListenerList();

    private static final String COMMAND_ADDITION_FINISHED = "Added";

    private Color aColor;

    //null constructor
    public CoordinatingBean() {
      System.out.println("CoordinatingBean, result = " + result);
    }

    //this is the only thing this class really does, besides bean bookkeeping
    private void doAddition(){
      result = argOne + argTwo;

    }

    //start accessors
    public void setArgOne (int argOne) {
      this.argOne = argOne;
      this.doAddition();
    }
    public int getArgOne () {
      return this.argOne;
    }

    public void setArgTwo (int argTwo) {
      this.argTwo = argTwo;
      this.doAddition();
      this.fireIndicationChanged(result);
    }
    public int getArgTwo () {
      return this.argTwo;
    }

    public void setResult(int result) {
      System.out.println("CoordinatingBean, setResult = " + result);
      this.result = result;
    }
    public int getResult() {
      return this.result;
    }

    public void setListenerList (EventListenerList listenerList) {
      this.listenerList = listenerList;
    }
    public EventListenerList getListenerList() {
      return this.listenerList;
    }

    public void indicationChanged(IndicationEvent e){
      this.indication = e.getIndication();
    }






    /**
     * adds an IndicationListener
     */
	public void addIndicationListener (IndicationListener l) {
		listenerList.add(IndicationListener.class, l);
	}
    /**
     * removes an IndicationListener from the component
     */
	public void removeIndicationListener (IndicationListener l) {
		listenerList.remove(IndicationListener.class, l);
	}
    /**
     * Notify all listeners that have registered interest for
     * notification on this event type. The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
	private void fireIndicationChanged (int newIndication) {

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
                    ((IndicationListener)listeners[i + 1]).indicationChanged(e);
                }
              }//next i

	}



  public int getIndication() {
    return indication;
  }

}
