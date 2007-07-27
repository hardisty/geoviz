package coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class DivseqTrapezoid extends SchemeObject{

  //this array stores the colors in lab format
  public LABcolor[][] labcolor;
  public LUVcolor[][] luvcolor;
  public CIECAM02Color[][] cIECAM02color;

  int vclass;
  int hclass;
  int maxlightness;
  int minlightness;
  int alpha;
  int radius;
  int shift_r;
  int shift_h;
  int shift_p;

  public DivseqTrapezoid(int vclass, int hclass, int maxlightness, int minlightness, int alpha, int radius, int shift_h, int shift_r, int shift_p) {

    //initializing the object
    this.vclass = vclass;
    this.hclass = hclass;
    this.maxlightness = maxlightness;
    this.minlightness = minlightness;
    this.alpha = alpha;
    this.radius = radius;
    this.shift_r = shift_r;
    this.shift_h = shift_h;
    this.shift_p = shift_p;

    //initializing the labcolor array
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

    this.cIECAM02color = new CIECAM02Color[vclass][hclass];
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        CIECAM02Color color = new CIECAM02Color(0.0, 0.0, 0.0);
        this.cIECAM02color[i][j] = color;
      }
    }

    //create a lightness sequence
    int[] lightness = new int[vclass];
    lightness[0] = maxlightness;
    lightness[vclass - 1] = minlightness;
    double lightnessStep;
    lightnessStep = (maxlightness - minlightness)/(double)(vclass - 1);
    for(int i = 1; i < vclass - 1; i ++){
      lightness[i] = (int)(maxlightness - i*lightnessStep);
    }

    //create a list of parameters to make the formula concise
    double height = Math.tan(Math.toRadians(alpha))*radius + minlightness;
    double slice = (double)(180 - 2*alpha)/(double)(hclass - 1);

    //two loops to calculate the matrix
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){

        if(((hclass - 1)/2)*2 == ((hclass - 2)/2)*2){

          if(j < hclass/2){

            double y = lightness[i];
            double x = (y - height)/Math.tan(Math.toRadians(alpha + j*slice));

            this.labcolor[i][j].a = x*Math.cos(Math.toRadians(shift_p));
            this.labcolor[i][j].b = x*Math.sin(Math.toRadians(shift_p));
            this.labcolor[i][j].L = y;

            this.luvcolor[i][j].U = x*Math.cos(Math.toRadians(shift_p));
            this.luvcolor[i][j].V = x*Math.sin(Math.toRadians(shift_p));
            this.luvcolor[i][j].L = y;

            this.cIECAM02color[i][j].a = x*Math.cos(Math.toRadians(shift_p));
            this.cIECAM02color[i][j].b = x*Math.sin(Math.toRadians(shift_p));
            this.cIECAM02color[i][j].J = y;

            this.cIECAM02color[i][j] = new CIECAM02Color(this.cIECAM02color[i][j].J, this.cIECAM02color[i][j].a, this.cIECAM02color[i][j].b);
          }

          if(j >= hclass/2){
            double y = lightness[i];
            double x = (y - height)/Math.tan(Math.toRadians(180 - alpha - (hclass - 1 - j)*slice));

            this.labcolor[i][j].a = x*Math.cos(Math.toRadians(shift_p));
            this.labcolor[i][j].b = x*Math.sin(Math.toRadians(shift_p));
            this.labcolor[i][j].L = y;

            this.luvcolor[i][j].U = x*Math.cos(Math.toRadians(shift_p));
            this.luvcolor[i][j].V = x*Math.sin(Math.toRadians(shift_p));
            this.luvcolor[i][j].L = y;

            this.cIECAM02color[i][j].a = x*Math.cos(Math.toRadians(shift_p));
            this.cIECAM02color[i][j].b = x*Math.sin(Math.toRadians(shift_p));
            this.cIECAM02color[i][j].J = y;

            this.cIECAM02color[i][j] = new CIECAM02Color(this.cIECAM02color[i][j].J, this.cIECAM02color[i][j].a, this.cIECAM02color[i][j].b);
          }

        }

        else{

          if(j == (hclass - 1)/2){
            this.labcolor[i][j].a = 0;
            this.labcolor[i][j].b = 0;
            this.labcolor[i][j].L = lightness[i];

            this.luvcolor[i][j].U = 0;
            this.luvcolor[i][j].V = 0;
            this.luvcolor[i][j].L = lightness[i];

            this.cIECAM02color[i][j].a = 0;
            this.cIECAM02color[i][j].b = 0;
            this.cIECAM02color[i][j].J = lightness[i];
          }

          if(j < (hclass - 1)/2){

            double y = lightness[i];
            double x = (y - height)/Math.tan(Math.toRadians(alpha + j*slice));

            this.labcolor[i][j].a = x*Math.cos(Math.toRadians(shift_p));
            this.labcolor[i][j].b = x*Math.sin(Math.toRadians(shift_p));
            this.labcolor[i][j].L = y;

            this.luvcolor[i][j].U = x*Math.cos(Math.toRadians(shift_p));
            this.luvcolor[i][j].V = x*Math.sin(Math.toRadians(shift_p));
            this.luvcolor[i][j].L = y;

            this.cIECAM02color[i][j].a = x*Math.cos(Math.toRadians(shift_p));
            this.cIECAM02color[i][j].b = x*Math.sin(Math.toRadians(shift_p));
            this.cIECAM02color[i][j].J = y;

            this.cIECAM02color[i][j] = new CIECAM02Color(this.cIECAM02color[i][j].J, this.cIECAM02color[i][j].a, this.cIECAM02color[i][j].b);
          }

          if(j > (hclass - 1)/2){
            double y = lightness[i];
            double x = (y - height)/Math.tan(Math.toRadians(180 - alpha - (hclass - 1 - j)*slice));

            this.labcolor[i][j].a = x*Math.cos(Math.toRadians(shift_p));
            this.labcolor[i][j].b = x*Math.sin(Math.toRadians(shift_p));
            this.labcolor[i][j].L = y;

            this.luvcolor[i][j].U = x*Math.cos(Math.toRadians(shift_p));
            this.luvcolor[i][j].V = x*Math.sin(Math.toRadians(shift_p));
            this.luvcolor[i][j].L = y;

            this.cIECAM02color[i][j].a = x*Math.cos(Math.toRadians(shift_p));
            this.cIECAM02color[i][j].b = x*Math.sin(Math.toRadians(shift_p));
            this.cIECAM02color[i][j].J = y;

            this.cIECAM02color[i][j] = new CIECAM02Color(this.cIECAM02color[i][j].J, this.cIECAM02color[i][j].a, this.cIECAM02color[i][j].b);
          }

        }
      }
    }
  }
}