package geovista.colorbrewer.coloreffect;

import java.io.IOException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * 
 */

public class CIELabToSRGB {

  double R255;
  double G255;
  double B255;

  //Default setting: The following Xn, Yn, and Zn are based on D65 x, y and z values and the actual Yn reading
  public static final double Xn =   95.0456;
  public static final double Yn =  100.0000;
  public static final double Zn =  108.8754;

  //The following Xn, Yn, and Zn are based on the actual CRT test results
  /*
  public static final double Xn =  100.3442;
  public static final double Yn =  113.0686;
  public static final double Zn =  146.6129;
  */

  /*
  //The following Xn, Yn, and Zn are based on SRGB x, y and z values and Yn = 80
  public static final double Xn =  76.03647416;
  public static final double Yn =  80;
  public static final double Zn =  87.10030395;
  */

  public CIELabToSRGB(double L, double A, double B) {

    LAB2XYZ lAB2XYZ1 = new LAB2XYZ(L, A, B);

    XYZ2SRGB xYZ2SRGB1 = new XYZ2SRGB(lAB2XYZ1.X, lAB2XYZ1.Y, lAB2XYZ1.Z);

    this.R255 = xYZ2SRGB1.R255;
    this.G255 = xYZ2SRGB1.G255;
    this.B255 = xYZ2SRGB1.B255;
  }



  public static void main(String[] args) throws IOException {
    CIELabToSRGB cIELabToSRGB1 = new CIELabToSRGB(6.19, -7.43, 1.55);

    System.out.println("R255 = " + cIELabToSRGB1.R255);
    System.out.println("G255 = " + cIELabToSRGB1.G255);
    System.out.println("B255 = " + cIELabToSRGB1.B255);
  }
}