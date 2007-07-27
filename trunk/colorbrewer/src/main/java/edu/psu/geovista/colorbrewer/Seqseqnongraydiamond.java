package edu.psu.geovista.colorbrewer;

/**
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class Seqseqnongraydiamond
 Copyright (c), 2004, GeoVISTA Center
 All Rights Reserved.
 Original Author: Biliang Zhou
 * @version 1.0
 */

public class Seqseqnongraydiamond {

  LABcolor[][] labcolor;

  int vclass;
  int hclass;
  int maxlightness;
  int minlightness;
  int alpha;
  int beta;
  int dx;
  int dy;
  int startinghue;

  public Seqseqnongraydiamond(int vclass, int hclass, int maxlightness, int minlightness, int alpha, int beta, int dx, int dy, int startinghue) {

    //initializing the object
    this.vclass = vclass;
    this.hclass = hclass;
    this.maxlightness = maxlightness;
    this.minlightness = minlightness;
    this.alpha = alpha;
    this.beta = beta;
    this.dx = dx;
    this.dy = dy;
    this.startinghue = startinghue;

    this.labcolor = new LABcolor[vclass][hclass];

    double L;
    double A;
    double B;
    double realheight;
    double radius1;
    double radius2;
    double divided;
    double divisor;

    realheight = (maxlightness - minlightness)/Math.cos(Math.toRadians(beta));

    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){

        divided = (realheight/(double)2)*((1 + 2*j)/(double)hclass - (1 + 2*i)/(double)vclass);
        divisor = Math.tan(Math.toRadians(90 - alpha/2)) - Math.tan(Math.toRadians(90 + alpha/2));
        radius1 = divided/divisor;

        L = (Math.tan(Math.toRadians(90 - alpha/2))*radius1 + maxlightness/Math.cos(Math.toRadians(beta)) - realheight*(1 + 2*j)/(2*hclass))*Math.cos(Math.toRadians(beta));
        radius2 = (Math.tan(Math.toRadians(90 - alpha/2))*radius1 + maxlightness/Math.cos(Math.toRadians(beta)) - realheight*(1 + 2*j)/(2*hclass))*Math.sin(Math.toRadians(beta));

        A = radius1*Math.cos(Math.toRadians(startinghue + 180)) + radius2*Math.cos(Math.toRadians(startinghue - 90));
        B = radius1*Math.sin(Math.toRadians(startinghue + 180)) + radius2*Math.sin(Math.toRadians(startinghue - 90));

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