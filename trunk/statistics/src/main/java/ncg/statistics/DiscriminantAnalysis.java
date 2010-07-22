package ncg.statistics;
/* 
Implementation of Global Discriminant Analysis
Peter Foley, 19.07.2010
*/

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.math.stat.correlation.Covariance;
import org.apache.commons.math.linear.LUDecomposition;
import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.StatUtils;

public class DiscriminantAnalysis {
	
	// array to hold predictor variables
	private RealMatrix predictorVariables = null;
	
	// array to hold classification variable
	private int[] classification = null;
	
	// array to hold prior probabilities
	private RealVector priorProbabilities = null;
	
	// array to hold unique class labels
	private int[] uniqueClasses = null;

	// array to hold unique class frequencies
	private int[] classFrequencies = null;
	
	// output variables
	private int[] classified = null;
	//private RealVector posteriorProbabilities = null;
	//private RealMatrix paramaters = null;
	private RealMatrix mahalanobisDistance2 = null;
	
	// empty constructor
	public DiscriminantAnalysis(){
	}
	
	// set the predictor variables
	public void setPredictorVariables(double[][] predictorVariables) {
		this.predictorVariables =  
			MatrixUtils.createRealMatrix(predictorVariables);
	}

	// get the predictor variables
	public double[][] getPredictorVariables() {
		return predictorVariables.getData();
	}
	
	// set the classification
	public void setClassification(int[] classification) {
		this.classification = classification;
		
		// compute unique class labels
		setUniqueClasses();

		// compute class frequencies
		setClassFrequencies();
		
	}

	// get the classification
	public int[] getClassification() {
		return classification;
	}

	// set prior probabilities
	public void setPriorProbabilities(double[] priorProbabilities) {
		this.priorProbabilities = 
				new ArrayRealVector(priorProbabilities);
	}

	// get prior probabilites
	public double[] getPriorProbabilities() {
		return priorProbabilities.getData();
	}

	// get classications
	public int[] getClassified() {
		return classified;
	}

	// get posterior probabilties
	//public double[] getPosteriorProbabilites() {
	//	return posteriorProbabilities.getData();
	//}

	// get paramaters
	//public double[][] getParamaters() {
	//	return paramaters.getData();
	//}

	// get mahalanobis distance squared
	public double[][] getMahalanobisDistance2() {
		return mahalanobisDistance2.getData();
	}

	
	//compute unique classes labels
	private void setUniqueClasses() {
		
		if (classification != null) {
			// compute unique classes in the classification array
			// using a set
			Set<Integer> classes = new HashSet<Integer>();
			for (int i = 0; i < classification.length; i++ ) {
				classes.add(classification[i]);
			}
		
			// convert the classes set to an array of ints
			uniqueClasses = new int[classes.size()];
			Iterator<Integer> classesIt = classes.iterator();
		
			int i = 0;
			while( classesIt.hasNext() ) {
				uniqueClasses[i++] = 
					((Integer)classesIt.next()).intValue();
			}
		}
	}
	
	// compute class frequencies
	private void setClassFrequencies() {
		
		if (classification != null && uniqueClasses != null) {
			
			// compute the frequency of each class label
			Frequency classFrequency = new Frequency();
			for (int i = 0; i < classification.length; i++ ) {
				classFrequency.addValue(classification[i]);
			}

			// save the class frequencies to an int array	
			classFrequencies = new int[uniqueClasses.length];
			for(int i = 0; i < uniqueClasses.length; i++) {
				classFrequencies[i] = 
					(int)classFrequency.getCount(uniqueClasses[i]);
			}
		}
	}

	// get unique class labels
	public int[] getUniqueClasses() {
		return uniqueClasses;
	}
	// get class frequencies
	public int[] getClassFrequencies() {
		return classFrequencies;
	}

	// return integer array of row indices for the classIndex th class
	// classIndex is the index of the classIndex th class in the 
	// integer array returned by uniqueClasses
	private int[] computeClassIndices(int classIndex){
		
		// array to hold indices for class with index 'classIndex'
		int[] classRowIndices = new int[classFrequencies[classIndex]];

		// identify indices for class with index 'classIndex'
		int j = 0;
		for(int i = 0; i < classification.length; i++) {
			
			if ( classification[i] == uniqueClasses[classIndex] ) {
				classRowIndices[j++] = i;
			}
		}

		return classRowIndices;
	}	
	
	// return integer array of column (field) indices
	private int[] computeFieldIndices() {
		
		int[] colIndices = new int[predictorVariables.getColumnDimension()];
		
		for(int j = 0; j < predictorVariables.getColumnDimension(); j++) {
			colIndices[j] = j; 
		}

		return colIndices;
	}
	
	// compute group means for class with index classIndex in the
	// array uniqueClasses
	private RealVector getClassMean(int classIndex) {
		
		// compute field indices
		int[] fieldIndices = computeFieldIndices();

		// compute row indices for class 'classIndex'
		int[] classRowIndices = computeClassIndices(classIndex);
		
		// compute sub matrix for class with index 'classIndex'
		RealMatrix predC = 
			predictorVariables.getSubMatrix(classRowIndices,fieldIndices);

		// assign memory for group means
		double[] groupMeans = new double[predC.getColumnDimension()];
			
		for ( int j = 0; j < predC.getColumnDimension(); j++ ) {

				groupMeans[j] = StatUtils.mean(predC.getColumn(j));
		}

		return new ArrayRealVector(groupMeans);
		
				
	}

