/* -------------------------------------------------------------------
 Java source file for the class TextureCacheMain
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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class TextureCacheMain
    extends JFrame {

  public TextureCacheMain(String name) {
    super(name);

    this.getContentPane()
        .setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));

    TextureCache cache = new TextureCache();
    TexturePaint[] cacheTextures = new TexturePaint[4];

    cacheTextures[1] = cache.getTexture(TextureCache.TEXTURE_BRICK);
    cacheTextures[2] = cache.getTexture(TextureCache.TEXTURE_BURLAP);
    cacheTextures[3] = cache.getTexture(TextureCache.TEXTURE_GROUND);
    TextureRepeat barkPanel = new TextureRepeat();
    barkPanel.setPaint(cache.getTexture(TextureCache.TEXTURE_BARK));
    barkPanel.setPreferredSize(new Dimension(300,300));
    barkPanel.setBorder(BorderFactory.createBevelBorder(0));
    this.getContentPane().add(barkPanel);


    TextureRepeat brickPanel = new TextureRepeat();
    brickPanel.setPaint(cache.getTexture(TextureCache.TEXTURE_BRICK));

        brickPanel.setPreferredSize(new Dimension(300,300));
            brickPanel.setBorder(BorderFactory.createBevelBorder(0));
    this.getContentPane().add(brickPanel);

  }

  public static void main(String[] args) {
    TextureCacheMain app = new TextureCacheMain("TextureCache Main Class");
    app.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    app.pack();
    app.setVisible(true);

  }

  public class TextureRepeat
      extends JPanel {

    private TexturePaint p;
    private TexturePaint p2;

    public void setPaint(TexturePaint p) {
      this.p = p;
      //this.repaint();
    }

    public void setRandomPaint(TexturePaint p) {
      this.p2 = p;
    }

    public void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      if (p2 != null) {
        g2.setPaint(p2);
        g2.fill(new Rectangle2D.Float(0, 0, this.getWidth(), this.getHeight()));
      }
      g2.setPaint(p);
      g2.fill(new Rectangle2D.Float(0, 0, this.getWidth(), this.getHeight()));

    }

  }
}
