package edu.psu.geovista.colorbrewer.coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

//Function: L = k*Math.exp(e, -(A2 + B2)/t)
public class Quaseqbellcurve extends SchemeObject{

  public LABcolor[][] labcolor;

  int vclass;
  int hclass;
  int maxlightness;
  int minlightness;
  int curvevertex;
  int divisor;
  int dx;
  int dy;
  int startinghue;

  public Quaseqbellcurve(int vclass, int hclass, int maxlightness, int minlightness, int curvevertex, int divisor, int dx, int dy, int startinghue) {

    //initializing the object
    this.vclass = vclass;
    this.hclass = hclass;
    this.maxlightness = maxlightness;
    this.minlightness = minlightness;
    this.curvevertex = curvevertex;
    this.divisor = divisor;
    this.dx = dx;
    this.dy = dy;
    this.startinghue = startinghue;

    this.labcolor = new LABcolor[vclass][hclass];

    double L;
    double A;
    double B;
    double radius;
    double eachslice;
    double realangle;

    int[] lightness = new int[vclass];
    lightness[0] = maxlightness;
    lightness[vclass - 1] = minlightness;
    double lightnessStep;
    lightnessStep = (maxlightness - minlightness)/(double)(vclass - 1);
    for(int i = 1; i < vclass - 1; i ++){
      lightness[i] = (int)(maxlightness - i*lightnessStep);
    }

    eachslice = 360/hclass;

    for(int i = 0; i < vclass; i++){
      L = lightness[i];
      radius = Math.sqrt(divisor*Math.log((double)curvevertex/L));
      for(int j = 0; j < hclass; j++){
        realangle = j*eachslice + startinghue;
        A = radius*Math.cos(Math.toRadians(realangle));
        B = radius*Math.sin(Math.toRadians(realangle));
        

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