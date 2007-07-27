/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class SelectionEvent
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: dxg231 $
 $Id: SelectionEvent.java,v 1.5 2004/05/20 17:12:06 dxg231 Exp $
 $Date: 2004/05/20 17:12:06 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.ui.event;

import java.awt.Color;
import java.util.EventObject;

/**
 * An SelectionEvent signals that a set of observations has been singled out.
 * This is often because the user has somehow indicated that these observations
 * are of interest
 *
 * The integers represents the indexes of that observation in the overall data set.
 *
 */
public class SelectionEvent
    extends EventObject {

  private transient int[] selection;
  private transient int[] higherLevelSelection;
  private transient Color[] multipleSlectionColors;
  private transient double[] selectedPortions;

  /**
   * The constructor is the same as that for EventObject, except that the
   * selection values are indicated.
   */
  public SelectionEvent(){
	  super(new Double(34d));
  }
  public SelectionEvent(Object source, int[] selection, int[] higherLevelSel, double[] portions) {
    super(source);
    this.selection = selection;
    this.higherLevelSelection = higherLevelSel;
    this.selectedPortions = portions;
  }

  public SelectionEvent(Object source, int[] selection, int[] higherLevelSel) {
    super(source);
    this.selection = selection;
    this.higherLevelSelection = higherLevelSel;
  }

  public SelectionEvent(Object source, int[] selection) {
    super(source);
    this.selection = selection;
  }

  public SelectionEvent(Object source, Color[] multipleSlectionColors) {
    super(source);
    this.multipleSlectionColors = multipleSlectionColors;
  }

  public int[] getHigherLevelSelection(){
    if (this.higherLevelSelection == null)
      return new int[0];
    return this.higherLevelSelection;
  }

  public double[] getSelectedPortions(){
    return this.selectedPortions;
  }

  public void setSelection(int[] selection){
	  this.selection = selection;
  }
  //begin accessors
  public int[] getSelection() {
    if (selection == null && multipleSlectionColors == null)
      return new int[0]; //jin: fix nullpointexception
    if (this.selection == null) {
      int selCount = 0;
      for (int i = 0; i < multipleSlectionColors.length; i++) {
        if (multipleSlectionColors[i] != null)
          selCount++;
      }
      selection = new int[selCount];
      selCount = 0;
      for (int i = 0; i < multipleSlectionColors.length; i++) {
        if (multipleSlectionColors[i] != null) {
          selection[selCount] = i;
          selCount++;
        }
      }
    }
    return this.selection;
  }

  public Color[] getMultipleSlectionColors() {
    return this.multipleSlectionColors;
  }
  //end accessors

}