/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the interface BoundaryClassifier
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: BoundaryClassifier.java,v 1.1 2003/04/25 18:18:05 hardisty Exp $
 $Date: 2003/04/25 18:18:05 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *

 */


package geovista.common.classification;


public interface BoundaryClassifier extends Classifier {


  /**
   * Return the boundaries for the requested number of classes.
   * It is assumed that the boundaries cover the whole data data range.
   * This should include both the start and end boundaries, so we pass back the
   * number of classes plus one.
   */

    public double[] getBoundaries(double[] data, int numClasses);

}
