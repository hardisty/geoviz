/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the interface DescribedClassifier
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: DescribedClassifier.java,v 1.2 2003/04/25 18:18:26 hardisty Exp $
 $Date: 2003/04/25 18:18:26 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *

 */


package geovista.common.classification;


public interface DescribedClassifier extends Classifier {
  /**
   * Returns descriptive names.
   */

    public String getFullName();
    public String getShortName();
}
