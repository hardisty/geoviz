/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class LayerRaster
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: LayerRaster.java,v 1.3 2004/05/05 17:23:10 hardisty Exp $
 $Date: 2004/05/05 17:23:10 $
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



package  geovista.geoviz.map;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;


/**
 * put your documentation comment here
 */
public class LayerRaster
        implements ImageObserver {
    //private GeoReference gvGeoReference;
    private transient BufferedImage bufferedImage;
    private transient Raster originalImage;
    /**
     * This always returns true. This method is here to allow this class to
     * implement the interface "ImageObserver".
     * @param img
     * @param infoflags
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public boolean imageUpdate (Image img, int infoflags, int x, int y, int width,
            int height) {
        return  true;
    }

    /*
     * selectionX1 is expected to be less than selectionX2, same with Y1 and y2
     */
    public void findSelection (int selectionX1, int selectionX2, int selectionY1,
            int selectionY2) {
        //replace old selection
        //LayerSelection oldLayerSelection = new LayerSelection(gvLayerSelection);
        //int x1 = oldLayerSelection.selectionX1;
        //int y1 = oldLayerSelection.selectionY1;
        //bufferedImage.set
        //gvLayerSelection.setSelection(selectionX1, selectionX2, selectionY1,
        //        selectionY2);
        //copy section to be colored differently
        //originalSection = new Raster();
        bufferedImage.setData(originalImage);
        //set selection pixels to selection color
        int intAlpha, intRed, intBlue, intGreen, intARGB;
        intAlpha = 255;         //
        intRed = 0;
        intBlue = 255;
        intGreen = 255;
        intARGB = (intAlpha << 24) | (intRed << 16) | (intGreen << 8) | (intBlue << 0);
        for (int pixelX = selectionX1; pixelX < selectionX2; pixelX++) {
            for (int pixelY = selectionY1; pixelY < selectionY2; pixelY++) {
                bufferedImage.setRGB(pixelX, pixelY, intARGB);
            }
        }
    }
    /*
     * selectionX1 is expected to be less than selectionX2, same with Y1 and y2
     */
    public void findSelectionShift (int selectionX1, int selectionX2, int selectionY1,
            int selectionY2) {

    }

    /**
     * put your documentation comment here
     * @param g2
     */
    public void render (Graphics2D g2) {
        //g2.setTransform(this.gvGeoReference.mapAffineTransform);
        //it would be really nice to specify the pixels to be drawn here, using
        //the affineTransform.
        //g2.drawImage(bufferedImage, BufferedImag);
        g2.drawImage((Image)bufferedImage, 0, 0, this);
    }

}
