/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.event;

import java.util.EventObject;

import geovista.common.color.Palette;

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
