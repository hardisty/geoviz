package edu.psu.geovista.colorbrewer.coloreffect;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

//Function: A2/a2 + B2/b2 + L2/c2 = 1 (a = b here)
public class DivdivEllipsoid extends SchemeObject {

  int vclass;
  int hclass;
  int range;
  int a;
  int c;
  int shift_r;
  int shift_h;
  int shift_p;

  public LABcolor[][] labcolor;
  public LUVcolor[][] luvcolor;
  public CIECAM02Color[][] cIECAM02color;

  public DivdivEllipsoid(int vclass, int hclass, int range, int a, int c, int shift_h, int shift_r, int shift_p) {

    //initializing the object
    this.vclass = vclass;
    this.hclass = hclass;
    this.range = range;
    this.a = a;
    this.c = c;
    this.shift_r = shift_r;
    this.shift_h = shift_h;
    this.shift_p = shift_p;

    this.labcolor = new LABcolor[vclass][hclass];
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        LABcolor color = new LABcolor(0.0, 0.0, 0.0);
        this.labcolor[i][j] = color;
      }
    }

    this.luvcolor = new LUVcolor[vclass][hclass];

    this.cIECAM02color = new CIECAM02Color[vclass][hclass];

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
        double m = (double)((j - y)*range/hclass);
        double n = (double)((x - i)*range/vclass);

        //a & b are decided by the relative location to the center point, and the degree of rotation
        A = m*Math.cos(Math.toRadians(shift_p)) - n*Math.sin(Math.toRadians(shift_p));
        B = n*Math.cos(Math.toRadians(shift_p)) + m*Math.sin(Math.toRadians(shift_p));

        //the half ellipsoid model
        L = Math.sqrt(a*a*a*a - a*a*A*A - a*a*B*B)*c/(a*a);

        //adding the deviations
        A = A + shift_r*Math.cos(Math.toRadians(shift_p));
        B = B + shift_r*Math.sin(Math.toRadians(shift_p));
        L = L + shift_h;

        LABcolor color = new LABcolor(L, A, B);
        labcolor[i][j] = color;

        LUVcolor color_luv = new LUVcolor(L, A, B);
        luvcolor[i][j] = color_luv;

        CIECAM02Color color_CIECAM02 = new CIECAM02Color(L, A, B);
        this.cIECAM02color[i][j] = color_CIECAM02;
      }
    }
  }
}