package geovista.touchgraph.mst.gui;

import java.awt.*;
import javax.swing.JPanel;

import geovista.touchgraph.mst.MSTEdge;

import java.util.*;

/**
 * Title:        DrawPanel
 * Description:  Panel used to draw the minimum spanning tree
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Markus Svensson
 * 
 * @version 1.5
 */

public class DrawPanel extends JPanel {
  private Vector mst;
  private int[][] vertexes = new int[100][3];
  private int currentX;
  private int currentY;

  /**
   * Constructor
   */
  public DrawPanel(){
    this.setSize(600,600);
    currentX = 10;
    currentY = 10;
  }

  /**
   * Set the minimum spanning tree to paint
   * @param The minimum spanning tree
   */
  public void setMST(Vector mst){
    this.mst = mst;
  }

  /**
   * Set up the vretexes for drawing.
   */
  public void setUp(){
    for(int i = 0; i < mst.size(); i++){
      MSTEdge temp = (MSTEdge)mst.get(i);
      if(vertexes[temp.getStart()][0] == 0){
        vertexes[temp.getStart()][0] = temp.getStart();
        vertexes[temp.getStart()][1] = currentX;
        vertexes[temp.getStart()][2] = currentY;
        currentX += 50;
        if(currentX > 200){
          currentY += 100;
          currentX = 10;
        }
      }
      if(vertexes[temp.getEnd()][0] == 0){
        vertexes[temp.getEnd()][0] = temp.getEnd();
        vertexes[temp.getEnd()][1] = currentX;
        vertexes[temp.getEnd()][2] = currentY;
        currentX += 50;
        if(currentX > 200){
          currentX = 10;
          currentY += 100;
        }
      }
    }
  }

  /**
   * Paint the panel
   */
  public void paintComponent(Graphics g){
    g.setColor(Color.black);

    for(int i = 0; i < 100; i++){
      if(vertexes[i][0] != 0){
        g.drawOval(vertexes[i][1], vertexes[i][2], 30, 30);
        g.drawString(""+i, vertexes[i][1], vertexes[i][2]);
      }
    }
    for(int i = 0; i < mst.size(); i++){
      MSTEdge temp = (MSTEdge)mst.get(i);
      g.drawLine(vertexes[temp.getStart()][1]+10, vertexes[temp.getStart()][2]+10, vertexes[temp.getEnd()][1]+10, vertexes[temp.getEnd()][2]+10);
    }
  }
}