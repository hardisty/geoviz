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

public class testdivdivcone extends JPanel{
  public static final int row = 5;
  public static final int column = 5;

  public testdivdivcone() {
    JFrame f = new JFrame();
    f.getContentPane().setLayout(new GridLayout(row, column));
    f.setVisible(true);
    f.setSize(300, 300);

    DivdivCone divdivcone1 = new DivdivCone(row, column, 600, 600, 400, 0, 0, 0);



    for(int i = 0; i < row; i ++){
      for(int j = 0; j < column; j ++){


        CIELUVtoSRGB cIELUVtoSRGB1 = new CIELUVtoSRGB(divdivcone1.labcolor[i][j].L, divdivcone1.labcolor[i][j].a, divdivcone1.labcolor[i][j].b);
        JPanel p = new JPanel();
        p.setBackground(new Color((int)cIELUVtoSRGB1.R255, (int)cIELUVtoSRGB1.G255, (int)cIELUVtoSRGB1.B255));
        f.getContentPane().add(p);

      }
    }

    f.setTitle("Div-Div: cone model");

    f.repaint();
    f.validate();
  }

}


