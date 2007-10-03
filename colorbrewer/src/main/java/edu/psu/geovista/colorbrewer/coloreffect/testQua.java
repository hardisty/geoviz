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

public class testQua {

  public static final int row = 2;
  public static final int column = 8;

  JPanel[][] jpanel = new JPanel[row][column];

  public testQua() {

    JFrame f = new JFrame();
    f.getContentPane().setLayout(new GridLayout(row, column));
    f.setVisible(true);
    f.setSize(300, 300);

    Qua qua1 = new Qua(row, column, 65, 40, 80, 0, 0, 0);




    for(int i = 0; i < row; i ++){
      for(int j = 0; j < column; j ++){


        CIELabToSRGB cIELabToSRGB1 = new CIELabToSRGB(qua1.labcolor[i][j].L, qua1.labcolor[i][j].a, qua1.labcolor[i][j].b);

        this.jpanel[i][j] = new JPanel();

        this.jpanel[i][j].setBackground(new Color((int)cIELabToSRGB1.R255, (int)cIELabToSRGB1.G255, (int)cIELabToSRGB1.B255));

        f.getContentPane().add(this.jpanel[i][j]);
      }
    }



    f.setTitle("Qualitative");

    f.repaint();
    f.validate();

  }

}