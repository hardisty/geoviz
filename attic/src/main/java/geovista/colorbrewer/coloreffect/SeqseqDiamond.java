package geovista.colorbrewer.coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * 
 */

public class SeqseqDiamond extends SchemeObject {

  int vclass;
  int hclass;
  int maxlightness;
  int minlightness;
  int top_angle;
  int shift_h;
  int shift_r;
  int shift_p;

  public LABcolor[][] labcolor;
  public LUVcolor[][] luvcolor;
  public CIECAM02Color[][] cIECAM02color;

  public SeqseqDiamond(int vclass, int hclass, int maxlightness, int minlightness, int top_angle, int shift_h, int shift_r, int shift_p) {

    this.labcolor = new LABcolor[vclass][hclass];
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        LABcolor color = new LABcolor(0.0, 0.0, 0.0);
        this.labcolor[i][j] = color;
      }
    }

    this.luvcolor = new LUVcolor[vclass][hclass];
    this.cIECAM02color = new CIECAM02Color[vclass][hclass];


    this.vclass = vclass;
    this.hclass = hclass;
    this.maxlightness = maxlightness;
    this.minlightness = minlightness;
    this.top_angle = top_angle;
    this.shift_r = shift_r;
    this.shift_h = shift_h;
    this.shift_p = shift_p;

    double L;
    double A;
    double B;
    double radius;
    double divided;
    double divisor;

    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){

        divided = ((maxlightness - minlightness)/(double)2)*((1 + 2*j)/(double)hclass - (1 + 2*i)/(double)vclass);
        divisor = Math.tan(Math.toRadians(90 - top_angle/2)) - Math.tan(Math.toRadians(90 + top_angle/2));
        radius = divided/divisor;

        A = radius*Math.cos(Math.toRadians(shift_p));
        B = radius*Math.sin(Math.toRadians(shift_p));
        L = Math.tan(Math.toRadians(90 - top_angle/2))*radius + maxlightness - (maxlightness - minlightness)*(1 + 2*j)/(2*hclass);

        //adding the deviations
        A = A + shift_r*Math.cos(Math.toRadians(shift_p));
        B = B + shift_r*Math.sin(Math.toRadians(shift_p));
        L = L + shift_h;

        LABcolor color_lab = new LABcolor(L, A, B);
        labcolor[i][j] = color_lab;

        LUVcolor color_luv = new LUVcolor(L, A, B);
        luvcolor[i][j] = color_luv;

        CIECAM02Color color_CIECAM02 = new CIECAM02Color(L, A, B);
        this.cIECAM02color[i][j] = color_CIECAM02;
      }
    }
  }
}