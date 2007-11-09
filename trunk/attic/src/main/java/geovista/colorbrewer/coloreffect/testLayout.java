package geovista.colorbrewer.coloreffect;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class testLayout extends JPanel{

  public testLayout() {

    this.setPreferredSize(new Dimension(300, 300));
    this.setLayout(new BorderLayout());

    JPanel centerPanel = new JPanel();
    centerPanel.setPreferredSize(new Dimension(200, 200));
    Border loweredetchedborder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    centerPanel.setBorder(loweredetchedborder);
    this.add(centerPanel, BorderLayout.CENTER);

    JPanel nPanel = new JPanel();
    JPanel sPanel = new JPanel();
    JPanel wPanel = new JPanel();
    JPanel ePanel = new JPanel();
    nPanel.setPreferredSize(new Dimension(300, 50));
    sPanel.setPreferredSize(new Dimension(300, 50));
    wPanel.setPreferredSize(new Dimension(50, 200));
    ePanel.setPreferredSize(new Dimension(50, 200));
    this.add(nPanel, BorderLayout.NORTH);
    this.add(sPanel, BorderLayout.SOUTH);
    this.add(wPanel, BorderLayout.WEST);
    this.add(ePanel, BorderLayout.EAST);
  }


  public static void main(String[] args) {
    testLayout testLayout1 = new testLayout();
    JFrame f = new JFrame();
    //f.setTitle("Interactive Legend");
    f.getContentPane().add(testLayout1);
    f.pack();
    f.setVisible(true);
  }
}