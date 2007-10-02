package edu.psu.geovista.common.classification;

/**
 * <p>Title: ClusterMixtureModels</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GeoVISTA</p>
 * @author Xiping Dai
 * @version 1.0
 */

import java.util.Vector;

import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.common.data.StatisticsVectors;

public class ClusterMixtureModels {

	private static double THRESHOLD = 0.001;
	private Object[] dataObject;
	private String[] attributesDisplay;
	private double[][] dataArray;
	private int[] selectedAttIdx;
	private ClassifierKMeans kMeans;
	private int[] kMeansClusters;
	private Vector[] dataInClasses;
	private int numClusters;
	private double[] priors;
	private MultiGaussian[] multiGaussian;
	private Vector clusterMeanVector;
	private Vector clusterCovVector;
	private double[][] posterior;
	private int[] clusterResults;
	//	private ColorSymbolizerForClassification clusterToColors;


	  /**
	   * @param data
	   * 
	   * This method is deprecated becuase it wants to create its very own pet
	   * DataSetForApps. This is no longer allowed, to allow for a mutable, 
	   * common data set. Use of this method may lead to unexpected
	   * program behavoir. 
	   * Please use setDataSet instead.
	   */
	  @Deprecated
	  public void setDataObject(Object[] data) {
		 this.setDataSet(new DataSetForApps(data));
	    
	  }
	public void setDataSet(DataSetForApps data)
	{
		// remove string data
		DataSetForApps dataObjTransfer = data;
		this.dataObject = dataObjTransfer.getDataSetNumericAndSpatial();//XXX
		this.attributesDisplay = dataObjTransfer.getAttributeNamesNumeric();
		dataArray = new double[dataObjTransfer.getNumObservations()][attributesDisplay.length];
		// transfer data array to double array
		for (int j=0;j<attributesDisplay.length;j++)
		{
			int t = 0;
			if (dataObject[j+1] instanceof double[]) t=0;
			else if (dataObject[j+1] instanceof int[]) t=1;
			else if (dataObject[j+1] instanceof boolean[]) t=2;
			for (int i=0;i<dataArray.length;i++)
			{
				switch (t)
				{
					case 0 :
						dataArray[i][j]=((double[])dataObject[j+1])[i];
						break;
					case 1 :
						dataArray[i][j]=(double)((int[])dataObject[j+1])[i];
						break;
					case 2 :
						dataArray[i][j]=((boolean[])dataObject[j+1])[i]?1.0:0.0;
						break;
				}
			}
		}
		if (this.selectedAttIdx == null){
			this.selectedAttIdx = new int[this.attributesDisplay.length];
			for (int i = 0; i < this.attributesDisplay.length; i ++){
				this.selectedAttIdx[i] = i;
			}
		}
		init();
	}

	public void setNumberOfCluster(int num){
		this.numClusters = num;
	}

	public int[] getClusterResults(){
		return this.clusterResults;
	}

/*	public Color[] getClusterColors()
	{
		clusterToColors = new ColorSymbolizerForClassification();
		clusterToColors.setClassification(this.clusterResults);
		clusterToColors.setNumClasses(this.numClusters);
		this.clusterColors = clusterToColors.getSymbolize();
		return this.clusterColors;
	}*/

