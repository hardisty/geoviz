package geovista.colorbrewer.coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class SRGB2CIELUV {

  double L, U, V;

  public SRGB2CIELUV(double R255, double G255, double B255) {

    SRGB2XYZ sRGB2XYZ1 = new SRGB2XYZ(R255, G255, B255);
    XYZ2LUV xYZ2LUV1 = new XYZ2LUV(sRGB2XYZ1.X, sRGB2XYZ1.Y, sRGB2XYZ1.Z);

    this.L = xYZ2LUV1.L;
    this.U = xYZ2LUV1.U;
    this.V = xYZ2LUV1.V;

  }

  public static void main(String[] args) {
    SRGB2CIELUV SRGB2CIELUV1 = new SRGB2CIELUV(230, 163, 42);

    System.out.println("L = " + SRGB2CIELUV1.L);
    System.out.println("U = " + SRGB2CIELUV1.U);
    System.out.println("V = " + SRGB2CIELUV1.V);
  }
}