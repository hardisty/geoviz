package coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class DivseqWedge extends SchemeObject{

  //this array stores the colors in lab format
  public LABcolor[][] labcolor;
  public LUVcolor[][] luvcolor;
  public CIECAM02Color[][] cIECAM02color;

  int vclass;
  int hclass;
  int maxlightness;
  int minlightness;
  int alpha;
  int beta;
  int range;
  int shift_r;
  int shift_h;
  int shift_p;

  public DivseqWedge(int vclass, int hclass, int maxlightness, int minlightness, int alpha, int beta, int range, int shift_h, int shift_r, int shift_p) {

    //initializing the object
    this.vclass = vclass;
    this.hclass = hclass;
    this.maxlightness = maxlightness;
    this.minlightness = minlightness;
    this.alpha = alpha;
    this.beta = beta;
    this.range = range;
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

    //create a parameter to make the formula concise
    double gridwidth = range/(hclass - 1);

    //two loops to calculate the matrix
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){

        double r = (j - (double)(hclass - 1)/2.0)*gridwidth;

        //when hclass is even
        if(((hclass - 1)/2)*2 == ((hclass - 2)/2)*2){

          if(j < hclass/2){

            double x = (maxlightness - i*lightnessStep + Math.tan(Math.toRadians(beta))*r)/(Math.tan(Math.toRadians(beta)) - Math.tan(Math.toRadians(90 - alpha/2)));
            double y = Math.tan(Math.toRadians(90 - alpha/2))*x + maxlightness - i*lightnessStep;

            this.labcolor[i][j].a = x*Math.cos(Math.toRadians(shift_p));
            this.labcolor[i][j].b = x*Math.sin(Math.toRadians(shift_p));
            this.labcolor[i][j].L = y;

            this.luvcolor[i][j].U = x*Math.cos(Math.toRadians(shift_p));
            this.luvcolor[i][j].V = x*Math.sin(Math.toRadians(shift_p));
            this.luvcolor[i][j].L = y;

            this.cIECAM02color[i][j].a = x*Math.cos(Math.toRadians(shift_p));
            this.cIECAM02color[i][j].b = x*Math.sin(Math.toRadians(shift_p));
            this.cIECAM02color[i][j].J = y;
          }

          if(j >= hclass/2){
            double x = (maxlightness - i*lightnessStep + Math.tan(Math.toRadians(180 - beta))*r)/(Math.tan(Math.toRadians(180 - beta)) - Math.tan(Math.toRadians(90 + alpha/2)));
            double y = Math.tan(Math.toRadians(90 + alpha/2))*x + maxlightness - i*lightnessStep;

            this.labcolor[i][j].a = x*Math.cos(Math.toRadians(shift_p));
            this.labcolor[i][j].b = x*Math.sin(Math.toRadians(shift_p));
            this.labcolor[i][j].L = y;

            this.luvcolor[i][j].U = x*Math.cos(Math.toRadians(shift_p));
            this.luvcolor[i][j].V = x*Math.sin(Math.toRadians(shift_p));
            this.luvcolor[i][j].L = y;

            this.cIECAM02color[i][j].a = x*Math.cos(Math.toRadians(shift_p));
            this.cIECAM02color[i][j].b = x*Math.sin(Math.toRadians(shift_p));
            this.cIECAM02color[i][j].J = y;
          }

        }

        //when hclass is odd
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

            double x = (maxlightness - i*lightnessStep + Math.tan(Math.toRadians(beta))*r)/(Math.tan(Math.toRadians(beta)) - Math.tan(Math.toRadians(90 - alpha/2)));
            double y = Math.tan(Math.toRadians(90 - alpha/2))*x + maxlightness - i*lightnessStep;

            this.labcolor[i][j].a = x*Math.cos(Math.toRadians(shift_p));
            this.labcolor[i][j].b = x*Math.sin(Math.toRadians(shift_p));
            this.labcolor[i][j].L = y;

            this.luvcolor[i][j].U = x*Math.cos(Math.toRadians(shift_p));
            this.luvcolor[i][j].V = x*Math.sin(Math.toRadians(shift_p));
            this.luvcolor[i][j].L = y;

            this.cIECAM02color[i][j].a = x*Math.cos(Math.toRadians(shift_p));
            this.cIECAM02color[i][j].b = x*Math.sin(Math.toRadians(shift_p));
            this.cIECAM02color[i][j].J = y;
          }

          if(j > (hclass - 1)/2){
            double x = (maxlightness - i*lightnessStep + Math.tan(Math.toRadians(180 - beta))*r)/(Math.tan(Math.toRadians(180 - beta)) - Math.tan(Math.toRadians(90 + alpha/2)));
            double y = Math.tan(Math.toRadians(90 + alpha/2))*x + maxlightness - i*lightnessStep;

            this.labcolor[i][j].a = x*Math.cos(Math.toRadians(shift_p));
            this.labcolor[i][j].b = x*Math.sin(Math.toRadians(shift_p));
            this.labcolor[i][j].L = y;

            this.luvcolor[i][j].U = x*Math.cos(Math.toRadians(shift_p));
            this.luvcolor[i][j].V = x*Math.sin(Math.toRadians(shift_p));
            this.luvcolor[i][j].L = y;

            this.cIECAM02color[i][j].a = x*Math.cos(Math.toRadians(shift_p));
            this.cIECAM02color[i][j].b = x*Math.sin(Math.toRadians(shift_p));
            this.cIECAM02color[i][j].J = y;
          }

        }

        //adding the deviations
        this.labcolor[i][j].a = this.labcolor[i][j].a + shift_r*Math.cos(Math.toRadians(shift_p));
        this.labcolor[i][j].b = this.labcolor[i][j].b + shift_r*Math.sin(Math.toRadians(shift_p));
        this.labcolor[i][j].L = this.labcolor[i][j].L + shift_h;

        this.luvcolor[i][j].U = this.luvcolor[i][j].U + shift_r*Math.cos(Math.toRadians(shift_p));
        this.luvcolor[i][j].V = this.luvcolor[i][j].V + shift_r*Math.sin(Math.toRadians(shift_p));
        this.luvcolor[i][j].L = this.luvcolor[i][j].L + shift_h;

        this.cIECAM02color[i][j].a = this.cIECAM02color[i][j].a + shift_r*Math.cos(Math.toRadians(shift_p));
        this.cIECAM02color[i][j].b = this.cIECAM02color[i][j].b + shift_r*Math.sin(Math.toRadians(shift_p));
        this.cIECAM02color[i][j].J = this.cIECAM02color[i][j].J + shift_h;

        this.cIECAM02color[i][j] = new CIECAM02Color(this.cIECAM02color[i][j].J, this.cIECAM02color[i][j].a, this.cIECAM02color[i][j].b);

      }
    }
  }
}