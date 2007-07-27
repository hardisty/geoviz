package edu.psu.geovista.classification;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @Xiping Dai
 * @version 1.0
 */

import java.util.Vector;

public class MultiGaussianModel {

  private MultiGaussian[] multiGaussian;
  private int classNumber;
  private Vector[] trainingDataVector;

  public MultiGaussianModel() {
  }

  public void setClassNumber(int classNumber){
          this.classNumber = classNumber;
  }

//before setting training data, set class number.
  public void setTrainingData(Vector[] data){

    this.trainingDataVector = data;
    this.classNumber = this.trainingDataVector.length;

    this.multiGaussian = new MultiGaussian[this.classNumber];

    for (int i = 0; i< this.classNumber; i ++){
      multiGaussian[i] = new MultiGaussian();
      multiGaussian[i].setTrainingData(this.trainingDataVector[i]);
    }
  }

  public MultiGaussian[] getMultiGaussianModel(){
    return this.multiGaussian;
  }
}
