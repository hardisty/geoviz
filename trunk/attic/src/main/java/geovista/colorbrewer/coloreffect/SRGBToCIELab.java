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

public class SRGBToCIELab {

  double L;
  double A;
  double B;

  public SRGBToCIELab(int R255, int G255, int B255) {

    SRGB2XYZ sRGB2XYZ1 = new SRGB2XYZ(R255, G255, B255);

    XYZ2LAB xYZ2LAB1 = new XYZ2LAB(sRGB2XYZ1.X, sRGB2XYZ1.Y, sRGB2XYZ1.Z);

    this.L = xYZ2LAB1.L;
    this.A = xYZ2LAB1.A;
    this.B = xYZ2LAB1.B;

  }

  public static void main(String[] args) throws IOException {
    SRGBToCIELab SRGBToCIELab1 = new SRGBToCIELab(238, 200, 27);

    logger.info("L = " + SRGBToCIELab1.L);
    logger.info("A = " + SRGBToCIELab1.A);
    logger.info("B = " + SRGBToCIELab1.B);
  }
}