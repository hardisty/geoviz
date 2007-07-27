
/* -------------------------------------------------------------------
GeoVISTA Center (Penn State, Dept. of Geography)
Java source file for the class ColorClassifierListener
Copyright (c), 2002, GeoVISTA Center
All Rights Reserved.
Original Author: Frank Hardisty
$Author: jmacgill $
$Id: ColorClassifierListener.java,v 1.1 2004/12/08 18:08:54 jmacgill Exp $
$Date: 2004/12/08 18:08:54 $
Reference: Document no:
___ ___
------------------------------------------------------------------- *
*/

package edu.psu.geovista.symbolization.event;

import java.util.EventListener;


/**
* This interface enables listening to senders of SelectionEvents.
*
* This interface also enables "fireSelectionChanged" methods in classes
* that generate and broadcast SelectionEvents.
*
*/
public interface ColorClassifierListener extends EventListener {


public void colorClassifierChanged(ColorClassifierEvent e);


}