	// compute covariance matrix for the class with index classIndex
	// in the array uniqueClases
	private RealMatrix getCovarianceMatrix(int classIndex) {
		
		// compute field indices
		int[] fieldIndices = computeFieldIndices();

		// compute row indices for class i
		int[] classRowIndices = computeClassIndices(classIndex);
		
		// compute sub matrix for current group
		RealMatrix predC = 
			predictorVariables.getSubMatrix(classRowIndices,fieldIndices);
		
		// compute covariance matrix for class C
		Covariance c = new Covariance(predC);
		RealMatrix covMatrix = c.getCovarianceMatrix();
			
		return covMatrix;
	}

	// compute the pooled class covariance matrix
	private RealMatrix getPooledCovMatrix() {
		
		// get number of fields
		int numFields = predictorVariables.getColumnDimension();

		// assign memory for pooled sum of squares matrix
		// java arrays values are automatically set to zero
		RealMatrix pooledSumSquares = 
			MatrixUtils.createRealMatrix(numFields,numFields);

		// get indices for each class in turn and compute the 
		// total sum of squares (in order to compute the pooled
		// class covariance matrix
		for( int classIndex = 0; classIndex < uniqueClasses.length; 
														classIndex++ ) {
			
			// compute covariance matrix for the class with index
			// classIndex in the array returned by getClasses()
			RealMatrix covMatrix = getCovarianceMatrix(classIndex);
			
			// compute the degrees of freedom for the class with index
			// classIndex in the array returned by getClasses()
			double degreesFreedom = 
							(double)(classFrequencies[classIndex] -1);
			
			// compute sum of squares for the class with index
			// classIndex in the array returned by getClasses()
			RealMatrix sumSquares = 
							covMatrix.scalarMultiply(degreesFreedom);
			
			// add the sum of squares for the class with index
			// classIndex in the array returned by getClasses()
			// to the pooledSumSquares matrix
			pooledSumSquares = pooledSumSquares.add(sumSquares);
		}
		
		// compute the covariance matrix (maximum likelihood version)
		RealMatrix pooledCovMatrix = 
						pooledSumSquares.scalarMultiply(
						1.0 / predictorVariables.getRowDimension());

		return pooledCovMatrix;
	}
	
	// return the index of the minimum element in the items array
	private int getMin(double[] items) {
		
		int minIndex = -1;

		if ( items.length > 0) {
			
			minIndex = 0;

			for (int i = 1; i < items.length; i++) {
				
				if (items[i] < items[minIndex] ) {
					minIndex = i;
				}
			}
		}

		return minIndex;
	}

	// compute confusion matrix
	public int[][] confusionMatrix() {
		
		int[][] cMatrix = null;

		if (classified != null && classification != null ) {

			// number of objects classified
			int numObjects = classified.length;
			
			// create the confusion matrix and set the 
			// values are set to zero initially by default in java
			cMatrix = new int[uniqueClasses.length][uniqueClasses.length];

			// compute confusion matrix
			for ( int c = 0; c < numObjects; c++) {
				
				for ( int i = 0; i < uniqueClasses.length; i++) {
					for ( int j = 0; j < uniqueClasses.length; j++) {
						if ( classification[c] == uniqueClasses[i]) {		
							// object in row c belongs to uniqueClasses[i]
							if ( classified[c] == uniqueClasses[j]) {
								// object in row c assigned to uniqueClasses[j]
								cMatrix[i][j]++;
							}
						}
	
					}
				}
			}
		}

		return cMatrix;
	}
	
	// do the classification
	public void classify() {
		
		int numObjects = predictorVariables.getRowDimension();
		int numClassObjects = classification.length;

		if (numClassObjects == numObjects) {
			
			// create pooled group covariance matrix
			// (maximum likelihood version)
			RealMatrix pooledCovMatrix = getPooledCovMatrix();

			// compute inverse of pooled group covariance matrix
			LUDecomposition inv = new LUDecompositionImpl(pooledCovMatrix);
			RealMatrix pooledCovMatrixInv = inv.getSolver().getInverse();

			// assign memory for mahalanobis distance squared
			mahalanobisDistance2 = 
			MatrixUtils.createRealMatrix(uniqueClasses.length,numObjects);

			// compute mahalanobis distance squared for each class
			for( int c = 0; c < uniqueClasses.length; c++ ) {
				
				// compute class means for the cth class
				RealVector classMeans = getClassMean(c);

				// compute mahalanobis distance squared separately for 
				// each object in the dataset
				for (int i = 0; i < numObjects; i++) {

					RealVector meanDiff = 
					predictorVariables.getRowVector(i).subtract(classMeans);
				
					double mh2 = pooledCovMatrixInv.preMultiply(meanDiff).dotProduct(meanDiff);
					mahalanobisDistance2.setEntry(c,i, mh2);
				}

			}

			// assign memory for classified array
			classified = new int[numObjects];

			// now classify the data by assigning each object to the
			// nearest class using the mahalanobis distance squared 
			// metric
			for (int i = 0; i < numObjects; i++) {
				double[] mh2Classes = mahalanobisDistance2.getColumn(i);
				int classIndex = getMin(mh2Classes);
				classified[i] = uniqueClasses[classIndex];
			}
			
		} else {
			System.out.println("Mismatch");
		}
	}
}
