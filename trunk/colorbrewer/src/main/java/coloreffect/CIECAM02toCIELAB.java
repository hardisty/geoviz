package coloreffect;

import java.util.logging.Logger;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class CIECAM02toCIELAB {

  double L;
  double A;
  double B;
  protected final static Logger logger = Logger.getLogger(CIECAM02toCIELAB.class.getName());
  public CIECAM02toCIELAB(double J, double C, double h) {
	 
    CIECAM02toXYZ cIECAM02toXYZ1 = new CIECAM02toXYZ(J, C, h, 95.05, 100.00, 108.88, 20.0, 63.6619, 1.0, 0.69, 1.0);

    XYZ2LAB xYZ2LAB1 = new XYZ2LAB(cIECAM02toXYZ1.X, cIECAM02toXYZ1.Y, cIECAM02toXYZ1.Z);

    this.L = xYZ2LAB1.L;
    this.A = xYZ2LAB1.A;
    this.B = xYZ2LAB1.B;

  }
  public static void main(String[] args) {
    CIECAM02toCIELAB CIECAM02toCIELAB1 = new CIECAM02toCIELAB(76.94, 60.99, 34.42);

    logger.finest("L = " + CIECAM02toCIELAB1.L);
    logger.finest("A = " + CIECAM02toCIELAB1.A);
    logger.finest("B = " + CIECAM02toCIELAB1.B);
  }
}