package geovista.matrix.scatterplot;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @Xiping Dai
 * @version 1.0
 */

import java.util.logging.Logger;

import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.EigenvalueDecomposition;

import geovista.common.data.StatisticsVectors;

public class EigenValuesVectors {

  private double[][] data;
  private int numVar;
  private int numObs;

  private DenseDoubleMatrix2D covarianceMatrices;
  private EigenvalueDecomposition eigenDecomposition;
  private double[] eigenValues;
  private double[][] eigenVectors;
  private double[] std;
  private double[][] covariance;
  protected final static Logger logger = Logger.getLogger(EigenValuesVectors.class.getName());
  public EigenValuesVectors() {
  }

  public void setData (double[] dataX, double[] dataY){
    this.numVar = 2;
    this.numObs = dataX.length;
    this.data = new double[this.numObs][this.numVar];
    for (int i = 0; i < this.numObs; i ++){
        this.data[i][0]=dataX[i];
        this.data[i][1]=dataY[i];
    }
    covariance = new double[this.numVar][this.numVar];
    covariance = StatisticsVectors.covarianceMatrx(data);
    this.calculateEigenValues();
  }

  public void setData (double[][] data){
    this.data = data;
    this.numVar = 2;
    covariance = new double[this.numVar][this.numVar];
    covariance = StatisticsVectors.covarianceMatrx(data);
    this.calculateEigenValues();
  }

  private void calculateEigenValues (){

    //double[] mean = new double[this.numVar];
    DenseDoubleMatrix2D eigenVectorsMatrix;
    DenseDoubleMatrix1D eigenValuesMatrix;

    //mean = StatisticsVectors.meanVector(data);

    this.covarianceMatrices = new DenseDoubleMatrix2D(covariance);
    this.eigenDecomposition = new EigenvalueDecomposition(this.covarianceMatrices);
    eigenVectorsMatrix = (DenseDoubleMatrix2D)this.eigenDecomposition.getV();
    eigenValuesMatrix = (DenseDoubleMatrix1D)this.eigenDecomposition.getRealEigenvalues();
    eigenVectors = eigenVectorsMatrix.toArray();
    eigenValues = eigenValuesMatrix.toArray();

    std = new double[this.numVar];
    for (int i = 0; i < this.numVar; i ++){
      std[i] = Math.sqrt(Math.abs(eigenValues[i]));
    }
      logger.finest("stds: " + std[0] + "," + std[1]);
  }

  public double[][] getEigenVectors() {
    return eigenVectors;
  }
  public double[] getEigenValues() {
    return eigenValues;
  }
  public double[][] getData() {
    return data;
  }
  public double[] getStd() {
    return std;
  }
  public void setStd(double[] std) {
    this.std = std;
  }
  public void setEigenVectors(double[][] eigenVectors) {
    this.eigenVectors = eigenVectors;
  }
  public void setEigenValues(double[] eigenValues) {
    this.eigenValues = eigenValues;
  }
  public double[][] getCovariance(){
    return covariance;
  }

  public void setCovariance(double[][] covariance){
    this.covariance = covariance;
    this.numVar = 2;
    this.calculateEigenValues();
  }

}
