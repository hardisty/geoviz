/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ShapeFileProjection
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
          Jin Chen
 $Id: ShapeFileProjection.java,v 1.3 2005/04/15 18:03:28 hardisty Exp $
 $Date: 2005/04/15 18:03:28 $
 Reference:        Document no:
 ___                ___
 -------------------------------------------------------------------  *
 */
package geovista.readers.shapefile;

import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.logging.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import geovista.common.data.DataSetForApps;
import geovista.common.data.GeneralPathLine;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.geoviz.map.Projection;
import geovista.geoviz.map.ProjectionEquidistantConic;
import geovista.geoviz.sample.GeoData48States;
import geovista.geoviz.sample.GeoDataWestCoast;

/**
 * This class, when passed a ShapeFile, will project it using it's current
 * projection. The original spatial data will not be changed. An ActionEvent will be
 * fired as well. It will also project and fire an ActionEvent
 *  on being passed a new projection.
 */
public class ShapeFileProjection
    implements ActionListener {
  private transient DataSetForApps inputDataSetForApps;
  private transient DataSetForApps outputDataSetForApps;
  private transient DataSetForApps inputAuxSpatialData;
  private transient DataSetForApps outputAuxSpatialData;
  private transient Projection proj;
  private transient ProjectionEquidistantConic projEQ;
  private EventListenerList listenerList;
  protected final static Logger logger = Logger.getLogger(ShapeFileProjection.class.getName());
  public ShapeFileProjection() {
    super();

    //proj = new ProjectionIdentity();
    projEQ = new ProjectionEquidistantConic();
    listenerList = new EventListenerList();
  }
  /**
   * Add by Jin Chen. This method is to do projection on individual shape data instead of on
   * DataSetForApps data. The method project() seems not work since initProjection() need to be invoked before the
   * being invoked.
   * @param shpData
   * @return
   */
  public Shape[] doProjection(Shape[] shpData){
     proj = this.initProjection(shpData);
     return this.project(shpData);
  }
  /**
   *
   * @param shpFile
   * @return
   * @see       <doProjection()>
   */
  public Shape[] project(Shape[] shpFile) {
    Shape[] shpNew = new Shape[shpFile.length];
    if (shpFile instanceof GeneralPathLine[]){

      shpNew = projectPolygons(shpFile);
    }
    else if (shpFile instanceof GeneralPath[]){

        shpNew = projectPolygons(shpFile);

    } else if (shpFile instanceof Shape[]){
    	shpNew = projectPolygons(shpFile);
    }

    else{
        throw new IllegalArgumentException("ShapeFileProjection.project," +
                                           " unknown file type");
    }

    return shpNew;
  }

  //todo: write me
  private double[] findBBox(Shape[] shp) {
    double minX;
    double maxX;
    double minY;
    double maxY;
    minX = Double.MAX_VALUE;
    maxX = Double.MAX_VALUE * -1;
    minY = Double.MAX_VALUE;
    maxY = Double.MAX_VALUE * -1;

    for (int i = 0; i < shp.length; i++) {
      Shape sha = shp[i];

      Rectangle2D rect = sha.getBounds2D();

      if (rect.getMinX() < minX) {
        minX = rect.getMinX();
      }

      if (rect.getMaxX() > maxX) {
        maxX = rect.getMaxX();
      }

      if (rect.getMinY() < minY) {
        minY = rect.getMinY();
      }

      if (rect.getMinY() > maxY) {
        maxY = rect.getMinY();
      }
    }

    //centralLat = bBox[1] + bBox[3];
    //centralLong = bBox[0] + bBox[2];
    double[] returnNums = new double[] {
        minX, minY, maxX, maxY};

    return returnNums;
  }

  private Projection initProjection(Shape[] shp) {
    double[] bBox = null;

    bBox = this.findBBox(shp);
    // boundingBox: {xMin, yMin, xMax, yMax, zMin, zMax, mMin, mMax}
    double centralLat;

    // boundingBox: {xMin, yMin, xMax, yMax, zMin, zMax, mMin, mMax}
    double centralLong;

    // boundingBox: {xMin, yMin, xMax, yMax, zMin, zMax, mMin, mMax}
    double paraOne;

    // boundingBox: {xMin, yMin, xMax, yMax, zMin, zMax, mMin, mMax}
    double paraTwo;
    centralLat = bBox[1] + bBox[3];
    centralLat = centralLat / 2;
    centralLat = Math.toRadians(centralLat);
    centralLong = bBox[0] + bBox[2];
    centralLong = centralLong / 2;
    centralLong = Math.toRadians(centralLong);

    //paraOne = centralLat - (paraDist * .25);
    paraOne = bBox[1];
    paraOne = Math.toRadians(paraOne);

    //paraTwo = centralLat + (paraDist * .25);
    paraTwo = bBox[3];
    paraTwo = Math.toRadians(paraTwo);
    projEQ.setCentralLatitude(centralLat);
    projEQ.setCentralMeridian(centralLong);
    projEQ.setStandardParallelOne(paraTwo);
    projEQ.setStandardParallelTwo(paraOne);

    return projEQ;
  }

  private Shape[] projectPolygons(Shape[] oldData) {
    Shape[] newData = new Shape[oldData.length];

    for (int i = 0; i < oldData.length; i++) {
      newData[i] = proj.project(oldData[i]);
    }

    return newData;
  }

  @SuppressWarnings("deprecation")
public void setInputDataSetForApps(DataSetForApps inputDataSet) {
    if (inputDataSet != null) {
      this.inputDataSetForApps = inputDataSet;
      this.outputDataSetForApps = new DataSetForApps();

      Object[] rawData = inputDataSet.getDataObjectOriginal();
      Object[] newData = new Object[rawData.length];

      for (int i = 0; i < rawData.length; i++) {
        newData[i] = rawData[i];
      }

      //ShapeFile spatialData = inputDataSet.getShapeFileData();
      Shape[] spatialData = inputDataSet.getShapeData();
      proj = this.initProjection(spatialData);

      int place = inputDataSet.getShapeDataPlace();
      newData[place] = this.project(spatialData);
      outputDataSetForApps.setDataObject(newData);
      this.fireActionPerformed("projected data");
      this.fireDataSetChanged(outputDataSetForApps);
    }
  }

  public DataSetForApps getOutputDataSetForApps() {
    return this.outputDataSetForApps;
  }

  public Object[] getOutputDataSet() {
    logger.finest("ShapeFileProject.getOutputDataSet(), Hi!!");
    return this.outputDataSetForApps.getDataObjectOriginal();
  }

  public void setInputDataSet(Object[] inputDataSet) {
    if (inputDataSet != null) {
      this.inputDataSetForApps = new DataSetForApps(inputDataSet);
      this.setInputDataSetForApps(this.inputDataSetForApps);
    }
  }

  public void setInputAuxiliaryData(Object[] auxDataSet) {
    if (auxDataSet != null) {
      this.inputAuxSpatialData = new DataSetForApps(auxDataSet);
      this.setInputAuxiliaryData(this.inputAuxSpatialData);
    }
  }

  @SuppressWarnings("deprecation")
public void setInputAuxiliaryData(DataSetForApps auxDataSet) {
    if (auxDataSet != null) {
      this.inputAuxSpatialData = auxDataSet;
      this.outputAuxSpatialData = new DataSetForApps();

      Object[] rawData = auxDataSet.getDataObjectOriginal();
      Object[] newData = new Object[rawData.length];

      for (int i = 0; i < rawData.length; i++) {
        newData[i] = rawData[i];
      }

      Shape[] spatialData = auxDataSet.getShapeData();
      int place = inputAuxSpatialData.getShapeDataPlace();
      newData[place] = this.project(spatialData);
      outputAuxSpatialData.setDataObject(newData);
      this.fireChangePerformed("projected aux data");

      //this.fireActionPerformed("projected aux data");
    }
  }

  public Object[] getOutputAuxiliarySpatialData() {
    if (this.outputAuxSpatialData != null) {
      return this.outputAuxSpatialData.getDataObjectOriginal();
    }

    return null;
  }

  public DataSetForApps getOutputAuxiliarySpatialDataForApps() {
    return this.outputAuxSpatialData;
  }

  public void setProj(Projection proj) {
    this.proj = proj;
  }

  public Projection getProj() {
    return this.proj;
  }

  public void setListenerList(EventListenerList listenerList) {
    this.listenerList = listenerList;
  }

  public EventListenerList getListenerList() {
    return this.listenerList;
  }

  public void actionPerformed(ActionEvent e) {
    Object obj = e.getSource();


    String className = obj.getClass().getName();

    Package pack = obj.getClass().getPackage(); //
    logger.finest(pack.getName()+"can't find package when we are an applet");
    if (className.equals("geovista.common.data.GeoDataWestCoast") ||
        className.equals("geovista.common.data.GeoData48States")) {
      if (e.getSource()instanceof GeoData48States) {
        GeoData48States data = (GeoData48States) e.getSource();
        this.setInputDataSet(data.getDataSet());
      }
      else if (e.getSource()instanceof GeoDataWestCoast) {
        GeoDataWestCoast data = (GeoDataWestCoast) e.getSource();
        this.setInputDataSet(data.getDataSet());
      }
    }
  }

  /**
   * implements ActionListener
   */
  public void addActionListener(ActionListener l) {
    listenerList.add(ActionListener.class, l);
  }

  /**
   * removes an ActionListener from the button
   */
  public void removeActionListener(ActionListener l) {
    listenerList.remove(ActionListener.class, l);
  }

  /**
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  protected void fireActionPerformed(String command) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    ActionEvent e = null;

    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ActionListener.class) {
        // Lazily create the event:
        if (e == null) {
          e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
        }

        ( (ActionListener) listeners[i + 1]).actionPerformed(e);
      }
    }
  }

  /**
   * implements ChangeListener
   */
  public void addChangeListener(ChangeListener l) {
    listenerList.add(ChangeListener.class, l);
  }

  /**
   * removes an ChangeListener from the button
   */
  public void removeChangeListener(ChangeListener l) {
    listenerList.remove(ChangeListener.class, l);
  }

  /**
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  protected void fireChangePerformed(String command) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    ChangeEvent e = null;

    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ChangeListener.class) {
        // Lazily create the event:
        if (e == null) {
          e = new ChangeEvent(this);
        }

        ( (ChangeListener) listeners[i + 1]).stateChanged(e);
      }
    }
  }

  /**
   * implements DataSetListener
   */
  public void addDataSetListener(DataSetListener l) {
    listenerList.add(DataSetListener.class, l);
  }

  /**
   * removes an DataSetListener from the button
   */
  public void removeDataSetListener(DataSetListener l) {
    listenerList.remove(DataSetListener.class, l);
  }

  /**
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  protected void fireDataSetChanged(DataSetForApps dataSet) {
    logger.finest("ShpToShp.fireDataSetChanged, Hi!!");
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    DataSetEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == DataSetListener.class) {
        // Lazily create the event:
        if (e == null) {
          e = new DataSetEvent(dataSet, this);

        }
        ( (DataSetListener) listeners[i + 1]).dataSetChanged(e);
      }
    }
  }

}
