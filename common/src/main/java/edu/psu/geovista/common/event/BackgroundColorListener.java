/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class BackgroundColorListener
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: jmacgill $
 $Id: BackgroundColorListener.java,v 1.1 2004/12/03 19:27:04 jmacgill Exp $
 $Date: 2004/12/03 19:27:04 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.common.event;

import java.util.EventListener;


/**
 * This interface enables listening to senders of BackgroundColorEvents.
 *
 * This interface also enables "fireBackgroundColorChanged" methods in classes
 * that generate and broadcast BackgroundColorEvents.
 *
 */
public interface BackgroundColorListener extends EventListener {


  public void backgroundChanged(BackgroundColorEvent e);


}