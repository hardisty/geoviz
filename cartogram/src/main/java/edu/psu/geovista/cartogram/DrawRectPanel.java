package edu.psu.geovista.cartogram;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawRectPanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener {

       private Vector rectVec = new Vector();
       private JButton resetButton = new JButton("Reset");
       private JButton calcAreaButton = new JButton("Calculate Area");

       private Rectangle2D rect = new Rectangle2D.Double(0, 0, 0, 0);
       private double rectX = 0;
       private double rectY = 0;
       private double rectW = 0;
       private double rectH = 0;
       private boolean makingRect = false;

       public DrawRectPanel() {
               super();

               setLayout(new BorderLayout());

               JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
               buttonPanel.add(resetButton);
               buttonPanel.add(calcAreaButton);
               add(buttonPanel, BorderLayout.NORTH);

               addMouseListener(this);
               addMouseMotionListener(this);
               resetButton.addActionListener(this);
               calcAreaButton.addActionListener(this);
       }

       public void actionPerformed(ActionEvent evt) {
               Object src = evt.getSource();

               if (src == resetButton) {
                       rectVec = new Vector();
                       repaint();
               }
               else if (src == calcAreaButton) {
                       Shape[] arr = new Shape[rectVec.size()];
                       for (int i = 0; i < arr.length; i++)
                               arr[i] = (Shape) rectVec.get(i);


                       repaint();
               }
       }

       // this method was used for testing purposes to compare the # of pixels vs. the area returned.
       // in all the test cases this method returned the same value.
       /* private int countBlackPixels() {
               BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
               Graphics2D g = bi.createGraphics();

               g.setColor(Color.white);
               g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
               g.setColor(Color.black);

               for (int i = 0; i < rectVec.size(); i++)
                       g.fill((Shape) rectVec.get(i));

    

               int count = 0;

               for (int i = 0; i < bi.getWidth(); i++) {
                       for (int j = 0; j < bi.getHeight(); j++) {
                               Color c = new Color(bi.getRGB(i, j));
                               if (c.equals(Color.black))
                                       count++;
                       }
               }

               return count;
       } */

       public void mousePressed(MouseEvent evt) {
               if (!makingRect) {
                       rectX = evt.getX();
                       rectY = evt.getY();
                       rectW = 0;
                       rectH = 0;
                       rect.setRect(rectX, rectY, 0, 0);
                       makingRect = true;
               }
               repaint();
       }

       public void mouseReleased(MouseEvent evt) {
               if (makingRect) {
                       makingRect = false;
                       rectVec.add(rect.clone());

                       
               }
               repaint();
       }
       public void mouseClicked(MouseEvent evt) {}
       public void mouseEntered(MouseEvent evt) {}
       public void mouseExited(MouseEvent evt) {}

       public void mouseMoved(MouseEvent evt) {}
       public void mouseDragged(MouseEvent evt) {
               rectW = evt.getX() - rectX;
               rectH = evt.getY() - rectY;
               repaint();
       }

       public void paint(Graphics _g) {
               super.paint(_g);

               Graphics2D g = (Graphics2D) _g;

               if (makingRect) {
                       setRect(rect, rectX, rectY, rectW, rectH);
                       g.fill(rect);
               }

               for (int i = 0; i < rectVec.size(); i++)
                       g.fill((Shape) rectVec.get(i));
       }

       private static void setRect(Rectangle2D r, double x, double y, double w, double h) {
               if (w < 0) {
                       x = x + w;
                       w = -w;
               }

               if (h < 0) {
                       y = y + h;
                       h = -h;
               }

               r.setRect(x, y, w, h);
       }

       public static void main(String[] args) {
               JFrame f = new JFrame();
               f.setContentPane(new DrawRectPanel());
               f.setSize(600, 400);
               f.setVisible(true);
       }
}
