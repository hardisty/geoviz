package edu.psu.geovista.common.classification;

/**
 * <p>Title: Studio applications</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GeoVSITA Center</p>
 * @author Xiping Dai
 * @version 1.0
 */

import java.util.Vector;
import java.util.logging.Logger;

import edu.psu.geovista.data.StatisticsVectors;

public class MultiGaussian
{
	private double[][] classTrainingData;
	private double[] meanVector;
	private double[][] covarianceMatrix;
	private double[][] inverseCovMatrix;
	private double determinant;
	private double sqrtDeterminant;
	private int n;
	private int len;//observation numbers
	private boolean isUnbias = true;
	protected final static Logger logger = Logger.getLogger(MultiGaussian.class.getName());
	public MultiGaussian(){
	}

	public MultiGaussian(double[][] classTrainingData){
		this.classTrainingData = classTrainingData;
		this.n = this.classTrainingData[0].length;
		getMultiGaussianDistribution();
	}

	public void setTrainingData(double[][] classTrainingData){
		this.classTrainingData = classTrainingData;
		this.n = this.classTrainingData[0].length;
		getMultiGaussianDistribution();
	}

	public void setTrainingData(Vector classTrainingDataVector){
		this.n = ((double[])classTrainingDataVector.get(0)).length;
		this.classTrainingData = new double[classTrainingDataVector.size()][n];
		for (int i = 0; i<classTrainingDataVector.size(); i ++){
			this.classTrainingData[i] = (double[])classTrainingDataVector.get(i);
		}
		getMultiGaussianDistribution();
	}

	public double[][] getTrainingData (){
		return this.classTrainingData;
	}

	public void setMeanVector(double[] meanVector){
		this.meanVector = meanVector;
	}

	public double[] getMeanVector(){
		return this.meanVector;
	}

	public void setCovarianceMatrix(double[][] covarianceM){
		this.covarianceMatrix = covarianceM;
	}

	public double[][] getCovarianceMatrix(){
		return this.covarianceMatrix;
	}

	public void setIsUnbias(boolean isUnbias){
		this.isUnbias = isUnbias;
	}

	public boolean getIsUnbias(){
		return this.isUnbias;
	}

	private void getMultiGaussianDistribution() {
		//p(X|wi)~N(Ui, Ei) for one class
		len = this.classTrainingData.length; //observation numbers
		int covDivident;
		if (this.isUnbias == true){
			covDivident = len -1;
		}else{
			covDivident = len;
		}

		this.meanVector = new double[n]; //n is the number of variables
		this.covarianceMatrix = new double[n][n];
		//mean
		for (int j = 0; j < len; j ++){
			this.meanVector = StatisticsVectors.plus(this.meanVector,
			this.classTrainingData[j]);
		}
		this.meanVector = StatisticsVectors.divide(this.meanVector, len);
		//covariance matrix
		for (int i = 0; i < n; i ++){
			for (int j = 0; j < n; j ++){
				this.covarianceMatrix[i][j] = 0;
				for (int l = 0; l < len; l ++){
					this.covarianceMatrix[i][j] += (this.classTrainingData[l][i] - this.meanVector[i])*
				 (this.classTrainingData[l][j] - this.meanVector[j]);
				}
				this.covarianceMatrix[i][j] /= covDivident;
				//System.out.print(covarianceMatrix[i][j]);
				//System.out.print(" ");
			}
		
		}
		try {
		this.inverseCovMatrix = StatisticsVectors.Inverse(this.covarianceMatrix);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		this.determinant = StatisticsVectors.Determinant(this.covarianceMatrix);
		this.sqrtDeterminant = Math.sqrt(this.determinant);
	}

	public double getPDF (double[] X){
		//p(X|wi) = 1 / ((2PI)power(n/2) * determinant(Ei) power(1/2) * e power(-1/2 * (X-Ui)power(T) * Ei power(-1) * (X - Ui))))
		double pdf = 0;
		int n = X.length;
		double[] XMinusMean = new double[n];
		XMinusMean = StatisticsVectors.minus(X, this.meanVector);
		double expValue = 0;
		double[] expVectorTmp = new double[n];

		try {
			expVectorTmp = StatisticsVectors.MultiplyMatrix(XMinusMean, this.inverseCovMatrix);
			expValue = - StatisticsVectors.multiply(expVectorTmp, XMinusMean) / 2;
		} catch (Exception ex){
			ex.printStackTrace();
		}


		pdf = 1/(Math.pow(2*Math.PI, (double)n/2) * this.sqrtDeterminant) * Math.exp(expValue);

		return pdf;
	  }

}