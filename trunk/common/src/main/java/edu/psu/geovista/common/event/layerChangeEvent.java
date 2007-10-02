/*------------------------------------------------------------------------------
* GeoVISTA Center, Penn State Geography Deptartment*
* Copyright (c), 2002, GeoVISTA Center
* All Rights Researved.
*
* Original Authors: Bonan Li
* $Author: jmacgill $
*
* $Date: 2004/03/10 20:24:12 $
*
* $Id: layerChangeEvent.java,v 1.1 2004/03/10 20:24:12 jmacgill Exp $
*
* To Do:
*
 ------------------------------------------------------------------------------*/


package edu.psu.geovista.common.event;

import java.io.File;
import java.util.EventObject;

import edu.psu.geovista.data.geog.DataSetForApps;
public class layerChangeEvent extends EventObject {

  private int activateIdx;
  private int removedIdx;
  private File activateFile;
  private DataSetForApps dataSetForApp;

  public layerChangeEvent(Object source, int activateIdx, int removedIdx, File activateFile, DataSetForApps dataSetForApp) {
    super(source);
    this.activateIdx = activateIdx;
    this.removedIdx = removedIdx;
    this.activateFile = activateFile;
    this.dataSetForApp = dataSetForApp;
  }

  public int getActivatedIdx() {
    return this.activateIdx;
  }
  public int getRemovedIdx() {
    return this.removedIdx;
  }
  public File getActivateFile(){
    return this.activateFile;
  }
  public DataSetForApps getDataSetForApp(){
    return this.dataSetForApp;
  }
}