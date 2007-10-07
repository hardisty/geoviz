package geovista.colorbrewer.coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class SRGB2XYZ {

  //The following matrix is set for D65 white point
  public static final double a00 =  0.412453;  public static final double a01 =  0.35758; public static final double a02 =  0.180423;
  public static final double a10 =  0.212671;  public static final double a11 =  0.71516; public static final double a12 =  0.072169;
  public static final double a20 =  0.019334;  public static final double a21 =  0.119193; public static final double a22 = 0.950227;

  double R255, G255, B255;
  double SR, SG, SB;
  double R709, G709, B709;
  double X, Y, Z;

  public SRGB2XYZ(double R255, double G255, double B255) {

    this.R255 = (double)R255;
    this.G255 = (double)G255;
    this.B255 = (double)B255;

    //step1
    this.SR = this.R255/255.0;
    this.SG = this.G255/255.0;
    this.SB = this.B255/255.0;

    //step2
    if(this.SR <= 0.04045){
      this.R709 = this.SR/12.92;
    }
    else{
      this.R709 = Math.pow((this.SR + 0.055)/1.055, 2.4);
    }

    if(this.SG <= 0.04045){
      this.G709 = this.SG/12.92;
    }
    else{
      this.G709 = Math.pow((this.SG + 0.055)/1.055, 2.4);
    }

    if(this.SB <= 0.04045){
      this.B709 = this.SB/12.92;
    }
    else{
      this.B709 = Math.pow((this.SB + 0.055)/1.055, 2.4);
    }

    //step3
    this.X = (a00*this.R709 + a01*this.G709 + a02*this.B709)*100;
    this.Y = (a10*this.R709 + a11*this.G709 + a12*this.B709)*100;
    this.Z = (a20*this.R709 + a21*this.G709 + a22*this.B709)*100;


  }
  public static void main(String[] args) {
    SRGB2XYZ SRGB2XYZ1 = new SRGB2XYZ(115, 80, 64);

    System.out.println("X = " + SRGB2XYZ1.X);
    System.out.println("Y = " + SRGB2XYZ1.Y);
    System.out.println("Z = " + SRGB2XYZ1.Z);
  }
}