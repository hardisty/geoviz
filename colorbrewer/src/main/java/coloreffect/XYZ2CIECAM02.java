package coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class XYZ2CIECAM02 {

  //MB: matrix used in chromatic adaptation
  private static final double MB00 =  0.7328;  private static final double MB01 =  0.4296; private static final double MB02 = -0.1624;
  private static final double MB10 = -0.7036;  private static final double MB11 =  1.6975; private static final double MB12 =  0.0061;
  private static final double MB20 =  0.0030;  private static final double MB21 = -0.0136; private static final double MB22 =  0.9834;


  public double J, C, h, Q, M, s;

  public XYZ2CIECAM02(double X, double Y, double Z, double xw, double yw, double zw, double yb, double la, double f, double c, double nc) {

    double r, g, b;
    double rc, gc, bc;
    double rp, gp, bp;
    double rpa, gpa, bpa;
    double rw, gw, bw;
    double f1, d;
    double n, nbb, ncb;
    double a, ca, cb;
    double aw;
    double e, t;
    double cz;

    //double J, C, h, Q, M, s;

    double rwgwbw[] = this.xyz_to_cat02(xw, yw, zw);
    rw = rwgwbw[0];
    gw = rwgwbw[1];
    bw = rwgwbw[2];

    double rgb[] = this.xyz_to_cat02(X, Y, Z);
    r = rgb[0];
    g = rgb[1];
    b = rgb[2];

    n = yb/yw;
    d = this.D_factor(f, la);
    f1 = this.calculate_F1_from_LA_CIECAM02(la);
    nbb = 0.725*Math.pow(1.0/n, 0.2);
    ncb = nbb;
    cz = 1.48 + Math.sqrt(n);
    aw = this.achromatic_response_to_white(xw, yw, zw, d, f1, nbb);

    rc = r*(((yw*d)/rw) + (1.0 - d));
    gc = g*(((yw*d)/gw) + (1.0 - d));
    bc = b*(((yw*d)/bw) + (1.0 - d));

    double rpgpbp[] = new double[3];
    rpgpbp = this.cat02_to_hpe(rc, gc, bc);
    rp = rpgpbp[0];
    gp = rpgpbp[1];
    bp = rpgpbp[2];

    rpa = this.nonlinear_adaptation(rp, f1);
    gpa = this.nonlinear_adaptation(gp, f1);
    bpa = this.nonlinear_adaptation(bp, f1);

    ca = rpa - ((12.0*gpa)/11.0) + (bpa/11.0);
    cb = (1.0/9.0)*(rpa + gpa - (2.0*bpa));

    h = (180.0/Math.PI)*Math.atan2(cb, ca);
    if(h < 0){
      h = h +360;
    }

    a = ((2.0*rpa) + gpa + ((1.0/20.0)*bpa) - 0.305)*nbb;

    J = 100.0*Math.pow(a/aw, c*cz);

    e = ((12500.0/13.0)*nc*ncb)*(Math.cos(((h*Math.PI)/180.0) + 2.0) + 3.8);

    t = (e*Math.sqrt(ca*ca + cb*cb))/(rpa + gpa + ((21.0/20.0)*bpa));

    C = Math.pow(t, 0.9)*Math.sqrt(J/100.0)*Math.pow(1.64 - Math.pow(0.29, n), 0.73);

    Q = (4.0/C)*Math.sqrt(J/100.0)*(aw + 4.0)*Math.pow(f1, 0.25);

    M = C*Math.pow(f1, 0.25);

    s = 100.0*Math.sqrt(M/Q);

  }

  private double calculate_F1_from_LA_CIECAM02(double LA){

    double LA5 = LA*5.0;
    double k = 1.0/(LA5 + 1.0);
    k = k*k;
    k = k*k;
    double F1 = (0.2*k*LA5) + (0.1*(1.0 - k)*(1.0 - k)*Math.pow(LA5, 1.0/3.0));

    return F1;

  }

  private double[] xyz_to_cat02(double x, double y, double z){

    double r = XYZ2CIECAM02.MB00*x + XYZ2CIECAM02.MB01*y + XYZ2CIECAM02.MB02*z;
    double g = XYZ2CIECAM02.MB10*x + XYZ2CIECAM02.MB11*y + XYZ2CIECAM02.MB12*z;
    double b = XYZ2CIECAM02.MB20*x + XYZ2CIECAM02.MB21*y + XYZ2CIECAM02.MB22*z;

    double rgb[] = new double[3];
    rgb[0] = r;
    rgb[1] = g;
    rgb[2] = b;

    return rgb;

  }

//  private double[] hpe_to_xyz(double r, double g, double b){
//
//    double xyz[] = new double[3];
//    xyz[0] = 1.91020*r - 1.11212*g + 0.20191*b;
//    xyz[1] = 0.37095*r + 0.62905*g - 0.00001*b;
//    xyz[2] = b;
//
//    return xyz;
//
//  }

  private double[] cat02_to_hpe(double r, double g, double b){

    double hpe[] = new double[3];
    hpe[0] =  0.7409792*r + 0.2180250*g + 0.0410058*b;
    hpe[1] =  0.2853532*r + 0.6242014*g + 0.0904454*b;
    hpe[2] = -0.0096280*r - 0.0056980*g + 1.0153260*b;
    return hpe;

  }

  private double D_factor(double f, double LA){

    double D = f*(1.0 - ((1.0/3.6)*Math.pow(Math.E, (-1*LA - 42.0)/92.0)));

    return D;

  }

//  private double[] Aab_to_rgb(double A, double aa, double bb, double Nbb){
//
//    double x = (A/Nbb) + 0.305;
//
//    double rgb[] = new double[3];
//    rgb[0] = 0.32787*x + 0.32145*aa + 0.20527*bb;
//    rgb[1] = 0.32787*x - 0.63507*aa - 0.18603*bb;
//    rgb[2] = 0.32787*x - 0.15681*aa - 4.49038*bb;
//
//    return rgb;
//
//  }

  private double nonlinear_adaptation(double c, double f1){

    double p = Math.pow((f1*c/100.0), 0.42);
    double result = (400.0*p)/(27.13 + p) + 0.1;
    return result;

  }

  private double achromatic_response_to_white(double x, double y, double z, double D, double f1, double nbb){

    double r, g, b;
    double rc, gc, bc;
    double rpa, gpa, bpa;
    double rgb[] = new double[3];
    double rpgpbp[] = new double[3];
    double result;

    rgb = this.xyz_to_cat02(x, y, z);
    r = rgb[0];
    g = rgb[1];
    b = rgb[2];

    rc = r*((y*D/r) + (1.0 - D));
    gc = g*((y*D/g) + (1.0 - D));
    bc = b*((y*D/b) + (1.0 - D));

    rpgpbp = this.cat02_to_hpe(rc, gc, bc);

    rpa = this.nonlinear_adaptation(rpgpbp[0], f1);
    gpa = this.nonlinear_adaptation(rpgpbp[1], f1);
    bpa = this.nonlinear_adaptation(rpgpbp[2], f1);

    result = (2.0*rpa + gpa + (1.0/20.0)*bpa - 0.305)*nbb;

    return result;

  }

  public static void main(String[] args) {
    XYZ2CIECAM02 XYZ2CIECAM021 = new XYZ2CIECAM02(19.31, 23.93, 10.14, 95.05, 100.00, 108.88, 20.0, 63.6619, 1.0, 0.69, 1.0);

    System.out.println("J = " + XYZ2CIECAM021.J);
    System.out.println("C = " + XYZ2CIECAM021.C);
    System.out.println("h = " + XYZ2CIECAM021.h);
  }
}