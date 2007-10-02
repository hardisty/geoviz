/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class IndicationListener
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: IndicationListener.java,v 1.2 2003/05/05 17:34:39 hardisty Exp $
 $Date: 2003/05/05 17:34:39 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.common.event;

import java.util.EventListener;


/**
 * This interface enables listening to senders of IndicationEvents.
 *
 * This interface also enables "fireIndicationChanged" methods in classes
 * that generate and broadcast IndicationEvents.
 *
 */
public interface IndicationListener extends EventListener {


  public void indicationChanged(IndicationEvent e);


}