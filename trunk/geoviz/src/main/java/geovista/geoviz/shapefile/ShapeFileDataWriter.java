/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ShapeFileDataWriter
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: ShapeFileDataWriter.java,v 1.2 2005/02/12 21:37:47 hardisty Exp $
 $Date: 2005/02/12 21:37:47 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package geovista.geoviz.shapefile;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.Vector;
import java.util.logging.Logger;

import geovista.common.data.DataSetForApps;


/**
 * Takes a file name and a set of shapes, and writes out a shapefile.
 *
 * also see DBaseFile, ShapeFile
 *
 */
public class ShapeFileDataWriter {
	protected final static Logger logger = Logger.getLogger(ShapeFileDataWriter.class.getName());
	/** uses old shapefile writer...
  public static void writeShapefile(GeneralPath[] paths, String fileName) {
  
      String baseName = fileName.substring(0, fileName.length() - 4);
      ShapeFile shapeFile = new ShapeFile();
      ShapeFileHeader header = new ShapeFileHeader();
      header.setShapeType(ShapeFile.SHAPE_TYPE_POLYGON);
      Rectangle2D unionRect = paths[0].getBounds2D();
      for (int i = 0; i < paths.length; i++) {
          GeneralPath path = paths[i];
          Rectangle2D rect = path.getBounds2D();
          unionRect = rect.createUnion(unionRect);
      }
      double[] theBox = {unionRect.getMinX(), unionRect.getMinY(),
                        unionRect.getMaxX(), unionRect.getMaxY(), -1, 1, -1,
                        1};
      header.setBoundingBox(theBox);
      shapeFile.setFileHeader(header);

      Vector theShapes = new Vector();
      ShapeFileRecordPolygon polygon;

      for (int i = 0; i < paths.length; i++) {
          polygon = new ShapeFileRecordPolygon();
          Rectangle2D bounds = paths[i].getBounds2D();
          double[] bbox = {bounds.getMinX(), bounds.getMinY(), bounds.getMaxX(),
                          bounds.getMaxY()};
          polygon.setBox(bbox);
          PathIterator pit = paths[i].getPathIterator(new AffineTransform());
          int nPoints = 0;
          int nParts = 0;
          float[] coords = {0, 1, 2, 3, 4, 5, 6};
          while (!pit.isDone()) {
              int segType = pit.currentSegment(coords);
              if (segType == PathIterator.SEG_MOVETO) {
                  nParts++;
              }
              nPoints++;
              pit.next();
          }
          logger.finest("nParts = " + nParts);
          logger.finest("nPoints = " + nPoints);
          int[] parts = new int[nParts];
          double[][] thePoints = new double[nPoints][2];
          pit = paths[i].getPathIterator(null); //fresh one
          nParts = 0;
          nPoints = 0;
          while (!pit.isDone()) {
              int segType = pit.currentSegment(coords);
              if (segType == PathIterator.SEG_MOVETO) {
                  parts[nParts] = nPoints;
                  nParts++;
              }
              thePoints[nPoints][0] = coords[0];
              thePoints[nPoints][1] = coords[1];
              nPoints++;
              pit.next();
          }
          polygon.setParts(parts);
          polygon.setNumParts(parts.length);
          polygon.setPoints(thePoints);
          polygon.setNumPoints(thePoints.length);
          theShapes.add(polygon);
      }
      shapeFile.setData(theShapes);
      try {
          shapeFile.write(fileName, baseName);
      } catch (Exception ex) {
          ex.printStackTrace();
      }

    }

    public static void main(String[] args) {
        String shapeFile = "C:/geovista_old/cartogram/testing/48states.shp";
        String newShapeFile = "C:/geovista_old/cartogram/testing/cartogram.shp";
        //genFile = "./map2.gen";
        //censusFile = "./census2.dat";

        //GeoData48States statesData = new GeoData48States();
        ShapeFileProjection shpProj = new ShapeFileProjection();
        ShapeFileDataReader reader = new ShapeFileDataReader();
        reader.setFileName(shapeFile);
        //reader.setFileName("./Export_Output.shp");
        shpProj.setInputDataSet(reader.getDataSet());
        //shpProj.setInputDataSet(reader.convertShpToShape(statesData.getDataSet()));
        Object[] dataSet = shpProj.getOutputDataSet();
        DataSetForApps dataSetApps = new DataSetForApps(dataSet);

        Shape[] paths = dataSetApps.getShapeData();
        GeneralPath[] newPaths = new GeneralPath[paths.length];
        for (int i = 0; i < paths.length; i++) {
            newPaths[i] = (GeneralPath) paths[i];
        }
        ShapeFileDataWriter.writeShapefile(newPaths, newShapeFile);


    }
*/


}
