/*
 * ColorClassifierEvent.java
 *
 * Created on December 3, 2004, 3:57 PM
 */

package edu.psu.geovista.symbolization.event;

/* -------------------------------------------------------------------
GeoVISTA Center (Penn State, Dept. of Geography)
Java source file for the class ColorClassifierEvent
Copyright (c), 2002, GeoVISTA Center
All Rights Reserved.
Original Author: Frank Hardisty
$Author: jmacgill $
$Id: ColorClassifierEvent.java,v 1.1 2004/12/08 18:08:54 jmacgill Exp $
$Date: 2004/12/08 18:08:54 $
Reference: Document no:
___ ___
------------------------------------------------------------------- *
*/


import java.util.EventObject;

import edu.psu.geovista.symbolization.ColorSymbolClassification;


/**
* An ColorClassifierEvent signals that a set of observations has been singled out.
* This is often because the user has somehow indicated that these observations
* are of interest
*
* The integers represents the indexes of that observation in the overall data set.
*
*/
public class ColorClassifierEvent extends EventObject {

private transient ColorSymbolClassification colorClasser;
public static final int SOURCE_ORIENTATION_X = 0;
public static final int SOURCE_ORIENTATION_Y = 1;
private transient int orientation;

/**
* The constructor is the same as that for EventObject, except that the
* colorClasser values are indicated.
*/

public ColorClassifierEvent(Object source, ColorSymbolClassification colorClasser){
super(source);
this.colorClasser = colorClasser;
this.orientation = -1;//none
}

//begin accessors
public void setColorSymbolClassification (ColorSymbolClassification colorClasser) {
this.colorClasser = colorClasser;
}
public ColorSymbolClassification getColorSymbolClassification () {
return this.colorClasser;
}

public void setOrientation (int orientation) {
this.orientation = orientation;
}
public int getOrientation() {
return this.orientation;
}

//end accessors

}