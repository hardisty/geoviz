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

public class testseqseqnongraydiamond extends JPanel{
  public static final int row = 7;
  public static final int column = 7;

  public testseqseqnongraydiamond() {
    JFrame f = new JFrame();
    f.getContentPane().setLayout(new GridLayout(row, column));
    f.setVisible(true);
    f.setSize(300, 300);

    Seqseqnongraydiamond seqseqnongraydiamond1 = new Seqseqnongraydiamond(row, column, 95, 0, 120, 15, 0, 0, 0);



    for(int i = 0; i < row; i ++){
      for(int j = 0; j < column; j ++){


        CIELabToSRGB cIELabToSRGB1 = new CIELabToSRGB(seqseqnongraydiamond1.labcolor[i][j].L, seqseqnongraydiamond1.labcolor[i][j].a, seqseqnongraydiamond1.labcolor[i][j].b);
        JPanel p = new JPanel();
        p.setBackground(new Color((int)cIELabToSRGB1.R255, (int)cIELabToSRGB1.G255, (int)cIELabToSRGB1.B255));
        f.getContentPane().add(p);

      }
    }

    f.setTitle("Sequential-Sequential NonGray Diamond");

    f.repaint();
    f.validate();
  }

}


