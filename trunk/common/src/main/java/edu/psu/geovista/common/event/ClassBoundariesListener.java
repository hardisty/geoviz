/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ClassBoundariesListener
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Xiping Dai
 $Author: xpdai $
 $Id: ClassBoundariesListener.java,v 1.1 2004/09/10 14:45:10 xpdai Exp $
 $Date: 2004/09/10 14:45:10 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.common.event;

import java.util.EventListener;

/**
 * This interface enables listening to senders of ClassBoundariesEvents.
 *
 * This interface also enables "fireClassBoundariesChanged" methods in classes
 * that generate and broadcast ClassBoundariesEvents.
 *
 */
public interface ClassBoundariesListener extends EventListener {


  public void classBoundariesChanged(ClassBoundariesEvent e);


}
