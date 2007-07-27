/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ColorArrayListener
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: jmacgill $
 $Id: ColorArrayListener.java,v 1.3 2004/12/03 19:27:10 jmacgill Exp $
 $Date: 2004/12/03 19:27:10 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.ui.event;

import java.util.EventListener;


/**
 * This interface enables listening to senders of SelectionEvents.
 *
 * This interface also enables "fireSelectionChanged" methods in classes
 * that generate and broadcast SelectionEvents.
 *
 */
public interface ColorArrayListener extends EventListener {


  public void colorArrayChanged(ColorArrayEvent e);


}