package edu.psu.geovista.colorbrewer.coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Seqseqgraydiamond extends SchemeObject {

  int vclass;
  int hclass;
  int maxlightness;
  int minlightness;
  int alpha;
  int dx;
  int dy;
  int startinghue;

  public LABcolor[][] labcolor;

  public Seqseqgraydiamond(int vclass, int hclass, int maxlightness, int minlightness, int alpha, int dx, int dy, int startinghue) {

    this.labcolor = new LABcolor[vclass][hclass];

    this.vclass = vclass;
    this.hclass = hclass;
    this.maxlightness = maxlightness;
    this.minlightness = minlightness;
    this.alpha = alpha;
    this.dx = dx;
    this.dy = dy;
    this.startinghue = startinghue;

    double L;
    double A;
    double B;
    double radius;
    double divided;
    double divisor;

    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){

        divided = ((maxlightness - minlightness)/(double)2)*((1 + 2*j)/(double)hclass - (1 + 2*i)/(double)vclass);
        divisor = Math.tan(Math.toRadians(90 - alpha/2)) - Math.tan(Math.toRadians(90 + alpha/2));
        radius = divided/divisor;

        A = radius*Math.cos(Math.toRadians(startinghue));
        B = radius*Math.sin(Math.toRadians(startinghue));
        L = Math.tan(Math.toRadians(90 - alpha/2))*radius + maxlightness - (maxlightness - minlightness)*(1 + 2*j)/(2*hclass);

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