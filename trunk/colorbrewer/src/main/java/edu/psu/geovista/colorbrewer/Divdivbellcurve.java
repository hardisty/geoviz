package edu.psu.geovista.colorbrewer;

/**
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class Divdivbellcurve
 Copyright (c), 2004, GeoVISTA Center
 All Rights Reserved.
 Original Author: Biliang Zhou
 * @version 1.0
 */

//Function: L = k*Math.exp(e, -(A2 + B2)/t)
public class Divdivbellcurve {

  int vclass;
  int hclass;
  int meshspan;
  int curvevertex;
  int divider;
  int dx;
  int dy;
  int startinghue;

  public LABcolor[][] labcolor;

  public Divdivbellcurve(int vclass, int hclass, int meshspan, int curvevertex, int divider, int dx, int dy, int startinghue) {

    //initializing the object
    this.vclass = vclass;
    this.hclass = hclass;
    this.meshspan = meshspan;
    this.curvevertex = curvevertex;
    this.divider = divider;
    this.dx = dx;
    this.dy = dy;
    this.startinghue = startinghue;

    this.labcolor = new LABcolor[vclass][hclass];

    double L;
    double A;
    double B;

    //create two parameters indicating the position of the center point
    double x = (vclass - 1)/2.0;
    double y = (hclass - 1)/2.0;

    //two loops to fit the a-b grid onto the surface
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        //create two variables to record the a & b value when no rotation happens
        double m = (double)((j - y)*meshspan/hclass);
        double n = (double)((x - i)*meshspan/vclass);

        //a & b are decided by the relative location to the center point, and the degree of rotation
        A = m*Math.cos(Math.toRadians(startinghue)) - n*Math.sin(Math.toRadians(startinghue));
        B = n*Math.cos(Math.toRadians(startinghue)) + m*Math.sin(Math.toRadians(startinghue));

        //the bell curve model
        L = curvevertex*Math.exp(-(A*A + B*B)/divider);

        //adding the deviations
        A = A + dx*Math.cos(Math.toRadians(startinghue));
        B = B + dx*Math.sin(Math.toRadians(startinghue));
        L = L + dy;

        LABcolor color = new LABcolor(L, A, B);
        labcolor[i][j] = color;
      }
    }

  }
}