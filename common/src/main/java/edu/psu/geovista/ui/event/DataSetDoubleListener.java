/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class DataSetListener
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Xiping
 $Author: xpdai $
 $Id: DataSetDoubleListener.java,v 1.1 2004/10/29 17:30:33 xpdai Exp $
 $Date: 2004/10/29 17:30:33 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.ui.event;

import java.util.EventListener;


/**
 * This interface enables listening to senders of DataSetDoubleEvents.
 *
 * This interface also enables "fireDataSetDoubleChanged" methods in classes
 * that generate and broadcast DataSetDoubleEvents.
 *
 */
public interface DataSetDoubleListener extends EventListener {


  public void dataSetDoubleChanged(DataSetDoubleEvent e);


}
