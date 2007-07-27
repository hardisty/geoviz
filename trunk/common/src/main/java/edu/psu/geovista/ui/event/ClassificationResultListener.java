/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ClassificationListener
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Xiping Dai
 $Author: xpdai $
 $Id: ClassificationResultListener.java,v 1.2 2004/10/07 21:26:01 xpdai Exp $
 $Date: 2004/10/07 21:26:01 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.ui.event;

import java.util.EventListener;

/**
 * This interface enables listening to senders of ClassificationEvents.
 *
 * This interface also enables "fireClassificationChanged" methods in classes
 * that generate and broadcast ClassificationEvents.
 *
 */
public interface ClassificationResultListener extends EventListener {


  public void classificationResultChanged(ClassificationResultEvent e);


}
