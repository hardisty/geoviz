/* -------------------------------------------------------------------
 Java source file for the class TextureMapMain
 Original Author: Frank Hardisty
 $Author: xpdai $
 $Id: MapMatrixElement.java,v 1.13 2005/01/04 19:14:33 xpdai Exp $
 $Date: 2005/01/04 19:14:33 $
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 -------------------------------------------------------------------   */



package edu.psu.geovista.texture;

import java.awt.TexturePaint;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import edu.psu.geovista.geoviz.map.GeoMap;
import edu.psu.geovista.geoviz.sample.GeoData48States;
import edu.psu.geovista.geoviz.shapefile.ShapeFileProjection;
import edu.psu.geovista.geoviz.shapefile.ShapeFileToShape;
import geovista.common.data.DataSetForApps;
import geovista.coordination.CoordinationManager;


public class TextureMapMain
    extends JFrame {

  public TextureMapMain(String name) {
    super(name);

  }

  public static void main(String[] args) {

//GeoMap map = new GeoMap();
    TextureMapMain app = new TextureMapMain("TextureMap Main Class");
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

    CoordinationManager coord = new CoordinationManager();
    ShapeFileToShape shpToShape = new ShapeFileToShape();
    ShapeFileProjection shpProj = new ShapeFileProjection();
    GeoData48States stateData = new GeoData48States();
    //GeoDataSCarolina stateData = new GeoDataSCarolina();
    coord.addBean(map2);
    coord.addBean(shpToShape);

    shpProj.setInputDataSetForApps(stateData.getDataForApps());
    Object[] data = null;
    data = shpProj.getOutputDataSet();
    shpToShape.setInputDataSet(data);

    //shpToShape.setInputDataSet(stateData.getDataSet());

    TextureCache cache = new TextureCache();
    TexturePaint[] cacheTextures = new TexturePaint[4];
    cacheTextures[0] = cache.getTexture(TextureCache.TEXTURE_BARK);
    cacheTextures[1] = cache.getTexture(TextureCache.TEXTURE_BRICK);
    cacheTextures[2] = cache.getTexture(TextureCache.TEXTURE_BURLAP);
    cacheTextures[3] = cache.getTexture(TextureCache.TEXTURE_GROUND);

    DataSetForApps dataSet = stateData.getDataForApps();
    int nObs = dataSet.getNumObservations();
    TexturePaint[] textures = new TexturePaint[nObs];
    for (int i = 0; i < nObs; i++){
      int whichTex = i % 4;

      textures[i] = cacheTextures[whichTex];
    }
    map2.setTextures(textures);



  }

}
