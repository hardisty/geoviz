package edu.psu.geovista.colorbrewer.coloreffect;

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

public class testDesignBoard extends JPanel {
  public static void main(String[] args) {
    DesignBoard designBoard1 = new DesignBoard();
    JFrame f = new JFrame("Bivariate Color Scheme Design Board (CIELAB version)");
    f.getContentPane().add(designBoard1);
    f.setVisible(true);
    f.pack();
  }
}
