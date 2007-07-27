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

public class testSeqseqFoldedDiamond extends JPanel{
  public static final int row = 5;
  public static final int column = 5;

  public testSeqseqFoldedDiamond() {
    JFrame f = new JFrame();
    f.getContentPane().setLayout(new GridLayout(row, column));
    f.setVisible(true);
    f.setSize(300, 300);

    SeqseqFoldedDiamond seqseqFoldedDiamond1 = new SeqseqFoldedDiamond(row, column, 100, 0, 45, 20, 164, 0, 0, 0);



    for(int i = 0; i < row; i ++){
      for(int j = 0; j < column; j ++){

/*
        CIELabToSRGB cIELabToSRGB1 = new CIELabToSRGB(seqseqFoldedDiamond1.labcolor[i][j].L, seqseqFoldedDiamond1.labcolor[i][j].a, seqseqFoldedDiamond1.labcolor[i][j].b);
        JPanel p = new JPanel();
        p.setBackground(new Color((int)cIELabToSRGB1.R255, (int)cIELabToSRGB1.G255, (int)cIELabToSRGB1.B255));
        f.getContentPane().add(p);
*/

        CIECAM02toSRGB cIECAM02toSRGB1 = new CIECAM02toSRGB(seqseqFoldedDiamond1.cIECAM02color[i][j].J, seqseqFoldedDiamond1.cIECAM02color[i][j].C, seqseqFoldedDiamond1.cIECAM02color[i][j].h);
        JPanel p = new JPanel();

        p.setBackground(new Color((int)cIECAM02toSRGB1.R255, (int)cIECAM02toSRGB1.G255, (int)cIECAM02toSRGB1.B255));
        f.getContentPane().add(p);
        //this.sample[this.getCurrentSample()].add(p);

      }
    }

    f.setTitle("Sequential-Sequential Folded Diamond");

    f.repaint();
    f.validate();
  }

}


