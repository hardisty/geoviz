package geovista.colorbrewer.coloreffect;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * 
 */

public class Seqseqnongray extends SchemeObject{

  LABcolor[][] labcolor;

  int vclass;
  int hclass;
  int maxlightness;
  int minlightness;
  int alpha;
  int beta;
  int dx;
  int dy;
  int startinghue;

  public Seqseqnongray(int vclass, int hclass, int maxlightness, int minlightness, int alpha, int beta, int dx, int dy, int startinghue){

    //initializing the object
    this.vclass = vclass;
    this.hclass = hclass;
    this.maxlightness = maxlightness;
    this.minlightness = minlightness;
    this.alpha = alpha;
    this.beta = beta;
    this.dx = dx;
    this.dy = dy;
    this.startinghue = startinghue;

    //creating the labcolor double array to store the values in LAB format
    this.labcolor = new LABcolor[vclass][hclass];

    //declaring the variable to use in calculating the colors
    double L;
    double a = 0;
    double b = 0;
    double x = (maxlightness - minlightness)/(4*Math.cos(Math.toRadians(alpha))*hclass*Math.sin(Math.toRadians(beta)));
    double y = (maxlightness - minlightness)/(4*Math.cos(Math.toRadians(alpha))*vclass*Math.sin(Math.toRadians(beta)));


    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){
        //calculating the a & b according to the matrix

        L = maxlightness - (1 + 2*i)*x*Math.cos(Math.toRadians(alpha)) - (1 + 2*j)*y*Math.cos(Math.toRadians(alpha));

        a = Math.cos(Math.toRadians(startinghue))*Math.sin(Math.toRadians(alpha))*((1 + 2*i)*x - (1 + 2*j)*y) + (Math.cos(Math.toRadians(startinghue))*(maxlightness - minlightness))/(Math.tan(Math.toRadians(beta)));

        b = Math.sin(Math.toRadians(startinghue))*Math.sin(Math.toRadians(alpha))*((1 + 2*i)*x - (1 + 2*j)*y) + (Math.sin(Math.toRadians(startinghue))*(maxlightness - minlightness))/(Math.tan(Math.toRadians(beta)));

        //a = Math.cos(Math.toRadians(startingcolor))*(i - j)*(maxlightness - minlightness)*Math.tan(Math.toRadians(alpha))/(2*vclass);

        //b = Math.sin(Math.toRadians(startingcolor))*(i - j)*(maxlightness - minlightness)*Math.tan(Math.toRadians(alpha))/(2*hclass);

        //a = 0;
        //b = 0;

        //assigning the lightness according to the matrix index
        //L = lightness[(i + j)];

        //adding the deviations
        a = a + dx*Math.cos(Math.toRadians(startinghue));
        b = b + dx*Math.sin(Math.toRadians(startinghue));
        L = L + dy;

        //creating colors
        LABcolor c = new LABcolor(L, a, b);
        this.labcolor[i][j] = c;
      }
    }
  }
}