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
 * 
 */

public class testDesignBoardCIECAM02 extends JPanel {

  public testDesignBoardCIECAM02() {
    //instantiate a designboard, and this initializes a set of components
    DesignBoardCIECAM02 designBoardCIECAM021 = new DesignBoardCIECAM02();

    //create a new frame, and customize it
    JFrame f = new JFrame();
    f.getContentPane().setLayout(new BorderLayout());
    f.setVisible(true);
    f.setSize(780, 480);
    f.setTitle("Bivariate Color Scheme Design Board (CIECAM02 version)");

    //add designBoard1 to the frame
    f.getContentPane().add(designBoardCIECAM021);

    f.repaint();
    f.validate();

  }






}