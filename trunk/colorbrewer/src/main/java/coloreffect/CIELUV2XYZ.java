package coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class CIELUV2XYZ {

  //Default setting: The following Xn, Yn, and Zn are based on D65 x, y and z values and the actual Yn reading
  public static final double Xn =   95.0456;
  public static final double Yn =  100.0000;
  public static final double Zn =  108.8754;

  double X, Y, Z;

  public CIELUV2XYZ(double L, double U, double V) {

    this.Y = (L + 16.0)/116.0;

    if(Math.pow(this.Y, 3) > 0.008856){
      this.Y = Math.pow(this.Y, 3);
    }

    else{
      this.Y = (this.Y - 16.0/116.0)/7.787;
    }

    double un = (4.0*CIELUV2XYZ.Xn)/(CIELUV2XYZ.Xn + 15.0*CIELUV2XYZ.Yn + 3.0*CIELUV2XYZ.Zn);
    double vn = (9.0*CIELUV2XYZ.Yn)/(CIELUV2XYZ.Xn + 15.0*CIELUV2XYZ.Yn + 3.0*CIELUV2XYZ.Zn);

    double u = U/(13.0*L) + un;
    double v = V/(13.0*L) + vn;

    this.Y = this.Y*100;
    this.X = (9.0*this.Y*u)/(4.0*v);
    this.Z = (9.0*this.Y - 15.0*v*this.Y - v*this.X)/(3.0*v);

  }
  public static void main(String[] args) {
    CIELUV2XYZ CIELUV2XYZ1 = new CIELUV2XYZ(40.37, 97.24, 16.60);

    System.out.println("X = " + CIELUV2XYZ1.X);
    System.out.println("Y = " + CIELUV2XYZ1.Y);
    System.out.println("Z = " + CIELUV2XYZ1.Z);
  }
}