package geovista.colorbrewer.coloreffect;

import java.util.logging.Logger;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * 
 */

public class CIECAM02toSRGB {

  public double R255, G255, B255;
  protected final static Logger logger = Logger.getLogger(CIECAM02toSRGB.class.getName());
  public CIECAM02toSRGB(double J, double C, double h) {

    CIECAM02toXYZ cIECAM02toXYZ1 = new CIECAM02toXYZ(J, C, h, 95.05, 100.00, 108.88, 20.0, 63.6619, 1.0, 0.69, 1.0);

    XYZ2SRGB xYZ2SRGB1 = new XYZ2SRGB(cIECAM02toXYZ1.X, cIECAM02toXYZ1.Y, cIECAM02toXYZ1.Z);

    this.R255 = xYZ2SRGB1.R255;
    this.G255 = xYZ2SRGB1.G255;
    this.B255 = xYZ2SRGB1.B255;
  }

  public static void main(String[] args) {
    CIECAM02toSRGB CIECAM02toSRGB1 = new CIECAM02toSRGB(59.11, 36.80, 195.68);

    logger.finest("R = " + CIECAM02toSRGB1.R255);
    logger.finest("G = " + CIECAM02toSRGB1.G255);
    logger.finest("B = " + CIECAM02toSRGB1.B255);
  }
}