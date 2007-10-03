package edu.psu.geovista.matrix.scatterplot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.psu.geovista.image_blur.image.BoxBlurFilter;

/**
 * 
 */

public class GradientStamp {
	public GradientStamp(){
		
	}
	public static BufferedImage makeGradientStamp(int size) {

		BufferedImage buff = new BufferedImage(size, size,
				BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = buff.getGraphics();
		Graphics2D g2 = (Graphics2D)g;
		g.setColor(Color.blue);
		int cenX = size/2;
		int cenY = size/2;
		BasicStroke underStroke = new BasicStroke(20f);
		g2.setStroke(underStroke);
		g.setColor(new Color(128, 128,128,20));
		g.drawOval(cenX, cenY, 0, 0);
		//g.drawOval(0, 0, size, size);
		
		
		
		g.drawOval(cenX/2, cenY/2, cenX, cenY);
		
		BasicStroke midStroke = new BasicStroke(10f);
		g2.setStroke(midStroke);
		g.drawOval(cenX, cenY, 3, 3);

		BoxBlurFilter filter = new BoxBlurFilter();
		filter.setHRadius(5);
		filter.setVRadius(2);
		filter.setIterations(3);
 
		BufferedImage blurBuff = new BufferedImage(buff
				.getWidth(), buff.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		
		//VolatileImage blurBuff= this.getGraphicsConfiguration().createCompatibleVolatileImage(this.drawingBuff.getWidth(this), this.drawingBuff.getHeight(this));
		blurBuff.getGraphics().drawImage(buff, 0, 0, null);
		filter.filter(blurBuff, blurBuff);
		g2.drawImage(blurBuff, null, 0, 0);
		g2.drawImage(blurBuff, null, 0, 0);
		
		return buff;

	}
	
	class PaintedPan extends JPanel{
		BufferedImage stamp;
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.drawImage(stamp, 0, 0, this);
		}
	}
	
	public static void main(String[] args) {
		JFrame app = new JFrame("test");
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GradientStamp stamper = new GradientStamp();
		PaintedPan pan = stamper.new PaintedPan();
		pan.stamp = GradientStamp.makeGradientStamp(50);
		pan.setBackground(Color.blue);
		Dimension size = new Dimension(200,200);
		pan.setPreferredSize(size);
		pan.setMaximumSize(size);
		app.add(pan);
		app.pack();
		app.setVisible(true);
		
		
		
	}
}
