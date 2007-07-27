package coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class SeqseqFoldedDiamond extends SchemeObject {

  int vclass;
  int hclass;
  int maxlightness;
  int minlightness;
  int angle;
  int leftwing_position;
  int rightwing_position;
  int shift_h;
  int shift_r;
  int shift_p;

  double L;
  double A;
  double B;
  double radius;
  double divided;
  double divisor;

  public LABcolor[][] labcolor;
  public LABcolor[][] labcolor_left;
  public LABcolor[][] labcolor_right;

  public LUVcolor[][] luvcolor;
  public LUVcolor[][] luvcolor_left;
  public LUVcolor[][] luvcolor_right;

  public CIECAM02Color[][] cIECAM02color;
  public CIECAM02Color[][] cIECAM02color_left;
  public CIECAM02Color[][] cIECAM02color_right;

  public SeqseqFoldedDiamond(int vclass, int hclass, int maxlightness, int minlightness, int angle, int leftwing_position, int rightwing_position, int shift_h, int shift_r, int shift_p) {

    this.vclass = vclass;
    this.hclass = hclass;
    this.maxlightness = maxlightness;
    this.minlightness = minlightness;
    this.angle = angle;
    this.leftwing_position = leftwing_position;
    this.rightwing_position = rightwing_position;
    this.shift_h = shift_h;
    this.shift_r = shift_r;
    this.shift_p = shift_p;

    this.labcolor = new LABcolor[vclass][hclass];
    this.labcolor_left = new LABcolor[vclass][hclass];
    this.labcolor_right = new LABcolor[vclass][hclass];
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        LABcolor color = new LABcolor(0.0, 0.0, 0.0);
        this.labcolor[i][j] = color;
        this.labcolor_left[i][j] = color;
        this.labcolor_right[i][j] = color;
      }
    }



    this.luvcolor = new LUVcolor[vclass][hclass];
    this.luvcolor_left = new LUVcolor[vclass][hclass];
    this.luvcolor_right = new LUVcolor[vclass][hclass];
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        LUVcolor color = new LUVcolor(0.0, 0.0, 0.0);
        this.luvcolor[i][j] = color;
        this.luvcolor_left[i][j] = color;
        this.luvcolor_right[i][j] = color;
      }
    }

    this.cIECAM02color = new CIECAM02Color[vclass][hclass];
    this.cIECAM02color_left = new CIECAM02Color[vclass][hclass];
    this.cIECAM02color_right = new CIECAM02Color[vclass][hclass];
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        CIECAM02Color color = new CIECAM02Color(0.0, 0.0, 0.0);
        this.cIECAM02color[i][j] = color;
        this.cIECAM02color_left[i][j] = color;
        this.cIECAM02color_right[i][j] = color;
      }
    }


    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){

        divided = ((maxlightness - minlightness)/(double)2)*((1 + 2*j)/(double)hclass - (1 + 2*i)/(double)vclass);
        divisor = Math.tan(Math.toRadians(angle/2)) - Math.tan(Math.toRadians(180 - angle/2));
        radius = divided/divisor;

        A = radius*Math.cos(Math.toRadians(leftwing_position + 180 + shift_p));
        B = radius*Math.sin(Math.toRadians(leftwing_position + 180 + shift_p));
        L = Math.tan(Math.toRadians(angle/2))*radius + maxlightness - (maxlightness - minlightness)*(1 + 2*j)/(2*hclass);

        //adding the deviations
        A = A + shift_r*Math.cos(Math.toRadians(leftwing_position + 180 + shift_p));
        B = B + shift_r*Math.sin(Math.toRadians(leftwing_position + 180 + shift_p));
        L = L + shift_h;

        LABcolor color = new LABcolor(L, A, B);
        labcolor_left[i][j] = color;

        LUVcolor color_luv = new LUVcolor(L, A, B);
        luvcolor_left[i][j] = color_luv;

        CIECAM02Color color_CIECAM02 = new CIECAM02Color(L, A, B);
        this.cIECAM02color_left[i][j] = color_CIECAM02;
      }
    }

    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){

        divided = ((maxlightness - minlightness)/(double)2)*((1 + 2*j)/(double)hclass - (1 + 2*i)/(double)vclass);
        divisor = Math.tan(Math.toRadians(angle/2)) - Math.tan(Math.toRadians(180 - angle/2));
        radius = divided/divisor;

        A = radius*Math.cos(Math.toRadians(rightwing_position + shift_p));
        B = radius*Math.sin(Math.toRadians(rightwing_position + shift_p));
        L = Math.tan(Math.toRadians(angle/2))*radius + maxlightness - (maxlightness - minlightness)*(1 + 2*j)/(2*hclass);

        //adding the deviations
        A = A + shift_r*Math.cos(Math.toRadians(rightwing_position + shift_p));
        B = B + shift_r*Math.sin(Math.toRadians(rightwing_position + shift_p));
        L = L + shift_h;

        LABcolor color = new LABcolor(L, A, B);
        labcolor_right[i][j] = color;

        LUVcolor color_luv = new LUVcolor(L, A, B);
        luvcolor_right[i][j] = color_luv;

        CIECAM02Color color_CIECAM02 = new CIECAM02Color(L, A, B);
        this.cIECAM02color_right[i][j] = color_CIECAM02;
      }
    }


    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        if((double)(j + 1)/(double)(i + 1) >= (double)hclass/(double)vclass){
          labcolor[i][j] = labcolor_right[i][j];
          luvcolor[i][j] = luvcolor_right[i][j];
          this.cIECAM02color[i][j] = this.cIECAM02color_right[i][j];
        }
        if((double)(j + 1)/(double)(i + 1) < (double)hclass/(double)vclass){
          labcolor[i][j] = labcolor_left[i][j];
          luvcolor[i][j] = luvcolor_left[i][j];
          this.cIECAM02color[i][j] = this.cIECAM02color_left[i][j];
        }
      }
    }


  }
}