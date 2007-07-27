package coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class CIECAM02toXYZ {

  //MB: matrix used in chromatic adaptation
  private static final double MB00 =  0.7328;  private static final double MB01 =  0.4296; private static final double MB02 = -0.1624;
  private static final double MB10 = -0.7036;  private static final double MB11 =  1.6975; private static final double MB12 =  0.0061;
  private static final double MB20 =  0.0030;  private static final double MB21 = -0.0136; private static final double MB22 =  0.9834;

  public double X, Y, Z;

  public CIECAM02toXYZ(double J, double C, double h, double xw, double yw, double zw, double yb, double la, double f, double c, double nc) {

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
    double x, y, z;

    double rwgwbw[] = this.xyz_to_cat02(xw, yw, zw);
    rw = rwgwbw[0];
    gw = rwgwbw[1];
    bw = rwgwbw[2];

    n = yb/yw;

    d = this.D_factor(f, la);

    f1 = this.calculate_F1_from_LA_CIECAM02(la);

    nbb = 0.725*Math.pow(1.0/n, 0.2);

    ncb = nbb;

    cz = 1.48 + Math.sqrt(n);

    aw = this.achromatic_response_to_white(xw, yw, zw, d, f1, nbb);

    e = ((12500.0/13.0)*nc*ncb)*((Math.cos((h*Math.PI/180.0) + 2.0)) + 3.8);

    a = Math.pow(J/100.0, 1.0/(c*cz))*aw;

    t = Math.pow(C/(Math.sqrt(J/100.0)*Math.pow(1.64 - Math.pow(0.29, n), 0.73)), 10.0/9.0);

    double cacb[] = this.calculate_ab(h, e, t, nbb, a);
    ca = cacb[0];
    cb = cacb[1];

    double rpagpabpa[] = this.Aab_to_rgb(a, ca, cb, nbb);
    rpa = rpagpabpa[0];
    gpa = rpagpabpa[1];
    bpa = rpagpabpa[2];

    rp = this.inverse_nonlinear_adaptation(rpa, f1);
    gp = this.inverse_nonlinear_adaptation(gpa, f1);
    bp = this.inverse_nonlinear_adaptation(bpa, f1);

    double xyz[] = this.hpe_to_xyz(rp, gp, bp);
    x = xyz[0];
    y = xyz[1];
    z = xyz[2];

    double rcgcbc[] = this.xyz_to_cat02(x, y , z);
    rc = rcgcbc[0];
    gc = rcgcbc[1];
    bc = rcgcbc[2];

    r = rc/((yw*d/rw) + (1.0 - d));
    g = gc/((yw*d/gw) + (1.0 - d));
    b = bc/((yw*d/bw) + (1.0 - d));

    double xyz1[] = this.cat02_to_xyz(r, g, b);
    this.X = xyz1[0];
    this.Y = xyz1[1];
    this.Z = xyz1[2];

  }

  private double calculate_F1_from_LA_CIECAM02(double LA){

    double LA5 = LA*5.0;
    double k = 1.0/(LA5 + 1.0);
    //calculating k*k*k*k
    k = k*k;
    k = k*k;
    double F1 = (0.2*k*LA5) + (0.1*(1.0 - k)*(1.0 - k)*Math.pow(LA5, 1.0/3.0));

    return F1;

  }

  private double[] xyz_to_cat02(double x, double y, double z){

    double r = CIECAM02toXYZ.MB00*x + CIECAM02toXYZ.MB01*y + CIECAM02toXYZ.MB02*z;
    double g = CIECAM02toXYZ.MB10*x + CIECAM02toXYZ.MB11*y + CIECAM02toXYZ.MB12*z;
    double b = CIECAM02toXYZ.MB20*x + CIECAM02toXYZ.MB21*y + CIECAM02toXYZ.MB22*z;

    double rgb[] = new double[3];
    rgb[0] = r;
    rgb[1] = g;
    rgb[2] = b;

    return rgb;

  }

  private double[] hpe_to_xyz(double r, double g, double b){

    double xyz[] = new double[3];
    xyz[0] = 1.91020*r - 1.11212*g + 0.20191*b;
    xyz[1] = 0.37095*r + 0.62905*g - 0.00001*b;
    xyz[2] = b;

    return xyz;

  }

  private double[] cat02_to_xyz(double r, double g, double b){

    double xyz[] = new double[3];
    xyz[0] =  1.096124*r - 0.278869*g + 0.182745*b;
    xyz[1] =  0.454369*r + 0.473533*g + 0.072098*b;
    xyz[2] = -0.009628*r - 0.005698*g + 1.015326*b;

    return xyz;

  }

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

  private double[] Aab_to_rgb(double A, double aa, double bb, double Nbb){

    double x = (A/Nbb) + 0.305;

    double rgb[] = new double[3];
    rgb[0] = 0.32787*x + 0.32145*aa + 0.20527*bb;
    rgb[1] = 0.32787*x - 0.63507*aa - 0.18603*bb;
    rgb[2] = 0.32787*x - 0.15681*aa - 4.49038*bb;

    return rgb;

  }

  private double nonlinear_adaptation(double c, double f1){

    double p = Math.pow((f1*c/100.0), 0.42);
    double result = (400.0*p)/(27.13 + p) + 0.1;
    return result;

  }

  private double inverse_nonlinear_adaptation(double c, double f1){

    double result = (100.0/f1)*Math.pow(((27.13*Math.abs(c - 0.1))/(400.0 - Math.abs(c - 0.1))), (1.0/0.42));

    return result;
  }

  private double[] calculate_ab(double h, double e, double t, double nbb, double a){

    double hrad = h*Math.PI/180.0;
    double sinh = Math.sin(hrad);
    double cosh = Math.cos(hrad);
    double x = (a/nbb) + 0.305;

    double aa;
    double bb;
    double ab[] = new double[2];

    if(Math.abs(sinh) >= Math.abs(cosh)){

      bb = ((0.32787*x)*(2.0 + 21.0/20.0))/((e/(t*sinh)) - (0.32145 - 0.63507 - (21.0/20.0)*0.15681)*(cosh/sinh) - (0.20527 - 0.18603 - (21.0/20.0)*4.49038));
      aa = bb*cosh/sinh;
    }

    else{
      aa = ((0.32787*x)*(2.0 + 21.0/20.0))/((e/(t*cosh)) - (0.32145 - 0.63507 - (21.0/20.0)*0.15681 - (0.20527 - 0.18603 - (21.0/20.0)*4.49038)*(sinh/cosh)));
      bb = aa*sinh/cosh;
    }

    ab[0] = aa;
    ab[1] = bb;

    return ab;

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
    CIECAM02toXYZ CIECAM02toXYZ1 = new CIECAM02toXYZ(50.0, 40.0, 170, 95.05, 100.00, 108.88, 20.0, 63.6619, 1.0, 0.69, 1.0);

    System.out.println("X = " + CIECAM02toXYZ1.X);
    System.out.println("Y = " + CIECAM02toXYZ1.Y);
    System.out.println("Z = " + CIECAM02toXYZ1.Z);
  }
}