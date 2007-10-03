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

public class testquaseqellipsecurve extends JPanel{
  public static final int row = 5;
  public static final int column = 5;

  public testquaseqellipsecurve() {
    JFrame f = new JFrame();
    f.getContentPane().setLayout(new GridLayout(row, column));
    f.setVisible(true);
    f.setSize(300, 300);

    QuaseqEllipsecurve quaseqellipsecurve1 = new QuaseqEllipsecurve(row, column, 95, 30, 150, 150, 0, 0, 0);



    for(int i = 0; i < row; i ++){
      for(int j = 0; j < column; j ++){


        CIELabToSRGB cIELabToSRGB1 = new CIELabToSRGB(quaseqellipsecurve1.labcolor[i][j].L, quaseqellipsecurve1.labcolor[i][j].a, quaseqellipsecurve1.labcolor[i][j].b);
        JPanel p = new JPanel();
        p.setBackground(new Color((int)cIELabToSRGB1.R255, (int)cIELabToSRGB1.G255, (int)cIELabToSRGB1.B255));
        f.getContentPane().add(p);

      }
    }

    f.setTitle("Sample effects: Qualitative-Sequential-Ellipse curve");

    f.repaint();
    f.validate();
  }

}
