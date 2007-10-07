/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class DataSetModifiedListener
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: DataSetModifiedListener.java,v 1.1 2004/05/05 16:33:31 hardisty Exp $
 $Date: 2004/05/05 16:33:31 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package geovista.common.event;

import java.util.EventListener;


/**
 * This interface enables listening to senders of DataSetEvents.
 *
 * This interface also enables "fireDataSetChanged" methods in classes
 * that generate and broadcast DataSetEvents.
 *
 */
public interface DataSetModifiedListener extends EventListener {


  public void dataSetModified(DataSetModifiedEvent e);


}
