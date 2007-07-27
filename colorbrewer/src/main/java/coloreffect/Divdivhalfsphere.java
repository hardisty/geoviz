package coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Divdivhalfsphere extends SchemeObject{

  //this array stores the colors in lab format
  public LABcolor[][] labcolor;

  int vclass;
  int hclass;
  int span;
  int radius;
  int dx;
  int dy;
  int startinghue;

  //the construction method using the half sphere model
  public Divdivhalfsphere(int vclass, int hclass, int span, int radius, int dx, int dy, int startingcolor) {

    //initializing the labcolor array
    this.labcolor = new LABcolor[vclass][hclass];
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        LABcolor color = new LABcolor(0.0, 0.0, 0.0);
        this.labcolor[i][j] = color;
      }
    }

    //create two parameters indicating the position of the center point
    double x = (vclass - 1)/2.0;
    double y = (hclass - 1)/2.0;

    //two loops to fit the a-b grid onto the surface
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        //create two variables to record the a & b value when no rotation happens
        double m = (double)((j - y)*span/hclass);
        double n = (double)((x - i)*span/vclass);

        //a & b are decided by the relative location to the center point, and the degree of rotation
        this.labcolor[i][j].a = m*Math.cos(Math.toRadians(startingcolor)) - n*Math.sin(Math.toRadians(startingcolor)) + dx*Math.cos(Math.toRadians(startingcolor));
        this.labcolor[i][j].b = n*Math.cos(Math.toRadians(startingcolor)) + m*Math.sin(Math.toRadians(startingcolor)) + dx*Math.sin(Math.toRadians(startingcolor));

        //the half sphere model
        this.labcolor[i][j].L = Math.sqrt(Math.pow(radius, 2) - Math.pow(this.labcolor[i][j].a, 2) - Math.pow(this.labcolor[i][j].b, 2)) + dy;
      }
    }
  }
}