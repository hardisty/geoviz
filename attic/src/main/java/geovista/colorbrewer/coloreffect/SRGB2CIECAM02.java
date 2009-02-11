package geovista.colorbrewer.coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * 
 */

public class SRGB2CIECAM02 {

  public double J, C, h, Q, M, s;

  public SRGB2CIECAM02(double R255, double G255, double B255) {

    SRGB2XYZ sRGB2XYZ1 = new SRGB2XYZ(R255, G255, B255);

    XYZ2CIECAM02 xYZ2CIECAM021 = new XYZ2CIECAM02(sRGB2XYZ1.X, sRGB2XYZ1.Y, sRGB2XYZ1.Z, 95.05, 100.00, 108.88, 20.0, 63.6619, 1.0, 0.69, 1.0);

    this.J = xYZ2CIECAM021.J;
    this.C = xYZ2CIECAM021.C;
    this.h = xYZ2CIECAM021.h;
  }

  public static void main(String[] args) {
    SRGB2CIECAM02 SRGB2CIECAM021 = new SRGB2CIECAM02(255, 204, 128);

    logger.info("J = " + SRGB2CIECAM021.J);
    logger.info("C = " + SRGB2CIECAM021.C);
    logger.info("h = " + SRGB2CIECAM021.h);
  }
}