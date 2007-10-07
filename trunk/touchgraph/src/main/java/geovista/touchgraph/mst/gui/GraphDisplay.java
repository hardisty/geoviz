package geovista.touchgraph.mst.gui;

import java.awt.*;
import javax.swing.*;
import java.util.Vector;

/**
 * Title:        MST Java
 * Description:  A program to construct a MST from a graph, given as a text file.
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Markus Svensson
 * @version 1.5
 */

public class GraphDisplay extends JFrame {
  private BorderLayout borderLayout1 = new BorderLayout();
  private JLabel statusLabel = new JLabel();
  private DrawPanel drawPanel = new DrawPanel();
  private JScrollPane scroller = new JScrollPane(drawPanel);

  /**
   * Constructor
   * @param The mst to draw
   */
  public GraphDisplay(Vector mst, String name){
      drawPanel.setMST(mst);
      jbInit();
      this.setTitle(name);
      this.setSize(450, 400);
  }

  /**
   * Init the display
   */
  private void jbInit() {
    statusLabel.setBackground(Color.lightGray);
    statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
    statusLabel.setText("MST Java V1.5");
    this.getContentPane().setLayout(borderLayout1);
    this.getContentPane().setBackground(Color.lightGray);
    scroller.setAutoscrolls(true);
    drawPanel.setBackground(Color.lightGray);
    drawPanel.setBorder(BorderFactory.createEtchedBorder());
    drawPanel.setUp();
    this.getContentPane().add(statusLabel, BorderLayout.SOUTH);
    this.getContentPane().add(scroller, BorderLayout.CENTER);
  }
}