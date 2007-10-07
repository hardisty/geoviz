/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ConditioningListener
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: ConditioningListener.java,v 1.2 2003/05/05 17:34:39 hardisty Exp $
 $Date: 2003/05/05 17:34:39 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package geovista.common.event;

import java.util.EventListener;


/**
 * This interface enables listening to senders of ConditioningEvents.
 *
 * This interface also enables "fireConditioningChanged" methods in classes
 * that generate and broadcast ConditioningEvents.
 *
 */
public interface ConditioningListener extends EventListener {


  public void conditioningChanged(ConditioningEvent e);


}