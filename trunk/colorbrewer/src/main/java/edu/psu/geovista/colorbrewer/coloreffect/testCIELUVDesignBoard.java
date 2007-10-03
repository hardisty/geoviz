package edu.psu.geovista.colorbrewer.coloreffect;

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

public class testCIELUVDesignBoard extends JPanel {

  public testCIELUVDesignBoard() {
    //instantiate a designboard, and this initializes a set of components
    CIELUVDesignBoard cIELUVdesignBoard1 = new CIELUVDesignBoard();

    //create a new frame, and customize it
    JFrame f = new JFrame();
    f.getContentPane().setLayout(new BorderLayout());
    f.setVisible(true);
    f.setSize(780, 480);
    f.setTitle("Bivariate Color Scheme Design Board (CIELUV version)");

    //add designBoard1 to the frame
    f.getContentPane().add(cIELUVdesignBoard1);

    f.repaint();
    f.validate();

  }






}