package geovista.colorbrewer;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class SRGBToCIELab
 Copyright (c), 2004, GeoVISTA Center
 All Rights Reserved.
 Original Author: Biliang Zhou
 * @version 1.0
 */

public class SRGBToCIELab {

  //The following matrix is set for D65 white point
  public static final double a00 =  0.412453;  public static final double a01 =  0.35758; public static final double a02 =  0.180423;
  public static final double a10 =  0.212671;  public static final double a11 =  0.71516; public static final double a12 =  0.072169;
  public static final double a20 =  0.019334;  public static final double a21 =  0.119193; public static final double a22 = 0.950227;

  double SR;
  double SG;
  double SB;

  double R709;
  double G709;
  double B709;

  double R255;
  double G255;
  double B255;

  double X;
  double Y;
  double Z;

  double L;
  double a;
  double b;

  //Default setting: The following Xn, Yn, and Zn are based on D65 x, y and z values and the actual Yn reading
  public static final double Xn =   95.0456;
  public static final double Yn =  100.0000;
  public static final double Zn =  108.8754;

  public SRGBToCIELab(int R255, int G255, int B255) {

    this.R255 = (double)R255;
    this.G255 = (double)G255;
    this.B255 = (double)B255;

    //step1
    this.SR = this.R255/(double)255;
    this.SG = this.G255/(double)255;
    this.SB = this.B255/(double)255;

    //step2
    if(this.SR <= 0.04045){
      this.R709 = this.SR/12.92;
    }
    else{
      this.R709 = Math.pow((this.SR + 0.055)/1.055, 2.4);
    }

    if(this.SG <= 0.04045){
      this.G709 = this.SG/12.92;
    }
    else{
      this.G709 = Math.pow((this.SG + 0.055)/1.055, 2.4);
    }

    if(this.SB <= 0.04045){
      this.B709 = this.SB/12.92;
    }
    else{
      this.B709 = Math.pow((this.SB + 0.055)/1.055, 2.4);
    }

    //step3
    this.X = (a00*this.R709 + a01*this.G709 + a02*this.B709)*100;
    this.Y = (a10*this.R709 + a11*this.G709 + a12*this.B709)*100;
    this.Z = (a20*this.R709 + a21*this.G709 + a22*this.B709)*100;

    //step4
    if(this.Y/SRGBToCIELab.Yn > 0.008856){
      this.L = 116*Math.pow(this.Y/SRGBToCIELab.Yn, 0.33333) - 16;
      this.a = 500*(Math.pow(this.X/SRGBToCIELab.Xn, 0.33333) - Math.pow(this.Y/SRGBToCIELab.Yn, 0.33333));
      this.b = 200*(Math.pow(this.Y/SRGBToCIELab.Yn, 0.33333) - Math.pow(this.Z/SRGBToCIELab.Zn, 0.33333));
    }

    else{
      this.L = 903.3*(this.Y/SRGBToCIELab.Yn);
      this.a = 500*7.787*(this.X/SRGBToCIELab.Xn - this.Y/SRGBToCIELab.Yn);
      this.b = 200*7.787*(this.Y/SRGBToCIELab.Yn - this.Z/SRGBToCIELab.Zn);
    }
  }

  public static void main(String[] args) throws IOException {
    SRGBToCIELab SRGBToCIELab1 = new SRGBToCIELab(5, 23, 17);

    // write the data out
    DataOutputStream out = new DataOutputStream(new FileOutputStream("D:/data2.txt"));

    Double dou1 = new Double(SRGBToCIELab1.L);
    out.writeChars(dou1.toString());
    out.writeChar('\t');
    Double dou2 = new Double(SRGBToCIELab1.a);
    out.writeChars(dou2.toString());
    out.writeChar('\t');
    Double dou3 = new Double(SRGBToCIELab1.b);
    out.writeChars(dou3.toString());
    out.writeChar('\t');

    out.close();
  }
}