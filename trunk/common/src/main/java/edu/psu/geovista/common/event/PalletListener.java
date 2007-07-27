/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class PalleteListener
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: PalletListener.java,v 1.1 2003/05/16 15:52:54 hardisty Exp $
 $Date: 2003/05/16 15:52:54 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.common.event;

import java.util.EventListener;


public interface PalletListener extends EventListener {
  public void palletChanged(PalletEvent e);
}