package edu.psu.geovista.colorbrewer.coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class DivdivCone {

  //this array stores the colors in lab format
  public LABcolor[][] labcolor;
  public LUVcolor[][] luvcolor;
  public CIECAM02Color[][] cIECAM02color;


  int vclass;
  int hclass;
  int range;
  int radius;
  int height;
  int shift_r;
  int shift_h;
  int shift_p;

  //the construction method using the cone model
  public DivdivCone(int vclass, int hclass, int range, int height, int radius, int shift_h, int shift_r, int shift_p) {

    //initializing the object
    this.vclass = vclass;
    this.hclass = hclass;
    this.range = range;
    this.height = height;
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

    //create two parameters indicating the position of the center point
    double x = (vclass - 1)/2.0;
    double y = (hclass - 1)/2.0;

    //two loops to fit the a-b grid onto the surface
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        //create two variables to record the a & b value when no rotation happens
        double m = (double)((j - y)*range/hclass);
        double n = (double)((x - i)*range/vclass);

        //a & b are decided by the relative location to the center point, and the degree of rotation
        this.labcolor[i][j].a = m*Math.cos(Math.toRadians(shift_p)) - n*Math.sin(Math.toRadians(shift_p)) + shift_r*Math.cos(Math.toRadians(shift_p));
        this.labcolor[i][j].b = n*Math.cos(Math.toRadians(shift_p)) + m*Math.sin(Math.toRadians(shift_p)) + shift_r*Math.sin(Math.toRadians(shift_p));

        //the cone model
        this.labcolor[i][j].L = height - height*Math.sqrt(Math.pow(this.labcolor[i][j].a, 2) + Math.pow(this.labcolor[i][j].b, 2))/radius + shift_h;


        this.luvcolor[i][j].U = m*Math.cos(Math.toRadians(shift_p)) - n*Math.sin(Math.toRadians(shift_p)) + shift_r*Math.cos(Math.toRadians(shift_p));
        this.luvcolor[i][j].V = n*Math.cos(Math.toRadians(shift_p)) + m*Math.sin(Math.toRadians(shift_p)) + shift_r*Math.sin(Math.toRadians(shift_p));
        this.luvcolor[i][j].L = height - height*Math.sqrt(Math.pow(this.luvcolor[i][j].U, 2) + Math.pow(this.luvcolor[i][j].V, 2))/radius + shift_h;

        this.cIECAM02color[i][j].a = m*Math.cos(Math.toRadians(shift_p)) - n*Math.sin(Math.toRadians(shift_p)) + shift_r*Math.cos(Math.toRadians(shift_p));
        this.cIECAM02color[i][j].b = n*Math.cos(Math.toRadians(shift_p)) + m*Math.sin(Math.toRadians(shift_p)) + shift_r*Math.sin(Math.toRadians(shift_p));
        this.cIECAM02color[i][j].J = height - height*Math.sqrt(Math.pow(this.cIECAM02color[i][j].a, 2) + Math.pow(this.cIECAM02color[i][j].b, 2))/radius + shift_h;

        this.cIECAM02color[i][j] = new CIECAM02Color(this.cIECAM02color[i][j].J, this.cIECAM02color[i][j].a, this.cIECAM02color[i][j].b);

      }
    }
  }
}