/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class PalleteListener
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: jmacgill $
 $Id: PaletteListener.java,v 1.3 2004/10/14 17:27:56 jmacgill Exp $
 $Date: 2004/10/14 17:27:56 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package geovista.common.event;

import java.util.EventListener;


public interface PaletteListener extends EventListener {
  public void paletteChanged(PaletteEvent e);
}

