package edu.psu.geovista.colorbrewer.coloreffect;

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

public class testdivdivellipsoid extends JPanel{
  public static final int row = 5;
  public static final int column = 5;

  public testdivdivellipsoid() {
    JFrame f = new JFrame();
    f.getContentPane().setLayout(new GridLayout(row, column));
    f.setVisible(true);
    f.setSize(300, 300);

    Divdivhalfellipsoid divdivellipsoid1 = new Divdivhalfellipsoid(row, column, 160, 150, 300, 0, 0, 0);



    for(int i = 0; i < row; i ++){
      for(int j = 0; j < column; j ++){


        CIELUVtoSRGB cIELUVtoSRGB1 = new CIELUVtoSRGB(divdivellipsoid1.labcolor[i][j].L, divdivellipsoid1.labcolor[i][j].a, divdivellipsoid1.labcolor[i][j].b);
        JPanel p = new JPanel();
        p.setBackground(new Color((int)cIELUVtoSRGB1.R255, (int)cIELUVtoSRGB1.G255, (int)cIELUVtoSRGB1.B255));
        f.getContentPane().add(p);

      }
    }

    f.setTitle("Div-Div: ellipsoid model");

    f.repaint();
    f.validate();
  }

}

