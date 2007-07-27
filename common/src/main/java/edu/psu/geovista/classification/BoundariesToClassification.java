package edu.psu.geovista.classification;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Xiping Dai
 * @version 1.0
 */

public class BoundariesToClassification {

  public BoundariesToClassification() {
  }

  public int[] getClassification(double[] boundaries, double[] data){
    int[] classification;
    int len = data.length;
    classification = new int[len];

    for (int i = 0; i < len; i ++){
      for (int j = 0; j < boundaries.length-1; j ++){
        if (boundaries[j] <= data[i] && data[i] < boundaries[j+1]){
          classification[i] = j;
          //break;
        }
        if (data[i] == boundaries[boundaries.length - 1]){
          classification[i] = boundaries.length - 2;
          //break;
        }
      }
    }

    return classification;
  }

}
