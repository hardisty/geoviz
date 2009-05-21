/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.geoviz.map;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;

/**
 * put your documentation comment here
 */
public class LayerRaster implements ImageObserver {
	// private GeoReference gvGeoReference;
	private transient BufferedImage bufferedImage;
	private transient Raster originalImage;

	/**
	 * This always returns true. This method is here to allow this class to
	 * implement the interface "ImageObserver".
	 * 
	 * @param img
	 * @param infoflags
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		return true;
	}

	/*
	 * selectionX1 is expected to be less than selectionX2, same with Y1 and y2
	 */
	public void findSelection(int selectionX1, int selectionX2,
			int selectionY1, int selectionY2) {
		// replace old selection
		// LayerSelection oldLayerSelection = new
		// LayerSelection(gvLayerSelection);
		// int x1 = oldLayerSelection.selectionX1;
		// int y1 = oldLayerSelection.selectionY1;
		// bufferedImage.set
		// gvLayerSelection.setSelection(selectionX1, selectionX2, selectionY1,
		// selectionY2);
		// copy section to be colored differently
		// originalSection = new Raster();
		bufferedImage.setData(originalImage);
		// set selection pixels to selection color
		int intAlpha, intRed, intBlue, intGreen, intARGB;
		intAlpha = 255; //
		intRed = 0;
		intBlue = 255;
		intGreen = 255;
		intARGB = (intAlpha << 24) | (intRed << 16) | (intGreen << 8)
				| (intBlue << 0);
		for (int pixelX = selectionX1; pixelX < selectionX2; pixelX++) {
			for (int pixelY = selectionY1; pixelY < selectionY2; pixelY++) {
				bufferedImage.setRGB(pixelX, pixelY, intARGB);
			}
		}
	}

	/*
	 * selectionX1 is expected to be less than selectionX2, same with Y1 and y2
	 */
	public void findSelectionShift(int selectionX1, int selectionX2,
			int selectionY1, int selectionY2) {

	}

	/**
	 * put your documentation comment here
	 * 
	 * @param g2
	 */
	public void render(Graphics2D g2) {
		// g2.setTransform(this.gvGeoReference.mapAffineTransform);
		// it would be really nice to specify the pixels to be drawn here, using
		// the affineTransform.
		// g2.drawImage(bufferedImage, BufferedImag);
		g2.drawImage(bufferedImage, 0, 0, this);
	}

}
