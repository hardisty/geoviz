package geovista.colorbrewer.coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * 
 */

public class QuaseqCone extends SchemeObject{

  public LABcolor[][] labcolor;
  public LUVcolor[][] luvcolor;
  public CIECAM02Color[][] cIECAM02Color;

  int vclass;
  int hclass;
  int maxlightness;
  int minlightness;
  int height;
  int radius;
  int shift_r;
  int shift_h;
  int shift_p;

  //sampling on the surface of a cone
  public QuaseqCone(int vclass, int hclass, int maxlightness, int minlightness, int height, int radius, int shift_h, int shift_r, int shift_p) {

    //initializing the object
    this.vclass = vclass;
    this.hclass = hclass;
    this.maxlightness = maxlightness;
    this.minlightness = minlightness;
    this.height = height;
    this.radius = radius;
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
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        LUVcolor color = new LUVcolor(0.0, 0.0, 0.0);
        this.luvcolor[i][j] = color;
      }
    }

    this.cIECAM02Color = new CIECAM02Color[vclass][hclass];
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        CIECAM02Color color = new CIECAM02Color(0.0, 0.0, 0.0);
        this.cIECAM02Color[i][j] = color;
      }
    }

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

      //the cone structure
      r = (height - L)*radius/height;

      for(int j = 0; j < hclass; j ++){
        //do a circle movement
        a = r*Math.cos(Math.toRadians(j*angle + shift_p));
        b = r*Math.sin(Math.toRadians(j*angle + shift_p));
        

        //adding the deviations
        a = a + shift_r*Math.cos(Math.toRadians(shift_p));
        b = b + shift_r*Math.sin(Math.toRadians(shift_p));
        L = L + shift_h;

        LABcolor c = new LABcolor(L, a, b);
        labcolor[i][j] = c;

        LUVcolor color_luv = new LUVcolor(L, a, b);
        luvcolor[i][j] = color_luv;

        CIECAM02Color color_CIECAM02 = new CIECAM02Color(L, a, b);
        this.cIECAM02Color[i][j] = color_CIECAM02;
      }
    }
  }
}