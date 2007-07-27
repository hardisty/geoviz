/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class PalleteEvent
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: xpdai $
 $Id: PaletteEvent.java,v 1.2 2004/06/03 21:29:37 xpdai Exp $
 $Date: 2004/06/03 21:29:37 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  */

package edu.psu.geovista.common.event;

import java.util.EventObject;

import edu.psu.geovista.common.color.Palette;



/**
 * An PalletEvent signals that there is a new or changed pallet.
 *
 * The recipient can then query the pallet to get colors.
 *
 */

public class PaletteEvent extends EventObject {

  private transient Palette pallet;
  /**
  * The constructor is the same as that for EventObject, except that the
  * pallet is indicated.
  */
  public PaletteEvent(Object source, Palette pallet) {
    super(source);
    this.pallet = pallet;
  }
  public Palette getPalette() {
    return pallet;
  }
  public void setPalette(Palette pallet) {
    this.pallet = pallet;
  }
}
