package geovista.colorbrewer;

/**
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class Divseqtrapezoid
 Copyright (c), 2004, GeoVISTA Center
 All Rights Reserved.
 Original Author: Biliang Zhou
 * @version 1.0
 */

public class Divseqtrapezoid {

  //this array stores the colors in lab format
  public LABcolor[][] labcolor;

  int vclass;
  int hclass;
  int maxlightness;
  int minlightness;
  int alpha;
  int radius;
  int dx;
  int dy;
  int startinghue;

  public Divseqtrapezoid(int vclass, int hclass, int maxlightness, int minlightness, int alpha, int radius, int dx, int dy, int startinghue) {

    //initializing the object
    this.vclass = vclass;
    this.hclass = hclass;
    this.maxlightness = maxlightness;
    this.minlightness = minlightness;
    this.alpha = alpha;
    this.radius = radius;
    this.dx = dx;
    this.dy = dy;
    this.startinghue = startinghue;

    //initializing the labcolor array
    this.labcolor = new LABcolor[vclass][hclass];
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        LABcolor color = new LABcolor(0.0, 0.0, 0.0);
        this.labcolor[i][j] = color;
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

    //create a list of sets to make the formula concise
    double height = Math.tan(Math.toRadians(alpha))*radius;
    double x = radius - (minlightness/Math.tan(Math.toRadians(alpha)));

    //figuring out whether the numberofClasses is odd or even
    double n = (double)(hclass);
    double k = Math.ceil(n/2.0);

    //two loops to calculate the matrix
    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        //if the number of classes is odd
        if((k % 2) == 1){
          //for the column at the center, assign gray color
          if(j == (int)(k - 1)){
            this.labcolor[i][j].a = 0;
            this.labcolor[i][j].b = 0;
            this.labcolor[i][j].L = lightness[i];
          }
          else{
            double r = (height - lightness[i])*(2*j*x/hclass - x)/(height - minlightness);
            this.labcolor[i][j].a = r*Math.cos(Math.toRadians(startinghue)) + dx*Math.cos(Math.toRadians(startinghue));
            this.labcolor[i][j].b = r*Math.sin(Math.toRadians(startinghue)) + dx*Math.sin(Math.toRadians(startinghue));
            this.labcolor[i][j].L = lightness[i] + dy;
          }
        }

        //if the number of classes is even
        else{
          double r = (height - lightness[i])*(2*j*x/hclass - x)/(height - minlightness);
          this.labcolor[i][j].a = r*Math.cos(Math.toRadians(startinghue)) + dx*Math.cos(Math.toRadians(startinghue));
          this.labcolor[i][j].b = r*Math.sin(Math.toRadians(startinghue)) + dx*Math.sin(Math.toRadians(startinghue));
          this.labcolor[i][j].L = lightness[i] + dy;
        }
      }
    }
  }


}