/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class SelectionListener
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: SelectionListener.java,v 1.2 2003/05/05 17:34:40 hardisty Exp $
 $Date: 2003/05/05 17:34:40 $
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
public interface SelectionListener extends EventListener {


  public void selectionChanged(SelectionEvent e);


}