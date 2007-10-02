package edu.psu.geovista.common.data;

/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class DataSetEvent
 Copyright (c), 2003, GeoVISTA Center
 All Rights Reserved.
 Original Author: Xiping Dai
 $Author: gary_liu $
 $Id: TrainingData.java,v 1.2 2005/02/04 04:54:23 gary_liu Exp $
 $Date: 2005/02/04 04:54:23 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */

import java.util.Vector;

public class TrainingData {

	Object[] dataObject;
	Object[] trainingData;
	String[] attributesDisplay;
	Vector[] trainingDataVector;
	Vector[] mergedTrainingDataVector;
        Vector[] classLabels;
	int classnumber;

	String[] trainingClassLabels;
        int[] trainingClassNumber;
	String[] trainingLabelsObs;
	String[] mergeClassLabels;
	String[] classLabelsAfterMerge;
	String mergedClassLabels; //i,j
	Vector mergedClassLabelsVector;

    public TrainingData() {
    }

	public void setDataObject(Object[] dataObject){
		this.dataObject = dataObject;
		this.setTrainingData(this.dataObject);
	}

	public void setNumberOfClasses (int numClass){
		this.classnumber = numClass;
	}

        public String[] getAttributeNames (){
                return this.attributesDisplay;
        }

	public String[] getTrainingClassInfo(){
		return this.trainingClassLabels;
	}

	public void setTrainingClassLabels (String[] classLabels){
		this.trainingClassLabels = classLabels;
	}

	public String[] getTrainingClassLabels (){
		return this.trainingClassLabels;
	}

        public int[] getTrainingClassNumber (){
                return this.trainingClassNumber;
        }


	public void setMergeClassLabels (String[] mergeClassLabels){
		this.mergeClassLabels = mergeClassLabels;
	}

	public String[] getClassLabelsAfterMerge (){
		return this.classLabelsAfterMerge;
	}

	public Vector[] getMergedTrainingDataVector(){
		return this.mergedTrainingDataVector;
	}

	public void setMegedClassLabels (String mergedClassLabels){
		this.mergedClassLabels = mergedClassLabels;
		mergedClassLabelsVector = new Vector();
		int end = 0;
		int begin = 0;
		String tmpLabel = new String();
		for (int i = 0; i < this.mergedClassLabels.length(); i ++){
			if ((this.mergedClassLabels.charAt(i))!= ','){
				continue;
			}
			end = i;
			tmpLabel = this.mergedClassLabels.substring(begin, end);
			mergedClassLabelsVector.add(tmpLabel);
			begin = end + 1;
		}
		tmpLabel = this.mergedClassLabels.substring(begin, this.mergedClassLabels.length());
		mergedClassLabelsVector.add(tmpLabel);
		mergedClassLabelsVector.trimToSize();

		if (mergedClassLabelsVector.size() <= 1){
			this.mergedTrainingDataVector = this.trainingDataVector;
			this.classLabelsAfterMerge = this.trainingClassLabels;
		}else {
			this.mergeClasses();
		}
	}

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
	  public void setTrainingData(Object[] data) {
		 this.setTrainingDataSet(new DataSetForApps(data));
	    
	  }
	
