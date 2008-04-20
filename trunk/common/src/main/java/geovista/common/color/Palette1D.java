/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.*/

package geovista.common.color;

import java.awt.Color;

/**
 * The <CODE>Palette1D</CODE> interface.
 * 
 * @author Chris Weaver
 */
public interface Palette1D extends OblivionPalette {

	int TYPE_SEQUENTIAL = 0;
	int TYPE_DIVERGING = 1;
	int TYPE_QUALITATIVE = 2;

	int getType();

	Color[] getColors(int length);
}
