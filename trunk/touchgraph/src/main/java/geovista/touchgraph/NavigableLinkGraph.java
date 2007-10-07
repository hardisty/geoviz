/*
 *
 *
 * Copyright (c) 2002 Frank Hardisty. All rights reserved.
 *
 *
 */
package geovista.touchgraph;

import javax.swing.JPopupMenu;

import geovista.touchgraph.interaction.HVScroll;
import geovista.touchgraph.interaction.LocalityScroll;
import geovista.touchgraph.interaction.RotateScroll;


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