	private void setTrainingDataSet(DataSetForApps data){
		// remove string data
		DataSetForApps dataObjTransfer = data;
		this.trainingData = dataObjTransfer.getDataObjectOriginal();
		attributesDisplay = dataObjTransfer.getAttributeNamesOriginal();
		double[][] trainingDataArray = new double[dataObjTransfer.getNumObservations()][attributesDisplay.length-1];//last column for classificaiton info.
		// transfer data array to double array
		for (int j=0;j<dataObjTransfer.getNumberNumericAttributes()-1;j++){
			int t = 0;
			if (trainingData[j+1] instanceof double[]) t=0;
			else if (trainingData[j+1] instanceof int[]) t=1;
			else if (trainingData[j+1] instanceof boolean[]) t=2;
			for (int i=0;i<trainingDataArray.length;i++)
			{
				switch (t){
				case 0 :
					trainingDataArray[i][j]=((double[])trainingData[j+1])[i];
					break;
				case 1 :
					trainingDataArray[i][j]=(double)((int[])trainingData[j+1])[i];
					break;
				case 2 :
					trainingDataArray[i][j]=((boolean[])trainingData[j+1])[i]?1.0:0.0;
					break;
				}
			}
		}
		//get class label from training data
		this.trainingDataVector = new Vector[this.classnumber];
		for (int i = 0; i < this.classnumber; i ++){
			this.trainingDataVector[i] = new Vector();
		}

		//If there isn't input for trainingClassLabels, the default will be 0, 1, 2, 3, 4...
		if (this.trainingClassLabels == null){
			this.trainingClassLabels = new String[this.classnumber];
			for (int i = 0; i < this.classnumber; i ++){
				//this.trainingClassLabels[i] = new String();
				this.trainingClassLabels[i] = Integer.toString(i);
			}
		}

                if (this.trainingClassNumber == null){
                        this.trainingClassNumber = new int[this.classnumber];
                        for (int i = 0; i < this.classnumber; i ++){
                               this.trainingClassNumber[i] = i+1;//for Kioloa dataset
                        }
                }


		int[] trainingLabelInt = new int[dataObjTransfer.getNumObservations()];
		this.trainingLabelsObs = new String[dataObjTransfer.getNumObservations()];
		//trainingLabelsObs = (String[]) trainingData[dataObjTransfer.getNumberNumericAttributes()];//last column reserved for classificaiton info.
		trainingLabelInt = (int[]) trainingData[dataObjTransfer.getNumberNumericAttributes()];
		for(int i = 0; i < dataObjTransfer.getNumObservations(); i ++){
			trainingLabelsObs[i] = (new Integer(trainingLabelInt[i])).toString();
		}
		for (int i=0;i<trainingDataArray.length;i++){
			for (int j = 0; j < this.classnumber; j ++){
				if (trainingLabelsObs[i].equals(this.trainingClassLabels[j])){
					this.trainingDataVector[j].add(trainingDataArray[i]);
					continue;
				}
			}
		}
	}

	private void mergeClasses(){
		int newNum = this.classnumber - this.mergedClassLabelsVector.size() + 1;

		this.mergedTrainingDataVector = new Vector[newNum];
		for (int i = 0; i < newNum; i ++){
			this.mergedTrainingDataVector[i] = new Vector();
		}
		Vector mergedTrainingClasses = new Vector();
		this.classLabelsAfterMerge = new String[newNum];
		int position = -1;
		int m = 0;
		boolean foundClassMerged = false;
		for (int i = 0; i < this.trainingDataVector.length; i ++){
			for (int j = 0; j < this.mergedClassLabelsVector.size(); j ++){
				if (this.trainingClassLabels[i].equals(this.mergedClassLabelsVector.elementAt(j))){
					for (int n=0; n <this.trainingDataVector[i].size(); n ++){
						mergedTrainingClasses.add(this.trainingDataVector[i].elementAt(n));
					}
					//mergedTrainingClasses.add(this.trainingDataVector[i]);
					if (position == -1){
						position = i;
						this.mergedTrainingDataVector[m] = mergedTrainingClasses;
						this.classLabelsAfterMerge[m] = this.mergedClassLabels;
						m ++;
					}
					//this.trainingDataVector
					foundClassMerged = true;
					continue;
				}

			}
			if (foundClassMerged == false){
				this.mergedTrainingDataVector[m] = this.trainingDataVector[i];
				this.classLabelsAfterMerge[m] = this.trainingClassLabels[i];
				m ++;
			}
			foundClassMerged = false;
		}
	}
    public Vector[] getTrainingDataVector() {
        return trainingDataVector;
    }
    public void setTrainingDataVector(Vector[] trainingDataVector) {
        this.trainingDataVector = trainingDataVector;
    }

}
