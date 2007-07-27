package coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

//Function: L = k*Math.exp(e, -(A2 + B2)/t)
public class QuaseqBellshape extends SchemeObject{

  public LABcolor[][] labcolor;
  public LUVcolor[][] luvcolor;
  public CIECAM02Color[][] cIECAM02color;

  int vclass;
  int hclass;
  int maxlightness;
  int minlightness;
  int curvevertex;
  int divisor;
  int shift_r;
  int shift_h;
  int shift_p;

  public QuaseqBellshape(int vclass, int hclass, int maxlightness, int minlightness, int curvevertex, int divisor, int shift_h, int shift_r, int shift_p) {

    //initializing the object
    this.vclass = vclass;
    this.hclass = hclass;
    this.maxlightness = maxlightness;
    this.minlightness = minlightness;
    this.curvevertex = curvevertex;
    this.divisor = divisor;
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
        realangle = j*eachslice + shift_p;
        A = radius*Math.cos(Math.toRadians(realangle));
        B = radius*Math.sin(Math.toRadians(realangle));


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