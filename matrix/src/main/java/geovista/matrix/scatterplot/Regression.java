package geovista.matrix.scatterplot;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface Regression {

  public String getFullName();
  public String getShortName();

  public double[] getRegression (double[] dataX, double[] dataY);

  public double getRSquare(double[] dataX, double[] dataY);

}
