package geovista.colorbrewer.coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * 
 */

public class XYZ2LUV {

  //Default setting: The following Xn, Yn, and Zn are based on D65 x, y and z values and the actual Yn reading
  public static final double Xn =   95.0456;
  public static final double Yn =  100.0000;
  public static final double Zn =  108.8754;

  double L, U, V;

  public XYZ2LUV(double X, double Y, double Z) {

    double un = (4.0*XYZ2LUV.Xn)/(XYZ2LUV.Xn + 15.0*XYZ2LUV.Yn + 3.0*XYZ2LUV.Zn);
    double vn = (9.0*XYZ2LUV.Yn)/(XYZ2LUV.Xn + 15.0*XYZ2LUV.Yn + 3.0*XYZ2LUV.Zn);

    double u = (4.0*X)/(X + 15.0*Y + 3.0*Z);
    double v = (9.0*Y)/(X + 15.0*Y + 3.0*Z);

    Y = Y/100.0;

    if(Y > 0.008856){
      Y = Math.pow(Y, 1.0/3);
    }

    else{
      Y = 7.787*Y + 16.0/116.0;
    }

    this.L = 116.0*Y - 16;
    this.U = 13*this.L*(u - un);
    this.V = 13*this.L*(v - vn);

  }

  public static void main(String[] args) {
    XYZ2LUV XYZ2LUV1 = new XYZ2LUV(19.78, 11.47, 4.88);

    System.out.println("L = " + XYZ2LUV1.L);
    System.out.println("U = " + XYZ2LUV1.U);
    System.out.println("V = " + XYZ2LUV1.V);
  }
}