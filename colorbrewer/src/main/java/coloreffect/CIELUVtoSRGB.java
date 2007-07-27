package coloreffect;

import java.io.IOException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class CIELUVtoSRGB {

  double R255, G255, B255;

  public CIELUVtoSRGB(double L, double U, double V) {

    CIELUV2XYZ cIELUV2XYZ1 = new CIELUV2XYZ(L, U, V);

    XYZ2SRGB xYZ2SRGB1 = new XYZ2SRGB(cIELUV2XYZ1.X, cIELUV2XYZ1.Y, cIELUV2XYZ1.Z);

    this.R255 = xYZ2SRGB1.R255;
    this.G255 = xYZ2SRGB1.G255;
    this.B255 = xYZ2SRGB1.B255;

  }

  public static void main(String[] args) throws IOException {

    CIELUVtoSRGB cIELUVtoSRGB1 = new CIELUVtoSRGB(40.37, 97.24, 16.60);

    System.out.println("R255 = " + cIELUVtoSRGB1.R255);
    System.out.println("G255 = " + cIELUVtoSRGB1.G255);
    System.out.println("B255 = " + cIELUVtoSRGB1.B255);


  }
}