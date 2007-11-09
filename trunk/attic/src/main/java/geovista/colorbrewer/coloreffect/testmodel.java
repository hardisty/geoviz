package geovista.colorbrewer.coloreffect;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class testmodel extends JPanel implements ActionListener{

  JFrame f = new JFrame();

  boolean isAlldisplayed = true;

  JPanel leftpanel1 = new JPanel();
  JPanel rightpanel1 = new JPanel();
  JPanel bar1 = new JPanel();
  JPanel bar2 = new JPanel();
  JButton lefttriangle1 = new JButton(createImageIcon("resources/lefttriangle.gif"));
  JButton righttriangle1 = new JButton(createImageIcon("resources/righttriangle.gif"));

  Border border1 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);


  public testmodel() {

    this.f.setTitle("Color Scheme Exporter");

    this.leftpanel1.setPreferredSize(new Dimension(100, 300));
    this.leftpanel1.setBorder(this.border1);

    this.rightpanel1.setPreferredSize(new Dimension(300, 300));
    this.rightpanel1.setBorder(this.border1);

    this.bar1.setPreferredSize(new Dimension(30, 300));
    this.bar1.setBorder(this.border1);
    this.bar2.setPreferredSize(new Dimension(30, 300));
    this.bar2.setBorder(this.border1);

    this.lefttriangle1.setPreferredSize(new Dimension(20, 40));
    this.lefttriangle1.addActionListener(this);

    this.righttriangle1.setPreferredSize(new Dimension(20, 40));
    this.righttriangle1.addActionListener(this);

    this.bar1.add(this.righttriangle1);
    this.bar2.add(this.lefttriangle1);

    this.f.getContentPane().setLayout(new FlowLayout());
    this.f.getContentPane().add(this.bar1);
    this.f.getContentPane().add(this.leftpanel1);
    this.f.getContentPane().add(this.rightpanel1);
    this.f.getContentPane().add(this.bar2);
    this.f.pack();
    this.f.setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == this.righttriangle1){

      this.f.getContentPane().removeAll();

      if(this.isAlldisplayed == true){
        this.isAlldisplayed = false;
        this.setLayout(new BorderLayout());
        this.f.getContentPane().add(this.bar2, BorderLayout.WEST);
        this.f.getContentPane().add(this.rightpanel1, BorderLayout.EAST);

        this.f.getContentPane().repaint();
        this.f.pack();
        this.f.setVisible(true);
      }
      else{
        this.isAlldisplayed = true;
        this.f.getContentPane().setLayout(new FlowLayout());
        this.f.getContentPane().add(this.bar1);
        this.f.getContentPane().add(this.leftpanel1);
        this.f.getContentPane().add(this.rightpanel1);
        this.f.getContentPane().add(this.bar2);
        this.f.pack();
        this.f.setVisible(true);
      }

    }
    if(e.getSource() == this.lefttriangle1){

      this.f.getContentPane().removeAll();

      if(this.isAlldisplayed == true){
        this.isAlldisplayed = false;
        this.f.getContentPane().setLayout(new BorderLayout());
        this.f.getContentPane().add(this.leftpanel1, BorderLayout.WEST);
        this.f.getContentPane().add(this.bar1, BorderLayout.EAST);
        this.f.pack();
        this.f.setVisible(true);
      }
      else{
        this.isAlldisplayed = true;
        this.f.getContentPane().setLayout(new FlowLayout());
        this.f.getContentPane().add(this.bar1);
        this.f.getContentPane().add(this.leftpanel1);
        this.f.getContentPane().add(this.rightpanel1);
        this.f.getContentPane().add(this.bar2);
        this.f.pack();
        this.f.setVisible(true);
      }


    }
  }

  /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = SRGBDesignBoard.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }



}