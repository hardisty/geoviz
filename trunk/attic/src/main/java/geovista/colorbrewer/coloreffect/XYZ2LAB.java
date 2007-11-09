package geovista.colorbrewer.coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class XYZ2LAB {

  //Default setting: The following Xn, Yn, and Zn are based on D65 x, y and z values and the actual Yn reading
  public static final double Xn =   95.0456;
  public static final double Yn =  100.0000;
  public static final double Zn =  108.8754;

  //public static final double Xn =   109.85;
  //public static final double Yn =  100.00;
  //public static final double Zn =  35.58;

  double L;
  double A;
  double B;

  public XYZ2LAB(double X, double Y, double Z) {

    this.L = 116*this.function(Y/XYZ2LAB.Yn) - 16;
    this.A = 500*(this.function(X/XYZ2LAB.Xn) - this.function(Y/XYZ2LAB.Yn));
    this.B = 200*(this.function(Y/XYZ2LAB.Yn) - this.function(Z/XYZ2LAB.Zn));

  }

  private double function(double x){

    double y;

    if(x > 0.008856){
      y = Math.pow(x, 1.0/3.0);
    }

    else{
      y = 7.787*x + 16.0/116.0;
    }

    return y;
  }

  public static void main(String[] args) {
    XYZ2LAB XYZ2LAB1 = new XYZ2LAB(14.51, 23.44, 9.39);

    System.out.println("L = " + XYZ2LAB1.L);
    System.out.println("A = " + XYZ2LAB1.A);
    System.out.println("B = " + XYZ2LAB1.B);
  }

}