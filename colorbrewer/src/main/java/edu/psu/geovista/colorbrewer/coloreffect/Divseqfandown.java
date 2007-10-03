package edu.psu.geovista.colorbrewer.coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Divseqfandown {

  //this array stores the colors in lab format
   public LABcolor[][] labcolor;

   public Divseqfandown(int vclass, int hclass, int maxlightness, int minlightness, int alpha, int radius, int startingcolor) {

     //initializing the labcolor array
     this.labcolor = new LABcolor[vclass][hclass];
     for(int i = 0; i < vclass; i ++){
       for(int j = 0; j < hclass; j ++){
         LABcolor color = new LABcolor(0.0, 0.0, 0.0);
         this.labcolor[i][j] = color;
       }
     }

     //r & angle are the real properties of the color vector
     double r;
     double angle;

     //two loops to calculate the matrix
     for(int i = 0; i < vclass; i ++){
       for(int j = 0; j < hclass; j ++){
         r = minlightness + (maxlightness - minlightness)*i/(vclass - 1);
         angle = (180 - alpha)/2 + alpha*j/(hclass - 1);
         this.labcolor[i][j].a = r*Math.cos(Math.toRadians(angle))*Math.cos(Math.toRadians(startingcolor));
         this.labcolor[i][j].b = r*Math.cos(Math.toRadians(angle))*Math.sin(Math.toRadians(startingcolor));
         this.labcolor[i][j].L = r*Math.sin(Math.toRadians(angle));
       }
     }
   }
}