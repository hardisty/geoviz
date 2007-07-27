/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class SubspaceListener
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: SubspaceListener.java,v 1.2 2003/05/05 17:34:40 hardisty Exp $
 $Date: 2003/05/05 17:34:40 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.ui.event;

import java.util.EventListener;


/**
 * This interface enables listening to senders of SubspaceEvents.
 *
 * This interface also enables "fireSubspaceChanged" methods in classes
 * that generate and broadcast SubspaceEvents.
 *
 */
public interface SubspaceListener extends EventListener {


  public void subspaceChanged(SubspaceEvent e);


}