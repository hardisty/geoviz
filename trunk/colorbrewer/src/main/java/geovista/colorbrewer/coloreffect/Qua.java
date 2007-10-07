package geovista.colorbrewer.coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Qua extends SchemeObject {

  public LABcolor[][] labcolor;

  public Qua(int vclass, int hclass, int maxlightness, int minlightness, int radius, int dx, int dy, int startinghue) {

    double L;
    double A;
    double B;
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

    this.labcolor = new LABcolor[vclass][hclass];

    eachslice = 360/hclass;

    for(int i = 0; i < vclass; i++){

      L = lightness[i];

      for(int j = 0; j < hclass; j++){
        realangle = j*eachslice + startinghue;
        A = radius*Math.cos(Math.toRadians(realangle));
        B = radius*Math.sin(Math.toRadians(realangle));

        //adding the deviations
        A = A + dx*Math.cos(Math.toRadians(startinghue));
        B = B + dx*Math.sin(Math.toRadians(startinghue));
        L = L + dy;

        LABcolor color = new LABcolor(L, A, B);
        this.labcolor[i][j] = color;
      }
    }

  }
}