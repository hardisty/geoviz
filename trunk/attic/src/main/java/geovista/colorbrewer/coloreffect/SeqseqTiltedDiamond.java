package geovista.colorbrewer.coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * 
 */

public class SeqseqTiltedDiamond extends SchemeObject {

  LABcolor[][] labcolor;
  public LUVcolor[][] luvcolor;
  public CIECAM02Color[][] cIECAM02color;


  int vclass;
  int hclass;
  int maxlightness;
  int minlightness;
  int top_angle;
  int tilt_angle;
  int shift_r;
  int shift_h;
  int shift_p;

  public SeqseqTiltedDiamond(int vclass, int hclass, int maxlightness, int minlightness, int top_Angle, int tilt_Angle, int shift_h, int shift_r, int shift_p) {

    //initializing the object
    this.vclass = vclass;
    this.hclass = hclass;
    this.maxlightness = maxlightness;
    this.minlightness = minlightness;
    this.top_angle = top_Angle;
    this.tilt_angle = tilt_Angle;
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
    double realheight;
    double radius1;
    double radius2;
    double divided;
    double divisor;

    realheight = (maxlightness - minlightness)/Math.cos(Math.toRadians(tilt_Angle));

    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){

        divided = (realheight/(double)2)*((1 + 2*j)/(double)hclass - (1 + 2*i)/(double)vclass);
        divisor = Math.tan(Math.toRadians(90 - top_Angle/2)) - Math.tan(Math.toRadians(90 + top_Angle/2));
        radius1 = divided/divisor;

        L = (Math.tan(Math.toRadians(90 - top_Angle/2))*radius1 + maxlightness/Math.cos(Math.toRadians(tilt_Angle)) - realheight*(1 + 2*j)/(2*hclass))*Math.cos(Math.toRadians(tilt_Angle));
        radius2 = (Math.tan(Math.toRadians(90 - top_Angle/2))*radius1 + maxlightness/Math.cos(Math.toRadians(tilt_Angle)) - realheight*(1 + 2*j)/(2*hclass))*Math.sin(Math.toRadians(tilt_Angle));

        A = radius1*Math.cos(Math.toRadians(shift_p + 180)) + radius2*Math.cos(Math.toRadians(shift_p - 90));
        B = radius1*Math.sin(Math.toRadians(shift_p + 180)) + radius2*Math.sin(Math.toRadians(shift_p - 90));

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