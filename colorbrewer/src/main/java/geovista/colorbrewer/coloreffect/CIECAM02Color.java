package geovista.colorbrewer.coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class CIECAM02Color {

  double J;
  double C;
  double h;
  double a;
  double b;

  public CIECAM02Color() {

  }

  //to be consistent with the geometric models, only cylindrical representation (J, a, b)
  //of CIECAM02 color space is adopted, and the polar representation (J, C, h) is derived from (J, a, b)
  public CIECAM02Color(double J, double a, double b) {
    this.J = J;
    this.a = a;
    this.b = b;

    this.C = Math.sqrt(a*a + b*b);

    if(a > 0){
      if(b > 0){
        this.h = Math.toDegrees(Math.atan(Math.abs(b)/Math.abs(a)));
      }
      if(b < 0){
        this.h = 360 - Math.toDegrees(Math.atan(Math.abs(b)/Math.abs(a)));
      }
      if(b == 0){
        this.h = 0;
      }
    }

    if(a < 0){
      if(b > 0){
        this.h = 180 - Math.toDegrees(Math.atan(Math.abs(b)/Math.abs(a)));
      }
      if(b < 0){
        this.h = 180 + Math.toDegrees(Math.atan(Math.abs(b)/Math.abs(a)));
      }
      if(b == 0){
        this.h = 180;
      }
    }

    if(a == 0){
      if(b > 0){
        this.h = 90;
      }
      if(b < 0){
        this.h = 270;
      }
      if(b == 0){
        this.h = 0;
      }
    }

/*
    if(a != 0){
      if(b > 0){
        this.h = Math.toDegrees(Math.atan(Math.abs(b)/Math.abs(a))) + 180;
      }
      if(b < 0){
        this.h = Math.toDegrees(Math.atan(Math.abs(b)/Math.abs(a)));
      }

    }

    if(a == 0){
      if(b > 0){
        this.h = 90;
      }
      if(b < 0){
        this.h = 270;
      }
      if(b == 0){
        this.h = 0;
      }
    }

    */

  }

}



