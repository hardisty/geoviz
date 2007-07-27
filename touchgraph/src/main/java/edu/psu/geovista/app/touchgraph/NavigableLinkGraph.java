/*
 *
 *
 * Copyright (c) 2002 Frank Hardisty. All rights reserved.
 *
 *
 */
package edu.psu.geovista.app.touchgraph;

import javax.swing.JPopupMenu;

import edu.psu.geovista.app.touchgraph.interaction.HVScroll;
import edu.psu.geovista.app.touchgraph.interaction.LocalityScroll;
import edu.psu.geovista.app.touchgraph.interaction.RotateScroll;


/** GLPanel contains code for adding scrollbars and interfaces to the TGPanel
  * The "GL" prefix indicates that this class is GraphLayout specific, and
  * will probably need to be rewritten for other applications.
  *
  * @author   Frank Hardisty
  * @version  1.20
  */
public interface NavigableLinkGraph {

  public TGPanel getTGPanel();
  public LocalityScroll getLocalityScroll();
  public RotateScroll getRotateScroll();
  public HVScroll getHVScroll();
  public JPopupMenu getGlPopup();


} // end com.touchgraph.graphlayout.NavigableLinkGraph
