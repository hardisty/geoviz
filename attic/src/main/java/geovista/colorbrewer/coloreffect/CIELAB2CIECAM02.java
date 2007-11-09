package geovista.colorbrewer.coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class CIELAB2CIECAM02 {

  public double J, C, h, Q, M, s;

  public CIELAB2CIECAM02(double L, double A, double B) {

    LAB2XYZ lAB2XYZ1 = new LAB2XYZ(L, A, B);

    XYZ2CIECAM02 xYZ2CIECAM021 = new XYZ2CIECAM02(lAB2XYZ1.X, lAB2XYZ1.Y, lAB2XYZ1.Z, 95.05, 100.00, 108.88, 20.0, 63.6619, 1.0, 0.69, 1.0);

    this.J = xYZ2CIECAM021.J;
    this.C = xYZ2CIECAM021.C;
    this.h = xYZ2CIECAM021.h;
  }
  public static void main(String[] args) {
    CIELAB2CIECAM02 CIELAB2CIECAM021 = new CIELAB2CIECAM02(80, 50, 40);

    System.out.println("J = " + CIELAB2CIECAM021.J);
    System.out.println("C = " + CIELAB2CIECAM021.C);
    System.out.println("h = " + CIELAB2CIECAM021.h);
  }
}