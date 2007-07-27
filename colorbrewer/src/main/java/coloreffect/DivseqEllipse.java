package coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class DivseqEllipse extends SchemeObject{

  //this array stores the colors in lab format
  public LABcolor[][] labcolor;
  public LUVcolor[][] luvcolor;
  public CIECAM02Color[][] cIECAM02color;


  int vclass;
  int hclass;
  int maxlightness;
  int minlightness;
  int alpha;
  int e;
  int shift_r;
  int shift_h;
  int shift_p;

  public DivseqEllipse(int vclass, int hclass, int maxlightness, int minlightness, int alpha, int e, int shift_h, int shift_r, int shift_p) {

    //initializing the object
    this.vclass = vclass;
    this.hclass = hclass;
    this.maxlightness = maxlightness;
    this.minlightness = minlightness;
    this.alpha = alpha;
    this.e = e;
    this.shift_r = shift_r;
    this.shift_h = shift_h;
    this.shift_p = shift_p;

    //initializing the labcolor array
    this.labcolor = new LABcolor[vclass][hclass];
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        LABcolor color = new LABcolor(0.0, 0.0, 0.0);
        this.labcolor[i][j] = color;
      }
    }


    this.luvcolor = new LUVcolor[vclass][hclass];
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        LUVcolor color = new LUVcolor(0.0, 0.0, 0.0);
        this.luvcolor[i][j] = color;
      }
    }

    this.cIECAM02color = new CIECAM02Color[vclass][hclass];
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        CIECAM02Color color = new CIECAM02Color(0.0, 0.0, 0.0);
        this.cIECAM02color[i][j] = color;
      }
    }

    //angle is the real properties of the color vector
    double angle;

    //a is the long half axis of the ellipse, and b is the short half axis of the ellipse
    double b;
    double a;

    //x & y are coordinates of the crosspoint of the ellipse and the line, and k: y = kx
    double x;
    double y;
    double k;

    //two loops to calculate the matrix
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){

        b = maxlightness - (maxlightness - minlightness)*i/(vclass - 1);
        a = b/Math.sqrt(1 - (double)e*e/10000.00);

        angle = 90 + alpha/2 - alpha*j/(hclass - 1);
        k = Math.tan(Math.toRadians(angle));

        y = Math.abs(a*b*k)/Math.sqrt(b*b + a*a*k*k);
        x = y/k;

        this.labcolor[i][j].a = (x + shift_r)*Math.cos(Math.toRadians(shift_p));
        this.labcolor[i][j].b = (x + shift_r)*Math.sin(Math.toRadians(shift_p));
        this.labcolor[i][j].L = (y + shift_h);


        this.luvcolor[i][j].U = (x + shift_r)*Math.cos(Math.toRadians(shift_p));
        this.luvcolor[i][j].V = (x + shift_r)*Math.sin(Math.toRadians(shift_p));
        this.luvcolor[i][j].L = (y + shift_h);

        this.cIECAM02color[i][j].a = (x + shift_r)*Math.cos(Math.toRadians(shift_p));
        this.cIECAM02color[i][j].b = (x + shift_r)*Math.sin(Math.toRadians(shift_p));
        this.cIECAM02color[i][j].J = (y + shift_h);

        this.cIECAM02color[i][j] = new CIECAM02Color(this.cIECAM02color[i][j].J, this.cIECAM02color[i][j].a, this.cIECAM02color[i][j].b);
      }
    }
  }
}