package edu.psu.geovista.common.event;

import java.util.EventObject;

import edu.psu.geovista.common.color.BivariatePalette;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed 
 * @version 1.0
 */

public class BivariatePaletteEvent extends EventObject {

  private transient BivariatePalette bivariatePalette;
  /**
  * The constructor is the same as that for EventObject, except that the
  * pallet is indicated.
  */
  public BivariatePaletteEvent(Object source, BivariatePalette bivariatePalette) {
    super(source);
    this.bivariatePalette = bivariatePalette;
  }
  public BivariatePalette getPalette() {
    return bivariatePalette;
  }
  public void setPallet(BivariatePalette bivariatePalette) {
    this.bivariatePalette = bivariatePalette;
  }
}
