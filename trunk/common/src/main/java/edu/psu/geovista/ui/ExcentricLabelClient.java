/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ExcentricLabelClient
 Copyright (c), 2000, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: jmacgill $
 $Id: ExcentricLabelClient.java,v 1.1 2004/09/22 18:12:42 jmacgill Exp $
 $Date: 2004/09/22 18:12:42 $
 Reference:        Document no:
 ___                ___
 -------------------------------------------------------------------   */
package edu.psu.geovista.ui;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;


/**
 * Display excentric labels around items in a visualization.
 *
 * @author Jean-Daniel Fekete
 * @version $Revision: 1.1 $
 */
public interface ExcentricLabelClient {

  public int[] pickAll (Rectangle2D hitBox);

  public Shape getShapeAt(int id);

  public String getObservationLabel(int id);

  public void repaint();

}
