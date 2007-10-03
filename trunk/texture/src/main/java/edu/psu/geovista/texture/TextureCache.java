/* -------------------------------------------------------------------
 Java source file for the class TextureCache
 Original Author: Frank Hardisty
 $Author: xpdai $
 $Id: MapMatrixElement.java,v 1.13 2005/01/04 19:14:33 xpdai Exp $
 $Date: 2005/01/04 19:14:33 $

 Textures taken from the GIMP 2.2.

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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * TextureCache is used to efficiently store and access textures.
 *
 * TextureCache lazily creates and then keeps instances of
 * textures to be called when needed.
 *
 */
public class TextureCache 
    extends Component {

  private transient TexturePaint[] textures;
  private  URL[] urls;

  public static final int TEXTURE_BARK = 0;
  public static final int TEXTURE_BRICK = 1;
  public static final int TEXTURE_BURLAP = 2;
  public static final int TEXTURE_INFO = 3;
  public static final int TEXTURE_GROUND = 4;
  public static final int TEXTURE_RED_GROUND = 5;
  public static final int TEXTURE_GREEN_GROUND = 6;
  public static final int TEXTURE_BLUE_GROUND = 7;
  private static final int numTextures = 8;

  private static final URL urlBark = TextureCache.class.getResource("resources/bark.gif");
  private static final URL urlBrick = TextureCache.class.getResource("resources/brick.gif");
  private static final URL urlBurlap = TextureCache.class.getResource("resources/burlap.gif");
  private static final URL urlGround = TextureCache.class.getResource("resources/ground.gif");
  private static final URL urlRedGround = TextureCache.class.getResource("resources/redGround.gif");
  private static final URL urlGreenGround = TextureCache.class.getResource("resources/greenGround.gif");
  private static final URL urlBlueGround = TextureCache.class.getResource("resources/blueGround.gif");

  /**
   * null ctr
   */
  public TextureCache() {
    textures = new TexturePaint[numTextures];
    urls = new URL[numTextures];
    urls[TextureCache.TEXTURE_BARK] = TextureCache.urlBark;
    urls[TextureCache.TEXTURE_BRICK] = TextureCache.urlBrick;
    urls[TextureCache.TEXTURE_BURLAP] = TextureCache.urlBurlap;
    urls[TextureCache.TEXTURE_GROUND] = TextureCache.urlGround;
    urls[TextureCache.TEXTURE_RED_GROUND] = TextureCache.urlRedGround;
    urls[TextureCache.TEXTURE_GREEN_GROUND] = TextureCache.urlGreenGround;
    urls[TextureCache.TEXTURE_BLUE_GROUND] = TextureCache.urlBlueGround;

  }

  /**
   * Returns the texture specified.
   */
  public TexturePaint getTexture(int texture) {

    //lazily create texture, if need be
    if (textures[texture] == null) {
      URL urlGif = urls[texture];
      ImageIcon imIcon = new ImageIcon(urlGif);
      Image im = imIcon.getImage();
      int imWidth = im.getWidth(this);
      int imHeight = im.getHeight(this);
      BufferedImage bim = new BufferedImage(imWidth, imHeight,BufferedImage.TYPE_INT_ARGB);
      Graphics g = bim.getGraphics();
      g.drawImage(im,0,0,Color.BLACK,this);
      TexturePaint tex = new TexturePaint(bim,new Rectangle2D.Float(0,0,im.getWidth(this),im.getHeight(this)));
      textures[texture] =tex;
    }
    return textures[texture];
  }

}
