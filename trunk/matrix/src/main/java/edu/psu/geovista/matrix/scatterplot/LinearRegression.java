package edu.psu.geovista.matrix.scatterplot;

import java.util.logging.Logger;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class LinearRegression implements Regression{

  private static final String shortName = "Linear Regression";
  private static final String fullName = "Linear Regression";
  double[] moranElement;
  protected final static Logger logger = Logger.getLogger(LinearRegression.class.getName());
  public LinearRegression() {
  }

  public String getShortName(){
    return LinearRegression.shortName;
  }

  public String getFullName(){
    return LinearRegression.fullName;
  }

  public double[] getRegression(double[] dataX, double[] dataY) {
      double[] moranElement = new double[2];
      double sumX, sumY, sumX2, sumXY, slope, intercept;
      int sampleSize = 0;
      sumX = sumY = sumX2 = sumXY = slope = intercept = 0;

      moranElement[0] = 0;
      moranElement[1] = 0;

      for(int i = 0; i < dataX.length; i++) {
                sumX = sumX + dataX[i];
                sumY = sumY + dataY[i];
                sumX2 = sumX2 + (dataX[i] * dataX[i]);
                sumXY = sumXY + (dataX[i] * dataY[i]);
          sampleSize++;
      }

      double denominator = 0;
      if (sampleSize > 0 ) {
          denominator = sumX2 - (sumX*sumX)/sampleSize;
      }

      if (Math.abs(denominator) > 1.0e-12) {
          slope = (sumXY - sumX * sumY / sampleSize) / denominator;
          intercept = (sumY - slope * sumX)/sampleSize;
      }

      moranElement[0] = slope;
      moranElement[1] = intercept;

      logger.finest("Slope is " + slope +"  Intercept is "+ intercept);

      return moranElement;
  }

  public double getRSquare(double[] dataX, double[] dataY){
    //the goodness of fit
    double rSqr=0;
    double[] regressionElements;
    double sumY = 0;
    double meanY;
    double SYY = 0;
    double RSS = 0;
    regressionElements = this.getRegression(dataX, dataY);
    for(int i = 0; i < dataY.length; i++) {
      sumY = sumY + dataY[i];
    }
    meanY = sumY / (double) dataY.length;
    for(int i = 0; i < dataY.length; i++) {
      SYY = SYY + Math.pow(dataY[i]-meanY, 2);
      RSS = RSS + Math.pow(dataY[i]-(regressionElements[0]*dataX[i]+regressionElements[1]), 2);
    }
    if (SYY != 0){
      rSqr = 1 - RSS / SYY;
    }else rSqr = Double.NaN;
    return rSqr;
  }

}
