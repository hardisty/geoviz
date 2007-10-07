/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class PalleteEvent
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: PalletEvent.java,v 1.1 2003/05/16 15:52:54 hardisty Exp $
 $Date: 2003/05/16 15:52:54 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  */

package geovista.common.event;

import java.util.EventObject;

import geovista.common.color.Pallet;

/**
 * An PalletEvent signals that there is a new or changed pallet.
 *
 * The recipient can then query the pallet to get colors.
 *
 */

public class PalletEvent extends EventObject {

  private transient Pallet pallet;
  /**
  * The constructor is the same as that for EventObject, except that the
  * pallet is indicated.
  */
  public PalletEvent(Object source, Pallet pallet) {
    super(source);
    this.pallet = pallet;
  }
  public Pallet getPallet() {
    return pallet;
  }
  public void setPallet(Pallet pallet) {
    this.pallet = pallet;
  }
}