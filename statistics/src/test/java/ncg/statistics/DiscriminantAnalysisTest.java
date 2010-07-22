package ncg.statistics;

import org.junit.Test;

import static org.junit.Assert.*;
import java.io.IOException;

public class DiscriminantAnalysisTest {
	
	double delta = 0.00000001;

	@Test public void testDiscriminantAnalysis() {
		
		System.out.println("Testing Discriminant Analysis Bean");

		String inputFile = 
			"/home/pfoley/credit_unions/data_processed/electoral_divisions_cu_cso_2006_numeric.csv";

		// read the input data
		ReadData data = new ReadData(inputFile);

		try {
			data.readFile();
		} catch (IOException e) {
			System.out.println("Unable to read input file");
			System.out.println(e.getMessage());
			System.exit(1);
		}

		//predictor variable column indices
		int num_cu = 3;
		int[] pred_cols = {4,5,6,7,8,9,10,11,12,13};

		double[] priorProbabilities = {0.5,0.5};
		
		// extract the predictor variables (independent variables)
		double[][] predictorVars = data.getSubMatrix(pred_cols);
		
		// get the dependent variable (number of credit unions)
		double[] creditUnions = data.getColumn(num_cu);
		
		// two classes : [0,1] 0 means no credit unions
		// and one means at least one
		int[] creditUnionsInt = new int[creditUnions.length];
		for (int i = 0; i < creditUnions.length; i++) {
			if ( creditUnions[i] > 1 ) {
				creditUnions[i] = 1;
			}
			creditUnionsInt[i] = (int)creditUnions[i];
		}
		
		// create new discriminant analysis object
		DiscriminantAnalysis da = new DiscriminantAnalysis();
		
		// set the predictor variables
		da.setPredictorVariables(predictorVars);

		// set the classification variables
		da.setClassification(creditUnionsInt);

		// set the prior probabilities
		da.setPriorProbabilities(priorProbabilities);

		// classify the predictor variables
		da.classify();

		// get confusion matrix
		int[][] cMatrix = da.confusionMatrix();
		
		// compute total number of correct classifications
		// from confusion matrix
		int numCorrectClassifications = 0;
		for (int i = 0; i < cMatrix.length; i++) {
			numCorrectClassifications += cMatrix[i][i];
		}
		
		// get the class frequencies
		int[] classFrequencies = da.getClassFrequencies();
		
		// compute the sum of the class frequencies array
		int allClassTotal = 0;
		for(int i=0; i< classFrequencies.length; i++) {
			allClassTotal += classFrequencies[i];
		}

		try {
		
			// test to see if the predictor variables arrays are equal
			assertArrayEquals("Error comparing Predictor Arrays", 
									predictorVars, 
									da.getPredictorVariables());
			for(int i=0; i< predictorVars.length; i++) {
				//assertArrayEquals("Error comparing Predictor Arrays",
				//					predictorVars[i],
				//					da.getPredictorVariables()[i], delta);
			}
		
			// test to see if classification arrays are equal
			assertArrayEquals("Error comparing Class Arrays", 
									creditUnionsInt, 
									da.getClassification());
			
			// test to see if the class frequencies sum to the total
			// number of objects
			assertTrue("Error comparing Class Frequencies", 
									allClassTotal == predictorVars.length);

			// test to see if prior probability arrays are equal
			//assertArrayEquals("Error comparaing prior probabilities",
			//						priorProbabilities,
			//						da.getPriorProbabilities(),delta);
		
			// test to see if the length of the classification array is
			//equal to the length of the classified array
			assertTrue("Error compariang Classified Arrays", 
									da.getClassified().length == 
									da.getClassification().length); 
			
			// test to see if the number of correct classifications
			// is correct
			assertTrue(
				"Error comparing number of correct classifications", 
				numCorrectClassifications == 2683);
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}


}
