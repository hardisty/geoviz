package geovista.colorbrewer.coloreffect;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

//Function: A2/a2 + B2/b2 + L2/c2 = 1 (a = b here)
public class Divdivhalfellipsoid extends SchemeObject {

  int vclass;
  int hclass;
  int range;
  int a;
  int c;
  int dx;
  int dy;
  int startinghue;

  public LABcolor[][] labcolor;

  public Divdivhalfellipsoid(int vclass, int hclass, int range, int a, int c, int dx, int dy, int startinghue) {

    //initializing the object
    this.vclass = vclass;
    this.hclass = hclass;
    this.range = range;
    this.a = a;
    this.c = c;
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
        double m = (double)((j - y)*range/hclass);
        double n = (double)((x - i)*range/vclass);

        //a & b are decided by the relative location to the center point, and the degree of rotation
        A = m*Math.cos(Math.toRadians(startinghue)) - n*Math.sin(Math.toRadians(startinghue));
        B = n*Math.cos(Math.toRadians(startinghue)) + m*Math.sin(Math.toRadians(startinghue));

        //the half ellipsoid model
        L = Math.sqrt(a*a*a*a - a*a*A*A - a*a*B*B)*c/(a*a);

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