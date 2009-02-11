package geovista.colorbrewer.coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * 
 */

public class LAB2XYZ {

  //Default setting: The following Xn, Yn, and Zn are based on D65 x, y and z values and the actual Yn reading
  public static final double Xn =   95.0456;
  public static final double Yn =  100.0000;
  public static final double Zn =  108.8754;

  double X;
  double Y;
  double Z;

  public LAB2XYZ(double L, double A, double B) {

    this.X = Xn*Math.pow(((double)(L + 16)/(double)116 + A/500), 3);
    this.Y = Yn*Math.pow((double)(L + 16)/(double)116, 3);
    this.Z = Zn*Math.pow(((double)(L + 16)/(double)116 - B/200), 3);

  }

  public static void main(String[] args) {
    LAB2XYZ LAB2XYZ1 = new LAB2XYZ(55.52, -41.06, 34.95);

    logger.info("X = " + LAB2XYZ1.X);
    logger.info("Y = " + LAB2XYZ1.Y);
    logger.info("Z = " + LAB2XYZ1.Z);
  }
}