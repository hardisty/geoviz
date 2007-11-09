package geovista.colorbrewer.coloreffect;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class testSRGBDesignBoard extends JPanel {

  public testSRGBDesignBoard() {
    //instantiate a designboard, and this initializes a set of components
    SRGBDesignBoard sRGBDesignBoard1 = new SRGBDesignBoard();

    //create a new frame, and customize it
    JFrame f = new JFrame();
    f.getContentPane().setLayout(new BorderLayout());
    f.setVisible(true);
    f.setSize(620, 470);
    f.setTitle("Bivariate Color Scheme Design Board (Standard RGB version)");

    //add designBoard1 to the frame
    f.getContentPane().add(sRGBDesignBoard1);

    f.repaint();
    f.validate();

  }






}