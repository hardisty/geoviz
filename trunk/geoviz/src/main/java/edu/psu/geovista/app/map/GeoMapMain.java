/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class GeoMapMain
 Copyright (c), 2000, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: GeoMapMain.java,v 1.2 2005/09/15 15:01:29 hardisty Exp $
 $Date: 2005/09/15 15:01:29 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------   */


package edu.psu.geovista.app.map;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import edu.psu.geovista.app.coordinator.CoordinationManager;
import edu.psu.geovista.data.sample.GeoData48States;
import edu.psu.geovista.data.shapefile.ShapeFileDataReader;
import edu.psu.geovista.data.shapefile.ShapeFileProjection;
import edu.psu.geovista.data.shapefile.ShapeFileToShape;

public class GeoMapMain
    extends JFrame {

  public GeoMapMain(String name) {
    super(name);

  }

  public static void main(String[] args) {
    boolean useProj = true;
    boolean useResource = true;
//GeoMap map = new GeoMap();
    GeoMapMain app = new GeoMapMain("MapBean Main Class");
    app.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    app.getContentPane()
        .setLayout(new BoxLayout(app.getContentPane(), BoxLayout.X_AXIS));
    //app.getContentPane().add(map);
    app.pack();
    app.setVisible(true);

    GeoMap map2 = new GeoMap();
    app.getContentPane().add(map2);
    app.pack();
    app.setVisible(true);

    String fileName = "C:\\arcgis\\arcexe81\\Bin\\TemplateData\\USA\\counties.shp";
    fileName = "C:\\temp\\shapefiles\\intrstat.shp";
    fileName = "C:\\data\\geovista_data\\shapefiles\\larger_cities.shp";
    fileName = "C:\\data\\geovista_data\\shapefiles\\jin\\CompanyProdLL2000Def.shp";
    fileName = "C:\\data\\geovista_data\\Historical-Demographic\\census\\census80_90_00.shp";

    ShapeFileDataReader shpRead = new ShapeFileDataReader();
    shpRead.setFileName(fileName);
    CoordinationManager coord = new CoordinationManager();
    ShapeFileToShape shpToShape = new ShapeFileToShape();
    ShapeFileProjection shpProj = new ShapeFileProjection();
    GeoData48States stateData = new GeoData48States();
    coord.addBean(map2);
    coord.addBean(shpToShape);

    if (useResource) {

      shpProj.setInputDataSetForApps(stateData.getDataForApps());
    }
    else {
      if (useProj) {
        stateData.addActionListener(shpProj);
        shpProj.setInputDataSet(shpRead.getDataSet());
      }
    }
    Object[] dataSet = null;
    if (useProj) {
      dataSet = shpProj.getOutputDataSet();
    }
    else {
      dataSet = shpRead.getDataSet();
    }

    shpToShape.setInputDataSet(dataSet);
//shpToShape.setInputDataSet(dataSet);
//Rectangle2D rect = new Rectangle2D.Float(-30f,-30f,600f,600f);
//SpatialExtentEvent ext = new SpatialExtentEvent(map,rect);


    //ShapeFileProjection shpProj2 = new ShapeFileProjection();
    //Projection proj = shpProj.getProj();
    //shpProj2.setProj(proj);
    //shpProj2.setInputAuxiliaryData(stateData.getDataSet());

    //shpToShape2.setInputDataSet(shpProj2.getOutputAuxiliarySpatialData());
    //map2.setAuxiliarySpatialData(shpToShape2.getOutputDataSet());
    ShapeFileToShape shpToShape2 = new ShapeFileToShape();


    //map2.setAuxiliarySpatialData(shpToShape2.getOutputDataSet());

    ShapeFileToShape shpToShape3 = new ShapeFileToShape();
    fileName = "C:\\data\\geovista_data\\shapefiles\\jin\\States.shp";

    ShapeFileDataReader shpRead3 = new ShapeFileDataReader();
    shpRead3.setFileName(fileName);
     //shpToShape3.setInputDataSet(shpRead3.getDataSet());
    shpToShape3.setInputDataSet(stateData.getDataSet());
    map2.setAuxiliarySpatialData(shpToShape3.getOutputDataSetForApps());

    //map2.setDataSet(shpToShape2.getOutputDataSet());

  }

}
