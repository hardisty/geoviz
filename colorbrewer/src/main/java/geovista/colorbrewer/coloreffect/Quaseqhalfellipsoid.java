package geovista.colorbrewer.coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Quaseqhalfellipsoid extends SchemeObject{

  public LABcolor[][] labcolor;

  int vclass;
  int hclass;
  int maxlightness;
  int minlightness;
  int a;
  int b;
  int c;
  int dx;
  int dy;
  int startinghue;

  public Quaseqhalfellipsoid(int vclass, int hclass, int maxlightness, int minlightness, int a, int b, int c, int dx, int dy, int startinghue) {

    //initializing the object
    this.vclass = vclass;
    this.hclass = hclass;
    this.maxlightness = maxlightness;
    this.minlightness = minlightness;
    this.a = a;
    this.b = b;
    this.c = c;
    this.dx = dx;
    this.dy = dy;
    this.startinghue = startinghue;

    this.labcolor = new LABcolor[vclass][hclass];

    double L;
    double A;
    double B;
    double radiusa;
    double radiusb;
    double eachslice;
    double realangle;

    eachslice = 360/hclass;
    int[] lightness = new int[vclass];
    lightness[0] = maxlightness;
    lightness[vclass - 1] = minlightness;
    double lightnessStep;
    lightnessStep = (maxlightness - minlightness)/(double)(vclass - 1);
    for(int i = 1; i < vclass - 1; i ++){
      lightness[i] = (int)(maxlightness - i*lightnessStep);
    }

    for(int i = 0; i < vclass; i ++){

      L = lightness[i];
      radiusa = Math.sqrt(c*c - L*L)*(double)a/(double)c;
      radiusb = Math.sqrt(c*c - L*L)*(double)b/(double)c;

      for(int j = 0; j < hclass; j ++){

        realangle = eachslice*j + startinghue;

        A = radiusa*radiusb*Math.cos(Math.toRadians(realangle))/Math.sqrt(radiusa*radiusa*Math.sin(Math.toRadians(realangle))*Math.sin(Math.toRadians(realangle)) + radiusb*radiusb*Math.cos(Math.toRadians(realangle))*Math.cos(Math.toRadians(realangle)));

        B = radiusa*radiusb*Math.sin(Math.toRadians(realangle))/Math.sqrt(radiusa*radiusa*Math.sin(Math.toRadians(realangle))*Math.sin(Math.toRadians(realangle)) + radiusb*radiusb*Math.cos(Math.toRadians(realangle))*Math.cos(Math.toRadians(realangle)));

        

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