	private void init(){
		this.clusterResults = new int[this.dataArray.length];
		kMeans = new ClassifierKMeans();
		kMeans.setClusterNumber(this.numClusters);
		kMeans.setAttributesDisplay(this.attributesDisplay);
		kMeans.setDataArray(this.dataArray);
		this.kMeansClusters = kMeans.getKMeansClusters();
		this.dataInClasses = new Vector[this.numClusters];
		this.priors = new double[this.numClusters];
		this.multiGaussian = new MultiGaussian[this.numClusters];
		this.clusterMeanVector = new Vector();
		this.clusterCovVector = new Vector();
		this.clusterCovVector.setSize(this.numClusters);
		this.posterior = new double[this.dataArray.length][this.numClusters];

		for(int i = 0; i < this.numClusters; i ++){
			this.dataInClasses[i] = new Vector();
		}
		//divide data vectors into individule classes.
		for (int i = 0; i < this.dataArray.length; i ++){
			for(int j = 0; j < this.numClusters; j ++){
				if (this.kMeansClusters[i] == j){
					this.dataInClasses[j].add(this.dataArray[i]);
				}
			}
		}
		//set up prior probability ak
		for(int i = 0; i < this.numClusters; i ++){
			this.dataInClasses[i].trimToSize();
			this.priors[i] = (double)this.dataInClasses[i].size() / (double)this.dataArray.length;
		}
		for (int i = 0; i< this.numClusters; i ++){
			multiGaussian[i] = new MultiGaussian();
			multiGaussian[i].setTrainingData(this.dataInClasses[i]);
		}

		double sumPosterior;
		double likelihoodOld = 0;
		double likelihood = 0;
		double stop=1000;
		double[] pdfForOneObvs = new double[this.numClusters];

		//E-step: compute the posterior probabilities for all i = 1, ..., n, k = 1, ...,K
		for (int i = 0; i < this.dataArray.length; i ++){
			sumPosterior = 0.0;
			for(int k = 0; k < this.numClusters; k ++){
				pdfForOneObvs[k] = this.multiGaussian[k].getPDF(this.dataArray[i]);
				sumPosterior += this.priors[k] * pdfForOneObvs[k];
			}
			for(int k = 0; k < this.numClusters; k ++){
				this.posterior[i][k] = this.priors[k] * pdfForOneObvs[k] / sumPosterior;
			}
			likelihoodOld += Math.log(sumPosterior);
		}

		while(stop > ClusterMixtureModels.THRESHOLD){
			//M-step
			double sumPik;
			for(int k = 0; k < this.numClusters; k ++){
				double[] mean = new double[this.dataArray[0].length];
				double[][] covMatrix = new double[this.dataArray[0].length][this.dataArray[0].length];
				sumPik = 0;
				for (int i = 0; i < this.dataArray.length; i ++){
					sumPik += this.posterior[i][k];
					mean = StatisticsVectors.plus(mean, StatisticsVectors.multiply(this.posterior[i][k], this.dataArray[i]));
				}
				//ak(P=1)
				this.priors[k] = sumPik / this.dataArray.length;
				mean = StatisticsVectors.divide(mean, sumPik);
				//uk(p+1)
				this.clusterMeanVector.add(k, mean.clone());
				//Ek(p+1)
				double[] xminusmean = new double[mean.length];
				for (int i = 0; i < this.dataArray.length; i ++){
					xminusmean = StatisticsVectors.minus(dataArray[i], mean);
					try{
						covMatrix = StatisticsVectors.plus(covMatrix, StatisticsVectors.multiply(posterior[i][k],
								StatisticsVectors.MultiplyMatrix(xminusmean, xminusmean)));
					} catch (Exception ex){
						ex.printStackTrace();
					}
				}
				covMatrix = StatisticsVectors.divide(covMatrix, sumPik);
				this.clusterCovVector.add(k, covMatrix.clone());
			}
			this.clusterCovVector.trimToSize();
			//objective function
			for (int i = 0; i< this.numClusters; i ++){
				multiGaussian[i] = new MultiGaussian();
				multiGaussian[i].setMeanVector((double[])this.clusterMeanVector.get(i));
				multiGaussian[i].setCovarianceMatrix((double[][])this.clusterCovVector.get(i));
			}

			for(int n = 0; n < this.dataArray.length; n ++){
				sumPosterior = 0.0;
				for(int k = 0; k < this.numClusters; k ++){
					pdfForOneObvs[k] = this.multiGaussian[k].getPDF(this.dataArray[n]);
					sumPosterior += this.priors[k] * pdfForOneObvs[k];
				}
				likelihood += Math.log(sumPosterior);
				for(int k = 0; k < this.numClusters; k ++){
					this.posterior[n][k] = this.priors[k] * pdfForOneObvs[k] / sumPosterior;
				}
			}
			stop = Math.abs((likelihood-likelihoodOld)/likelihood);
			likelihoodOld = likelihood;
		}

		//classify based on posterior
		int tmpClass = 0;
		double[] pdfs = new double[this.numClusters];
		for (int i = 0; i < this.dataArray.length; i ++){
			for (int j = 0; j < this.numClusters; j ++){
				pdfs[j] = this.posterior[i][j];
			}
			//find the biggest pdf using density function of each class
			tmpClass = 0;
			for (int j = 1; j < this.numClusters; j ++){
				if (pdfs[j] > pdfs[tmpClass]){
					tmpClass = j;
				}
			}
			//assign the class information to each observation.
			//this.classes[i] = tmpClass+1;//class 1-5, especially for Kioloa data.
			this.clusterResults[i] = tmpClass;
		}
	}

}