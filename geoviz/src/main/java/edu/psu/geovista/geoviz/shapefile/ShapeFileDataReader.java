/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ShapeFileDataReader
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: ShapeFileDataReader.java,v 1.3 2005/04/11 17:37:47 hardisty Exp $
 $Date: 2005/04/11 17:37:47 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.geoviz.shapefile;

import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.common.data.GeneralPathLine;
import edu.psu.geovista.common.event.DataSetEvent;
import edu.psu.geovista.common.event.DataSetListener;
import edu.psu.geovista.geoviz.dbase.DBaseFieldDescriptor;
import edu.psu.geovista.geoviz.dbase.DBaseFile;
import edu.psu.geovista.readers.csv.GeogCSVReader;
import edu.psu.geovista.readers.geog.AttributeDescriptionFile;
import edu.psu.geovista.readers.geog.ShapeFile;
import edu.psu.geovista.readers.geog.ShapeFileRecordPoint;
import edu.psu.geovista.readers.geog.ShapeFileRecordPolyLine;
import edu.psu.geovista.readers.geog.ShapeFileRecordPolygon;

/**
 * Takes a file name and returns an Object[] with:
 * Object[0] = names of variables
 * 0bject[1] = data (double[], int[], or String[])
 * 0bject[1] = data (double[], int[], or String[])
 * ...
 * Object[n-1] = the shapefile data
 *
 * also see DBaseFile, ShapeFile
 *
 */
