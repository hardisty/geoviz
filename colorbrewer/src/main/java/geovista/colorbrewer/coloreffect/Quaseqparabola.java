package geovista.colorbrewer.coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Quaseqparabola extends SchemeObject{

  public LABcolor[][] labcolor;

  int vclass;
  int hclass;
  int maxlightness;
  int minlightness;
  int constant;
  int dx;
  int dy;
  int startinghue;

  //sampling on the surface of a parabola shape
  public Quaseqparabola(int vclass, int hclass, int maxlightness, int minlightness, int constant, int dx, int dy, int startinghue){

    //initializing the object
    this.vclass = vclass;
    this.hclass = hclass;
    this.maxlightness = maxlightness;
    this.minlightness = minlightness;
    this.constant = constant;
    this.dx = dx;
    this.dy = dy;
    this.startinghue = startinghue;

    this.labcolor = new LABcolor[vclass][hclass];
    int[] lightness = new int[vclass];
    lightness[0] = maxlightness;
    lightness[vclass - 1] = minlightness;
    double lightnessStep;
    lightnessStep = (maxlightness - minlightness)/(double)(vclass - 1);
    for(int i = 1; i < vclass - 1; i ++){
      lightness[i] = (int)(maxlightness - i*lightnessStep);
    }

    double L;
    double a;
    double b;
    double r;
    double angle;

    angle = 360/hclass;

    for(int i = 0; i < vclass; i ++){
      //assigning lightness to each of the levels
      L = lightness[i];

      //the structure of parabola shape
      r = constant/L;

      for(int j = 0; j < hclass; j ++){
        //do a circle movement
        a = r*Math.cos(Math.toRadians(j*angle + startinghue));
        b = r*Math.sin(Math.toRadians(j*angle + startinghue));
        

        //adding the deviations
        a = a + dx*Math.cos(Math.toRadians(startinghue));
        b = b + dx*Math.sin(Math.toRadians(startinghue));
        L = L + dy;

        LABcolor c = new LABcolor(L, a, b);
        labcolor[i][j] = c;
      }
    }
  }
}