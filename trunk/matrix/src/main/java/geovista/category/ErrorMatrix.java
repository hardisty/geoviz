package geovista.category;

/**
 * <p>Title: Error Matrix</p>
 * <p>Description: Error Matrix for Classification</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GeoVSITA Center</p>
 * @author Xiping Dai
 * @version 1.0
 */

import java.util.Vector;

public class ErrorMatrix
{

	private int[] referenceClasses;//training data, should be Object[] or general ones.
	private int[] resultingClasses;
	private int numberOfRecords;
	private int classNumber;
	private int[] categoryIdNumbers;
	private int[][] errorMatrix;
	private int[] userTotal;
	private int[] producerTotal;
	private double[] userAccuracy;
	private double[] producerAccuracy;
	private double[] errorsOfOmission;
	private double[] errorsOfCommission;
	private double kappa;
        private int totalCorrect;

    public ErrorMatrix()
    {
    }
    public int[] getReferenceClasses()
    {
        return referenceClasses;
    }

    public void setReferenceClasses(int[] referenceClasses)
	{
		this.referenceClasses = referenceClasses;
	}

    public int[] getResultingClasses()
    {
        return resultingClasses;
    }

    public void setResultingClasses(int[] resultingClasses)
    {
        this.resultingClasses = resultingClasses;
        this.numberOfRecords = this.resultingClasses.length;
    }

    public int getClassNumber()
    {
        return classNumber;
    }

    public void setClassNumber(int classNumber)
    {
        this.classNumber = classNumber;
    }

    public int[] getCategoryIdNumbers()
    {
        return categoryIdNumbers;
    }

    public void setCategoryIdNumbers(int[] categoryIdNumbers)
    {
        this.categoryIdNumbers = categoryIdNumbers;
        this.classNumber = this.categoryIdNumbers.length;
    }

	private void calculateErrorMatrix(){
		this.errorMatrix = new int[this.classNumber][this.classNumber];
		this.userTotal = new int[this.classNumber];
		this.producerTotal = new int[this.classNumber];
		for(int i = 0; i < this.numberOfRecords; i ++){

			for(int cl = 0; cl < this.classNumber; cl ++){
				if (this.categoryIdNumbers[cl] == this.referenceClasses[i]){
					for(int resultCl = 0; resultCl < this.classNumber; resultCl ++){
						if (this.categoryIdNumbers[resultCl] == this.resultingClasses[i]+1){//+1 for kioloa dataset, 1,..9
							this.errorMatrix[cl][resultCl] ++;
							continue;
						}
					}
					continue;
				}
			}
		}

		for(int cl = 0; cl < this.classNumber; cl ++){
			for(int i = 0; i < this.classNumber; i ++){
				this.userTotal[cl] += this.errorMatrix[i][cl];
				this.producerTotal[cl] += this.errorMatrix[cl][i];
			}
		}
	}

	private void calculateAccuracy(){
          this.userAccuracy = new double[this.classNumber];
          this.producerAccuracy = new double[this.classNumber];
          this.errorsOfCommission = new double[this.classNumber];
          this.errorsOfOmission = new double[this.classNumber];
		for(int cl = 0; cl < this.classNumber; cl ++){
			this.userAccuracy[cl] = (double)this.errorMatrix[cl][cl]/(double)this.userTotal[cl];
			this.producerAccuracy[cl] = (double)this.errorMatrix[cl][cl]/(double)this.producerTotal[cl];
			this.errorsOfOmission[cl] = 1 - this.producerAccuracy[cl];
			this.errorsOfCommission[cl] = (double)(this.userTotal[cl] - this.errorMatrix[cl][cl])/(double)this.producerTotal[cl];
		}
	}

	private void kappaMeasurement(){
		//k (hat) = (observed - expected)/(1 - expected)
		int sumOfDiagonal = 0;
		int totalOfSamples = 0;
		double observed = 0.0;
		for(int cl = 0; cl < this.classNumber; cl ++){
			sumOfDiagonal += this.errorMatrix[cl][cl];
			totalOfSamples += this.producerTotal[cl];
		}
		observed = (double)sumOfDiagonal / (double)totalOfSamples;
                this.totalCorrect = sumOfDiagonal;

		//products of row and column marginals
		int[][] productsOfMarginals = new int[this.classNumber][this.classNumber];
		int grandTotal = 0;
		int sumOfDiagonalProducts = 0;
		for(int r = 0; r < this.classNumber; r ++){
			for(int c = 0; c < this.classNumber; c ++){
				productsOfMarginals[r][c] = this.producerTotal[r]*this.userTotal[c];
				grandTotal += productsOfMarginals[r][c];
			}
			sumOfDiagonalProducts += productsOfMarginals[r][r];
		}

		//expected agreement by chance = sum of diagonal entries / grand total
		double expected = 0.0;
		expected = (double)sumOfDiagonalProducts / (double)grandTotal;

		this.kappa = (observed - expected) / (1 - expected);
	}

        public Vector updatematrix(int[] referenceClasses, int[] resultingClasses, int[] categoryIdNumbers)
        {
          this.classNumber = categoryIdNumbers.length;//categoryIdNumbers are category Labels. Using 1,2,3...9
          this.categoryIdNumbers = categoryIdNumbers;
          this.referenceClasses = referenceClasses;
          this.setResultingClasses(resultingClasses);
                //int len = this.errormat.length;
                Vector tVe;
                Vector finVec = new Vector();
                this.calculateErrorMatrix();
                this.calculateAccuracy();
                this.kappaMeasurement();

                tVe= new Vector();
                tVe.add("");
                for (int i = 0; i< this.classNumber; i++)
                {
                  tVe.add((new Integer(this.categoryIdNumbers[i])).toString());
                }
                tVe.add("Totals");
                tVe.add("PA%");
                tVe.add("EO%");
                tVe.add("EC%");
                finVec.add(tVe);

                for (int i = 0; i< this.classNumber; i++)
                {
                        tVe = new Vector();
                        tVe.add((new Integer(this.categoryIdNumbers[i])).toString());
                        //tVe.add("NewImage/Reference");
                        for(int j =0;j<this.classNumber; j++)
                        {
                                tVe.add(new Integer(this.errorMatrix[i][j]).toString());
                        }

                        tVe.add(new Integer(this.producerTotal[i]).toString());
                        tVe.add(new Double(this.producerAccuracy[i]*100).toString());
                        tVe.add(new Double(this.errorsOfOmission[i]*100).toString());
                        tVe.add(new Double(this.errorsOfCommission[i]*100).toString());
                        finVec.add(tVe);
                }
                tVe = new Vector();
                        tVe.add("Totals");
                        //tVe.add((Object)new javax.swing.JLabel("Totals"));
                        //tVe.add(" ");
                for (int i =0; i<this.classNumber;i++)
                {
                    tVe.add(new Integer(this.userTotal[i]).toString());
                }
                tVe.add(new Integer(this.totalCorrect).toString());
                tVe.add("");
                tVe.add("");
                tVe.add("");
                finVec.add(tVe);
                tVe = new Vector();
                        tVe.add("CA% ");
                        //tVe.add(" ");
                for (int i = 0; i<this.classNumber; i++)
                {
                    tVe.add(new Double(this.userAccuracy[i]*100).toString());
                }
                tVe.add("");
                tVe.add("");
                tVe.add("Kappa");
                tVe.add(new Double(this.kappa).toString());
                finVec.add(tVe);
                return finVec;
        }

}