public class ShapeFileDataReader
    implements Serializable {
	protected final static Logger logger = Logger.getLogger(ShapeFileDataReader.class.getName());
  public static final String COMMAND_DATA_SET_MADE = "dataMade";
  public static final int FILE_TYPE_DBF = 0;
  public static final int FILE_TYPE_CSV = 1;

  protected transient DataSetForApps dataForApps;
  protected transient String fileName;
  protected transient EventListenerList listenerList;

  private transient long pointCount = 0;

  public ShapeFileDataReader() {
    super();
    listenerList = new EventListenerList();

  }

  private Shape[] makeShapes(ShapeFile shp) {
    Shape[] shapes = (Shape[])this.transform(shp);

    return shapes;

  }

  //return the data without attribute names
  public Object[] getDBData() {
    Object[] dbData = null;

    try {
      String dbFileName = fileName + ".dbf";
      DBaseFile dbf = new DBaseFile(dbFileName);
      dbData = new Object[dbf.getDataSet().length];
      for (int i = 0; i < dbData.length - 1; i++)
        dbData[i] = dbf.getDataSet()[i + 1];
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return dbData;
  }

  //return attribute names of the DB Data
  public String[] getAttributeNames() {
    String[] names = null;
    Object[] dbData = null;
    DBaseFieldDescriptor descriptor;
    try {
      String dbFileName = fileName + ".dbf";
      DBaseFile dbf = new DBaseFile(dbFileName);
      dbData = dbf.getDataSet();
      names = new String[dbData.length - 1];
      for (int i = 0; i < dbData.length - 1; i++) {
        descriptor = dbf.getRecordDiscriptor(i + 1);
        names[i] = descriptor.getFieldName();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return names;
  }

  protected DataSetForApps makeDataSetForApps(String fileName) {
    logger.finest("debug");
    Object[] shpData = null;
    ShapeFile shpFile = null;
    try {
      String dbFileName = fileName + ".dbf";
      DBaseFile db = new DBaseFile(dbFileName);
      Object[] dbData = db.getDataSet();
      shpData = new Object[dbData.length + 2];
      for (int i = 0; i < dbData.length; i++) {
        shpData[i] = dbData[i];
      }

      shpFile = new ShapeFile(fileName + ".shp");
      shpData[dbData.length] = this.transform(shpFile);

      AttributeDescriptionFile desc = null;

      desc = new AttributeDescriptionFile(fileName + ".desc");

      shpData[dbData.length + 1] = desc.getAttributeDescriptions();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    //this.fireActionPerformed(COMMAND_DATA_SET_MADE);
    int type = this.getDataSetForAppsSpatialType(shpFile.getFileHeader().getShapeType());
    this.dataForApps = new DataSetForApps(shpData);
    this.dataForApps.setSpatialType(type);
    return dataForApps;
  }

  private int getDataSetForAppsSpatialType(int shapeFileSpatialType){

    if (shapeFileSpatialType == ShapeFile.SHAPE_TYPE_POINT){
      return DataSetForApps.SPATIAL_TYPE_POINT;
    } else if (shapeFileSpatialType == ShapeFile.SHAPE_TYPE_POLYGON){
      return DataSetForApps.SPATIAL_TYPE_POLYGON;
    } else if (shapeFileSpatialType == ShapeFile.SHAPE_TYPE_POLYLINE){
      return DataSetForApps.SPATIAL_TYPE_LINE;
    } else {
      throw new IllegalArgumentException("unsupported spatial type used");
    }

  }

  private DataSetForApps makeDataSetForAppsCSV(String fileName) {
    Object[] shpData = null;
          ShapeFile shpFile = null;
    try {
      String dbFileName = fileName + ".csv";

      GeogCSVReader csv = new GeogCSVReader();
      FileInputStream inStream = new FileInputStream(dbFileName);
      Object[] dbData = csv.readFile(inStream);
      shpData = new Object[dbData.length + 2];
      for (int i = 0; i < dbData.length; i++) {
        shpData[i] = dbData[i];
      }
      shpFile = new ShapeFile(fileName + ".shp");
      shpData[dbData.length] = this.transform(shpFile);

      AttributeDescriptionFile desc = new AttributeDescriptionFile(fileName +
          ".desc");
      shpData[dbData.length + 1] = desc.getAttributeDescriptions();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    //this.fireActionPerformed(COMMAND_DATA_SET_MADE);
    int type = this.getDataSetForAppsSpatialType(shpFile.getFileHeader().getShapeType());
    this.dataForApps.setSpatialType(type);
    return dataForApps;

  }

  protected String removeExtension(String fileName) {
    String removed = fileName;
    int index = fileName.lastIndexOf(".");
    if (index > -1) { //if it was found
      removed = fileName.substring(0, index);
    }
    return removed;
  }

  private  String getExtension(String fileName){
   String extension = fileName;
   int index = fileName.lastIndexOf(".");
   if (index > -1) { //if it was found
     //int len = fileName.length();
     extension = fileName.substring(index+1);
   }
   return extension;
 }

  public void setDataForApps(DataSetForApps dataForApps) {
    this.dataForApps = dataForApps;
  }

  public DataSetForApps getDataForApps() {
    return this.dataForApps;
  }

  public Object[] getDataSet() {
    return this.dataForApps.getDataObjectOriginal();
  }

  public void setFileName (String fileName) {
  //this.fileName = this.removeExtension(fileName);
  if ((this.getExtension(fileName).toLowerCase()).equals("dbf")){
    this.fileName = this.removeExtension(fileName);
    this.dataForApps = this.makeDataSetForApps(this.fileName);
  }else if((this.getExtension(fileName).toLowerCase()).equals("csv")){
    this.fileName = this.removeExtension(fileName);
    this.dataForApps = this.makeDataSetForAppsCSV(this.fileName);
  }else if((this.getExtension(fileName).toLowerCase()).equals("shp")){
    this.fileName = this.removeExtension(fileName);
    String fileDbf = this.fileName + ".dbf";
    String fileCsv = this.fileName + ".csv";
    if ((new File(fileDbf)).exists()){
      this.dataForApps = this.makeDataSetForApps(this.fileName);
    }else if ((new File(fileCsv)).exists()){
      this.dataForApps = this.makeDataSetForAppsCSV(this.fileName);
    }else{
      try {
        throw new FileNotFoundException("Couldn't find dbf or csv for shapefile " + fileName);
      }
      catch (FileNotFoundException ex) {
        ex.printStackTrace();
      }
    }
  }
  this.fireActionPerformed(COMMAND_DATA_SET_MADE);
  this.fireDataSetChanged(this.dataForApps);
}

  public void setFileNameCSV(String fileName) {
    if ((this.getExtension(fileName).toLowerCase()).equals("dbf")){
      this.fileName = this.removeExtension(fileName);
      this.dataForApps = this.makeDataSetForApps(this.fileName);
    }else if((this.getExtension(fileName).toLowerCase()).equals("csv")){
      this.fileName = this.removeExtension(fileName);
      this.dataForApps = this.makeDataSetForAppsCSV(this.fileName);
    }else {
      this.fileName = this.removeExtension(fileName);
      this.dataForApps = this.makeDataSetForAppsCSV(this.fileName);
    }
    this.fireActionPerformed(COMMAND_DATA_SET_MADE);
    this.fireDataSetChanged(this.dataForApps);
  }

  public void setFileName(String fileName, int fileType) {
    if (fileType == ShapeFileDataReader.FILE_TYPE_DBF) {
      this.setFileName(fileName);
    }
    else if (fileType == ShapeFileDataReader.FILE_TYPE_CSV) {
      this.setFileNameCSV(fileName);
    }
    else {
      throw new IllegalArgumentException(
          "ShapeFileDataReader, unexpected file type");
    }
  }

  public String getFileName() {
    return this.fileName;
  }

  public String getShortFileName(){
    int idx = this.fileName.lastIndexOf("\\");
    return this.fileName.substring(idx);
  }

  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
  }

  private void readObject(ObjectInputStream ois) throws ClassNotFoundException,
      IOException {
    ois.defaultReadObject();
  }

  public Object[] convertShpToShape(Object[] dataIn) {
    if (dataIn[dataIn.length - 1] instanceof ShapeFile) {
      ShapeFile shp = (ShapeFile) dataIn[dataIn.length - 1];
      dataIn[dataIn.length - 1] = this.makeShapes(shp);

    }
    return dataIn;
  }

  public Object[] transform(ShapeFile shpFile) {
    if (shpFile == null) {
      throw new IllegalArgumentException(
          "null arg passed in to ShapeFileToShape.transform");
    }
    //Shape[] shpNew = new Shape[shpFile.getData().size()];
    Vector oldData = shpFile.getData();
    Object[] newData = null;

    switch (shpFile.getFileHeader().getShapeType()) {
      case ShapeFile.SHAPE_TYPE_POINT:
        newData = transformPoints(oldData);
        //outData.setSpatialType(outData.SPATIAL_TYPE_POINT);
        break;
      case ShapeFile.SHAPE_TYPE_POLYLINE:
        newData = transformPolylines(oldData);
        break;
      case ShapeFile.SHAPE_TYPE_POLYGON:
        newData = transformPolygons(oldData);
        //outData.setSpatialType(outData.SPATIAL_TYPE_POLYGON);
        break;
      default:
        throw new IllegalArgumentException("ShapeFileToShape.transform,"
                                           + " unknown file type or missing shapefile");
    }
    return newData;
  }

  private Point2D[] transformPoints(Vector shapeFileData) {
    logger.finest("transformPoints(Vector shapeFileData)...");

    Point2D[] newShapes = new Point2D[shapeFileData.size()];
    int currShape = 0;
    double x = 0;
    double y = 0;
    for (Enumeration e = shapeFileData.elements(); e.hasMoreElements(); ) {
      ShapeFileRecordPoint pointOld = (ShapeFileRecordPoint) e.nextElement();
      x = pointOld.getX();
      y = pointOld.getY();

      Point2D newShape = new Point2D.Double(x, y);

      newShapes[currShape] = newShape;
      currShape++;
    }

    return newShapes;
  }

  /**
   * Frank here.... this null in the array business didn't seem like a good idea
   * I'm going to change this to creating GeneralPathLine objects.
   * */
  private GeneralPathLine[] transformPolylines(Vector shapeFileData) {
    logger.finest("transformPolylines(Vector shapeFileData)...");

   int len = shapeFileData.size();
    GeneralPathLine[] newShapes = new GeneralPathLine[len];
    int currShape = 0;
    for (Enumeration e = shapeFileData.elements(); e.hasMoreElements(); ) {
      ShapeFileRecordPolyLine polyOld = (ShapeFileRecordPolyLine) e.nextElement();

      double[][] dataOld = polyOld.getPoints();
      //XXX next line
      pointCount = pointCount + dataOld.length;
      int[] parts = polyOld.getParts();
      int part = 0;
      GeneralPathLine newShape = new GeneralPathLine();
      for (int counter = 0; counter < dataOld.length; counter++) {
        if (part < parts.length && counter == parts[part]) {
          newShape.moveTo( (float) dataOld[counter][0],
                          (float) dataOld[counter][1]);
          part++;
        }
        else {
          newShape.lineTo( (float) dataOld[counter][0],
                          (float) dataOld[counter][1]);
        }
      }
      newShapes[currShape] = newShape;
      currShape++;
    }

    logger.finest("len = " + len +", final total = "+ currShape);

    //newShapes[len] = null;
    return newShapes;
  }

  private Shape[] transformPolygons(Vector shapeFileData) {
    logger.finest("transformPolygons(Vector shapeFileData)...");

    GeneralPath[] newShapes = new GeneralPath[shapeFileData.size()];
    int currShape = 0;
    for (Enumeration e = shapeFileData.elements(); e.hasMoreElements(); ) {
      ShapeFileRecordPolygon polyOld = (ShapeFileRecordPolygon) e.nextElement();

      double[][] dataOld = polyOld.getPoints();
      //XXX next line
      pointCount = pointCount + dataOld.length;
      int[] parts = polyOld.getParts();
      int part = 0;
      GeneralPath newShape = new GeneralPath();
      for (int counter = 0; counter < dataOld.length; counter++) {
        if (part < parts.length && counter == parts[part]) {
          newShape.moveTo( (float) dataOld[counter][0],
                          (float) dataOld[counter][1]);
          part++;
        }
        else {
          newShape.lineTo( (float) dataOld[counter][0],
                          (float) dataOld[counter][1]);
        }
      }
      newShapes[currShape] = newShape;
      currShape++;
    }

    return newShapes;
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
          e = new ActionEvent(this,
                              ActionEvent.ACTION_PERFORMED,
                              command);
        }
        ( (ActionListener) listeners[i + 1]).actionPerformed(e);
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
           ((DataSetListener)listeners[i + 1]).dataSetChanged(e);
          }
        }
  }

}
