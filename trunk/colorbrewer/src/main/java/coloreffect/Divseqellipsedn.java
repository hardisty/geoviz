package coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Divseqellipsedn extends SchemeObject{

  //this array stores the colors in lab format
  public LABcolor[][] labcolor;

  int vclass;
  int hclass;
  int maxlightness;
  int minlightness;
  int alpha;
  int e;
  int dx;
  int dy;
  int startinghue;

  public Divseqellipsedn(int vclass, int hclass, int maxlightness, int minlightness, int alpha, int e, int dx, int dy, int startinghue) {

    //initializing the object
    this.vclass = vclass;
    this.hclass = hclass;
    this.maxlightness = maxlightness;
    this.minlightness = minlightness;
    this.alpha = alpha;
    this.e = e;
    this.dx = dx;
    this.dy = dy;
    this.startinghue = startinghue;

    //initializing the labcolor array
    this.labcolor = new LABcolor[vclass][hclass];
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        LABcolor color = new LABcolor(0.0, 0.0, 0.0);
        this.labcolor[i][j] = color;
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

        this.labcolor[i][j].a = (x + dx)*Math.cos(Math.toRadians(startinghue));
        this.labcolor[i][j].b = (x + dx)*Math.sin(Math.toRadians(startinghue));
        this.labcolor[i][j].L = (y + dy);
      }
    }
  }
}