/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ClassColorListener
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Xiping Dai
 $Author: xpdai $
 $Id: ClassNumberListener.java,v 1.1 2004/03/03 18:16:28 xpdai Exp $
 $Date: 2004/03/03 18:16:28 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.ui.event;

import java.util.EventListener;


/**
 * This interface enables listening to senders of IndicationEvents.
 *
 * This interface also enables "fireClassNumberChanged" methods in classes
 * that generate and broadcast ClassNumberEvents.
 *
 */
public interface ClassNumberListener extends EventListener {


  public void classNumberChanged(ClassNumberEvent e);


}
