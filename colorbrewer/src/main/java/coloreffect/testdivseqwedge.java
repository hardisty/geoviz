package coloreffect;

import java.awt.Color;
import java.awt.GridLayout;

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

public class testdivseqwedge extends JPanel{
  public static final int row = 5;
  public static final int column = 5;

  public testdivseqwedge() {
    JFrame f = new JFrame();
    f.getContentPane().setLayout(new GridLayout(row, column));
    f.setVisible(true);
    f.setSize(300, 300);

    DivseqWedge testdivseqwedge1 = new DivseqWedge(row, column, 100, 30, 145, 86, 180, 0, 0, 0);



    for(int i = 0; i < row; i ++){
      for(int j = 0; j < column; j ++){


        CIECAM02toSRGB cIECAM02toSRGB1 = new CIECAM02toSRGB(testdivseqwedge1.cIECAM02color[i][j].J, testdivseqwedge1.cIECAM02color[i][j].C, testdivseqwedge1.cIECAM02color[i][j].h);
        JPanel p = new JPanel();

        p.setBackground(new Color((int)cIECAM02toSRGB1.R255, (int)cIECAM02toSRGB1.G255, (int)cIECAM02toSRGB1.B255));
        f.getContentPane().add(p);

      }
    }

    f.setTitle("Sample effects: Diverging-Sequential");

    f.repaint();
    f.validate();
  }

}


